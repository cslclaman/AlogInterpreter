package alog.control;

import alog.analise.Erro;
import alog.analise.TipoErro;
import alog.expressao.ChamadaFuncao;
import alog.expressao.Expressao;
import alog.expressao.TokenDelimitador;
import alog.expressao.Operando;
import alog.expressao.Operacao;
import alog.instrucao.*;
import alog.token.FuncaoToken;
import alog.instrucao.TipoInstrucao;
import alog.model.*;
import alog.token.Token;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Analisador sintático que verifica uma sequência de tokens e retorna expressões executáveis.
 * @author Caique
 */
public class Parser {
    private final List<Token> tokens;
    
    private Map<String, TipoVariavel> declVariaveis;
    
    private LinkedList<FuncaoToken> funcoesEsperadas;
    private LinkedList<Erro> erros;
    private TipoInstrucao tipoUltimaInstrucao;
    private int pos;
    private int size;
    private boolean fimAtingido;
    
    public Parser (List<Token> tokens){
        this.tokens = tokens;
        this.size = tokens.size();
        declVariaveis = new HashMap<>();
        erros = new LinkedList<>();
        funcoesEsperadas = new LinkedList<>();
        tipoUltimaInstrucao = TipoInstrucao._INDEFINIDO;
        
        pos = 0;
        fimAtingido = false;
        
        funcoesEsperadas = funcoesAlgoritmo();
    }
    
    public boolean existeProxima(){
        return pos < size;
    }
    
