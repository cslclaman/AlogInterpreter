/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.geral.instrucao;

import fatec.alg.geral.token.FuncaoToken;

/**
 *
 * @author Caique
 */
public enum TipoInstrucao {
    
    /**
     * Instrução sem tipo definido (recém-inicializada).
     * Não deve conter tokens e, consequentemente, não pode ser executada.
     */
    _INDEFINIDO ("Indefinida"),
    
    /**
     * Instrução inválida, ou seja, que não pode ser executada.
     * Para ser inválida, contem tokens fora da ordem sintática esperada.
     */
    _INVALIDO ("Instrução Inválida"),
    
    /**
     * Módulo Principal do programa (equivalente ao {@code main()} de algumas linguagens).
     * O módulo deve conter:
     * <ul>
     * <li>Token {@link FuncaoToken#RES_ALGORITMO}</li>
     * <li>Token com nome identificador {@link FuncaoToken#IDENT_NOME_ALGORITMO}</li>
     * <li>Instrução {@link Bloco}</li>
     * </ul>
     */
    MODULO_PRINCIPAL ("Módulo Principal"),
    
    /**
     * Módulo de Rotina declarada.
     * O módulo deve conter:
     * <ul>
     * <li>Token {@link FuncaoToken#RES_MOD_ROTINA}</li>
     * <li>Token com nome identificador {@link FuncaoToken#IDENT_NOME_ROTINA}</li>
     * <li>(Caso necessário) Lista ordenada de parâmetros ({@link FuncaoToken#IDENT_NOME_VARIAVEL} e identificador de tipo)</li>
     * <li>Instrução {@link Bloco}</li>
     * </ul>
     */
    MODULO_ROTINA ("Módulo Rotina"),
    
    /**
     * Módulo de Função declarada.
     * O módulo deve conter:
     * <ul>
     * <li>Token identificador {@link FuncaoToken#RES_MOD_FUNCAO}</li>
     * <li>(Caso necessário) Lista ordenada de parâmetros ({@link FuncaoToken#IDENT_NOME_VARIAVEL} e identificador de tipo)</li>
     * <li>Tipo de dado retornado ({@link FuncaoToken#RES_TIPO_CARACTER} ou {@link FuncaoToken#RES_TIPO_INTEIRO}
     * ou {@link FuncaoToken#RES_TIPO_REAL} ou {@link FuncaoToken#RES_TIPO_LOGICO})</li>
     * <li>Instrução {@link Bloco}</li>
     * <li>É obrigatório que o bloco contenha pelo menos
     * uma instrução de retorno ({@link #RETORNO_FUNCAO}).</li>
     * </ul>
     */
    MODULO_FUNCAO ("Módulo Função"),
    
    /**
     * Bloco de instruções. Instrução executada como coleção de instruções internas.
     * O bloco deve conter:
     * <ul>
     * <li>Token identificador {@link FuncaoToken#RES_BLOCO_INICIO}</li>
     * <li>Lista ordenada com uma ou mais de uma {@link Instrucao}</li>
     * <li>Token identificador {@link FuncaoToken#RES_BLOCO_FIM}</li>
     * </ul>
     */
    BLOCO ("Bloco de Instruções"),
    
    /**
     * Instrução de declaração (criação) de variáveis.
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token que declare o tipo ({@link FuncaoToken#RES_TIPO_CARACTER} ou {@link FuncaoToken#RES_TIPO_INTEIRO}
     * ou {@link FuncaoToken#RES_TIPO_REAL} ou {@link FuncaoToken#RES_TIPO_LOGICO})</li>
     * <li>Lista ordenada de Tokens do tipo {@link FuncaoToken#IDENT_NOME_VARIAVEL}</li>
     * </ul>
     * Tokens opcionais (para fins de exibição, não usados pelo interpretador) são: 
     *      {@link FuncaoToken#DELIM_DOIS_PONTOS} (após declaração do tipo),
     *      {@link FuncaoToken#DELIM_VIRGULA} (entre nomes de variáveis) e 
     *      {@link FuncaoToken#DELIM_PONTO_VIRGULA} (finalização da instrução).
     */
    DECLARACAO_VARIAVEL ("Declaração de Variável"),
    
