package fatec.alg.geral.instrucao;

import fatec.alg.geral.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Instrução que corresponde à rotina de entrada de dados para variáveis.
 * @author Caique Souza
 */
public class EntradaDados extends Instrucao {

    private Token nome;
    private LinkedList<Token> parametros;
    private int numParametros;
    
    /**
     * Instancia uma instrução de tipo Entrada de Dados e sem parâmetros.
     */
    public EntradaDados() {
        super();
        tipo = TipoInstrucao.ENTRADA_DE_DADOS;
        parametros = new LinkedList<>();
        numParametros = 0;
    }
    
    /**
     * Define o Token que identifica a rotina (LEIA)
     * @param token token LEIA
     */
    public void setTokenNome(Token token){
        nome = token;
        super.addToken(token);
    }
    
    /**
     * Adiciona um token identificador de variável como parâmetro
     * @param token token ident. variável
     */
    public void addVariavel(Token token) {
        parametros.add(token);
        numParametros ++;
        super.addToken(token);
    }

    /**
     * Retorna token que identifica a rotina
     * @return token LEIA
     */
    public Token getNome() {
        return nome;
    }

    /**
     * Retorna lista de tokens que identificam variáveis.
     * @return lista de tokens
     */
    public List<Token> getParametros() {
        return parametros;
    }
    
    /**
     * Retorna um token identificador no índice informado.
     * @param index Índice do token (base 0)
     * @return Token no índice indicado ou NULL (caso índice seja menor que 0 ou maior que o número de parâmetros)
     */
    public Token getParametroAt(int index) {
        if (parametros.isEmpty()) {
            return null;
        } else {
            if (index >= 0 && index < numParametros) {
                return parametros.get(index);
            } else {
                return null;
            } 
        }
    }
    
    /**
     * Retorna o número de parâmetros.
     * @return num
     */
    public int getNumParametros() {
        return numParametros;
    }
    
    /**
     * Valida se o token identificador não é nulo e se há pelo menos um parâmetro.
     */
    @Override
    public void finaliza() {
        if (valida) {
            valida = 
                nome != null &&
                !parametros.isEmpty();
        }
    }
}
