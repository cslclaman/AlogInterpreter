package fatec.alg.geral.expressao;

import fatec.alg.execucao.Calculator;
import fatec.alg.geral.instrucao.Instrucao;
import fatec.alg.geral.instrucao.TipoInstrucao;
import fatec.alg.geral.tipo.TipoDado;
import fatec.alg.geral.variavel.Variavel;
import fatec.alg.geral.token.Token;

/**
 * Instrução que representa uma expressão que retorna um valor ao ser resolvida.
 * Essa expressão pode ser uma constante, uma referência a uma variável, uma 
 * chamada a função ou operação aritmética, relacional ou lógica (vide subclasses).
 * @author Caique
 */
public abstract class Expressao extends Instrucao {
    
    /**
     * Tipo da expressão. Deve ser informado por cada subclasse em seu construtor.
     */
    protected TipoExpressao tipoExpressao;

    /**
     * Tipo de dado de retorno da expressão. Definido em tempo de execução.
     */
    protected TipoDado tipoResultado;

    /**
     * Valor do resultado após a execução da expressão.
     * NOTA: Resultado não-nulo é algo diferente de um resultado válido.
     */
    protected String resultado;

    /**
     * Token de parênteses de abertura da expressão, se houver.
     */
    protected Token parentesesAbre;

    /**
     * Token de parênteses de fechamento da expressão, se houver.
     */
    protected Token parentesesFecha;
    private boolean resolvida;
    
    /**
     * Inicializa uma expressão de tipo indefinido e não resolvida.
     */
    protected Expressao() {
        super();
        tipo = TipoInstrucao.EXPRESSAO;
        tipoExpressao = TipoExpressao._INDEFINIDO;
        tipoResultado = null;
        resultado = "";
        resolvida = false;
    }

    /**
     * Retorna o tipo da expressão
     * @return tipo
     */
    public TipoExpressao getTipoExpressao() {
        return tipoExpressao;
    }

    /**
     * Retorna o tipo de dado do resultado
     * @return tipo de dado ou NULL caso a expressão não tenha sido resolvida.
     */
    public TipoDado getTipoResultado() {
        return tipoResultado;
    }

    /**
     * Define o tipo de resultado da expressão.
     * @param tipoResultado tipo de dado
     */
    public void setTipoResultado(TipoDado tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    /**
     * Retorna o resultado da expressão sem alterações em uma string.
     * @return String com resultado
     */
    public String getResultado() {
        return resultado;
    }
    
    /**
     * Retorna o valor do resultado como um número inteiro
     * @return resultado convertido para Long ou NULL em caso de erro de conversão
     */
    public Long getResultadoInteiro() {
        try {
            return Long.parseLong(resultado);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Retorna o valor do resultado como um número real (decimal)
     * @return resultado convertido para Double ou NULL em caso de erro de conversão
     */
    public Double getResultadoReal() {
        try {
            return Double.parseDouble(resultado);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Retorna o valor do resultado como um valor lógico (booleano)
     * @return resultado convertido para Boolean ou NULL em caso de erro de conversão
     */
    public Boolean getResultadoLogico() {
        if (resultado.equals("verdadeiro") || resultado.equals("falso")) {
            return resultado.equals("verdadeiro");
        } else {
            return null;
        }
    }
    
    /**
     * Retorna o valor do resultado como um valor caractere.
     * @return string com resultado (sem aspas duplas, caso possua).
     */
    public String getResultadoCaracter() {
        return resultado.replace("\"", "");
    }

    /**
     * Define o resultado da expressão a partir de uma string e define
     * a expressão como sendo resolvida.
     * @param resultado String com resultado.
     */
    public void setResultado(String resultado) {
        this.resultado = resultado;
        this.resolvida = true;
    }
    
    /**
     * Define o resultado da expressão a partir de uma variável com o valor.
     * Usando esse método, não é necessário definir o tipo do resultado com o método
     * {@link #setTipoResultado(fatec.alg.geral.tipo.TipoDado) }
     * @param variavel Variável de onde será obtido o valor.
     */
    public void setResultado(Variavel variavel) {
        setTipoResultado(variavel.getTipo());
        setResultado(variavel.getValor());
    }
    
    /**
     * Define o resultado da expressão a partir de uma calculadora com resultado.
     * Usando esse método, não é necessário definir o tipo do resultado com o método
     * {@link #setTipoResultado(fatec.alg.geral.tipo.TipoDado) }
     * @param calc Calculadora de onde será obtido o valor.
     */
    public void setResultado(Calculator calc) {
        setTipoResultado(calc.getTipo());
        setResultado(calc.getValor());
    }

    /**
     * Retorna o token de parênteses de abertura
     * @return token ou NULL caso não possua
     */
    public Token getParentesesAbre() {
        return parentesesAbre;
    }

    /**
     * Define o token de parênteses de abertura
     * @param parentesesAbre token
     */
    public void setParentesesAbre(Token parentesesAbre) {
        this.parentesesAbre = parentesesAbre;
        tokens.addFirst(parentesesAbre);
        texto.insert(0, parentesesAbre.getPalavra() + " ");
    }

    /**
     * Retorna o token de parênteses de fechamento
     * @return token ou NULL caso não possua
     */
    public Token getParentesesFecha() {
        return parentesesFecha;
    }

    /**
     * Define o token de parênteses de fechamento
     * @param parentesesFecha token
     */
    public void setParentesesFecha(Token parentesesFecha) {
        this.parentesesFecha = parentesesFecha;
        tokens.addLast(parentesesFecha);
        texto.append(parentesesFecha.getPalavra());
    }

    /**
     * Retorna se a expressão já foi executada e/ou teve seu resultado definido
     * @return TRUE se ja´foi resolvida e se o resultado não é nulo
     */
    public boolean isResolvida() {
        return resolvida;
    }

    /**
     * Retorna o texto da expressão. Igual ao método {@link #getTexto() }
     * @return Expressão como texto (ex. "3 + Num * ( Pot ( 4 , 2 ) )")
     */
    @Override
    public String toString() {
        return getTexto();
    }

    /**
     * Método que DEVE imprimir a expressão de forma recursiva.
     * @return String com impressão da expressão.
     */
    public abstract String imprimeExpressao();
    
    /**
     * Redefine a expressão, tornando seu resultado vazio e definindo-a como não resolvida.
     * Normalmente chamado pelo interpretador ao adicionar a expressão na pilha de execução.
     */
    public void redefine() {
        resolvida = false;
        resultado = "";
    }
    
    /**
     * Valida a expressão verificando se seu tipo não é INDEFINIDO.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            valida = tipoExpressao != TipoExpressao._INDEFINIDO;
        }
    }
    
}
