/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.token;

/**
 *
 * @author Caique
 */
public enum FuncaoToken {
    
    //CATEGORIA: INDEFINIDOS (Tokens a serem categorizados ou inválidos)

    /**
     * Token não reconhecido
     */
    _INVALIDO ("TOKEN INVÁLIDO") ,
    
    /**
     * Pode ser identificador de variável, função ou rotina.
     * Token com mais de uma função possível (classificada pelo Parser).
     */
    _INDEF_ALFABETICO ("Identificador Alfabético"),
    /**
     * Pode ser identificador de variável, função ou rotina.
     * Token com mais de uma função possível (classificada pelo Parser).
     */
    _INDEF_ALFANUMERICO ("Identificador Alfanumérico"),
    /**
     * Pode ser constante inteira ou real.
     * Token com mais de uma função possível (classificada pelo Parser).
     */
    _INDEF_NUMERICO ("Numeral"),
    
    //CATEGORIA: PALAVRAS RESERVADAS (Funções e identificadores exclusivos)
    
    /**
     * Identificador do módulo principal do algoritmo: "algoritmo"
     */
    RES_ALGORITMO ("Algoritmo"),
    /**
     * Delimitador de bloco: "início" ou "inicio"
     */
    RES_BLOCO_INICIO ("Início"),
    /**
     * Delimitador de bloco: "fim"
     */
    RES_BLOCO_FIM ("Fim"),
    
    /**
     * Estrutura condicional: "se"
     */
    RES_COND_SE ("Se"),
    /**
     * Estrutura condicional: "então" ou "entao"
     */
    RES_COND_ENTAO ("Então"),
    /**
     * Estrutura condicional: "senão" ou "senao"
     */
    RES_COND_SENAO ("Senão"),
    
    /**
     * Estrutura repetitiva: "para"
     */
    RES_REP_PARA ("Para"),
    /**
     * Estrutura repetitiva: "até" ou "ate"
     */
    RES_REP_ATE ("Até"),
    /**
     * Estrutura repetitiva: "faca" ou "faça"
     */
    RES_REP_FACA ("Faça"),
    /**
     * Estrutura repetitiva: "enquanto"
     */
    RES_REP_ENQUANTO ("Enquanto"),
    
    //Palavras reservadas - Tipo
    /**
     * Identificador de matriz: "matriz"
     */
    RES_TIPO_MATRIZ ("Matriz"),
    /**
     * Identificador de tipo de variável: "inteiro"
     */
    RES_TIPO_INTEIRO ("Inteiro"),
    /**
     * Identificador de tipo de variável: "real"
     */
    RES_TIPO_REAL ("Real"),
    /**
     * Identificador de tipo de variável: "caracter"
     */
    RES_TIPO_CARACTER ("Caracter"),
    /**
     * Identificador de tipo de variável: "lógico" ou "logico"
     */
    RES_TIPO_LOGICO ("Lógico"),
    
    /**
     * Identificador de criação de função: "função", "funçao", "funcão" ou "funcao"
     */
    RES_MOD_FUNCAO ("Função"),
    /**
     * Identificador de criação de rotina: "rotina"
     */
    RES_MOD_ROTINA ("Rotina"),
    /**
     * Identificador de retorno de função: "retorna"
     */
    RES_MOD_RETORNA ("Retorna"),
    
    /**
     * Conectivo "de" (usado para criação de matriz e repetição)
     */
    RES_COMUM_DE ("De"),
    
    
    //CATEGORIA: CONSTANTES E LITERAIS
    
    /**
     * Constante Caracter: Sequências de caracteres delimitadas por aspas duplas (String literal).
     * Exemplo: "Texto de exemplo."
     */
    CONST_CARACTER ("CONSTANTE CARACTER"),
    /**
     * Constante Inteira: Sequências de números [0-9] sem ponto decimal.
     * Definida pelo Parser.
     * Exemplo: 1, 500
     */
    CONST_INTEIRA ("CONSTANTE INTEIRA"),
    /**
     * Constante Real: Sequências de números [0-9] com ponto decimal.
     * Definida pelo Parser.
     * Exemplo: 0.5 ,  700.2
     */
    CONST_REAL ("CONSTANTE REAL"),
    /**
     * Constante Lógica: palavras "verdadeiro" ou "falso".
     * Exemplo: VERDADEIRO, falso
     */
    CONST_LOGICA ("CONSTANTE LÓGICA"),
    
