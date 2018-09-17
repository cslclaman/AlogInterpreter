package alog.control;

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
    private class ErroSintatico {
        Token token;
        String erro;

        public ErroSintatico(Token token, String erro) {
            this.token = token;
            this.erro = erro;
        }
        
        @Override
        public String toString(){
            return String.format(
                    "Linha %d, Coluna %d: %s",
                    token.getLinha() + 1, token.getColuna() + 1, erro);
        }
    }
    
    private List<Token> tokens;
    private Map<String, TipoVariavel> variaveis;
    
    private LinkedList<FuncaoToken> funcoesEsperadas;
    private LinkedList<ErroSintatico> erros;
    private TipoInstrucao tipoUltimaInstrucao;
    private int pos;
    private boolean fimAtingido;
    
    public Parser (List<Token> tokens){
        this.tokens = tokens;
        
        variaveis = new HashMap<>();
        erros = new LinkedList<>();
        funcoesEsperadas = new LinkedList<>();
        tipoUltimaInstrucao = TipoInstrucao._INDEFINIDO;
        
        pos = 0;
        fimAtingido = false;
        
        funcoesPorInstrucao(tipoUltimaInstrucao);
    }
    
    public boolean existeProxima(){
        return pos < tokens.size();
    }
    
    public Instrucao proxima(){
        Instrucao instrucao = null;
        
        if (!existeProxima()){
            return null;
        }
        
        Token token = tokens.get(pos);
        if (!funcaoValida(token)){
            return null;
        }
        
        if (instrucao != null){
            erros.add(new ErroSintatico(
                    token, "Instrução anterior não encerrada corretamente"));
            return instrucao;
        }
        
        switch(token.getFuncaoToken()){
            //Inicializa uma instrução tipo bloco
            case RES_BLOCO_INICIO:
                instrucao = instrucaoBloco();
                funcoesPorInstrucao(tipoUltimaInstrucao);
                break;

            // Ao finalizar o bloco
            case RES_BLOCO_FIM:
                instrucao = null;
                fimAtingido = true;
                funcoesEsperadas.clear();
                break;

            //Inicializa instrução de declaração de variáveis
            case RES_TIPO_CARACTER:
            case RES_TIPO_INTEIRO:
            case RES_TIPO_REAL:
                instrucao = instrucaoDeclaracaoVariaveis();
                funcoesPorInstrucao(tipoUltimaInstrucao);
                break;
        
            //Inicializa instrução de entrada de dados
            case LIB_IO_LEIA:
                instrucao = instrucaoEntradaDados();
                funcoesPorInstrucao(tipoUltimaInstrucao);
                break;
                
            //Inicializa instrução de saída de dados
            case LIB_IO_ESCREVA:
                instrucao = instrucaoSaidaDados();
                funcoesPorInstrucao(tipoUltimaInstrucao);
                break;
                
            //Define Chamada de Rotina ou Atribuição.
            case _INDEF_ALFABETICO:
            case _INDEF_ALFANUMERICO:
                if (variaveis.containsKey(token.getPalavra())){
                    //Inicializa instrução de atribuição
                    token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                    tokens.set(pos, token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    
                    instrucao = instrucaoAtribuicao();
                    funcoesPorInstrucao(tipoUltimaInstrucao);
                    break;
                } else {
                    erros.add(new ErroSintatico(token, "Essa variável não foi declarada"));
                    break;
                }
                
            //Define estrutura Condicional
            case RES_COND_SE:
                instrucao = instrucaoCondicional();
                funcoesPorInstrucao(tipoUltimaInstrucao);
                break;
        }
        
        return instrucao;
    }
    
    public LinkedList<Instrucao> listaExpressoes(){
        LinkedList<Instrucao> lista = new LinkedList<>();
        while (existeProxima()){
            lista.add(proxima());
        }
        return lista;
    }
    
    public int getNumErros(){
        return erros.size();
    }
    
    public String getStringErros(){
        StringBuilder msg = new StringBuilder();
        for (ErroSintatico err : erros){
            if (msg.length() > 0){
                msg.append("\n");
            }
            msg.append(err.toString());
        }
        return msg.toString();
    }
    
    public Token getTokenUltimoErro(){
        return erros.isEmpty() ? null : erros.get(erros.size() - 1).token;
    }
    
    public String getMsgUltimoErro(){
        return erros.isEmpty() ? "" : erros.get(erros.size() - 1).erro;
    }
    
    public List<Token> getTokensErros(){
        LinkedList<Token> errToken = new LinkedList<>();
        for (ErroSintatico err : erros){
            errToken.add(err.token);
        }
        return errToken;
    }
    
    private boolean funcaoValida(Token token){
        if (!funcoesEsperadas.contains(token.getFuncaoToken())){
            String msgErro = "Encontrou " + token.getFuncaoToken() + ", mas esperava:";
            for (FuncaoToken ft: funcoesEsperadas){
                msgErro += "\n - " + ft;
            }
            erros.add(new ErroSintatico(token, msgErro));
            return false;
        } else {
            return true;
        }
    }
        
    private void funcoesPorInstrucao(TipoInstrucao instrucao){
        funcoesEsperadas.clear();
        switch (instrucao) {
            // Indefinido, nesse contexto, é a primeira instrução do algoritmo.
            case _INDEFINIDO:
                // Caso modularizado:
                /* 
                // Espera por declaração de função
                funcoesEsperadas.add(FuncaoToken.RES_MOD_FUNCAO);
                // Espera por declaração de rotina
                funcoesEsperadas.add(FuncaoToken.RES_MOD_ROTINA);
                */

                // Caso não modularizado:
                funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                break;
                
            // Bloco de instruções.
            case BLOCO:
                //Bloco interno (espero que não seja usado...)
                funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                //Declaração de variáveis
                funcoesEsperadas.add(FuncaoToken.RES_TIPO_INTEIRO);
                funcoesEsperadas.add(FuncaoToken.RES_TIPO_REAL);
                funcoesEsperadas.add(FuncaoToken.RES_TIPO_CARACTER);
                //Entrada/Saída de dados
                funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                //Atribuição ou Chamada de rotina
                funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                //Condicional
                funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                //Repetições
                funcoesEsperadas.add(FuncaoToken.RES_REP_PARA);
                funcoesEsperadas.add(FuncaoToken.RES_REP_ENQUANTO);
                funcoesEsperadas.add(FuncaoToken.RES_REP_FACA);
                break;
            
            // Estrutura condicional
            case CONDICIONAL:
                //Bloco de instruções
                funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                
                //Instruções únicas
                //Entrada/Saída de dados
                funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                //Atribuição ou Chamada de rotina
                funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                //Condicional encadeada
                funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                //Repetições
                funcoesEsperadas.add(FuncaoToken.RES_REP_PARA);
                funcoesEsperadas.add(FuncaoToken.RES_REP_ENQUANTO);
                funcoesEsperadas.add(FuncaoToken.RES_REP_FACA);
                
            default:
                break;
        }
    }
    
    private Bloco instrucaoBloco(){
        Token token = tokens.get(pos++);
        Bloco bloco = new Bloco();
        bloco.setInicio(token);

        Parser parserInterno = new Parser(tokens);
        parserInterno.variaveis = variaveis;
        parserInterno.pos = pos;
        parserInterno.tipoUltimaInstrucao = bloco.getTipo();
        parserInterno.funcoesPorInstrucao(bloco.getTipo());

        while (parserInterno.existeProxima() && !parserInterno.fimAtingido){
            Instrucao proxima = parserInterno.proxima();
            if (!proxima.instrucaoValida()){
                erros.add(parserInterno.erros.getLast());
            } else {
                bloco.addInstrucao(proxima);
            }
        }

        if (!parserInterno.existeProxima() && !parserInterno.fimAtingido){
            erros.add(new ErroSintatico(
                    ((Bloco)bloco).getInicio(),
                    "Bloco não fechado corretamente"));
        }

        pos = parserInterno.pos;
        variaveis = parserInterno.variaveis;
        token = tokens.get(pos++);

        bloco.setFim(token);
        return bloco;
    }
    
    private DeclaracaoVariaveis instrucaoDeclaracaoVariaveis(){
        DeclaracaoVariaveis declaracaoVariaveis = new DeclaracaoVariaveis();
        
        boolean go = true;
        while (go && pos < tokens.size()){
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
                        erros.add(new ErroSintatico(token, "Identificador de variável não pode começar com número"));
                        go = false;
                    } else {
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        if (variaveis.containsKey(token.getPalavra())){
                            erros.add(new ErroSintatico(token, "Outra variável já foi declarada com esse nome"));
                            go = false;
                        } else {
                            declaracaoVariaveis.addNomeVariavel(token);
                            
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                        }
                    }
                    break;
                            
                case DELIM_PONTO_VIRGULA:
                    for (Variavel var : declaracaoVariaveis.getVariaveis() ){
                        variaveis.put(var.getNome(), var.getTipo());
                    }
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
        while (go && pos < tokens.size()){
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
                    if (variaveis.containsKey(token.getPalavra())){
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        entradaDados.addVariavel(token);
                        funcoesEsperadas.clear();
                        funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    } else {
                        erros.add(new ErroSintatico(token, "Essa variável não foi declarada"));
                        go = false;
                    }
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    entradaDados.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            
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
        while (go && pos < tokens.size()){
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
        while (go && pos < tokens.size()){
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
        while (go && pos < tokens.size()){
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
                    
                case RES_COND_ENTAO:
                    condicional.addToken(token);
                    funcoesPorInstrucao(condicional.getTipo());
                    
                    Parser parserInterno = new Parser(tokens);
                    parserInterno.variaveis = variaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = condicional.getTipo();
                    parserInterno.funcoesPorInstrucao(condicional.getTipo());
                    
                    Instrucao instrucaoInterna = parserInterno.proxima();
                    if (!instrucaoInterna.instrucaoValida()){
                        erros.add(parserInterno.erros.getLast());
                    } else {
                        condicional.setInstrucaoSe(instrucaoInterna);
                    }
                    
                    pos = parserInterno.pos;
                    variaveis = parserInterno.variaveis;
                    go = false;
                    funcoesEsperadas.clear();
                    break;
                    
                //case Expressão:?
                
            }
        }
        
        if (go && pos < tokens.size()){
            Token token = tokens.get(pos+1);
            if (token.getFuncaoToken() == FuncaoToken.RES_COND_SENAO){
                condicional.setTokenSenao(token);
                funcoesPorInstrucao(condicional.getTipo());

                Parser parserInterno = new Parser(tokens);
                parserInterno.variaveis = variaveis;
                parserInterno.pos = pos;
                parserInterno.tipoUltimaInstrucao = condicional.getTipo();
                parserInterno.funcoesPorInstrucao(condicional.getTipo());

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
