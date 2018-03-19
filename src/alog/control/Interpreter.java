/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Expressao;
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
        }
        return false;
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
                
                boolean valorValido = true;
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
