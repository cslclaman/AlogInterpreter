/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog;

import alog.control.Parser;
import alog.control.PreProcessor;
import alog.control.Scanner;
import alog.instrucao.Instrucao;
import alog.token.Token;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Caique Souza
 */
public class TC {

    public final static boolean IMPRIME_ERROS_SCANNER = false;
    public final static boolean IMPRIME_ERROS_PARSER = true;
    public final static boolean IMPRIME_ERROS_PREPROCESSOR = false;
    public final static boolean IMPRIME_TEMPO_PARCIAL = true;
    public final static String CHARACTER_ENCODING = "UTF-8";
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        long media = 0;
        
        String path = String.format("%s%s%s%s",
                System.getProperty("user.dir"),
                System.getProperty("file.separator"),
                "exemplos_algoritmos",
                System.getProperty("file.separator")
        );
        File pasta = new File(path);
        int pos = 0;
        
        int errScanner = 0;
        int errParser = 0;
        int errPreProc = 0;
        
        for (File arquivo : pasta.listFiles()){
            
            long tempo = System.currentTimeMillis();
            
            System.out.printf("\nArquivo %d: %s\n", ++pos, arquivo.getName());
            
            StringBuilder codigofonte = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), CHARACTER_ENCODING));
            String texto;
            while ((texto = br.readLine()) != null){
                codigofonte.append(texto);
                codigofonte.append("\n");
            }
            String codigo = codigofonte.toString();
            
            Scanner scanner = new Scanner(codigo);
            List<Token> tokens = new ArrayList<>();
            while (scanner.existeProximo()){
                tokens.add(scanner.proximo());
            }

            if (scanner.getNumErros() == 0){
                System.out.println("\tScanner sem erros de leitura");
            } else {
                System.out.printf("\t%d erros encontrados no Scanner\n", scanner.getNumErros());
                errScanner ++;
                if (IMPRIME_ERROS_SCANNER)
                    System.out.println(scanner.printErros());
            }

            Parser parser = new Parser(tokens);
            List<Instrucao> instrucoes = new LinkedList<>();
            while (parser.existeProxima()){
                instrucoes.add(parser.proxima());
            }

            if (parser.getNumErros() == 0){
                System.out.println("\tParser sem erros de leitura");
            } else {
                System.out.printf("\t%d erros encontrados no Parser\n", parser.getNumErros());
                errParser ++;
                if (IMPRIME_ERROS_PARSER)
                    System.out.println(parser.printErros());
            }
            
            PreProcessor preProcessor = new PreProcessor(instrucoes);
            preProcessor.verificaPrograma();
            
            if (preProcessor.getNumErros() == 0){
                System.out.println("\tPré processamento sem erros encontrados");
            } else {
                System.out.printf("\t%d erros encontrados no pré-processamento\n", preProcessor.getNumErros());
                errPreProc ++;
                if (IMPRIME_ERROS_PREPROCESSOR)
                    System.out.println(preProcessor.printErros());
            }
            
            tempo = System.currentTimeMillis() - tempo;
            media += tempo;
            if (IMPRIME_TEMPO_PARCIAL) {
                System.out.println("\tTempo decorrido: " + tempo + "ms");
            }
            
        }
        
        System.out.println("\nTotal de arquivos processados:     " + pos);
        System.out.println("Arquivos com erro no Scanner:      " + errScanner);
        System.out.println("Arquivos com erro no Parser:       " + errParser);
        System.out.println("Arquivos com erro no PreProcessor: " + errPreProc);
        System.out.println("Tempo total decorrido:             " + media + "ms");
        System.out.println("Média de tempo por algoritmo:      " + media /pos + "ms");
    }
    
}
