/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.analise.Erro;
import alog.expressao.Expressao;
import alog.instrucao.Instrucao;
import alog.model.Variavel;
import alog.token.Token;

/**
 * Interface para permitir interação com o interpretador.
 * Para facilitar o desenvolvimento de uma interface básica
 * (que exibe apenas comandos de entrada e saída de dados),
 * a maioria dos métodos tem implementação padrão (default) sem ação.
 * @author Caique Souza
 */
public interface InterfaceExecucao {
    
    /**
     * Atualiza à interface qual a instrução sendo executada. Para fins de controle.
     * <br>Um bloco, nesse caso, não é considerado como instrução única. São consideradas suas instruções internas.
     * @param instrucao Instrução sendo executada
     */
    void atualizaInstrucaoAtual(Instrucao instrucao);
    
    /**
     * Atualiza à interface qual o passo (token) sendo executado dentro da instrução atual. Para fins de controle.
     * <br>Caso o próximo token seja uma expressão, esse método retornará o resultado de
     * {@link alog.expressao.Expressao#getAsToken() }.
     * @param token token sendo executado.
     */
    void atualizaPassoAtual(Token token);
    
    /**
     * Atualiza à interface uma expressão que esteja sendo executada dentro da instrução atual. Para fins de controle.
     * @param expressao expressão sendo executada.
     */
    void atualizaExpressaoAtual(Expressao expressao);
    
    /**
     * Indica que a instrução atual indica um bloco. Por padrão, não tem ação.
     */
    default void inicioBloco(){};
    
    /**
     * Indica que a instrução atual finaliza um bloco. Por padrão, não tem ação.
     */
    default void fimBloco(){};
    
    /**
     * Indica uma declaração de variável e informa a variável que acabou de ser criada.
     * Note que, para uma instrução que declara mais de uma variável, esse método é chamado mais de uma vez.
     * Exemplo: Na instrução <code>Inteiro: Idade, Dia, Hora;</code>, haverá:
     * <br>Uma chamada passando a variável <code>Inteiro: Idade</code>,
     * <br>uma chamada passando a variável <code>Inteiro: Dia</code> e
     * <br>uma chamada passando a variável <code>Inteiro: Hora</code>.
     * <br>Por padrão, não há ação.
     * @param variavel A variável que acabou de ser declarada.
     */
    default void declaracaoVariavel(Variavel variavel){};
    
    /**
     * Realiza a leitura de dados para uma variável anteriormente declarada.
     * Assim como no método {@link declaracaoVariavel(alog.model.Variavel) },
     * esse método terá uma chamada para cada variável informada como parâmetro.
     * <br>Caso o tipo do valor lido não possa ser convertido para o tipo da variável,
     * será chamado o método {@link erroEntradaDados(alog.model.Variavel, alog.analise.Erro)}
     * e depois tornará a chamar este mesmo método.
     * <br><b>Nota: caso a interface não consiga causar uma interrupção para retornar
     * o valor lido, este método <u><i>DEVE</i></u> retornar <code>null</code> e depois
     * chamar o método {@link #entradaDadosRetorno() }</b>
     * @param variavel A variável (tipo Inteiro) que receberá o valor lido.
     * @return String com o valor lido a ser salvo na variável ou
     * <code>null</code> caso a interface não tenha poder de fazer o interpretador esperar.
     */
    String entradaDados(Variavel variavel);
    
    /**
     * Retorna o valor a ser armazenado em uma variável durante uma entrada de dados.
     * Esse método será usado caso o método {@link #entradaDados(alog.model.Variavel) } retorne <code>null</code>.
     * @return String com o valor lido a ser salvo na variável. Se for <code>null</code>, encerra a interpretação.
     */
    String entradaDadosRetorno();
    
    /**
     * Indica um erro ao realizar uma entrada de dados, principalmente de conversão de tipo.
     * <br>Acompanhado de uma nova chamada ao método {@link #entradaDados(alog.model.Variavel) }
     * a não ser que o erro ocorrido seja fatal.
     * @param variavel A variável que teve erro ao receber o valor lido.
     * @param erro {@link Erro} com Detalhes do erro encontrado (nível, token, mensagem).
     */
    void erroEntradaDados(Variavel variavel, Erro erro);
    
    /**
     * Realiza a saída de dados de uma determinada expressão como parâmetro.
     * Assim como no método {@link declaracaoVariavel(alog.model.Variavel) },
     * esse método terá uma chamada para cada expressão informada como parâmetro.
     * <br>Caso a expressão tenha resultado inválido, será retornada uma String vazia.
     * @param saida O valor a ser impresso.
     */
    void saidaDados(String saida);
    
    /**
     * Indica a execução de uma operação de atribuição, após a interpretação da expressão.
     * Não tem ação por padrão.
     * @param variavel A variável que receberá o valor.
     * @param valor O valor que será atribuído (após interpretação).
     */
    default void atribuicao(Variavel variavel, String valor){};
    
    /**
     * Indica que a instrução atual indica uma estrutura de controle (condicional ou repetitiva),
     * que possua condição e instrução a ser executada. Por padrão, não tem ação.
     */
    default void estruturaControle(){};
    
    /**
     * Indica que a instrução atual é a instrução do "então" de uma condicional.
     */
    default void condicionalSe(){};
    
    /**
     * Indica que a instrução atual é a instrução do "senão" de uma condicional (caso exista).
     */
    default void condicionalSenao(){};
    
    /**
     * Indica que a instrução atual é a instrução de uma repetição Para-Faça.
     * @param variavel Variável contadora
     */
    default void repeticaoPara(Variavel variavel){};
    
    /**
     * Indica que a instrução atual é a instrução de uma repetição Faça-Enquanto.
     */
    default void repeticaoFaça(){};
    
    /**
     * Indica que a instrução atual é a instrução de uma repetição Enquanto-Faça.
     */
    default void repeticaoEnquanto(){};
    
    /**
     * Indica que a instrução atual é a instrução de uma repetição Repita-Até.
     */
    default void repeticaoRepita(){};
    
    /**
     * Retorna um erro fatal, ou seja, um erro que impeça o interpretador de continuar a execução.
     * @param erro Detalhes do erro ocorrido.
     */
    void erroFatal(Erro erro);
    
}
