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
 * Enumerador de tipos de tokens.
 * O tipo indica a classe de caracteres da palavra do token.
 * @author Caique
 */
public enum TipoToken {
    
    /**
     * Token contendo letras maiúsculas/minúsculas e underline.
     */
    ALFABETICO      ("Alfabético"),
    /**
     * Token contendo letras (maiúsculas/minúsculas), underline e números.
     */
    ALFANUMERICO    ("Alfanumérico"),
    /**
     * Token contendo números e ponto decimal.
     */
    NUMERICO        ("Numérico"),
    /**
     * Token contendo caracteres delimitadores (pontuação, parênteses, colchetes...).
     */
    DELIMITADOR     ("Delimitador"),
    /**
     * Token contendo caracteres operadores (+, -, *, /, =...).
     */
    OPERADOR        ("Operador"),
    /**
     * Token contendo quaisquer caracteres delimitados por aspas duplas.
     */
    LITERAL         ("Literal"),
    /**
     * Token com caracteres não inclusos em nenhum dos grupos acima.
     */
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
