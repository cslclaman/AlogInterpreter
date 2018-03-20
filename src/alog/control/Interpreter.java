/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Expressao;
import alog.model.FuncaoToken;
import alog.model.TipoVariavel;
import alog.model.Token;
import alog.model.Variavel;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Classe que recebe uma expressão e executa.
 * @author Caique
 */
public class Interpreter {
    private int bloco;
    private HashMap<String,Variavel> variaveis;
    
    public Interpreter(){
        bloco = 0;
        variaveis = new HashMap<>();
    }
    
    public void reseta(){
        bloco = 0;
        variaveis = new HashMap<>();
    }
    
    public boolean executa(Expressao expressao){
        switch(expressao.getTipo()){
            case DELIM_BLOCO:
                return execDelimBloco(expressao);
            case CRIACAO_VARIAVEL:
                return execCriacaoVariavel(expressao);
            case ENTRADA_DE_DADOS:
                return execEntradaDados(expressao);
            case SAIDA_DE_DADOS:
                return execSaidaDados(expressao);
            case OPERACAO_ATRIBUICAO:
            case OPERACAO_ARITMETICA:
                return execOperacao(expressao);
            case _INVALIDO:
                System.err.println("Expressão inválida");
                System.err.println("\t" + expressao.getTexto());
                //System.err.println("\tERRO - " + expressao.getErro());
                return false;
            default:
                System.err.println("Expressão indefinida");
                System.err.println("\t" + expressao.getTexto());
                return false;
        }
        
    }
    
    public boolean execDelimBloco(Expressao expressao){
        switch (expressao.getNext().getFuncaoToken()){
            case RES_BLOCO_INICIO:
                bloco ++;
                break;
            case RES_BLOCO_FIM:
                bloco --;
                break;
        }
        return true;
    }
    
    public boolean execCriacaoVariavel(Expressao expressao){
        TipoVariavel tipoVar;
        switch (expressao.getNext().getFuncaoToken()){
            case IDENT_TIPO_INTEIRO:
                tipoVar = TipoVariavel.INTEIRO;
                break;
            case IDENT_TIPO_REAL:
                tipoVar = TipoVariavel.REAL;
                break;
            case IDENT_TIPO_CARACTER:
                tipoVar = TipoVariavel.CARACTER;
                break;
            default:
                return false;
        }
        while (expressao.hasNext()){
            String nomeVar = expressao.getNext().getPalavra();
            Variavel variavel = new Variavel(tipoVar, nomeVar);
            variaveis.put(nomeVar, variavel);
        }
        
        return true;
    }
    
    public boolean execEntradaDados(Expressao expressao){
        expressao.setIndice(1);
        while (expressao.hasNext()){
            String nomeVar = expressao.getNext().getPalavra();
            Variavel variavel = variaveis.get(nomeVar);
            if (variavel == null){
                return false;
            }
            
            System.out.println("Valor para a variável " + nomeVar + " (" + variavel.getTipo() + ")");
            java.util.Scanner sc = new java.util.Scanner(System.in);

            boolean valorValido;
            do {
                valorValido = true;
                String linha = sc.nextLine();
                switch(variavel.getTipo()){
                    case CARACTER:
                        variavel.setValor(linha);
                        break;
                    case INTEIRO:
                        try {
                            int valor = Integer.parseInt(linha);
                            variavel.setValor(String.valueOf(valor));
                        } catch (NumberFormatException ex){
                            System.out.println("Valor \"" + linha + "\" inválido - esperado valor inteiro");
                            valorValido = false;
                        }
                        break;
                    case REAL:
                        try {
                            double valor = Double.parseDouble(linha);
                            variavel.setValor(String.valueOf(valor));
                        } catch (NumberFormatException ex){
                            System.out.println("Valor \"" + linha + "\" inválido - esperado valor real");
                            valorValido = false;
                        }
                        break;
                }
            } while (!valorValido);
            variaveis.put(nomeVar, variavel);
        }
        
        return true;
    }
    
    public boolean execSaidaDados(Expressao expressao){
        expressao.setIndice(1);
        int pos = 1;
        int qtd = expressao.getNumTokens() - 1;
        while (expressao.hasNext()){
            Token token = expressao.getNext();
            String saida = "";
            
            switch (token.getFuncaoToken()){
                case CONST_CARACTER:
                    saida = token.getPalavra().replace("\"", "");
                    break;
                case CONST_INTEIRA:
                case CONST_REAL:
                    saida = token.getPalavra();
                    break;
                case IDENT_NOME_VARIAVEL:
                    String nomeVar = expressao.getNext().getPalavra();
                    Variavel variavel = variaveis.get(nomeVar);
                    if (variavel == null){
                        return false;
                    }
                    saida = variavel.getValor();
            }
            
            System.out.printf("%s%s", saida, pos++ < qtd ? " " : "\n");
        }
        
        return true;
    }
    
