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
    _INVALIDO ("TOKEN INVÁLIDO") ,
    
    /**
     * Token que contém letras (maiúsculas/minúsculas) e underline.
     * Token com mais de uma função possível (classificada pelo Parser):
     * <ul>
     * <li>{@link #IDENT_NOME_VARIAVEL} (<code>Inteiro: <u>Numero</u> ...</code>)</li>
     * <li>{@link #IDENT_NOME_FUNCAO} (<code>Função <u>CalcDobro</u> (...</code>)</li>
     * <li>{@link #IDENT_NOME_ROTINA} (<code>Rotina <u>ImprimeItem</u> ...</code>)</li>
     * </ul>
     */
    _INDEF_ALFABETICO ("Identificador Alfabético"),
    /**
     * Token que contém letras (maiúsculas/minúsculas), underline e números.
     * Token com mais de uma função possível (classificada pelo Parser):
     * <ul>
     * <li>{@link #IDENT_NOME_VARIAVEL} (<code>Inteiro: <u>Cont01</u> ...</code>)</li>
     * <li>{@link #IDENT_NOME_FUNCAO} (<code>Função <u>Desconto7Porcento</u> (...</code>)</li>
     * <li>{@link #IDENT_NOME_ROTINA} (<code>Rotina <u>Gera2Via</u> ...</code>)</li>
     * </ul>
     */
    _INDEF_ALFANUMERICO ("Identificador Alfanumérico"),
    
    //CATEGORIA: PALAVRAS RESERVADAS (Funções e identificadores exclusivos)
    
    /**
     * Palavra reservada "De" (ambíguo).
     * Token com mais de uma função possível (classificada pelo Parser):
     * <ul>
     * <li>{@link #RES_ED_TIPO_DE} (<code>Matriz [0..10] <u>De</u> Caracter...</code>)</li>
     * <li>{@link #RES_REP_DE} (<code>Para Cont <u>De</u> 1 Até 10 ...</code>)</li>
     * </ul>
     */
    RES_COMUM_DE ("Reservado DE"),
    
    /**
     * Identificador do módulo principal do algoritmo: "algoritmo"
     */
    RES_ALGORITMO ("Definição Módulo principal"),
    /**
     * Delimitador de bloco: "início" ou "inicio"
     */
    RES_BLOCO_INICIO ("Início de bloco"),
    /**
     * Delimitador de bloco: "fim"
     */
    RES_BLOCO_FIM ("Fim de bloco"),
    
    /**
     * Estrutura condicional: "se"
     */
    RES_COND_SE ("Condicional - Se"),
    /**
     * Estrutura condicional: "então" ou "entao"
     */
    RES_COND_ENTAO ("Condicional - Então"),
    /**
     * Estrutura condicional: "senão" ou "senao"
     */
    RES_COND_SENAO ("Condicional - Senão"),
    
    /**
     * Estrutura repetitiva: "para"
     */
    RES_REP_PARA ("Repetição - Para"),
    /**
     * Estrutura repetitiva: "de"
     */
    RES_REP_DE ("Repetição - De"),
    /**
     * Estrutura repetitiva: "até" ou "ate"
     */
    RES_REP_ATE ("Repetição - Até"),
    /**
     * Estrutura repetitiva: "passo"
     */
    RES_REP_PASSO ("Repetição - Passo"),
    /**
     * Estrutura repetitiva: "faca" ou "faça"
     */
    RES_REP_FACA ("Repetição - Faça"),
    /**
     * Estrutura repetitiva: "repita"
     */
    RES_REP_REPITA ("Repetição - Repita"),
    /**
     * Estrutura repetitiva: "enquanto"
     */
    RES_REP_ENQUANTO ("Repetição - Enquanto"),
    
    //Palavras reservadas - Tipo
    /**
     * Identificador de matriz: "matriz"
     */
    RES_ED_MATRIZ ("Agrupamento de Dados Matriz"),
    /**
     * Conectivo para tipo de estrutura: "de"
     */
    RES_ED_TIPO_DE ("Conectivo para tipo de estrutura"),
    /**
     * Identificador de tipo de variável: "inteiro"
     */
    RES_TIPO_INTEIRO ("Tipo de Variável - Inteiro"),
    /**
     * Identificador de tipo de variável: "real"
     */
    RES_TIPO_REAL ("Tipo de Variável - Real"),
    /**
     * Identificador de tipo de variável: "caracter"
     */
    RES_TIPO_CARACTER ("Tipo de Variável - Caracter"),
    /**
     * Identificador de tipo de variável: "lógico" ou "logico"
     */
    RES_TIPO_LOGICO ("Tipo de Variável - Lógico"),
    
    /**
     * Identificador de criação de função: "função", "funçao", "funcão" ou "funcao"
     */
    RES_MOD_FUNCAO ("Criação de módulo - Função"),
    /**
     * Identificador de criação de rotina: "rotina"
     */
    RES_MOD_ROTINA ("Criação de módulo - Rotina"),
    /**
     * Identificador de retorno de função: "retorna"
     */
    RES_MOD_RETORNA ("Retorno de valor de Função"),
    
    //CATEGORIA: CONSTANTES E LITERAIS
    
    /**
     * Constante Caracter: Sequências de caracteres delimitadas por aspas duplas (String literal).
     * Exemplo: "Texto de exemplo."
     */
    CONST_CARACTER ("Constante Caracter"),
    /**
     * Constante Inteira: Sequências de números [0-9] sem ponto decimal.
     * Definida pelo Parser.
     * Exemplo: 1, 500
     */
    CONST_INTEIRA ("Constante Inteira"),
    /**
     * Constante Real: Sequências de números [0-9] com ponto decimal.
     * Definida pelo Parser.
     * Exemplo: 0.5 ,  700.2
     */
    CONST_REAL ("Constante Real"),
    /**
     * Constante Lógica: palavras "verdadeiro" ou "falso".
     * Exemplo: VERDADEIRO, falso
     */
    CONST_LOGICA ("Constante Lógica"),
    
    //CATEGORIA: IDENTIFICADORES (Nomes de variáveis e funções declaradas pelo usuário)
    
    /**
     * Identificador de Variável: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_VARIAVEL ("Identificador de Variável"),
    /**
     * Identificador de Função: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Vem após {@link #RES_MOD_FUNCAO}.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_FUNCAO ("Identificador de Função"),
    /**
     * Identificador de Rotina: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Vem após {@link #RES_MOD_ROTINA}.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_ROTINA ("Identificador de Rotina"),
    /**
     * Identificador do Algoritmo: Sequência de caracteres iniciada em [a-zA-Z], contendo letras, números, underlines.
     * Vem após {@link #RES_ALGORITMO}.
     * Definida pelo Parser (scanner define como {@link #_INDEF_ALFABETICO} ou {@link #_INDEF_ALFANUMERICO}.
     */
    IDENT_NOME_ALGORITMO ("Identificador do Algoritmo"),
    
    //CATEGORIA: FUNÇÕES INTERNAS (bibliotecas internas padrão)

    /**
     * Função interna de entrada de dados: "leia"
     */
    LIB_IO_LEIA ("Função interna E/S - Entrada de Dados"),
    /**
     * Função interna de saída de dados: "escreva"
     */
    LIB_IO_ESCREVA ("Função interna E/S - Saída de Dados"),
    
    /**
     * Função interna matemática de potenciação: "pot"
     */
    LIB_MATH_POT ("Função interna Matemática - Potenciação"),
    /**
     * Função interna matemática de radiciação (raiz quadrada): "raiz"
     */
    LIB_MATH_RAIZ ("Função interna Matemática - Raiz Quadrada"),
    
    
    //CATEGORIA: DELIMITADORES (símbolos de separação de tokens)
    
    /**
     * Delimitador: Dois Pontos ":"
     */
    DELIM_DOIS_PONTOS ("Dois Pontos"),
    /**
     * Delimitador: Ponto e vírgula ";"
     */
    DELIM_PONTO_VIRGULA ("Ponto e Vírgula"),
    /**
     * Delimitador: Ponto "."
     */
    DELIM_PONTO ("Ponto"),
    /**
     * Delimitador: Vírgula ","
     */
    DELIM_VIRGULA ("Vírgula"),
    /**
     * Delimitador: Abre parênteses "("
     */
    DELIM_PARENTESES_ABRE ("Abre Parênteses"),
    /**
     * Delimitador: Fecha parênteses ")"
     */
    DELIM_PARENTESES_FECHA ("Fecha Parênteses"),
    /**
     * Delimitador: Abre colchetes "["
     */
    DELIM_COLCHETES_ABRE ("Abre Colchetes"),
    /**
     * Delimitador: Fecha colchetes "]"
     */
    DELIM_COLCHETES_FECHA ("Fecha Colchetes"),
    
    
    //CATEGORIA: OPERADORES
    
    /**
     * Operador de atribuição: "&lt;-"
     */
    OP_ATRIBUICAO ("Operador Atribuição"),
    
    /**
     * Operador matemático: Soma "+"
     */
    OP_MAT_SOMA ("Operador Soma"),
    /**
     * Operador matemático: Subtração "-"
     */
    OP_MAT_SUBTRACAO ("Operador Subtração"),
    /**
     * Operador matemático: Multiplicação "*"
     */
    OP_MAT_MULTIPLICACAO ("Operador Multiplicação"),
    /**
     * Operador matemático: Divisão inteira "div"
     */
    OP_MAT_DIV_INTEIRA ("Operador Divisão Inteira"),
    /**
     * Operador matemático: Divisão real "/"
     */
    OP_MAT_DIV_REAL ("Operador Divisão Real"),
    /**
     * Operador matemático: Módulo de divisão inteira "mod"
     */
    OP_MAT_MOD ("Operador Módulo de Divisão Inteira"),
    /**
     * Operador relacional: Maior "&gt;"
     */
    OP_REL_MAIOR ("Operador Maior que"),
    /**
     * Operador relacional: Menor "&lt;"
     */
    OP_REL_MENOR ("Operador Menor que"), 
    /**
     * Operador relacional: Maior ou Igual "&gt;="
     */
    OP_REL_MAIOR_IGUAL ("Operador Maior ou igual a"), 
    /**
     * Operador relacional: Menor ou Igual "&lt;="
     */
    OP_REL_MENOR_IGUAL ("Operador Menor ou igual a"),
    /**
     * Operador relacional: Igual "="
     */
    OP_REL_IGUAL ("Operador Igual"),
    /**
     * Operador relacional: diferente "&lt;&gt;"
     */
    OP_REL_DIFERENTE ("Operador Diferente"),
    /**
     * Operador lógico E: "e"
     */
    OP_LOG_E ("Operador E"), 
    /**
     * Operador lógico OU: "ou"
     */
    OP_LOG_OU ("Operador Ou"),    
    /**
     * Operador lógico de inversão/negação: "não" ou "nao"
     */
    OP_LOG_NAO ("Operador Negação"),
    /**
     * Operador Intervalo de Índice Matriz: ".."
     */
    OP_ED_INTERVALO ("Intervalo Matriz"),
    ;
    
    
    private final String exibicao;
    
    private FuncaoToken(String exibicao){
        this.exibicao = exibicao;
    }
    
    @Override
    public String toString() {
        return exibicao;
    }
}
