/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.execucao.config;

import fatec.alg.geral.config.Configuracao;

/**
 * Configurações para o interpretador.
 * @author Caique Siqueira
 */
public class ConfigInterpreter extends Configuracao {
    
    /**
     * (Boolean) define se, após inserir uma instrução na pilha de execução,
     * já executará seu primeiro passo. (Padrão: SIM)
     */
    public final static String RUNNEXT_BLOCO_PILHA = "runnext.bloco.pilha";
    /**
     * (Boolean) define se, após confirmação da entrada de dados,
     * já executa o passo da atribuição para variável. (Padrão: NÃO)
     */
    public final static String RUNNEXT_LEIA_ATRIB = "runnext.leia.atrib";
    /**
     * (Boolean) define se a instrução do loop PARA-FAÇA será executada logo após
     * a verificação da condição. (Padrão: SIM).
     */
    public final static String RUNNEXT_PARA_LOOP = "runnext.para.loop";
    /**
     * (Boolean) Avalia se uma expressão constante será executada assim que for inserida
     * na pilha de execução por uma instrução. (padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_EXEC_CONST = "runnext.expressao.exec.constante";
    /**
     * (Boolean) Avalia se uma expressão com variável será executada assim que for inserida
     * na pilha de execução por uma instrução. (padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_EXEC_VAR = "runnext.expressao.exec.variavel";
    /**
     * (Boolean) Avalia se uma expressão de função será executada assim que for inserida
     * na pilha de execução por uma instrução. (padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_EXEC_FUNC = "runnext.expressao.exec.funcao";
    /**
     * (Boolean) Avalia se uma expressão unária será executada assim que for inserida
     * na pilha de execução por uma instrução. (padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_EXEC_UNARIA = "runnext.expressao.exec.opunaria";
    /**
     * (Boolean) Avalia se uma expressão binária será executada assim que for inserida
     * na pilha de execução por uma instrução. (padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_EXEC_OPBIN = "runnext.expressao.exec.opbinaria";
    /**
     * (Boolean) Avalia se será dada sequência à execução logo após a resolução de uma
     * expressão constante. (padrão: SIM)
     */
    public final static String RUNNEXT_EXPR_RES_CONST = "runnext.expressao.result.constante";
    /**
     * (Boolean) Avalia se será dada sequência à execução logo após a resolução de uma
     * expressão de variável. (padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_RES_VAR = "runnext.expressao.result.variavel";
    /**
     * (Boolean) Avalia se será dada sequência à execução logo após a resolução de uma
     * expressão de função. (padrão: SIM)
     */
    public final static String RUNNEXT_EXPR_RES_FUNC = "runnext.expressao.result.funcao";
    /**
     * (Boolean) Avalia se será dada sequência à execução logo após a resolução de uma
     * expressão unária. (padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_RES_UNARIA = "runnext.expressao.result.opunaria";
    /**
     * (Boolean) Avalia se será dada sequência à execução logo após a resolução de uma
     * expressão binária. (padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_RES_OPBIN = "runnext.expressao.result.opbinaria";
    /**
     * (Boolean) Define se uma expressão constante, ao ser finalizada, já será avaliada
     * pela instrução/expressão que a chamou. (Padrão: SIM)
     */
    public final static String RUNNEXT_EXPR_FIN_CONST = "runnext.expressao.final.constante";
    /**
     * (Boolean) Define se uma expressão com variável, ao ser finalizada, já será avaliada
     * pela instrução/expressão que a chamou. (Padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_FIN_VAR = "runnext.expressao.final.variavel";
    /**
     * (Boolean) Define se uma expressão de função, ao ser finalizada, já será avaliada
     * pela instrução/expressão que a chamou. (Padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_FIN_FUNC = "runnext.expressao.final.funcao";
    /**
     * (Boolean) Define se uma expressão unária, ao ser finalizada, já será avaliada
     * pela instrução/expressão que a chamou. (Padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_FIN_UNARIA = "runnext.expressao.final.opunaria";
    /**
     * (Boolean) Define se uma expressão binária, ao ser finalizada, já será avaliada
     * pela instrução/expressão que a chamou. (Padrão: NÃO)
     */
    public final static String RUNNEXT_EXPR_FIN_OPBIN = "runnext.expressao.final.opbinaria";
    /**
     * (Boolean) Define se, ao finalizar uma instrução "Escreva" (saída de dados),
     * será impressa na tela uma quebra de linha ("\n"). (Padrão: SIM)
     */
    public final static String FORMAT_ESCREVA_QUEBRA = "format.escreva.quebralinha";
    /**
     * (Boolean) Define se, entre cada parâmetro de uma instrução "Escreva" (saída de dados),
     * será impresso na tela um espaço em branco. (Padrão: NÃO)
     */
    public final static String FORMAT_ESCREVA_ESPACO = "format.escreva.espaco";
    /**
     * (Boolean) Define, <b>caso a configuração {@link #FORMAT_ESCREVA_ESPACO} seja verdadeira</b>,
     * se valores caracter terão espaços iniciais e finais removidos. (Padrão: SIM)
     */
    public final static String FORMAT_ESCREVA_ESPACO_TRIM = "format.escreva.espaco.trimcarac";
    /**
     * (String) Define o formato de números decimais. (Padrão: %.3f)
     */
    public final static String FORMAT_ESCREVA_REAL = "format.escreva.num.real";
    
    public ConfigInterpreter(){
        super();
        defaults();
    }
    
    private void defaults(){
        set(FORMAT_ESCREVA_QUEBRA, true);
        set(FORMAT_ESCREVA_ESPACO, false);
        set(FORMAT_ESCREVA_ESPACO_TRIM, true);
        
        set(FORMAT_ESCREVA_REAL, "%.3f");
        
        set(RUNNEXT_BLOCO_PILHA, true);
        set(RUNNEXT_LEIA_ATRIB, false);
        set(RUNNEXT_PARA_LOOP, true);
        
        set(RUNNEXT_EXPR_EXEC_CONST, false);
        set(RUNNEXT_EXPR_EXEC_VAR, false);
        set(RUNNEXT_EXPR_EXEC_FUNC, false);
        set(RUNNEXT_EXPR_EXEC_UNARIA, false);
        set(RUNNEXT_EXPR_EXEC_OPBIN, false);
        
        set(RUNNEXT_EXPR_RES_CONST, true);
        set(RUNNEXT_EXPR_RES_VAR, false);
        set(RUNNEXT_EXPR_RES_FUNC, true);
        set(RUNNEXT_EXPR_RES_UNARIA, false);
        set(RUNNEXT_EXPR_RES_OPBIN, false);
        
        set(RUNNEXT_EXPR_FIN_CONST, true);
        set(RUNNEXT_EXPR_FIN_VAR, false);
        set(RUNNEXT_EXPR_FIN_FUNC, false);
        set(RUNNEXT_EXPR_FIN_UNARIA, false);
        set(RUNNEXT_EXPR_FIN_OPBIN, false);
    }
    
}
