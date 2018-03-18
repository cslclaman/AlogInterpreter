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

/**
 * Analisador sintático que verifica uma sequência de tokens e retorna expressões executáveis.
 * @author Caique
 */
public class Parser {
    private ArrayList<Token> tokens;
    private ArrayList<String> variaveis;
    private ArrayList<Expressao> expressoes;
    
    private ArrayList<FuncaoToken> funcoesEsperadas;
    
    private int pos;
    private String erro;
    
    public Parser (String texto){
        Scanner scanner = new Scanner(texto);
        tokens = scanner.getAll();
        
        variaveis = new ArrayList<>();
        expressoes = new ArrayList<>();
        funcoesEsperadas = new ArrayList<>();
        funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
        
        pos = 0;
    }
    
    public boolean hasNext(){
        return pos < tokens.size();
    }
    
    public boolean parseExpression(){
        Expressao expr = new Expressao();
        
        boolean go = true;
        boolean add = false;
        
        
        
        while (go && pos < tokens.size()){
            Token token = tokens.get(pos++);
            
            if (!estadoValido(token.getFuncaoToken())){
                return false;
            }
            switch(token.getFuncaoToken()){
                //MODO: INÍCIO/FIM DE BLOCO
                case RES_BLOCO_INICIO:
                    expr.setTipo(TipoExpressao.DELIM_BLOCO_INICIO);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_INTEIRO);
                    funcoesEsperadas.add(FuncaoToken.IDENT_TIPO_REAL);
                    
                    add = true;
                    go = false;
                    break;
                case RES_BLOCO_FIM:
                    expr.setTipo(TipoExpressao.DELIM_BLOCO_FIM);
                    
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
                    
                case DELIM_VIRGULA:
                    switch (expr.getTipo()){
                        case CRIACAO_VARIAVEL:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            add = false;
                            break;
                    }
                    break;
                    
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    switch (expr.getTipo()){
                        case CRIACAO_VARIAVEL:
                            char inicial = token.getPalavra().charAt(0);
                            if (inicial >= '0' && inicial <= '9'){
                                erro = "Identificador de variável não pode começar com número";
                                return false;
                            } else {
                                variaveis.add(token.getPalavra());
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                            }
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            add = true;
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
            
            if (add){
                expr.addToken(token);
            }
        }
        
        return true;
    }
    
    public String getErro(){
        return erro;
    }
    
    private boolean estadoValido(FuncaoToken funcaoEsperada){
        if (!funcoesEsperadas.contains(funcaoEsperada)){
            erro = "Encontrou " + funcaoEsperada + ", mas esperava:";
            for (FuncaoToken ft: funcoesEsperadas){
                erro += "\n - " + ft;
            }
            return false;
        } else {
            return true;
        }
    }
    
}
