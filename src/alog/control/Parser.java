/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Bloco;
import alog.model.Instrucao;
import alog.token.FuncaoToken;
import alog.model.TipoInstrucao;
import alog.model.TipoVariavel;
import alog.token.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Analisador sintático que verifica uma sequência de tokens e retorna expressões executáveis.
 * @author Caique
 */
public class Parser {
    private class ErroSintatico {
        Token token;
        String erro;

        public ErroSintatico(Token token, String erro) {
            this.token = token;
            this.erro = erro;
        }
        
        @Override
        public String toString(){
            return String.format(
                    "Linha %d, Coluna %d: %s",
                    token.getLinha() + 1, token.getColuna() + 1, erro);
        }
    }
    
    private List<Token> tokens;
    private Map<String, TipoVariavel> variaveis;
    
    private LinkedList<FuncaoToken> funcoesEsperadas;
    private LinkedList<TipoInstrucao> pilhaTiposInstrucoes;
    private TipoInstrucao tipoInstrucaoAtual;
    private LinkedList<ErroSintatico> erros;
    private int pos;
    private boolean fimAtingido;
    
    public Parser (List<Token> tokens){
        this.tokens = tokens;
        
        variaveis = new HashMap<>();
        erros = new LinkedList<>();
        funcoesEsperadas = new LinkedList<>();
        pilhaTiposInstrucoes = new LinkedList<>();
        
        pos = 0;
        fimAtingido = false;
        
        // Caso modularizado:
        /* 
        // Espera por declaração de função
        funcoesEsperadas.add(FuncaoToken.RES_MOD_FUNCAO);
        // Espera por declaração de rotina
        funcoesEsperadas.add(FuncaoToken.RES_MOD_ROTINA);
        */
        
        // Caso não modularizado:
        funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
        tipoInstrucaoAtual = TipoInstrucao._INDEFINIDO;
    }
    
    public boolean existeProxima(){
        return pos < tokens.size();
    }
    
