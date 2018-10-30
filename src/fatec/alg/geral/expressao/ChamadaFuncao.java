package fatec.alg.geral.expressao;

import fatec.alg.geral.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Expressão de chamada de função (trecho de código nomeado com ou sem parâmetros
 * e que retorna um resultado após sua execução).
 * @author Caique Souza
 */
public class ChamadaFuncao extends Operando {
    
    private LinkedList<Expressao> parametros;
    private int numParametros;
    
    /**
     * Instancia uma expressão do tipo OPERANDO_FUNCAO sem parâmetros.
     */
    public ChamadaFuncao() {
        super();
        tipoExpressao = TipoExpressao.OPERANDO_FUNCAO;
        parametros = new LinkedList<>();
        numParametros = 0;
    }

    /**
     * Define o token que identifica a função.
     * @param token token
     */
    public void setTokenNome(Token token){
        super.setOperando(token);
    }
    
    /**
     * Retorna o token identificador da função.
     * @return token
     */
    public Token getTokenNome() {
        return super.getOperando();
    }
    
    /**
     * Adiciona uma expressão de parâmetro de entrada da função.
     * @param expressao parâmetro de entrada
     */
    public void addParametro(Expressao expressao) {
        parametros.add(expressao);
        numParametros ++;
        for (Token token : expressao.listaTokens()){
            super.addToken(token);
        }
    }
    
    /**
     * Retorna string com o nome e chama método {@link #imprimeExpressao() } para cada parâmetro.
     * @return "&lt; Identificador &gt; ( &lt; Expressão 2 &gt; [ , &lt; Expressão 2 &gt; ... ] )"
     */
    @Override
    public String imprimeExpressao (){
        String params = "";
        for (Expressao ex : parametros) {
            if (!params.isEmpty()) params += " , ";
            params += ex.imprimeExpressao();
        }
        return "< " + 
                (parentesesAbre == null ? "" : "( ") +
                operando.getPalavra() + " ( " + params + ")" +
                (parentesesFecha == null ? "" : " )") +
                " >";
    }

    /**
     * Retorna lista de expressões parâmetros.
     * @return lista de parâmetros
     */
    public List<Expressao> getParametros() {
        return parametros;
    }
    
    /**
     * Atualiza a expressão na posição indicada.
     * @param pos Índice da instrução na lista (base 0)
     * @param expressao Expressão com novo valor
     */
    public void atualizaParametro(int pos, Expressao expressao) {
        parametros.set(pos, expressao);
    }
    
    /**
     * Retorna o número de parâmetros da expressão.
     * @return Número de parâmetros.
     */
    public int getNumParametros () {
        return numParametros;
    }
    
    /**
     * Valida se o token identificador não é nulo e se todos os parâmetros são válidos.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            int numValidos = 0;
            for (Expressao expressao : parametros) {
                if (expressao.isValida()){
                    numValidos ++;
                }
            }

            valida = 
                    operando != null &&
                    numValidos == numParametros;
        }
    }
}
