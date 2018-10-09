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
    }
    
    private InterfaceExecucao interfaceExecucao;
    private HashMap<String, Variavel> variaveis;
    private List<Instrucao> programa;
    private LinkedList<Instrucao> filaInstrucoes;
    private LinkedList<Executavel> pilhaExecucao;
    private int pos;
    private int tam;
    private boolean canGo;

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
    }
    
    public boolean existeProxima() {
        return canGo && (!pilhaExecucao.isEmpty() || pos < tam);
    }
    
    public void proxima () {
        if (canGo) return;
        
        if (pilhaExecucao.isEmpty()) {
            if (filaInstrucoes.isEmpty()) {
                if (pos < tam) {
                    filaInstrucoes.add(programa.get(pos++));
                    pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                } else {
                    return;
                }
            } else {
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
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
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaModuloPrincipal(exec);
                break;
            
            case BLOCO:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaBloco(exec);
                break;

            case DECLARACAO_VARIAVEL:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaDeclaracaoVariaveis(exec);
                break;

            case ENTRADA_DE_DADOS:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaEntradaDados(exec);
                break;

            case SAIDA_DE_DADOS:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaSaidaDados(exec);
                break;

            case ATRIBUICAO:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaAtribuicao(exec);
                break;

            case CONDICIONAL:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaCondicional(exec);
                break;

            case REPETICAO_ENQUANTO:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaRepeticaoEnquanto(exec);
                break;

            case REPETICAO_FACA:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaRepeticaoFaca(exec);
                break;

            case REPETICAO_REPITA:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaRepeticaoRepita(exec);
                break;

            case REPETICAO_PARA:
                interfaceExecucao.atualizaInstrucaoAtual(exec.instrucao);
                executaRepeticaoPara(exec);
                break;

            case EXPRESSAO:
                if (exec.total < exec.count) {
                    interfaceExecucao.atualizaExpressaoAtual((Expressao)exec.instrucao);
                    executaInstrucao(exec);
                } else {
                    try {
                        Executavel inst = pilhaExecucao.pop();
                        pilhaExecucao.push(exec);
                        pilhaExecucao.push(inst);
                        proxima();
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
    }
    
    private void executaModuloPrincipal (Executavel exec) {
        ModuloPrincipal moduloPrincipal = (ModuloPrincipal) exec.instrucao;
        if (exec.total == 0) {
            exec.total = 3;
            if (moduloPrincipal.isDeclarado()) {
                exec.count = 1;
            } 
        }
        switch (exec.count++) {
            case 0:
                Token token = geraTokenExib(moduloPrincipal.getTipoModulo(), moduloPrincipal.getNome());
                interfaceExecucao.atualizaPassoAtual(token);
                pilhaExecucao.push(exec);
                break;
            case 1:
                interfaceExecucao.atualizaPassoAtual(moduloPrincipal.getInicio());
                for (Instrucao sub : moduloPrincipal.listaInstrucoes()) {
                    filaInstrucoes.add(sub);
                }
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                break;
            case 2:
                interfaceExecucao.atualizaPassoAtual(moduloPrincipal.getFim());
                break;
        }
    }
    
    private void executaBloco (Executavel exec) {
        Bloco bloco = (Bloco) exec.instrucao;
        if (exec.total == 0) {
            exec.total = 2;
        }
        switch (exec.count++) {
            case 0:
                interfaceExecucao.atualizaPassoAtual(bloco.getInicio());
                for (Instrucao sub : bloco.listaInstrucoes()) {
                    filaInstrucoes.add(sub);
                }
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(filaInstrucoes.poll()));
                break;
            case 1:
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
            interfaceExecucao.atualizaPassoAtual(token);
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
        boolean readed = false;
        Token token = entradaDados.getParametros().get(nvar);
        Variavel variavel = variaveis.get(token.nome());
        
        if (nvar < exec.total) {
            switch (passo) {
                case 0:
                    interfaceExecucao.atualizaPassoAtual(token);
                    retorno = interfaceExecucao.entradaDados(variavel);
                    if (retorno != null) {
                        readed = true;
                        exec.count += exec.total;
                    }
                    break;
                case 1:
                    interfaceExecucao.atualizaPassoAtual(token);
                    if (!readed) {
                        retorno = interfaceExecucao.entradaDadosRetorno();
                    }
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
                        exec.count = nvar + 1;
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
                }
            } 
            
            if (expressao == null) {
                expressao = saidaDados.getParametros().get(exec.count);
                pilhaExecucao.push(exec);
                pilhaExecucao.push(new Executavel(expressao));
            } else {
                interfaceExecucao.saidaDados(expressao.getResultado());
                exec.count++;
                if (exec.count < exec.total) {
                    pilhaExecucao.push(exec);
                }
            }
        }
    }
    
    private void verificaAtribuicao(Instrucao instrucao) {
        Atribuicao atribuicao = (Atribuicao)instrucao;
        Expressao expressao = atribuicao.getExpressao();
        Token variavel = atribuicao.getVariavel();
        verificaExpressao(expressao);
        geraErroTipoDadoAtribuicao(variavel, expressao.getTipoResultado());
        VariavelVerif var = variaveis.get(variavel.nome());
        var.inicializada = true;
        var.chamadas += 1;
        variaveis.replace(variavel.nome(), var);
    }
    
    private void verificaCondicional(Instrucao instrucao) {
        Condicional condicional = (Condicional)instrucao;
        Expressao condicao = condicional.getCondicao();
        verificaExpressao(condicao);
        int nerr = geraErroTipoDadoCondicional(condicional.getTokenSe(), condicao.getTipoResultado(), TipoDado.LOGICO);
        if (nerr == 0) {
            verificaInstrucao(condicional.getInstrucaoSe());
            if (condicional.isComposta()) {
                verificaInstrucao(condicional.getInstrucaoSenao());
            }
        }
    }
    
    private void verificaRepeticaoEnquanto(Instrucao instrucao) {
        RepeticaoEnquanto repetitiva = (RepeticaoEnquanto)instrucao;
        Expressao condicao = repetitiva.getCondicao();
        verificaExpressao(condicao);
        int nerr = geraErroTipoDadoCondicional(repetitiva.getTokenEnquanto(), condicao.getTipoResultado(), TipoDado.LOGICO);
        if (nerr == 0) {
            verificaInstrucao(repetitiva.getInstrucao());
        }
    }
    
    private void verificaRepeticaoFaca(Instrucao instrucao) {
        RepeticaoFaca repetitiva = (RepeticaoFaca)instrucao;
        Expressao condicao = repetitiva.getCondicao();
        verificaExpressao(condicao);
        int nerr = geraErroTipoDadoCondicional(repetitiva.getTokenFaca(), condicao.getTipoResultado(), TipoDado.LOGICO);
        if (nerr == 0) {
            verificaInstrucao(repetitiva.getInstrucao());
        }
    }
    
    private void verificaRepeticaoRepita(Instrucao instrucao) {
        RepeticaoRepita repetitiva = (RepeticaoRepita)instrucao;
        Expressao condicao = repetitiva.getCondicao();
        verificaExpressao(condicao);
        int nerr = geraErroTipoDadoCondicional(repetitiva.getTokenRepita(), condicao.getTipoResultado(), TipoDado.LOGICO);
        if (nerr == 0) {
            verificaInstrucao(repetitiva.getInstrucao());
        }
    }
    
    private void verificaRepeticaoPara(Instrucao instrucao) {
        RepeticaoPara repetitiva = (RepeticaoPara)instrucao;
        Token variavel = repetitiva.getVariavelCont();
        VariavelVerif varVerif = variaveis.get(variavel.nome());
        Expressao valorDe = repetitiva.getExpressaoDe();
        Expressao valorAte = repetitiva.getExpressaoAte();
        
        verificaExpressao(valorDe);
        verificaExpressao(valorAte);
        
        int nerr = geraErroTipoDadoCondicional(repetitiva.getTokenPara(), varVerif.tipo,
                TipoDado.INTEIRO, TipoDado.REAL);
        if (nerr == 0) {
            TipoDado[] esperados;
            if (varVerif.tipo == TipoDado.INTEIRO) {
                esperados = new TipoDado[]{TipoDado.INTEIRO};
            } else {
                esperados = new TipoDado[]{TipoDado.INTEIRO, TipoDado.REAL};
            }
            
            nerr += geraErroTipoDadoCondicional(repetitiva.getTokenPara(), valorDe.getTipoResultado(),esperados);
            nerr += geraErroTipoDadoCondicional(repetitiva.getTokenPara(), valorAte.getTipoResultado(),esperados);
            if (nerr == 0) {
                verificaInstrucao(repetitiva.getInstrucao());
                varVerif.chamadas += 1;
                variaveis.replace(variavel.nome(), varVerif);
            }
        }
    }
    
    private void verificaExpressao(Expressao expressao) {
        switch (expressao.getTipoExpressao()) {
            case OPERACAO_UNARIA:
                verificaOperacaoUnaria(expressao);
                break;
            case OPERACAO_ARITMETICA:
                verificaOperacaoAritmetica(expressao);
                break;
            case OPERACAO_RELACIONAL:
                verificaOperacaoRelacional(expressao);
                break;
            case OPERACAO_LOGICA:
                verificaOperacaoLogica(expressao);
                break;
            case OPERANDO_VARIAVEL:
                verificaOperandoVariavel(expressao);
                break;
            case OPERANDO_FUNCAO:
                verificaOperandoFuncao(expressao);
                break;
            default:
                break;
        }
    }
    
    private void verificaOperacaoUnaria (Expressao expressao) {
        OperacaoUnaria operacaoUnaria = (OperacaoUnaria)expressao;
        Expressao expr = operacaoUnaria.getExpressao();
        Token operador = operacaoUnaria.getOperador();
        verificaExpressao(expr);
        TipoDado[] esperados;
        
        switch (operador.getFuncaoToken()){
            case OP_LOG_NAO:
                esperados = new TipoDado[] {TipoDado.LOGICO};
                break;
            case OP_SIG_POSITIVO:
            case OP_SIG_NEGATIVO:
                esperados = new TipoDado[] {TipoDado.INTEIRO, TipoDado.REAL};
                break;
            default:
                esperados = new TipoDado[] {};
        }
        int nerr = geraErroTipoDadoInvalidoOperador(operador, expr.getAsToken(), expr.getTipoResultado(), esperados);
        if (nerr == 0) {
            operacaoUnaria.setTipoResultado(expr.getTipoResultado());
        }
    }
    
    private void verificaOperacaoAritmetica (Expressao expressao) {
        Operacao operacao = (Operacao)expressao;
        Expressao exprEsq = operacao.getExpressaoEsq();
        Expressao exprDir = operacao.getExpressaoDir();
        Token operador = operacao.getOperador();
        verificaExpressao(exprEsq);
        verificaExpressao(exprDir);
        
        TipoDado[] esperados;
        
        switch (operador.getFuncaoToken()){
            case OP_MAT_SOMA:
            case OP_MAT_SUBTRACAO:
            case OP_MAT_MULTIPLICACAO:
            case OP_MAT_DIV_REAL:
                esperados = new TipoDado[] {TipoDado.INTEIRO, TipoDado.REAL};
                break;
            case OP_MAT_DIV_INTEIRA:
            case OP_MAT_MOD:
                esperados = new TipoDado[] {TipoDado.INTEIRO};
                break;
            default:
                esperados = new TipoDado[] {};
                break;
        }
        int nerr = 0;
        nerr += geraErroTipoDadoInvalidoOperador(operador, exprEsq.getAsToken(), exprEsq.getTipoResultado(), esperados);
        nerr += geraErroTipoDadoInvalidoOperador(operador, exprDir.getAsToken(), exprDir.getTipoResultado(), esperados);
        if (nerr == 0) {
            if (exprEsq.getTipoResultado() == TipoDado.INTEIRO && exprDir.getTipoResultado() == TipoDado.INTEIRO) {
                operacao.setTipoResultado(TipoDado.INTEIRO);
            } else {
                operacao.setTipoResultado(TipoDado.REAL);
            }
        }
    }
    
    private void verificaOperacaoRelacional (Expressao expressao) {
        Operacao operacao = (Operacao)expressao;
        Expressao exprEsq = operacao.getExpressaoEsq();
        Expressao exprDir = operacao.getExpressaoDir();
        Token operador = operacao.getOperador();
        verificaExpressao(exprEsq);
        verificaExpressao(exprDir);
        
        TipoDado[] esperados;
        
        switch (operador.getFuncaoToken()){
            case OP_REL_MAIOR:
            case OP_REL_MAIOR_IGUAL:
            case OP_REL_MENOR:
            case OP_REL_MENOR_IGUAL:
                switch (exprDir.getTipoResultado()) {
                    case CARACTER:
                        esperados = new TipoDado[] {TipoDado.CARACTER};
                        break;
                    default:
                        esperados = new TipoDado[] {TipoDado.INTEIRO, TipoDado.REAL};
                        break;
                }
                break;
            case OP_REL_IGUAL:
            case OP_REL_DIFERENTE:
                switch (exprDir.getTipoResultado()) {
                    case CARACTER:
                        esperados = new TipoDado[] {TipoDado.CARACTER};
                        break;
                    case LOGICO:
                        esperados = new TipoDado[] {TipoDado.LOGICO};
                        break;
                    default:
                        esperados = new TipoDado[] {TipoDado.INTEIRO, TipoDado.REAL};
                        break;
                }
                break;
            default:
                esperados = new TipoDado[] {};
                break;
        }
        int nerr = 0;
        nerr += geraErroTipoDadoInvalidoOperador(operador, exprEsq.getAsToken(), exprEsq.getTipoResultado(),esperados);
        nerr += geraErroTipoDadoInvalidoOperador(operador, exprDir.getAsToken(), exprDir.getTipoResultado(),esperados);
        if (nerr == 0) {
            operacao.setTipoResultado(TipoDado.LOGICO);
        }
    }
    
    private void verificaOperacaoLogica (Expressao expressao) {
        Operacao operacao = (Operacao)expressao;
        Expressao exprEsq = operacao.getExpressaoEsq();
        Expressao exprDir = operacao.getExpressaoDir();
        Token operador = operacao.getOperador();
        verificaExpressao(exprEsq);
        verificaExpressao(exprDir);
        
        TipoDado[] esperados;
        
        switch (operador.getFuncaoToken()){
            case OP_LOG_E:
            case OP_LOG_OU:
                esperados = new TipoDado[] {TipoDado.LOGICO};
                break;
            default:
                esperados = new TipoDado[] {};
                break;
        }
        int nerr = 0;
        nerr += geraErroTipoDadoInvalidoOperador(operador, exprEsq.getAsToken(), exprEsq.getTipoResultado(), esperados);
        nerr += geraErroTipoDadoInvalidoOperador(operador, exprDir.getAsToken(), exprDir.getTipoResultado(), esperados);
        if (nerr == 0) {
            operacao.setTipoResultado(TipoDado.LOGICO);
        }
    }
    
    private void verificaOperandoVariavel(Expressao expressao) {
        Operando operando = (Operando)expressao;
        Token var = operando.getOperando();
        geraErroVariavelNaoInicializada(var);
        VariavelVerif varVerif = variaveis.get(var.nome());
        operando.setTipoResultado(varVerif.tipo);
        varVerif.chamadas += 1;
        variaveis.replace(var.nome(), varVerif);
    }
    
    private void verificaOperandoFuncao (Expressao expressao) {
        ChamadaFuncao chamadaFuncao = (ChamadaFuncao)expressao;
        Token funcao = chamadaFuncao.getTokenNome();
        TipoDado[] tiposParametros = new TipoDado[chamadaFuncao.getNumParametros()];
        int c = 0;
        for (Expressao param : chamadaFuncao.getParametros()) {
            verificaExpressao(param);
            tiposParametros[c++] = param.getTipoResultado();
        }
        int nerr = geraErroFuncaoParametros(funcao, tiposParametros);
        chamadaFuncao.setTipoResultado(funcoes.get(funcao.nome()).retorno);
    }
    
    private Token geraTokenExib(Token... tokens) {
        return new Token(Arrays.asList(tokens));
    }
    
}
