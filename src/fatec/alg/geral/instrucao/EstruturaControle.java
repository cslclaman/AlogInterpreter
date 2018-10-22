package fatec.alg.geral.instrucao;

import fatec.alg.geral.expressao.Expressao;

/**
 * Representa estruturas de controle em geral (condicionais e repetitivas).
 * <br>Nesse tipo de instrução, é necessário que a expressão de condição
 * (uma expressão lógica ou relacional) seja verdadeira (ou falsa, dependendo
 * da estrutura) para que a instrução seja executada ou repetida.
 * @author Caique Souza
 */
public abstract class EstruturaControle extends Instrucao {
    
    /**
     * Expressão (com resultado lógico) que define a execução da instrução.
     */
    protected Expressao condicao;
    /**
     * Instrução a ser executada de acordo com o resultado da expressão.
     */
    protected Instrucao instrucao;
    
    /**
     * Define a expressão condicional da estrutura. Essa expressão deve ser
     * lógica ou relacional (ou seja, resultado lógico) e seu resultado define
     * se a instrução da estrutura será executada.
     * @param condicao Expressão com resultado lógico.
     */
    public void setExpressao (Expressao condicao) {
        this.condicao = condicao;
        tokens.addAll(condicao.listaTokens());
        texto.append(condicao.toString()).append(" ");
    }
    
    /**
     * Define a instrução principal da estrutura. Essa instrução será executada,
     * em geral, se a expressão condicional for verdadeira (podem haver exceções).
     * @param instrucao Instrução ou bloco de instruções a ser executado
     */
    public void setInstrucao(Instrucao instrucao) {
        this.instrucao = instrucao;
        tokens.addAll(instrucao.listaTokens());
        texto.append("\n    ").append(instrucao.toString()).append(" ");
    }
    
    /**
     * Retorna a expressão condicional da estrutura.
     * @return Expressão
     */
    public Expressao getCondicao() {
        return condicao;
    }

    /**
     * Retorna a instrução da estrutura.
     * @return Instrução ou bloco.
     */
    public Instrucao getInstrucao() {
        return instrucao;
    }
    
}
