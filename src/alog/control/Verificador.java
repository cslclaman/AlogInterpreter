/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.analise.Erro;
import alog.expressao.*;
import alog.instrucao.*;
import alog.model.TipoDado;
import alog.model.Variavel;
import alog.token.Token;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Analisador semântico que verifica a coerência das expressões criadas pelo Parser
 * @author Caique
 */
public class Verificador {
    
    private class VariavelVerif {
        int chamadas;
        TipoDado tipo;
        boolean inicializada;

        public VariavelVerif(TipoDado tipo) {
            this.tipo = tipo;
            this.inicializada = false;
            this.chamadas = 0;
        }
    }
    
    private class RotinaVerif {
        int chamadas;
        TipoDado[] parametros;

        public RotinaVerif() {
            this.chamadas = 0;
        }
        
        public RotinaVerif(TipoDado[] parametros) {
            this.parametros = parametros;
            this.chamadas = 0;
        }
    }
    
    private class FuncaoVerif extends RotinaVerif {
        TipoDado retorno; 
        
        public FuncaoVerif() {
            super();
        }
        
        public FuncaoVerif(TipoDado[] parametros) {
            super(parametros);
        }

        public FuncaoVerif(TipoDado[] parametros, TipoDado retorno) {
            super(parametros);
            this.retorno = retorno;
        }
    }
    
    private LinkedList<Erro> erros;
    private List<Instrucao> programa;
    private HashMap <String, VariavelVerif> variaveis;
    private HashMap <String, FuncaoVerif> funcoes;
    private int pos;
    private int len;
    
    public Verificador(List<Instrucao> programa) {
        this.programa = programa;
        
        erros = new LinkedList<>();
        variaveis = new HashMap<>();
        funcoes = new HashMap<>();
        
        funcoes.put("pot", new FuncaoVerif(new TipoDado[]{TipoDado.REAL, TipoDado.REAL}, TipoDado.REAL));
        funcoes.put("raiz", new FuncaoVerif(new TipoDado[]{TipoDado.REAL}, TipoDado.REAL));
        
        pos = 0;
        len = programa.size();
    }
    
    public List<Instrucao> verificaPrograma(){
        while (pos < len) {
            Instrucao instrucao = programa.get(pos++);
            switch (instrucao.getTipo()) {
                case BLOCO:
                    Verificador verificador = new Verificador(programa);
                    verificador.erros = this.erros;
                    verificador.variaveis = this.variaveis;
                    verificador.funcoes = this.funcoes;
                    verificador.pos = this.pos;
                    
                    verificador.verificaPrograma();
                    
                    this.erros = verificador.erros;
                    this.variaveis = verificador.variaveis;
                    this.funcoes = verificador.funcoes;
                    this.pos = verificador.pos;
                    
                    break;
                    
                case DECLARACAO_VARIAVEL:
                    DeclaracaoVariaveis declaracaoVariaveis = (DeclaracaoVariaveis)instrucao;
                    for (Variavel variavel : declaracaoVariaveis.getVariaveis()) {
                        variaveis.put(variavel.getNome(), new VariavelVerif(variavel.getTipo()));
                    }
                    break;
                    
                case ENTRADA_DE_DADOS:
                    EntradaDados entradaDados = (EntradaDados)instrucao;
                    for (Token token : entradaDados.getParametros()) {
                        VariavelVerif var = variaveis.get(token.getPalavra());
                        var.inicializada = true;
                        variaveis.put(token.getPalavra(), var);
                    }
                    break;
                    
                case SAIDA_DE_DADOS:
                    SaidaDados saidaDados = (SaidaDados)instrucao;
                    for (Expressao expressao : saidaDados.getParametros()) {
                        
                    }
                    break;
            }
        }
    }
    
    private Expressao verificaExpressao(Expressao expressao) {
        switch (expressao.getTipoExpressao()) {
            case OPERACAO_UNARIA:
                
        }
    }
    
}
