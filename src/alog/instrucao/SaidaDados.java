/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.instrucao;

/**
 *
 * @author Caique
 */
public class SaidaDados extends ChamadaRotina {

    public SaidaDados() {
        super();
        tipo = TipoInstrucao.SAIDA_DE_DADOS;
    }
    
    @Override
    public void finaliza() {
        super.finaliza();
        if (valida) {
            valida = numParametros > 0;
        }
    }
}
