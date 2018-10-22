package fatec.alg.geral.instrucao;

import fatec.alg.geral.expressao.Expressao;
import fatec.alg.geral.token.FuncaoToken;
import fatec.alg.geral.token.Token;

/**
 * Instrução de estrutura PARA (repetitiva com número fixo de iterações e auto-incremento).
 * @author Caique Souza
 */
public class RepeticaoPara extends EstruturaControle {
    private Token tokenPara;
    private Token tokenFaca;
    private Token variavelCont;
    private Expressao expressaoDe;
    private Expressao expressaoAte;
    private Expressao expressaoPasso;
    
    /**
     * Instancia uma instrução com tipo REPETICAO_PARA
     */
    public RepeticaoPara() {
        super();
        tipo = TipoInstrucao.REPETICAO_PARA;
    }

    /**
     * Define o token reservado PARA
     * @param tokenPara token
     */
    public void setTokenPara(Token tokenPara) {
        this.tokenPara = tokenPara;
        super.addToken(tokenPara);
    }

    /**
     * Define o token reservado FAÇA
     * @param tokenFaca token
     */
    public void setTokenFaca(Token tokenFaca) {
        this.tokenFaca = tokenFaca;
        super.addToken(tokenPara);
    }

    /**
     * Define o token que identifica a variável de contagem.
     * @param variavelCont token identificador de variável numérica
     */
    public void setVariavelCont(Token variavelCont) {
        if (variavelCont.getFuncaoToken() == FuncaoToken.IDENT_NOME_VARIAVEL) {
            this.variavelCont = variavelCont;
        } 
        super.addToken(variavelCont);
    }

    /**
     * Define a expressão a ser atribuída para a variável no início da execução
     * @param expressaoDe expressão constante, variável ou aritmética
     */
    public void setExpressaoDe(Expressao expressaoDe) {
        this.expressaoDe = expressaoDe;
        tokens.addAll(expressaoDe.listaTokens());
        texto.append(expressaoDe.toString()).append(" ");
    }
    
    /**
     * Define a expressão de limite da execução
     * @param expressaoAte expressão constante, variável ou aritmética
     */
    public void setExpressaoAte(Expressao expressaoAte) {
        this.expressaoAte = expressaoAte;
        tokens.addAll(expressaoAte.listaTokens());
        texto.append(expressaoAte.toString()).append(" ");
    }

    /**
     * Define a expressão de passo (incremento/decremento) da variável contadora.
     * @param expressaoPasso expressão constante, variável ou aritmética
     */
    public void setExpressaoPasso(Expressao expressaoPasso) {
        this.expressaoPasso = expressaoPasso;
    }
    
    /**
     * Retorna o token reservado Para
     * @return token
     */
    public Token getTokenPara() {
        return tokenPara;
    }

    /**
     * Retorna o token reservado Faça
     * @return token
     */
    public Token getTokenFaca() {
        return tokenFaca;
    }

    /**
     * Retorna o token identificador da variável de contagem
     * @return token
     */
    public Token getVariavelCont() {
        return variavelCont;
    }

    /**
     * Retorna a expressão de inicialização
     * @return expressão
     */
    public Expressao getExpressaoDe() {
        return expressaoDe;
    }

    /**
     * Retorna a expressão de limite
     * @return expressão
     */
    public Expressao getExpressaoAte() {
        return expressaoAte;
    }

    /**
     * Retorna a expressão de incremento da variável
     * @return expressão
     */
    public Expressao getExpressaoPasso() {
        return expressaoPasso;
    }
    
    /**
     * Valida se as expressões e a instrução não são nulas e são válidas.
     */
    @Override
    public void fazValidacao() {
        if (valida) {
            valida =
                tokenPara != null &&
                variavelCont != null &&
                (expressaoDe != null && expressaoDe.isValida()) &&
                (expressaoAte != null && expressaoAte.isValida()) &&
                (instrucao != null && instrucao.isValida());
        }
    }
    
}
