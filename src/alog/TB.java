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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.sound.midi.SysexMessage;

/**
 *
 * @author Caique
 */
public class TB {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String path = String.format("%s%s%s",
                System.getProperty("user.dir"),
                System.getProperty("file.separator"),
                "test_parser01.txt"
        );
        FileReader fr = new FileReader(path);
        
        StringBuilder sb = new StringBuilder();
        int c = fr.read();
        while (c != -1) {
            sb.append((char)c);
            c = fr.read();
        }
        
        String codigo = sb.toString();
        
        Scanner scanner = new Scanner(codigo);
        List<Token> tokens = scanner.listaTokens();
        if (scanner.getNumErros() > 0){
            System.out.println(scanner.imprimeErros());
        } else {
            System.out.println("Scanner sem erros de leitura");
        }
        
        for (Token t : tokens){
            System.out.println("*** " + t);
        }
        
        Parser parser = new Parser(tokens);
        List<Instrucao> instrucoes = parser.listaExpressoes();
        
        if (parser.getNumErros() > 0){
            System.out.println(parser.imprimeErros());
        } else {
            System.out.println("Parser sem erros de leitura");
        }
        for (Instrucao i : instrucoes){
            System.out.println("*** " + i);
        }
        
    }
    
}
