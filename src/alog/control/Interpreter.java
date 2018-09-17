/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.model.Bloco;
import alog.model.Expressao;
import alog.token.FuncaoToken;
import alog.model.TipoVariavel;
import alog.token.Token;
import alog.model.Variavel;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Classe que recebe uma expressão e executa.
 * @author Caique
 */
public class Interpreter {
    private int blocoAtual;
    private HashMap<String,Variavel> variaveis;
    private LinkedList<Boolean> condicionaisResult;
    private boolean execProx;
    
    public Interpreter(){
        blocoAtual = 0;
        variaveis = new HashMap<>();
        condicionaisResult = new LinkedList<>();
        execProx = true;
    }
    
    public void reseta(){
        blocoAtual = 0;
        variaveis = new HashMap<>();
        condicionaisResult = new LinkedList<>();
        execProx = true;
    }
    
    public boolean executa(Expressao expressao){
        if (!execProx){
            execProx = true;
            return true;
        }
        switch(expressao.getTipo()){
            case _BLOCO:
                Bloco bloco = (Bloco)expressao;
                boolean res = true;
                while (bloco.hasNextExpressao()){
                    res = executa(bloco.getNextExpressao());
                    if (!res){
                        break;
                    }
                }
                return res;
            case DELIM_BLOCO:
                return execDelimBloco(expressao);
            case CRIACAO_VARIAVEL:
                return execCriacaoVariavel(expressao);
            case ENTRADA_DE_DADOS:
                return execEntradaDados(expressao);
            case SAIDA_DE_DADOS:
                return execSaidaDados(expressao);
            case OPERACAO_ATRIBUICAO:
            case OPERACAO_ARITMETICA:
                return execOperacao(expressao);
            case OPERACAO_LOGICA:
                return execCondicional(expressao);
            case _INVALIDO:
                System.err.println("Expressão inválida");
                System.err.println("\t" + expressao.getTexto());
                //System.err.println("\tERRO - " + expressao.getErro());
                return false;
            default:
                System.err.println("Expressão indefinida");
                System.err.println("\t" + expressao.getTexto());
                return false;
        }
        
    }
    
    public boolean execDelimBloco(Expressao expressao){
        switch (expressao.getNextToken().getFuncaoToken()){
            case RES_BLOCO_INICIO:
                blocoAtual ++;
                break;
            case RES_BLOCO_FIM:
                blocoAtual --;
                break;
        }
        return true;
    }
    
    public boolean execCriacaoVariavel(Expressao expressao){
        TipoVariavel tipoVar;
        switch (expressao.getNextToken().getFuncaoToken()){
            case RES_TIPO_INTEIRO:
                tipoVar = TipoVariavel.INTEIRO;
                break;
            case RES_TIPO_REAL:
                tipoVar = TipoVariavel.REAL;
                break;
            case RES_TIPO_CARACTER:
                tipoVar = TipoVariavel.CARACTER;
                break;
            default:
                return false;
        }
        while (expressao.hasNextToken()){
            String nomeVar = expressao.getNextToken().getPalavra();
            Variavel variavel = new Variavel(tipoVar, nomeVar);
            variaveis.put(nomeVar, variavel);
        }
        
        return true;
    }
    
    public boolean execEntradaDados(Expressao expressao){
        expressao.setIndice(1);
        while (expressao.hasNextToken()){
            String nomeVar = expressao.getNextToken().getPalavra();
            Variavel variavel = variaveis.get(nomeVar);
            if (variavel == null){
                return false;
            }
            
            System.out.println("Valor para a variável " + nomeVar + " (" + variavel.getTipo() + ")");
            java.util.Scanner sc = new java.util.Scanner(System.in);

            boolean valorValido;
            do {
                valorValido = true;
                String linha = sc.nextLine();
                switch(variavel.getTipo()){
                    case CARACTER:
                        variavel.setValor(linha);
                        break;
                    case INTEIRO:
                        try {
                            int valor = Integer.parseInt(linha);
                            variavel.setValor(String.valueOf(valor));
                        } catch (NumberFormatException ex){
                            System.out.println("Valor \"" + linha + "\" inválido - esperado valor inteiro");
                            valorValido = false;
                        }
                        break;
                    case REAL:
                        try {
                            double valor = Double.parseDouble(linha);
                            variavel.setValor(String.valueOf(valor));
                        } catch (NumberFormatException ex){
                            System.out.println("Valor \"" + linha + "\" inválido - esperado valor real");
                            valorValido = false;
                        }
                        break;
                }
            } while (!valorValido);
            variaveis.put(nomeVar, variavel);
        }
        
        return true;
    }
    
