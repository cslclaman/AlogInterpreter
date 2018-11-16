package fatec.alg.analise;

import fatec.alg.geral.tipo.TipoDado;
import fatec.alg.geral.expressao.Expressao;
import fatec.alg.geral.expressao.ChamadaFuncao;
import fatec.alg.geral.expressao.Operacao;
import fatec.alg.geral.expressao.OperacaoUnaria;
import fatec.alg.geral.expressao.Operando;
import fatec.alg.geral.expressao.TokenDelimitador;
import fatec.alg.geral.instrucao.Instrucao;
import fatec.alg.geral.instrucao.DeclaracaoVariaveis;
import fatec.alg.geral.instrucao.FimBloco;
import fatec.alg.geral.instrucao.RepeticaoRepita;
import fatec.alg.geral.instrucao.RepeticaoEnquanto;
import fatec.alg.geral.modulo.Bloco;
import fatec.alg.geral.instrucao.InstrucaoInvalida;
import fatec.alg.geral.instrucao.RepeticaoFaca;
import fatec.alg.geral.instrucao.SaidaDados;
import fatec.alg.geral.instrucao.Condicional;
import fatec.alg.geral.instrucao.RepeticaoPara;
import fatec.alg.geral.instrucao.Atribuicao;
import fatec.alg.geral.instrucao.EntradaDados;
import fatec.alg.geral.modulo.ModuloPrincipal;
import fatec.alg.geral.log.Erro;
import fatec.alg.geral.log.TipoErro;
import fatec.alg.geral.token.FuncaoToken;
import fatec.alg.geral.instrucao.TipoInstrucao;
import fatec.alg.geral.token.Token;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Analisador sintático (parser) que verifica uma sequência de tokens
 * de acordo com as regras gramaticais da linguagem e retorna expressões executáveis.
 * @author Caique
 */
public class AnalisadorSintatico extends Verificador {
    private static final Logger logger = Logger.getLogger(AnalisadorSintatico.class.getName());
    
    private final List<Token> tokens;
    
    private Map<String, TipoDado> declVariaveis;
    
    private LinkedList<FuncaoToken> funcoesEsperadas;
    private TipoInstrucao tipoUltimaInstrucao;
    private int pos;
    private int size;
    private boolean fimAtingido;
    
    /**
     * Inicializa o analisador com sequência de tokens.
     * @param tokens Lista de tokens gerada pelo {@link AnalisadorLexico}
     */
    public AnalisadorSintatico (List<Token> tokens){
        super();
        
        this.tokens = tokens;
        this.size = tokens.size();
        declVariaveis = new HashMap<>();
        funcoesEsperadas = new LinkedList<>();
        tipoUltimaInstrucao = TipoInstrucao._INDEFINIDO;
        
        pos = 0;
        fimAtingido = false;
        
        funcoesEsperadas = funcoesAlgoritmo();
    }
    
    /**
     * Retorna se ainda há tokens a serem analisados.
     * @return True se ainda houverem tokens a analisar
     */
    public boolean existeProxima(){
        return pos < size;
    }
    
    /**
     * Retorna uma instrução formada por tokens ainda não analisados e que
     * formem uma instrução sintaticamente válida.
     * <p>Erros possíveis:
     * <ul>
     * <li>Nenhum token encontrado (lista de tokens vazia)
     * <li>Fim do programa atingido (método chamado quando não existem mais tokens a serem lidos)
     * <li>Bloco vazio declarado
     * <li>Variável não declarada
     * <li>Início de instrução não reconhecido (token com função inválida para iniciar instrução)
     * </ul></p>
     * @return Instrução gerada, ou uma {@link InstrucaoInvalida} caso não haja
     * mais tokens a serem lidos ou caso os tokens não estejam em sequência válida.
     */
    public Instrucao proxima(){
        Instrucao instrucao = new InstrucaoInvalida();
        
        if (!existeProxima()){
            if (size <= 0){
                erros.add(new Erro(TipoErro.ERRO, ' ', 0, 0, 0,
                        "Nenhum token encontrado"));
            } else {
                erros.add(new Erro(TipoErro.ALERTA, tokens.get(size - 1),
                        "Fim do programa atingido"));
            }
            instrucao.fazValidacao();
            return instrucao;
        } 
        
        Token token = tokens.get(pos);
        if (!funcaoValida(token)){
            pos++;
            instrucao.addToken(token);
            instrucao.fazValidacao();
            return instrucao;
        }
        
        switch(token.getFuncaoToken()){
            
            //Algoritmo nomeado
            case RES_ALGORITMO:
                instrucao = instrucaoModuloPrincipal();
                if (((ModuloPrincipal)instrucao).listaInstrucoes().isEmpty()) {
                    erros.add(new Erro(TipoErro.ALERTA, ((ModuloPrincipal)instrucao).getTipoModulo(),
                        "Módulo principal vazio declarado"));
                }
                funcoesEsperadas.clear();
                break;
                
            //Inicializa uma instrução tipo bloco
            case RES_BLOCO_INICIO:
                instrucao = instrucaoBloco();
                if (((Bloco)instrucao).listaInstrucoes().isEmpty()) {
                    erros.add(new Erro(TipoErro.ALERTA, ((Bloco)instrucao).getInicio(),
                        "Bloco vazio declarado"));
                }
                
                funcoesEsperadas.clear();
                switch (tipoUltimaInstrucao){
                    case BLOCO:
                    case MODULO_PRINCIPAL:
                        funcoesEsperadas = funcoesBloco();
                        break;
                    case _INDEFINIDO:
                        break;
                }
                break;

            // Ao finalizar o bloco
            case RES_BLOCO_FIM:
                instrucao = new FimBloco();
                instrucao.addToken(token);
                fimAtingido = true;
                break;

            //Inicializa instrução de declaração de variáveis
            case RES_TIPO_CARACTER:
            case RES_TIPO_INTEIRO:
            case RES_TIPO_REAL:
                instrucao = instrucaoDeclaracaoVariaveis();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
        
            //Inicializa instrução de entrada de dados
            case LIB_IO_LEIA:
                instrucao = instrucaoEntradaDados();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Inicializa instrução de saída de dados
            case LIB_IO_ESCREVA:
                instrucao = instrucaoSaidaDados();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Define Chamada de Rotina ou Atribuição.
            case _INDEF_ALFABETICO:
            case _INDEF_ALFANUMERICO:
                if (declVariaveis.containsKey(token.nome())){
                    //Inicializa instrução de atribuição
                    token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                    tokens.set(pos, token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.IDENT_NOME_VARIAVEL);
                    
                    instrucao = instrucaoAtribuicao();
                    funcoesEsperadas.clear();
                    funcoesEsperadas = funcoesBloco();
                } else {
                    erros.add(new Erro(TipoErro.ERRO, token, 
                            String.format("Variável \"%s\" não declarada", token.getPalavra())));
                    
                    pos++;
                    instrucao.addToken(token);
                }
                break;
                
            //Define estrutura Condicional
            case RES_COND_SE:
                instrucao = instrucaoCondicional();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Define estrutura Repetitiva "Enquanto-Faça"
            case RES_REP_ENQUANTO:
                instrucao = instrucaoRepeticaoEnquanto();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Define estrutura Repetitiva "Enquanto-Faça"
            case RES_REP_FACA:
                instrucao = instrucaoRepeticaoFaca();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Define estrutura Repetitiva "Para-de-até-Faça"
            case RES_REP_PARA:
                instrucao = instrucaoRepeticaoPara();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Define estrutura Repetitiva "Para-de-até-Faça"
            case RES_REP_REPITA:
                instrucao = instrucaoRepeticaoRepita();
                funcoesEsperadas.clear();
                funcoesEsperadas = funcoesBloco();
                break;
                
            //Em caso de instrução ainda não implementada
            default:
                instrucao.addToken(token);
                erros.add(new Erro(TipoErro.ERRO, token, 
                            String.format("Início de instrução não reconhecido: \"%s\"", token.getPalavra())));
                pos++;
                break;
        }
        
        instrucao.fazValidacao();
        return instrucao;
    }
    
