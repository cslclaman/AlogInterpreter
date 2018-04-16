/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Bloco;
import alog.model.Expressao;
import alog.model.FuncaoToken;
import alog.model.TipoExpressao;
import alog.model.Token;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Analisador semântico que verifica a coerência das expressões criadas pelo Parser
 * @author Caique
 */
public class Semantic {
    private class ErroSemantico {
        Expressao expr;
        String msgErro;

        public ErroSemantico(Expressao expr, String msgErro) {
            this.expr = expr;
            this.msgErro = msgErro;
        }

        @Override
        public String toString() {
            return "Expressão inválida na linha " + expr.getLinha() + ": " + msgErro;
        }
    }
    
    private ArrayList<ErroSemantico> erros;
    private ArrayList<Expressao> programa;
    private HashMap <String, String> variaveis;
    private HashMap <String, Integer> funcoes;
    private int pos;
    
    public Semantic(ArrayList<Expressao> expressoes) {
        programa = new ArrayList<>();
        carregaExpressoes(expressoes);
        
        erros = new ArrayList<>();
        variaveis = new HashMap<>();
        funcoes = new HashMap<>();
        funcoes.put("POT", 2);
        funcoes.put("RAIZ", 1);
        pos = 0;
    }
    
    private void carregaExpressoes(ArrayList<Expressao> expressoes){
        for (Expressao expr : expressoes){
            if (expr.getTipo() == TipoExpressao._BLOCO){
                carregaExpressoes(((Bloco)expr).listExpressoes());
            } else {
                programa.add(expr);
            }
        }
    }
    
    public boolean hasNext(){
        return pos < programa.size();
    }
    
    public void verifyNext(){
        if (hasNext()){
            Expressao expressao = programa.get(pos++);
            switch (expressao.getTipo()){
                case CRIACAO_VARIAVEL:
                    String tipoVar = "";
                    for (Token t : expressao.listTokens()){
                        if (t.getFuncaoToken() == FuncaoToken.RES_TIPO_CARACTER ||
                            t.getFuncaoToken() == FuncaoToken.RES_TIPO_INTEIRO ||
                            t.getFuncaoToken() == FuncaoToken.RES_TIPO_REAL) {
                            tipoVar = t.getPalavra().toUpperCase();
                            continue;
                        }
                        String nomeVar = t.getPalavra();
                        if (variaveis.containsKey(nomeVar)){
                            erros.add(new ErroSemantico(expressao, "Variável já declarada: " + variaveis.get(nomeVar) + " " + nomeVar));
                        } else {
                            variaveis.put(nomeVar, tipoVar);
                        }
                    }
                    break;
                case ENTRADA_DE_DADOS:
                    for (Token t : expressao.listTokens()){
                        String nome = t.getPalavra();
                        switch (t.getFuncaoToken()){
                            case IDENT_NOME_VARIAVEL:
                                if (!variaveis.containsKey(nome)){
                                    erros.add(new ErroSemantico(expressao, "Variável não declarada: " + nome));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                    
                case OPERACAO_ARITMETICA:
                case OPERACAO_ATRIBUICAO:
                case OPERACAO_LOGICA:
                case SAIDA_DE_DADOS:
                case CHAMADA_FUNCAO:
                    int paramEsperado = 0;
                    int paramEncontrado = 0;
                    for (Token t : expressao.listTokens()){
                        String nome = t.getPalavra();
                        switch (t.getFuncaoToken()){
                            case LIB_MATH_POT:
                            case LIB_MATH_RAIZ:
                                paramEsperado = funcoes.get(nome.toUpperCase());
                                break;
                            case IDENT_NOME_VARIAVEL:
                                if (!variaveis.containsKey(nome)){
                                    erros.add(new ErroSemantico(expressao, "Variável não declarada: " + nome));
                                }
                                break;
                            case DELIM_VIRGULA:
                            case DELIM_PARENTESES_FECHA:
                                paramEncontrado ++;
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                    
                default:
                    break;
            }
        }
    }
    
}
