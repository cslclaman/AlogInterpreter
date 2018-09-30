package alog.expressao;

import alog.model.TipoVariavel;

/**
 *
 * @author Caique
 */
public enum TipoExpressao {
    
    /**
     * Expressão recém-inicializada.
     */
    _INDEFINIDO,
    
    /**
     * Expressão com {@link #OPERANDO}s numéricos que retorna um valor numérico.
     * <br>Exemplos:
     * <ul>
     * <li>Expressões matemáticas (ex. <code>2 + 2</code> ou <code>2 * (1 + 1)</code>, ambas retornam <code>4</code>)
     * <li>Funções que retornem valores numéricos (ex. <code>pot(2,2)</code>, retorna <code>4</code> )
     * </ul>
     * ou ainda funções que retornem valores de um desses tipos.
     * <br>Considere como operadores: <code>+</code> (soma), <code>-</code> (subtração),
     * <code>*</code> (multiplicação), <code>/</code> (divisão real),
     * <code>div</code> (quociente de divisão inteira) e <code>mod</code>. (resto (módulo) de divisão inteira)
     * <br>O retorno desse tipo de função será Real se pelo menos um de seus operandos for Real ou quando se tratar de divisão real
     * , senão será Inteiro.
     */
    ARITMETICA,
    
    /**
     * Expressão com {@link #OPERANDO}s numéricos ou caracter que retornam um valor lógico.
     * <br>Exemplos:
     * <ul>
     * <li>Comparação de igualdade entre números ou expressões aritméticas (ex. <code>2 = 2</code>, retorna <code>verdadeiro</code>
     * ou <code>2 &lt;&gt; 1 + 1</code>, retorna <code>falso</code>)
     * <li>Comparação de grandeza entre números ou exppressões aritméticas (ex. <code>2 &gt; 2</code>, retorna <code>falso</code>
     * ou <code>2 &lt; 2 + 2</code>, retorna <code>verdadeiro</code>)
     * <li>Comparação de igualdade ou grandeza entre operandos caracter (ex. <code>"aa" = "ab"</code>, retorna <code>falso</code>
     * ou <code>"a" &lt; "b"</code>, retorna <code>verdadeiro</code>)
     * <br>Neste caso, a comparação verifica a ordem alfabética entre os operandos caracter.
     * </ul>
     * <br>Considere como operadores: <code>&gt;</code> (maior que), <code>&lt;</code> (menor que),
     * <code>&gt;=</code> (maior ou igual a), <code>&lt;=</code> (menor ou igual a),
     * <code>=</code> (igual a) e <code>&lt;&gt;</code> (diferente de).
     */
    RELACIONAL,
    
    /**
     * Expressão com operandos lógicos que retornam um valor lógico.
     * <br>Exemplos:
     * <ul>
     * <li>Comparação lógica E (ex. <code>1 = 1 E 2 < 4</code>, retorna <code>verdadeiro</code> ou 
     * <code>"a" &lt;&gt; "b" E "a" &lt;&gt; "a"</code>, retorna <code>falso</code>)
     * <li>Comparação lógica OU (ex. <code>1 = 1 OU 1 = 2</code>, retorna <code>verdadeiro</code>
     * </ul>
     * Considere como operandos expressões relacionais.
     * <br>Considere como operadores: <code>E OU</code>.
     */
    LOGICA,
    
    /**
     * Operando - seja constante, variável ou função - que retorna um valor de seu tipo declarado.
     * <br>
     * O operando é o membro final de uma expressão. O resultado de uma expressão com apenas
     * um operando é o valor do próprio operando.
     * <br>Seu tipo pode ser:
     * <ul>
     * <li><b>Variável</b> declarada, de tipo {@link TipoVariavel#CARACTER},
     * {@link TipoVariavel#INTEIRO} ou {@link TipoVariavel#REAL}</li>
     * <li><b>Constante</b> numérica inteira ({@code 15}, {@code 2402}, {@code -7}),
     * numérica real ({@code 2.5}, {@code 0.00742}, {@code -3.75}) ou sequência literal
     * de caracteres ({@code "texto de exemplo"}, {@code "Caso de Teste #1:"})</li>
     * <li><b>Funções</b> matemáticas ou declaradas que retornem um resultado
     * equivalente a um dos tipos de variáveis ({@link TipoVariavel#CARACTER},
     * {@link TipoVariavel#INTEIRO} ou {@link TipoVariavel#REAL}).</li>
     * </ul>
     */
    OPERANDO,
}
