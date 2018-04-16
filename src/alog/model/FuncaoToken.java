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
    
    //CATEGORIA: INDEFINIDOS (Tokens a serem categorizados ou inválidos)

    //Token não reconhecido
    _INVALIDO,
    //Tokens com mais de uma função (Parser classifica)
    _INDEF_ALFABETICO,          //Pode ser identificador de variável, função ou rotina
    _INDEF_ALFANUMERICO,        //Pode ser identificador de variável, função ou rotina
    _INDEF_NUMERICO,            //Pode ser constante inteira ou real
    
    //CATEGORIA: PALAVRAS RESERVADAS (Funções e identificadores exclusivos)
    
    //Palavras reservadas - início e fim de bloco
    RES_BLOCO_INICIO,           //"início" ou "inicio"
    RES_BLOCO_FIM,              //"fim"
    //Palavras reservadas - estrutura condicional
    RES_COND_SE,                //"se"
    RES_COND_ENTAO,             //"então" ou "entao"
    RES_COND_SENAO,             //"senão" ou "senao"
    //Palavras reservadas - Identificadores de tipo de variável
    RES_TIPO_INTEIRO,           //"inteiro"
    RES_TIPO_REAL,              //"real"
    RES_TIPO_CARACTER,          //"caracter"
    
    //CATEGORIA: CONSTANTES E LITERAIS
    
    //Constantes
    CONST_CARACTER,             //Sequências de caracteres delimitadas por aspas duplas (String literal)
    CONST_INTEIRA,              //Sequências de números [0-9] sem ponto decimal
    CONST_REAL,                 //Sequências de números [0-9] com ponto decimal
    
    //CATEGORIA: IDENTIFICADORES (Nomes de variáveis e funções declaradas pelo usuário)
    
    //Identificadores de nome
    IDENT_NOME_VARIAVEL,        //Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines
    
    //CATEGORIA: FUNÇÕES INTERNAS (bibliotecas internas padrão)

    //Funções internas - Entrada/Saída
    LIB_IO_LEIA,                //"leia"
    LIB_IO_ESCREVA,             //"escreva"
    //Funções internas - Potência/Raiz
    LIB_MATH_POT,               //"pot"
    LIB_MATH_RAIZ,              //"raiz"
    
    //CATEGORIA: DELIMITADORES (símbolos de separação de tokens)
    
    //Delimitadores
    DELIM_DOIS_PONTOS,          //":"
    DELIM_PONTO_VIRGULA,        //";"
    DELIM_PONTO,                //"."
    DELIM_VIRGULA,              //","
    DELIM_PARENTESES_ABRE,      //"("
    DELIM_PARENTESES_FECHA,     //")"
    
    //CATEGORIA: OPERADORES
    
    //Operador de atribuição
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
