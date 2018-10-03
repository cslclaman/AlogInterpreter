package alog.expressao;

import alog.instrucao.Instrucao;
import alog.instrucao.TipoInstrucao;
import alog.model.TipoVariavel;
import alog.token.Token;

/**
 * 
 * @author Caique
 */
public abstract class Expressao extends Instrucao {

    protected TipoExpressao tipoExpressao;
    protected TipoVariavel tipoResultado;
    protected String resultado;
    protected Token parentesesAbre;
    protected Token parentesesFecha;
    
    protected Expressao() {
        super();
        tipo = TipoInstrucao.EXPRESSAO;
        tipoExpressao = TipoExpressao._INDEFINIDO;
    }

    public TipoExpressao getTipoExpressao() {
        return tipoExpressao;
    }

    public TipoVariavel getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(TipoVariavel tipoResultado) {
        this.tipoResultado = tipoResultado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Token getParentesesAbre() {
        return parentesesAbre;
    }

    public void setParentesesAbre(Token parentesesAbre) {
        this.parentesesAbre = parentesesAbre;
        tokens.addFirst(parentesesAbre);
        texto.insert(0, parentesesAbre.getPalavra() + " ");
    }

    public Token getParentesesFecha() {
        return parentesesFecha;
    }

    public void setParentesesFecha(Token parentesesFecha) {
        this.parentesesFecha = parentesesFecha;
        tokens.addLast(parentesesFecha);
        texto.append(parentesesFecha.getPalavra());
    }

    @Override
    public String toString() {
        return getTexto();
    }

    public abstract String imprimeExpressao();
    
}
