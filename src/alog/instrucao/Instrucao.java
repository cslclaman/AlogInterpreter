package alog.instrucao;

import alog.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Uma instrução é uma sequência de Tokens que pode ser executada.
 * Por padrão, a instrução possui tipo {@link TipoInstrucao#_INDEFINIDO }.
 * @author Caique
 */
public abstract class Instrucao {
    protected LinkedList<Token> tokens;
    protected TipoInstrucao tipo;
    protected StringBuffer texto;
    
    public Instrucao(){
        tokens = new LinkedList<>();
        tipo = TipoInstrucao._INDEFINIDO;
        texto = new StringBuffer();
    }
    
    /**
     * Retorna instrução completa, aproximadamente como foi escrita. Ex.: {@code "Var <- Op1 + ( Op2 - Op3 ) ;" }
     * @return {@code "Var <- Op1 + ( Op2 - Op3 ) ;" }
     */
    public String getTexto() {
        return texto.toString().trim();
    }
    
    /**
     * Retorna o tipo da instrução.
     * @return {@link TipoInstrucao}
     */
    public TipoInstrucao getTipo() {
        return tipo;
    }

    /**
     * Retorna uma lista com todos os Tokens que a Instrução armazena.
     * @return 
     */
    public List<Token> listaTokens(){
        return tokens;
    }
    
    /**
     * Adiciona um token genérico à lista de tokens da instrução.
     * A instrução pode ou não checar o tipo do token sendo adicionado.
     * @param token 
     */
    public void addToken(Token token) {
        tokens.add(token);
        texto.append(token.getPalavra()).append(" ");
    }
    
    /**
     * Retorna se a instrução é válida e executável.
     * Note que validade se refere à análise sintática, mas não previne erros em tempo de execução.
     * @return 
     */
    public boolean instrucaoValida(){
        return tipo != TipoInstrucao._INVALIDO;
    }

    /**
     * Define a instrução como inválida.
     */
    public void invalidaInstrucao(){
        tipo = TipoInstrucao._INVALIDO;
    }
    
    /**
     * Retorna descrição da expressão e o texto armazenado.
     * @return {@code "ENTRADA DE DADOS - Leia Op1 Op2 Op3" }
     */
    @Override
    public String toString() {
        return tipo + " - " + getTexto();
    }
    
}