    public Instrucao proxima(){
        Instrucao instrucao = new InstrucaoInvalida();
        
        if (!existeProxima()){
            if (size <= 0){
                erros.add(new Erro(TipoErro.ERRO, ' ', 0, 0, 0,
                        "Nenhum token encontrado"));
            } else {
                erros.add(new Erro(TipoErro.ALERTA, tokens.get(size - 1),
                        "Fim do programa atingido"));
            }
            return instrucao;
        } 
        
        Token token = tokens.get(pos);
        if (!funcaoValida(token)){
            pos++;
            instrucao.addToken(token);
            return instrucao;
        }
        
        switch(token.getFuncaoToken()){
            //Inicializa uma instrução tipo bloco
            case RES_BLOCO_INICIO:
                instrucao = instrucaoBloco();
                
                switch (tipoUltimaInstrucao){
                    case _INDEFINIDO:
                        //funcoesEsperadas = funcoesAlgoritmo();
                        break;
                    case BLOCO:
                        funcoesEsperadas.clear();
                        funcoesEsperadas = funcoesBloco();
                        break;
                }
                break;

            // Ao finalizar o bloco
            case RES_BLOCO_FIM:
                instrucao = new FimBloco();
                instrucao.addToken(token);
                fimAtingido = true;
                break;

            //Inicializa instrução de declaração de variáveis
            case RES_TIPO_CARACTER:
            case RES_TIPO_INTEIRO:
            case RES_TIPO_REAL:
                instrucao = instrucaoDeclaracaoVariaveis();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
        
            //Inicializa instrução de entrada de dados
            case LIB_IO_LEIA:
                instrucao = instrucaoEntradaDados();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Inicializa instrução de saída de dados
            case LIB_IO_ESCREVA:
                instrucao = instrucaoSaidaDados();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Define Chamada de Rotina ou Atribuição.
            case _INDEF_ALFABETICO:
            case _INDEF_ALFANUMERICO:
                if (declVariaveis.containsKey(token.getPalavra())){
                    //Inicializa instrução de atribuição
                    token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                    tokens.set(pos, token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    
                    instrucao = instrucaoAtribuicao();
                    funcoesEsperadas.clear();
                    funcoesEsperadas = funcoesBloco();
                } else {
                    erros.add(new Erro(TipoErro.ERRO, token, 
                            String.format("Variável, função ou rotina \"%s\" não declarada", token.getPalavra())));
                    
                    pos++;
                    instrucao.addToken(token);
                }
                break;
                
            //Define estrutura Condicional
            case RES_COND_SE:
                instrucao = instrucaoCondicional();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
        }
        
        return instrucao;
    }
    
    public LinkedList<Instrucao> listaInstrucoes(){
        LinkedList<Instrucao> lista = new LinkedList<>();
        while (existeProxima()){
            lista.add(proxima());
        }
        return lista;
    }
    
    public int getNumErros(){
        return erros.size();
    }
    
    public String imprimeErros(){
        StringBuilder msg = new StringBuilder();
        for (Erro err : erros){
            if (msg.length() > 0){
                msg.append("\n");
            }
            msg.append(err.toString());
        }
        return msg.toString();
    }
    
    public Token getTokenUltimoErro(){
        return erros.isEmpty() ? null : erros.get(erros.size() - 1).getToken();
    }
    
    public String getMsgUltimoErro(){
        return erros.isEmpty() ? "" : erros.get(erros.size() - 1).getErro();
    }
    
    public List<Token> getTokensErros(){
        LinkedList<Token> errToken = new LinkedList<>();
        for (Erro err : erros){
            errToken.add(err.getToken());
        }
        return errToken;
    }
    
    public List<String> getListaErros(){
        LinkedList<String> err = new LinkedList<>();
        for (Erro e : erros){
            err.add(e.toString());
        }
        return err;
    }
    
    private boolean funcaoValida(Token token){
        if (!funcoesEsperadas.contains(token.getFuncaoToken())){
            String msgErro = "Encontrou " + token.getFuncaoToken() + ", mas esperava:";
            for (FuncaoToken ft: funcoesEsperadas){
                msgErro += "\n - " + ft;
            }
            erros.add(new Erro(TipoErro.ERRO, token, msgErro));
            return false;
        } else {
            return true;
        }
    }
        
    private Bloco instrucaoBloco(){
        Token token = tokens.get(pos++);
        Bloco bloco = new Bloco();
        bloco.setInicio(token);

        Parser parserInterno = new Parser(tokens);
        parserInterno.declVariaveis = declVariaveis;
        parserInterno.pos = pos;
        parserInterno.tipoUltimaInstrucao = bloco.getTipo();
        parserInterno.funcoesEsperadas = funcoesBloco();

        while (parserInterno.existeProxima() && !parserInterno.fimAtingido){
            Instrucao proxima = parserInterno.proxima();
            if (!(proxima instanceof FimBloco)){
                bloco.addInstrucao(proxima);
            }
        }

        for (Erro erro : parserInterno.erros){
            erros.add(erro);
        }
        
        pos = parserInterno.pos;
        declVariaveis = parserInterno.declVariaveis;

        if (!existeProxima() && !parserInterno.fimAtingido){
            erros.add(new Erro(
                    TipoErro.ERRO, 
                    bloco.getInicio(),
                    "Bloco iniciado não foi fechado corretamente"));
        } else {
            token = tokens.get(pos++);
            if (token.getFuncaoToken() == FuncaoToken.RES_BLOCO_FIM){
                bloco.setFim(token);
            } 
        }
        return bloco;
    }
    
    private DeclaracaoVariaveis instrucaoDeclaracaoVariaveis(){
        DeclaracaoVariaveis declaracaoVariaveis = new DeclaracaoVariaveis();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                declaracaoVariaveis.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case RES_TIPO_CARACTER:
                case RES_TIPO_INTEIRO:
                case RES_TIPO_REAL:
                //case RES_TIPO_LOGICO:
                    declaracaoVariaveis.setTipoVariavel(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_DOIS_PONTOS);
                    break;
                
                case DELIM_DOIS_PONTOS:
                case DELIM_VIRGULA:
                    declaracaoVariaveis.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    break;
                            
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    char inicial = token.getPalavra().charAt(0);
                    if (Character.isDigit(inicial)){
                        declaracaoVariaveis.addToken(token);
                        erros.add(new Erro(TipoErro.ERRO, token,
                                "Identificador de variável não pode começar com número"));
                        declaracaoVariaveis.invalidaInstrucao();
                    } else {
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        if (declVariaveis.containsKey(token.getPalavra())){
                            declaracaoVariaveis.addToken(token);
                            erros.add(new Erro(TipoErro.ERRO, token,
                                    String.format("Variável \"%s\" já declarada", token.getPalavra())));
                            declaracaoVariaveis.invalidaInstrucao();
                        } else {
                            declaracaoVariaveis.addNomeVariavel(token);
                            declVariaveis.put(token.getPalavra(), declaracaoVariaveis.getTipoVariavel());
                        }
                    }
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                    break;
                            
                case DELIM_PONTO_VIRGULA:
                    declaracaoVariaveis.addToken(token);
                    go = false;
                    break;
            }
                    
        }
        
