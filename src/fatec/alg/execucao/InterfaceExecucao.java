/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.execucao;

import fatec.alg.geral.log.Erro;
import fatec.alg.geral.expressao.Expressao;
import fatec.alg.geral.variavel.Variavel;
import fatec.alg.geral.token.Token;

/**
 * Interface para permitir interação com o interpretador.
 * Para facilitar o desenvolvimento de uma interface básica
 * (que exibe apenas comandos de entrada e saída de dados),
 * a maioria dos métodos tem implementação padrão (default) sem ação.
 * @author Caique Souza
 */
public interface InterfaceExecucao {
    
    /**
     * Avisa à interface que uma nova instrução será executada.
     */
    default void atualizaInstrucao() {};
    
    /**
     * Atualiza à interface qual o passo (token) sendo executado dentro da instrução atual. Para fins de controle.
     * <br>Caso o próximo token seja uma expressão, esse método retornará o resultado de
     * {@link alog.expressao.Expressao#getAsToken() }.
     * @param token token sendo executado.
     */
    default void atualizaPassoAtual(Token token) {};
    
    /**
     * Atualiza à interface qual o passo (token) sendo executado dentro da instrução atual. Para fins de controle.
     * Permite um parâmetro adicional para um token de controle (por exemplo, SE, PARA, FUNÇÃO...)
     * @param controle token da instrução 
     * @param token token sendo executado.
     */
    default void atualizaPassoAtual(Token controle, Token token) {};
    
    /**
     * Atualiza à interface os tokens sendo executados dentro da instrução atual. Para fins de controle.
     * Permite um token de controle (por exemplo, SE, PARA, FUNÇÃO...) e diversos tokens de passo.
     * @param controle token da instrução 
     * @param token Array com tokens do passo atual.
     */
    default void atualizaPassoAtual(Token controle, Token... token) {};
    
    /**
     * Atualiza à interface uma expressão que esteja sendo executada dentro da instrução atual. Para fins de controle.
     * @param expressao expressão sendo executada.
     */
    default void atualizaExpressaoAtual(Expressao expressao) {};
    
    /**
     * Indica que uma expressão foi completamente finalizada.
     */
    default void finalizaExpressao() {};
    
    /**
     * Indica que o passo foi concluído e a próxima instrução pode ser executada.
     */
    default void finalizaPasso() {};
    
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
     * Indica que o valor de uma variável acabou de ser alterado.
     * Pode ser chamado em atribuições ou entradas de dados.
     * @param variavel A variável que acabou de ter o valor alterado.
     */
    default void defineValorVariavel(Variavel variavel){};
    
    /**
     * Indica que o valor de uma variável acabou de ser alterado.
     * Pode ser chamado em atribuições ou entradas de dados.
     * @param identificador O token com o identificador da variável.
     * @param variavel A variável que acabou de ter o valor alterado.
     */
    default void defineValorVariavel(Token identificador, Variavel variavel){};
    
    /**
     * Realiza a leitura de dados para uma variável anteriormente declarada.
     * Assim como no método {@link declaracaoVariavel(alog.model.Variavel) },
     * esse método terá uma chamada para cada variável informada como parâmetro.
     * <br>Após a leitura, o interpretador chamará o método {@link #entradaDadosRetorno() }
     * para obter o valor que foi informado por algum tipo de entrada de dados.
     * <br>Caso o tipo do valor lido não possa ser convertido para o tipo da variável,
     * será chamado o método {@link erroEntradaDados(alog.model.Variavel, alog.analise.Erro)}
     * e depois tornará a chamar este mesmo método.
     * @param variavel A variável (tipo Inteiro) que receberá o valor lido.
     */
    void entradaDados(Variavel variavel);
    
    /**
     * Retorna o valor a ser armazenado em uma variável durante uma entrada de dados.
     * Esse método será usado após o método {@link #entradaDados(alog.model.Variavel) }.
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
     * Indica que uma variável está sendo selecionada em alguma operação.
     * Não tem ação por padrão.
     * @param variavel A variável selecionada na instrução
     */
    default void selecionaVariavel(Variavel variavel){};
    
    /**
     * Retorna um erro fatal, ou seja, um erro que impeça o interpretador de continuar a execução.
     * @param erro Detalhes do erro ocorrido.
     */
    void erroFatal(Erro erro);
    
    /**
     * Indica que a interpretação terminou com sucesso.
     * Mais especificamente, esse método é chamado quando a instrução "FIM"
     * do módulo principal do algoritmo é processada.
     */
    void finalizado();
    
}
