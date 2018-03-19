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
import java.util.InputMismatchException;
import java.util.LinkedList;

/**
 * Classe que recebe uma expressão e executa.
 * @author Caique
 */
public class Interpreter {
    private int bloco;
    private LinkedList<Variavel> listaVariaveis;
    
    public Interpreter(){
        bloco = 0;
        listaVariaveis = new LinkedList<>();
    }
    
    public void reseta(){
        bloco = 0;
        listaVariaveis = new LinkedList<>();
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
            Variavel variavel = new Variavel(tipoVar, expressao.getNext().getPalavra());
            listaVariaveis.add(variavel);
        }
        
        return true;
    }
    
    public boolean execEntradaDados(Expressao expressao){
        expressao.setIndice(1);
        while (expressao.hasNext()){
            int indiceVar = pesquisaVariavel(expressao.getNext().getPalavra());
            if (indiceVar >= 0){
                Variavel variavel = listaVariaveis.get(indiceVar);
                System.out.println("Valor para a variável " + variavel.getNome() + " (" + variavel.getTipo() + ")");
                java.util.Scanner sc = new java.util.Scanner(System.in);
                
                boolean valorValido;
                do {
                    valorValido = true;
                    switch(variavel.getTipo()){
                        case CARACTER:
                            variavel.setValor(sc.nextLine());
                            break;
                        case INTEIRO:
                            try {
                                int valor = sc.nextInt();
                                variavel.setValor(String.valueOf(valor));
                            } catch (InputMismatchException ex){
                                System.out.println("Valor inválido - esperado valor inteiro");
                                valorValido = false;
                            }
                            break;
                        case REAL:
                            try {
                                double valor = sc.nextDouble();
                                variavel.setValor(String.valueOf(valor));
                            } catch (InputMismatchException ex){
                                System.out.println("Valor inválido - esperado valor real");
                                valorValido = false;
                            }
                            break;
                    }
                } while (!valorValido);
                
                listaVariaveis.set(indiceVar, variavel);
            }
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
    
    private Token efetuaOperacao(Token operador, Token tok1, Token tok2){
        switch (operador.getFuncaoToken()){
            case OP_ATRIBUICAO:
                int indiceVar = pesquisaVariavel(tok1.getPalavra());
                if (indiceVar >= 0){
                    Variavel variavel = listaVariaveis.get(indiceVar);
                    String valor = "";
                    switch (tok2.getFuncaoToken()){
                        case IDENT_NOME_VARIAVEL:
                            int indiceVar2 = pesquisaVariavel(tok1.getPalavra());
                            
                    }
                } else {
                    return null;
                }
                    
            case OP_SOMA:
            case OP_SUBTRACAO:
            case OP_MULTIPLICACAO:
            case OP_DIV_INTEIRA:
            case OP_DIV_REAL:
            case OP_MOD:
        }
        return operador;
    }
    
    private int pesquisaVariavel(String nome){
        int indice = 0;
        for (Variavel v : listaVariaveis){
            if (v.getNome().equals(nome)){
                return indice;
            } else {
                indice ++;
            }
        }
        return -1;
    }
}
