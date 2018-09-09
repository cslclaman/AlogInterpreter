/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Bloco;
import alog.model.Expressao;
import alog.token.FuncaoToken;
import alog.model.TipoExpressao;
import alog.token.Token;
import java.util.ArrayList;
import java.util.LinkedList;

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
            return "Linha " + (token.getLinha() + 1) + ", coluna " + (token.getColuna() + 1) + " - " + erro;
        }
    }
    
    private ArrayList<Token> tokens;
    private ArrayList<String> variaveis;
    
    private ArrayList<FuncaoToken> funcoesEsperadas;
    private int pos;
    private ArrayList<ErroSintatico> erros;
    
    private boolean fimAtingido;
    private boolean erro;
    
    public Parser (ArrayList<Token> tokens){
        this.tokens = tokens;
        
        variaveis = new ArrayList<>();
        erros = new ArrayList<>();
        
        funcoesEsperadas = new ArrayList<>();
        funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
        funcoesEsperadas.add(FuncaoToken.RES_TIPO_CARACTER);
        funcoesEsperadas.add(FuncaoToken.RES_TIPO_INTEIRO);
        funcoesEsperadas.add(FuncaoToken.RES_TIPO_REAL);
        funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
        funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
        funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
        funcoesEsperadas.add(FuncaoToken.RES_COND_SENAO);
        funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
        funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
        
        pos = 0;
        fimAtingido = false;
    }
    
    public boolean fimAtingido(){
        return fimAtingido;
    }
    
    public int getPos(){
        return pos;
    }
    
    public void setPos(int pos){
        this.pos = pos;
    }
    
    public boolean hasNext(){
        return pos < tokens.size();
    }
    
    public Expressao parseExpression(){
        Expressao expr = new Expressao();
        Bloco bloco = null;
        
        LinkedList<Token> parentesesAbre = new LinkedList<>();
        LinkedList<Token> parentesesFecha = new LinkedList<>();
        
        boolean go = true;
        boolean add = false;
        erro = false;
        
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
                    expr.atualizaTexto(token.getPalavra());
                    expr.addToken(token);
                    
                    bloco = new Bloco();
                    bloco.addExpressao(expr);
                    
                    Parser innerParser = new Parser(tokens);
                    innerParser.setPos(pos);
                    while (innerParser.hasNext() && !innerParser.fimAtingido()){
                        bloco.addExpressao(innerParser.parseExpression());
                        if (innerParser.hasErroParsing()){
                            erros.add(new ErroSintatico(innerParser.getTokenUltimoErro(), innerParser.getMsgUltimoErro()));
                        }
                    }
                    
                    if (!innerParser.hasNext() && !innerParser.fimAtingido()){
                        erros.add(new ErroSintatico(bloco.getExpressaoAt(0).getTokenAt(0), "Delimitador FIM para INÍCIO indicado não encontrado"));
                    }
                    pos = innerParser.getPos();
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_INTEIRO);
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_REAL);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SENAO);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                    
                    add = false;
                    go = false;
                    break;
                case RES_BLOCO_FIM:
                    expr.setTipo(TipoExpressao.DELIM_BLOCO);

                    fimAtingido = true;
                    funcoesEsperadas.clear();
                    
                    add = true;
                    go = false;
                    break;
                    
                //MODO: CRIAÇÃO DE VARIÁVEIS
                case RES_TIPO_CARACTER:
                case RES_TIPO_INTEIRO:
                case RES_TIPO_REAL:
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
                case OP_MAT_SOMA:
                case OP_MAT_SUBTRACAO:
                case OP_MAT_MULTIPLICACAO:
                case OP_MAT_DIV_INTEIRA:
                case OP_MAT_DIV_REAL:
                case OP_MAT_MOD:
                    if (expr.getTipo() == TipoExpressao.OPERACAO_ATRIBUICAO){
                        expr.setTipo(TipoExpressao.OPERACAO_ARITMETICA);
                    }
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
                    
                //MODO: CONDICIONAL
                case RES_COND_SE:
                    expr.setTipo(TipoExpressao.OPERACAO_LOGICA);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    add = true;
                    break;
                    
                case OP_REL_MAIOR:
                case OP_REL_MAIOR_IGUAL:
                case OP_REL_MENOR:
                case OP_REL_MENOR_IGUAL:
                case OP_REL_IGUAL:
                case OP_REL_DIFERENTE:
                case OP_LOG_E:
                case OP_LOG_OU:
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    add = true;
                    break;
                    
                case RES_COND_ENTAO:
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SENAO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    
                    add = false;
                    go = false;
                    break;
                    
                case RES_COND_SENAO:
                    expr.setTipo(TipoExpressao.OPERACAO_LOGICA);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SENAO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    
                    add = true;
                    go = false;
                    break;
                    
                //DIVERSOS MODOS
                case DELIM_PARENTESES_ABRE:
                    parentesesAbre.push(token);
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
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                            funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                            add = false;
                            break;
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
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
                        case CHAMADA_FUNCAO:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            add = true;
                            break;
                        case OPERACAO_LOGICA:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                            add = true;
                            break;
                    }
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    parentesesFecha.offer(token);
                    switch (expr.getTipo()){
                        case ENTRADA_DE_DADOS:
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            add = false;
                            break;
                        case CHAMADA_FUNCAO:
                            expr.setTipo(TipoExpressao.OPERACAO_ARITMETICA);
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
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
                            add = true;
                            break;
                        case CHAMADA_FUNCAO:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            add = true;
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
                        case OPERACAO_ATRIBUICAO:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            add = true;
                            break;
                        case OPERACAO_LOGICA:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_DIFERENTE);
                            funcoesEsperadas.add(FuncaoToken.OP_LOG_E);
                            funcoesEsperadas.add(FuncaoToken.OP_LOG_OU);
                            funcoesEsperadas.add(FuncaoToken.RES_COND_ENTAO);
                            add = true;
                    }
                    break;
                    
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    switch (expr.getTipo()){
                        case CRIACAO_VARIAVEL:
                            char inicial = token.getPalavra().charAt(0);
                            if (inicial >= '0' && inicial <= '9'){
                                erros.add(new ErroSintatico(token, "Identificador de variável não pode começar com número"));
                                erro = true;
                                add = false;
                                go = false;
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
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Variável \"" + token.getPalavra() + "\" não declarada"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                                add = true;
                            //}
                            break;
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Variável \"" + token.getPalavra() + "\" não declarada"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                                add = true;
                            //}
                            break;
                        case CHAMADA_FUNCAO:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Variável \"" + token.getPalavra() + "\" não declarada"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                                funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                                add = true;
                            //}
                            break;
                        case OPERACAO_LOGICA:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Variável \"" + token.getPalavra() + "\" não declarada"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR_IGUAL);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR_IGUAL);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_IGUAL);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_DIFERENTE);
                                funcoesEsperadas.add(FuncaoToken.OP_LOG_E);
                                funcoesEsperadas.add(FuncaoToken.OP_LOG_OU);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                                funcoesEsperadas.add(FuncaoToken.RES_COND_ENTAO);
                                add = true;
                            //}
                            break;
                            
                        case _INDEFINIDO:
                        default:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Comando, variável ou função não identificada: " + token.getPalavra()));
                                
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.OP_ATRIBUICAO);
                                add = true;
                            //}
                            break;
                    }
                    break;
                    
                case _INDEF_NUMERICO:
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
                    
                    switch (expr.getTipo()){
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                            break;
                        case CHAMADA_FUNCAO:
                            funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            break;
                        case OPERACAO_LOGICA:
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_DIFERENTE);
                            funcoesEsperadas.add(FuncaoToken.OP_LOG_E);
                            funcoesEsperadas.add(FuncaoToken.OP_LOG_OU);
                            funcoesEsperadas.add(FuncaoToken.RES_COND_ENTAO);
                            add = true;
                            break;
                    }
                    break;
                    
                case DELIM_PONTO:
                    switch (expr.getTipo()){
                        case CHAMADA_FUNCAO:
                        case OPERACAO_ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                        case OPERACAO_LOGICA:
                            lastToken = expr.getTokenAt(expr.getNumTokens() - 1);
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
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_INTEIRO);
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_REAL);
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_FIM);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SENAO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    
                    add = false;
                    go = false;
                    break;
                    
                case _INVALIDO:
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
        
        while (!parentesesAbre.isEmpty() && !parentesesFecha.isEmpty()){
            parentesesAbre.pop();
            parentesesFecha.pop();
        }
        
        while (!parentesesAbre.isEmpty()){
            erros.add(new ErroSintatico(parentesesAbre.pop(), "Parêntese não fechado"));
            erro = true;
        }
        
        while (!parentesesFecha.isEmpty()){
            erros.add(new ErroSintatico(parentesesFecha.pop(), "Parêntese fechado sem necessidade"));
            erro = true;
        }
        
        if (bloco == null){
            if (erro){
                expr.setTipo(TipoExpressao._INVALIDO);
            }
            return expr;
        } else {
            return bloco;
        }
    }
    
    public LinkedList<Expressao> getAllExpressions(){
        LinkedList<Expressao> lista = new LinkedList<>();
        while (hasNext()){
            lista.add(parseExpression());
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
    
    public boolean hasErroParsing(){
        return erro;
    }
    
    public Token getTokenUltimoErro(){
        return erros.isEmpty() ? null : erros.get(erros.size() - 1).token;
    }
    
    public String getMsgUltimoErro(){
        return erros.isEmpty() ? "" : erros.get(erros.size() - 1).erro;
    }
    
    public ArrayList<Token> getTokensErros(){
        ArrayList<Token> errToken = new ArrayList<>();
        for (ErroSintatico err : erros){
            errToken.add(err.token);
        }
        return errToken;
    }
    
    private boolean estadoValido(Token token){
        if (!funcoesEsperadas.contains(token.getFuncaoToken())){
            String msgErro = "Encontrou " + token.getFuncaoToken() + ", mas esperava:";
            for (FuncaoToken ft: funcoesEsperadas){
                msgErro += "\n - " + ft;
            }
            erros.add(new ErroSintatico(token, msgErro));
            erro = true;
            return false;
        } else {
            return true;
        }
    }
    
}
