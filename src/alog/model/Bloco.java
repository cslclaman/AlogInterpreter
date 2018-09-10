package alog.model;

import alog.token.FuncaoToken;
import alog.token.Token;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que representa um bloco de expressões (delimitado por início/fim)
 * @author Caique Souza
 */
public class Bloco extends Instrucao {
    private LinkedList<Token> tokens;
    private LinkedList<Instrucao> instrucoes;
    private Token inicio;
    private Token fim;
    private boolean instrucoesValidas;
    
    public Bloco() {
        tokens = new LinkedList<>();
        instrucoes = new LinkedList<>();
        tipo = TipoInstrucao.BLOCO;
        texto = "";
        instrucoesValidas = true;
    }

    public boolean insereInstrucao(Instrucao instrucao){
        if (texto.isEmpty() || !instrucao.instrucaoValida()){
            instrucoesValidas = false;
            return false;
        } 
        instrucoes.add(instrucao);
        texto += "\n    " + instrucao.getTipo();
        for (Token token : instrucao.listaTokens()){
            tokens.add(token);
        }
        return true;
    }

    public int getNumInstrucoes(){
        return instrucoes.size();
    }
    
    public List<Instrucao> listaInstrucoes(){
        return instrucoes;
    }

    @Override
    public int getNumTokens() {
        return tokens.size();
    }

    @Override
    public boolean insereToken(Token token) {
        switch (token.getFuncaoToken()){
            case RES_BLOCO_INICIO:
                if (!texto.isEmpty()){
                    instrucoesValidas = false;
                    return false;
                } else {
                    inicio = token;
                    texto = token.getPalavra();
                }
                break;
            case RES_BLOCO_FIM:
                if (texto.isEmpty()){
                    instrucoesValidas = false;
                    return false;
                } else {
                    fim = token;
                    texto = "\n" + token.getPalavra();
                }
                break;
            default:
                return false;
        }
        tokens.add(token);
        return true;
    }

    @Override
    public List<Token> listaTokens() {
        return tokens;
    }

    @Override
    public boolean instrucaoValida() {
        boolean valido =
                inicio != null &&
                fim != null &&
                // instrucoes.size() > 0 &&
                instrucoesValidas;
        
        if (!valido){
            tipo = TipoInstrucao._INVALIDO;
        }
        return valido;
    }

    public Token getInicio() {
        return inicio;
    }

    public Token getFim() {
        return fim;
    }
}
