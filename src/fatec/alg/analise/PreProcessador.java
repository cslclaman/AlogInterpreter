package fatec.alg.analise;

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
import fatec.alg.geral.log.Erro;
import fatec.alg.geral.log.TipoErro;
import fatec.alg.geral.tipo.TipoDado;
import fatec.alg.geral.modulo.ModuloPrincipal;
import fatec.alg.geral.programa.Programa;
import fatec.alg.geral.token.FuncaoToken;
import fatec.alg.geral.token.Token;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Analisador semântico e pré-processador que verifica a coerência das expressões criadas pelo Parser.
 * Sua função é analisar chamadas de função e pré-executar expressões,
 * gerando alertas para tipos inválidos, número de parâmetros incorretos em funções e variáveis mal definidas.
 * @author Caique
 */
public class PreProcessador extends Verificador {
    private static final Logger logger = Logger.getLogger(PreProcessador.class.getName());
    
    /**
     * Classe de verificação de variáveis: verifica se foi inicializada e se
     * possui usos pelo código.
     */
    private class VariavelVerif {
        private TipoDado tipo;
        private Token token;
        private int chamadas;
        private boolean inicializada;

        public VariavelVerif(Token token, TipoDado tipo) {
            this.token = token;
            this.tipo = tipo;
            this.inicializada = false;
            this.chamadas = 0;
        }
    }
    
    /**
     * Classe de verificação de rotinas: verifica o número de chamadas e o número
     * e tipo dos parâmetros.
     */
    private class RotinaVerif {
        protected int chamadas;
        protected TipoDado[] parametros;

        public RotinaVerif() {
            this.chamadas = 0;
        }
        
        public RotinaVerif(TipoDado[] parametros) {
            this.parametros = parametros;
            this.chamadas = 0;
        }
    }
    
    /**
     * Classe de verificação de funções: verifica as mesmas coisas que a rotina,
     * além de analisar seu tipo de retorno.
     */
    private class FuncaoVerif extends RotinaVerif {
        private TipoDado retorno; 
        
        public FuncaoVerif() {
            super();
        }
        
        public FuncaoVerif(TipoDado[] parametros) {
            super(parametros);
        }

        public FuncaoVerif(TipoDado[] parametros, TipoDado retorno) {
            super(parametros);
            this.retorno = retorno;
        }
    }
    
    private List<Instrucao> instrucoes;
    private Programa programa;
    private HashMap <String, VariavelVerif> variaveis;
    private HashMap <String, FuncaoVerif> funcoes;
    private int pos;
    private int len;
    private int contFilho;
    
    /**
     * Constrói um novo verificador e passa o programa a ser processado.
     * O programa consiste nas instruções já tratadas pelo Parser.
     * @param instrucoes Lista de instruções a serem verificadas.
     */
    public PreProcessador(List<Instrucao> instrucoes) {
        this.instrucoes = instrucoes;
        programa = new Programa();
        
        erros = new LinkedList<>();
        variaveis = new HashMap<>();
        funcoes = new HashMap<>();
        
        funcoes.put("pot", new FuncaoVerif(new TipoDado[]{TipoDado.REAL, TipoDado.REAL}, TipoDado.REAL));
        funcoes.put("raiz", new FuncaoVerif(new TipoDado[]{TipoDado.REAL}, TipoDado.REAL));
        
        pos = 0;
        len = instrucoes.size();
        contFilho = 0;
    }

    /**
     * Rotina para realizar a verificação.
     * Após sua execução, o resultado e os ocasionais erros podem ser consultados.
     */
    public void verificaPrograma(){
        while (pos < len) {
            if (contFilho == 0) {
                variaveis.clear();
            }
            
            Instrucao instrucao = instrucoes.get(pos++);
            verificaInstrucao(instrucao);
            
            if (contFilho == 0) {
                geraErroVariavelNaoUsada();
            }
        }
    }

