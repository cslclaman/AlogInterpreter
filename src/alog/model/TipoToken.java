/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.model;

/**
 *
 * @author Caique
 */
public enum TipoToken {
    ALFABETICO      ("Alfabético"),
    ALFANUMERICO    ("Alfanumérico"),
    NUMERICO        ("Numérico"),
    DELIMITADOR     ("Delimitador"),    
    OPERADOR        ("Operador"),
    LITERAL         ("Literal"),
    INDEFINIDO      ("Indefinido");

    private final String exibicao;
    
    private TipoToken(String exibicao){
        this.exibicao = exibicao;
    }
    
    @Override
    public String toString() {
        return exibicao;
    }
}
