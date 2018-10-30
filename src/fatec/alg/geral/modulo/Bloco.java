package fatec.alg.geral.modulo;

import fatec.alg.geral.instrucao.Instrucao;
import fatec.alg.geral.instrucao.TipoInstrucao;
import fatec.alg.geral.token.Token;
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
    private int numInstrucoes;
    /**
     * Constrói um bloco vazio, sem instruções.
     */
    public Bloco() {
        super();
        tipo = TipoInstrucao.BLOCO;
        instrucoes = new LinkedList<>();
        numInstrucoes = 0;
    }

    /**
     * Adiciona uma instrução previamente analisada ao bloco.
     * @param instrucao Instrução a adicionar
     */
    public void addInstrucao(Instrucao instrucao){
        instrucoes.add(instrucao);
        numInstrucoes ++;
        texto.append("\n    ").append(instrucao.toString());
        for (Token token : instrucao.listaTokens()){
            tokens.add(token);
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
     * Define o token que identifica o início do bloco
     * @param inicio Token de início
     */
    public void setInicio(Token inicio){
        this.inicio = inicio;
        addToken(inicio);
    }
    
    /**
     * Define o token que identifica o fim do bloco
     * @param fim Token de fim
     */
    public void setFim(Token fim){
        this.fim = fim;
        texto.append("\n").append(fim.getPalavra());
        tokens.add(fim);
    }

    /**
     * Retorna o token que identifica o início do bloco
     * @return Token início
     */
    public Token getInicio() {
        return inicio;
    }

    /**
     * Retorna o token que identifica o fim do bloco
     * @return Token fim
     */
    public Token getFim() {
        return fim;
    }

    /**
     * Retorna o número de instruções do bloco
     * @return número
     */
    public int getNumInstrucoes() {
        return numInstrucoes;
    }
    
    /**
     * Retorna a instrução do bloco no índice determinado
     * @param index Índice da instrução (base 0)
     * @return Instrução ou null caso o índice seja menor que 0 e maior que o num de instruções.
     */
    public Instrucao getInstrucaoAt(int index) {
        if (instrucoes.isEmpty()) {
            return null;
        } else {
            if (index >= 0 && index < numInstrucoes) {
                return instrucoes.get(index);
            } else {
                return null;
            } 
        }
    }
    
    /**
     * Valida o bloco: verifica se os tokens de início e fim não são nulos
     * e se todas as instruções são válidas.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            int instrValidas = 0;
            for (Instrucao instrucao : instrucoes) {
                if (instrucao.isValida()) {
                    instrValidas ++;
                }
            }

            valida =
                    inicio != null &&
                    fim != null &&
                    instrValidas == numInstrucoes;
        }
    }
}
