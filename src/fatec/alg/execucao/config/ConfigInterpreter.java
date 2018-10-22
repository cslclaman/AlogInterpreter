/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.execucao.config;

/**
 *
 * @author Caique Siqueira
 */
public class ConfigInterpreter {
    private boolean leiaAutoProx;
    private boolean escrevaAutoProx;
    private boolean escrevaQuebraLinha;
    private boolean execConstAutoProx;
    private boolean execVarAutoProx;
    private boolean execBinFinAutoProx;
    private boolean pushExprAutoProx;
    
    public ConfigInterpreter() {
        this.leiaAutoProx = false;
        this.escrevaAutoProx = false;
        this.escrevaQuebraLinha = true;
        this.execConstAutoProx = true;
        this.execVarAutoProx = false;
        this.execBinFinAutoProx = false;
        this.pushExprAutoProx = false;
    }

    public void setLeiaAutoProx(boolean leiaAutoProxLigado) {
        this.leiaAutoProx = leiaAutoProxLigado;
    }

    public void setEscrevaAutoProx(boolean escrevaAutoProx) {
        this.escrevaAutoProx = escrevaAutoProx;
    }

    public void setEscrevaQuebraLinha(boolean escrevaQuebraLinha) {
        this.escrevaQuebraLinha = escrevaQuebraLinha;
    }

    public void setExecConstAutoProx(boolean execConstAutoProx) {
        this.execConstAutoProx = execConstAutoProx;
    }

    public void setExecVarAutoProx(boolean ExecVarAutoProx) {
        this.execVarAutoProx = ExecVarAutoProx;
    }

    public void setExecBinFinAutoProx(boolean execBinFinAutoProx) {
        this.execBinFinAutoProx = execBinFinAutoProx;
    }

    public void setPushExprAutoProx(boolean pushExprAutoProx) {
        this.pushExprAutoProx = pushExprAutoProx;
    }
    
    public boolean isLeiaAutoProx() {
        return leiaAutoProx;
    }

    public boolean isEscrevaAutoProx() {
        return escrevaAutoProx;
    }

    public boolean isEscrevaQuebraLinha() {
        return escrevaQuebraLinha;
    }

    public boolean isExecConstAutoProx() {
        return execConstAutoProx;
    }

    public boolean isExecVarAutoProx() {
        return execVarAutoProx;
    }

    public boolean isExecBinFinAutoProx() {
        return execBinFinAutoProx;
    }

    public boolean isPushExprAutoProx() {
        return pushExprAutoProx;
    }
    
    
}
