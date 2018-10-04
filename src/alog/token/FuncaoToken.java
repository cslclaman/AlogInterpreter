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
     * Token não reconhecido.
     */
    _INVALIDO ("TOKEN INVÁLIDO", CategoriaToken._INDEFINIDO) ,
    
    /**
     * Token que contém letras (maiúsculas/minúsculas) e underline.
     * Token com mais de uma função possível (classificada pelo Parser):
     * <ul>
     * <li>{@link #IDENT_NOME_VARIAVEL} (<code>Inteiro: <u>Numero</u> ...</code>)</li>
     * <li>{@link #IDENT_NOME_FUNCAO} (<code>Função <u>CalcDobro</u> (...</code>)</li>
     * <li>{@link #IDENT_NOME_ROTINA} (<code>Rotina <u>ImprimeItem</u> ...</code>)</li>
     * <li>{@link #IDENT_NOME_ALGORITMO} (<code>Algoritmo <u>ExercicioCondicional</u> ...</code>)</li>
     * </ul>
     */
    _INDEF_ALFABETICO ("Identificador Alfabético", CategoriaToken._INDEFINIDO),
    /**
     * Token que contém letras (maiúsculas/minúsculas), underline e números.
     * Token com mais de uma função possível (classificada pelo Parser):
     * <ul>
     * <li>{@link #IDENT_NOME_VARIAVEL} (<code>Inteiro: <u>Cont01</u> ...</code>)</li>
     * <li>{@link #IDENT_NOME_FUNCAO} (<code>Função <u>Desconto7Porcento</u> (...</code>)</li>
     * <li>{@link #IDENT_NOME_ROTINA} (<code>Rotina <u>Gera2Via</u> ...</code>)</li>
     * <li>{@link #IDENT_NOME_ALGORITMO} (<code>Algoritmo <u>Exercicio02a</u> ...</code>)</li>
     * </ul>
     */
    _INDEF_ALFANUMERICO ("Identificador Alfanumérico", CategoriaToken._INDEFINIDO),
    /**
     * Palavra reservada "De" (ambíguo).
     * Token com mais de uma função possível (classificada pelo Parser):
     * <ul>
     * <li>{@link #RES_ED_TIPO_DE} (<code>Matriz [0..10] <u>De</u> Caracter...</code>)</li>
     * <li>{@link #RES_REP_DE} (<code>Para Cont <u>De</u> 1 Até 10 ...</code>)</li>
     * </ul>
     */
    _INDEFINIDO_RES_DE ("Reservado DE", CategoriaToken._INDEFINIDO),
    
    //CATEGORIA: PALAVRAS RESERVADAS (Funções e identificadores exclusivos)
    
    /**
     * Identificador do módulo principal do algoritmo: "algoritmo"
     */
    RES_ALGORITMO ("Definição Módulo principal", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Delimitador de bloco: "início" ou "inicio"
     */
    RES_BLOCO_INICIO ("Início de bloco", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Delimitador de bloco: "fim"
     */
    RES_BLOCO_FIM ("Fim de bloco", CategoriaToken.PALAVRA_RESERVADA),
    
    /**
     * Estrutura condicional: "se"
     */
    RES_COND_SE ("Condicional - Se", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Estrutura condicional: "então" ou "entao"
     */
    RES_COND_ENTAO ("Condicional - Então", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Estrutura condicional: "senão" ou "senao"
     */
    RES_COND_SENAO ("Condicional - Senão", CategoriaToken.PALAVRA_RESERVADA),
    
    /**
     * Estrutura repetitiva: "para"
     */
    RES_REP_PARA ("Repetição - Para", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Estrutura repetitiva: "de"
     */
    RES_REP_DE ("Repetição - De", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Estrutura repetitiva: "até" ou "ate"
     */
    RES_REP_ATE ("Repetição - Até", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Estrutura repetitiva: "passo"
     */
    RES_REP_PASSO ("Repetição - Passo", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Estrutura repetitiva: "faca" ou "faça"
     */
    RES_REP_FACA ("Repetição - Faça", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Estrutura repetitiva: "repita"
     */
    RES_REP_REPITA ("Repetição - Repita", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Estrutura repetitiva: "enquanto"
     */
    RES_REP_ENQUANTO ("Repetição - Enquanto", CategoriaToken.PALAVRA_RESERVADA),
    
    //Palavras reservadas - Tipo
    /**
     * Identificador de matriz: "matriz"
     */
    RES_ED_MATRIZ ("Agrupamento de Dados Matriz", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Conectivo para tipo de estrutura: "de"
     */
    RES_ED_TIPO_DE ("Conectivo para tipo de estrutura", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Identificador de tipo de variável: "inteiro"
     */
    RES_TIPO_INTEIRO ("Tipo de Variável - Inteiro", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Identificador de tipo de variável: "real"
     */
    RES_TIPO_REAL ("Tipo de Variável - Real", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Identificador de tipo de variável: "caracter"
     */
    RES_TIPO_CARACTER ("Tipo de Variável - Caracter", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Identificador de tipo de variável: "lógico" ou "logico"
     */
    RES_TIPO_LOGICO ("Tipo de Variável - Lógico", CategoriaToken.PALAVRA_RESERVADA),
    
    /**
     * Identificador de criação de função: "função", "funçao", "funcão" ou "funcao"
     */
    RES_MOD_FUNCAO ("Criação de módulo - Função", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Identificador de criação de rotina: "rotina"
     */
    RES_MOD_ROTINA ("Criação de módulo - Rotina", CategoriaToken.PALAVRA_RESERVADA),
    /**
     * Identificador de retorno de função: "retorna"
     */
    RES_MOD_RETORNA ("Retorno de valor de Função", CategoriaToken.PALAVRA_RESERVADA),
    
    //CATEGORIA: CONSTANTES E LITERAIS
    
    /**
     * Constante Caracter: Sequências de caracteres delimitadas por aspas duplas (String literal).
     * Exemplo: "Texto de exemplo."
     */
    CONST_CARACTER ("Constante Caracter", CategoriaToken.CONSTANTE),
    /**
     * Constante Inteira: Sequências de números [0-9] sem ponto decimal.
     * Exemplo: 1, 500
     */
    CONST_INTEIRA ("Constante Inteira", CategoriaToken.CONSTANTE),
    /**
     * Constante Real: Sequências de números [0-9] com ponto decimal.
     * Exemplo: 0.5 ,  700.2
     */
    CONST_REAL ("Constante Real", CategoriaToken.CONSTANTE),
    /**
     * Constante Lógica: palavras "verdadeiro" ou "falso".
     * Exemplo: VERDADEIRO, falso
     */
    CONST_LOGICA ("Constante Lógica", CategoriaToken.CONSTANTE),
    
    //CATEGORIA: IDENTIFICADORES (Nomes de variáveis e funções declaradas pelo usuário)
    
    /**
     * Identificador de Variável: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_VARIAVEL ("Identificador de Variável", CategoriaToken.IDENTIFICADOR),
    /**
     * Identificador de Função: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Vem após {@link #RES_MOD_FUNCAO}.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_FUNCAO ("Identificador de Função", CategoriaToken.IDENTIFICADOR),
    /**
     * Identificador de Rotina: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Vem após {@link #RES_MOD_ROTINA}.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_ROTINA ("Identificador de Rotina", CategoriaToken.IDENTIFICADOR),
    /**
     * Identificador do Algoritmo: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Vem após {@link #RES_ALGORITMO}.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_ALGORITMO ("Identificador do Algoritmo", CategoriaToken.IDENTIFICADOR),
    
    //CATEGORIA: FUNÇÕES INTERNAS (bibliotecas internas padrão)

    /**
     * Função interna de entrada de dados: "leia"
     */
    LIB_IO_LEIA ("Função interna E/S - Entrada de Dados", CategoriaToken.BIBLIOTECA),
    /**
     * Função interna de saída de dados: "escreva"
     */
    LIB_IO_ESCREVA ("Função interna E/S - Saída de Dados", CategoriaToken.BIBLIOTECA),
    
    /**
     * Função interna matemática de potenciação: "pot"
     */
    LIB_MATH_POT ("Função interna Matemática - Potenciação", CategoriaToken.BIBLIOTECA),
    /**
     * Função interna matemática de radiciação (raiz quadrada): "raiz"
     */
    LIB_MATH_RAIZ ("Função interna Matemática - Raiz Quadrada", CategoriaToken.BIBLIOTECA),
    
    
    //CATEGORIA: DELIMITADORES (símbolos de separação de tokens)
    
    /**
     * Delimitador: Dois Pontos ":"
     */
    DELIM_DOIS_PONTOS ("Dois Pontos", CategoriaToken.DELIMITADOR),
    /**
     * Delimitador: Ponto e vírgula ";"
     */
    DELIM_PONTO_VIRGULA ("Ponto e Vírgula", CategoriaToken.DELIMITADOR),
    /**
     * Delimitador: Ponto "."
     */
    DELIM_PONTO ("Ponto", CategoriaToken.DELIMITADOR),
    /**
     * Delimitador: Vírgula ","
     */
    DELIM_VIRGULA ("Vírgula", CategoriaToken.DELIMITADOR),
    /**
     * Delimitador: Abre parênteses "("
     */
    DELIM_PARENTESES_ABRE ("Abre Parênteses", CategoriaToken.DELIMITADOR),
    /**
     * Delimitador: Fecha parênteses ")"
     */
    DELIM_PARENTESES_FECHA ("Fecha Parênteses", CategoriaToken.DELIMITADOR),
    /**
     * Delimitador: Abre colchetes "["
     */
    DELIM_COLCHETES_ABRE ("Abre Colchetes", CategoriaToken.DELIMITADOR),
    /**
     * Delimitador: Fecha colchetes "]"
     */
    DELIM_COLCHETES_FECHA ("Fecha Colchetes", CategoriaToken.DELIMITADOR),
    
    
    //CATEGORIA: OPERADORES
    
    /**
     * Operador de atribuição: "&lt;-"
     */
    OP_ATRIBUICAO ("Operador Atribuição", CategoriaToken.OPERADOR),
    
    /**
     * Operador unário de sinal: Positivo "+"
     */
    OP_SIG_POSITIVO ("Operador Positivo", CategoriaToken.OPERADOR),
    /**
     * Operador unário de sinal: Negativo "-"
     */
    OP_SIG_NEGATIVO ("Operador Negativo", CategoriaToken.OPERADOR),
    
    /**
     * Operador matemático: Soma "+"
     */
    OP_MAT_SOMA ("Operador Soma", CategoriaToken.OPERADOR),
    /**
     * Operador matemático: Subtração "-"
     */
    OP_MAT_SUBTRACAO ("Operador Subtração", CategoriaToken.OPERADOR),
    /**
     * Operador matemático: Multiplicação "*"
     */
    OP_MAT_MULTIPLICACAO ("Operador Multiplicação", CategoriaToken.OPERADOR),
    /**
     * Operador matemático: Divisão inteira "div"
     */
    OP_MAT_DIV_INTEIRA ("Operador Divisão Inteira", CategoriaToken.OPERADOR),
    /**
     * Operador matemático: Divisão real "/"
     */
    OP_MAT_DIV_REAL ("Operador Divisão Real", CategoriaToken.OPERADOR),
    /**
     * Operador matemático: Módulo de divisão inteira "mod"
     */
    OP_MAT_MOD ("Operador Módulo de Divisão Inteira", CategoriaToken.OPERADOR),
    /**
     * Operador relacional: Maior "&gt;"
     */
    OP_REL_MAIOR ("Operador Maior que", CategoriaToken.OPERADOR),
    /**
     * Operador relacional: Menor "&lt;"
     */
    OP_REL_MENOR ("Operador Menor que", CategoriaToken.OPERADOR), 
    /**
     * Operador relacional: Maior ou Igual "&gt;="
     */
    OP_REL_MAIOR_IGUAL ("Operador Maior ou igual a", CategoriaToken.OPERADOR), 
    /**
     * Operador relacional: Menor ou Igual "&lt;="
     */
    OP_REL_MENOR_IGUAL ("Operador Menor ou igual a", CategoriaToken.OPERADOR),
    /**
     * Operador relacional: Igual "="
     */
    OP_REL_IGUAL ("Operador Igual", CategoriaToken.OPERADOR),
    /**
     * Operador relacional: diferente "&lt;&gt;"
     */
    OP_REL_DIFERENTE ("Operador Diferente", CategoriaToken.OPERADOR),
    /**
     * Operador lógico E: "e"
     */
    OP_LOG_E ("Operador E", CategoriaToken.OPERADOR), 
    /**
     * Operador lógico OU: "ou"
     */
    OP_LOG_OU ("Operador Ou", CategoriaToken.OPERADOR),
    /**
     * Operador lógico de inversão/negação: "não" ou "nao"
     */
    OP_LOG_NAO ("Operador Negação", CategoriaToken.OPERADOR),
    /**
     * Operador Intervalo de Índice Matriz: ".."
     */
    OP_ED_INTERVALO ("Intervalo Matriz", CategoriaToken.OPERADOR),
    ;
    
    
    private final String exibicao;
    private final CategoriaToken categoria;
    
    private FuncaoToken(String exibicao, CategoriaToken categoria){
        this.exibicao = exibicao;
        this.categoria = categoria;
    }
    
    @Override
    public String toString() {
        return exibicao;
    }
    
    public CategoriaToken getCategoria() {
        return categoria;
    }
}
