/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.execucao;

import fatec.alg.analise.Verificador;
import fatec.alg.geral.expressao.Expressao;
import fatec.alg.geral.expressao.ChamadaFuncao;
import fatec.alg.geral.expressao.Operacao;
import fatec.alg.geral.expressao.OperacaoUnaria;
import fatec.alg.geral.expressao.Operando;
import fatec.alg.geral.instrucao.Instrucao;
import fatec.alg.geral.instrucao.DeclaracaoVariaveis;
import fatec.alg.geral.instrucao.RepeticaoFaca;
import fatec.alg.geral.instrucao.SaidaDados;
import fatec.alg.geral.instrucao.RepeticaoRepita;
import fatec.alg.geral.instrucao.Condicional;
import fatec.alg.geral.instrucao.RepeticaoEnquanto;
import fatec.alg.geral.instrucao.RepeticaoPara;
import fatec.alg.geral.modulo.Bloco;
import fatec.alg.geral.instrucao.Atribuicao;
import fatec.alg.geral.instrucao.EntradaDados;
import fatec.alg.geral.modulo.ModuloPrincipal;
import fatec.alg.geral.log.Erro;
import fatec.alg.geral.log.TipoErro;
import fatec.alg.execucao.config.ConfigInterpreter;
import fatec.alg.geral.tipo.TipoDado;
import fatec.alg.geral.variavel.Variavel;
import fatec.alg.geral.programa.Programa;
import fatec.alg.geral.token.TipoToken;
import fatec.alg.geral.token.Token;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que recebe um programa e executa as instruções nele contidas.
 * Esse interpretador é baseado em passos, ou seja, executa um passo (token, 
 * parte de expressão ou trecho de instrução) a cada chamada do método
 * {@link #proxima() }.
 * <br>Alguns passos são executados automaticamente por serem processos internos.
 * A configuração de execução desses passos pode ser alterada por configurações
 * no método {@link #setConfigInterpreter(fatec.alg.execucao.config.ConfigInterpreter) }.
 * <br>Interações de entrada ou saída de dados e outras exibições são realizadas por
 * meio da interface {@link InterfaceExecucao}.
 * <br>Operações (aritméticas, lógicas e relacionais) são realizadas por meio da classe
 * {@link Calculator} após execução de quaisquer sub-expressões que compõe a expressão
 * sendo executada. 
 * @author Caique
 * @version 1.0.0
 */
public class Interpreter extends Verificador {
    private static final Logger logger = Logger.getLogger(Interpreter.class.getName());
    
    private class Executavel {
        Instrucao instrucao;
        int passo;
        int total;
        int count;
        
        public Executavel(Instrucao instrucao){
            this.instrucao = instrucao;
            if (instrucao instanceof Expressao) {
                ((Expressao)instrucao).redefine();
            }
            this.passo = 0;
            this.total = 0;
            this.count = 0;
        }

        @Override
        public String toString() {
            return instrucao.getTipo() + " (" + count + " de " + total + ")";
        }
    }
    
    private InterfaceExecucao interfaceExecucao;
    private ConfigInterpreter config;
    private HashMap<String, Variavel> variaveis;
    private Programa programa;
    //private LinkedList<Instrucao> filaInstrucoes;
    private LinkedList<Executavel> pilhaExecucao;
    //private int pos;
    //private int tam;
    private boolean executed;
    private boolean canGo;
    private boolean runNext;
    private boolean locked;

    /**
     * Instancia um interpretador com configurações padrão e em modo de espera.
     * @param interfaceExecucao Interface de controle, entrada e saída
     * @param programa O programa (instruções) a ser executado
     */
    public Interpreter(InterfaceExecucao interfaceExecucao, Programa programa) {
        super();
        this.config = new ConfigInterpreter();
        this.interfaceExecucao = interfaceExecucao;
        this.programa = programa;
        variaveis = new HashMap<>();
        pilhaExecucao = new LinkedList<>();
        executed = false;
        canGo = true;
        runNext = false;
        locked = false;
    }

    /**
     * Define configurações para a execução do programa
     * @param configInterpreter 
     */
    public void setConfigInterpreter(ConfigInterpreter configInterpreter) {
        this.config = configInterpreter;
    }
    
    /**
     * Retorna se ainda existem expressões 
     * @return 
     */
    public boolean existeProxima() {
        return canGo && (!executed || !pilhaExecucao.isEmpty());
    }
    
    public void proxima () {
        /*
         * Bloqueio para evitar casos em que este método é chamado enquanto uma
         * chamada anterior não foi finalizada ainda.
         */
        if (locked) {
            return;
        }
        locked = true;
        
        do {
            /*
             * Caso a execução não seja mais possível (instrução inválida ou
             * simplesmente fim do programa).
             */
            if (!canGo) {
                return;
            }
            /*
             * Desativa execução automática do próximo passo (evita loop infinito)
             */
            runNext = false;
            /*
             * As instruções/expressões a serem executadas sempre estarão na pilha.
             * Caso a pilha ainda não tenha sido inicializada (primeira instrução),
             * adiciona o módulo principal do programa.
             */
            if (pilhaExecucao.isEmpty()) {
                if (!executed) { //Valida que esse programa já foi executado
                    pilhaExecucao.push(new Executavel(programa.getModuloPrincipal()));
                    executed = true;
                    interfaceExecucao.atualizaInstrucao();
                } else {
                    return; //Significa que o programa encerrou.
                }
            }

            Executavel exec;
            try {
                exec = pilhaExecucao.pop();
            } catch (NoSuchElementException ex) {
                Erro fatal = new Erro(TipoErro.ERRO, ' ', 1, 1, 1, 
                    "Falha ao carregar próxima instrução - interpretador finalizado");
                logger.log(Level.WARNING, "{0}:\n {1} - {2}",
                    new Object[]{
                        "Pilha sem elemento de instrução",
                        ex.getClass().getName(),
                        ex.getMessage()
                    });
                erros.add(fatal);
                canGo = false;
                interfaceExecucao.erroFatal(fatal);
                return;
            }

            // Executa a instrução de acordo com seu tipo.
            switch (exec.instrucao.getTipo()) {
                case MODULO_PRINCIPAL:
                    executaModuloPrincipal(exec);
                    break;

                case BLOCO:
                    executaBloco(exec);
                    break;

                case DECLARACAO_VARIAVEL:
                    executaDeclaracaoVariaveis(exec);
                    break;

                case ENTRADA_DE_DADOS:
                    executaEntradaDados(exec);
                    break;

                case SAIDA_DE_DADOS:
                    executaSaidaDados(exec);
                    break;

                case ATRIBUICAO:
                    executaAtribuicao(exec);
                    break;

                case CONDICIONAL:
                    executaCondicional(exec);
                    break;

                case REPETICAO_ENQUANTO:
                    executaRepeticaoEnquanto(exec);
                    break;

                case REPETICAO_FACA:
                    executaRepeticaoFaca(exec);
                    break;

                case REPETICAO_REPITA:
                    executaRepeticaoRepita(exec);
                    break;

                case REPETICAO_PARA:
                    executaRepeticaoPara(exec);
                    break;

                case EXPRESSAO:
                    if (exec.total == 0 || exec.count < exec.total) {
                        //Expressão que ainda tem passos para executar
                        executaExpressao(exec);
                    } else {
                        //Expressão finalizada
                        try {
                            Executavel inst = pilhaExecucao.pop();
                            Expressao expr = (Expressao)exec.instrucao;
                            /*
                             * A manobra abaixo existe porque toda instrução que
                             * contém uma expressão verifica se há uma expressão
                             * no topo da pilha (contendo o resultado esperado).
                             */
                            pilhaExecucao.push(exec); //Expressão finalizada
                            pilhaExecucao.push(inst); //Instrução que espera o resultado da expressão.
                            interfaceExecucao.atualizaExpressaoAtual(expr);
                            runNext = avaliaRunNextCloseExpressao(expr);
                        } catch (NoSuchElementException ex) {
                            Erro fatal = new Erro(TipoErro.ERRO, ' ', 1, 1, 1, 
                                "Falha ao carregar próxima instrução - interpretador finalizado");
                            logger.log(Level.WARNING, "{0}:\n {1} - {2}",
                                new Object[]{
                                    "Pilha sem elemento de instrução",
                                    ex.getClass().getName(),
                                    ex.getMessage()
                                });
                            erros.add(fatal);
                            canGo = false;
                            interfaceExecucao.erroFatal(fatal);
                        }
                    }
                    break;

                default:
                    logger.log(Level.WARNING, "{0} {1} {2}",
                        new Object[]{
                            "Instrução \"",
                            exec.instrucao.getTipo(),
                            "\" não esperada"
                        });
                    break;
            }

        } while (runNext) ;
        
        locked = false;
    }
    
    private void executaModuloPrincipal (Executavel exec) {
        ModuloPrincipal moduloPrincipal = (ModuloPrincipal) exec.instrucao;
        if (exec.total == 0) {
            exec.total = 4;
            if (!moduloPrincipal.isDeclarado()) {
                exec.count = 1;
            } 
        }
        switch (exec.count) {
            case 0:
                /*
                 * Passo que exibe declaração de módulo ("Algoritmo NOME").
                 * Módulos não declarados (leia-se: algoritmos que iniciam com
                 * "Início" pulam esse passo.
                 */
                Token token = geraTokenExib(moduloPrincipal.getTipoModulo(), moduloPrincipal.getNome());
                interfaceExecucao.atualizaPassoAtual(token);
                exec.count ++;
                pilhaExecucao.push(exec);
                break;
            case 1:
                /*
                 * Passo que exibe o token "Início".
                 */
                interfaceExecucao.atualizaPassoAtual(moduloPrincipal.getInicio());
                exec.count ++;
                pilhaExecucao.push(exec);
                break;
            case 2:
                /*
                 * Passo que controla a instrução em execução enquanto houverem
                 * instruções a serem executadas.
                 */
                Instrucao instrucao = moduloPrincipal.getInstrucaoAt(exec.passo++);
                if (instrucao != null) {
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(instrucao));
                    interfaceExecucao.atualizaInstrucao();
                } else {
                    exec.count ++;
                    pilhaExecucao.push(exec);
                }
                runNext = config.getBoolean(ConfigInterpreter.RUNNEXT_BLOCO_PILHA);
                break;
            case 3:
                /*
                 * Passo que exibe/seleciona o token "Fim" e informa à interface
                 * que a execução do algoritmo terminou.
                 */
                interfaceExecucao.atualizaPassoAtual(moduloPrincipal.getFim());
                interfaceExecucao.finalizado();
                break;
        }
    }
    
    private void executaBloco (Executavel exec) {
        Bloco bloco = (Bloco) exec.instrucao;
        if (exec.total == 0) {
            exec.total = 3;
        } 
        switch (exec.count) {
            case 0:
                /*
                 * Passo que exibe o token "Início".
                 */
                interfaceExecucao.atualizaPassoAtual(bloco.getInicio());
                exec.count ++;
                pilhaExecucao.push(exec);
                break;
            case 1:
                /*
                 * Passo que controla a instrução em execução enquanto houverem
                 * instruções a serem executadas.
                 */
                Instrucao instrucao = bloco.getInstrucaoAt(exec.passo++);
                if (instrucao != null) {
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(instrucao));
                    interfaceExecucao.atualizaInstrucao();
                } else {
                    exec.count ++;
                    pilhaExecucao.push(exec);
                }
                runNext = config.getBoolean(ConfigInterpreter.RUNNEXT_BLOCO_PILHA);
                break;
            case 2:
                /*
                 * Passo que exibe/seleciona o token "Fim".
                 */
                interfaceExecucao.atualizaPassoAtual(bloco.getFim());
                break;
        }
    }
    
    private void executaDeclaracaoVariaveis(Executavel exec) {
        DeclaracaoVariaveis declaracaoVariaveis = (DeclaracaoVariaveis) exec.instrucao;
        if (exec.total == 0) {
            exec.total = declaracaoVariaveis.getNumVariaveis();
        }
        /*
         * Executa enquanto houverem variáveis a declarar.
         */
        if (exec.count < exec.total) {
            Token token = declaracaoVariaveis.getTokensNomesVariaveis().get(exec.count);
            interfaceExecucao.atualizaPassoAtual(declaracaoVariaveis.getTokenTipoVariavel(), token);
            Variavel variavel = new Variavel(
                    declaracaoVariaveis.getTipoVariavel(),
                    token.getPalavra()
            );
            variaveis.put(token.nome(), variavel);
            interfaceExecucao.declaracaoVariavel(variavel);
            exec.count ++;
        }
        if (exec.count < exec.total) {
            pilhaExecucao.push(exec);
        }
    }
    
    private void executaEntradaDados(Executavel exec) {
        EntradaDados entradaDados = (EntradaDados) exec.instrucao;
        if (exec.total == 0) {
            exec.total = 2;
        }
        
        Token token = entradaDados.getParametroAt(exec.passo);
        Variavel variavel = variaveis.get(token.nome());
        interfaceExecucao.atualizaPassoAtual(entradaDados.getNome(), token);
        interfaceExecucao.selecionaVariavel(variavel);
        
        if (exec.count < exec.total) {
            switch (exec.count) {
                case 0:
                    /*
                     * Passo em que informa-se à interface qual a variável que
                     * está sendo lida.
                     */
                    interfaceExecucao.entradaDados(variavel);
                    exec.count ++;
                    break;
                case 1:
                    /*
                     * Passo em que se obtém o valor lido e armazena na variável.
                     */
                    String retorno = interfaceExecucao.entradaDadosRetorno();
                    if (retorno == null) {
                        Erro erro = new Erro(TipoErro.ERRO, token, 
                            "Falha ao realizar entrada de dados - interpretador finalizado");
                        logger.log(Level.WARNING, "Retorno nulo");
                        erros.add(erro);
                        canGo = false;
                        return;
                    } 
                    exec.count = 0;
                    try {
                        switch (variavel.getTipo()){
                            case INTEIRO:
                                variavel.setValorInteiro(Long.parseLong(retorno));
                                break;
                            case REAL:
                                variavel.setValorReal(Double.parseDouble(retorno));
                                break;
                            case CARACTER:
                                variavel.setValorCaracter(retorno);
                                break;
                            default:
                                variavel.setValor(retorno);
                                break;
                        }
                        
                        exec.passo ++;
                        variaveis.replace(token.nome(), variavel);
                        interfaceExecucao.defineValorVariavel(token, variavel);
                        
                        runNext = 
                                exec.passo < entradaDados.getNumParametros() &&
                                config.getBoolean(ConfigInterpreter.RUNNEXT_LEIA_ATRIB);
                    } catch (NumberFormatException ex) {
                        Erro erro = new Erro(TipoErro.ERRO, token, 
                            "Valor informado não era do tipo " + variavel.getTipo().toString() + " - tente novamente");
                        erros.add(erro);
                        logger.log(Level.WARNING, "{0} {1}:\n {2} - {3}",
                        new Object[]{
                            "Conversão não realizada para tipo",
                            variavel.getTipo().toString(),
                            ex.getClass().getName(),
                            ex.getMessage()
                        });
                        interfaceExecucao.erroEntradaDados(variavel, erro);
                        runNext = true;
                    }
                    break;
            } 
        }
        if (exec.passo < entradaDados.getNumParametros()) {
            pilhaExecucao.push(exec);
        } else {
            exec.count ++;
        }
    }
    
    private void executaSaidaDados(Executavel exec) {
        SaidaDados saidaDados = (SaidaDados)exec.instrucao;
        if (exec.total == 0) {
            exec.total = saidaDados.getNumParametros();
        }
        if (exec.count < exec.total) {
            Expressao expressao = removeExpressaoDaPilha();
            if (expressao == null) {
                expressao = saidaDados.getParametroAt(exec.count);
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
                interfaceExecucao.atualizaPassoAtual(saidaDados.getTokenNome(), expressao.getAsToken());
                interfaceExecucao.atualizaExpressaoAtual(expressao);
                runNext = avaliaRunNextExecExpressao(expressao);
            } else {
                interfaceExecucao.expressaoFinalizada();
                interfaceExecucao.atualizaPassoAtual(saidaDados.getTokenNome(), expressao.getAsToken());
                if (expressao.getTipoResultado() == TipoDado.CARACTER) {
                    String impressao = expressao.getResultadoCaracter();
                    if (config.getBoolean(ConfigInterpreter.FORMAT_ESCREVA_ESPACO) &&
                        config.getBoolean(ConfigInterpreter.FORMAT_ESCREVA_ESPACO_TRIM)) {
                        impressao = impressao.trim();
                    }
                    interfaceExecucao.saidaDados(impressao);
                } else {
                    interfaceExecucao.saidaDados(expressao.getResultado());
                }
                exec.count++;
                if (exec.count < exec.total) {
                    if (config.getBoolean(ConfigInterpreter.FORMAT_ESCREVA_ESPACO)) {
                        interfaceExecucao.saidaDados(" ");
                    }
                    pilhaExecucao.push(exec);
                } else {
                    if (config.getBoolean(ConfigInterpreter.FORMAT_ESCREVA_QUEBRA)) {
                        interfaceExecucao.saidaDados("\n");
                    }
                }
            }
        }
    }
    
    private void executaAtribuicao(Executavel exec) {
        Atribuicao atribuicao = (Atribuicao)exec.instrucao;
        if (exec.total == 0) {
            exec.total = 1;
        }
        if (exec.total == exec.count) return;
        
        Expressao expressao = removeExpressaoDaPilha();
        if (expressao == null) {
            expressao = atribuicao.getExpressao();
            pilhaExecucao.push(exec);
            pilhaExecucao.push(new Executavel(expressao));
            interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
            interfaceExecucao.atualizaExpressaoAtual(expressao);
            runNext = avaliaRunNextExecExpressao(expressao);
        } else {
            interfaceExecucao.expressaoFinalizada();
            Token token = atribuicao.getVariavel();
            Variavel variavel = variaveis.get(token.nome());
            interfaceExecucao.selecionaVariavel(variavel);
            String retorno = expressao.getResultado();
            
            TipoDado[] esperado;
            switch (variavel.getTipo()){
                case INTEIRO:
                    esperado = new TipoDado[]{TipoDado.INTEIRO};
                    break;
                case REAL:
                    esperado = new TipoDado[]{TipoDado.INTEIRO, TipoDado.REAL};
                    break;
                case CARACTER:
                    esperado = new TipoDado[]{TipoDado.CARACTER};
                    break;
                default:
                    esperado = new TipoDado[] {};
            }
            
            if (tiposDadosCorretos(expressao.getTipoResultado(), esperado)) {
                variavel.setValor(retorno);
                variaveis.replace(token.nome(), variavel);
                interfaceExecucao.atualizaPassoAtual(token, expressao.getAsToken());
                interfaceExecucao.defineValorVariavel(token, variavel);
                exec.count++;
            } else {
                logger.log(Level.WARNING, "{0} {1} (tipo {2}) {3} tipo {4}",
                    new Object[]{
                        "Não pode atribuir valor",
                        retorno,
                        expressao.getTipoResultado(),
                        "a variável",
                        variavel.getTipo()
                    });
                Erro erro = new Erro(TipoErro.ERRO, token, String.format(
                    "Falha ao realizar atribuição: não pode atribuir valor %s a variável de tipo %s"
                    + " - interpretador finalizado", expressao.getTipoResultado(), variavel.getTipo()));
                interfaceExecucao.erroFatal(erro);
                erros.add(erro);
                canGo = false;
            }
        }
    }
    
    private void executaCondicional(Executavel exec) {
        Condicional condicional = (Condicional)exec.instrucao;
        if (exec.total == 0) {
            exec.total = condicional.isComposta() ? 3 : 2;
        }
        if (exec.count == 0) { // SE (Condição)
            Expressao expressao = removeExpressaoDaPilha();
            if (expressao == null) {
                expressao = condicional.getCondicao();
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
                interfaceExecucao.atualizaPassoAtual(condicional.getTokenSe(), expressao.getAsToken());
                interfaceExecucao.atualizaExpressaoAtual(expressao);
                runNext = avaliaRunNextExecExpressao(expressao);
            } else {
                interfaceExecucao.expressaoFinalizada();
                if (!tiposDadosCorretos(expressao.getTipoResultado(), TipoDado.LOGICO)) {
                    logger.log(Level.WARNING, "Não interpretou resultado {0} (tipo {1}) como lógico para condicional",
                    new Object[]{
                        expressao.getResultado(),
                        expressao.getTipoResultado()
                    });
                    Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), String.format(
                        "Esperava um resultado lógico para a condição, mas encontrou %s"
                        + " - interpretador finalizado", expressao.getTipoResultado()));
                    interfaceExecucao.erroFatal(erro);
                    erros.add(erro);
                    canGo = false;
                    return;
                }

                if (expressao.getResultadoLogico()) {
                    exec.count += 1;
                } else {
                    exec.count += 2;
                }
            }
        }
        
        switch (exec.count) { 
            case 1: // Então INSTRUÇÃO
                exec.count += 2;
                if (condicional.isComposta()) {
                    pilhaExecucao.push(exec);
                }
                pilhaExecucao.push(new Executavel(condicional.getInstrucaoSe()));
                interfaceExecucao.atualizaInstrucao();
                break;
            case 2: // Senão INSTRUÇÃO (caso seja composta)
                if (condicional.isComposta()) {
                    interfaceExecucao.atualizaPassoAtual(condicional.getTokenSenao());
                    pilhaExecucao.push(new Executavel(condicional.getInstrucaoSenao()));
                    interfaceExecucao.atualizaInstrucao();
                    exec.count ++;
                }
                break;
            default:
                break;
        }
    }
    
    private void executaRepeticaoEnquanto(Executavel exec) {
        RepeticaoEnquanto repetitiva = (RepeticaoEnquanto)exec.instrucao;
        if (exec.total == 0) {
            exec.total = 2;
        }
        switch (exec.count) {
            case 0: // Enquanto...
                Expressao expressao = removeExpressaoDaPilha();
                if (expressao == null) {
                    expressao = repetitiva.getCondicao();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenEnquanto(), expressao.getAsToken());
                    interfaceExecucao.atualizaExpressaoAtual(expressao);
                    runNext = avaliaRunNextExecExpressao(expressao);
                } else {
                    interfaceExecucao.expressaoFinalizada();
                    if (!tiposDadosCorretos(expressao.getTipoResultado(), TipoDado.LOGICO)) {
                        logger.log(Level.WARNING, "Não interpretou resultado {0} (tipo {1}) como lógico para repetição",
                        new Object[]{
                            expressao.getResultado(),
                            expressao.getTipoResultado()
                        });
                        Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), String.format(
                            "Esperava um resultado lógico para a condição, mas encontrou %s"
                            + " - interpretador finalizado", expressao.getTipoResultado()));
                        erros.add(erro);
                        interfaceExecucao.erroFatal(erro);
                        canGo = false;
                        return;
                    }

                    if (expressao.getResultadoLogico()) {
                        exec.count += 1;
                    } else {
                        exec.count += 2;
                    }
                    pilhaExecucao.push(exec);
                }
                break;
            case 1: // Instrução a ser repetida
                exec.count = 0; // para repetir
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(repetitiva.getInstrucao()));
                interfaceExecucao.atualizaInstrucao();
                break;
        } 
    }
    
    private void executaRepeticaoFaca(Executavel exec) {
        RepeticaoFaca repetitiva = (RepeticaoFaca)exec.instrucao;
        if (exec.total == 0) {
            exec.total = 3;
        }
        
        switch (exec.count) {
            case 0: // Faça 
                interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenFaca());
                exec.count ++;
                pilhaExecucao.push(exec);
                break;
            case 1: // Instrução a ser repetida
                exec.count ++;
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(repetitiva.getInstrucao()));
                interfaceExecucao.atualizaInstrucao();
                break;
            case 2: // Enquanto
                Expressao expressao = removeExpressaoDaPilha();
                if (expressao == null) {
                    expressao = repetitiva.getCondicao();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                    interfaceExecucao.atualizaExpressaoAtual(expressao);
                    runNext = avaliaRunNextExecExpressao(expressao);
                } else {
                    interfaceExecucao.expressaoFinalizada();
                    if (!tiposDadosCorretos(expressao.getTipoResultado(), TipoDado.LOGICO)) {
                        logger.log(Level.WARNING, "Não interpretou resultado {0} (tipo {1}) como lógico para repetição",
                        new Object[]{
                            expressao.getResultado(),
                            expressao.getTipoResultado()
                        });
                        Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), String.format(
                            "Esperava um resultado lógico para a condição, mas encontrou %s"
                            + " - interpretador finalizado", expressao.getTipoResultado()));
                        erros.add(erro);
                        interfaceExecucao.erroFatal(erro);
                        canGo = false;
                        return;
                    }

                    if (expressao.getResultadoLogico()) {
                        exec.count = 1;
                        pilhaExecucao.push(exec);
                    } else {
                        exec.count ++;
                    }
                }
                break;
        }
    }
    
    private void executaRepeticaoRepita(Executavel exec) {
        RepeticaoRepita repetitiva = (RepeticaoRepita)exec.instrucao;
        if (exec.total == 0) {
            exec.total = 3;
        }
        
        switch (exec.count) {
            case 0: // Repita
                interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenRepita());
                exec.count ++;
                pilhaExecucao.push(exec);
                break;
            case 1: // Instrução a ser repetida
                exec.count ++;
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(repetitiva.getInstrucao()));
                interfaceExecucao.atualizaInstrucao();
                break;
            case 2: // Até
                Expressao expressao = removeExpressaoDaPilha();
                if (expressao == null) {
                    expressao = repetitiva.getCondicao();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                    interfaceExecucao.atualizaExpressaoAtual(expressao);
                    runNext = avaliaRunNextExecExpressao(expressao);
                } else {
                    interfaceExecucao.expressaoFinalizada();
                    if (!tiposDadosCorretos(expressao.getTipoResultado(), TipoDado.LOGICO)) {
                        logger.log(Level.WARNING, "Não interpretou resultado {0} (tipo {1}) como lógico para repetição",
                        new Object[]{
                            expressao.getResultado(),
                            expressao.getTipoResultado()
                        });
                        Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), String.format(
                            "Esperava um resultado lógico para a condição, mas encontrou %s"
                            + " - interpretador finalizado", expressao.getTipoResultado()));
                        erros.add(erro);
                        canGo = false;
                        interfaceExecucao.erroFatal(erro);
                        return;
                    }

                    if (!expressao.getResultadoLogico()) {
                        exec.count = 1;
                        pilhaExecucao.push(exec);
                    } else {
                        exec.count ++;
                    }
                }
                break;
        }
    }
    
    private void executaRepeticaoPara(Executavel exec) {
        RepeticaoPara repetitiva = (RepeticaoPara)exec.instrucao;
        if (exec.total == 0) {
            exec.total = 6;
        }
        
        Token tokenVarCont = repetitiva.getVariavelCont();
        Variavel variavel = variaveis.get(tokenVarCont.nome());
        
        switch (exec.count) {
            case 0: // Para VARIAVEL
                interfaceExecucao.selecionaVariavel(variavel);
                interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenPara(), tokenVarCont);
                
                if (!tiposDadosCorretos(variavel.getTipo(), TipoDado.INTEIRO, TipoDado.REAL)){
                    logger.log(Level.WARNING, "Variável de tipo inválido: {0}",
                        new Object[]{
                            variavel.getTipo()
                        });
                    Erro erro = new Erro(TipoErro.ERRO, tokenVarCont, String.format(
                        "Variável contadora deve ser numérica (inteira ou real), mas encontrou %s "
                                + "- interpretador finalizado", variavel.getTipo()));
                    interfaceExecucao.erroFatal(erro);
                    erros.add(erro);
                    canGo = false;
                    return;
                }
                
                exec.count ++;
                pilhaExecucao.push(exec);
                break;
            case 1: // De EXPRESSAO
                Expressao expressao = removeExpressaoDaPilha();
                if (expressao == null) {
                    expressao = repetitiva.getExpressaoDe();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenPara(), expressao.getAsToken());
                    interfaceExecucao.atualizaExpressaoAtual(expressao);
                    runNext = avaliaRunNextExecExpressao(expressao);
                } else {
                    interfaceExecucao.expressaoFinalizada();
                    TipoDado[] esperados;
                    if (variavel.getTipo() == TipoDado.INTEIRO) {
                        esperados = new TipoDado[] {TipoDado.INTEIRO};
                    } else {
                        esperados = new TipoDado[] {TipoDado.INTEIRO, TipoDado.REAL};
                    }
                    
                    if (tiposDadosCorretos(expressao.getTipoResultado(), esperados)) {
                        variavel.setValor(expressao.getResultado());
                        variaveis.replace(tokenVarCont.nome(), variavel);
                        interfaceExecucao.defineValorVariavel(tokenVarCont, variavel);
                        exec.count ++;
                        pilhaExecucao.push(exec);
                    } else {
                        logger.log(Level.WARNING, "Não interpretou resultado {0} (tipo {1}) como numérico para incremento de repetição",
                        new Object[]{
                            expressao.getResultado(),
                            expressao.getTipoResultado()
                        });
                        Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), String.format(
                            "Esperava um resultado %s para início de repetição, mas encontrou %s"
                            + " - interpretador finalizado", variavel.getTipo(), expressao.getTipoResultado()));
                        interfaceExecucao.erroFatal(erro);
                        erros.add(erro);
                        canGo = false;
                    }
                }
                break;
            case 2: // Até EXPRESSAO 
                Token result = null;
                expressao = removeExpressaoDaPilha();
                if (expressao == null) {
                    expressao = repetitiva.getExpressaoAte();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenPara(), expressao.getAsToken());
                    interfaceExecucao.atualizaExpressaoAtual(expressao);
                    runNext = avaliaRunNextExecExpressao(expressao);
                } else {
                    interfaceExecucao.expressaoFinalizada();
                    TipoDado[] esperados;
                    if (variavel.getTipo() == TipoDado.INTEIRO) {
                        esperados = new TipoDado[] {TipoDado.INTEIRO};
                    } else {
                        esperados = new TipoDado[] {TipoDado.INTEIRO, TipoDado.REAL};
                    }
                    
                    if (tiposDadosCorretos(expressao.getTipoResultado(), esperados)) {
                        result = new Token(
                                tokenVarCont.getLinha(),
                                tokenVarCont.getColuna() + tokenVarCont.getTamanho() + 4,
                                tokenVarCont.getPosicao() + tokenVarCont.getTamanho() + 4,
                                tokenVarCont.getOrdem() + 2
                        );
                        result.setPalavra(expressao.getResultado());
                        result.setTipoToken(TipoToken.NUMERICO);

                        Operando operVar = new Operando();
                        operVar.setOperando(tokenVarCont);
                        operVar.setTipoResultado(variavel.getTipo());

                        Operando operLim = new Operando();
                        operLim.setOperando(result);
                        operLim.setResultado(result.getPalavra());
                        operLim.setTipoResultado(TipoDado.mapTokenToVariavel(result));

                        Token sigCond = new Token(
                                tokenVarCont.getLinha(),
                                tokenVarCont.getColuna() + tokenVarCont.getTamanho() + 2,
                                tokenVarCont.getPosicao() + tokenVarCont.getTamanho() + 2,
                                tokenVarCont.getOrdem() + 1
                        );
                        
                        Token sigPas = new Token(
                                tokenVarCont.getLinha(),
                                tokenVarCont.getColuna() + tokenVarCont.getTamanho() + 2,
                                tokenVarCont.getPosicao() + tokenVarCont.getTamanho() + 2,
                                tokenVarCont.getOrdem() + 1
                        );
                        
                        Token pas = new Token(
                                sigPas.getLinha(),
                                sigPas.getColuna() + sigPas.getTamanho() + 2,
                                sigPas.getPosicao() + sigPas.getTamanho() + 2,
                                sigPas.getOrdem() + 1
                        );
                        pas.setPalavra("1");
                        
                        boolean passoAsc;
                        if (variavel.getTipo() == TipoDado.INTEIRO) {
                            if (operLim.getTipoResultado() == TipoDado.INTEIRO) {
                                passoAsc = variavel.getValorInteiro() <= operLim.getResultadoInteiro();
                            } else {
                                passoAsc = variavel.getValorInteiro() <= operLim.getResultadoReal();
                            }
                        } else {
                            if (operLim.getTipoResultado() == TipoDado.INTEIRO) {
                                passoAsc = variavel.getValorReal() <= operLim.getResultadoInteiro();
                            } else {
                                passoAsc = variavel.getValorReal() <= operLim.getResultadoReal();
                            }
                        }
                        if (passoAsc){
                            sigCond.setPalavra("<=");
                            sigPas.setPalavra("+");
                        } else {
                            sigCond.setPalavra(">=");
                            sigPas.setPalavra("-");
                        }
                        sigCond.setTipoToken(TipoToken.OPERADOR);
                        sigPas.setTipoToken(TipoToken.OPERADOR);
                        pas.setTipoToken(TipoToken.NUMERICO);
                        
                        Operando operPas = new Operando();
                        operPas.setOperando(pas);
                        operPas.setTipoResultado(TipoDado.mapTokenToVariavel(pas));
                        operPas.setResultado(pas.getPalavra());
                        
                        Operacao condicao = new Operacao();
                        condicao.setExpressaoEsq(operVar);
                        condicao.setExpressaoDir(operLim);
                        condicao.setOperador(sigCond);
                        
                        Operacao passo = new Operacao();
                        passo.setExpressaoEsq(operVar);
                        passo.setExpressaoDir(operPas);
                        passo.setOperador(sigPas);

                        repetitiva.setExpressaoPasso(passo);
                        repetitiva.setExpressao(condicao);
                        exec.instrucao = repetitiva;
                        exec.count ++;
                        pilhaExecucao.push(exec);
                    } else {
                        logger.log(Level.WARNING, "Não interpretou resultado {0} (tipo {1}) como numérico para incremento de repetição",
                        new Object[]{
                            expressao.getResultado(),
                            expressao.getTipoResultado()
                        });
                        Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), String.format(
                            "Esperava um resultado %s para início de repetição, mas encontrou %s"
                            + " - interpretador finalizado", variavel.getTipo(), expressao.getTipoResultado()));
                        interfaceExecucao.erroFatal(erro);
                        erros.add(erro);
                        canGo = false;
                    }
                }
                break;
               
            case 3: // Faça INSTRUÇÕES
                expressao = removeExpressaoDaPilha();
                if (expressao == null) {
                    exec.passo ++;
                    expressao = repetitiva.getCondicao();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenPara(), 
                            tokenVarCont, repetitiva.getExpressaoAte().getAsToken());
                    interfaceExecucao.atualizaExpressaoAtual(expressao);
                } else {
                    interfaceExecucao.expressaoFinalizada();
                    if (expressao.getResultadoLogico()) {
                        interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenPara(), repetitiva.getTokenFaca());
                        exec.count += 1;
                        pilhaExecucao.push(exec);
                        runNext = config.getBoolean(ConfigInterpreter.RUNNEXT_PARA_LOOP);
                    } else {
                        exec.count += 2;
                    }
                }
                break;
                
            case 4:
                exec.count ++; // para incrementar
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(repetitiva.getInstrucao()));
                interfaceExecucao.atualizaInstrucao();
                break;
                
            case 5:
                expressao = removeExpressaoDaPilha();
                if (expressao == null) {
                    expressao = repetitiva.getExpressaoPasso();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenPara(), 
                            tokenVarCont);
                    interfaceExecucao.atualizaExpressaoAtual(expressao);
                    runNext = avaliaRunNextExecExpressao(expressao);
                } else {
                    interfaceExecucao.expressaoFinalizada();
                    interfaceExecucao.expressaoFinalizada();
                    interfaceExecucao.selecionaVariavel(variavel);
                    String retorno = expressao.getResultado();
                    variavel.setValor(retorno);
                    variaveis.replace(tokenVarCont.nome(), variavel);
                    interfaceExecucao.defineValorVariavel(tokenVarCont, variavel);
                    exec.count = 3; // para repetir
                    pilhaExecucao.push(exec);
                }
                break;
        } 
    }
    
    private boolean avaliaRunNextExecExpressao(Expressao expressao) {
        switch (expressao.getTipoExpressao()) {
            case OPERANDO_CONSTANTE:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_EXEC_CONST);
            case OPERANDO_VARIAVEL:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_EXEC_VAR);
            case OPERANDO_FUNCAO:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_EXEC_FUNC);
            case OPERACAO_UNARIA:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_EXEC_UNARIA);
            case OPERACAO_ARITMETICA:
            case OPERACAO_RELACIONAL:
            case OPERACAO_LOGICA:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_EXEC_OPBIN);
            default:
                return false;
        }
    }
    
    private boolean avaliaRunNextCloseExpressao(Expressao expressao) {
        switch (expressao.getTipoExpressao()) {
            case OPERANDO_CONSTANTE:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_FIN_CONST);
            case OPERANDO_VARIAVEL:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_FIN_VAR);
            case OPERANDO_FUNCAO:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_FIN_FUNC);
            case OPERACAO_UNARIA:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_FIN_UNARIA);
            case OPERACAO_ARITMETICA:
            case OPERACAO_RELACIONAL:
            case OPERACAO_LOGICA:
                return config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_FIN_OPBIN);
            default:
                return false;
        }
    }
    
    private void executaExpressao(Executavel exec) {
        Expressao expressao = (Expressao)exec.instrucao;
        switch (expressao.getTipoExpressao()) {
            case OPERANDO_CONSTANTE:
                executaExpressaoOperandoConstante(exec);
                break;
            case OPERANDO_VARIAVEL:
                executaExpressaoOperandoVariavel(exec);
                break;
            case OPERANDO_FUNCAO:
                executaExpressaoOperandoFuncao(exec);
                break;
            case OPERACAO_UNARIA:
                executaExpressaoOperacaoUnaria(exec);
                break;
            case OPERACAO_ARITMETICA:
            case OPERACAO_RELACIONAL:
            case OPERACAO_LOGICA:
                executaExpressaoOperacao(exec);
                break;
            default:
                break;
        }
    }
    
    private void executaExpressaoOperandoConstante(Executavel exec) {
        Operando operando = (Operando)exec.instrucao;
        if (exec.total == 0) {
            exec.total = 1;
        }
        Token token = operando.getOperando();
        operando.setTipoResultado(TipoDado.mapTokenToVariavel(token));
        operando.setResultado(token.getPalavra());
        
        exec.instrucao = operando;
        exec.count ++;
        pilhaExecucao.push(exec);
        runNext = config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_RES_CONST);
    }
    
    private void executaExpressaoOperandoVariavel(Executavel exec) {
        Operando operando = (Operando)exec.instrucao;
        if (exec.total == 0) {
            exec.total = 1;
        }
        Token token = operando.getOperando();
        Variavel variavel = variaveis.get(token.nome());
        interfaceExecucao.selecionaVariavel(variavel);
        if (!variavel.isInicializada()) {
            Erro fatal = new Erro(TipoErro.ERRO, token, 
                "Variável não inicializada");
            erros.add(fatal);
            canGo = false;
            interfaceExecucao.erroFatal(fatal);
        } else {
            operando.setResultado(variavel);
            exec.instrucao = operando;
            exec.count ++;
            pilhaExecucao.push(exec);
            runNext = config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_RES_VAR);
        }
    }
    
    private void executaExpressaoOperandoFuncao (Executavel exec) {
        ChamadaFuncao chamadaFuncao = (ChamadaFuncao)exec.instrucao;
        int npar = chamadaFuncao.getNumParametros();
        Token nome = chamadaFuncao.getOperando();
        if (exec.total == 0) {
            exec.total = npar + 1;
            if (nome.nome().equals("pot") && npar != 2) {
                Erro fatal = new Erro(TipoErro.ERRO, nome, 
                    "Número de parâmetros incorretos - esperava 2, recebeu " + npar);
                erros.add(fatal);
                canGo = false;
                interfaceExecucao.erroFatal(fatal);
            }
            if (nome.nome().equals("raiz") && npar != 1) {
                Erro fatal = new Erro(TipoErro.ERRO, nome, 
                    "Número de parâmetros incorretos - esperava 1, recebeu " + npar);
                erros.add(fatal);
                canGo = false;
                interfaceExecucao.erroFatal(fatal);
            }
        }
        
        if (exec.count < exec.total - 1) {
            Expressao expressao = removeExpressaoDaPilha();
            if (expressao == null) {
                expressao = chamadaFuncao.getParametros().get(exec.count);
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
                runNext = avaliaRunNextExecExpressao(expressao);
            } else {
                if (tiposDadosCorretos(expressao.getTipoResultado(), esperadosPorFuncao(nome.nome())[exec.count])) {
                    chamadaFuncao.atualizaParametro(exec.count, expressao);
                    exec.count ++;
                    pilhaExecucao.push(exec);
                    runNext = config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_RES_FUNC);
                } else {
                    logger.log(Level.WARNING, "Não interpretou resultado {0} (tipo {1}) como parâmetro {2} de função {3}",
                        new Object[]{
                            expressao.getResultado(),
                            expressao.getTipoResultado(),
                            exec.count + 1, 
                            nome.getPalavra()
                        });
                    Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), String.format(
                        "Esperava um resultado %s para o parâmetro, mas encontrou %s"
                        + " - interpretador finalizado", "Inteiro ou real", expressao.getTipoResultado()));
                    interfaceExecucao.erroFatal(erro);
                    erros.add(erro);
                    canGo = false;
                }
            }
        } else {
            if (exec.count < exec.total) {
                double result = 0;
                switch (nome.nome()) {
                    case "pot":
                        double base = chamadaFuncao.getParametros().get(0).getResultadoReal();
                        double expo = chamadaFuncao.getParametros().get(1).getResultadoReal();
                        result = Math.pow(base, expo);
                        break;
                    case "raiz":
                        double num = chamadaFuncao.getParametros().get(0).getResultadoReal();
                        if (num >= 0) {
                            result = Math.sqrt(num);
                        }
                        break;
                    default:
                        break;
                }
                
                chamadaFuncao.setTipoResultado(TipoDado.REAL);
                chamadaFuncao.setResultado(String.valueOf(result));
                exec.count ++;
                pilhaExecucao.push(exec);
            }
        }
    }
    
    private void executaExpressaoOperacaoUnaria (Executavel exec) {
        OperacaoUnaria operacaoUnaria = (OperacaoUnaria)exec.instrucao;
        Token operador = operacaoUnaria.getOperador();
        if (exec.total == 0) {
            exec.total = 1;
        }
        
        if (exec.count < exec.total) {
            Expressao expressao = removeExpressaoDaPilha();
            if (expressao == null) {
                expressao = operacaoUnaria.getExpressao();
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
                runNext = avaliaRunNextExecExpressao(expressao);
            } else {
                Calculator calc = new Calculator(expressao);
                Calculator res = null;
                switch (operador.getFuncaoToken()) {
                    case OP_SIG_NEGATIVO:
                        res = calc.negativo();
                        break;
                    case OP_SIG_POSITIVO:
                        res = calc.positivo();
                        break;
                    case OP_LOG_NAO:
                        res = calc.nao();
                        break;
                    default:
                        break;
                }
                if (res == null) {
                    Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), "Expressão sem resultado");
                    interfaceExecucao.erroFatal(erro);
                    erros.add(erro);
                    canGo = false;
                } else {
                    operacaoUnaria.setResultado(res);
                    exec.instrucao = operacaoUnaria;
                    exec.count ++;
                    pilhaExecucao.push(exec);
                    runNext = config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_RES_UNARIA);
                }
            }
        }
    }
    
    private void executaExpressaoOperacao (Executavel exec) {
        Operacao operacao = (Operacao)exec.instrucao;
        Token operador = operacao.getOperador();
        if (exec.total == 0) {
            exec.total = 3;
        }
        
        if (exec.count < exec.total) {
            Expressao expressao = null;
            
            switch (exec.count) {
                case 0:
                    expressao = removeExpressaoDaPilha();
                    if (expressao == null) {
                        expressao = operacao.getExpressaoEsq();
                        pilhaExecucao.push(exec);
                        pilhaExecucao.push(new Executavel(expressao));
                    } else {
                        operacao.atualizaExpressaoEsq(expressao);
                        exec.count ++;
                        pilhaExecucao.push(exec);
                    }
                    runNext = avaliaRunNextExecExpressao(expressao);
                    break;
                case 1:
                    expressao = removeExpressaoDaPilha();
                    if (expressao == null) {
                        expressao = operacao.getExpressaoDir();
                        pilhaExecucao.push(exec);
                        pilhaExecucao.push(new Executavel(expressao));
                    } else {
                        operacao.atualizaExpressaoDir(expressao);
                        exec.count ++;
                        pilhaExecucao.push(exec);
                    }
                    runNext = avaliaRunNextExecExpressao(expressao);
                    break;
                case 2:
                    Calculator calc = new Calculator(operacao.getExpressaoEsq());
                    Calculator oper = new Calculator(operacao.getExpressaoDir());
                    Calculator res = null;
                    switch (operador.getFuncaoToken()) {
                        case OP_MAT_SOMA:
                            res = calc.soma(oper);
                            break;
                        case OP_MAT_SUBTRACAO:
                            res = calc.subtr(oper);
                            break;
                        case OP_MAT_MULTIPLICACAO:
                            res = calc.mult(oper);
                            break;
                        case OP_MAT_DIV_REAL:
                            res = calc.divReal(oper);
                            break;
                        case OP_MAT_DIV_INTEIRA:
                            res = calc.divInteira(oper);
                            break;
                        case OP_MAT_MOD:
                            res = calc.mod(oper);
                            break;
                        case OP_REL_MAIOR:
                            res = calc.maior(oper);
                            break;
                        case OP_REL_MAIOR_IGUAL:
                            res = calc.maiorIgual(oper);
                            break;
                        case OP_REL_MENOR:
                            res = calc.menor(oper);
                            break;
                        case OP_REL_MENOR_IGUAL:
                            res = calc.menorIgual(oper);
                            break;
                        case OP_REL_IGUAL:
                            res = calc.igual(oper);
                            break;
                        case OP_REL_DIFERENTE:
                            res = calc.diferente(oper);
                            break;
                        case OP_LOG_E:
                            res = calc.e(oper);
                            break;
                        case OP_LOG_OU:
                            res = calc.ou(oper);
                            break;
                        default:
                            break;
                    }
                    if (res == null) {
                        Erro erro = new Erro(TipoErro.ERRO, operacao.getAsToken(), "Expressão sem resultado");
                        interfaceExecucao.erroFatal(erro);
                        erros.add(erro);
                        canGo = false;
                    } else {
                        operacao.setResultado(res);
                        exec.instrucao = operacao;
                        exec.count ++;
                        pilhaExecucao.push(exec);
                        runNext = config.getBoolean(ConfigInterpreter.RUNNEXT_EXPR_RES_OPBIN);
                    }
                    break;
            }
        }
    }
    
    private Expressao removeExpressaoDaPilha() {
        Expressao expressao = null;
        if (!pilhaExecucao.isEmpty()) {
            Executavel instr = pilhaExecucao.pop();
            if (
                    instr.instrucao instanceof Expressao &&
                    ((Expressao)instr.instrucao).isResolvida()) {
                expressao = (Expressao)instr.instrucao;
            } else {
                pilhaExecucao.push(instr);
            }
        }
        return expressao;
    }
    
    private boolean tiposDadosCorretos (TipoDado encontrado, TipoDado... esperados){
        boolean found = false;
        for (TipoDado tipo : esperados) {
            if (tipo == encontrado) {
                found = true;
                break;
            }
        }
        return found;
    }
    
    private TipoDado[][] esperadosPorFuncao(String nome) {
        TipoDado[] real = {TipoDado.REAL, TipoDado.INTEIRO};
        TipoDado[] inteiro = {TipoDado.INTEIRO};
        
        switch (nome) {
            case "pot":
                return new TipoDado[][] { real , real };
            case "raiz":
                return new TipoDado[][] { real };
            default:
                return new TipoDado[][] {};
        }
    }
    
    private Token geraTokenExib(Token... tokens) {
        return new Token(Arrays.asList(tokens));
    }
    
}
