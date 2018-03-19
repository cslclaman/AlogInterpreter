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
import java.util.InputMismatchException;
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
            case OPERACAO_ATRIBUICAO:
            case OPERACAO_ARITMETICA:
                return execOperacaoAritmetica(expressao);
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
    
    public boolean execOperacaoAritmetica(Expressao expressao){
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
        
        // DEPURAÇÃO APENAS
        for (Token t : saida){
            System.out.print(t.getPalavra() + " ");
        }
        System.out.println("\n");
        
        return true;
    }
}
