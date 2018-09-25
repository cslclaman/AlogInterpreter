package alog.control;

import alog.analise.Erro;
import alog.analise.TipoErro;
import alog.expressao.Expressao;
import alog.instrucao.*;
import alog.token.FuncaoToken;
import alog.instrucao.TipoInstrucao;
import alog.model.*;
import alog.token.Token;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        
    private LinkedList<FuncaoToken> funcoesAlgoritmo(){
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
    
    private LinkedList<FuncaoToken> funcoesBloco(){
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
    
    private LinkedList<FuncaoToken> funcoesCondicional(){
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
    
    private LinkedList<FuncaoToken> funcoesRepeticao(){
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
                    
                    //deve esperar expressão
                    
                    break;
                            
                case DELIM_PARENTESES_FECHA:
                    saidaDados.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            
                case DELIM_PONTO_VIRGULA:
                    saidaDados.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
                    
                //case Expressão:?
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
                    
                    //deve esperar expressão
                    
                    break;
                    
                //case Expressão:?
                    
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
                break;
            }
            
            switch (token.getFuncaoToken()){
                case RES_COND_SE:
                
                    condicional.setTokenSe(token);
                    funcoesEsperadas.clear();
                    
                    // deve esperar expressão
                    
                    break;
                    
                //case Expressão:?
                    
                case RES_COND_ENTAO:
                    condicional.addToken(token);
                    funcoesEsperadas.clear();
                    
                    Parser parserInterno = new Parser(tokens);
                    parserInterno.declVariaveis = declVariaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = condicional.getTipo();
                    parserInterno.funcoesEsperadas = funcoesCondicional();
                    
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
            Token token = tokens.get(pos + 1);
            if (token.getFuncaoToken() == FuncaoToken.RES_COND_SENAO){
                condicional.setTokenSenao(token);
                
                Parser parserInterno = new Parser(tokens);
                parserInterno.declVariaveis = declVariaveis;
                parserInterno.pos = pos;
                parserInterno.tipoUltimaInstrucao = condicional.getTipo();
                parserInterno.funcoesEsperadas = funcoesCondicional();

                Instrucao instrucaoInterna = parserInterno.proxima();
                if (!instrucaoInterna.instrucaoValida()){
                    erros.add(parserInterno.erros.getLast());
                } else {
                    condicional.setInstrucaoSenao(instrucaoInterna);
                }
            }
        }
        
        return condicional;
    }
    
}
