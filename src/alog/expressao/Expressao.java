package alog.expressao;

import alog.instrucao.Instrucao;
import alog.instrucao.TipoInstrucao;
import alog.model.TipoDado;
import alog.token.Token;

/**
 * 
 * @author Caique
 */
public abstract class Expressao extends Instrucao {

    protected TipoExpressao tipoExpressao;
    protected TipoDado tipoResultado;
    protected String resultado;
    protected Token parentesesAbre;
    protected Token parentesesFecha;
    
    protected Expressao() {
        super();
        tipo = TipoInstrucao.EXPRESSAO;
        tipoExpressao = TipoExpressao._INDEFINIDO;
        tipoResultado = null;
    }

    public TipoExpressao getTipoExpressao() {
        return tipoExpressao;
    }

    public TipoDado getTipoResultado() {
        return tipoResultado;
    }

    public void setTipoResultado(TipoDado tipoResultado) {
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
    
    @Override
    public void finaliza() {
        if (valida) {
            valida = tipoExpressao != TipoExpressao._INDEFINIDO;
        }
    }
    
}
