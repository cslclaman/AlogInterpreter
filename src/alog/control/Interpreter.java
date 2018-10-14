/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.analise.Erro;
import alog.analise.TipoErro;
import alog.instrucao.*;
import alog.expressao.*;
import alog.model.TipoDado;
import alog.model.Variavel;
import alog.token.TipoToken;
import alog.token.Token;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Classe que recebe uma expressão e executa.
 * @author Caique
 */
public class Interpreter extends Verificator {
    
    private class Executavel {
        Instrucao instrucao;
        int total;
        int count;
        
        public Executavel(Instrucao instrucao){
            this.instrucao = instrucao;
            this.total = 0;
            this.count = 0;
        }

        @Override
        public String toString() {
            return instrucao.getTipo() + " (" + count + " de " + total + ")";
        }
        
        
    }
    
    private InterfaceExecucao interfaceExecucao;
    private HashMap<String, Variavel> variaveis;
    private List<Instrucao> programa;
    private LinkedList<Instrucao> filaInstrucoes;
    private LinkedList<Executavel> pilhaExecucao;
    private int pos;
    private int tam;
    private boolean canGo;
    private boolean runNext;

    public Interpreter(InterfaceExecucao interfaceExecucao, List<Instrucao> programa) {
        super();
        this.interfaceExecucao = interfaceExecucao;
        this.programa = programa;
        variaveis = new HashMap<>();
        filaInstrucoes = new LinkedList<>();
        pilhaExecucao = new LinkedList<>();
        tam = programa.size();
        pos = 0;
        canGo = true;
        runNext = false;
    }
    
    public boolean existeProxima() {
        return canGo && (!pilhaExecucao.isEmpty() || pos < tam);
    }
    
    public void proxima () {
        if (!canGo) return;
        
        if (pilhaExecucao.isEmpty()) {
            if (filaInstrucoes.isEmpty()) {
                if (pos < tam) {
                    filaInstrucoes.add(programa.get(pos++));
                    pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                    interfaceExecucao.atualizaInstrucao();
                } else {
                    return;
                }
            } else {
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                interfaceExecucao.atualizaInstrucao();
            }
        }
        
        Executavel exec;
        try {
            exec = pilhaExecucao.pop();
        } catch (NoSuchElementException ex) {
            erros.add(new Erro(TipoErro.DEVEL, ' ', 1, 1, 1, String.format(
                "Pilha sem elemento de instrução: %s - %s",ex.getClass().getName(), ex.getMessage())));
            Erro fatal = new Erro(TipoErro.ERRO, ' ', 1, 1, 1, 
                "Falha ao carregar próxima instrução - interpretador finalizado");
            erros.add(fatal);
            canGo = false;
            interfaceExecucao.erroFatal(fatal);
            return;
        }
        
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
                    interfaceExecucao.atualizaExpressaoAtual((Expressao)exec.instrucao);
                    executaExpressao(exec);
                } else {
                    try {
                        Executavel inst = pilhaExecucao.pop();
                        pilhaExecucao.push(exec);
                        pilhaExecucao.push(inst);
                        runNext = true;
                    } catch (NoSuchElementException ex) {
                        erros.add(new Erro(TipoErro.DEVEL, ' ', 1, 1, 1, String.format(
                            "Pilha sem elemento de instrução: %s - %s",ex.getClass().getName(), ex.getMessage())));
                        Erro fatal = new Erro(TipoErro.ERRO, ' ', 1, 1, 1, 
                            "Falha ao carregar próxima instrução - interpretador finalizado");
                        erros.add(fatal);
                        canGo = false;
                        interfaceExecucao.erroFatal(fatal);
                    }
                }
                break;
                
