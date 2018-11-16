/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduação do curso de Analise e Desenvolvimento de Sistemas
 * Autor: Caique de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */
package fatec.alg.geral.expressao;

import fatec.alg.geral.token.Token;
import java.util.LinkedList;

/**
 *
 * @author Caique Souza
 * @version 2
 */
public class ExibidorExpressao {
    
    private class Exibicao {
        private Exibicao anterior;
        private Expressao expressao;
        private Token token;
        
        public Exibicao(Expressao expressao) {
            this.anterior = null;
            this.expressao = expressao;
        }
    }
    
    private LinkedList<Exibicao> expressoes;
    private Expressao inicial;
    private StringBuffer texto;
    private Token selecionado;
    private Token resolvido;
    private int posicao;
    private boolean hasResult;
    
    public ExibidorExpressao(Expressao expressao) {
        expressoes = new LinkedList<>();
        inicial = expressao;
        selecionado = geraToken("");
        resolvido = geraToken("");
        defineTexto(expressao);
    }
    
    public void setExpressaoAtual(Expressao expressao) {
        //Para saber se é o início de uma nova expressão ou se é um passo da mesma
        boolean setted = false; 
        for (Exibicao i : expressoes) {
            if (i.expressao == expressao) {
                i.expressao = expressao;
                setted = true;
                break;
            }
        }
        if (!setted) {
            inicial = expressao;
        }
        defineTexto(expressao);
    }
    
    private void defineTexto(Expressao exprAtual){
        expressoes.clear();
        expressoes = new LinkedList<>();
        posicao = 0;
        texto = new StringBuffer();
        imprime(inicial, null);
        for (Exibicao i : expressoes) {
            if (i.expressao == exprAtual) {
                if (hasResult = exprAtual.isResolvida()) {
                    resolvido = i.token;
                    selecionado = (i.anterior == null ? i.token : i.anterior.token);
                } else {
                    resolvido = new Token(0, 0, 0, 0);
                    selecionado = i.token;
                }
                break;
            }
        }
    }
    
    private void imprime(Expressao e, Exibicao a) {
        Exibicao i = new Exibicao(e);
        if (e != inicial) {
            i.anterior = a;
        }
        if (e.isResolvida()) {
            i.token = geraToken(e.getResultado());
            posicao += i.token.getTamanho() + 1;
            texto.append(i.token.getPalavra()).append(" ");
        } else {
            String tp;
            int posIni = posicao;
            if (e.getParentesesAbre() != null) {
                tp = e.getParentesesAbre().getPalavra() + " ";
                texto.append(tp);
                posicao += tp.length();
            }
            switch (e.tipoExpressao) {
                case OPERANDO_CONSTANTE:
                case OPERANDO_VARIAVEL:
                    Operando operando = (Operando)e;
                    tp = operando.getOperando().getPalavra() + " ";
                    texto.append(tp);
                    posicao += tp.length();
                    break;
                case OPERACAO_UNARIA:
                    OperacaoUnaria operacaoUnaria = (OperacaoUnaria)e;
                    tp = operacaoUnaria.getOperador().getPalavra() + " ";
                    texto.append(tp);
                    posicao += tp.length();
                    imprime(operacaoUnaria.getExpressao(), i);
                    break;
                case OPERACAO_ARITMETICA:
                case OPERACAO_RELACIONAL:
                case OPERACAO_LOGICA:
                    Operacao operacao = (Operacao)e;
                    imprime(operacao.getExpressaoEsq(), i);
                    tp = operacao.getOperador().getPalavra() + " ";
                    texto.append(tp);
                    posicao += tp.length();
                    imprime(operacao.getExpressaoDir(), i);
                    break;
                case OPERANDO_FUNCAO:
                    ChamadaFuncao chamadaFuncao = (ChamadaFuncao)e;
                    tp = chamadaFuncao.getTokenNome().getPalavra() + " ( ";
                    texto.append(tp);
                    posicao += tp.length();
                    
                    boolean first = true;
                    for (Expressao expr : chamadaFuncao.getParametros()) {
                        if (first) {
                            first = false;
                        } else {
                            tp = ", ";
                            texto.append(tp);
                            posicao += tp.length();
                        }
                        imprime(expr, i);
                    }
                    tp = ") ";
                    texto.append(tp);
                    posicao += tp.length();
                    break;
            }
            if (e.getParentesesFecha() != null) {
                tp = e.getParentesesFecha().getPalavra() + " ";
                texto.append(tp);
                posicao += tp.length();
            }
            i.token = geraToken(posIni, texto.toString().substring(posIni, posicao));
        }
        expressoes.add(i);
    }
    
    public String getTexto() {
        return texto.toString().trim();
    }

    public Token getTokenExprAtual() {
        return selecionado;
    }

    public Token getTokenResultado() {
        return resolvido;
    }
    
    public boolean hasTokenResultado() {
        return hasResult;
    }
    
    private Token geraToken(String palavra) {
        return geraToken(posicao, palavra);
    }
    
    private Token geraToken(int pos, String palavra) {
        Token token = new Token(0, pos, pos, 0);
        token.setPalavra(palavra.trim());
        return token;
    }
}
