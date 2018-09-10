/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog;

import alog.control.Interpreter;
import alog.control.Parser;
import alog.control.Scanner;
import alog.model.Instrucao;
import alog.token.Token;
import alog.view.FrmGui;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Caique
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 1 && args[0].equals("-console")){
            try {
                StringBuilder codigofonte = new StringBuilder();
                InputStreamReader fr = new InputStreamReader(new FileInputStream(args[1]), "UTF-8");
                BufferedReader br = new BufferedReader(fr);
                String texto;
                while ((texto = br.readLine()) != null){
                    codigofonte.append(texto);
                    codigofonte.append("\n");
                }
                
                Scanner scanner = new Scanner(codigofonte.toString());
                List<Token> tokens = scanner.listaTokens();
                
                if (scanner.getNumErros() > 0){
                    System.err.println(scanner.imprimeErros());
                    System.out.println("\n" + scanner.getNumErros() +
                            " erros encontrados na análise léxica.");
                    if (args.length > 2 && args[3].equals("-ignore")){
                        System.out.println("Prosseguindo com análise sintática\n");
                    } else {
                        System.out.println("Verifique os erros, corrija o código e tente novamente.");
                        System.exit(0);
                    }
                } 
                
                Parser parser = new Parser(tokens);
                LinkedList<Instrucao> expressoes = new LinkedList<>();
                while (parser.hasNext()){
                    expressoes.add(parser.parseExpression());
                }
                System.err.println(parser.getStringErros());
                
                if (parser.getNumErros() > 0){
                    System.out.println("\n" + parser.getNumErros() +
                            " erros encontrados na análise sintática.");
                    if (args.length > 2 && args[3].equals("-ignore")){
                        System.out.println("Prosseguindo com execução\n");
                    } else {
                        System.out.println("Verifique os erros, corrija o código e tente novamente.");
                        System.exit(0);
                    }
                } 
                
                boolean execEnd = true;
                int linha = 0;
                
                Interpreter interpreter = new Interpreter();
                for (Instrucao expr : expressoes){
                    linha = expr.getLinha();
                    if (!interpreter.executa(expr)){
                        execEnd = false;
                        break;
                    }
                }
                
                if (execEnd){
                    System.out.println("Execução concluída com sucesso");
                } else {
                    System.out.println("Execução interrompida na linha " + linha);
                }
                
            } catch (IOException ex){
                System.err.println(ex.toString());
                System.exit(1);
            }
            
        } else {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Windows".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(FrmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            java.awt.EventQueue.invokeLater(() -> {
                new FrmGui().setVisible(true);
            });
        }
    }
    
}
