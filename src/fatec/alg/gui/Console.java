/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduacao do curso de Analise e Desenvolvimento de Sistemas
 * Autor: Caique de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */
package fatec.alg.gui;

import fatec.alg.analise.AnalisadorLexico;
import fatec.alg.analise.AnalisadorSintatico;
import fatec.alg.analise.PreProcessador;
import fatec.alg.execucao.InterfaceExecucao;
import fatec.alg.execucao.Interpreter;
import fatec.alg.execucao.config.ConfigInterpreter;
import fatec.alg.geral.instrucao.Instrucao;
import fatec.alg.geral.log.Erro;
import fatec.alg.geral.log.TipoErro;
import fatec.alg.geral.token.Token;
import fatec.alg.geral.variavel.Variavel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe de execução de um algoritmo via console.
 * <br>Toda a entrada e impressão de dados é feita via System.in/System.out.
 * Nesse contexto, não há depuração e nem exibição dos passos, apenas execução.
 * Impressão de logs só será feita caso seja passado o argumento -debuglog.
 * 
 * @author alani
 */
public class Console implements InterfaceExecucao {

    private String entradaDados;
    private Interpreter interpreter = null;
    private Scanner scanner;

    public Console() {
        analiseAlgoritmo("");
    }

    public Console(String filename) {
        analiseAlgoritmo(filename);
    }
    
    private void analiseAlgoritmo(String filename) {
        scanner = new Scanner(System.in);
        
        if (filename == null || filename.isEmpty()) {
            System.out.println("Informe o nome do arquivo a executar\n"
                    + "(Você também pode reiniciar o interpretador e passar o argumento -filename=\"NOME DO ARQUIVO\")");
            filename = scanner.nextLine().replace("\"", "").replace("'", "");
        }
        
        try {
            String codigoFonte;
            File arquivo = new File(filename);
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"));
            String texto;
            while ((texto = br.readLine()) != null){
                for (char ch : texto.toCharArray()){
                    if (ch == '\t'){
                        sb.append("    ");
                    } else {
                        sb.append(ch);
                    }
                }
                sb.append("\n");
            }
            codigoFonte = sb.toString();
            br.close();
            
            System.out.println("\nAnálise do arquivo \"" + arquivo.getName() + "\"");
            
            AnalisadorLexico scanner = new AnalisadorLexico(codigoFonte);
            List<Token> tokens = scanner.listaTokens();

            int numErros = scanner.getNumErros(TipoErro.getMax());
            if (numErros > 0){
                System.out.println(numErros + " erros encontrados - verifique seu algoritmo\n");
                System.out.println("SCANNER (Análise Léxica)");

                for (Erro e : scanner.getErros(TipoErro.getMax())){
                    System.out.println(e.toString());
                }
            } else {
                AnalisadorSintatico parser = new AnalisadorSintatico(tokens);
                LinkedList<Instrucao> instrucoes = parser.listaInstrucoes();

                numErros = parser.getNumErros(TipoErro.getMax());
                if (numErros > 0){
                    System.out.println(numErros + " erros encontrados - verifique seu algoritmo\n");
                    System.out.println("PARSER (Análise Sintática)");

                    for (Erro e : parser.getErros(TipoErro.getMax())){
                        System.out.println(e.toString());
                    }
                } else {

                    PreProcessador processor = new PreProcessador(instrucoes);
                    processor.verificaPrograma();

                    numErros = processor.getNumErros(TipoErro.ERRO);
                    if (numErros > 0){
                        System.out.println(numErros + " erros encontrados - verifique seu algoritmo\n");
                        System.out.println("PRE PROCESSOR (Análise Semântica)");

                        for (Erro e : parser.getErros(TipoErro.getMax())){
                            System.out.println(e.toString());
                        }
                    } else {
                        numErros = processor.getNumErros(TipoErro.getMax());
                        if (numErros > 0) {
                            System.out.println(numErros + " alertas detectados");
                        } else {
                            System.out.println("Nenhum erro encontrado");
                        }

                        ConfigInterpreter configInterpr = new ConfigInterpreter();
                        configInterpr.set(ConfigInterpreter.RUNNEXT_LEIA_ATRIB, true);
                        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_EXEC_CONST, true);
                        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_CONST, true);
                        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_EXEC_VAR, true);
                        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_VAR, true);
                        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_UNARIA, true);
                        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_OPBIN, true);
                        configInterpr.set(ConfigInterpreter.FORMAT_ESCREVA_ESPACO, false);

                        interpreter = new Interpreter(this, processor.getPrograma());
                        interpreter.setConfigInterpreter(configInterpr);
                        
                        System.out.println("EXECUÇÃO INICIADA\n");
                    }
                }
            }
        } catch (IOException ex){
            System.err.println("Não foi possível carregar o arquivo especificado: " + ex.toString()); 
        }
    }
    
    public boolean canExecute() {
        return interpreter != null && interpreter.existeProxima();
    }
    
    private void proxima() {
        interpreter.proxima();
    }
    
    public void executa() {
        while (canExecute()) {
            proxima();
        }
    }
    
    @Override
    public void entradaDados(Variavel variavel) {
        System.out.printf("Informe um valor tipo %s para a variável %s:\n", 
                variavel.getTipo().toString(), variavel.getNome());
        
        entradaDados = scanner.nextLine();
        interpreter.proxima();
    }

    @Override
    public String entradaDadosRetorno() {
        return entradaDados;
    }

    @Override
    public void erroEntradaDados(Variavel variavel, Erro erro) {
        System.out.println(erro.toString());
    }

    @Override
    public void saidaDados(String saida) {
        System.out.print(saida);
    }

    @Override
    public void erroFatal(Erro erro) {
        System.out.println(erro.toString());
    }

    @Override
    public void finalizado() {
        System.out.println("\nEXECUÇÃO FINALIZADA");
    }
    
}
