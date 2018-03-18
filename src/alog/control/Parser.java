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
        
        boolean go = true;
        boolean add = false;
        erro = "";
        
        while (go && pos < tokens.size()){
            Token token = tokens.get(pos++);
            
            if (!estadoValido(token)){
                break;
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
                    
                
                
                //MODO: ENTRADA DE DADOS
                case LIB_IO_LEIA:
                    expr.setTipo(TipoExpressao.ENTRADA_DE_DADOS);
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
                    }
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    switch (expr.getTipo()){
                        case ENTRADA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            add = false;
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
                    }
                    break;
                    
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    switch (expr.getTipo()){
                        case CRIACAO_VARIAVEL:
                            char inicial = token.getPalavra().charAt(0);
                            if (inicial >= '0' && inicial <= '9'){
                                escreveErro(token, "Identificador de variável não pode começar com número");
                            }
                            variaveis.add(token.getPalavra());
                            token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                            
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            add = true;
                            break;
                        case ENTRADA_DE_DADOS:
                            if (!variaveis.contains(token.getPalavra())){
                                escreveErro(token, "Variável \"" + token.getPalavra() + "\" não declarada");
                            }
                            token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                            
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
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
        
        return expr;
    }
    
    public ArrayList<Expressao> getAllExpressions(){
        ArrayList<Expressao> lista = new ArrayList<>();
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