    public Instrucao proxima(){
        Instrucao instrucao;
        
        LinkedList<Token> parentesesAbre = new LinkedList<>();
        LinkedList<Token> parentesesFecha = new LinkedList<>();
        
        boolean go = true;
        
        while (go && pos < tokens.size()){
            Token token = tokens.get(pos);
            if (!funcaoValida(token)){
                break;
            }
            switch(token.getFuncaoToken()){
                //Ao iniciar um bloco:
                case RES_BLOCO_INICIO:
                    instrucao = new Bloco();
                    
                    pilhaTiposInstrucoes.push(tipoInstrucaoAtual);
                    tipoInstrucaoAtual = instrucao.getTipo();
                    
                    if (!tipoValido(instrucao)){
                        break;
                    }
                    instrucao.insereToken(token);
                    
                    Parser parserInterno = new Parser(tokens);
                    parserInterno.variaveis = variaveis;
                    parserInterno.pos = pos++;
                    parserInterno.pilhaTiposInstrucoes = pilhaTiposInstrucoes;
                    parserInterno.tipoInstrucaoAtual = tipoInstrucaoAtual;
                    
                    while (parserInterno.existeProxima() && !parserInterno.fimAtingido){
                        Instrucao proxima = parserInterno.proxima();
                        if (!((Bloco)instrucao).insereInstrucao(proxima)){
                            erros.add(parserInterno.erros.getLast());
                        }
                    }
                    
                    if (!parserInterno.existeProxima() && !parserInterno.fimAtingido){
                        erros.add(new ErroSintatico(
                                ((Bloco)instrucao).getInicio(),
                                "Bloco não fechado corretamente"));
                    }
                    
                    pos = parserInterno.pos;
                    token = tokens.get(pos++);
                    variaveis = parserInterno.variaveis;
                    
                    instrucao.insereToken(token);
                    
                    funcoesEsperadas.clear();
                    
                    tipoInstrucaoAtual = pilhaTiposInstrucoes.pop();
                    switch (tipoInstrucaoAtual){
                        //Caso o bloco seja referente a um módulo:
                        case MODULO_FUNCAO:
                        case MODULO_ROTINA:
                            /*funcoesEsperadas.add(FuncaoToken.RES_MOD_FUNCAO);
                            funcoesEsperadas.add(FuncaoToken.RES_MOD_ROTINA);
                            funcoesEsperadas.add(FuncaoToken.RES_ALGORITMO);*/
                            break;
                        //Caso seja um módulo principal:
                        case MODULO_PRINCIPAL:
                            /*funcoesEsperadas.add(FuncaoToken.RES_MOD_FUNCAO);
                            funcoesEsperadas.add(FuncaoToken.RES_MOD_ROTINA);*/
                            break;
                        //Caso o bloco esteja dentro de outro bloco (por alguma razão obscura):
                        case BLOCO:
                        //Caso o bloco seja um algoritmo básico (sem identificador "Algoritmo"
                        case _INDEFINIDO:
                            //Declarações de variáveis
                            funcoesEsperadas.add(FuncaoToken.RES_TIPO_INTEIRO);
                            funcoesEsperadas.add(FuncaoToken.RES_TIPO_REAL);
                            funcoesEsperadas.add(FuncaoToken.RES_TIPO_CARACTER);
                            //Atribuição ou Chamada de rotina
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            //Condicional
                            funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                            //Repetições
                            funcoesEsperadas.add(FuncaoToken.RES_REP_PARA);
                            funcoesEsperadas.add(FuncaoToken.RES_REP_ENQUANTO);
                            funcoesEsperadas.add(FuncaoToken.RES_REP_FACA);
                            //Entrada/Saída
                            funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                            funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                            break;
                        default:
                            erros.add(new ErroSintatico(
                                ((Bloco)instrucao).getInicio(),
                                "Bloco não fechado corretamente"));
                    }
                    go = false;
                    break;
                
                case RES_BLOCO_FIM:
                    fimAtingido = true;
                    funcoesEsperadas.clear();
                    
                    go = false;
                    break;
                    
                //MODO: CRIAÇÃO DE VARIÁVEIS
                case RES_TIPO_CARACTER:
                case RES_TIPO_INTEIRO:
                case RES_TIPO_REAL:
                    instrucao.setTipo(TipoInstrucao.DECLARACAO_VARIAVEL);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_DOIS_PONTOS);
                    add = true;
                    break;
                    
                case DELIM_DOIS_PONTOS:
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    add = false;
                    break;
                    
                
                
                //MODO: ENTRADA DE DADOS
                case LIB_IO_LEIA:
                    instrucao.setTipo(TipoInstrucao.ENTRADA_DE_DADOS);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    add = true;
                    break;
                    
                //MODO: SAÍDA DE DADOS
                case LIB_IO_ESCREVA:
                    instrucao.setTipo(TipoInstrucao.SAIDA_DE_DADOS);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    add = true;
                    break;
                
                //MODO: ATRIBUIÇÃO
                case OP_ATRIBUICAO:
                    instrucao.setTipo(TipoInstrucao.ATRIBUICAO);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    add = true;
                    break;
                    
                //MODO: OPERAÇÃO ARITMÉTICA
                case OP_MAT_SOMA:
                case OP_MAT_SUBTRACAO:
                case OP_MAT_MULTIPLICACAO:
                case OP_MAT_DIV_INTEIRA:
                case OP_MAT_DIV_REAL:
                case OP_MAT_MOD:
                    if (instrucao.getTipo() == TipoInstrucao.ATRIBUICAO){
                        instrucao.setTipo(TipoInstrucao.OPERACAO_ARITMETICA);
                    }
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    add = true;
                    break;
                
                case LIB_MATH_POT:
                case LIB_MATH_RAIZ:
                    instrucao.setTipo(TipoInstrucao.CHAMADA_FUNCAO);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    add = true;
                    break;
                    
                //MODO: CONDICIONAL
                case RES_COND_SE:
                    instrucao.setTipo(TipoInstrucao.OPERACAO_LOGICA);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    add = true;
                    break;
                    
                case OP_REL_MAIOR:
                case OP_REL_MAIOR_IGUAL:
                case OP_REL_MENOR:
                case OP_REL_MENOR_IGUAL:
                case OP_REL_IGUAL:
                case OP_REL_DIFERENTE:
                case OP_LOG_E:
                case OP_LOG_OU:
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    add = true;
                    break;
                    
                case RES_COND_ENTAO:
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SENAO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    
                    add = false;
                    go = false;
                    break;
                    
                case RES_COND_SENAO:
                    instrucao.setTipo(TipoInstrucao.OPERACAO_LOGICA);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SENAO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    
                    add = true;
                    go = false;
                    break;
                    
                //DIVERSOS MODOS
                case DELIM_PARENTESES_ABRE:
                    parentesesAbre.push(token);
                    switch (instrucao.getTipo()){
                        case ENTRADA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            add = false;
                            break;
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                            funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                            add = false;
                            break;
                        case ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                            add = true;
                            break;
                        case CHAMADA_FUNCAO:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            add = true;
                            break;
                        case OPERACAO_LOGICA:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                            funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                            add = true;
                            break;
                    }
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    parentesesFecha.offer(token);
                    switch (instrucao.getTipo()){
                        case ENTRADA_DE_DADOS:
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            add = false;
                            break;
                        case CHAMADA_FUNCAO:
                            instrucao.setTipo(TipoInstrucao.OPERACAO_ARITMETICA);
                        case ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                            add = true;
                            break;
                    }
                    break;
                    
                case DELIM_VIRGULA:
                    switch (instrucao.getTipo()){
                        case DECLARACAO_VARIAVEL:
                        case ENTRADA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            add = false;
                            break;
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                            add = true;
                            break;
                        case CHAMADA_FUNCAO:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            add = true;
                            break;
                    }
                    break;
                
                case CONST_CARACTER:
                    switch (instrucao.getTipo()){
                        case SAIDA_DE_DADOS:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            add = true;
                            break;
                        case ATRIBUICAO:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            add = true;
                            break;
                        case OPERACAO_LOGICA:
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_DIFERENTE);
                            funcoesEsperadas.add(FuncaoToken.OP_LOG_E);
                            funcoesEsperadas.add(FuncaoToken.OP_LOG_OU);
                            funcoesEsperadas.add(FuncaoToken.RES_COND_ENTAO);
                            add = true;
                    }
                    break;
                    
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    switch (instrucao.getTipo()){
                        case DECLARACAO_VARIAVEL:
                            char inicial = token.getPalavra().charAt(0);
                            if (inicial >= '0' && inicial <= '9'){
                                erros.add(new ErroSintatico(token, "Identificador de variável não pode começar com número"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {
                                variaveis.add(token.getPalavra());
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                                add = true;
                            }
                            break;
                        case ENTRADA_DE_DADOS:
                        case SAIDA_DE_DADOS:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Variável \"" + token.getPalavra() + "\" não declarada"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                                add = true;
                            //}
                            break;
                        case ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Variável \"" + token.getPalavra() + "\" não declarada"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                                add = true;
                            //}
                            break;
                        case CHAMADA_FUNCAO:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Variável \"" + token.getPalavra() + "\" não declarada"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                                funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                                funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                                add = true;
                            //}
                            break;
                        case OPERACAO_LOGICA:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Variável \"" + token.getPalavra() + "\" não declarada"));
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR_IGUAL);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR_IGUAL);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_IGUAL);
                                funcoesEsperadas.add(FuncaoToken.OP_REL_DIFERENTE);
                                funcoesEsperadas.add(FuncaoToken.OP_LOG_E);
                                funcoesEsperadas.add(FuncaoToken.OP_LOG_OU);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                                funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                                funcoesEsperadas.add(FuncaoToken.RES_COND_ENTAO);
                                add = true;
                            //}
                            break;
                            
