/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.analise.Erro;
import alog.instrucao.Instrucao;
import alog.model.TipoDado;
import alog.model.Variavel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Analisador semântico que verifica a coerência das expressões criadas pelo Parser
 * @author Caique
 */
public class Verificador {
    
    private LinkedList<Erro> erros;
    private ArrayList<Instrucao> programa;
    private HashMap <String, Variavel> variaveis;
    private HashMap <String, TipoDado[] > funcoes;
    private int pos;
    
    public Verificador(List<Instrucao> programa) {
        this.programa = new ArrayList<>(programa.size());
        this.programa.addAll(programa);
        
        erros = new LinkedList<>();
        variaveis = new HashMap<>();
        funcoes = new HashMap<>();
        funcoes.put("pot", new TipoDado[]{TipoDado.REAL, TipoDado.REAL} );
        funcoes.put("raiz", new TipoDado[]{TipoDado.REAL} );
        pos = 0;
    }
    
    public boolean existeProxima(){
        return pos < programa.size();
    }
    
    public void verifyNext(){
        if (hasNext()){
            Instrucao expressao = programa.get(pos++);
            switch (expressao.getTipo()){
                case DECLARACAO_VARIAVEL:
                    String tipoVar = "";
                    for (Token t : expressao.listTokens()){
                        if (t.getFuncaoToken() == FuncaoToken.RES_TIPO_CARACTER ||
                            t.getFuncaoToken() == FuncaoToken.RES_TIPO_INTEIRO ||
                            t.getFuncaoToken() == FuncaoToken.RES_TIPO_REAL) {
                            tipoVar = t.getPalavra().toUpperCase();
                            continue;
                        }
                        String nomeVar = t.getPalavra();
                        if (variaveis.containsKey(nomeVar)){
                            erros.add(new ErroSemantico(expressao, "Variável já declarada: " + variaveis.get(nomeVar) + " " + nomeVar));
                        } else {
                            variaveis.put(nomeVar, tipoVar);
                        }
                    }
                    break;
                case ENTRADA_DE_DADOS:
                    for (Token t : expressao.listTokens()){
                        String nome = t.getPalavra();
                        switch (t.getFuncaoToken()){
                            case IDENT_NOME_VARIAVEL:
                                if (!variaveis.containsKey(nome)){
                                    erros.add(new ErroSemantico(expressao, "Variável não declarada: " + nome));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                    
                case OPERACAO_ARITMETICA:
                case ATRIBUICAO:
                case OPERACAO_LOGICA:
                case SAIDA_DE_DADOS:
                case CHAMADA_FUNCAO:
                    int paramEsperado = 0;
                    int paramEncontrado = 0;
                    for (Token t : expressao.listTokens()){
                        String nome = t.getPalavra();
                        switch (t.getFuncaoToken()){
                            case LIB_MATH_POT:
                            case LIB_MATH_RAIZ:
                                paramEsperado = funcoes.get(nome.toUpperCase());
                                break;
                            case IDENT_NOME_VARIAVEL:
                                if (!variaveis.containsKey(nome)){
                                    erros.add(new ErroSemantico(expressao, "Variável não declarada: " + nome));
                                }
                                break;
                            case DELIM_VIRGULA:
                            case DELIM_PARENTESES_FECHA:
                                paramEncontrado ++;
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                    
                default:
                    break;
            }
        }
    }
    
}
