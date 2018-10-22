package fatec.alg.geral.instrucao;

/**
 * Instrução de chamada de rotina de saída de dados para a tela/interface.
 * Extende os métodos da instrução {@link ChamadaRotina}
 * @author Caique Souza
 */
public class SaidaDados extends ChamadaRotina {

    /**
     * Instancia uma instrução com tipo SAIDA_DE_DADOS.
     */
    public SaidaDados() {
        super();
        tipo = TipoInstrucao.SAIDA_DE_DADOS;
    }
    
    /**
     * Valida se a saída de dados possui pelo menos um parâmetro.
     */
    @Override
    public void finaliza() {
        super.finaliza();
        if (valida) {
            valida = numParametros > 0;
        }
    }
}
