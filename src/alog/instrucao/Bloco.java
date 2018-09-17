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
    
    /**
     * Constrói um bloco vazio, sem instruções.
     */
    public Bloco() {
        tokens = new LinkedList<>();
        instrucoes = new LinkedList<>();
        tipo = TipoInstrucao.BLOCO;
        texto = "";
    }

    /**
     * Adiciona uma instrução previamente parseada ao bloco.
     * @param instrucao 
     */
    public void addInstrucao(Instrucao instrucao){
        if (inicio != null && instrucao.instrucaoValida()){
            instrucoes.add(instrucao);
            texto += "\n    " + instrucao.getTipo();
            for (Token token : instrucao.listaTokens()){
                tokens.add(token);
            }
        }
    }

    /**
     * Lista todas as instruções contidas no bloco.
     * @return 
     */
    public List<Instrucao> listaInstrucoes(){
        return instrucoes;
    }

    /**
     * Retorna se o bloco possui início, fim e pelo menos uma instrução válida
     * @return 
     */
    @Override
    public boolean instrucaoValida() {
        if (inicio == null ||
            fim == null ||
            instrucoes.isEmpty()){
            
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }
    
    public void setInicio(Token inicio){
        if (this.inicio == null){
            this.inicio = inicio;
            texto += inicio.getPalavra();
            tokens.add(inicio);
        }
    }
    
    public void setFim(Token fim){
        if (inicio != null && !instrucoes.isEmpty()){
            this.fim = fim;
            texto += "\n" + fim.getPalavra();
            tokens.add(fim);
        }
    }

    public Token getInicio() {
        return inicio;
    }

    public Token getFim() {
        return fim;
    }
}