    /**
     * Retorna o programa executável gerado após o pré-processamento.
     * @return Programa.
     */
    public Programa getPrograma() {
        return programa;
    }
    
    private void verificaInstrucao(Instrucao instrucao) {
        if (instrucao == null) {
            logger.log(Level.WARNING, "Instrução nula");
            return;
        }
        if (!instrucao.isValida()) {
            logger.log(Level.WARNING, "{0} {1} {2}",
                    new Object[]{
                        "Instrução \"",
                        instrucao.getTipo(),
                        "\" inválida/incorreta"
                    });
            return;
        }
        switch (instrucao.getTipo()) {
            case MODULO_PRINCIPAL:
                verificaBloco(instrucao);
                programa.setModuloPrincipal((ModuloPrincipal)instrucao);
                break;
                
            case BLOCO:
                verificaBloco(instrucao);
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
                logger.log(Level.WARNING, "{0} {1} {2}",
                    new Object[]{
                        "Instrução \"",
                        instrucao.getTipo(),
                        "\" não esperada"
                    });
                break;
        }
    }
    
    private void verificaBloco (Instrucao instrucao) {
        Bloco bloco = (Bloco) instrucao;
        PreProcessador verificadorInterno = new PreProcessador(bloco.listaInstrucoes());
        verificadorInterno.programa = this.programa;
        verificadorInterno.erros = this.erros;
        verificadorInterno.variaveis = this.variaveis;
        verificadorInterno.funcoes = this.funcoes;
        verificadorInterno.contFilho = this.contFilho + 1;
        
        verificadorInterno.verificaPrograma();

        this.erros = verificadorInterno.erros;
        this.variaveis = verificadorInterno.variaveis;
        this.funcoes = verificadorInterno.funcoes;
    }
    
    private void verificaDeclaracaoVariaveis(Instrucao instrucao) {
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
        verificaInstrucao(repetitiva.getInstrucao());
        verificaExpressao(condicao);
        geraErroTipoDadoCondicional(repetitiva.getTokenFaca(), condicao.getTipoResultado(), TipoDado.LOGICO);
    }
    