                        case _INDEFINIDO:
                        default:
                            /*if (!variaveis.contains(token.getPalavra())){
                                erros.add(new ErroSintatico(token, "Comando, variável ou função não identificada: " + token.getPalavra()));
                                
                                erro = true;
                                add = false;
                                go = false;
                            } else {*/
                                token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);

                                funcoesEsperadas.clear();
                                funcoesEsperadas.add(FuncaoToken.OP_ATRIBUICAO);
                                add = true;
                            //}
                            break;
                    }
                    break;
                    
                case _INDEF_NUMERICO:
                    funcoesEsperadas.clear();
                            
                    Token lastToken = instrucao.getTokenAt(instrucao.getNumTokens() - 1);
                    if (lastToken.getFuncaoToken() == FuncaoToken.CONST_REAL){
                        lastToken.atualizaPalavra(token.getPalavra());
                        instrucao.setTokenAt(instrucao.getNumTokens() - 1, lastToken);
                        add = false;
                    } else {
                        token.setFuncaoToken(FuncaoToken.CONST_INTEIRA);
                        add = true;
                        funcoesEsperadas.add(FuncaoToken.DELIM_PONTO);
                    }
                    
                    switch (instrucao.getTipo()){
                        case ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                            funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_SOMA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_SUBTRACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_DIV_REAL);
                            funcoesEsperadas.add(FuncaoToken.OP_MAT_MOD);
                            break;
                        case CHAMADA_FUNCAO:
                            funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                            funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                            break;
                        case OPERACAO_LOGICA:
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MAIOR_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_MENOR_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_IGUAL);
                            funcoesEsperadas.add(FuncaoToken.OP_REL_DIFERENTE);
                            funcoesEsperadas.add(FuncaoToken.OP_LOG_E);
                            funcoesEsperadas.add(FuncaoToken.OP_LOG_OU);
                            funcoesEsperadas.add(FuncaoToken.RES_COND_ENTAO);
                            add = true;
                            break;
                    }
                    break;
                    
                case DELIM_PONTO:
                    switch (instrucao.getTipo()){
                        case CHAMADA_FUNCAO:
                        case ATRIBUICAO:
                        case OPERACAO_ARITMETICA:
                        case OPERACAO_LOGICA:
                            lastToken = instrucao.getTokenAt(instrucao.getNumTokens() - 1);
                            lastToken.atualizaPalavra(token.getPalavra());
                            lastToken.setFuncaoToken(FuncaoToken.CONST_REAL);
                            instrucao.setTokenAt(instrucao.getNumTokens() - 1, lastToken);
                            
                            funcoesEsperadas.clear();
                            funcoesEsperadas.add(FuncaoToken._INDEF_NUMERICO);
                            add = false;
                            break;
                    }
                    break;
                
                case DELIM_PONTO_VIRGULA:
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_INTEIRO);
                    funcoesEsperadas.add(FuncaoToken.RES_TIPO_REAL);
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_FIM);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_LEIA);
                    funcoesEsperadas.add(FuncaoToken.LIB_IO_ESCREVA);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SE);
                    funcoesEsperadas.add(FuncaoToken.RES_COND_SENAO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    
                    add = false;
                    go = false;
                    break;
                    
                case _INVALIDO:
                default:
                    add = false;
                    go = false;
                    break;
            }
            
            instrucao.atualizaTexto(token.getPalavra());
            if (add){
                instrucao.addToken(token);
            }
        }
        
        while (!parentesesAbre.isEmpty() && !parentesesFecha.isEmpty()){
            parentesesAbre.pop();
            parentesesFecha.pop();
        }
        
        while (!parentesesAbre.isEmpty()){
            erros.add(new ErroSintatico(parentesesAbre.pop(), "Parêntese não fechado"));
            erro = true;
        }
        
        while (!parentesesFecha.isEmpty()){
            erros.add(new ErroSintatico(parentesesFecha.pop(), "Parêntese fechado sem necessidade"));
            erro = true;
        }
        
        if (bloco == null){
            if (erro){
                instrucao.setTipo(TipoInstrucao._INVALIDO);
            }
            return instrucao;
        } else {
            return bloco;
        }
    }
    
    public LinkedList<Instrucao> getAllExpressions(){
        LinkedList<Instrucao> lista = new LinkedList<>();
        while (hasNext()){
            lista.add(parseExpression());
        }
        return lista;
    }
    
    public int getNumErros(){
        return erros.size();
    }
    
    public String getStringErros(){
        StringBuilder msg = new StringBuilder();
        for (ErroSintatico err : erros){
            if (msg.length() > 0){
                msg.append("\n");
            }
            msg.append(err.toString());
        }
        return msg.toString();
    }
    
    public boolean hasErroParsing(){
        return erro;
    }
    
    public Token getTokenUltimoErro(){
        return erros.isEmpty() ? null : erros.get(erros.size() - 1).token;
    }
    
    public String getMsgUltimoErro(){
        return erros.isEmpty() ? "" : erros.get(erros.size() - 1).erro;
    }
    
    public ArrayList<Token> getTokensErros(){
        ArrayList<Token> errToken = new ArrayList<>();
        for (ErroSintatico err : erros){
            errToken.add(err.token);
        }
        return errToken;
    }
    
    private boolean funcaoValida(Token token){
        if (!funcoesEsperadas.contains(token.getFuncaoToken())){
            String msgErro = "Encontrou " + token.getFuncaoToken() + ", mas esperava:";
            for (FuncaoToken ft: funcoesEsperadas){
                msgErro += "\n - " + ft;
            }
            erros.add(new ErroSintatico(token, msgErro));
            return false;
        } else {
            return true;
        }
    }
        
}