    /**
     * Instrução que possui operação de entrada de dados.
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token com chamada à rotina de entrada de dados {@link FuncaoToken#LIB_IO_LEIA}</li>
     * <li>Lista ordenada de Tokens do tipo {@link FuncaoToken#IDENT_NOME_VARIAVEL}</li>
     * </ul>
     * Tokens opcionais (para fins de exibição, não usados pelo interpretador) são: 
     *      {@link FuncaoToken#DELIM_PARENTESES_ABRE} (após chamada à rotina),
     *      {@link FuncaoToken#DELIM_VIRGULA} (entre nomes de variáveis),
     *      {@link FuncaoToken#DELIM_PARENTESES_FECHA} (após passagem de todos os parâmetros) e
     *      {@link FuncaoToken#DELIM_PONTO_VIRGULA} (finalização da instrução).
     */
    ENTRADA_DE_DADOS ("Entrada de dados"),
    
    /**
     * Instrução que possui operação de saída de dados.
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token com chamada à rotina de saída de dados {@link FuncaoToken#LIB_IO_ESCREVA}</li>
     * <li>Lista ordenada com uma ou mais de uma {@link TipoInstrucao#EXPRESSAO}</li>
     * </ul>
     * Tokens opcionais (para fins de exibição, não usados pelo interpretador) são: 
     *      {@link FuncaoToken#DELIM_PARENTESES_ABRE} (após chamada à rotina),
     *      {@link FuncaoToken#DELIM_VIRGULA} (entre expressões/parâmetros),
     *      {@link FuncaoToken#DELIM_PARENTESES_FECHA} (após passagem de todos os parâmetros) e
     *      {@link FuncaoToken#DELIM_PONTO_VIRGULA} (finalização da instrução).
     */
    SAIDA_DE_DADOS ("Saída de dados"),
    
    /**
     * Instrução que atribui o resultado de uma expressão à uma variável.
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token que identifique a variável que receberá o valor {@link FuncaoToken#IDENT_NOME_VARIAVEL}</li>
     * <li>{@link FuncaoToken#OP_ATRIBUICAO}</li>
     * <li>Valor a ser atribuído ({@link TipoInstrucao#EXPRESSAO})</li>
     * </ul>
     * Token opcional (para fins de exibição, não usado pelo interpretador):
     * {@link FuncaoToken#DELIM_PONTO_VIRGULA} (finalização da instrução).
     */
    ATRIBUICAO ("Atribuição"),
    
    /**
     * Instrução de Estrutura Condicional (Se-Então-Senão).
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token SE ({@link FuncaoToken#RES_COND_SE})</li>
     * <li>Condição booleana a testar ({@link TipoInstrucao#EXPRESSAO})</li>
     * <li>Instrução (ou {@link Bloco}) a ser executado caso a condição seja verdadeira</li>
     * </ul>
     * Pode conter (opcional), em caso de estrutura condicional composta:
     * <ul>
     * <li>Token SENÃO ({@link FuncaoToken#RES_COND_SENAO})</li>
     * <li>Instrução (ou {@link Bloco}) a ser executado caso a condição seja falsa</li>
     * </ul>
     
     * Token opcional (para fins de exibição, não usado pelo interpretador): 
     *      {@link FuncaoToken#RES_COND_ENTAO} (após a expressão de condição)
     */
    CONDICIONAL ("Estrutura Condicional"),
    
    /**
     * Instrução de Estrutura Repetitiva "Para-Faça" (número fixo de interações).
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token Para ({@link FuncaoToken#RES_REP_PARA})</li>
     * <li>Variável contadora ({@link FuncaoToken#IDENT_NOME_VARIAVEL})</li>
     * <li>Valor inicial ({@link FuncaoToken#CONST_INTEIRA} ou {@link FuncaoToken#IDENT_NOME_VARIAVEL})</li>
     * <li>Valor final ({@link FuncaoToken#CONST_INTEIRA} ou {@link FuncaoToken#IDENT_NOME_VARIAVEL})</li>
     * <li>Instrução (ou {@link Bloco}) a ser executada até que a variável
     * contadora tenha valor maior que o valor final</li>
     * </ul>
     * Tokens opcionais (para fins de exibição, não usados pelo interpretador): 
     *      {@link FuncaoToken#RES_REP_DE} (após a variável contadora),
     *      {@link FuncaoToken#RES_REP_ATE} (após o valor inicial) e
     *      {@link FuncaoToken#RES_REP_FACA} (após o valor final)
     */
    REPETICAO_PARA ("Estrutura Repetitiva Para-Faça"),
    
    /**
     * Instrução de Estrutura Repetitiva "Enquanto" (teste no início).
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token Enquanto ({@link FuncaoToken#RES_REP_ENQUANTO})</li>
     * <li>Condição booleana a testar ({@link TipoInstrucao#EXPRESSAO})</li>
     * <li>Instrução (ou {@link Bloco}) a ser executada enquanto a
     * condição for verdadeira</li>
     * </ul>
     * Token opcional (para fins de exibição, não usado pelo interpretador): 
     *      {@link FuncaoToken#RES_REP_FACA} (após a expressão de condição)
     */
    REPETICAO_ENQUANTO ("Estrutura Repetitiva Enquanto"),
    