    public boolean execOperacao(Expressao expressao){
        LinkedList<Token> pilha = new LinkedList<>();
        LinkedList<Token> saida = new LinkedList<>();
        
        while (expressao.hasNext()){
            Token token = expressao.getNext();
            switch (token.getFuncaoToken()){
                case IDENT_NOME_VARIAVEL:
                case CONST_CARACTER:
                case CONST_INTEIRA:
                case CONST_REAL:
                    saida.add(token);
                    break;

                case OP_ATRIBUICAO:
                case OP_SOMA:
                case OP_SUBTRACAO:
                case OP_MULTIPLICACAO:
                case OP_DIV_INTEIRA:
                case OP_DIV_REAL:
                case OP_MOD:
                    while (!pilha.isEmpty() && pilha.peek().getPrecedencia() > token.getPrecedencia()){
                        saida.add(pilha.pop());
                    }
                    pilha.push(token);
                    break;
                
                case DELIM_PARENTESES_ABRE:
                    pilha.push(token);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    while (!pilha.isEmpty()){
                        Token out = pilha.pop();
                        if (out.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE){
                            break;
                        } else {
                            saida.add(out);
                        }
                    }
                    break;
            }
        }
        while (!pilha.isEmpty()){
            saida.add(pilha.pop());
        }
        
        Calculator calculadora;
        
        while (!saida.isEmpty()){
            Token token = saida.pop();
            Variavel op1, op2;
            switch (token.getFuncaoToken()){
                case IDENT_NOME_VARIAVEL:
                case CONST_CARACTER:
                case CONST_INTEIRA:
                case CONST_REAL:
                    pilha.push(token);
                    break;
                case OP_SOMA:
                case OP_SUBTRACAO:
                case OP_MULTIPLICACAO:
                case OP_DIV_INTEIRA:
                case OP_DIV_REAL:
                case OP_MOD:
                    op2 = retornaVariavel(pilha.pop());
                    op1 = retornaVariavel(pilha.pop());
                    //depuração apenas
                    System.out.println(op1.getValor() + " " + token.getPalavra() + " " + op2.getValor());
                    
                    calculadora = new Calculator(token);
                    token = calculadora.executaOperacaoAritmetica(op1, op2);
                    if (token == null){
                        return false;
                    } else {
                        //depuração apenas
                        System.out.println(token.getPalavra());
                        pilha.push(token);
                    }
                    break;
                case OP_ATRIBUICAO:
                    op1 = retornaVariavel(pilha.pop());
                    Token tokVar = pilha.pop();
                    if (tokVar.getFuncaoToken() != FuncaoToken.IDENT_NOME_VARIAVEL){
                        System.err.println("Atribuição inválida - Esperava Variável, encontrou Constante");
                        return false;
                    }
                    String nomeVar = tokVar.getPalavra();
                    Variavel variavel = variaveis.get(nomeVar);
                    if (variavel == null){
                        return false;
                    }
                    switch (variavel.getTipo()){
                        case INTEIRO:
                            if (op1.getTipo() != TipoVariavel.INTEIRO){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                        case REAL:
                            if (op1.getTipo() != TipoVariavel.REAL && op1.getTipo() != TipoVariavel.INTEIRO){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                        case CARACTER:
                            if (op1.getTipo() != TipoVariavel.CARACTER){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                    }
                    variaveis.put(nomeVar, variavel);
            }
        }
        
        return true;
    }
    
    public Variavel retornaVariavel(Token token){
        Variavel temp;
        String nomeVar = "temp" + token.getOrdem();
        switch (token.getFuncaoToken()){
            case IDENT_NOME_VARIAVEL:
                temp = variaveis.get(token.getPalavra());
                break;
            case CONST_CARACTER:
                temp = new Variavel(TipoVariavel.CARACTER, nomeVar);
                temp.setValor(token.getPalavra().replace("\"", ""));
                break;
            case CONST_INTEIRA:
                temp = new Variavel(TipoVariavel.INTEIRO, nomeVar);
                temp.setValor(token.getPalavra());
                break;
            case CONST_REAL:
                temp = new Variavel(TipoVariavel.REAL, nomeVar);
                temp.setValor(token.getPalavra());
                break;
            default:
                System.out.println("Tipo de token inválido para efetuar operação: " + token.getFuncaoToken());
                temp = null;
        }
        return temp;
    }
}
