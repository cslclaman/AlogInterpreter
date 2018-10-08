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
import alog.model.Variavel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe que recebe uma expressão e executa.
 * @author Caique
 */
public class Interpreter extends Verificator {
    
    private InterfaceExecucao interfaceExecucao;
    private HashMap<String, Variavel> variaveis;
    private List<Instrucao> programa;
    private LinkedList<Instrucao> filaExec;
    private int pos;
    private int tam;
    private boolean canGo;

    public Interpreter(InterfaceExecucao interfaceExecucao, List<Instrucao> programa) {
        super();
        this.interfaceExecucao = interfaceExecucao;
        this.programa = programa;
        variaveis = new HashMap<>();
        filaExec = new LinkedList<>();
        tam = programa.size();
        pos = 0;
        canGo = true;
    }
    
    public boolean existeProxima() {
        return canGo && (!filaExec.isEmpty() || pos < tam);
    }
    
    public void proxima () {
        if (!canGo) return;
        
        if (filaExec.isEmpty()) {
            if (pos < tam) {
                filaExec.add(programa.get(pos++));
            }
        }
        
        Instrucao instrucao = filaExec.poll();
        if (instrucao == null) {
            erros.add(new Erro(TipoErro.ERRO, ' ', 1, 1, 1, 
                "Falha ao carregar próxima instrução - interpretador finalizado"));
            canGo = false;
            return;
        } else {
            interfaceExecucao.atualizaInstrucaoAtual(instrucao);
        }
        
        switch (instrucao.getTipo()) {
            case MODULO_PRINCIPAL:
                executaModuloPrincipal(instrucao);
                break;
            
            case BLOCO:
                executaBloco(instrucao);
                break;

            case DECLARACAO_VARIAVEL:
                verificaDeclaracaoVariaveis(instrucao);
                break;

            case ENTRADA_DE_DADOS:
                verificaEntradaDados(instrucao);
                break;

            case SAIDA_DE_DADOS:
                verificaSaidaDados(instrucao);
                break;

            case ATRIBUICAO:
                verificaAtribuicao(instrucao);
                break;

            case CONDICIONAL:
                verificaCondicional(instrucao);
                break;

            case REPETICAO_ENQUANTO:
                verificaRepeticaoEnquanto(instrucao);
                break;

            case REPETICAO_FACA:
                verificaRepeticaoFaca(instrucao);
                break;

            case REPETICAO_REPITA:
                verificaRepeticaoRepita(instrucao);
                break;

            case REPETICAO_PARA:
                verificaRepeticaoPara(instrucao);
                break;

            default:
                erros.add(new Erro(TipoErro.DEVEL, instrucao.listaTokens().get(0), 
                "Instrução \"" + instrucao.getTipo() + "\" não esperada"));
        }
    }
    
    private void executaModuloPrincipal (Instrucao instrucao) {
        ModuloPrincipal moduloPrincipal = (ModuloPrincipal) instrucao;
        for (Instrucao sub : moduloPrincipal.listaInstrucoes()) {
            filaExec.add(sub);
            filaExec.add(new FimBloco());
        }
    }
    
    private void executaBloco (Instrucao instrucao) {
        Bloco bloco = (Bloco) instrucao;
        for (Instrucao sub : bloco.listaInstrucoes()) {
            filaExec.add(sub);
        }
    }
    
    private void executaDeclaracaoVariaveis(Instrucao instrucao) {
        DeclaracaoVariaveis declaracaoVariaveis = (DeclaracaoVariaveis)instrucao;
        for (Token token : declaracaoVariaveis.getTokensNomesVariaveis()) {
            variaveis.put(token.nome(), new VariavelVerif(token, declaracaoVariaveis.getTipoVariavel()));
        }
    }
    
    private void verificaEntradaDados(Instrucao instrucao) {
        EntradaDados entradaDados = (EntradaDados)instrucao;
        for (Token token : entradaDados.getParametros()) {
            VariavelVerif var = variaveis.get(token.nome());
            var.inicializada = true;
            var.chamadas += 1;
            variaveis.replace(token.nome(), var);
        }
    }
    
    private void verificaSaidaDados(Instrucao instrucao) {
        SaidaDados saidaDados = (SaidaDados)instrucao;
        for (Expressao expressao : saidaDados.getParametros()) {
            verificaExpressao(expressao);
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
    
}
