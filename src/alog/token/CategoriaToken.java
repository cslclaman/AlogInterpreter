/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.token;

/**
 *
 * @author Caique Souza
 */
public enum CategoriaToken {
    
    /**
     * Tokens ambíguos, indefinidos ou inválidos.
     */
    _INDEFINIDO,
    
    /**
     * Palavras reservadas.
     */
    PALAVRA_RESERVADA,
    
    /**
     * valores constantes.
     */
    CONSTANTE,
    
    /**
     * Identificadores declarados.
     */
    IDENTIFICADOR,
    
    /**
     * Funções padrão do sistema.
     */
    BIBLIOTECA,
    
    /**
     * Delimitadores e pontuações.
     */
    DELIMITADOR,
    
    /**
     * Operadores aritméticos, relacionais, lógicos e afins.
     */
    OPERADOR,
}