        return declaracaoVariaveis;
    }
    
    private EntradaDados instrucaoEntradaDados(){
        EntradaDados entradaDados = new EntradaDados();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                entradaDados.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case LIB_IO_LEIA:
                    entradaDados.setTokenNome(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                case DELIM_VIRGULA:
                    entradaDados.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    break;
                            
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    if (declVariaveis.containsKey(token.getPalavra())){
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        entradaDados.addVariavel(token);
                    } else {
                        entradaDados.addToken(token);
                        erros.add(new Erro(TipoErro.ERRO, token, 
                        String.format("Variável \"%s\" não declarada", token.getPalavra())));
                        entradaDados.invalidaInstrucao();
                    }
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    entradaDados.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                    break;
                            
                case DELIM_PONTO_VIRGULA:
                    entradaDados.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
            }
                    
        }
        
        return entradaDados;
    }
    
    private SaidaDados instrucaoSaidaDados(){
        SaidaDados saidaDados = new SaidaDados();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                saidaDados.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case LIB_IO_ESCREVA:
                    saidaDados.setTokenNome(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                case DELIM_VIRGULA:
                    saidaDados.addToken(token);
                    funcoesEsperadas.clear();
                    saidaDados.addParametro(instrucaoExpressao(
                            FuncaoToken.DELIM_VIRGULA, FuncaoToken.DELIM_PARENTESES_FECHA));
                    break;
                            
                case DELIM_PARENTESES_FECHA:
                    saidaDados.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                    break;
                            
                case DELIM_PONTO_VIRGULA:
                    saidaDados.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
            }
                    
        }
        
        return saidaDados;
    }
    
    private Atribuicao instrucaoAtribuicao(){
        Atribuicao atribuicao = new Atribuicao();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                atribuicao.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case IDENT_NOME_VARIAVEL:
                    atribuicao.setVariavel(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.OP_ATRIBUICAO);
                    break;
                    
                case OP_ATRIBUICAO:
                    atribuicao.addToken(token);
                    funcoesEsperadas.clear();
                    atribuicao.setExpressao(instrucaoExpressao(FuncaoToken.DELIM_PONTO_VIRGULA));
                    break;
                    
                case DELIM_PONTO_VIRGULA:
                    atribuicao.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
            }
        }
        return atribuicao;
    }
    
    private Condicional instrucaoCondicional(){
        Condicional condicional = new Condicional();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                condicional.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case RES_COND_SE:
                    condicional.setTokenSe(token);
                    funcoesEsperadas.clear();
                    condicional.setExpressao(instrucaoExpressao(FuncaoToken.RES_COND_ENTAO));
                    break;
                    
                case RES_COND_ENTAO:
                    condicional.addToken(token);
                    funcoesEsperadas.clear();
                    
                    Parser parserInterno = new Parser(tokens);
                    parserInterno.declVariaveis = declVariaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = condicional.getTipo();
                    parserInterno.funcoesEsperadas = funcoesEstrutura();
                    
                    Instrucao instrucaoInterna = parserInterno.proxima();
                    if (!instrucaoInterna.instrucaoValida()){
                        erros.add(parserInterno.erros.getLast());
                    } else {
                        condicional.setInstrucaoSe(instrucaoInterna);
                    }
                    
                    pos = parserInterno.pos;
                    declVariaveis = parserInterno.declVariaveis;
                    go = false;
                    break;
            }
        }
        
        if (existeProxima()){
            Token token = tokens.get(pos);
            if (token.getFuncaoToken() == FuncaoToken.RES_COND_SENAO){
                pos++;
                condicional.setTokenSenao(token);
                
                Parser parserInterno = new Parser(tokens);
                parserInterno.declVariaveis = declVariaveis;
                parserInterno.pos = pos;
                parserInterno.tipoUltimaInstrucao = condicional.getTipo();
                parserInterno.funcoesEsperadas = funcoesEstrutura();

                Instrucao instrucaoInterna = parserInterno.proxima();
                if (!instrucaoInterna.instrucaoValida()){
                    erros.add(parserInterno.erros.getLast());
                } else {
                    condicional.setInstrucaoSenao(instrucaoInterna);
                }
                
                pos = parserInterno.pos;
                declVariaveis = parserInterno.declVariaveis;
            }
        }
        
        return condicional;
    }
    
    private Expressao instrucaoExpressao(FuncaoToken... condicoesParada) {
        boolean expressaoValida = true;
        Token token;
        Token topo;
        LinkedList<Token> pilhaTokens = new LinkedList<>();
        LinkedList<Token> saida = new LinkedList<>();
        HashMap<Token, ChamadaFuncao> mapaFuncoes = new HashMap<>();
        
        int balancParenteses = 0;
        funcoesEsperadas.clear();
        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
        funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
        funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
        funcoesEsperadas.add(FuncaoToken.CONST_INTEIRA);
        funcoesEsperadas.add(FuncaoToken.CONST_REAL);
        //funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
        funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
        funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
        funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    
        while (existeProxima()){
            token = tokens.get(pos++);
            if (!funcaoValida(token)) {
                expressaoValida = false;
                break;
            }
            boolean parada = false;
            for (FuncaoToken condParada : condicoesParada) {
                if (condParada == token.getFuncaoToken()) {
                    if (condParada == FuncaoToken.DELIM_PARENTESES_FECHA &&
                            balancParenteses > 0) {
                        break;
                    }
                    parada = true;
                    break;
                }
            }
            if (parada) {
                pos--;
                break;
            }
            switch (token.getFuncaoToken()) {
                case DELIM_PARENTESES_ABRE:
                    saida.add(token);
                    pilhaTokens.push(token);
                    balancParenteses ++;
                    break;
                    
                case CONST_CARACTER:
                    saida.add(token);
                    funcoesEsperadas.clear();
                    for (FuncaoToken condicaoParada : condicoesParada) {
                        funcoesEsperadas.add(condicaoParada);
                    }
                    if (balancParenteses > 0) {
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    }
                    funcoesEsperadas.addAll(funcoesExpressaoRelacional());
                    funcoesEsperadas.addAll(funcoesExpressaoLogica());
                    break;
                    
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    if (declVariaveis.containsKey(token.getPalavra())){
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        saida.add(token);
                    } else {
                        erros.add(new Erro(TipoErro.ERRO, token, 
                        String.format("Variável \"%s\" não declarada", token.getPalavra())));
                        expressaoValida = false;
                    }
                    funcoesEsperadas.clear();
                    for (FuncaoToken condicaoParada : condicoesParada) {
                        funcoesEsperadas.add(condicaoParada);
                    }
                    if (balancParenteses > 0) {
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    }
                    funcoesEsperadas.addAll(funcoesExpressaoAritmetica());
                    funcoesEsperadas.addAll(funcoesExpressaoRelacional());
                    funcoesEsperadas.addAll(funcoesExpressaoLogica());
                    break;
                    
                case CONST_INTEIRA:
                case CONST_REAL:
                    saida.add(token);
                    funcoesEsperadas.clear();
                    for (FuncaoToken condicaoParada : condicoesParada) {
                        funcoesEsperadas.add(condicaoParada);
                    }
                    if (balancParenteses > 0) {
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    }
                    funcoesEsperadas.addAll(funcoesExpressaoAritmetica());
                    funcoesEsperadas.addAll(funcoesExpressaoRelacional());
                    funcoesEsperadas.addAll(funcoesExpressaoLogica());
                    break;
                    
                case LIB_MATH_POT:
                case LIB_MATH_RAIZ:
                    pos--;
                    ChamadaFuncao chamadaFuncao = expressaoChamadaFuncao();
                    mapaFuncoes.put(chamadaFuncao.getTokenNome(), chamadaFuncao);
                    
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            pilhaTokens.push(topo);
                            break;
                        } else {
                            if (topo.getPrecedencia() < token.getPrecedencia()) {
                                pilhaTokens.push(topo);
                                break;
                            } else {
                                saida.add(topo);
                            }
                        }
                    }
                    pilhaTokens.push(token);
                    
                    for (FuncaoToken condicaoParada : condicoesParada) {
                        funcoesEsperadas.add(condicaoParada);
                    }
                    if (balancParenteses > 0) {
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    }
                    funcoesEsperadas.addAll(funcoesExpressaoAritmetica());
                    funcoesEsperadas.addAll(funcoesExpressaoRelacional());
                    funcoesEsperadas.addAll(funcoesExpressaoLogica());
                    
                    break;
                    
                case OP_MAT_SOMA:
                case OP_MAT_SUBTRACAO:
                case OP_MAT_MULTIPLICACAO:
                case OP_MAT_DIV_REAL:
                case OP_MAT_DIV_INTEIRA:
                case OP_MAT_MOD:
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            pilhaTokens.push(topo);
                            break;
                        } else {
                            if (topo.getPrecedencia() < token.getPrecedencia()) {
                                pilhaTokens.push(topo);
                                break;
                            } else {
                                saida.add(topo);
                            }
                        }
                    }
                    pilhaTokens.push(token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_INTEIRA);
                    funcoesEsperadas.add(FuncaoToken.CONST_REAL);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    break;
                    
                case OP_REL_MAIOR:
                case OP_REL_MAIOR_IGUAL:
                case OP_REL_MENOR:
                case OP_REL_MENOR_IGUAL:
                case OP_REL_IGUAL:
                case OP_REL_DIFERENTE:
                case OP_LOG_E:
                case OP_LOG_OU:
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            pilhaTokens.push(topo);
                            break;
                        } else {
                            if (topo.getPrecedencia() < token.getPrecedencia()) {
                                pilhaTokens.push(topo);
                                break;
                            } else {
                                saida.add(topo);
                            }
                        }
                    }
                    pilhaTokens.push(token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.CONST_INTEIRA);
                    funcoesEsperadas.add(FuncaoToken.CONST_REAL);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    topo = null;
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            break;
                        } else {
                            saida.add(topo);
                            topo = null;
                        }
                    }
                    
                    if (topo == null) {
                        erros.add(new Erro(TipoErro.ERRO, token, 
                        "Parêntese de abertura não encontrado"));
                        expressaoValida = false;
                    } else {
                        saida.add(token);
                    }
                    balancParenteses --;
                    break;
            }
        }
        
        while (!pilhaTokens.isEmpty()) {
            token = pilhaTokens.pop();
            if (token.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                erros.add(new Erro(TipoErro.ERRO, token, 
                "Parêntese de fechamento não encontrado"));
                expressaoValida = false;
            } else {
                saida.add(token);
            }
        }
        
        Expressao expressao = null;
        LinkedList<Expressao> pilhaExpressoes = new LinkedList<>();
        
        for (Token t : saida) {
            switch (t.getFuncaoToken()){
                case CONST_CARACTER:
                case CONST_INTEIRA:
                case CONST_REAL:
                case IDENT_NOME_VARIAVEL:
                    Operando operando = new Operando();
                    operando.setOperando(t);
                    pilhaExpressoes.push(operando);
                    break;
                
                case LIB_MATH_POT:
                case LIB_MATH_RAIZ:
                    ChamadaFuncao chamadaFuncao = mapaFuncoes.get(t);
                    pilhaExpressoes.push(chamadaFuncao);
                    break;
                    
                case OP_MAT_SOMA:
                case OP_MAT_SUBTRACAO:
                case OP_MAT_MULTIPLICACAO:
                case OP_MAT_DIV_REAL:
                case OP_MAT_DIV_INTEIRA:
                case OP_MAT_MOD:
                case OP_REL_MAIOR:
                case OP_REL_MAIOR_IGUAL:
                case OP_REL_MENOR:
                case OP_REL_MENOR_IGUAL:
                case OP_REL_IGUAL:
                case OP_REL_DIFERENTE:
                case OP_LOG_E:
                case OP_LOG_OU:
                    Operacao subExpressao = new Operacao();
                    subExpressao.setOperador(t);
                    try {
                        subExpressao.setExpressaoDir(pilhaExpressoes.pop());
                        subExpressao.setExpressaoEsq(pilhaExpressoes.pop());
                    } catch (NoSuchElementException ex){
                        erros.add(new Erro(TipoErro.ERRO, t, 
                            "Erro ao montar expressão"));
                        expressaoValida = false;
                        subExpressao.addToken(t);
                    }
                    pilhaExpressoes.push(subExpressao);
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                    TokenDelimitador tokenDelimitador = new TokenDelimitador();
                    tokenDelimitador.setDelimitador(t);
                    pilhaExpressoes.push(tokenDelimitador);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    try {
                        Expressao parenteseada = pilhaExpressoes.pop();
                        TokenDelimitador abrePar = (TokenDelimitador)pilhaExpressoes.pop();
                        parenteseada.setParentesesAbre(abrePar.getDelimitador());
                        parenteseada.setParentesesFecha(t);
                        pilhaExpressoes.push(parenteseada);
                    } catch (NoSuchElementException | ClassCastException ex){
                        erros.add(new Erro(TipoErro.ALERTA, t, 
                            "Alerta ao montar expressão"));
                    }
                    break;
            }
        }

        try {
            expressao = pilhaExpressoes.pop();
        } catch (NoSuchElementException ex){
            erros.add(new Erro(TipoErro.ERRO, saida.getFirst(), 
                "Erro ao finalizar expressão"));
            expressaoValida = false;
            expressao = new Operando();
        }
        
        if (!expressaoValida) {
            expressao.invalidaInstrucao();
        }
        
        funcoesEsperadas.clear();
        for (FuncaoToken condicaoParada : condicoesParada) {
            funcoesEsperadas.add(condicaoParada);
        }
        
        return expressao;
    }
    
    private ChamadaFuncao expressaoChamadaFuncao(){
        ChamadaFuncao chamadaFuncao = new ChamadaFuncao();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                chamadaFuncao.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case LIB_MATH_POT:
                case LIB_MATH_RAIZ:
                //case IDENT_NOME_FUNCAO:
                    chamadaFuncao.setTokenNome(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                case DELIM_VIRGULA:
                    chamadaFuncao.addToken(token);
                    funcoesEsperadas.clear();
                    chamadaFuncao.addParametro(instrucaoExpressao(
                            FuncaoToken.DELIM_VIRGULA, FuncaoToken.DELIM_PARENTESES_FECHA));
                    break;
                            
                case DELIM_PARENTESES_FECHA:
                    chamadaFuncao.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
            }
        }
        
        return chamadaFuncao;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funções esperadas no início de um algoritmo">
    private static LinkedList<FuncaoToken> funcoesAlgoritmo(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        /*
        // Espera por declaração de função
        funcoes.add(FuncaoToken.RES_MOD_FUNCAO);
        // Espera por declaração de rotina
        funcoes.add(FuncaoToken.RES_MOD_ROTINA);
        // Espera por declaração de nome de algoritmo
        funcoes.add(FuncaoToken.RES_ALGORITMO);
        */
        // Espera por início de bloco caso não modularizado
        funcoes.add(FuncaoToken.RES_BLOCO_INICIO);
        return funcoes;
    }
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="Funções esperadas após início de bloco">
    private static LinkedList<FuncaoToken> funcoesBloco(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        //Bloco interno (espero que não seja usado...)
        funcoes.add(FuncaoToken.RES_BLOCO_INICIO);
        //Declaração de variáveis
        funcoes.add(FuncaoToken.RES_TIPO_INTEIRO);
        funcoes.add(FuncaoToken.RES_TIPO_REAL);
        funcoes.add(FuncaoToken.RES_TIPO_CARACTER);
        //Entrada/Saída de dados
        funcoes.add(FuncaoToken.LIB_IO_LEIA);
        funcoes.add(FuncaoToken.LIB_IO_ESCREVA);
        //Atribuição ou Chamada de rotina
        funcoes.add(FuncaoToken._INDEF_ALFABETICO);
        funcoes.add(FuncaoToken._INDEF_ALFANUMERICO);
        //Condicional
        funcoes.add(FuncaoToken.RES_COND_SE);
        //Repetições
        funcoes.add(FuncaoToken.RES_REP_PARA);
        funcoes.add(FuncaoToken.RES_REP_ENQUANTO);
        funcoes.add(FuncaoToken.RES_REP_FACA);
        //Fechamento do bloco
        funcoes.add(FuncaoToken.RES_BLOCO_FIM);
        return funcoes;
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Funções esperadas dentro de uma instrução condicional ou repetitiva">
    private static LinkedList<FuncaoToken> funcoesEstrutura(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        //Bloco de instruções
        funcoes.add(FuncaoToken.RES_BLOCO_INICIO);

        //Instruções únicas
        //Entrada/Saída de dados
        funcoes.add(FuncaoToken.LIB_IO_LEIA);
        funcoes.add(FuncaoToken.LIB_IO_ESCREVA);
        //Atribuição ou Chamada de rotina
        funcoes.add(FuncaoToken._INDEF_ALFABETICO);
        funcoes.add(FuncaoToken._INDEF_ALFANUMERICO);
        //Condicional encadeada
        funcoes.add(FuncaoToken.RES_COND_SE);
        //Repetições
        funcoes.add(FuncaoToken.RES_REP_PARA);
        funcoes.add(FuncaoToken.RES_REP_ENQUANTO);
        funcoes.add(FuncaoToken.RES_REP_FACA);
        return funcoes;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Operadores aritméticos">
    private static LinkedList<FuncaoToken> funcoesExpressaoAritmetica(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        funcoes.add(FuncaoToken.OP_MAT_SOMA);
        funcoes.add(FuncaoToken.OP_MAT_SUBTRACAO);
        funcoes.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
        funcoes.add(FuncaoToken.OP_MAT_DIV_REAL);
        funcoes.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
        funcoes.add(FuncaoToken.OP_MAT_MOD);
        return funcoes;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Operadores relacionais">
    private static LinkedList<FuncaoToken> funcoesExpressaoRelacional(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        funcoes.add(FuncaoToken.OP_REL_MAIOR);
        funcoes.add(FuncaoToken.OP_REL_MAIOR_IGUAL);
        funcoes.add(FuncaoToken.OP_REL_MENOR);
        funcoes.add(FuncaoToken.OP_REL_MENOR_IGUAL);
        funcoes.add(FuncaoToken.OP_REL_IGUAL);
        funcoes.add(FuncaoToken.OP_REL_DIFERENTE);
        return funcoes;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Operadores lógicos">
    private static LinkedList<FuncaoToken> funcoesExpressaoLogica(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        funcoes.add(FuncaoToken.OP_LOG_E);
        funcoes.add(FuncaoToken.OP_LOG_OU);
        return funcoes;
    }
    // </editor-fold>
}
