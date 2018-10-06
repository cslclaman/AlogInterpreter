/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.analise.Erro;
import alog.analise.TipoErro;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Classe que define lista e controle de erros de verificação/interpretação.
 * @author Caique Souza
 */
public abstract class Verificator {
    protected LinkedList<Erro> erros;
    
    /**
     * Inicializa lista de erros.
     */
    protected Verificator () {
        erros = new LinkedList<>();
    }
    
    /**
     * Retorna se houve erros. Mais especificamente, retorna se a lista de erros tem pelo menos um elemento.
     * @return 
     */
    public boolean hasErros() {
        return erros.isEmpty();
    }
    
    /**
     * Retorna número de erros encontrados ou, mais especificamente, contidos na lista.
     * @return Número de erros encontrados.
     */
    public int getNumErros(){
        return erros.size();
    }
    
    /**
     * Retorna os erros encontrados.
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
    public List<Erro> getErros(){
        return getErros(TipoErro.getDefault());
    }
            
    public List<Erro> getErros(TipoErro nivel){
        LinkedList<Erro> retorno = new LinkedList<>();
        for (Erro erro : erros) {
            if (erro.getTipo().getNivel() <= nivel.getNivel()) {
                retorno.add(erro);
            }
        }
        return retorno;
    }
    
    /**
     * Retorna lista com mensagens de erro que ocorreram durante a análise.
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
    public String printErros(){
        return printErros(TipoErro.getDefault());
    }
    
    public String printErros(TipoErro nivel){
        StringBuilder msg = new StringBuilder();
        for (Erro erro : erros){
            if (erro.getTipo().getNivel() <= nivel.getNivel()) {
                if (msg.length() > 0){
                    msg.append("\n");
                }
                msg.append(erro.toString());
            }
        }
        return msg.toString();
    }
    
    public Erro getUltimoErro(){
        try {
            return erros.getLast();
        } catch (NoSuchElementException ex) {
            return null;
        }
    }
    
    public Erro getErroAt(int index) {
        if (erros.isEmpty()) {
            return null;
        } else {
            if (index < 0) {
                return erros.get(0);
            } else {
                if (index > getNumErros() - 1) {
                    return erros.get(getNumErros() - 1);
                } else {
                    return erros.get(index);
                }
            } 
        }
    }
}
