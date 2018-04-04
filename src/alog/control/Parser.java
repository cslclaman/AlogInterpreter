/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Expressao;
import alog.model.FuncaoToken;
import alog.model.TipoExpressao;
import alog.model.Token;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Analisador sintático que verifica uma sequência de tokens e retorna expressões executáveis.
 * @author Caique
 */
public class Parser {
    private ArrayList<Token> tokens;
    private ArrayList<String> variaveis;
    
    private ArrayList<FuncaoToken> funcoesEsperadas;
    
    private int pos;
    private String erro;
    
    public Parser (String texto){
        Scanner scanner = new Scanner(texto);
        tokens = scanner.getAll();
        
        variaveis = new ArrayList<>();
        funcoesEsperadas = new ArrayList<>();
        funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
        
        pos = 0;
    }
    
    public boolean hasNext(){
        return pos < tokens.size();
    }
    
    public Expressao parseExpression(){
        Expressao expr = new Expressao();
        
        int balancParenteses = 0;
        
        boolean go = true;
        boolean add = false;
        erro = "";
        
        while (go && pos < tokens.size()){
            Token token = tokens.get(pos++);
            if (expr.getLinha() == 0){
                expr.setLinha(token.getLinha() + 1);
            }
            
            if (!estadoValido(token)){
                break;
            }
            switch(token.getFuncaoToken()){
                //MODO: INÍCIO/FIM DE BLOCO
                case RES_BLOCO_INICIO:
                    expr.setTipo(TipoExpressao.DELIM_BLOCO);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_INTEIRO);
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_REAL);
                    
                    add = true;
                    go = false;
                    break;
                case RES_BLOCO_FIM:
                    expr.setTipo(TipoExpressao.DELIM_BLOCO);
                    
                    funcoesEsperadas.clear();
                    
                    add = true;
                    go = false;
                    break;
                    
                //MODO: CRIAÇÃO DE VARIÁVEIS
                case IDENT_TIPO_CARACTER:
                case IDENT_TIPO_INTEIRO:
                case IDENT_TIPO_REAL:
                    expr.setTipo(TipoExpressao.CRIACAO_VARIAVEL);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_DOIS_PONTOS);
                    add = true;
                    break;
                    
                case DELIM_DOIS_PONTOS:
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    add = false;
                    break;
                    
                
                
                //MODO: ENTRADA DE DADOS
                case LIB_IO_LEIA:
                    expr.setTipo(TipoExpressao.ENTRADA_DE_DADOS);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    add = true;
                    break;
                    
                //MODO: SAÍDA DE DADOS
                case LIB_IO_ESCREVA:
                    expr.setTipo(TipoExpressao.SAIDA_DE_DADOS);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    add = true;
                    break;
                
                //MODO: ATRIBUIÇÃO
                case OP_ATRIBUICAO:
                    expr.setTipo(TipoExpressao.OPERACAO_ATRIBUICAO);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    add = true;
                    break;
                    
                //MODO: OPERAÇÃO ARITMÉTICA
                case OP_SOMA:
                case OP_SUBTRACAO:
                case OP_MULTIPLICACAO:
                case OP_DIV_INTEIRA:
                case OP_DIV_REAL:
                case OP_MOD:
                    expr.setTipo(TipoExpressao.OPERACAO_ARITMETICA);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    add = true;
                    break;
                
                case LIB_MATH_POT:
                case LIB_MATH_RAIZ:
                    expr.setTipo(TipoExpressao.CHAMADA_FUNCAO);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    add = true;
                    break;
                    
                //DIVERSOS MODOS
                case DELIM_PARENTESES_ABRE:
                    switch (expr.getTipo()){
                        case ENTRADA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            add = false;
                            break;
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                            add = false;
                            break;
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            balancParenteses ++;
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                            add = true;
                            break;
                    }
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    switch (expr.getTipo()){
                        case ENTRADA_DE_DADOS:
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            add = false;
                            break;
                        case CHAMADA_FUNCAO:
                            //expr.setTipo(TipoExpressao.OPERACAO_ARITMETICA);
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            balancParenteses --;
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            funcoesEsperadas.add(FuncaoToken.OP_SOMA);
                            funcoesEsperadas.add(FuncaoToken.OP_SUBTRACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MULTIPLICACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_DIV_INTEIRA);
                            funcoesEsperadas.add(FuncaoToken.OP_DIV_REAL);
                            funcoesEsperadas.add(FuncaoToken.OP_MOD);
                            add = true;
                            break;
                    }
                    break;
                    
                case DELIM_VIRGULA:
                    switch (expr.getTipo()){
                        case CRIACAO_VARIAVEL:
                        case ENTRADA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            add = false;
                            break;
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                            add = false;
                            break;
                        case CHAMADA_FUNCAO:
                        case OPERACAO_ARITMETICA:
                        case OPERACAO_ATRIBUICAO:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            add = false;
                            break;
                    }
                    break;
                
