/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog;

import alog.control.Parser;
import alog.control.Scanner;
import alog.instrucao.Instrucao;
import alog.token.Token;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Caique
 */
public class TB {

    public final static boolean IMPRIME_TOKENS = false;
    public final static boolean IMPRIME_INSTRUCOES = false;
    public final static boolean IMPRIME_EXPRESSOES_ARVORE = false;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String path = String.format("%s%s%s%s%s",
                System.getProperty("user.dir"),
                System.getProperty("file.separator"),
                "RevisaoParser",
                System.getProperty("file.separator"),
                "RunFE_2_3.txt"
                //"RunParser01.txt"
        );
        FileReader fr = new FileReader(path);
        
        StringBuilder sb = new StringBuilder();
        int c = fr.read();
        while (c != -1) {
            sb.append((char)c);
            c = fr.read();
        }
        
        String codigo = sb.toString();
        
        int lastErros = 0;
        int thisErros = 0;
        
        System.out.println("Iniciando Scanner");
        
        Scanner scanner = new Scanner(codigo);
        List<Token> tokens = new ArrayList<>();
        while (scanner.existeProximo()){
            Token token = scanner.proximo();
            if (IMPRIME_TOKENS) {
                System.out.println("*** " + token);
            }
            tokens.add(token);
            thisErros = scanner.getNumErros();
            if (thisErros > lastErros){
                for (int e = lastErros; e < thisErros; e++){
                    System.out.println(scanner.getErros().get(e).toString());
                }
                lastErros = thisErros;
            }
        }
        
        thisErros = scanner.getNumErros();
        if (thisErros == 0){
            System.out.println("Scanner sem erros de leitura");
        } else {
            if (thisErros > lastErros){
                for (int e = lastErros; e < thisErros; e++){
                    System.out.println(scanner.getErros().get(e).toString());
                }
            }
        }
        
        thisErros = 0;
        lastErros = 0;
        
        System.out.println("Iniciando Parser");
        
        Parser parser = new Parser(tokens);
        List<Instrucao> instrucoes = new LinkedList<>();
        while (parser.existeProxima()){
            Instrucao instrucao = parser.proxima();
            instrucoes.add(instrucao);
            if (IMPRIME_INSTRUCOES) {
                System.out.println("*** " + instrucao.toString());
            }
            thisErros = parser.getNumErros();
            if (thisErros > lastErros){
                for (int e = lastErros; e < thisErros; e++){
                    System.out.println(parser.getErros().get(e).toString());
                }
                lastErros = thisErros;
            }
        }
        
        if (thisErros == 0){
            System.out.println("Parser sem erros de leitura");
        }
        
        thisErros = parser.getNumErros();
        if (thisErros == 0){
            System.out.println("Parser sem erros de leitura");
        } else {
            if (thisErros > lastErros){
                for (int e = lastErros; e < thisErros; e++){
                    System.out.println(parser.getErros().get(e).toString());
                }
            }
        }
    }
}
