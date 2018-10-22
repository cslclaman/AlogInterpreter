package fatec.alg.geral.instrucao;

import fatec.alg.geral.expressao.Expressao;
import fatec.alg.geral.token.Token;
import java.util.LinkedList;
import java.util.List;

/**
 * Instrução que realiza a chamada a uma rotina (módulo que recebe parâmetros
 * e não retorna valor após sua execução).
 * <br>Rotinas do sistema: Leia, Escreva.
 * @author Caique Souza
 */
public class ChamadaRotina extends Instrucao {

    /**
     * Token do do tipo Identificador Nome Rotina, Leia ou Escreva.
     */
    protected Token nome;
    /**
     * Lista de parâmetros de entrada da rotina (expressões).
     */
    protected LinkedList<Expressao> parametros;
    /**
     * Número de parâmetros, incrementado a cada expressão adicionada.
     */
    protected int numParametros;
    
    /**
     * Instancia uma instrução do tipo CHAMADA_ROTINA e com nenhum parâmetro.
     */
    public ChamadaRotina() {
        super();
        tipo = TipoInstrucao.CHAMADA_ROTINA;
        parametros = new LinkedList<>();
        numParametros = 0;
    }

    /**
     * Define o token que identifica a rotina.
     * @param token Token Identificador de Rotina
     */
    public void setTokenNome(Token token){
        nome = token;
        super.addToken(token);
    }
    
    /**
     * Adiciona uma expressão como um dos parâmetros de entrada para a rotina.
     * @param expressao Parâmetro.
     */
    public void addParametro(Expressao expressao) {
        parametros.add(expressao);
        numParametros ++;
        for (Token token : expressao.listaTokens()){
            super.addToken(token);
        }
    }

    /**
     * Retorna o token que identifica a rotina.
     * @return Token Identificador de Rotina
     */
    public Token getTokenNome() {
        return nome;
    }
    
    /**
     * Retorna lista com todos os parâmetros de entrada da rotina.
     * @return Lista de expressões
     */
    public List<Expressao> getParametros() {
        return parametros;
    }
    
    /**
     * Retorna a expressão parâmetro de entrada na posição indicada.
     * @param index Índice da expressão que se deseja obter.
     * @return Expressão ou null caso o índice seja menor que 0 e maior que o num de expressões.
     */
    public Expressao getParametroAt(int index) {
        if (parametros.isEmpty()) {
            return null;
        } else {
            if (index >= 0 && index < numParametros) {
                return parametros.get(index);
            } else {
                return null;
            } 
        }
    }
    
    /**
     * Retorna o número de parâmetros encontrados.
     * @return Num de parâmetros (expressões)
     */
    public int getNumParametros() {
        return numParametros;
    }

    /**
     * Valida a instrução: verifica se o identificador de nome não é nulo e se
     * todas as expressões/parâmetros são válidas.
     */
    @Override
    public void finaliza() {
        if (valida) {
            int numValidos = 0;
            for (Expressao expressao : parametros) {
                if (expressao.isValida()){
                    numValidos ++;
                }
            }

            valida = 
                    nome != null &&
                    numValidos == numParametros;
        }
    }

}