                case CONST_CARACTER:
                    switch (expr.getTipo()){
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            add = true;
                            break;
                    }
                    break;
                    
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    switch (expr.getTipo()){
                        case CRIACAO_VARIAVEL:
                            char inicial = token.getPalavra().charAt(0);
                            if (inicial >= '0' && inicial <= '9'){
                                escreveErro(token, "Identificador de variável não pode começar com número");
                                add = false;
                            } else {
                                variaveis.add(token.getPalavra());
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                                add = true;
                            }
                            break;
                        case ENTRADA_DE_DADOS:
                        case SAIDA_DE_DADOS:
                            if (!variaveis.contains(token.getPalavra())){
                                escreveErro(token, "Variável \"" + token.getPalavra() + "\" não declarada");
                                add = false;
                            } else {
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                                add = true;
                            }
                            break;
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            if (!variaveis.contains(token.getPalavra())){
                                escreveErro(token, "Variável \"" + token.getPalavra() + "\" não declarada");
                                add = false;
                            } else {
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                                funcoesEsperadas.add(FuncaoToken.OP_SOMA);
                                funcoesEsperadas.add(FuncaoToken.OP_SUBTRACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MULTIPLICACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_DIV_INTEIRA);
                                funcoesEsperadas.add(FuncaoToken.OP_DIV_REAL);
                                funcoesEsperadas.add(FuncaoToken.OP_MOD);
                                add = true;
                            }
                            break;
                        case _INDEFINIDO:
                        default:
                            if (!variaveis.contains(token.getPalavra())){
                                escreveErro(token, "Comando, variável ou função não identificada: " + token.getPalavra());
                                add = false;
                            } else {
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.OP_ATRIBUICAO);
                                add = true;
                            }
                            break;
                    }
                    break;
                    
                case _INDEF_NUMERICO:
                    switch (expr.getTipo()){
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            
                            funcoesEsperadas.clear();
                            
                            Token lastToken = expr.getTokenAt(expr.getNumTokens() - 1);
                            if (lastToken.getFuncaoToken() == FuncaoToken.CONST_REAL){
                                lastToken.atualizaPalavra(token.getPalavra());
                                expr.setTokenAt(expr.getNumTokens() - 1, lastToken);
                                add = false;
                            } else {
                                token.setFuncaoToken(FuncaoToken.CONST_INTEIRA);
                                add = true;
                                funcoesEsperadas.add(FuncaoToken.DELIM_PONTO);
                            }
                            
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            funcoesEsperadas.add(FuncaoToken.OP_SOMA);
                            funcoesEsperadas.add(FuncaoToken.OP_SUBTRACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MULTIPLICACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_DIV_INTEIRA);
                            funcoesEsperadas.add(FuncaoToken.OP_DIV_REAL);
                            funcoesEsperadas.add(FuncaoToken.OP_MOD);
                            
                            break;
                    }
                    break;
                    
                case DELIM_PONTO:
                    switch (expr.getTipo()){
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            Token lastToken = expr.getTokenAt(expr.getNumTokens() - 1);
                            lastToken.atualizaPalavra(token.getPalavra());
                            lastToken.setFuncaoToken(FuncaoToken.CONST_REAL);
                            expr.setTokenAt(expr.getNumTokens() - 1, lastToken);
                            
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            add = false;
                            break;
                    }
                    break;
                
                case DELIM_PONTO_VIRGULA:
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_INTEIRO);
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_REAL);
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_FIM);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    
                    add = false;
                    go = false;
                    break;
                    
                case _INDEFINIDO:
                default:
                    add = false;
                    go = false;
                    break;
            }
            
            
            
            expr.atualizaTexto(token.getPalavra());
            if (add){
                expr.addToken(token);
            }
        }
        
        if (balancParenteses != 0){
            String msg = "Parênteses não balanceados: ";
            if (balancParenteses > 0){
                msg += "Esperado fechamento de " + balancParenteses + " parenteses abertos";
            } else {
                msg += -balancParenteses + " parenteses fechados sem necessidade";
            }
            escreveErro(expr.getTokenAt(0), msg);
        } 
        
        if (!erro.isEmpty()){
            expr.setTipo(TipoExpressao._INVALIDO);
        }
        
        return expr;
    }
    
    public LinkedList<Expressao> getAllExpressions(){
        LinkedList<Expressao> lista = new LinkedList<>();
        String erros = "";
        while (hasNext()){
            lista.add(parseExpression());
            if (!erro.isEmpty()){
                erros += erro + "\n";
            }
        }
        erro = erros;
        return lista;
    }
    
    public String getErro(){
        return erro;
    }
    
    private void escreveErro(Token token, String msg){
        erro = "Linha " + (token.getLinha() + 1) + ", coluna " + (token.getColuna() + 1) + " - " + msg;
    }
    
    private boolean estadoValido(Token token){
        if (!funcoesEsperadas.contains(token.getFuncaoToken())){
            String msgErro = "Encontrou " + token.getFuncaoToken() + ", mas esperava:";
            for (FuncaoToken ft: funcoesEsperadas){
                msgErro += "\n - " + ft;
            }
            escreveErro(token, msgErro);
            return false;
        } else {
            return true;
        }
    }
    
}
