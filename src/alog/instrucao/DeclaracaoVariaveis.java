package alog.instrucao;

import alog.model.TipoDado;
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
    private TipoDado tipoVariavel;
    private LinkedList<Token> nomesVariaveis;
    
    /**
     * Cria uma instrução vazia.
     */
    public DeclaracaoVariaveis() {
        super();
        tipo = TipoInstrucao.DECLARACAO_VARIAVEL;
        nomesVariaveis = new LinkedList<>();
    }

    /**
     * Define o token que identifica qual o tipo da(s) variável(is) sendo criada(s).
     * Esse token será mapeado para {@link TipoDado}
     * @param token 
     */
    public void setTipoVariavel(Token token) {
        if (this.tokenTipoVariavel == null) {
            this.tokenTipoVariavel = token;
            tipoVariavel = TipoDado.mapTokenToVariavel(token);
            addToken(token);
        }
    }
    
    /**
     * Adiciona um token que representa um nome de variável sendo declarada.
     * @param token 
     */
    public void addNomeVariavel(Token token) {
        if (tokenTipoVariavel != null) {
            nomesVariaveis.add(token);
            addToken(token);
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
        if (tipoVariavel == null || nomesVariaveis.isEmpty()){
            invalidaInstrucao();
        }
        return super.instrucaoValida();
    }

    /**
     * Retorna o tipo da(s) variável(is) sendo criada(s).
     * @return 
     */
    public TipoDado getTipoVariavel(){
        return tipoVariavel;
    }
    
    /**
     * Retorna uma lista de {@link Variavel}.
     * @return 
     */
    public List<Variavel> getVariaveis(){
        LinkedList<Variavel> variaveis = new LinkedList<>();
        TipoDado tipoVar = tipoVariavel;
        for (Token token : nomesVariaveis) {
            variaveis.add(new Variavel(tipoVar, token.getPalavra()));
        }
        return variaveis;
    }
    
}
