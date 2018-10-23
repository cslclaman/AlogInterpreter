/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduação do curso de An�lise e Desenvolvimento de Sistemas
 * Autor: Ca�que de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */
package fatec.alg.geral.expressao;

import fatec.alg.geral.token.Token;
import java.util.LinkedList;

/**
 *
 * @author Caique Souza
 */
public class ImpressoraExpressaoV2 {
    
    private class Impressao {
        private Impressao anterior;
        private Expressao expressao;
        private Token token;
        
        public Impressao(Expressao e) {
            anterior = null;
            expressao = e;
        }
    }
    
    private LinkedList<Impressao> expressoes;
    private Expressao inicial;
    private String texto;
    private Token selecionado;
    private Token resolvido;
    private int posicao;
    
    public ImpressoraExpressaoV2(Expressao expressao) {
        expressoes = new LinkedList<>();
        inicial = expressao;
        selecionado = geraToken("");
        resolvido = geraToken("");
        defineTexto(expressao);
    }
    
    public void setExpressaoAtual(Expressao expressao) {
        boolean setted = false;
        for (Impressao i : expressoes) {
            if (i.expressao == expressao) {
                i.expressao = expressao;
                setted = true;
                break;
            }
        }
        if (!setted) {
            expressoes.clear();
            expressoes = new LinkedList<>();
            inicial = expressao;
        }
        defineTexto(expressao);
    }
    
    private void defineTexto(Expressao exprAtual){
        posicao = 0;
        imprime(inicial, null);
        for (Impressao i : expressoes) {
            if (i.expressao == inicial) {
                texto = i.token.getPalavra();
            }
            if (i.expressao == exprAtual) {
                selecionado = (i.anterior == null ? i.token : i.anterior.token);
                if (exprAtual.isResolvida()) {
                    resolvido = i.token;
                }
                break;
            }
        }
    }
    
    private void imprime(Expressao e, Impressao a) {
        Impressao i = new Impressao(e);
        if (e != inicial) {
            i.anterior = a;
        }
        if (e.isResolvida()) {
            i.token = geraToken(e.getResultado());
            posicao += i.token.getTamanho() + 1;
        } else {
            StringBuffer buf = new StringBuffer();
            if (e.getParentesesAbre() != null) {
                buf.append(e.getParentesesAbre().getPalavra()).append(" ");
                posicao += e.getParentesesAbre().getTamanho() + 1;
            }
            switch (e.tipoExpressao) {
                case OPERANDO_CONSTANTE:
                case OPERANDO_VARIAVEL:
                    Operando operando = (Operando)e;
                    buf.append(operando.getOperando().getPalavra()).append(" ");
                    posicao += operando.getOperando().getTamanho() + 1;
                    break;
                case OPERACAO_UNARIA:
                    OperacaoUnaria operacaoUnaria = (OperacaoUnaria)e;
                    buf.append(operacaoUnaria.getOperador().getPalavra()).append(" ");
                    posicao += operacaoUnaria.getOperador().getTamanho() + 1;
                    imprime(operacaoUnaria.getExpressao(), i);
                    break;
                case OPERACAO_ARITMETICA:
                case OPERACAO_RELACIONAL:
                case OPERACAO_LOGICA:
                    Operacao operacao = (Operacao)e;
                    imprime(operacao.getExpressaoEsq(), i);
                    buf.append(operacao.getOperador().getPalavra()).append(" ");
                    posicao += operacao.getOperador().getTamanho() + 1;
                    imprime(operacao.getExpressaoDir(), i);
                    break;
                case OPERANDO_FUNCAO:
                    ChamadaFuncao chamadaFuncao = (ChamadaFuncao)e;
                    buf.append(chamadaFuncao.getTokenNome().getPalavra()).append(" ");
                    posicao += chamadaFuncao.getTokenNome().getTamanho() + 1;
                    buf.append("( ");
                    posicao += 2;
                    boolean first = true;
                    for (Expressao expr : chamadaFuncao.getParametros()) {
                        if (first) {
                            first = false;
                        } else {
                            buf.append(", ");
                            posicao += 2;
                        }
                        imprime(expr, i);
                    }
                    buf.append(") ");
                    posicao += 2;
                    break;
            }
            if (e.getParentesesFecha() != null) {
                buf.append(e.getParentesesFecha().getPalavra()).append(" ");
                posicao += e.getParentesesFecha().getTamanho() + 1;
            }
            i.token = geraToken(buf.toString());
        }
        expressoes.add(i);
    }
    
    public String getTexto() {
        return texto;
    }

    public Token getTokenExprAtual() {
        return selecionado;
    }

    public Token getTokenResultado() {
        return resolvido;
    }
    
    private Token geraToken(String palavra) {
        Token token = new Token(0, posicao, posicao, 0);
        token.setPalavra(palavra);
        return token;
    }
}