    /**
     * Retorna uma lista com todas as instruções geradas.
     * Executa internamente o método {@link #proxima() } até atingir o fim da lista de tokens.
     * @return Lista com instruções
     */
    public LinkedList<Instrucao> listaInstrucoes(){
        LinkedList<Instrucao> lista = new LinkedList<>();
        while (existeProxima()){
            lista.add(proxima());
        }
        return lista;
    }
    
    /**
     * Verifica se o token informado tem uma função dentro das esperadas para a instrução sendo analisada.
     * @param token O token a ser verificado
     * @return TRUE caso a função do token seja uma das funções esperadas atuais, 
     * ou FALSE e gera um erro caso contrário.
     */
    private boolean funcaoValida(Token token){
        if (!funcoesEsperadas.contains(token.getFuncaoToken())){
            String msgErro = "Encontrou " + token.getFuncaoToken() + ", mas esperava:";
            for (FuncaoToken ft: funcoesEsperadas){
                msgErro += "\n - " + ft;
            }
            erros.add(new Erro(TipoErro.ERRO, token, msgErro));
            return false;
        } else {
            return true;
        }
    }
        
    /**
     * Analisa a sintaxe de um módulo principal.
     * <br>Sintaxe válida:
     * <p><code>Algoritmo &lt;Nome do algoritmo sem espaços&gt;
     * <p><code>Início
     * <br>    ... &lt;lista de instruções&gt; ...
     * <br>Fim</code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Nome inválido (iniciado em números ou sem espaço)
     * <li>Módulo iniciado não fechado corretamente (falta de "FIM")
     * </ul>
     * @return Módulo Principal.
     */
    private Bloco instrucaoModuloPrincipal(){
        ModuloPrincipal moduloPrincipal = new ModuloPrincipal();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                moduloPrincipal.invalidaInstrucao();
            }
        
            switch (token.getFuncaoToken()){
                case RES_ALGORITMO:
                    moduloPrincipal.setTipoModulo(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    break;
                            
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    char inicial = token.nome().charAt(0);
                    if (Character.isDigit(inicial)){
                        moduloPrincipal.addToken(token);
                        erros.add(new Erro(TipoErro.ERRO, token,
                                "Identificador de variável não pode começar com número"));
                        moduloPrincipal.invalidaInstrucao();
                    } else {
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_ALGORITMO);
                        moduloPrincipal.setNome(token);
                    }
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                    break;
                    
                //Para pessoas que insistirem em dar nome entre aspas para o algoritmo (como se fosse o Portugol do Visualg)
                case CONST_CARACTER:
                    moduloPrincipal.setNome(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_INICIO);
                    break;
                    
                case RES_BLOCO_INICIO:
                    moduloPrincipal.setInicio(token);

                    AnalisadorSintatico parserInterno = new AnalisadorSintatico(tokens);
                    parserInterno.declVariaveis = declVariaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = moduloPrincipal.getTipo();
                    parserInterno.funcoesEsperadas = funcoesBloco();

                    while (parserInterno.existeProxima() && !parserInterno.fimAtingido){
                        Instrucao proxima = parserInterno.proxima();
                        if (!(proxima instanceof FimBloco)){
                            moduloPrincipal.addInstrucao(proxima);
                        }
                    }

                    erros.addAll(parserInterno.erros);
                    pos = parserInterno.pos;
                    declVariaveis = parserInterno.declVariaveis;

                    if (!existeProxima() && !parserInterno.fimAtingido){
                        erros.add(new Erro(
                                TipoErro.ERRO, 
                                moduloPrincipal.getInicio(),
                                "Módulo principal iniciado não foi fechado corretamente"));
                        go = false;
                    } 
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.RES_BLOCO_FIM);
                    break;
                    
