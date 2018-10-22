package fatec.alg.geral.instrucao;

/**
 * Instrução inválida. Usada pelo analisador sintático em casos de tokens inválidos
 * (caracteres desconhecidos) e quando uma expressão não pôde ser concluída.
 * @author Caique
 */
public class InstrucaoInvalida extends Instrucao {

    /**
     * Instancia uma instrução inválida.
     */
    public InstrucaoInvalida() {
        super();
        tipo = TipoInstrucao._INVALIDO;
        valida = false;
    }

    /**
     * Sem ação.
     */
    @Override
    public void fazValidacao() {
    }
}
