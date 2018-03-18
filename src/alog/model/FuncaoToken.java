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
    //Palavras reservadas - início e fim de bloco
    RES_BLOCO_INICIO,
    RES_BLOCO_FIM,
    //Identificadores de variável
    IDENT_TIPO_INTEIRO,
    IDENT_TIPO_REAL,
    IDENT_TIPO_CARACTER,
    IDENT_NOME_VARIAVEL,
    //Delimitadores
    DELIM_DOIS_PONTOS,
    DELIM_PONTO_VIRGULA,
    DELIM_PONTO,
    DELIM_VIRGULA,
    DELIM_PARENTESES_ABRE,
    DELIM_PARENTESES_FECHA,
    //Constantes
    CONST_CARACTER,
    CONST_INTEIRA,
    CONST_REAL,
    //Funções internas - Entrada/Saída
    LIB_IO_LEIA,
    LIB_IO_ESCREVA,
    //Operadores - atribuição
    OP_ATRIBUICAO,
    //Operadores matemáticos
    OP_SOMA,
    OP_SUBTRACAO,
    OP_MULTIPLICACAO,
    OP_DIV_INTEIRA,
    OP_DIV_REAL,
    OP_MOD,
    //Operadores lógicos
    OP_MAIOR,
    OP_MENOR,
    OP_IGUAL,
    OP_DIFERENTE,
    OP_MAIOR_IGUAL,
    OP_MENOR_IGUAL
    
}