    private void verificaRepeticaoRepita(Instrucao instrucao) {
        RepeticaoRepita repetitiva = (RepeticaoRepita)instrucao;
        Expressao condicao = repetitiva.getCondicao();
        verificaInstrucao(repetitiva.getInstrucao());
        verificaExpressao(condicao);
        geraErroTipoDadoCondicional(repetitiva.getTokenRepita(), condicao.getTipoResultado(), TipoDado.LOGICO);
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
                varVerif.inicializada = true;
                varVerif.chamadas += 1;
                variaveis.replace(variavel.nome(), varVerif);
                verificaInstrucao(repetitiva.getInstrucao());
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
            case OPERANDO_CONSTANTE:
                verificaOperandoConstante(expressao);
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
            if (operador.getFuncaoToken() == FuncaoToken.OP_MAT_DIV_REAL) {
                operacao.setTipoResultado(TipoDado.REAL);
            } else {
                if (exprEsq.getTipoResultado() == TipoDado.INTEIRO && exprDir.getTipoResultado() == TipoDado.INTEIRO) {
                    operacao.setTipoResultado(TipoDado.INTEIRO);
                } else {
                    operacao.setTipoResultado(TipoDado.REAL);
                }
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
    
    private void verificaOperandoConstante(Expressao expressao) {
        Operando operando = (Operando)expressao;
        Token token = operando.getOperando();
        operando.setTipoResultado(TipoDado.mapTokenToVariavel(token));
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

    private int geraErroVariavelNaoUsada () {
        int count = 0;
        for (HashMap.Entry<String, VariavelVerif> variavel : variaveis.entrySet()) {
            
            VariavelVerif conteudo = variavel.getValue();
            if (conteudo.chamadas == 0) {
                erros.add(new Erro(TipoErro.INFO, conteudo.token, 
                        String.format("Variável \"%s\" não foi usada",
                            conteudo.token.getPalavra())));
                count ++;
            }
        }
        return count;
    }
    
    private int geraErroVariavelNaoInicializada(Token token) {
        int count = 0;
        VariavelVerif varVerif = variaveis.get(token.nome());
        if (!varVerif.inicializada) {
            erros.add(new Erro(TipoErro.ALERTA, token,
                String.format("Variável \"%s\" não inicializada",
                token.getPalavra())));
            count ++;
        }
        return count;
    }
    
    private int geraErroFuncaoParametros(Token token, TipoDado[] parametros) {
        int count = 0;
        TipoDado[] paramsDeclarados = funcoes.get(token.nome()).parametros;
        int numParamsEsperado = paramsDeclarados.length;
        int numParamsPassado = parametros.length;
        
        StringBuilder lista = new StringBuilder();
        
        if (numParamsEsperado != numParamsPassado) {
            erros.add(new Erro(TipoErro.ALERTA, token,
                String.format("Função \"%s\" encontrou %d parâmetro(s), mas esperava %d",
                token.getPalavra(), numParamsPassado, numParamsEsperado)));
            count ++;
        } else {
            for (int c = 0; c < numParamsEsperado; c++) {
                if (paramsDeclarados[c] != parametros[c] &&
                    !(paramsDeclarados[c] == TipoDado.REAL && parametros[c] == TipoDado.INTEIRO)
                ) {
                    count ++;
                    lista.append(String.format("\n %do Parâmetro: Encontrou %s, esperava %s.",
                            c + 1, parametros[c] == null ? "": parametros[c], paramsDeclarados[c]));
                }
            }
            if (count > 0) {
                erros.add(new Erro(TipoErro.ERRO, token, 
                String.format("Função \"%s\" com parâmetros de tipos inválidos:%s",
                token.getPalavra(), lista.toString())));
            }
        }
        return count;
    }
    
    private int geraErroTipoDadoInvalidoOperador (Token operador, Token operando, TipoDado encontrado, TipoDado... esperados) {
        StringBuilder lista = new StringBuilder();
        for (TipoDado esperado : esperados) {
            if (esperado == encontrado || 
                (esperado == TipoDado.REAL && encontrado == TipoDado.INTEIRO)) {
                return 0;
            } else {
                if (lista.length() > 0) {
                    lista.append(", ");
                }
                lista.append(esperado);
            }
        }
        erros.add(new Erro(TipoErro.ERRO, operando, 
                String.format("Tipo de dado inválido para operador \"%s\":\n Encontrou %s, esperava %s",
                operador.getPalavra(), encontrado, lista.toString())));
        return 1;
    }
    
    private int geraErroTipoDadoCondicional (Token token, TipoDado encontrado, TipoDado... esperados){
        StringBuilder lista = new StringBuilder();
        for (TipoDado esperado : esperados) {
            if (esperado == encontrado) {
                return 0;
            } else {
                if (lista.length() > 0) {
                    lista.append(" ou ");
                }
                lista.append(esperado);
            }
        }
        erros.add(new Erro(TipoErro.ERRO, token,
            String.format("Resultado da expressão inválido para estrutura \"%s\":\n Encontrou %s, esperava %s",
            token.getFuncaoToken().toString(), encontrado, lista.toString())));
        return 1;
    }
    
    private int geraErroTipoDadoAtribuicao (Token token, TipoDado encontrado){
        VariavelVerif varVerif = variaveis.get(token.nome());
        TipoDado esperado = varVerif.tipo;
        if (esperado == encontrado || 
            (esperado == TipoDado.REAL && encontrado == TipoDado.INTEIRO)) {
            return 0;
        } else {
            erros.add(new Erro(TipoErro.ERRO, token,
                String.format("Tipo de expressão inválido para atribuir à variável \"%s\":\n Encontrou %s, esperava %s",
                token.getPalavra(), encontrado, esperado)));
            return 1;
        }
    }
}
