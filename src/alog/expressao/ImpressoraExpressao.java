/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.expressao;

import alog.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Caique Souza
 */
public class ImpressoraExpressao {
    private final Token tokenVazio;
    private final int ordem;
    private final int pos;
    private Token[] tokens;
    private String texto;
    private Expressao expressao;
    
    private Token exprAtual;
    private Token resultado;
    
    public ImpressoraExpressao(Expressao expressao) {
        tokenVazio = new Token();
        tokenVazio.setPalavra("");
        
        this.expressao = expressao;
        System.out.println(expressao + " - " + (expressao.isResolvida() ? "Result " + expressao.getResultado() : " nres"));
        tokens = new Token[expressao.listaTokens().size()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokenVazio;
        }
        
        pos = expressao.listaTokens().get(0).getPosicao();
        ordem = expressao.listaTokens().get(0).getOrdem();
        
        exprAtual = tokenVazio;
        resultado = tokenVazio;
        defineTexto();
    }
    
    public void setExpressaoAtual(Expressao expressao) {
        this.expressao = expressao;
        System.out.println(expressao + " - " + (expressao.isResolvida() ? "Result " + expressao.getResultado() : " nres"));
        defineTexto();
    }
    
    private void defineTexto(){
        Token res;
        
        if (exprAtual == null) exprAtual = new Token();
        if (resultado == null) resultado = new Token();
        Token espec = new Token(0,0,0,-1);
        LinkedList<Integer> ordens = new LinkedList<>();
        
        if (!expressao.isResolvida()) {
            for (Token t : expressao.listaTokens()) {
                int ord = t.getOrdem() - ordem;
                res = new Token(0, 0, 0, ord);
                res.setPalavra(t.getPalavra());
                tokens[ord] = res;
                ordens.offer(ord);
            }
        } else {
            Token first = expressao.listaTokens().get(0);
            int ord = 0;
            for (Token t : expressao.listaTokens()) {
                ord = t.getOrdem() - ordem;
                tokens[ord] = tokenVazio;
            }
            ord = first.getOrdem() - ordem;
            ordens.offer(ord);
            res = new Token(0, 0, 0, ord);
            res.setPalavra(expressao.getResultado());
            tokens[ord] = res;
        }
        StringBuilder print = new StringBuilder();
        int c = 0;
        for (int i = 0; i < tokens.length; i++) {
            Token t = tokens[i];
            if (!t.getPalavra().isEmpty()) {
                print.append(t.getPalavra()).append(" ");
            }
            t.setPosicao(c);
            c += print.length();
            tokens[i] = t;
            if (ordens.contains(i)){
                if (espec.getOrdem() == -1) {
                    espec.setPosicao(t.getPosicao());
                    espec.setOrdem(t.getOrdem());
                    espec.setColuna(t.getColuna());
                }
                espec.atualizaPalavra(t.getPalavra() + " ");
            }
        }
        espec.setPalavra(espec.getPalavra().trim());
        if (!expressao.isResolvida()) {
            exprAtual = espec;
        } else {
            resultado = espec;
        }
        texto = print.toString().trim();
    }

    public String getTexto() {
        return texto;
    }

    public Token getTokenExprAtual() {
        return exprAtual;
    }

    public Token getTokenResultado() {
        return resultado;
    }
    
}
