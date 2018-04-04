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
public enum FuncaoToken {
    _INDEFINIDO,
    _INDEF_ALFABETICO,
    _INDEF_ALFANUMERICO,
    _INDEF_NUMERICO,
    //Palavras reservadas - início e fim de bloco
    RES_BLOCO_INICIO,           //"início" ou "inicio"
    RES_BLOCO_FIM,              //"fim"
    //Palavras reservadas - estrutura condicional
    RES_COND_SE,                //"se"
    RES_COND_ENTAO,             //"então" ou "entao"
    RES_COND_SENAO,             //"senão" ou "senao"
    //Identificadores de variável
    IDENT_TIPO_INTEIRO,         //"inteiro"
    IDENT_TIPO_REAL,            //"real"
    IDENT_TIPO_CARACTER,        //"caracter"
    IDENT_NOME_VARIAVEL,        //Sequência de caracteres iniciada em [A-Z], contendo letras, números, underlines
    //Delimitadores
    DELIM_DOIS_PONTOS,          //":"
    DELIM_PONTO_VIRGULA,        //";"
    DELIM_PONTO,                //"."
    DELIM_VIRGULA,              //","
    DELIM_PARENTESES_ABRE,      //"("
    DELIM_PARENTESES_FECHA,     //")"
    //Constantes
    CONST_CARACTER,             //Sequências de caracteres delimitadas por aspas duplas
    CONST_INTEIRA,              //Sequências de números [0-9] sem ponto decimal
    CONST_REAL,                 //Sequências de números [0-9] com ponto decimal
    //Funções internas - Entrada/Saída
    LIB_IO_LEIA,                //"leia"
    LIB_IO_ESCREVA,             //"escreva"
    //Funções internas - Potência/Raiz
    LIB_MATH_POT,               //"pot"
    LIB_MATH_RAIZ,              //"raiz"
    //Operadores - atribuição
    OP_ATRIBUICAO,              //"<-"
    //Operadores matemáticos
    OP_SOMA,                    //"+"
    OP_SUBTRACAO,               //"-"
    OP_MULTIPLICACAO,           //"*"
    OP_DIV_INTEIRA,             //"div"
    OP_DIV_REAL,                //"/"
    OP_MOD,                     //"mod"
    //Operadores relacionais
    OP_MAIOR,                   //">"
    OP_MENOR,                   //"<"
    OP_IGUAL,                   //"="
    OP_DIFERENTE,               //"<>"
    OP_MAIOR_IGUAL,             //">="
    OP_MENOR_IGUAL,             //"<="
    //Operadores lógicos
    OP_E,                       //"e"
    OP_OU,                      //"ou"
    
}
