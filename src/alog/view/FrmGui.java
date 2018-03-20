/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.view;

import alog.control.Parser;
import alog.model.Expressao;
import alog.model.FuncaoToken;
import alog.model.TipoVariavel;
import alog.model.Token;
import alog.model.Variavel;
import java.util.LinkedList;
import javax.swing.text.DefaultStyledDocument;
import alog.view.append.TextLineNumber;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Caret;
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
    private final int FORMAT_PLAIN = 0;
    
    private final Style stylePerc;
    private final int FORMAT_PERC = 1;
    
    private final Style styleError;
    private final int FORMAT_ERROR = 2;
    
    private final Color backgroundDisabled = javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground");
    private final Color backgroundEnabled = javax.swing.UIManager.getDefaults().getColor("FormattedTextField.background");
    
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
        
        styleError = sc.addStyle("tokenErrado", null);
        styleError.addAttribute(StyleConstants.Background, Color.RED);
        
        initComponents();
        txpIde.setDocument(new DefaultStyledDocument());
        doc = (DefaultStyledDocument)txpIde.getDocument();
        formatacao = FORMAT_PLAIN;
        
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
        btnInicioPerc = new javax.swing.JButton();
        btnProxPerc = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        txpEntrada = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txpSaida = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        txpProcessamento = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnEntradaConfirma = new javax.swing.JButton();
        btnVerificar = new javax.swing.JButton();
        lblVariavelEntrada = new javax.swing.JLabel();
        btnProcContinuar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane2.setVerifyInputWhenFocusTarget(false);

        txpIde.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        txpIde.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txpIdeKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(txpIde);

        jScrollPane2.setRowHeaderView(new TextLineNumber(txpIde, 2));

        btnInicioPerc.setText("|< Início");
        btnInicioPerc.setEnabled(false);
        btnInicioPerc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInicioPercActionPerformed(evt);
            }
        });

        btnProxPerc.setText(">> Próximo passo");
        btnProxPerc.setEnabled(false);
        btnProxPerc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProxPercActionPerformed(evt);
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
        jTable1.setSelectionBackground(java.awt.Color.green);
        jTable1.setSelectionForeground(java.awt.Color.black);
        jScrollPane1.setViewportView(jTable1);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txpEntrada.setEditable(false);
        txpEntrada.setBackground(javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground"));
        txpEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txpEntradaKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txpEntrada);

        jLabel1.setText("Entrada");

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        txpSaida.setEditable(false);
        txpSaida.setBackground(javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground"));
        jScrollPane6.setViewportView(txpSaida);

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txpProcessamento.setEditable(false);
        txpProcessamento.setBackground(javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground"));
        jScrollPane7.setViewportView(txpProcessamento);

        jLabel2.setText("Processamento");

        jLabel3.setText("Saída");

        btnEntradaConfirma.setText("Confirmar");
        btnEntradaConfirma.setEnabled(false);
        btnEntradaConfirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntradaConfirmaActionPerformed(evt);
            }
        });

        btnVerificar.setText("Verificar algoritmo");
        btnVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarActionPerformed(evt);
            }
        });

        lblVariavelEntrada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnProcContinuar.setText("Continuar");
        btnProcContinuar.setEnabled(false);
        btnProcContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcContinuarActionPerformed(evt);
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
                                .addComponent(btnEntradaConfirma, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVariavelEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVerificar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)
                                .addComponent(btnInicioPerc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProxPerc))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProcContinuar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnVerificar, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btnInicioPerc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProxPerc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblVariavelEntrada))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEntradaConfirma, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProcContinuar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnInicioPercActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInicioPercActionPerformed
        exprIndex = 1;
        nomeVar = "";
        btnProxPerc.setEnabled(true);
        btnInicioPerc.setEnabled(false);
        formatacao = FORMAT_PLAIN;
        
        for (Expressao e : expressoes){
            switch (e.getTipo()){
                case CRIACAO_VARIAVEL:
                case ENTRADA_DE_DADOS:
                case SAIDA_DE_DADOS:
                    e.setIndice(1);
                    break;
                default:
                    e.setIndice(0);
                    break;
            }
        }
        
        if (tokenAnt != null){
            doc.setCharacterAttributes(tokenAnt.getPosicao(), tokenAnt.getTamanho(), stylePlain, true);
            tokenAnt = null;
        }
        
        tabVariaveis.setRowCount(0);
        variaveis = new HashMap<>();
        varOrdem = new HashMap<>();
        expressao = expressoes.getFirst();
        
    }//GEN-LAST:event_btnInicioPercActionPerformed
   
    private void btnProxPercActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProxPercActionPerformed
        btnInicioPerc.setEnabled(true);
        
        if (tokenAnt != null){
            doc.setCharacterAttributes(tokenAnt.getPosicao(), tokenAnt.getTamanho(), stylePlain, true);
            if (tokenAnt.getFuncaoToken() == FuncaoToken.RES_BLOCO_FIM){
                btnProxPerc.setEnabled(false);
                formatacao = FORMAT_PLAIN;
                JOptionPane.showMessageDialog(this, "Execução concluída", "Execução concluída", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        
        if (!expressao.hasNext()){
            expressao = expressoes.get(exprIndex++);
        }
        
        if (exprIndex - 1 >= expressoes.size()){
            btnProxPerc.setEnabled(false);
        } 
        
        formatacao = FORMAT_PERC;
        Token token = expressao.getNext();
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
                jTable1.clearSelection();
                
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
                nomeVar = token.getPalavra();
                Variavel variavel = new Variavel(tipoVar, nomeVar);
                variaveis.put(nomeVar, variavel);

                ArrayList<Variavel> lista = new ArrayList(variaveis.values());
                Collections.sort(lista, (v1, v2) -> {
                    int comp = v1.getTipo().toString().compareTo(v2.getTipo().toString());
                    if (comp == 0){
                        return v1.getNome().compareToIgnoreCase(v2.getNome());
                    } else {
                        return comp;
                    }
                });
                
                tabVariaveis.setRowCount(0);
                for (Variavel v : lista){
                    tabVariaveis.addRow(new String[]{v.getTipo().toString(),v.getNome(),v.getValor()});
                    varOrdem.put(v.getNome(), lista.indexOf(v));
                }
                
                jTable1.addRowSelectionInterval(varOrdem.get(nomeVar), varOrdem.get(nomeVar));
                break;
            case ENTRADA_DE_DADOS:
                jTable1.clearSelection();
                
                nomeVar = token.getPalavra();
                execEntradaDados();
                break;
                
            case SAIDA_DE_DADOS:
                jTable1.clearSelection();
                
                String saida = "";
                switch (token.getFuncaoToken()){
                    case CONST_CARACTER:
                        saida = token.getPalavra().replace("\"", "");
                        break;
                    case CONST_INTEIRA:
                    case CONST_REAL:
                        saida = token.getPalavra();
                        break;
                    case IDENT_NOME_VARIAVEL:
                        nomeVar = token.getPalavra();
                        variavel = variaveis.get(nomeVar);
                        
                        if (variavel == null){
                            System.err.println("Variável " + nomeVar + " não encontrada");
                        } else {
                            jTable1.addRowSelectionInterval(varOrdem.get(nomeVar), varOrdem.get(nomeVar));
                            if (!variavel.isInicializada()){
                                System.err.println("Variável " + nomeVar + " não inicializada");
                            } else {
                                switch (variavel.getTipo()){
                                    case REAL:
                                        double varReal = Double.parseDouble(variavel.getValor());
                                        saida = String.format(Locale.ENGLISH, "%.3f", varReal);
                                        break;
                                    case INTEIRO:
                                    case CARACTER:
                                        saida = variavel.getValor();
                                        break;
                                }
                            }
                        }
                        break;
                }
                txpSaida.setText(txpSaida.getText() + saida);
                break;
                
            case OPERACAO_ATRIBUICAO:
            case OPERACAO_ARITMETICA:
                jTable1.clearSelection();
                execOperacao();
                break;
                
            case _INVALIDO:
                System.err.println("Expressão inválida");
                System.err.println("\t" + expressao.getTexto());
                
                formatacao = FORMAT_ERROR;
                doc.setCharacterAttributes(token.getPosicao(), token.getTamanho(), styleError, true);
                
                break;
            default:
                System.err.println("Expressão indefinida");
                System.err.println("\t" + expressao.getTexto());
                
                formatacao = FORMAT_ERROR;
                doc.setCharacterAttributes(token.getPosicao(), token.getTamanho(), styleError, true);
                
                break;
        }
        
        tokenAnt = token;
    }//GEN-LAST:event_btnProxPercActionPerformed

    private void btnVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarActionPerformed
        oldText = txpIde.getText();
        Parser parser = new Parser(txpIde.getText());
        expressoes = new LinkedList<>();
        int erros = 0;
        
        while (parser.hasNext()){
            Expressao e = parser.parseExpression();
            if (!parser.getErro().isEmpty()){
                System.err.println(parser.getErro());
                erros ++;
                formatacao = FORMAT_ERROR;
                for (Token t : e.listTokens()){
                    doc.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleError, true);
                }
            }
            switch (e.getTipo()){
                case CRIACAO_VARIAVEL:
                case ENTRADA_DE_DADOS:
                case SAIDA_DE_DADOS:
                    e.setIndice(1);
                    break;
                default:
                    break;
            }
            expressoes.add(e);
        }
        
        if (erros > 0){
            JOptionPane.showMessageDialog(this, erros + " erros encontrados - verifique seu algoritmo", "Verificação concluída", JOptionPane.WARNING_MESSAGE);
            btnProxPerc.setEnabled(false);
            btnInicioPerc.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum erro encontrado - pronto para execução", "Verificação concluída", JOptionPane.INFORMATION_MESSAGE);
            btnVerificar.setEnabled(false);
            
            tabVariaveis.setRowCount(0);
            variaveis = new HashMap<>();
            varOrdem = new HashMap<>();
            expressao = expressoes.getFirst();

            nomeVar = "";
            exprIndex = 1;
            btnProxPerc.setEnabled(true);
            btnInicioPerc.setEnabled(false);
        }
    }//GEN-LAST:event_btnVerificarActionPerformed

    private void txpIdeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txpIdeKeyReleased
        String text = txpIde.getText();
        if (!text.equals(oldText)){
            btnVerificar.setEnabled(true);
        }
        if (formatacao != FORMAT_PLAIN){
            doc.setCharacterAttributes(0, doc.getLength(), stylePlain, true);
        }
        switch (evt.getKeyCode()){
            case KeyEvent.VK_TAB:
            default:
                break;
        }
    }//GEN-LAST:event_txpIdeKeyReleased

    private void execEntradaDados(){
        Variavel variavel = variaveis.get(nomeVar);
        if (variavel == null){
            JOptionPane.showMessageDialog(this, "Variável " + nomeVar + " não encontrada", "Erro de execução", JOptionPane.ERROR_MESSAGE);
        } else {
            jTable1.clearSelection();
            jTable1.setSelectionBackground(Color.YELLOW);
            jTable1.addRowSelectionInterval(varOrdem.get(nomeVar), varOrdem.get(nomeVar));
            
            lblVariavelEntrada.setText("Variável " + nomeVar + " (tipo " + variavel.getTipo() + "):");
            btnProxPerc.setEnabled(false);
            
            txpEntrada.setBackground(backgroundEnabled);
            txpEntrada.setEditable(true);
            btnEntradaConfirma.setEnabled(true);
            txpEntrada.requestFocus();
            Caret caret = txpEntrada.getCaret();
            caret.setDot(0);
            txpEntrada.setCaret(caret);
        }
    }
    
    private void btnEntradaConfirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradaConfirmaActionPerformed
        Variavel variavel = variaveis.get(nomeVar);
        
        boolean valorValido = true;
        String linha = txpEntrada.getText().replace("\n", "");

        switch(variavel.getTipo()){
            case CARACTER:
                variavel.setValor(linha);
                break;
            case INTEIRO:
                try {
                    int valor = Integer.parseInt(linha);
                    variavel.setValor(String.valueOf(valor));
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(this, "Valor \"" + linha + "\" inválido - esperado valor inteiro", "Valor inválido", JOptionPane.WARNING_MESSAGE);
                    valorValido = false;
                }
                break;
            case REAL:
                try {
                    double valor = Double.parseDouble(linha);
                    variavel.setValor(String.valueOf(valor));
                } catch (NumberFormatException ex){
                    JOptionPane.showMessageDialog(this, "Valor \"" + linha + "\" inválido - esperado valor real", "Valor inválido", JOptionPane.WARNING_MESSAGE);
                    valorValido = false;
                }
                break;
        }
        
        if (valorValido){
            variaveis.put(nomeVar, variavel);
            jTable1.setSelectionBackground(Color.GREEN);
            tabVariaveis.setValueAt(variavel.getValor(), varOrdem.get(nomeVar), 2);
            
            jTable1.clearSelection();
            jTable1.addRowSelectionInterval(varOrdem.get(nomeVar), varOrdem.get(nomeVar));

            btnProxPerc.setEnabled(true);
            txpEntrada.setText("");
            lblVariavelEntrada.setText("");
            txpEntrada.setEditable(false);
            btnEntradaConfirma.setEnabled(false);
            txpEntrada.setBackground(backgroundDisabled);
        } 
    }//GEN-LAST:event_btnEntradaConfirmaActionPerformed

    private void txpEntradaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txpEntradaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            btnEntradaConfirmaActionPerformed(null);
        }
    }//GEN-LAST:event_txpEntradaKeyPressed

    private LinkedList<Token> pilha;
    private LinkedList<Token> saida;
    
    public boolean execOperacao(){
        if (pilha == null){
            pilha = new LinkedList<>();
        }
        if (saida == null){
            saida = new LinkedList<>();
        }
        
        while (expressao.hasNext()){
            Token token = expressao.getNext();
            switch (token.getFuncaoToken()){
                case IDENT_NOME_VARIAVEL:
                case CONST_CARACTER:
                case CONST_INTEIRA:
                case CONST_REAL:
                    saida.add(token);
                    break;

                case OP_ATRIBUICAO:
                case OP_SOMA:
                case OP_SUBTRACAO:
                case OP_MULTIPLICACAO:
                case OP_DIV_INTEIRA:
                case OP_DIV_REAL:
                case OP_MOD:
                    while (!pilha.isEmpty() && pilha.peek().getPrecedencia() > token.getPrecedencia()){
                        saida.add(pilha.pop());
                    }
                    pilha.push(token);
                    break;
                
                case DELIM_PARENTESES_ABRE:
                    pilha.push(token);
                    break;
                    
                case DELIM_PARENTESES_FECHA:
                    while (!pilha.isEmpty()){
                        Token out = pilha.pop();
                        if (out.getFuncaoToken() == FuncaoToken.DELIM_PARENTESES_ABRE){
                            break;
                        } else {
                            saida.add(out);
                        }
                    }
                    break;
            }
        }
        while (!pilha.isEmpty()){
            saida.add(pilha.pop());
        }
        
        Calculator calculadora;
        
        while (!saida.isEmpty()){
            Token token = saida.pop();
            Variavel op1, op2;
            switch (token.getFuncaoToken()){
                case IDENT_NOME_VARIAVEL:
                case CONST_CARACTER:
                case CONST_INTEIRA:
                case CONST_REAL:
                    pilha.push(token);
                    break;
                case OP_SOMA:
                case OP_SUBTRACAO:
                case OP_MULTIPLICACAO:
                case OP_DIV_INTEIRA:
                case OP_DIV_REAL:
                case OP_MOD:
                    op2 = retornaVariavel(pilha.pop());
                    op1 = retornaVariavel(pilha.pop());
                    
                    calculadora = new Calculator(token);
                    token = calculadora.executaOperacaoAritmetica(op1, op2);
                    if (token == null){
                        return false;
                    } else {
                        pilha.push(token);
                    }
                    break;
                case OP_ATRIBUICAO:
                    op1 = retornaVariavel(pilha.pop());
                    Token tokVar = pilha.pop();
                    if (tokVar.getFuncaoToken() != FuncaoToken.IDENT_NOME_VARIAVEL){
                        System.err.println("Atribuição inválida - Esperava Variável, encontrou Constante");
                        return false;
                    }
                    String nomeVar = tokVar.getPalavra();
                    Variavel variavel = variaveis.get(nomeVar);
                    if (variavel == null){
                        return false;
                    }
                    switch (variavel.getTipo()){
                        case INTEIRO:
                            if (op1.getTipo() != TipoVariavel.INTEIRO){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                        case REAL:
                            if (op1.getTipo() != TipoVariavel.REAL && op1.getTipo() != TipoVariavel.INTEIRO){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                        case CARACTER:
                            if (op1.getTipo() != TipoVariavel.CARACTER){
                                System.err.println("Atribuição inválida - Esperava " + variavel.getTipo() + ", encontrou " + op1.getTipo());
                                return false;
                            }
                            variavel.setValor(op1.getValor());
                            break;
                    }
                    variaveis.put(nomeVar, variavel);
            }
        }
        
        return true;
    }
    
    private void btnProcContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcContinuarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnProcContinuarActionPerformed
    
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
    private javax.swing.JButton btnEntradaConfirma;
    private javax.swing.JButton btnInicioPerc;
    private javax.swing.JButton btnProcContinuar;
    private javax.swing.JButton btnProxPerc;
    private javax.swing.JButton btnVerificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblVariavelEntrada;
    private javax.swing.JTextPane txpEntrada;
    private javax.swing.JTextPane txpIde;
    private javax.swing.JTextPane txpProcessamento;
    private javax.swing.JTextPane txpSaida;
    // End of variables declaration//GEN-END:variables
    private LinkedList<Expressao> expressoes;
    private HashMap<String,Variavel> variaveis;
    private HashMap<String,Integer> varOrdem;
    private Expressao expressao;
    private int exprIndex;
    private int tabIndex;
    private DefaultStyledDocument doc;
    private int formatacao;
    private String oldText;
    
    private DefaultTableModel tabVariaveis;
    private int bloco = 0;
    private Token tokenAnt = null;
    private String nomeVar;
}