    /**
     * Instrução de Estrutura Repetitiva "Faça-Enquanto" (teste no final).
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token Faça ({@link FuncaoToken#RES_REP_FACA})</li>
     * <li>Instrução (ou {@link Bloco}) a ser executada enquanto a
     * condição for verdadeira</li>
     * <li>Condição booleana a testar ({@link TipoInstrucao#EXPRESSAO})</li>
     * </ul>
     * Tokens opcionais (para fins de exibição, não usados pelo interpretador): 
     *      {@link FuncaoToken#RES_REP_ENQUANTO} (antes da expressão de condição) e 
     *      {@link FuncaoToken#DELIM_PONTO_VIRGULA} (após a expressão de condição)
     */
    REPETICAO_FACA ("Estrutura Repetitiva Faça-Enquanto"),
    
    /**
     * Instrução de Estrutura Repetitiva "Repita-Até" (teste no final, condição negativa).
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token Repita ({@link FuncaoToken#RES_REP_REPITA})</li>
     * <li>Instrução (ou {@link Bloco}) a ser executada enquanto a
     * condição for falsa</li>
     * <li>Condição booleana a testar ({@link TipoInstrucao#EXPRESSAO})</li>
     * </ul>
     * Tokens opcionais (para fins de exibição, não usados pelo interpretador): 
     *      {@link FuncaoToken#RES_REP_ATE} (antes da expressão de condição) e 
     *      {@link FuncaoToken#DELIM_PONTO_VIRGULA} (após a expressão de condição)
     */
    REPETICAO_REPITA ("Estrutura Repetitiva Repita-Até"),
    
    /**
     * Chamada de rotina declarada anteriormente.
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token Identificador da Rotina ({@link FuncaoToken#IDENT_NOME_ROTINA})</li>
     * </ul>
     * Caso a rotina possua parâmetros, deve conter:
     * <ul>
     * <li>Lista de expressões ({@link TipoInstrucao#EXPRESSAO}) como parâmetros</li>
     * </ul>
     * Tokens opcionais (para fins de exibição, não usados pelo interpretador) são: 
     *      {@link FuncaoToken#DELIM_PARENTESES_ABRE} (após chamada à rotina),
     *      {@link FuncaoToken#DELIM_VIRGULA} (entre expressões/parâmetros),
     *      {@link FuncaoToken#DELIM_PARENTESES_FECHA} (após passagem de todos os parâmetros) e
     *      {@link FuncaoToken#DELIM_PONTO_VIRGULA} (finalização da instrução).
     */
    CHAMADA_ROTINA ("Chamada a Rotina Declarada"),
    
    /**
     * Expressão calculável/executável.
     * De acordo com seus operandos e seu retorno, pode ser:
     * <ul>
     * <li>ARITMÉTICA: Operandos numéricos, resultado numérico (Real ou Inteiro)</li>
     * <li>RELACIONAL: Operandos numéricos ou literais, resultado lógico</li>
     * <li>LÓGICA: Operandos lógicos, resultado lógico</li>
     * </ul>
     * Deve conter:
     * <ul>
     * <li><b>Ou</b> Operando (Constante, Variável ou Chamada de Função)</li>
     * <li><b>Ou</b> Token {@link FuncaoToken#DELIM_PARENTESES_ABRE}, Instrução {@link #EXPRESSAO} e Token {@link FuncaoToken#DELIM_PARENTESES_FECHA}</li>
     * <li><b>Ou</b> Instrução {@link #EXPRESSAO}, Operador e Instrução {@link #EXPRESSAO}</li>
     * </ul>
     */
    EXPRESSAO ("Expressão"),
    
    /**
     * Valor de retorno de uma função declarada.
     * Deve conter (obrigatoriamente):
     * <ul>
     * <li>Token Identificador {@link FuncaoToken#RES_MOD_RETORNA})</li>
     * <li>Instrução {@link #EXPRESSAO} com valor a ser retornado</li>
     * </ul>
     * Token opcional (para fins de exibição, não usado pelo interpretador): 
     *      {@link FuncaoToken#DELIM_PONTO_VIRGULA} (para encerrar a instrução).
     */
    RETORNO_FUNCAO ("Retorno de Função"),
    ;
    
    private final String exibicao;
    
    private TipoInstrucao(String exibicao){
        this.exibicao = exibicao;
    }
    
    /**
     * Retorna texto de exibição
     * @return
     */
    @Override
    public String toString() {
        return exibicao;
    }
}
