/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog;

import alog.control.Parser;
import alog.control.Scanner;
import alog.instrucao.Atribuicao;
import alog.instrucao.ChamadaRotina;
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

    public final static boolean IMPRIME_TOKENS = true;
    public final static boolean IMPRIME_INSTRUCOES = true;
    public final static boolean IMPRIME_EXPRESSOES_ARVORE = false;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        String path = String.format("%s%s%s%s%s",
                System.getProperty("user.dir"),
                System.getProperty("file.separator"),
                "RevisaoParser",
                System.getProperty("file.separator"),
                "RunParser01.txt"
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
            tokens.add(scanner.proximo());
            thisErros = scanner.getNumErros();
            if (thisErros > lastErros){
                for (int e = lastErros; e < thisErros; e++){
                    System.out.println(scanner.getListaErros().get(e));
                }
                lastErros = thisErros;
            }
        }
        
        if (scanner.getNumErros() == 0){
            System.out.println("Scanner sem erros de leitura");
        } else {
            System.out.println(scanner.imprimeErros());
        }
        
        thisErros = 0;
        lastErros = 0;
        
        if (IMPRIME_TOKENS) {
            for (Token t : tokens){
                System.out.println("*** " + t);
            }
        }
        
        System.out.println("Iniciando Parser");
        
        Parser parser = new Parser(tokens);
        List<Instrucao> instrucoes = new LinkedList<>();
        while (parser.existeProxima()){
            instrucoes.add(parser.proxima());
            thisErros = parser.getNumErros();
            if (thisErros > lastErros){
                for (int e = lastErros; e < thisErros; e++){
                    System.out.println(parser.getListaErros().get(e));
                }
                lastErros = thisErros;
            }
        }
        
        if (thisErros == 0){
            System.out.println("Parser sem erros de leitura");
        }
        
        if (IMPRIME_INSTRUCOES) {
            for (Instrucao i : instrucoes){
                System.out.println("*** " + i.toString());
            }
        }
    }
    
}
