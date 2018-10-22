package fatec.alg.geral.expressao;

import fatec.alg.geral.tipo.TipoDado;

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
     * Expressão com um operador unário e uma subexpressão.
     * <br>Exemplos:
     * <ul>
     * <li>Símbolos de alteração de sinal (positivo/negativo) em expressões (ex. <code><b>+</b>2</code> ou <code><b>-</b> (1 + 1)</code>)
     * <li>Operador lógico inversor NÃO (ex. <code><b>Não</b> 2 &gt; 5</code> )
     * </ul>
     * Um operador unário retorna o mesmo tipo de resultado que a expressão que o segue,
     * porém com inversão em seu resultado.
     */
    OPERACAO_UNARIA,
    
    /**
     * Expressão com operandos numéricos que retorna um valor numérico.
     * <br>Exemplos:
     * <ul>
     * <li>Expressões matemáticas (ex. <code>2 + 2</code> ou <code>2 * (1 + 1)</code>, ambas retornam <code>4</code>)</li>
     * </ul>
     * <br>Considere como operadores: <code>+</code> (soma), <code>-</code> (subtração),
     * <code>*</code> (multiplicação), <code>/</code> (divisão real),
     * <code>div</code> (quociente de divisão inteira) e <code>mod</code>. (resto (módulo) de divisão inteira)
     * <br>O retorno desse tipo de função será Real se pelo menos um de seus operandos for Real ou quando se tratar de divisão real
     * , senão será Inteiro.
     */
    OPERACAO_ARITMETICA,
    
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
     * <br>Considere como operadores: <code>&gt;</code> (maior que), <code>&lt;</code> (menor que),
     * <code>&gt;=</code> (maior ou igual a), <code>&lt;=</code> (menor ou igual a),
     * <code>=</code> (igual a) e <code>&lt;&gt;</code> (diferente de).
     */
    OPERACAO_RELACIONAL,
    
    /**
     * Expressão com operandos lógicos que retornam um valor lógico.
     * <br>Exemplos:
     * <ul>
     * <li>Comparação lógica E (ex. <code>1 = 1 E 2 &lt; 4</code>, retorna <code>verdadeiro</code> ou 
     * <code>"a" &lt;&gt; "b" E "a" &lt;&gt; "a"</code>, retorna <code>falso</code>)
     * <li>Comparação lógica OU (ex. <code>1 = 1 OU 1 = 2</code>, retorna <code>verdadeiro</code>
     * </ul>
     * Considere como operandos expressões relacionais.
     * <br>Considere como operadores: <code>E OU</code>.
     */
    OPERACAO_LOGICA,
    
    /**
     * Operando de valor constante e tipo fixo.
     * <br>
     * O operando é o membro final de uma expressão. O resultado de uma expressão com apenas
     * um operando é o valor do próprio operando.
     * <br>Seu tipo pode ser:
     * <ul>
     * <li>Constante numérica inteira ({@code 15}, {@code 2402}, {@code -7})</li>
     * <li>Constante numérica real ({@code 2.5}, {@code 0.00742}, {@code -3.75})</li>
     * <li>Sequência literal de caracteres ({@code "texto de exemplo"}, {@code "Caso de Teste #1:"})</li>
     * </ul>
     */
    OPERANDO_CONSTANTE,
    
    /**
     * Operando que referencia uma variável declarada e retorna o valor salvo de acordo com o tipo definido.
     * <br>
     * O operando é o membro final de uma expressão. O resultado de uma expressão com apenas
     * um operando é o valor do próprio operando.
     * <br>Seu tipo pode ser:
     * <ul>
     * <li>Variável {@link TipoDado#CARACTER} (exemplo: {@code Caracter: Nome; Nome <- "João Silva";})</li>
     * <li>Variável {@link TipoDado#INTEIRO} (exemplo: {@code Inteiro: Idade; Idade <- 25;})</li>
     * <li>Variável {@link TipoDado#REAL} (exemplo: {@code Real: Peso; Peso <- 62.5;})</li>
     * </ul>
     */
    OPERANDO_VARIAVEL,
    
    /**
     * Função executável que pode receber parâmetros e retorna .
     * <br>
     * O operando é o membro final de uma expressão. O resultado de uma expressão com apenas
     * um operando é o valor do próprio operando.
     * <br>Seu tipo pode ser:
     * <ul>
     * <li>Função matemática nativa {@code POT (base, expoente)} - 
     * potenciação do valor base ({@link TipoDado#REAL}) pelo expoente ({@link TipoDado#REAL}), retorna {@link TipoDado#REAL}</li>
     * <li>Função matemática nativa {@code RAIZ (operando)} - 
     * raiz quadrada do operando ({@link TipoDado#REAL}), retorna {@link TipoDado#REAL}</li>
     * </ul>
     */
    OPERANDO_FUNCAO,
}
