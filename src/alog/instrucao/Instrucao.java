package alog.instrucao;

import alog.token.FuncaoToken;
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
    protected boolean valida;
    
    public Instrucao(){
        tokens = new LinkedList<>();
        tipo = TipoInstrucao._INDEFINIDO;
        texto = new StringBuffer();
        valida = true;
    }
    
    /**
     * Retorna instrução completa, aproximadamente como foi escrita. Ex.: {@code "Var <- Op1 + ( Op2 - Op3 ) ;" }
     * @return {@code "Var <- Op1 + ( Op2 - Op3 ) ;" }
     */
    public final String getTexto() {
        return texto.toString().trim();
    }
    
    /**
     * Retorna o tipo da instrução.
     * @return {@link TipoInstrucao}
     */
    public final TipoInstrucao getTipo() {
        return tipo;
    }

    /**
     * Retorna uma lista com todos os Tokens que a Instrução armazena.
     * @return Lista de tokens.
     */
    public final List<Token> listaTokens(){
        return tokens;
    }
    
    /**
     * Adiciona um token genérico à lista de tokens da instrução.
     * @param token Token a adicionar na instrução
     */
    public final void addToken(Token token) {
        tokens.add(token);
        texto.append(token.getPalavra()).append(" ");
    }
    
    /**
     * Retorna a expressão dentro de um Token de tipo "INVÁLIDO".
     * Deve ser usado apenas para fins de exibição e depuração.
     * @return Token com palavra igual ao do texto da expressão.
     */
    public final Token getAsToken () {
        Token t = tokens.getFirst();
        Token novo = new Token(t.getLinha(), t.getColuna(), t.getPosicao(), t.getOrdem());
        t.setFuncaoToken(FuncaoToken._INVALIDO);
        int pos = t.getPosicao();
        
        for (Token token : tokens) {
            pos += novo.getTamanho();
            for (int p2 = pos; p2 < token.getPosicao(); p2++) {
                novo.atualizaPalavra(" ");
            }
            novo.atualizaPalavra(token.getPalavra());
        }
        
        return novo;
    }
    
    /**
     * Retorna se a instrução é válida e executável.
     * Note que validade se refere à análise sintática, mas não previne erros em tempo de execução.
     * @return Se tipo de instrução não é inválido
     */
    public final boolean isValida(){
        return valida;
    }

    /**
     * Define a instrução como inválida.
     */
    public final void invalidaInstrucao(){
        valida = false;
    }
    
    /**
     * Finaliza a instrução. Isso pode implicar em validar atributos e definir validade.
     */
    public abstract void finaliza();
    
    /**
     * Retorna descrição da expressão e o texto armazenado.
     * @return {@code "ENTRADA DE DADOS - Leia Op1 Op2 Op3" }
     */
    @Override
    public String toString() {
        return tipo + " - " + getTexto();
    }
    
}
