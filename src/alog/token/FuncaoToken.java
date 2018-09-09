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
    _INVALIDO,
    
    /**
     * Pode ser identificador de variável, função ou rotina.
     * Token com mais de uma função possível (classificada pelo Parser).
     */
    _INDEF_ALFABETICO,
    /**
     * Pode ser identificador de variável, função ou rotina.
     * Token com mais de uma função possível (classificada pelo Parser).
     */
    _INDEF_ALFANUMERICO,
    /**
     * Pode ser constante inteira ou real.
     * Token com mais de uma função possível (classificada pelo Parser).
     */
    _INDEF_NUMERICO,
    
    //CATEGORIA: PALAVRAS RESERVADAS (Funções e identificadores exclusivos)
    
    /**
     * Identificador do módulo principal do algoritmo: "algoritmo"
     */
    RES_ALGORITMO,
    /**
     * Delimitador de bloco: "início" ou "inicio"
     */
    RES_BLOCO_INICIO,
    /**
     * Delimitador de bloco: "fim"
     */
    RES_BLOCO_FIM,
    
    /**
     * Estrutura condicional: "se"
     */
    RES_COND_SE,
    /**
     * Estrutura condicional: "então" ou "entao"
     */
    RES_COND_ENTAO,
    /**
     * Estrutura condicional: "senão" ou "senao"
     */
    RES_COND_SENAO,
    
    /**
     * Estrutura repetitiva: "para"
     */
    RES_REP_PARA,
    /**
     * Estrutura repetitiva: "de"
     */
    RES_REP_DE,
    /**
     * Estrutura repetitiva: "até" ou "ate"
     */
    RES_REP_ATE,
    /**
     * Estrutura repetitiva: "faca" ou "faça"
     */
    RES_REP_FACA,
    /**
     * Estrutura repetitiva: "enquanto"
     */
    RES_REP_ENQUANTO,
    
    //Palavras reservadas - 
    /**
     * Identificador de tipo de variável: "inteiro"
     */
    RES_TIPO_INTEIRO,
    /**
     * Identificador de tipo de variável: "real"
     */
    RES_TIPO_REAL,
    /**
     * Identificador de tipo de variável: "caracter"
     */
    RES_TIPO_CARACTER,
    /**
     * Identificador de tipo de variável: "lógico" ou "logico"
     */
    RES_TIPO_LOGICO,
    
    /**
     * Identificador de criação de função: "função", "funçao", "funcão" ou "funcao"
     */
    RES_MOD_FUNCAO,
    /**
     * Identificador de criação de rotina: "rotina"
     */
    RES_MOD_ROTINA,
    /**
     * Identificador de retorno de função: "retorna"
     */
    RES_MOD_RETORNA,
    
    
    //CATEGORIA: CONSTANTES E LITERAIS
    
    /**
     * Constante Caracter: Sequências de caracteres delimitadas por aspas duplas (String literal).
     * Exemplo: "Texto de exemplo."
     */
    CONST_CARACTER,
    /**
     * Constante Inteira: Sequências de números [0-9] sem ponto decimal.
     * Definida pelo Parser.
     * Exemplo: 1, 500
     */
    CONST_INTEIRA,
    /**
     * Constante Real: Sequências de números [0-9] com ponto decimal.
     * Definida pelo Parser.
     * Exemplo: 0.5 ,  700.2
     */
    CONST_REAL,
    /**
     * Constante Lógica: palavras "verdadeiro" ou "falso".
     * Exemplo: VERDADEIRO, falso
     */
    CONST_LOGICA,
    
    //CATEGORIA: IDENTIFICADORES (Nomes de variáveis e funções declaradas pelo usuário)
    
    /**
     * Identificador de Variável: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Definida pelo Parser.
     */
    IDENT_NOME_VARIAVEL,
    /**
     * Identificador de Função: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Definida pelo Parser.
     */
    IDENT_NOME_FUNCAO,
    
    //CATEGORIA: FUNÇÕES INTERNAS (bibliotecas internas padrão)

    /**
     * Função interna de entrada de dados: "leia"
     */
    LIB_IO_LEIA,
    /**
     * Função interna de saída de dados: "escreva"
     */
    LIB_IO_ESCREVA,
    
    /**
     * Função interna matemática de potenciação: "pot"
     */
    LIB_MATH_POT,
    /**
     * Função interna matemática de radiciação (raiz quadrada): "raiz"
     */
    LIB_MATH_RAIZ,
    
    
    //CATEGORIA: DELIMITADORES (símbolos de separação de tokens)
    
    /**
     * Delimitador: Dois Pontos ":"
     */
    DELIM_DOIS_PONTOS,
    /**
     * Delimitador: Ponto e vírgula ";"
     */
    DELIM_PONTO_VIRGULA,
    /**
     * Delimitador: Ponto "."
     */
    DELIM_PONTO,
    /**
     * Delimitador: Vírgula ","
     */
    DELIM_VIRGULA,
    /**
     * Delimitador: Abre parênteses "("
     */
    DELIM_PARENTESES_ABRE,
    /**
     * Delimitador: Fecha parênteses ")"
     */
    DELIM_PARENTESES_FECHA,
    /**
     * Delimitador: Abre colchetes "["
     */
    DELIM_COLCHETES_ABRE,
    /**
     * Delimitador: Fecha colchetes "]"
     */
    DELIM_COLCHETES_FECHA,
    
    
    //CATEGORIA: OPERADORES
    
    /**
     * Operador de atribuição: "<-"
     */
    OP_ATRIBUICAO,
    
    /**
     * Operador matemático: Soma "+"
     */
    OP_MAT_SOMA,
    /**
     * Operador matemático: Subtração "-"
     */
    OP_MAT_SUBTRACAO,
    /**
     * Operador matemático: Multiplicação "*"
     */
    OP_MAT_MULTIPLICACAO,
    /**
     * Operador matemático: Divisão inteira "div"
     */
    OP_MAT_DIV_INTEIRA,
    /**
     * Operador matemático: Divisão real "/"
     */
    OP_MAT_DIV_REAL,
    /**
     * Operador matemático: Módulo de divisão inteira "mod"
     */
    OP_MAT_MOD,
    /**
     * Operador relacional: Maior "&gt;"
     */
    OP_REL_MAIOR,
    /**
     * Operador relacional: Menor "&lt;"
     */
    OP_REL_MENOR, 
    /**
     * Operador relacional: Maior ou Igual "&gt;="
     */
    OP_REL_MAIOR_IGUAL, 
    /**
     * Operador relacional: Menor ou Igual "&lt;="
     */
    OP_REL_MENOR_IGUAL,
    /**
     * Operador relacional: Igual "="
     */
    OP_REL_IGUAL,
    /**
     * Operador relacional: diferente "&lt;&gt;"
     */
    OP_REL_DIFERENTE,
    /**
     * Operador lógico E: "e"
     */
    OP_LOG_E, 
    /**
     * Operador lógico OU: "ou"
     */
    OP_LOG_OU,    
    /**
     * Operador lógico de inversão/negação: "não" ou "nao"
     */
    OP_LOG_NAO, 
}