    //CATEGORIA: IDENTIFICADORES (Nomes de variáveis e funções declaradas pelo usuário)
    
    /**
     * Identificador de Variável: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_VARIAVEL ("IDENTIFICADOR DE VARIÁVEL"),
    /**
     * Identificador de Função: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_FUNCAO ("IDENTIFICADOR DE FUNÇÃO"),
    /**
     * Identificador de Rotina: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_ROTINA ("IDENTIFICADOR DE ROTINA"),
    
    //CATEGORIA: FUNÇÕES INTERNAS (bibliotecas internas padrão)

    /**
     * Função interna de entrada de dados: "leia"
     */
    LIB_IO_LEIA ("Leia"),
    /**
     * Função interna de saída de dados: "escreva"
     */
    LIB_IO_ESCREVA ("Escreva"),
    
    /**
     * Função interna matemática de potenciação: "pot"
     */
    LIB_MATH_POT ("Pot"),
    /**
     * Função interna matemática de radiciação (raiz quadrada): "raiz"
     */
    LIB_MATH_RAIZ ("Raiz"),
    
    
    //CATEGORIA: DELIMITADORES (símbolos de separação de tokens)
    
    /**
     * Delimitador: Dois Pontos ":"
     */
    DELIM_DOIS_PONTOS (":"),
    /**
     * Delimitador: Ponto e vírgula ";"
     */
    DELIM_PONTO_VIRGULA (";"),
    /**
     * Delimitador: Ponto "."
     */
    DELIM_PONTO ("."),
    /**
     * Delimitador: Vírgula ","
     */
    DELIM_VIRGULA (","),
    /**
     * Delimitador: Abre parênteses "("
     */
    DELIM_PARENTESES_ABRE ("("),
    /**
     * Delimitador: Fecha parênteses ")"
     */
    DELIM_PARENTESES_FECHA (")"),
    /**
     * Delimitador: Abre colchetes "["
     */
    DELIM_COLCHETES_ABRE ("["),
    /**
     * Delimitador: Fecha colchetes "]"
     */
    DELIM_COLCHETES_FECHA ("]"),
    
    
    //CATEGORIA: OPERADORES
    
    /**
     * Operador de atribuição: "&lt;-"
     */
    OP_ATRIBUICAO ("<-"),
    
    /**
     * Operador matemático: Soma "+"
     */
    OP_MAT_SOMA ("+"),
    /**
     * Operador matemático: Subtração "-"
     */
    OP_MAT_SUBTRACAO ("-"),
    /**
     * Operador matemático: Multiplicação "*"
     */
    OP_MAT_MULTIPLICACAO ("*"),
    /**
     * Operador matemático: Divisão inteira "div"
     */
    OP_MAT_DIV_INTEIRA ("Div"),
    /**
     * Operador matemático: Divisão real "/"
     */
    OP_MAT_DIV_REAL ("/"),
    /**
     * Operador matemático: Módulo de divisão inteira "mod"
     */
    OP_MAT_MOD ("Mod"),
    /**
     * Operador relacional: Maior "&gt;"
     */
    OP_REL_MAIOR (">"),
    /**
     * Operador relacional: Menor "&lt;"
     */
    OP_REL_MENOR ("<"), 
    /**
     * Operador relacional: Maior ou Igual "&gt;="
     */
    OP_REL_MAIOR_IGUAL (">="), 
    /**
     * Operador relacional: Menor ou Igual "&lt;="
     */
    OP_REL_MENOR_IGUAL ("<="),
    /**
     * Operador relacional: Igual "="
     */
    OP_REL_IGUAL ("="),
    /**
     * Operador relacional: diferente "&lt;&gt;"
     */
    OP_REL_DIFERENTE ("<>"),
    /**
     * Operador lógico E: "e"
     */
    OP_LOG_E ("E"), 
    /**
     * Operador lógico OU: "ou"
     */
    OP_LOG_OU ("Ou"),    
    /**
     * Operador lógico de inversão/negação: "não" ou "nao"
     */
    OP_LOG_NAO ("Não");
    
    private final String exibicao;
    
    private FuncaoToken(String exibicao){
        this.exibicao = exibicao;
    }
    
    @Override
    public String toString() {
        return exibicao;
    }
}
