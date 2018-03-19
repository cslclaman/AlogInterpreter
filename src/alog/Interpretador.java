/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog;

import alog.control.Interpreter;
import alog.control.Parser;
import alog.model.Expressao;
import alog.model.TipoExpressao;
import alog.view.FrmGui;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Caique
 */
public class Interpretador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args[0].equals("-console")){
            try {
                StringBuffer codigofonte = new StringBuffer();
                BufferedReader br = new BufferedReader(new FileReader(args[1]));
                
                String texto;
                while ((texto = br.readLine()) != null){
                    codigofonte.append(texto);
                }
                
                Parser parser = new Parser(codigofonte.toString());
                LinkedList<Expressao> expressoes = new LinkedList<>();
                while (parser.hasNext()){
                    Expressao e = parser.parseExpression();
                    if (!parser.getErro().isEmpty()){
                        System.err.println(parser.getErro());
                    }
                    expressoes.add(e);
                }
                
                Interpreter interpreter = new Interpreter();
                for (Expressao expr : expressoes){
                    if (!interpreter.executa(expr)){
                        System.err.println("Expressão \"" + expr.getTexto() + "\" não executada - linha " + expr.getLinha());
                        System.exit(2);
                    }
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
