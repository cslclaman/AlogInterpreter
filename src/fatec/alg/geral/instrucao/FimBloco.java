package fatec.alg.geral.instrucao;

import fatec.alg.geral.token.FuncaoToken;
import fatec.alg.geral.token.Token;

/**
 * Instrução que contém um único token {@link FuncaoToken#RES_BLOCO_FIM}.
 * Essa instrução serve apenas para a análise sintática - não será executada.
 * @author Caique Souza
 */
public class FimBloco extends Instrucao {
    private Token fim;
    
    /**
     * Instancia uma instrução com tipo Bloco.
     */
    public FimBloco() {
        super();
        tipo = TipoInstrucao.BLOCO;
    }

    /**
     * Define o token FIM de um bloco criado anteriormente.
     * @param token token Fim
     */
    public void setTokenFim(Token token) {
        this.fim = token;
        super.addToken(token);
    }
    
    /**
     * Valida se o token Fim não é nulo.
     */
    @Override
    public void fazValidacao() {
        if (valida){
            valida = fim != null;
        }
    }

}
