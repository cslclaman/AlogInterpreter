package alog.expressao;

import alog.instrucao.Instrucao;
import alog.instrucao.TipoInstrucao;
import java.util.LinkedList;

/**
 * 
 * @author Caique
 */
public class Expressao extends Instrucao {

    private TipoExpressao tipoExpressao;
    
    public Expressao() {
        texto = "";
        tipo = TipoInstrucao.EXPRESSAO;
        tipoExpressao = TipoExpressao._INDEFINIDO;
        tokens = new LinkedList<>();
    }
    
    @Override
    public boolean instrucaoValida() {
        return true;
    }
    
}
