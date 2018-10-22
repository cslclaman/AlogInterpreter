/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduacao para o curso de Analise e Desenvolvimento de Sistemas
 * Autor: Caique de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */

package fatec.alg.geral.token;

/**
 * Classe de enumeração de categorias de funções de tokens.
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
     * Delimitador de comentário.
     */
    COMENTARIO,
    
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
