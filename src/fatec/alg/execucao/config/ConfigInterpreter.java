/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.execucao.config;

import java.util.HashMap;

/**
 *
 * @author Caique Siqueira
 */
public class ConfigInterpreter {
    
    private HashMap<String, Object> configs;
    
    public final static String RUNNEXT_BLOCO_PILHA = "runnext.bloco.pilha";
    public final static String RUNNEXT_LEIA_ATRIB = "runnext.leia.atrib";
    public final static String RUNNEXT_ESCREVA_PILHA = "runnext.escreva.pilha";
    public final static String RUNNEXT_EXPR_PILHA = "runnext.expressao.pilha";
    public final static String RUNNEXT_PARA_LOOP = "runnext.para.loop";
    public final static String RUNNEXT_EXPR_EXEC_CONST = "runnext.expressao.exec.constante";
    public final static String RUNNEXT_EXPR_EXEC_VAR = "runnext.expressao.exec.variavel";
    public final static String RUNNEXT_EXPR_EXEC_FUNC = "runnext.expressao.exec.funcao";
    public final static String RUNNEXT_EXPR_EXEC_UNARIA = "runnext.expressao.exec.opunaria";
    public final static String RUNNEXT_EXPR_EXEC_OPBIN = "runnext.expressao.exec.opbinaria";
    public final static String RUNNEXT_EXPR_RES_CONST = "runnext.expressao.result.constante";
    public final static String RUNNEXT_EXPR_RES_VAR = "runnext.expressao.result.variavel";
    public final static String RUNNEXT_EXPR_RES_FUNC = "runnext.expressao.result.funcao";
    public final static String RUNNEXT_EXPR_RES_UNARIA = "runnext.expressao.result.opunaria";
    public final static String RUNNEXT_EXPR_RES_OPBIN = "runnext.expressao.result.opbinaria";
    public final static String RUNNEXT_EXPR_FIN_CONST = "runnext.expressao.final.constante";
    public final static String RUNNEXT_EXPR_FIN_VAR = "runnext.expressao.final.variavel";
    public final static String RUNNEXT_EXPR_FIN_FUNC = "runnext.expressao.final.funcao";
    public final static String RUNNEXT_EXPR_FIN_UNARIA = "runnext.expressao.final.opunaria";
    public final static String RUNNEXT_EXPR_FIN_OPBIN = "runnext.expressao.final.opbinaria";
    public final static String FORMAT_ESCREVA_QUEBRA = "format.escreva.quebralinha";
    public final static String FORMAT_ESCREVA_ESPACO = "format.escreva.espacamento";
    
    public ConfigInterpreter(){
        configs = new HashMap();
        
        configs.put(FORMAT_ESCREVA_QUEBRA, true);
        configs.put(FORMAT_ESCREVA_ESPACO, false);
        
        configs.put(RUNNEXT_BLOCO_PILHA, true);
        configs.put(RUNNEXT_LEIA_ATRIB, false);
        configs.put(RUNNEXT_ESCREVA_PILHA, false);
        configs.put(RUNNEXT_EXPR_PILHA, true);
        configs.put(RUNNEXT_PARA_LOOP, true);
        
        configs.put(RUNNEXT_EXPR_EXEC_CONST, false);
        configs.put(RUNNEXT_EXPR_EXEC_VAR, false);
        configs.put(RUNNEXT_EXPR_EXEC_FUNC, false);
        configs.put(RUNNEXT_EXPR_EXEC_UNARIA, false);
        configs.put(RUNNEXT_EXPR_EXEC_OPBIN, false);
        
        configs.put(RUNNEXT_EXPR_RES_CONST, true);
        configs.put(RUNNEXT_EXPR_RES_VAR, false);
        configs.put(RUNNEXT_EXPR_RES_FUNC, true);
        configs.put(RUNNEXT_EXPR_RES_UNARIA, false);
        configs.put(RUNNEXT_EXPR_RES_OPBIN, false);
        
        configs.put(RUNNEXT_EXPR_FIN_CONST, true);
        configs.put(RUNNEXT_EXPR_FIN_VAR, false);
        configs.put(RUNNEXT_EXPR_FIN_FUNC, false);
        configs.put(RUNNEXT_EXPR_FIN_UNARIA, false);
        configs.put(RUNNEXT_EXPR_FIN_OPBIN, false);
    }
    
    public boolean getBoolean(String key){
        if (configs.containsKey(key) && configs.get(key) instanceof Boolean ) {
            return (boolean) configs.get(key);
        } else {
            return false;
        }
    }
    
    public void set (String key, Object valor) {
        configs.put(key, valor);
    }
    
}