            default:
                erros.add(new Erro(TipoErro.DEVEL, exec.instrucao.listaTokens().get(0), 
                "Instrução \"" + exec.instrucao.getTipo() + "\" não esperada"));
        }
        
        if (runNext) {
            runNext = false;
            proxima();
        }
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
                Token token = geraTokenExib(moduloPrincipal.getTipoModulo(), moduloPrincipal.getNome());
                interfaceExecucao.atualizaPassoAtual(token);
                exec.count ++;
                pilhaExecucao.push(exec);
                break;
            case 1:
                interfaceExecucao.atualizaPassoAtual(moduloPrincipal.getInicio());
                for (Instrucao sub : moduloPrincipal.listaInstrucoes()) {
                    filaInstrucoes.add(sub);
                }
                exec.count ++;
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                interfaceExecucao.atualizaInstrucao();
                break;
            case 2:
                if (!filaInstrucoes.isEmpty()) {
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                    interfaceExecucao.atualizaInstrucao();
                } else {
                    exec.count ++;
                    pilhaExecucao.push(exec);
                }
                runNext = true;
                break;
            case 3:
                interfaceExecucao.atualizaPassoAtual(moduloPrincipal.getFim());
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
                interfaceExecucao.atualizaPassoAtual(bloco.getInicio());
                for (Instrucao sub : bloco.listaInstrucoes()) {
                    filaInstrucoes.add(sub);
                }
                exec.count ++;
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                interfaceExecucao.atualizaInstrucao();
                break;
            case 1:
                if (!filaInstrucoes.isEmpty()) {
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                    interfaceExecucao.atualizaInstrucao();
                    runNext = true;
                } else {
                    exec.count ++;
                    pilhaExecucao.push(exec);
                }
                break;
            case 2:
                interfaceExecucao.atualizaPassoAtual(bloco.getFim());
                break;
        }
    }
    
    private void executaDeclaracaoVariaveis(Executavel exec) {
        DeclaracaoVariaveis declaracaoVariaveis = (DeclaracaoVariaveis) exec.instrucao;
        if (exec.total == 0) {
            exec.total = declaracaoVariaveis.getNumVariaveis();
        }
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
            exec.total = entradaDados.getNumParametros();
        }
        int nvar = exec.count % exec.total;
        int passo = exec.count / exec.total;
        
        String retorno = null;
        Token token = entradaDados.getParametros().get(nvar);
        Variavel variavel = variaveis.get(token.nome());
        interfaceExecucao.atualizaPassoAtual(entradaDados.getNome(), token);
        interfaceExecucao.selecionaVariavel(variavel);
        
        if (nvar < exec.total) {
            switch (passo) {
                case 0:
                    interfaceExecucao.entradaDados(variavel);
                    exec.count += exec.total;
                    break;
                case 1:
                    retorno = interfaceExecucao.entradaDadosRetorno();
                    if (retorno == null) {
                        erros.add(new Erro(TipoErro.DEVEL, token, "Retorno nulo"));
                        Erro erro = new Erro(TipoErro.ERRO, token, 
                            "Falha ao realizar entrada de dados - interpretador finalizado");
                        erros.add(erro);
                        canGo = false;
                        return;
                    }
                    try {
                        switch (variavel.getTipo()){
                            case INTEIRO:
                                variavel.setValorInteiro(Long.parseLong(retorno));
                                break;
                            case REAL:
                                variavel.setValorReal(Double.parseDouble(retorno));
                                break;
                            default:
                                variavel.setValor(retorno);
                                break;
                        }
                        exec.count = ++nvar;
                        variaveis.replace(token.nome(), variavel);
                        interfaceExecucao.defineValorVariavel(token, variavel);
                    } catch (NumberFormatException ex) {
                        erros.add(new Erro(TipoErro.DEVEL, token, String.format(
                            "Conversão não realizada para tipo %s: %s - %s", 
                                variavel.getTipo().toString(), ex.getClass().getName(), ex.getMessage())));
                        Erro erro = new Erro(TipoErro.ERRO, token, 
                            "Valor informado não era do tipo " + variavel.getTipo().toString() + " - tente novamente");
                        erros.add(erro);
                        interfaceExecucao.erroEntradaDados(variavel, erro);
                        exec.count = nvar;
                    }
                    break;
            } 
        }
        if (nvar < exec.total) {
            pilhaExecucao.push(exec);
        }
    }
    
    private void executaSaidaDados(Executavel exec) {
        SaidaDados saidaDados = (SaidaDados)exec.instrucao;
        if (exec.total == 0) {
            exec.total = saidaDados.getNumParametros();
        }
        if (exec.count < exec.total) {
            Expressao expressao = null;
            if (!pilhaExecucao.isEmpty()) {
                Executavel instr = pilhaExecucao.pop();
                if (instr.instrucao instanceof Expressao) {
                    expressao = (Expressao)instr.instrucao;
                } else {
                    pilhaExecucao.push(instr);
                }
            } 
            
            if (expressao == null) {
                expressao = saidaDados.getParametros().get(exec.count);
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
                interfaceExecucao.atualizaPassoAtual(saidaDados.getTokenNome(), expressao.getAsToken());
            } else {
                if (expressao.getTipoResultado() == TipoDado.CARACTER) {
                    interfaceExecucao.saidaDados(expressao.getResultadoCaracter());
                } else {
                    interfaceExecucao.saidaDados(expressao.getResultado());
                }
                
                exec.count++;
                if (exec.count < exec.total) {
                    pilhaExecucao.push(exec);
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
        
        Expressao expressao = null;
        if (!pilhaExecucao.isEmpty()) {
            Executavel instr = pilhaExecucao.pop();
            if (instr.instrucao instanceof Expressao) {
                expressao = (Expressao)instr.instrucao;
            } else {
                pilhaExecucao.push(instr);
            }
        } 

        if (expressao == null) {
            expressao = atribuicao.getExpressao();
            pilhaExecucao.push(exec);
            pilhaExecucao.push(new Executavel(expressao));
            interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
            runNext = true;
        } else {
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
                erros.add(new Erro(TipoErro.DEVEL, token, String.format(
                    "Não pode atribuir valor %s (%s) a variável %s"
                    + " - interpretador finalizado", retorno, expressao.getTipoResultado(), variavel.getTipo())));
                Erro erro = new Erro(TipoErro.ERRO, token, String.format(
                    "Falha ao realizar atribuição: não pode atribuir valor %s a variável de tipo %s"
                    + " - interpretador finalizado", expressao.getTipoResultado(), variavel.getTipo()));
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
            Expressao expressao = null;
            if (!pilhaExecucao.isEmpty()) {
                Executavel instr = pilhaExecucao.pop();
                if (instr.instrucao instanceof Expressao) {
                    expressao = (Expressao)instr.instrucao;
                } else {
                    pilhaExecucao.push(instr);
                }
            } 

            if (expressao == null) {
                expressao = condicional.getCondicao();
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
                interfaceExecucao.atualizaPassoAtual(condicional.getTokenSe(), expressao.getAsToken());
            } else {
                if (!tiposDadosCorretos(expressao.getTipoResultado(), TipoDado.LOGICO)) {
                    erros.add(new Erro(TipoErro.DEVEL, expressao.getAsToken(), String.format(
                        "Não interpretou resultado %s (%s) como lógico para condicional",
                        expressao.getResultado(), expressao.getTipoResultado())));
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
        
        switch (exec.count) { // Senão INSTRUÇÃO
            case 1:
                //interfaceExecucao.atualizaPassoAtual(condicional.getTokenEntao());
                filaInstrucoes.add(condicional.getInstrucaoSe());
                exec.count += 2;
                if (condicional.isComposta()) {
                    pilhaExecucao.push(exec);
                }
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                interfaceExecucao.atualizaInstrucao();
                break;
            case 2:
                if (condicional.isComposta()) {
                    interfaceExecucao.atualizaPassoAtual(condicional.getTokenSenao());
                    filaInstrucoes.add(condicional.getInstrucaoSenao());
                    pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
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
                Expressao expressao = null;
                if (!pilhaExecucao.isEmpty()) {
                    Executavel instr = pilhaExecucao.pop();
                    if (instr.instrucao instanceof Expressao) {
                        expressao = (Expressao)instr.instrucao;
                    } else {
                        pilhaExecucao.push(instr);
                    }
                } 

                if (expressao == null) {
                    expressao = repetitiva.getCondicao();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                } else {
                    if (!tiposDadosCorretos(expressao.getTipoResultado(), TipoDado.LOGICO)) {
                        erros.add(new Erro(TipoErro.DEVEL, expressao.getAsToken(), String.format(
                            "Não interpretou resultado %s (%s) como lógico para repetição",
                            expressao.getResultado(), expressao.getTipoResultado())));
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
                //interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenFaca());
                filaInstrucoes.add(repetitiva.getInstrucao());
                exec.count = 0; // para repetir
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
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
                filaInstrucoes.add(repetitiva.getInstrucao());
                exec.count ++;
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                interfaceExecucao.atualizaInstrucao();
                break;
            case 2: // Enquanto
                Expressao expressao = null;
                if (!pilhaExecucao.isEmpty()) {
                    Executavel instr = pilhaExecucao.pop();
                    if (instr.instrucao instanceof Expressao) {
                        expressao = (Expressao)instr.instrucao;
                    } else {
                        pilhaExecucao.push(instr);
                    }
                } 

                if (expressao == null) {
                    expressao = repetitiva.getCondicao();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                } else {
                    if (!tiposDadosCorretos(expressao.getTipoResultado(), TipoDado.LOGICO)) {
                        erros.add(new Erro(TipoErro.DEVEL, expressao.getAsToken(), String.format(
                            "Não interpretou resultado %s (%s) como lógico para repetição",
                            expressao.getResultado(), expressao.getTipoResultado())));
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
                filaInstrucoes.add(repetitiva.getInstrucao());
                exec.count ++;
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                interfaceExecucao.atualizaInstrucao();
                break;
            case 2: // Até
                Expressao expressao = null;
                if (!pilhaExecucao.isEmpty()) {
                    Executavel instr = pilhaExecucao.pop();
                    if (instr.instrucao instanceof Expressao) {
                        expressao = (Expressao)instr.instrucao;
                    } else {
                        pilhaExecucao.push(instr);
                    }
                } 

                if (expressao == null) {
                    expressao = repetitiva.getCondicao();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                } else {
                    if (!tiposDadosCorretos(expressao.getTipoResultado(), TipoDado.LOGICO)) {
                        erros.add(new Erro(TipoErro.DEVEL, expressao.getAsToken(), String.format(
                            "Não interpretou resultado %s (%s) como lógico para repetição",
                            expressao.getResultado(), expressao.getTipoResultado())));
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
            exec.total = 5;
        }
        
        Token tokenVarCont = repetitiva.getVariavelCont();
        Variavel variavel = variaveis.get(tokenVarCont.nome());
        
        switch (exec.count) {
            case 0: // Para VARIAVEL
                interfaceExecucao.selecionaVariavel(variavel);
                interfaceExecucao.atualizaPassoAtual(tokenVarCont);
                
                if (!tiposDadosCorretos(variavel.getTipo(), TipoDado.INTEIRO, TipoDado.REAL)){
                    erros.add(new Erro(TipoErro.DEVEL, tokenVarCont, String.format(
                        "Variável de tipo inválido: %s", variavel.getTipo())));
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
                Expressao expressao = null;
                if (!pilhaExecucao.isEmpty()) {
                    Executavel instr = pilhaExecucao.pop();
                    if (instr.instrucao instanceof Expressao) {
                        expressao = (Expressao)instr.instrucao;
                    } else {
                        pilhaExecucao.push(instr);
                    }
                } 
                if (expressao == null) {
                    expressao = repetitiva.getExpressaoDe();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                } else {
                    TipoDado[] esperados;
                    if (variavel.getTipo() == TipoDado.INTEIRO) {
                        esperados = new TipoDado[] {TipoDado.INTEIRO};
                    } else {
                        esperados = new TipoDado[] {TipoDado.INTEIRO, TipoDado.REAL};
                    }
                    
                    if (tiposDadosCorretos(expressao.getTipoResultado(), esperados)) {
                        variavel.setValor(expressao.getResultado());
                        variaveis.replace(tokenVarCont.nome(), variavel);
                        interfaceExecucao.defineValorVariavel(variavel);
                        exec.count ++;
                        
                    } else {
                        erros.add(new Erro(TipoErro.DEVEL, expressao.getAsToken(), String.format(
                            "Não interpretou resultado %s (%s) como numérico para repetição",
                            expressao.getResultado(), expressao.getTipoResultado())));
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
                expressao = null;
                if (!pilhaExecucao.isEmpty()) {
                    Executavel instr = pilhaExecucao.pop();
                    if (instr.instrucao instanceof Expressao) {
                        expressao = (Expressao)instr.instrucao;
                    } else {
                        pilhaExecucao.push(instr);
                    }
                } 
                if (expressao == null) {
                    expressao = repetitiva.getExpressaoAte();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                } else {
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
                    } else {
                        erros.add(new Erro(TipoErro.DEVEL, expressao.getAsToken(), String.format(
                            "Não interpretou resultado %s (%s) como numérico para repetição",
                            expressao.getResultado(), expressao.getTipoResultado())));
                        Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), String.format(
                            "Esperava um resultado %s para início de repetição, mas encontrou %s"
                            + " - interpretador finalizado", variavel.getTipo(), expressao.getTipoResultado()));
                        interfaceExecucao.erroFatal(erro);
                        erros.add(erro);
                        canGo = false;
                        return;
                    }
                }
                Operando operVar = new Operando();
                operVar.setOperando(tokenVarCont);
                operVar.setTipoResultado(variavel.getTipo());
                
                Operando operLim = new Operando();
                operLim.setOperando(result);
                
                Token sig = new Token(
                        tokenVarCont.getLinha(),
                        tokenVarCont.getColuna() + tokenVarCont.getTamanho() + 2,
                        tokenVarCont.getPosicao() + tokenVarCont.getTamanho() + 2,
                        tokenVarCont.getOrdem() + 1
                );
                if (variavel.getValorReal() <= operLim.getResultadoReal()){
                    sig.setPalavra("<=");
                } else {
                    sig.setPalavra(">=");
                }
                sig.setTipoToken(TipoToken.OPERADOR);
                
                Operacao condicao = new Operacao();
                condicao.setExpressaoEsq(operVar);
                condicao.setExpressaoDir(operLim);
                condicao.setOperador(sig);
                
                repetitiva.setExpressao(condicao);
                exec.instrucao = repetitiva;
                exec.count ++;
                pilhaExecucao.push(exec);
                
                break;
               
            case 3: // Faça INSTRUÇÕES
                expressao = null;
                if (!pilhaExecucao.isEmpty()) {
                    Executavel instr = pilhaExecucao.pop();
                    if (instr.instrucao instanceof Expressao) {
                        expressao = (Expressao)instr.instrucao;
                    } else {
                        pilhaExecucao.push(instr);
                    }
                } 
                if (expressao == null) {
                    expressao = repetitiva.getCondicao();
                    pilhaExecucao.push(exec);
                    pilhaExecucao.push(new Executavel(expressao));
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                } else {
                    if (expressao.getResultadoLogico()) {
                        exec.count += 1;
                        pilhaExecucao.push(exec);
                    } else {
                        exec.count += 2;
                    }
                }
                break;
                
            case 4:
                //interfaceExecucao.atualizaPassoAtual(repetitiva.getTokenFaca());
                exec.count = 3; // para repetir
                filaInstrucoes.add(repetitiva.getInstrucao());
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                interfaceExecucao.atualizaInstrucao();
                break;
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
        interfaceExecucao.atualizaExpressaoAtual(operando);
        interfaceExecucao.atualizaPassoAtual(operando.getOperando());
        
        exec.instrucao = operando;
        exec.count ++;
        pilhaExecucao.push(exec);
    }
    
    private void executaExpressaoOperandoVariavel(Executavel exec) {
        Operando operando = (Operando)exec.instrucao;
        if (exec.total == 0) {
            exec.total = 1;
        }
        interfaceExecucao.atualizaExpressaoAtual(operando);
        interfaceExecucao.atualizaPassoAtual(operando.getOperando());
        
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
            operando.setTipoResultado(variavel.getTipo());
            operando.setResultado(variavel.getValor());
            exec.instrucao = operando;
            exec.count ++;
            pilhaExecucao.push(exec);
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
            Expressao expressao = null;
            if (!pilhaExecucao.isEmpty()) {
                Executavel instr = pilhaExecucao.pop();
                if (instr.instrucao instanceof Expressao) {
                    expressao = (Expressao)instr.instrucao;
                } else {
                    pilhaExecucao.push(instr);
                }
            } 
            if (expressao == null) {
                expressao = chamadaFuncao.getParametros().get(exec.count);
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
                interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
            } else {
                if (tiposDadosCorretos(expressao.getTipoResultado(), esperadosPorFuncao(nome.nome())[exec.count])) {
                    chamadaFuncao.atualizaParametro(exec.count, expressao);
                    exec.count ++;
                    pilhaExecucao.push(exec);
                    interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                } else {
                    erros.add(new Erro(TipoErro.DEVEL, expressao.getAsToken(), String.format(
                        "Não interpretou resultado %s (%s) como parâmetro %d de função %s",
                        expressao.getResultado(), expressao.getTipoResultado(), exec.count, nome.getPalavra())));
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
            Expressao expressao = null;
            if (!pilhaExecucao.isEmpty()) {
                Executavel instr = pilhaExecucao.pop();
                if (instr.instrucao instanceof Expressao) {
                    expressao = (Expressao)instr.instrucao;
                } else {
                    pilhaExecucao.push(instr);
                }
            } 
            if (expressao == null) {
                expressao = operacaoUnaria.getExpressao();
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
                interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
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
                    operacaoUnaria.setTipoResultado(res.getTipo());
                    operacaoUnaria.setResultado(res.getValor());
                    exec.instrucao = operacaoUnaria;
                    exec.count ++;
                    pilhaExecucao.push(exec);
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
                    if (!pilhaExecucao.isEmpty()) {
                        Executavel instr = pilhaExecucao.pop();
                        if (instr.instrucao instanceof Expressao) {
                            expressao = (Expressao)instr.instrucao;
                        } else {
                            pilhaExecucao.push(instr);
                        }
                    } 
                    if (expressao == null) {
                        expressao = operacao.getExpressaoEsq();
                        pilhaExecucao.push(exec);
                        pilhaExecucao.push(new Executavel(expressao));
                        interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                    } else {
                        operacao.atualizaExpressaoEsq(expressao);
                        exec.count ++;
                        pilhaExecucao.push(exec);
                    }
                    break;
                case 1:
                    if (!pilhaExecucao.isEmpty()) {
                        Executavel instr = pilhaExecucao.pop();
                        if (instr.instrucao instanceof Expressao) {
                            expressao = (Expressao)instr.instrucao;
                        } else {
                            pilhaExecucao.push(instr);
                        }
                    } 
                    if (expressao == null) {
                        expressao = operacao.getExpressaoDir();
                        pilhaExecucao.push(exec);
                        pilhaExecucao.push(new Executavel(expressao));
                        interfaceExecucao.atualizaPassoAtual(expressao.getAsToken());
                    } else {
                        operacao.atualizaExpressaoDir(expressao);
                        exec.count ++;
                        pilhaExecucao.push(exec);
                    }
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
                        Erro erro = new Erro(TipoErro.ERRO, expressao.getAsToken(), "Expressão sem resultado");
                        interfaceExecucao.erroFatal(erro);
                        erros.add(erro);
                        canGo = false;
                    } else {
                        operacao.setTipoResultado(res.getTipo());
                        operacao.setResultado(res.getValor());
                        exec.instrucao = operacao;
                        exec.count ++;
                        pilhaExecucao.push(exec);
                    }
                    break;
            }
        }
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
