package alog.expressao;

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
     * Expressão com operandos numéricos que retorna um valor numérico.
     * <br>Exemplos:
     * <ul>
     * <li>Expressões com apenas um operando (ex. <code>2</code>, retorna <code>2</code>)
     * <li>Expressões matemáticas (ex. <code>2 + 2</code> ou <code>2 * (1 + 1)</code>, ambas retornam <code>4</code>)
     * <li>Funções que retornem valores numéricos (ex. <code>pot(2,2)</code>, retorna <code>4</code> )
     * </ul>
     * Considere como operandos variáveis ou constantes de tipo inteiro ou real,
     * ou ainda funções que retornem valores de um desses tipos.
     * <br>Considere como operadores: <code>+ - * / div mod</code>.
     * <br>O retorno desse tipo de função será Real se pelo menos um de seus operandos for Real ou quando se tratar de divisão real
     * , senão será Inteiro.
     */
    ARITMETICA,
    
    /**
     * Expressão com operandos numéricos ou caracter que retornam um valor lógico.
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
     * Considere como operandos variáveis ou constantes de tipo inteiro, real ou caracter,
     * ou ainda funções que retornem valores de um desses tipos.
     * <br>Considere como operadores: <code>&gt; &lt; &gt;= &lt;= = &lt;&gt;</code>.
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
     * Operador ou função que retorna um valor numérico inteiro.
     * <ul>
     * <li>Variável declarada com tipo Inteiro (<code>Inteiro: Num</code>)
     * <li>Constante numérica sem ponto decimal (<code>1  200</code>)
     * <li>Função com tipo de retorno declarado como Inteiro ou que retorne número sem ponto decimal.
     * </ul>
     */
    OPERADOR_INTEIRO,
    
    /**
     * Operador ou função que retorna um valor numérico real.
     * <ul>
     * <li>Variável declarada com tipo Real (<code>Real: NumDec</code>)
     * <li>Constante numérica com ponto decimal (<code>1.5  0.752</code>)
     * <li>Função com tipo de retorno declarado como Real ou que retorne número com ponto decimal.
     * </ul>
     */
    OPERADOR_REAL,
    
    /**
     * Operador ou função que retorna um valor caracter.
     * <ul>
     * <li>Variável declarada com tipo Caracter (<code>Caracter: Texto</code>)
     * <li>Constante literal (<code>"exemplo"</code>)
     * <li>Função com tipo de retorno declarado como Caracter ou que retorne sequência de caracteres.
     * </ul>
     */
    OPERADOR_CARACTER,
    
    /**
     * Operador que retorna um valor lógico.
     * <ul>
     * <li>Constante lógica (<code>verdadeiro  falso</code>)
     * </ul>
     */
    OPERADOR_LOGICO,
}
