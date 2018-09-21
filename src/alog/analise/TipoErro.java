/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.analise;

/**
 *
 * @author Caique Souza
 */
public enum TipoErro {
    /**
     * Informação básica que não indica nenhum problema de análise.
     */
    INFO,
    
    /**
     * Alertas que podem causar resultados incorretos ou comandos ignorados,
     * mas não impedem a execução do código como um todo.
     */
    ALERTA,
    
    /**
     * Erro de análise que impede que o código seja executado totalmente.
     */
    ERRO,
}
