package alog.instrucao;

import alog.model.TipoVariavel;
import alog.model.Variavel;
import alog.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Instrução que corresponde ao mecanismo de criação de variáveis.
 * @author Caique
 */
public class DeclaracaoVariaveis extends Instrucao {

    private Token tokenTipoVariavel;
    private TipoVariavel tipoVariavel;
    private LinkedList<Token> nomesVariaveis;
    
    /**
     * Cria uma instrução vazia.
     */
    public DeclaracaoVariaveis() {
        tokens = new LinkedList<>();
        tipo = TipoInstrucao.DECLARACAO_VARIAVEL;
        tokens = new LinkedList<>();
        nomesVariaveis = new LinkedList<>();
    }

    /**
     * Define o token que identifica qual o tipo da(s) variável(is) sendo criada(s).
     * Esse token será mapeado para {@link TipoVariavel}
     * @param token 
     */
    public void setTipoVariavel(Token token) {
        if (this.tokenTipoVariavel == null) {
            this.tokenTipoVariavel = token;
            tipoVariavel = TipoVariavel.mapTokenToVariavel(token);
            tokens.add(token);
        }
    }
    
    /**
     * Adiciona um token que representa um nome de variável sendo declarada.
     * @param token 
     */
    public void addNomeVariavel(Token token) {
        if (tokenTipoVariavel != null) {
            nomesVariaveis.add(token);
            tokens.add(token);
        }
    }

    @Override
    public List<Token> listaTokens() {
        return tokens;
    }

    /**
     * Valida se foi definido um tipo válido e se há pelo menos um nome de variável informado.
     * @return 
     */
    @Override
    public boolean instrucaoValida() {
        if (tipoVariavel != null && !nomesVariaveis.isEmpty()){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }

    /**
     * Retorna o tipo da(s) variável(is) sendo criada(s).
     * @return 
     */
    public TipoVariavel getTipoVariavel(){
        return tipoVariavel;
    }
    
    /**
     * Retorna uma lista de {@link Variavel}.
     * @return 
     */
    public List<Variavel> getVariaveis(){
        LinkedList<Variavel> variaveis = new LinkedList<>();
        TipoVariavel tipoVar = tipoVariavel;
        for (Token token : nomesVariaveis) {
            variaveis.add(new Variavel(tipoVar, token.getPalavra()));
        }
        return variaveis;
    }
    
}