                case RES_BLOCO_FIM:
                    moduloPrincipal.setFim(token);
                    break;
            }
        }
        
        return moduloPrincipal;
    }
    
    /**
     * Analisa a sintaxe de um bloco de instruções.
     * <br>Sintaxe válida:
     * <p><code>Início
     * <br>    ... &lt;lista de instruções&gt; ...
     * <br>Fim</code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Bloco iniciado não fechado corretamente (falta de "FIM")
     * </ul>
     * @return Bloco de instruções.
     */
    private Bloco instrucaoBloco(){
        Token token = tokens.get(pos++);
        Bloco bloco;
        if (tipoUltimaInstrucao == TipoInstrucao._INDEFINIDO) {
            bloco = ModuloPrincipal.moduloImplicito();
        } else {
            bloco = new Bloco();
        }
        bloco.setInicio(token);

        AnalisadorSintatico parserInterno = new AnalisadorSintatico(tokens);
        parserInterno.declVariaveis = declVariaveis;
        parserInterno.pos = pos;
        parserInterno.tipoUltimaInstrucao = bloco.getTipo();
        parserInterno.funcoesEsperadas = funcoesBloco();

        while (parserInterno.existeProxima() && !parserInterno.fimAtingido){
            Instrucao proxima = parserInterno.proxima();
            if (!(proxima instanceof FimBloco)){
                bloco.addInstrucao(proxima);
            }
        }

        erros.addAll(parserInterno.erros);
        pos = parserInterno.pos;
        declVariaveis = parserInterno.declVariaveis;

        if (!existeProxima() && !parserInterno.fimAtingido){
            erros.add(new Erro(
                    TipoErro.ERRO, 
                    bloco.getInicio(),
                    "Bloco iniciado não foi fechado corretamente"));
        } else {
            token = tokens.get(pos++);
            if (token.getFuncaoToken() == FuncaoToken.RES_BLOCO_FIM){
                bloco.setFim(token);
            } 
        }
        return bloco;
    }
    
    /**
     * Analisa a sintaxe de uma declaração de variáveis.
     * <br>Sintaxe válida:
     * <p><code>&lt;Tipo de dado&gt; : &lt;Identificador1&gt; [ , &lt;Identificador2&gt; , ... ] ;
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Identificador de variável não pode começar com número
     * <li>Variável já declarada
     * </ul>
     * @return Declaração de variáveis.
     */
    private DeclaracaoVariaveis instrucaoDeclaracaoVariaveis(){
        DeclaracaoVariaveis declaracaoVariaveis = new DeclaracaoVariaveis();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                declaracaoVariaveis.invalidaInstrucao();
            }
            
            switch (token.getFuncaoToken()){
                case RES_TIPO_CARACTER:
                case RES_TIPO_INTEIRO:
                case RES_TIPO_REAL:
                //case RES_TIPO_LOGICO:
                    declaracaoVariaveis.setTokenTipoVariavel(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_DOIS_PONTOS);
                    break;
                
                case DELIM_DOIS_PONTOS:
                case DELIM_VIRGULA:
                    declaracaoVariaveis.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    break;
                            
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    char inicial = token.nome().charAt(0);
                    if (Character.isDigit(inicial)){
                        declaracaoVariaveis.addToken(token);
                        erros.add(new Erro(TipoErro.ERRO, token,
                                "Identificador de variável não pode começar com número"));
                        declaracaoVariaveis.invalidaInstrucao();
                    } else {
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        if (declVariaveis.containsKey(token.nome())){
                            declaracaoVariaveis.addToken(token);
                            erros.add(new Erro(TipoErro.ERRO, token,
                                    String.format("Variável \"%s\" já declarada", token.getPalavra())));
                            declaracaoVariaveis.invalidaInstrucao();
                        } else {
                            declaracaoVariaveis.addNomeVariavel(token);
                            declVariaveis.put(token.nome(), declaracaoVariaveis.getTipoVariavel());
                        }
                    }
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                    break;
                            
                case DELIM_PONTO_VIRGULA:
                    declaracaoVariaveis.addToken(token);
                    go = false;
                    break;
                    
                default:
                    declaracaoVariaveis.addToken(token);
                    break;
            }
                    
        }
        
        return declaracaoVariaveis;
    }
    
    /**
     * Analisa a sintaxe de chamada de rotina de entrada de dados para variáveis.
     * <br>Sintaxe válida:
     * <p><code>Leia ( &lt;Identificador1&gt; [ , &lt;Identificador2&gt; , ... ] ) ;
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Variável não declarada
     * </ul>
     * @return Declaração de variáveis.
     */
    private EntradaDados instrucaoEntradaDados(){
        EntradaDados entradaDados = new EntradaDados();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                entradaDados.invalidaInstrucao();
            }
            
            switch (token.getFuncaoToken()){
                case LIB_IO_LEIA:
                    entradaDados.setTokenNome(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                case DELIM_VIRGULA:
                    entradaDados.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    break;
                            
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    if (declVariaveis.containsKey(token.nome())){
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        entradaDados.addVariavel(token);
                    } else {
                        entradaDados.addToken(token);
                        erros.add(new Erro(TipoErro.ERRO, token, 
                        String.format("Variável \"%s\" não declarada", token.getPalavra())));
                        entradaDados.invalidaInstrucao();
                    }
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_VIRGULA);
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    entradaDados.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                    break;
                            
                case DELIM_PONTO_VIRGULA:
                    entradaDados.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
                    
                default:
                    entradaDados.addToken(token);
                    break;
            }
                    
        }
        
        return entradaDados;
    }
    
    /**
     * Analisa a sintaxe de chamada de rotina de saída de dados.
     * <br>Sintaxe válida:
     * <p><code>Escreva ( &lt;Expressão1&gt; [ , &lt;Expressão2&gt; , ... ] ) ;
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Vide {@link #instrucaoExpressao(fatec.alg.geral.token.FuncaoToken...) }
     * </ul>
     * @return Declaração de variáveis.
     */
    private SaidaDados instrucaoSaidaDados(){
        SaidaDados saidaDados = new SaidaDados();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                saidaDados.invalidaInstrucao();
            }
            
            switch (token.getFuncaoToken()){
                case LIB_IO_ESCREVA:
                    saidaDados.setTokenNome(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                case DELIM_VIRGULA:
                    saidaDados.addToken(token);
                    funcoesEsperadas.clear();
                    saidaDados.addParametro(instrucaoExpressao(
                            FuncaoToken.DELIM_VIRGULA, FuncaoToken.DELIM_PARENTESES_FECHA));
                    break;
                            
                case DELIM_PARENTESES_FECHA:
                    saidaDados.addToken(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                    break;
                            
                case DELIM_PONTO_VIRGULA:
                    saidaDados.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
                    
                default:
                    saidaDados.addToken(token);
                    break;
            }
                    
        }
        
        return saidaDados;
    }
    
    /**
     * Analisa a sintaxe de operação de atribuição.
     * <br>Sintaxe válida:
     * <p><code>&lt;IdentificadorVariável&gt; &lt;- &lt;Expressão&gt; ;
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Vide {@link #instrucaoExpressao(fatec.alg.geral.token.FuncaoToken...) }
     * </ul>
     * @return Atribuição.
     */
    private Atribuicao instrucaoAtribuicao(){
        Atribuicao atribuicao = new Atribuicao();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                atribuicao.invalidaInstrucao();
            }
            
            switch (token.getFuncaoToken()){
                case IDENT_NOME_VARIAVEL:
                    atribuicao.setVariavel(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.OP_ATRIBUICAO);
                    break;
                    
                case OP_ATRIBUICAO:
                    atribuicao.addToken(token);
                    funcoesEsperadas.clear();
                    atribuicao.setExpressao(instrucaoExpressao(FuncaoToken.DELIM_PONTO_VIRGULA));
                    break;
                    
                case DELIM_PONTO_VIRGULA:
                    atribuicao.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
                    
                default:
                    atribuicao.addToken(token);
                    break;
            }
        }
        return atribuicao;
    }
    
    /**
     * Analisa a sintaxe de instrução condicional.
     * <br>Sintaxe válida (para condicional simples):
     * <p><code>Se &lt; Expressão &gt; Então
     * <br>... &lt; instrução ou bloco &gt; ...
     * </code></p>
     * <br>Sintaxe válida (para condicional composta):
     * <p><code>Se &lt; Expressão &gt; Então
     * <br>... &lt; instrução ou bloco caso verdadeiro &gt; ...
     * <br>Senão
     * <br>... &lt; instrução ou bloco caso falso &gt; ...
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Vide {@link #instrucaoExpressao(fatec.alg.geral.token.FuncaoToken...) }
     * </ul>
     * @return Condicional.
     */
    private Condicional instrucaoCondicional(){
        Condicional condicional = new Condicional();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                condicional.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case RES_COND_SE:
                    condicional.setTokenSe(token);
                    funcoesEsperadas.clear();
                    condicional.setExpressao(instrucaoExpressao(FuncaoToken.RES_COND_ENTAO));
                    break;
                    
                case RES_COND_ENTAO:
                    condicional.setTokenEntao(token);
                    funcoesEsperadas.clear();
                    
                    AnalisadorSintatico parserInterno = new AnalisadorSintatico(tokens);
                    parserInterno.declVariaveis = declVariaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = condicional.getTipo();
                    parserInterno.funcoesEsperadas = funcoesEstrutura();
                    
                    Instrucao instrucaoInterna = parserInterno.proxima();
                    erros.addAll(parserInterno.erros);
                    condicional.setInstrucaoSe(instrucaoInterna);
                    
                    pos = parserInterno.pos;
                    declVariaveis = parserInterno.declVariaveis;
                    go = false;
                    break;
            }
        }
        
        if (existeProxima()){
            Token token = tokens.get(pos);
            if (token.getFuncaoToken() == FuncaoToken.RES_COND_SENAO){
                pos++;
                condicional.setTokenSenao(token);
                
                AnalisadorSintatico parserInterno = new AnalisadorSintatico(tokens);
                parserInterno.declVariaveis = declVariaveis;
                parserInterno.pos = pos;
                parserInterno.tipoUltimaInstrucao = condicional.getTipo();
                parserInterno.funcoesEsperadas = funcoesEstrutura();

                Instrucao instrucaoInterna = parserInterno.proxima();
                erros.addAll(parserInterno.erros);
                condicional.setInstrucaoSenao(instrucaoInterna);
                
                pos = parserInterno.pos;
                declVariaveis = parserInterno.declVariaveis;
            }
        }
        
        return condicional;
    }
    
    /**
     * Analisa a sintaxe de expressão enquanto-faça
     * (nº de repetições indeterminado com teste no início).
     * <br>Sintaxe válida:
     * <p><code>Enquanto &lt; Expressão Condição &gt; Faça
     * <br>... &lt; instrução ou bloco &gt; ...
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Vide {@link #instrucaoExpressao(fatec.alg.geral.token.FuncaoToken...) }
     * </ul>
     * @return Repetição Enquanto-Faça.
     */
    private RepeticaoEnquanto instrucaoRepeticaoEnquanto(){
        RepeticaoEnquanto repetitiva = new RepeticaoEnquanto();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                repetitiva.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case RES_REP_ENQUANTO:
                    repetitiva.setTokenEnquanto(token);
                    funcoesEsperadas.clear();
                    repetitiva.setExpressao(instrucaoExpressao(FuncaoToken.RES_REP_FACA));
                    break;
                    
                case RES_REP_FACA:
                    repetitiva.addToken(token);
                    funcoesEsperadas.clear();
                    
                    AnalisadorSintatico parserInterno = new AnalisadorSintatico(tokens);
                    parserInterno.declVariaveis = declVariaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = repetitiva.getTipo();
                    parserInterno.funcoesEsperadas = funcoesEstrutura();
                    
                    Instrucao instrucaoInterna = parserInterno.proxima();
                    erros.addAll(parserInterno.erros);
                    repetitiva.setInstrucao(instrucaoInterna);

                    pos = parserInterno.pos;
                    declVariaveis = parserInterno.declVariaveis;
                    go = false;
                    break;
            }
        }
        
        return repetitiva;
    }
    
    /**
     * Analisa a sintaxe de expressão faça-enquanto
     * (nº de repetições indeterminado com teste no final, condição de parada positiva).
     * <br>Sintaxe válida:
     * <p><code>Faça 
     * <br>... &lt; instrução ou bloco &gt; ...
     * <br>Enquanto &lt; Expressão Condição &gt; ;
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Vide {@link #instrucaoExpressao(fatec.alg.geral.token.FuncaoToken...) }
     * </ul>
     * @return Repetição Faça-Enquanto.
     */
    private RepeticaoFaca instrucaoRepeticaoFaca(){
        RepeticaoFaca repetitiva = new RepeticaoFaca();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                repetitiva.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case RES_REP_FACA:
                    repetitiva.setTokenFaca(token);
                    funcoesEsperadas.clear();
                    
                    AnalisadorSintatico parserInterno = new AnalisadorSintatico(tokens);
                    parserInterno.declVariaveis = declVariaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = repetitiva.getTipo();
                    parserInterno.funcoesEsperadas = funcoesEstrutura();
                    
                    Instrucao instrucaoInterna = parserInterno.proxima();
                    erros.addAll(parserInterno.erros);
                    repetitiva.setInstrucao(instrucaoInterna);
                    
                    pos = parserInterno.pos;
                    declVariaveis = parserInterno.declVariaveis;
                    
                    funcoesEsperadas.add(FuncaoToken.RES_REP_ENQUANTO);
                    break;
                    
                case RES_REP_ENQUANTO:
                    repetitiva.setTokenEnquanto(token);
                    repetitiva.setExpressao(instrucaoExpressao(FuncaoToken.DELIM_PONTO_VIRGULA));
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PONTO_VIRGULA);
                    break;
                    
                case DELIM_PONTO_VIRGULA:
                    repetitiva.addToken(token);
                    funcoesEsperadas.clear();
                    go = false;
                    break;
            }
        }

        return repetitiva;
    }
    
    /**
     * Analisa a sintaxe de expressão repita-até
     * (nº de repetições indeterminado com teste no final, condição de parada negativa).
     * <br>Sintaxe válida:
     * <p><code>Repita 
     * <br>... &lt; instrução ou bloco &gt; ...
     * <br>Até &lt; Expressão Condição &gt; ;
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Vide {@link #instrucaoExpressao(fatec.alg.geral.token.FuncaoToken...) }
     * </ul>
     * @return Repetição Faça-Enquanto.
     */
    private RepeticaoRepita instrucaoRepeticaoRepita(){
        RepeticaoRepita repetitiva = new RepeticaoRepita();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                repetitiva.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case RES_REP_REPITA:
                    repetitiva.setTokenRepita(token);
                    funcoesEsperadas.clear();
                    
                    AnalisadorSintatico parserInterno = new AnalisadorSintatico(tokens);
                    parserInterno.declVariaveis = declVariaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = repetitiva.getTipo();
                    parserInterno.funcoesEsperadas = funcoesEstrutura();
                    
                    Instrucao instrucaoInterna = parserInterno.proxima();
                    erros.addAll(parserInterno.erros);
                    repetitiva.setInstrucao(instrucaoInterna);
                    
                    pos = parserInterno.pos;
                    declVariaveis = parserInterno.declVariaveis;
                    
                    funcoesEsperadas.add(FuncaoToken.RES_REP_ATE);
                    break;
                    
                case RES_REP_ATE:
                    repetitiva.setTokenAte(token);
                    repetitiva.setExpressao(instrucaoExpressao(FuncaoToken.DELIM_PONTO_VIRGULA));
                    break;
                    
                case DELIM_PONTO_VIRGULA:
                    repetitiva.addToken(token);
                    funcoesEsperadas.clear();
                    go = false;
                    break;
            }
        }

        return repetitiva;
    }
    
    /**
     * Analisa a sintaxe de expressão para-faça
     * (nº de repetições determinado).
     * <br>Sintaxe válida:
     * <p><code>Para &lt; variável &gt; De &lt; expressão ou constante &gt; Até &lt; Expressão ou constante &gt; Faça
     * <br>... &lt; instrução ou bloco &gt; ...
     * </code></p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Variável não declarada
     * <li>Outras: vide {@link #instrucaoExpressao(fatec.alg.geral.token.FuncaoToken...) }
     * </ul>
     * @return Repetição Faça-Enquanto.
     */
    private RepeticaoPara instrucaoRepeticaoPara(){
        RepeticaoPara repetitiva = new RepeticaoPara();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                repetitiva.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case RES_REP_PARA:
                    repetitiva.setTokenPara(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    break;
                            
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    if (declVariaveis.containsKey(token.nome())){
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        repetitiva.setVariavelCont(token);
                    } else {
                        repetitiva.addToken(token);
                        erros.add(new Erro(TipoErro.ERRO, token, 
                        String.format("Variável \"%s\" não declarada", token.getPalavra())));
                        repetitiva.invalidaInstrucao();
                    }
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken._INDEFINIDO_RES_DE);
                    break;
                
                case _INDEFINIDO_RES_DE:
                    token.setFuncaoToken(FuncaoToken.RES_REP_DE);
                    repetitiva.addToken(token);
                    repetitiva.setExpressaoDe(instrucaoExpressao(FuncaoToken.RES_REP_ATE));
                    break;
                    
                case RES_REP_ATE:
                    repetitiva.addToken(token);
                    repetitiva.setExpressaoAte(instrucaoExpressao(FuncaoToken.RES_REP_FACA));
                    break;
                    
                case RES_REP_FACA:
                    repetitiva.setTokenFaca(token);
                    funcoesEsperadas.clear();
                    
                    AnalisadorSintatico parserInterno = new AnalisadorSintatico(tokens);
                    parserInterno.declVariaveis = declVariaveis;
                    parserInterno.pos = pos;
                    parserInterno.tipoUltimaInstrucao = repetitiva.getTipo();
                    parserInterno.funcoesEsperadas = funcoesEstrutura();
                    
                    Instrucao instrucaoInterna = parserInterno.proxima();
                    erros.addAll(parserInterno.erros);
                    if (instrucaoInterna.isValida()){
                        repetitiva.setInstrucao(instrucaoInterna);
                    }
                    
                    pos = parserInterno.pos;
                    declVariaveis = parserInterno.declVariaveis;
                    go = false;
                    break;
            }
        }

        return repetitiva;
    }
    
    /**
     * Analisa uma expressão e gera sua árvore sintática.
     * <p>A expressão pode ser:
     * <ul>
     * <li>Operando Constante (ex.: "texto", 1.25 )
     * <li>Operando Variável (ex.: Num1, Contador, ValorTotal )
     * <li>Operando função (ex.: Pot(2, 8) )
     * <li>Unária (ex. - &lt;Operando&gt; )
     * <li>Aritmética (ex. &lt;Expressão&gt; / &lt;Expressão&gt; )
     * <li>Relacional (ex. &lt;Expressão&gt; = &lt;Expressão&gt; )
     * <li>Lógica (ex. &lt;Expressão&gt; OU &lt;Expressão&gt; )
     * </ul></p>
     * <p>
     * Qualquer tipo de expressão pode ser circundada por parênteses.
     * </p>
     * <br>Erros possíveis:
     * <ul>
     * <li>Variável não declarada
     * <li>Parêntese de abertura não encontrado
     * <li>Parêntese de fechamento não encontrado
     * <li>Erro ao montar expressão - operando não encontrado
     * <li>Alerta ao montar expressão - não pôde encontrar parênteses
     * <li>Erro ao finalizar expressão - última operação inválida ou nula
     * </ul>
     * @param condicoesParada Tokens de finalização da expressão (ponto e vírgula, vírgula, etc, dependendo do contexto).
     * @return Árvore de expressões
     */
    private Expressao instrucaoExpressao(FuncaoToken... condicoesParada) {
        boolean expressaoValida = true;
        Token token;
        Token topo;
        LinkedList<Token> pilhaTokens = new LinkedList<>();
        LinkedList<Token> saida = new LinkedList<>();
        HashMap<Token, ChamadaFuncao> mapaFuncoes = new HashMap<>();
        
        int balancParenteses = 0;
        funcoesEsperadas.clear();
        funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
        funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
        funcoesEsperadas.add(FuncaoToken.CONST_INTEIRA);
        funcoesEsperadas.add(FuncaoToken.CONST_REAL);
        funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
        funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
        funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
        funcoesEsperadas.add(FuncaoToken.OP_SIG_NEGATIVO);
        funcoesEsperadas.add(FuncaoToken.OP_SIG_POSITIVO);
        funcoesEsperadas.add(FuncaoToken.OP_LOG_NAO);
                    
        while (existeProxima()){
            token = tokens.get(pos++);
            if (!funcaoValida(token)) {
                expressaoValida = false;
                break;
            }
            boolean parada = false;
            for (FuncaoToken condParada : condicoesParada) {
                if (condParada == token.getFuncaoToken()) {
                    if (condParada == FuncaoToken.DELIM_PARENTESES_FECHA &&
                            balancParenteses > 0) {
                        break;
                    }
                    parada = true;
                    break;
                }
            }
            if (parada) {
                pos--;
                break;
            }
            switch (token.getFuncaoToken()) {
                case DELIM_PARENTESES_ABRE:
                    saida.add(token);
                    pilhaTokens.push(token);
                    balancParenteses ++;
                    funcoesEsperadas.add(FuncaoToken.OP_SIG_NEGATIVO);
                    funcoesEsperadas.add(FuncaoToken.OP_SIG_POSITIVO);
                    funcoesEsperadas.add(FuncaoToken.OP_LOG_NAO);
                    break;
                    
                case CONST_CARACTER:
                    saida.add(token);
                    funcoesEsperadas.clear();
                    for (FuncaoToken condicaoParada : condicoesParada) {
                        funcoesEsperadas.add(condicaoParada);
                    }
                    if (balancParenteses > 0) {
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    }
                    funcoesEsperadas.addAll(funcoesExpressaoRelacional());
                    funcoesEsperadas.addAll(funcoesExpressaoLogica());
                    break;
                    
                case _INDEF_ALFABETICO:
                case _INDEF_ALFANUMERICO:
                    if (declVariaveis.containsKey(token.nome())){
                        token.setFuncaoToken(FuncaoToken.IDENT_NOME_VARIAVEL);
                        saida.add(token);
                    } else {
                        erros.add(new Erro(TipoErro.ERRO, token, 
                        String.format("Variável \"%s\" não declarada", token.getPalavra())));
                        expressaoValida = false;
                    }
                    funcoesEsperadas.clear();
                    for (FuncaoToken condicaoParada : condicoesParada) {
                        funcoesEsperadas.add(condicaoParada);
                    }
                    if (balancParenteses > 0) {
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    }
                    funcoesEsperadas.addAll(funcoesExpressaoAritmetica());
                    funcoesEsperadas.addAll(funcoesExpressaoRelacional());
                    funcoesEsperadas.addAll(funcoesExpressaoLogica());
                    break;
                    
                case CONST_INTEIRA:
                case CONST_REAL:
                    saida.add(token);
                    funcoesEsperadas.clear();
                    for (FuncaoToken condicaoParada : condicoesParada) {
                        funcoesEsperadas.add(condicaoParada);
                    }
                    if (balancParenteses > 0) {
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    }
                    funcoesEsperadas.addAll(funcoesExpressaoAritmetica());
                    funcoesEsperadas.addAll(funcoesExpressaoRelacional());
                    funcoesEsperadas.addAll(funcoesExpressaoLogica());
                    break;
                    
                case LIB_MATH_POT:
                case LIB_MATH_RAIZ:
                    pos--;
                    ChamadaFuncao chamadaFuncao = expressaoChamadaFuncao();
                    mapaFuncoes.put(chamadaFuncao.getTokenNome(), chamadaFuncao);
                    
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            pilhaTokens.push(topo);
                            break;
                        } else {
                            if (topo.getPrecedencia() < token.getPrecedencia()) {
                                pilhaTokens.push(topo);
                                break;
                            } else {
                                saida.add(topo);
                            }
                        }
                    }
                    pilhaTokens.push(token);
                    
                    for (FuncaoToken condicaoParada : condicoesParada) {
                        funcoesEsperadas.add(condicaoParada);
                    }
                    if (balancParenteses > 0) {
                        funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_FECHA);
                    }
                    funcoesEsperadas.addAll(funcoesExpressaoAritmetica());
                    funcoesEsperadas.addAll(funcoesExpressaoRelacional());
                    funcoesEsperadas.addAll(funcoesExpressaoLogica());
                    break;
                    
                case OP_SIG_NEGATIVO:
                case OP_SIG_POSITIVO:
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            pilhaTokens.push(topo);
                            break;
                        } else {
                            if (topo.getPrecedencia() < token.getPrecedencia()) {
                                pilhaTokens.push(topo);
                                break;
                            } else {
                                saida.add(topo);
                            }
                        }
                    }
                    pilhaTokens.push(token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_INTEIRA);
                    funcoesEsperadas.add(FuncaoToken.CONST_REAL);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    break;
                
                case OP_LOG_NAO:
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            pilhaTokens.push(topo);
                            break;
                        } else {
                            if (topo.getPrecedencia() < token.getPrecedencia()) {
                                pilhaTokens.push(topo);
                                break;
                            } else {
                                saida.add(topo);
                            }
                        }
                    }
                    pilhaTokens.push(token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.CONST_INTEIRA);
                    funcoesEsperadas.add(FuncaoToken.CONST_REAL);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    funcoesEsperadas.add(FuncaoToken.OP_SIG_NEGATIVO);
                    funcoesEsperadas.add(FuncaoToken.OP_SIG_POSITIVO);
                    break;
                    
                case OP_MAT_SOMA:
                case OP_MAT_SUBTRACAO:
                case OP_MAT_MULTIPLICACAO:
                case OP_MAT_DIV_REAL:
                case OP_MAT_DIV_INTEIRA:
                case OP_MAT_MOD:
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            pilhaTokens.push(topo);
                            break;
                        } else {
                            if (topo.getPrecedencia() < token.getPrecedencia()) {
                                pilhaTokens.push(topo);
                                break;
                            } else {
                                saida.add(topo);
                            }
                        }
                    }
                    pilhaTokens.push(token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_INTEIRA);
                    funcoesEsperadas.add(FuncaoToken.CONST_REAL);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    funcoesEsperadas.add(FuncaoToken.OP_SIG_NEGATIVO);
                    funcoesEsperadas.add(FuncaoToken.OP_SIG_POSITIVO);
                    break;
                    
                case OP_REL_MAIOR:
                case OP_REL_MAIOR_IGUAL:
                case OP_REL_MENOR:
                case OP_REL_MENOR_IGUAL:
                case OP_REL_IGUAL:
                case OP_REL_DIFERENTE:
                case OP_LOG_E:
                case OP_LOG_OU:
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            pilhaTokens.push(topo);
                            break;
                        } else {
                            if (topo.getPrecedencia() < token.getPrecedencia()) {
                                pilhaTokens.push(topo);
                                break;
                            } else {
                                saida.add(topo);
                            }
                        }
                    }
                    pilhaTokens.push(token);
                    
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFABETICO);
                    funcoesEsperadas.add(FuncaoToken._INDEF_ALFANUMERICO);
                    funcoesEsperadas.add(FuncaoToken.CONST_CARACTER);
                    funcoesEsperadas.add(FuncaoToken.CONST_INTEIRA);
                    funcoesEsperadas.add(FuncaoToken.CONST_REAL);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_POT);
                    funcoesEsperadas.add(FuncaoToken.LIB_MATH_RAIZ);
                    funcoesEsperadas.add(FuncaoToken.OP_SIG_NEGATIVO);
                    funcoesEsperadas.add(FuncaoToken.OP_SIG_POSITIVO);
                    funcoesEsperadas.add(FuncaoToken.OP_LOG_NAO);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    topo = null;
                    while (!pilhaTokens.isEmpty()) {
                        topo = pilhaTokens.pop();
                        if (topo.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                            break;
                        } else {
                            saida.add(topo);
                            topo = null;
                        }
                    }
                    
                    if (topo == null) {
                        erros.add(new Erro(TipoErro.ERRO, token, 
                        "Parêntese de abertura não encontrado"));
                        expressaoValida = false;
                    } else {
                        saida.add(token);
                    }
                    balancParenteses --;
                    break;
            }
        }
        
        while (!pilhaTokens.isEmpty()) {
            token = pilhaTokens.pop();
            if (token.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE) {
                erros.add(new Erro(TipoErro.ERRO, token, 
                "Parêntese de fechamento não encontrado"));
                expressaoValida = false;
            } else {
                saida.add(token);
            }
        }
        
        Expressao expressao = null;
        LinkedList<Expressao> pilhaExpressoes = new LinkedList<>();
        
        for (Token t : saida) {
            switch (t.getFuncaoToken()){
                case CONST_CARACTER:
                case CONST_INTEIRA:
                case CONST_REAL:
                case IDENT_NOME_VARIAVEL:
                    Operando operando = new Operando();
                    operando.setOperando(t);
                    pilhaExpressoes.push(operando);
                    break;
                
                case LIB_MATH_POT:
                case LIB_MATH_RAIZ:
                    ChamadaFuncao chamadaFuncao = mapaFuncoes.get(t);
                    pilhaExpressoes.push(chamadaFuncao);
                    break;
                    
                case OP_SIG_NEGATIVO:
                case OP_SIG_POSITIVO:
                case OP_LOG_NAO:
                    OperacaoUnaria operacaoUnaria = new OperacaoUnaria();
                    operacaoUnaria.setOperador(t);
                    try {
                        operacaoUnaria.setExpressao(pilhaExpressoes.pop());
                    } catch (NoSuchElementException ex){
                        erros.add(new Erro(TipoErro.ERRO, t, 
                            "Erro ao montar expressão - operando não encontrado"));
                        logger.log(Level.WARNING, "{0}:\n {1} - {2}",
                                new Object[]{
                                    "Pilha sem elemento p/ gerar operação unária",
                                    ex.getClass().getName(),
                                    ex.getMessage()
                                });
                        expressaoValida = false;
                        operacaoUnaria.addToken(t);
                    }
                    pilhaExpressoes.push(operacaoUnaria);
                    break;
                    
                case OP_MAT_SOMA:
                case OP_MAT_SUBTRACAO:
                case OP_MAT_MULTIPLICACAO:
                case OP_MAT_DIV_REAL:
                case OP_MAT_DIV_INTEIRA:
                case OP_MAT_MOD:
                case OP_REL_MAIOR:
                case OP_REL_MAIOR_IGUAL:
                case OP_REL_MENOR:
                case OP_REL_MENOR_IGUAL:
                case OP_REL_IGUAL:
                case OP_REL_DIFERENTE:
                case OP_LOG_E:
                case OP_LOG_OU:
                    Operacao subExpressao = new Operacao();
                    subExpressao.setOperador(t);
                    int devCount = 0;
                    try {
                        subExpressao.setExpressaoDir(pilhaExpressoes.pop());
                        devCount ++;
                        subExpressao.setExpressaoEsq(pilhaExpressoes.pop());
                        devCount ++;
                    } catch (NoSuchElementException ex){
                        erros.add(new Erro(TipoErro.ERRO, t, 
                            "Erro ao montar expressão - operando não encontrado"));
                        logger.log(Level.WARNING, "{0}{1}{2}:\n{1} - {2}",
                                new Object[]{
                                    "Pilha sem elemento ",
                                    devCount + 1,
                                    "  p/ gerar operação binária",
                                    ex.getClass().getName(),
                                    ex.getMessage()
                                });
                        expressaoValida = false;
                        subExpressao.addToken(t);
                    }
                    pilhaExpressoes.push(subExpressao);
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                    TokenDelimitador tokenDelimitador = new TokenDelimitador();
                    tokenDelimitador.setDelimitador(t);
                    pilhaExpressoes.push(tokenDelimitador);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    try {
                        Expressao parenteseada = pilhaExpressoes.pop();
                        try {
                            TokenDelimitador abrePar = (TokenDelimitador)pilhaExpressoes.pop();
                            parenteseada.setParentesesAbre(abrePar.getDelimitador());
                            parenteseada.setParentesesFecha(t);
                        } catch (NoSuchElementException | ClassCastException ex){
                            erros.add(new Erro(TipoErro.ALERTA, t,
                                "Alerta ao montar expressão - não pôde encontrar parênteses"));
                            logger.log(Level.WARNING, "{0}:\n{1} - {2}",
                                new Object[]{
                                    "Pilha sem elemento Abre Parênteses",
                                    ex.getClass().getName(),
                                    ex.getMessage()
                                });
                        } 
                        pilhaExpressoes.push(parenteseada);
                    } catch (NoSuchElementException ex){
                        erros.add(new Erro(TipoErro.ERRO, t, 
                            "Erro ao montar expressão - operando não encontrado"));
                        logger.log(Level.WARNING, "{0}:\n{1} - {2}",
                                new Object[]{
                                    "Pilha sem expressão a colocar parênteses",
                                    ex.getClass().getName(),
                                    ex.getMessage()
                                });
                    }
                    break;
            }
        }

        try {
            expressao = pilhaExpressoes.pop();
        } catch (NoSuchElementException ex){
            erros.add(new Erro(TipoErro.ERRO, tokens.get(pos-1), 
                "Erro ao finalizar expressão - última operação inválida ou nula"));
            logger.log(Level.WARNING, "{0}:\n{1} - {2}",
                    new Object[]{
                        "Pilha vazia ao finalizar",
                        ex.getClass().getName(),
                        ex.getMessage()
                    });
            expressaoValida = false;
            expressao = new Operando();
        }
        
        if (!expressaoValida) {
            expressao.invalidaInstrucao();
        }
        
        funcoesEsperadas.clear();
        for (FuncaoToken condicaoParada : condicoesParada) {
            funcoesEsperadas.add(condicaoParada);
        }
        
        return expressao;
    }
    
    /**
     * Analisa uma expressão de chamada de função.
     * <br>Sintaxe válida:
     * <p><code>&lt;Nome Função&gt; ( &lt;Expressão1&gt; [ , &lt;Expressão2&gt; , ... ] ) 
     * </code></p>
     * @return Chamada de função
     */
    private ChamadaFuncao expressaoChamadaFuncao(){
        ChamadaFuncao chamadaFuncao = new ChamadaFuncao();
        
        boolean go = true;
        while (go && existeProxima()){
            Token token = tokens.get(pos++);
            if (!funcaoValida(token)){
                chamadaFuncao.invalidaInstrucao();
                break;
            }
            
            switch (token.getFuncaoToken()){
                case LIB_MATH_POT:
                case LIB_MATH_RAIZ:
                //case IDENT_NOME_FUNCAO:
                    chamadaFuncao.setTokenNome(token);
                    funcoesEsperadas.clear();
                    funcoesEsperadas.add(FuncaoToken.DELIM_PARENTESES_ABRE);
                    break;
                    
                case DELIM_PARENTESES_ABRE:
                case DELIM_VIRGULA:
                    chamadaFuncao.addToken(token);
                    funcoesEsperadas.clear();
                    chamadaFuncao.addParametro(instrucaoExpressao(
                            FuncaoToken.DELIM_VIRGULA, FuncaoToken.DELIM_PARENTESES_FECHA));
                    break;
                            
                case DELIM_PARENTESES_FECHA:
                    chamadaFuncao.addToken(token);
                    go = false;
                    funcoesEsperadas.clear();
                    break;
            }
        }
        
        return chamadaFuncao;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funções esperadas no início de um algoritmo">
    private static LinkedList<FuncaoToken> funcoesAlgoritmo(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();

        // Espera por declaração de função
        /*funcoes.add(FuncaoToken.RES_MOD_FUNCAO);*/
        // Espera por declaração de rotina
        /*funcoes.add(FuncaoToken.RES_MOD_ROTINA);*/
        // Espera por declaração de nome de algoritmo
        funcoes.add(FuncaoToken.RES_ALGORITMO);
        
        // Espera por início de bloco caso não modularizado
        funcoes.add(FuncaoToken.RES_BLOCO_INICIO);
        return funcoes;
    }
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="Funções esperadas após início de bloco">
    private static LinkedList<FuncaoToken> funcoesBloco(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        //Bloco interno (espero que não seja usado...)
        funcoes.add(FuncaoToken.RES_BLOCO_INICIO);
        //Declaração de variáveis
        funcoes.add(FuncaoToken.RES_TIPO_INTEIRO);
        funcoes.add(FuncaoToken.RES_TIPO_REAL);
        funcoes.add(FuncaoToken.RES_TIPO_CARACTER);
        //Entrada/Saída de dados
        funcoes.add(FuncaoToken.LIB_IO_LEIA);
        funcoes.add(FuncaoToken.LIB_IO_ESCREVA);
        //Atribuição ou Chamada de rotina
        funcoes.add(FuncaoToken._INDEF_ALFABETICO);
        funcoes.add(FuncaoToken._INDEF_ALFANUMERICO);
        //Condicional
        funcoes.add(FuncaoToken.RES_COND_SE);
        //Repetições
        funcoes.add(FuncaoToken.RES_REP_PARA);
        funcoes.add(FuncaoToken.RES_REP_ENQUANTO);
        funcoes.add(FuncaoToken.RES_REP_REPITA);
        funcoes.add(FuncaoToken.RES_REP_FACA);
        //Fechamento do bloco
        funcoes.add(FuncaoToken.RES_BLOCO_FIM);
        return funcoes;
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Funções esperadas dentro de uma instrução condicional ou repetitiva">
    private static LinkedList<FuncaoToken> funcoesEstrutura(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        //Bloco de instruções
        funcoes.add(FuncaoToken.RES_BLOCO_INICIO);

        //Instruções únicas
        //Entrada/Saída de dados
        funcoes.add(FuncaoToken.LIB_IO_LEIA);
        funcoes.add(FuncaoToken.LIB_IO_ESCREVA);
        //Atribuição ou Chamada de rotina
        funcoes.add(FuncaoToken._INDEF_ALFABETICO);
        funcoes.add(FuncaoToken._INDEF_ALFANUMERICO);
        //Condicional encadeada
        funcoes.add(FuncaoToken.RES_COND_SE);
        //Repetições
        funcoes.add(FuncaoToken.RES_REP_PARA);
        funcoes.add(FuncaoToken.RES_REP_ENQUANTO);
        funcoes.add(FuncaoToken.RES_REP_FACA);
        return funcoes;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Operadores aritméticos">
    private static LinkedList<FuncaoToken> funcoesExpressaoAritmetica(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        funcoes.add(FuncaoToken.OP_MAT_SOMA);
        funcoes.add(FuncaoToken.OP_MAT_SUBTRACAO);
        funcoes.add(FuncaoToken.OP_MAT_MULTIPLICACAO);
        funcoes.add(FuncaoToken.OP_MAT_DIV_REAL);
        funcoes.add(FuncaoToken.OP_MAT_DIV_INTEIRA);
        funcoes.add(FuncaoToken.OP_MAT_MOD);
        return funcoes;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Operadores relacionais">
    private static LinkedList<FuncaoToken> funcoesExpressaoRelacional(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        funcoes.add(FuncaoToken.OP_REL_MAIOR);
        funcoes.add(FuncaoToken.OP_REL_MAIOR_IGUAL);
        funcoes.add(FuncaoToken.OP_REL_MENOR);
        funcoes.add(FuncaoToken.OP_REL_MENOR_IGUAL);
        funcoes.add(FuncaoToken.OP_REL_IGUAL);
        funcoes.add(FuncaoToken.OP_REL_DIFERENTE);
        return funcoes;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Operadores lógicos">
    private static LinkedList<FuncaoToken> funcoesExpressaoLogica(){
        LinkedList<FuncaoToken> funcoes = new LinkedList<>();
        funcoes.add(FuncaoToken.OP_LOG_E);
        funcoes.add(FuncaoToken.OP_LOG_OU);
        return funcoes;
    }
    // </editor-fold>
}
