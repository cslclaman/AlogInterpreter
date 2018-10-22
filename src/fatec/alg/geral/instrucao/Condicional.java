package fatec.alg.geral.instrucao;

import fatec.alg.geral.token.Token;

/**
 * Instrução de estrutura condicional - execução de cada um dos blocos
 * depende de sua condição ser verdadeira ou falsa.
 * @author Caique
 */
public class Condicional extends EstruturaControle {
    private Token tokenSe;
    private Token tokenEntao;
    private boolean composta;
    private Token tokenSenao;
    private Instrucao instrucaoSenao;

    /**
     * Cria uma nova instrução com tipo CONDICIONAL simples (se).
     */
    public Condicional() {
        super();
        tipo = TipoInstrucao.CONDICIONAL;
        composta = false;
    }

    /**
     * Define o token reservado "SE"
     * @param tokenSe token 
     */
    public void setTokenSe(Token tokenSe) {
        this.tokenSe = tokenSe;
        super.addToken(tokenSe);
    }
    
    /**
     * Define o token reservado "ENTÃO"
     * @param tokenEntao token
     */
    public void setTokenEntao(Token tokenEntao) {
        this.tokenEntao = tokenEntao;
        super.addToken(tokenEntao);
    }

    /**
     * Define a instrução a ser executada caso a condição seja verdadeira.
     * @param instrucaoSe instrução ou bloco
     */
    public void setInstrucaoSe(Instrucao instrucaoSe) {
        super.setInstrucao(instrucaoSe);
    }

    /**
     * Define o token reservado "SENÃO" e define a condicional como composta.
     * @param tokenSenao token
     */
    public void setTokenSenao(Token tokenSenao) {
        this.tokenSenao = tokenSenao;
        texto.append("\n    ");            
        super.addToken(tokenSenao);
        composta = true;
    }

    /**
     * Define a instrução a ser executada caso a condição seja falsa.
     * @param instrucaoSenao instrução ou bloco
     */
    public void setInstrucaoSenao(Instrucao instrucaoSenao) {
        this.instrucaoSenao = instrucaoSenao;
        tokens.addAll(instrucaoSenao.listaTokens());
        texto.append("\n    ").append(instrucaoSenao.toString());
    }
    
    /**
     * Retorna se a condicional é composta (se-senão)
     * @return TRUE caso seja composta (se-senão)
     */
    public boolean isComposta() {
        return composta;
    }

    /**
     * Retorna o token reservado SE
     * @return token
     */
    public Token getTokenSe() {
        return tokenSe;
    }

    /**
     * Retorna o token reservado SENÃO
     * @return token
     */
    public Token getTokenEntao() {
        return tokenEntao;
    }

    /**
     * Retorna a instrução para condição verdadeira.
     * @return Instrução ou bloco
     */
    public Instrucao getInstrucaoSe() {
        return instrucao;
    }

    /**
     * Retorna o token reservado ENTÃO
     * @return token
     */
    public Token getTokenSenao() {
        return tokenSenao;
    }

    /**
     * Retorna a instrução para condição verdadeira.
     * @return Instrução ou bloco
     */
    public Instrucao getInstrucaoSenao() {
        return instrucaoSenao;
    }
    
    /**
     * Valida a instrução: verifica se os tokens reservados não são nulos e se
     * as expressões e instruções são válidas.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            boolean compostaValida = 
                (!composta) ||
                (composta && tokenSenao != null &&
                (instrucaoSenao != null && instrucaoSenao.isValida()));
        
            valida =
                tokenSe != null &&
                (condicao != null && condicao.isValida()) &&
                (instrucao != null && instrucao.isValida()) &&
                compostaValida;
        }
    }
    
}
