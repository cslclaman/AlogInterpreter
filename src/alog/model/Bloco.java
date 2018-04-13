package alog.model;

import java.util.ArrayList;

/**
 * Classe que representa um bloco de expressões (delimitado por início/fim)
 * @author Caique Souza
 */
public class Bloco extends Expressao {
    protected ArrayList<Expressao> expressoes;
    protected int linhaFinal;
    
    public Bloco() {
        super();
        expressoes = new ArrayList<>();
        linhaFinal = 0;
    }

    public int getLinhaFinal() {
        return linhaFinal;
    }

    @Override
    public void setLinha(int linha) {
        super.setLinha(linha);
        linhaFinal = expressoes.size() + linha;
    }

    public void addExpressao(Expressao expressao){
        expressoes.add(expressao);
        linhaFinal = expressoes.size() + linha;
    }

    public int getNumExpressoes(){
        return expressoes.size();
    }
    
    public ArrayList<Expressao> listExpressoes(){
        return expressoes;
    }
    
    public boolean hasNextExpressao(){
        return indice < expressoes.size();
    }
    
    public Expressao getNextExpressao(){
        if (hasNextExpressao()){
            return expressoes.get(indice++);
        } else {
            return null;
        }
    }
    
    @Override
    public String printTokens(){
        StringBuilder str = new StringBuilder();
        for (Expressao expr : expressoes){
            str.append(expr.printTokens());
            str.append("\n");
        }
        return str.toString();
    }
}
