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

    /**
     * Lista de {@link Erro}s.
     */
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
     * Note que ele conta todos os erros encontrados, independente do tipo.
     * @return Número de erros encontrados.
     */
    public int getNumErros(){
        return erros.size();
    }
    
    /**
     * Retorna número de erros encontrados ou, mais especificamente, contidos na lista.
     * Esse método retorna apenas os erros com prioridade igual ou acima a do nível informado.
     * @param nivel Filtragem.
     * @return Número de erros encontrados.
     */
    public int getNumErros(TipoErro nivel){
        int nerr = 0;
        for (Erro erro : erros) {
            if (erro.getTipo().getNivel() <= nivel.getNivel()) {
                nerr ++;
            }
        }
        return nerr;
    }
    
    /**
     * Retorna todos os erros encontrados, independente de prioridade.
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
    public List<Erro> getErros(){
        return getErros(TipoErro.getMax());
    }
          
     /**
     * Retorna os erros encontrados que tenham prioridade superior ou igual ao nível informado.
     * @param nivel Filtragem
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
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
     * Retorna lista com mensagens de todos os erros encontrados durante a análise.
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
    public String printErros(){
        return printErros(TipoErro.getMax());
    }
    
    /**
     * Retorna lista com mensagens dos erros encontrados durante a análise
     * que tenham prioridade maior ou igual ao nível informado.
     * @param nivel Filtragem
     * @return Lista com mensagens ou lista vazia caso não tenham erros
     */
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
    
    /**
     * Retorna o último erro lançado, ou <code>null</code> caso não tenham erros.
     * @return 
     */
    public Erro getUltimoErro(){
        try {
            return erros.getLast();
        } catch (NoSuchElementException ex) {
            return null;
        }
    }
    
    /**
     * Retorna o erro na posição informada, ou <code>null</code> caso não tenham erros.
     * @param index Índice do erro na lista.
     * @return erro no índice ou null se não houver erros.
     */
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
