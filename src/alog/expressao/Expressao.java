package alog.expressao;

import alog.instrucao.Instrucao;
import alog.instrucao.TipoInstrucao;
import alog.model.TipoVariavel;
import alog.token.Token;
import java.util.LinkedList;

/**
 * 
 * @author Caique
 */
public class Expressao extends Instrucao {

    private TipoExpressao tipoExpressao;
    private TipoVariavel tipoResultado;
    private String resultado;
    private Token parentesesAbre;
    private Token parentesesFecha;
    private Token operador;
    private Token operando;
    private Expressao membroEsq;
    private Expressao membroDir;
    
    public Expressao() {
        super();
        tipo = TipoInstrucao.EXPRESSAO;
        tipoResultado = null;
        tipoExpressao = TipoExpressao._INDEFINIDO;
        tokens = new LinkedList<>();
    }
}
