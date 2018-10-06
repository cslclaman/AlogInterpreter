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
     * Erro de análise que impede que o código seja executado totalmente.
     */
    ERRO ("Erro", 1),
    
    /**
     * Alertas que podem causar resultados incorretos ou comandos ignorados,
     * mas não impedem a execução do código como um todo.
     */
    ALERTA ("Alerta", 2),
    
    /**
     * Informação básica que não indica nenhum problema de análise.
     */
    INFO ("Info", 3),
    
    /**
     * Informação que só deveria ser exibida ao desenvolvedor.
     */
    DEVEL ("Devel", 4);
    
    
    public static TipoErro getDefault() {
        return ALERTA;
    }
    
    public static TipoErro getMax() {
        return DEVEL;
    }
    
    
    private final String nome;
    private final int nivel;

    private TipoErro(String nome, int nivel) {
        this.nivel = nivel;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }
    
    @Override
    public String toString() {
        return nome.toUpperCase();
    }
}