    public boolean execSaidaDados(Expressao expressao){
        expressao.setIndice(1);
        int pos = 1;
        int qtd = expressao.getNumTokens() - 1;
        while (expressao.hasNextToken()){
            Token token = expressao.getNextToken();
            String saida = "";
            
            switch (token.getFuncaoToken()){
                case CONST_CARACTER:
                    saida = token.getPalavra().replace("\"", "");
                    break;
                case CONST_INTEIRA:
                case CONST_REAL:
                    saida = token.getPalavra();
                    break;
                case IDENT_NOME_VARIAVEL:
                    String nomeVar = token.getPalavra();
                    Variavel variavel = variaveis.get(nomeVar);
                    if (variavel == null){
                        return false;
                    }
                    switch (variavel.getTipo()){
                        case REAL:
                            int indicePonto = variavel.getValor().indexOf(".");
                            if (indicePonto >= 0 && variavel.getValor().substring(indicePonto + 1).length() > 3){
                                saida = variavel.getValor().substring(0, indicePonto + 4);
                            } else {
                                saida = variavel.getValor();
                            }
                            break;
                        case INTEIRO:
                        case CARACTER:
                            saida = variavel.getValor();
                            break;
                    }
            }
            
            System.out.printf("%s%s", saida, pos++ < qtd ? "" : "\n");
        }
        
        return true;
    }
    
