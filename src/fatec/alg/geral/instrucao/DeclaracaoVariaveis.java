package fatec.alg.geral.instrucao;

import fatec.alg.geral.tipo.TipoDado;
import fatec.alg.geral.variavel.Variavel;
import fatec.alg.geral.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Instrução que corresponde ao mecanismo de criação de variáveis.
 * @author Caique
 */
public class DeclaracaoVariaveis extends Instrucao {

    private Token tokenTipoVariavel;
    private TipoDado tipoVariavel;
    private LinkedList<Token> nomesVariaveis;
    
    /**
     * Cria uma instrução vazia.
     */
    public DeclaracaoVariaveis() {
        super();
        tipo = TipoInstrucao.DECLARACAO_VARIAVEL;
        nomesVariaveis = new LinkedList<>();
    }

    /**
     * Define o token que identifica qual o tipo da(s) variável(is) sendo criada(s).
     * Esse token será mapeado para {@link TipoDado}
     * @param token Token que representa o tipo de variável
     */
    public void setTokenTipoVariavel(Token token) {
        this.tokenTipoVariavel = token;
        tipoVariavel = TipoDado.mapTokenToVariavel(token);
        super.addToken(token);
    }

    /**
     * Retorna o token que representa o tipo da variável sendo criada
     * @return Token que representa o tipo de variável
     */
    public Token getTokenTipoVariavel() {
        return tokenTipoVariavel;
    }
    
    /**
     * Adiciona um token que representa um nome de variável sendo declarada.
     * @param token Token que representa um nome de variável.
     */
    public void addNomeVariavel(Token token) {
        nomesVariaveis.add(token);
        super.addToken(token);
    }

    /**
     * Retorna o tipo da(s) variável(is) sendo criada(s).
     * @return 
     */
    public TipoDado getTipoVariavel(){
        return tipoVariavel;
    }

    /**
     * Retorna uma lista com todos os tokens que identificam as variáveis.
     * @return 
     */
    public List<Token> getTokensNomesVariaveis() {
        return nomesVariaveis;
    }

    /**
     * Retorna o número de variaveis declaradas nessa instrução.
     * @return Número de variáveis declaradas.
     */
    public int getNumVariaveis() {
        return nomesVariaveis.size();
    }
    
    /**
     * Valida se foi definido um tipo válido e se há pelo menos um nome de variável informado.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            valida = 
                tipoVariavel != null && 
                !nomesVariaveis.isEmpty();
        }
    }
    
}
