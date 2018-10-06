package alog.instrucao;

import alog.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa um bloco de expressões (delimitado por início/fim)
 * @author Caique Souza
 */
public class Bloco extends Instrucao {
    private LinkedList<Instrucao> instrucoes;
    private Token inicio;
    private Token fim;
    private boolean blocoValido;
    /**
     * Constrói um bloco vazio, sem instruções.
     */
    public Bloco() {
        super();
        tipo = TipoInstrucao.BLOCO;
        instrucoes = new LinkedList<>();
        blocoValido = true;
    }

    /**
     * Adiciona uma instrução previamente analisada ao bloco.
     * @param instrucao Instrução a adicionar
     */
    public void addInstrucao(Instrucao instrucao){
        instrucoes.add(instrucao);
        texto.append("\n    ").append(instrucao.toString());
        for (Token token : instrucao.listaTokens()){
            tokens.add(token);
        }
        if (!instrucao.instrucaoValida()){
            blocoValido = false;
        }
    }

    /**
     * Lista todas as instruções contidas no bloco.
     * @return Lista de instruções
     */
    public List<Instrucao> listaInstrucoes(){
        return instrucoes;
    }

    /**
     * Retorna se o bloco possui início, fim e pelo menos uma instrução válida
     * @return se o bloco é válido
     */
    @Override
    public boolean instrucaoValida() {
        if (inicio == null ||
            fim == null ||
            instrucoes.isEmpty() ||
            !blocoValido
        ){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }
    
    public void setInicio(Token inicio){
        this.inicio = inicio;
        addToken(inicio);
    }
    
    public void setFim(Token fim){
        this.fim = fim;
        texto.append("\n").append(fim.getPalavra());
        tokens.add(fim);
    }

    public Token getInicio() {
        return inicio;
    }

    public Token getFim() {
        return fim;
    }
}