    public boolean execOperacao(Expressao expressao){
        LinkedList<Token> pilha = new LinkedList<>();
        LinkedList<Token> saida = new LinkedList<>();
        int funcParam = 0;
        int contParam = 0;
        
        while (expressao.hasNextToken()){
            Token token = expressao.getNextToken();
            switch (token.getFuncaoToken()){
                case IDENT_NOME_VARIAVEL:
                case CONST_CARACTER:
                case CONST_INTEIRA:
                case CONST_REAL:
                    saida.add(token);
                    if (funcParam > 0){
                        contParam++;
                    }
                    if (contParam > funcParam){
                        /*
                        Essa parte de verificação de número de argumentos da função ficaria melhor no Parser
                        (Esse tipo de erro não fica bem de ser lançado em tempo de execução)
                        */
                        System.err.println("Chamada de função inválida (muitos argumentos) - esperava " + funcParam + " argumentos, encontrou " + contParam);
                        return false;
                    }
                    break;

                case OP_ATRIBUICAO:
                case OP_MAT_SOMA:
                case OP_MAT_SUBTRACAO:
                case OP_MAT_MULTIPLICACAO:
                case OP_MAT_DIV_INTEIRA:
                case OP_MAT_DIV_REAL:
                case OP_MAT_MOD:
                    while (!pilha.isEmpty() && pilha.peek().getPrecedencia() > token.getPrecedencia()){
                        saida.add(pilha.pop());
                    }
                    pilha.push(token);
                    break;
                
                case LIB_MATH_POT:
                    while (!pilha.isEmpty() && pilha.peek().getPrecedencia() > token.getPrecedencia()){
                        saida.add(pilha.pop());
                    }
                    pilha.push(token);
                    contParam = 0;
                    funcParam = 2;
                    break;
                    
                case LIB_MATH_RAIZ:
                    while (!pilha.isEmpty() && pilha.peek().getPrecedencia() > token.getPrecedencia()){
                        saida.add(pilha.pop());
                    }
                    pilha.push(token);
                    contParam = 0;
                    funcParam = 1;
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                    pilha.push(token);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    if (contParam < funcParam){
                        /*
                        Essa parte de verificação de número de argumentos da função ficaria melhor no Parser
                        (Esse tipo de erro não fica bem de ser lançado em tempo de execução)
                        */
                        System.err.println("Chamada de função inválida (poucos argumentos) - esperava " + funcParam + " argumentos, encontrou " + contParam);
                        return false;
                    } else {
                        funcParam = 0;
                        contParam = 0;
                    }
                    while (!pilha.isEmpty()){
                        Token out = pilha.pop();
                        if (out.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE){
                            break;
                        } else {
                            saida.add(out);
                        }
                    }
                    break;
            }
        }
        while (!pilha.isEmpty()){
            saida.add(pilha.pop());
        }
        
        Calculator calculadora;
        
        while (!saida.isEmpty()){
            Token token = saida.pop();
            Variavel op1, op2;
            switch (token.getFuncaoToken()){
                case IDENT_NOME_VARIAVEL:
                case CONST_CARACTER:
                case CONST_INTEIRA:
                case CONST_REAL:
                    pilha.push(token);
                    break;
                    
                case OP_MAT_SOMA:
                case OP_MAT_SUBTRACAO:
                case OP_MAT_MULTIPLICACAO:
                case OP_MAT_DIV_INTEIRA:
                case OP_MAT_DIV_REAL:
                case OP_MAT_MOD:
                    op2 = retornaVariavel(pilha.pop());
                    op1 = retornaVariavel(pilha.pop());
                    
                    calculadora = new Calculator(token);
                    token = calculadora.executaOperacaoAritmetica(op1, op2);
                    if (token == null){
                        return false;
                    } else {
                        pilha.push(token);
                    }
                    break;
                    
                case LIB_MATH_POT:
                    op2 = retornaVariavel(pilha.pop());
                    op1 = retornaVariavel(pilha.pop());
                    calculadora = new Calculator(token);
                    token = calculadora.executaFuncaoPot(op1, op2);
                    if (token == null){
                        return false;
                    } else {
                        pilha.push(token);
                    }
                    break;
                    
                case LIB_MATH_RAIZ:
                    op1 = retornaVariavel(pilha.pop());
                    calculadora = new Calculator(token);
                    token = calculadora.executaFuncaoRaiz(op1);
                    if (token == null){
                        return false;
                    } else {
                        pilha.push(token);
                    }
                    break;
                    
                case OP_ATRIBUICAO:
                    op1 = retornaVariavel(pilha.pop());
                    Token tokVar = pilha.pop();
                    if (tokVar.getFuncaoToken() != FuncaoToken.IDENT_NOME_VARIAVEL){
                        System.err.println("Atribuição inválida - Esperava Variável, encontrou Constante");
                        return false;
                    }
                    String nomeVar = tokVar.getPalavra();
                    Variavel variavel = variaveis.get(nomeVar);
                    if (variavel == null){
                        return false;
                    }
                    switch (variavel.getTipo()){
                        case INTEIRO:
                            if (op1.getTipo() != TipoVariavel.INTEIRO){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                        case REAL:
                            if (op1.getTipo() != TipoVariavel.REAL && op1.getTipo() != TipoVariavel.INTEIRO){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                        case CARACTER:
                            if (op1.getTipo() != TipoVariavel.CARACTER){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                    }
                    variaveis.put(nomeVar, variavel);
            }
        }
        
        return true;
    }
    
    public boolean execCondicional(Expressao expressao){
        switch (expressao.getNextToken().getFuncaoToken()){
            case RES_COND_SE:
                LinkedList<Token> pilha = new LinkedList<>();
                LinkedList<Token> saida = new LinkedList<>();
                int funcParam = 0;
                int contParam = 0;

                while (expressao.hasNextToken()){
                    Token token = expressao.getNextToken();
                    switch (token.getFuncaoToken()){
                        case IDENT_NOME_VARIAVEL:
                        case CONST_CARACTER:
                        case CONST_INTEIRA:
                        case CONST_REAL:
                            saida.add(token);
                            if (funcParam > 0){
                                contParam++;
                            }
                            if (contParam > funcParam){
                                /*
                                Essa parte de verificação de número de argumentos da função ficaria melhor no Parser
                                (Esse tipo de erro não fica bem de ser lançado em tempo de execução)
                                */
                                System.err.println("Chamada de função inválida (muitos argumentos) - esperava " + funcParam + " argumentos, encontrou " + contParam);
                                return false;
                            }
                            break;

                        case OP_ATRIBUICAO:
                        case OP_MAT_SOMA:
                        case OP_MAT_SUBTRACAO:
                        case OP_MAT_MULTIPLICACAO:
                        case OP_MAT_DIV_INTEIRA:
                        case OP_MAT_DIV_REAL:
                        case OP_MAT_MOD:
                        case OP_REL_MAIOR:
                        case OP_REL_MAIOR_IGUAL:
                        case OP_REL_MENOR:
                        case OP_REL_MENOR_IGUAL:
                        case OP_REL_IGUAL:
                        case OP_REL_DIFERENTE:
                        case OP_LOG_E:
                        case OP_LOG_OU:
                            while (!pilha.isEmpty() && pilha.peek().getPrecedencia() > token.getPrecedencia()){
                                saida.add(pilha.pop());
                            }
                            pilha.push(token);
                            break;

                        case LIB_MATH_POT:
                            while (!pilha.isEmpty() && pilha.peek().getPrecedencia() > token.getPrecedencia()){
                                saida.add(pilha.pop());
                            }
                            pilha.push(token);
                            contParam = 0;
                            funcParam = 2;
                            break;

                        case LIB_MATH_RAIZ:
                            while (!pilha.isEmpty() && pilha.peek().getPrecedencia() > token.getPrecedencia()){
                                saida.add(pilha.pop());
                            }
                            pilha.push(token);
                            contParam = 0;
                            funcParam = 1;
                            break;

                        case DELIM_PARENTESES_ABRE:
                            pilha.push(token);
                            break;

                        case DELIM_PARENTESES_FECHA:
                            if (contParam < funcParam){
                                /*
                                Essa parte de verificação de número de argumentos da função ficaria melhor no Parser
                                (Esse tipo de erro não fica bem de ser lançado em tempo de execução)
                                */
                                System.err.println("Chamada de função inválida (poucos argumentos) - esperava " + funcParam + " argumentos, encontrou " + contParam);
                                return false;
                            } else {
                                funcParam = 0;
                                contParam = 0;
                            }
                            while (!pilha.isEmpty()){
                                Token out = pilha.pop();
                                if (out.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE){
                                    break;
                                } else {
                                    saida.add(out);
                                }
                            }
                            break;
                            
                        case RES_COND_ENTAO:
                            break;
                    }
                }
                while (!pilha.isEmpty()){
                    saida.add(pilha.pop());
                }

                Calculator calculadora;

                while (!saida.isEmpty()){
                    Token token = saida.pop();
                    Variavel op1, op2;
                    switch (token.getFuncaoToken()){
                        case IDENT_NOME_VARIAVEL:
                        case CONST_CARACTER:
                        case CONST_INTEIRA:
                        case CONST_REAL:
                            pilha.push(token);
                            break;

                        case OP_MAT_SOMA:
                        case OP_MAT_SUBTRACAO:
                        case OP_MAT_MULTIPLICACAO:
                        case OP_MAT_DIV_INTEIRA:
                        case OP_MAT_DIV_REAL:
                        case OP_MAT_MOD:
                            op2 = retornaVariavel(pilha.pop());
                            op1 = retornaVariavel(pilha.pop());

                            calculadora = new Calculator(token);
                            token = calculadora.executaOperacaoAritmetica(op1, op2);
                            if (token == null){
                                return false;
                            } else {
                                pilha.push(token);
                            }
                            break;

                        case OP_REL_MAIOR:
                        case OP_REL_MAIOR_IGUAL:
                        case OP_REL_MENOR:
                        case OP_REL_MENOR_IGUAL:
                        case OP_REL_IGUAL:
                        case OP_REL_DIFERENTE:
                            op2 = retornaVariavel(pilha.pop());
                            op1 = retornaVariavel(pilha.pop());

                            calculadora = new Calculator(token);
                            token = calculadora.executaOperacaoRelacional(op1, op2);
                            if (token == null){
                                return false;
                            } else {
                                pilha.push(token);
                            }
                            break;
                            
                        case OP_LOG_E:
                        case OP_LOG_OU:
                            op2 = retornaVariavel(pilha.pop());
                            op1 = retornaVariavel(pilha.pop());

                            calculadora = new Calculator(token);
                            token = calculadora.executaOperacaoLogica(op1, op2);
                            if (token == null){
                                return false;
                            } else {
                                pilha.push(token);
                            }
                            break;
                        
                        case LIB_MATH_POT:
                            op2 = retornaVariavel(pilha.pop());
                            op1 = retornaVariavel(pilha.pop());
                            calculadora = new Calculator(token);
                            token = calculadora.executaFuncaoPot(op1, op2);
                            if (token == null){
                                return false;
                            } else {
                                pilha.push(token);
                            }
                            break;

                        case LIB_MATH_RAIZ:
                            op1 = retornaVariavel(pilha.pop());
                            calculadora = new Calculator(token);
                            token = calculadora.executaFuncaoRaiz(op1);
                            if (token == null){
                                return false;
                            } else {
                                pilha.push(token);
                            }
                            break;
                    }
                }
                if (pilha.isEmpty()){
                    System.err.println("Erro na execução - pilha vazia");
                    return false;
                } 
                Token t = pilha.pop();
                Variavel r = retornaVariavel(t);
                if (r == null || r.getTipo() != TipoVariavel.INTEIRO){
                    System.err.println("Erro na execução - variável para token " + t + " inválida");
                    return false;
                }
                condicionaisResult.push(r.getValorInteiro() == 1);
                execProx = condicionaisResult.peek();
                break;
            case RES_COND_SENAO:
                execProx = !condicionaisResult.pop();
                break;
        }
        return true;
    }
    
    public Variavel retornaVariavel(Token token){
        Variavel temp;
        String nomeVar = "temp" + token.getOrdem();
        switch (token.getFuncaoToken()){
            case IDENT_NOME_VARIAVEL:
                temp = variaveis.get(token.getPalavra());
                break;
            case CONST_CARACTER:
                temp = new Variavel(TipoVariavel.CARACTER, nomeVar);
                temp.setValor(token.getPalavra().replace("\"", ""));
                break;
            case CONST_INTEIRA:
                temp = new Variavel(TipoVariavel.INTEIRO, nomeVar);
                temp.setValor(token.getPalavra());
                break;
            case CONST_REAL:
                temp = new Variavel(TipoVariavel.REAL, nomeVar);
                temp.setValor(token.getPalavra());
                break;
            default:
                System.out.println("Tipo de token inválido para efetuar operação: " + token.getFuncaoToken());
                temp = null;
        }
        return temp;
    }
}
