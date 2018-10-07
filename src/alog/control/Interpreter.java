/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.instrucao.*;
import alog.expressao.*;
import alog.model.Variavel;
import java.util.HashMap;
import java.util.List;

/**
 * Classe que recebe uma expressão e executa.
 * @author Caique
 */
public class Interpreter extends Verificator {
    
    private InterfaceExecucao interfaceExecucao;
    private HashMap<String, Variavel> variaveis;
    private List<Instrucao> programa;
    private int numInstrucoes;
    private int pos;

    public Interpreter(InterfaceExecucao interfaceExecucao, List<Instrucao> programa) {
        super();
        variaveis = new HashMap<>();
        this.interfaceExecucao = interfaceExecucao;
        this.programa = programa;
        numInstrucoes = programa.size();
        pos = 0;
    }
    
    public boolean existeProxima() {
        return pos < numInstrucoes;
    }
    
    public void proxima () {
        Instrucao instrucao = programa.get(pos++);
        
    }
    
}
