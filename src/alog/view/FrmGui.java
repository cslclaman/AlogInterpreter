/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.view;

import alog.control.Parser;
import alog.control.Scanner;
import alog.model.Expressao;
import alog.model.TipoExpressao;
import alog.model.TipoToken;
import alog.model.TipoVariavel;
import alog.model.Token;
import alog.model.Variavel;
import java.util.LinkedList;
import javax.swing.text.DefaultStyledDocument;
import alog.view.append.TextLineNumber;
import java.awt.Color;
import java.util.Collections;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Caique
 */
public class FrmGui extends javax.swing.JFrame {
    
    StyleContext sc;
    private final Style stylePlain;
    private final Style stylePerc;
    
    private final Style styleLit;
    private final Style styleABC123;
    private final Style style123;
    private final Style styleABC;
    private final Style styleDel;
    private final Style styleOp;
    private final Style styleOMG;
    
    /**
     * Creates new form FrmTeste
     */
    public FrmGui() {
        oldText = "";
        
        sc = new StyleContext();
        
        stylePlain = sc.addStyle("plainBasicao", null);
        stylePlain.addAttribute(StyleConstants.Foreground, Color.BLACK);
        stylePlain.addAttribute(StyleConstants.Background, Color.WHITE);
        
        stylePerc = sc.addStyle("percurso", null);
        stylePerc.addAttribute(StyleConstants.Background, Color.YELLOW);
        
        styleLit = sc.addStyle("literal", null);
        styleLit.addAttribute(StyleConstants.Foreground, Color.LIGHT_GRAY);
        
        styleABC123 = sc.addStyle("alfanum", null);
        styleABC123.addAttribute(StyleConstants.Foreground, Color.GREEN);
        
        style123 = sc.addStyle("num", null);
        style123.addAttribute(StyleConstants.Foreground, Color.ORANGE);
        
        styleABC = sc.addStyle("alfa", null);
        styleABC.addAttribute(StyleConstants.Foreground, Color.BLUE);
        
        styleDel = sc.addStyle("del", null);
        styleDel.addAttribute(StyleConstants.Foreground, Color.RED);
        
        styleOp = sc.addStyle("oper", null);
        styleOp.addAttribute(StyleConstants.Foreground, Color.DARK_GRAY);
        
        styleOMG = sc.addStyle("none", null);
        styleOMG.addAttribute(StyleConstants.Foreground, Color.BLACK);
        
        initComponents();
        txpIde.setDocument(new DefaultStyledDocument());
        doc = (DefaultStyledDocument)txpIde.getDocument();
        formated = false;
        
        tabVariaveis = (DefaultTableModel)jTable1.getModel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        txpIde = new javax.swing.JTextPane();
        btnScanner = new javax.swing.JButton();
        btnInicioPerc = new javax.swing.JButton();
        btnProxPerc = new javax.swing.JButton();
        btnFormat = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane4 = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPane5 = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnScanner1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane2.setVerifyInputWhenFocusTarget(false);

        txpIde.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        txpIde.setText("Início\n    Caracter: Nome;\n    Inteiro: Idade;\n    Real: Altura, CentPorDia <- 0;\n    \n    Leia(Nome, Idade, Altura);\n    \n    Idade <- Idade * 365.25;\n    CentPorDia <- Altura / Idade;\n    \n    Escreva(Nome,\" tem \",CentPorDia,\" centímetros por dia de vida\");\nFim");
        jScrollPane2.setViewportView(txpIde);

        jScrollPane2.setRowHeaderView(new TextLineNumber(txpIde, 2));

        btnScanner.setText("Scanner");
        btnScanner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScannerActionPerformed(evt);
            }
        });

        btnInicioPerc.setText("|<");
        btnInicioPerc.setEnabled(false);
        btnInicioPerc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioPercActionPerformed(evt);
            }
        });

        btnProxPerc.setText(">>");
        btnProxPerc.setEnabled(false);
        btnProxPerc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProxPercActionPerformed(evt);
            }
        });

        btnFormat.setText("Format");
        btnFormat.setEnabled(false);
        btnFormat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFormatActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo", "Nome", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jScrollPane3.setViewportView(jTextPane1);

        jLabel1.setText("Entrada");

        jScrollPane6.setViewportView(jTextPane4);

        jScrollPane7.setViewportView(jTextPane5);

        jLabel2.setText("Processamento");

        jLabel3.setText("Saída");

        jButton1.setText("Confirmar");

        btnScanner1.setText("Parser");
        btnScanner1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScanner1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnScanner)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnScanner1)
                                .addGap(80, 80, 80)
                                .addComponent(btnInicioPerc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProxPerc)
                                .addGap(114, 114, 114)
                                .addComponent(btnFormat)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnScanner)
                    .addComponent(btnInicioPerc)
                    .addComponent(btnProxPerc)
                    .addComponent(btnFormat)
                    .addComponent(btnScanner1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnScannerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScannerActionPerformed
        scn = new Scanner(txpIde.getText());
        tokens = new LinkedList<>();
        while (scn.hasNext()){
            tokens.add(scn.getNext());
        }
        exprIndex = 0;

        btnInicioPerc.setEnabled(true);
        btnProxPerc.setEnabled(true);
        btnFormat.setEnabled(true);
        
        System.out.println("POS\tLINHA\tCOLUNA\tTIPO\tPALAVRA");
        for (Token w : tokens){
            System.out.println((w.getOrdem()+1) + "\t" + (w.getLinha()+1) + "\t" + (w.getColuna()+1) + "\t" + w.getTipoToken().toString().substring(0, 2) + "\t" + w.getPalavra());
        }
    }//GEN-LAST:event_btnScannerActionPerformed
    
    private void btnInicioPercActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioPercActionPerformed
        exprIndex = 1;
        btnProxPerc.setEnabled(true);
        btnInicioPerc.setEnabled(false);
        
        if (tokenAnt != null){
            if (formated){
                doc.setCharacterAttributes(tokenAnt.getPosicao(), tokenAnt.getTamanho(), tokenStyle(tokenAnt.getTipoToken()), true);
            } else {
                doc.setCharacterAttributes(tokenAnt.getPosicao(), tokenAnt.getTamanho(), stylePlain, true);
            }
            tokenAnt = null;
        }
        
    }//GEN-LAST:event_btnInicioPercActionPerformed

    private int bloco = 0;
    private Token tokenAnt = null;
    
    private void btnProxPercActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProxPercActionPerformed
        btnInicioPerc.setEnabled(true);
        
        if (!expressao.hasNext()){
            expressao = expressoes.get(exprIndex++);
        }
        
        if (exprIndex >= expressoes.size()){
            btnProxPerc.setEnabled(false);
        }
        
        Token token = expressao.getNext();
        if (tokenAnt != null){
            if (formated){
                doc.setCharacterAttributes(tokenAnt.getPosicao(), tokenAnt.getTamanho(), tokenStyle(tokenAnt.getTipoToken()), true);
            } else {
                doc.setCharacterAttributes(tokenAnt.getPosicao(), tokenAnt.getTamanho(), stylePlain, true);
            }
        }
        doc.setCharacterAttributes(token.getPosicao(), token.getTamanho(), stylePerc, true);

        switch(expressao.getTipo()){
            case DELIM_BLOCO:
                switch (token.getFuncaoToken()){
                    case RES_BLOCO_INICIO:
                        bloco ++;
                        break;
                    case RES_BLOCO_FIM:
                        bloco --;
                        break;
                }
                break;
            case CRIACAO_VARIAVEL:
                TipoVariavel tipoVar;
                switch (expressao.getTokenAt(0).getFuncaoToken()){
                    case IDENT_TIPO_INTEIRO:
                        tipoVar = TipoVariavel.INTEIRO;
                        break;
                    case IDENT_TIPO_REAL:
                        tipoVar = TipoVariavel.REAL;
                        break;
                    case IDENT_TIPO_CARACTER:
                    default:
                        tipoVar = TipoVariavel.CARACTER;
                        break;
                }
                Variavel variavel = new Variavel(tipoVar, token.getPalavra());
                variaveis.add(variavel);

                Collections.sort(variaveis, (v1, v2) -> {
                    int comp = v1.getTipo().toString().compareTo(v2.getTipo().toString());
                    if (comp == 0){
                        return v1.getNome().compareToIgnoreCase(v2.getNome());
                    } else {
                        return comp;
                    }
                });

                tabVariaveis.setRowCount(0);
                for (Variavel v : variaveis){
                    tabVariaveis.addRow(new String[]{v.getTipo().toString(),v.getNome(),v.getValor()});
                }
                break;
        }
        
        tokenAnt = token;
    }//GEN-LAST:event_btnProxPercActionPerformed

    private void btnFormatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFormatActionPerformed
        if (!formated){
            for (Token t : tokens){
                doc.setCharacterAttributes(t.getPosicao(), t.getTamanho(), tokenStyle(t.getTipoToken()), true);
            }
        }else {
            doc.setCharacterAttributes(doc.getStartPosition().getOffset(), doc.getLength(), stylePlain, true);
        }
        formated = !formated;
    }//GEN-LAST:event_btnFormatActionPerformed

    private void btnScanner1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScanner1ActionPerformed
        Parser parser = new Parser(txpIde.getText());
        expressoes = new LinkedList<>();
        System.out.println("EXPRESSÕES");
        while (parser.hasNext()){
            Expressao e = parser.parseExpression();
            System.out.println(e.toString());
            if (!parser.getErro().isEmpty()){
                System.err.println(parser.getErro());
            }
            if (e.getTipo() == TipoExpressao.CRIACAO_VARIAVEL){
                e.setIndice(1);
            }
            expressoes.add(e);
        }
        
        variaveis = new LinkedList<>();
        expressao = expressoes.getFirst();
        
        exprIndex = 1;
        btnProxPerc.setEnabled(true);
        btnInicioPerc.setEnabled(false);
    }//GEN-LAST:event_btnScanner1ActionPerformed

    private Style tokenStyle (TipoToken tokenType){
        if (tokenType == null){
            return styleOMG;
        } else {
            switch (tokenType){
                case LITERAL:
                    return styleLit;
                case ALFANUMERICO:
                    return styleABC123;
                case NUMERICO:
                    return style123;
                case ALFABETICO:
                    return styleABC;
                case DELIMITADOR:
                    return styleDel;
                case OPERADOR:
                    return styleOp;
                default:
                    return styleOMG;
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFormat;
    private javax.swing.JButton btnInicioPerc;
    private javax.swing.JButton btnProxPerc;
    private javax.swing.JButton btnScanner;
    private javax.swing.JButton btnScanner1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane4;
    private javax.swing.JTextPane jTextPane5;
    private javax.swing.JTextPane txpIde;
    // End of variables declaration//GEN-END:variables
    private LinkedList<Token> tokens;
    private LinkedList<Expressao> expressoes;
    private LinkedList<Variavel> variaveis;
    private Expressao expressao;
    private int exprIndex;
    private Scanner scn;
    private DefaultStyledDocument doc;
    private boolean formated;
    private String oldText;
    
    private DefaultTableModel tabVariaveis;
}
