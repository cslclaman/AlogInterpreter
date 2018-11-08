/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fatec.alg.gui;

import fatec.alg.Principal;
import fatec.alg.geral.log.Erro;
import fatec.alg.geral.log.TipoErro;
import fatec.alg.execucao.config.ConfigInterpreter;
import fatec.alg.execucao.InterfaceExecucao;
import fatec.alg.execucao.Interpreter;
import fatec.alg.analise.AnalisadorSintatico;
import fatec.alg.analise.PreProcessador;
import fatec.alg.analise.AnalisadorLexico;
import fatec.alg.geral.expressao.Expressao;
import fatec.alg.geral.expressao.ExibidorExpressao;
import fatec.alg.geral.instrucao.Instrucao;
import fatec.alg.geral.token.Token;
import fatec.alg.geral.variavel.Variavel;
import java.util.LinkedList;
import javax.swing.text.DefaultStyledDocument;
import fatec.alg.gui.componente.TextLineNumber;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Caret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Caique
 */
public class FrmGui extends javax.swing.JFrame implements InterfaceExecucao {
    
    private Interpreter interpreter = null;
    private boolean emExecucao = false;
    
    private boolean modoEntradaDados = false;
    private boolean modoSaidaDados = false;
    private boolean modoExpressao = false;
    
    StyleContext sc;
    private final Style stylePlain;
    private final int FORMAT_PLAIN = 0;
    
    private final Style stylePerc;
    private final Style styleVarS;
    private final Style styleOper;
    private final int FORMAT_PERC = 1;
    
    private final Style styleError;
    private final Style styleWarn;
    private final Style styleInfo;
    private final Style styleRes;
    private final int FORMAT_ERROR = 2;
    
    private final Color backgroundDisabled = javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground");
    private final Color backgroundEnabled = javax.swing.UIManager.getDefaults().getColor("FormattedTextField.background");
    
    private FrmListaErros frmListaErros;
    private String textoOrig;
    
    /**
     * Creates new form FrmTeste
     */
    public FrmGui() {
        oldText = "";
        textoOrig = "";
        
        sc = new StyleContext();
        
        stylePlain = sc.addStyle("plainBasicao", null);
        stylePlain.addAttribute(StyleConstants.Foreground, Color.BLACK);
        stylePlain.addAttribute(StyleConstants.Background, Color.WHITE);
        
        stylePerc = sc.addStyle("percurso", null);
        stylePerc.addAttribute(StyleConstants.Background, Color.YELLOW);
        styleVarS = sc.addStyle("variavel", null);
        styleVarS.addAttribute(StyleConstants.Background, Color.GREEN);
        
        styleRes = sc.addStyle("percursoRes", null);
        styleRes.addAttribute(StyleConstants.Foreground, Color.BLUE);
        
        styleOper = sc.addStyle("percursoOperador", null);
        styleOper.addAttribute(StyleConstants.Background, Color.CYAN);
        
        styleError = sc.addStyle("tokenErrado", null);
        styleError.addAttribute(StyleConstants.Background, Color.RED);
        styleWarn = sc.addStyle("tokenAlerta", null);
        styleWarn.addAttribute(StyleConstants.Background, Color.ORANGE);
        styleInfo = sc.addStyle("tokenInfo", null);
        styleInfo.addAttribute(StyleConstants.Background, Color.LIGHT_GRAY);
        
        initComponents();
        txpIde.setDocument(new DefaultStyledDocument());
        docIde = (DefaultStyledDocument)txpIde.getDocument();
        docProc = (DefaultStyledDocument)txpProcessamento.getDocument();
        formatacao = FORMAT_PLAIN;
        
        tabVariaveis = (DefaultTableModel)tblVariaveis.getModel();
        tokensAnt = new LinkedList<>();
        
        frmListaErros = new FrmListaErros();
        
        alteracoesAnt = new LinkedList<>();
        alteracoesProx = new LinkedList<>();
        
        this.setTitle(String.format("Interpretador de Algoritmos - versão %s do TG do aluno %s - %s",
                Principal.VERSAO_NUM, "Caíque de Souza Lima Siqueira", Principal.VERSAO_DATA));
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
        btnProxPerc = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVariaveis = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txpEntrada = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txpProcessamento = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txpSaida = new javax.swing.JTextPane();
        btnEntradaConfirma = new javax.swing.JButton();
        btnVerificar = new javax.swing.JButton();
        lblVariavelEntrada = new javax.swing.JLabel();
        lblPosCaret = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        lblLogo1 = new javax.swing.JLabel();
        btnSair = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuArquivo = new javax.swing.JMenu();
        mitNovo = new javax.swing.JMenuItem();
        mitAbrir = new javax.swing.JMenuItem();
        mitSalvar = new javax.swing.JMenuItem();
        mitSalvarComo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mitSair = new javax.swing.JMenuItem();
        mnuEditar = new javax.swing.JMenu();
        mitDesfazer = new javax.swing.JMenuItem();
        mitRefazer = new javax.swing.JMenuItem();
        mnuVerificar = new javax.swing.JMenu();
        mitVerificarAlgoritmo = new javax.swing.JMenuItem();
        mitExibirErros = new javax.swing.JMenuItem();
        mitProximoPasso = new javax.swing.JMenuItem();
        mnuAjuda = new javax.swing.JMenu();
        mitSobre = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Interpreter");
        setMinimumSize(new java.awt.Dimension(800, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jScrollPane2.setVerifyInputWhenFocusTarget(false);

        txpIde.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        txpIde.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txpIdeCaretUpdate(evt);
            }
        });
        txpIde.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                txpIdeCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        txpIde.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txpIdeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txpIdeKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(txpIde);

        jScrollPane2.setRowHeaderView(new TextLineNumber(txpIde, 2));

        btnProxPerc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-next-arrow-24.png"))); // NOI18N
        btnProxPerc.setText("Próximo passo (seta p/ baixo)");
        btnProxPerc.setActionCommand("Próximo passo >>");
        btnProxPerc.setEnabled(false);
        btnProxPerc.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnProxPerc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProxPercActionPerformed(evt);
            }
        });

        tblVariaveis.setModel(new javax.swing.table.DefaultTableModel(
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
        tblVariaveis.setSelectionBackground(java.awt.Color.green);
        tblVariaveis.setSelectionForeground(java.awt.Color.black);
        jScrollPane1.setViewportView(tblVariaveis);

        jLabel1.setText("Entrada");

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txpEntrada.setEditable(false);
        txpEntrada.setBackground(javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground"));
        txpEntrada.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        txpEntrada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txpEntradaKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txpEntrada);

        jLabel2.setText("Processamento");

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane7.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        txpProcessamento.setEditable(false);
        txpProcessamento.setBackground(javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground"));
        txpProcessamento.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jScrollPane7.setViewportView(txpProcessamento);

        jLabel3.setText("Saída");

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        txpSaida.setEditable(false);
        txpSaida.setBackground(javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground"));
        txpSaida.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        txpSaida.setMinimumSize(new java.awt.Dimension(13, 66));
        txpSaida.setName(""); // NOI18N
        txpSaida.setPreferredSize(new java.awt.Dimension(13, 66));
        jScrollPane6.setViewportView(txpSaida);

        btnEntradaConfirma.setText("Confirmar");
        btnEntradaConfirma.setEnabled(false);
        btnEntradaConfirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntradaConfirmaActionPerformed(evt);
            }
        });

        btnVerificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-check1-24.png"))); // NOI18N
        btnVerificar.setText("Verificar algoritmo");
        btnVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarActionPerformed(evt);
            }
        });

        lblVariavelEntrada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        lblPosCaret.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        lblPosCaret.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblPosCaret.setText("Linha 1, Coluna 1");
        lblPosCaret.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setText("Variáveis");

        lblLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/logo-fatecso-64.png"))); // NOI18N

        lblLogo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/logo-cps-64.png"))); // NOI18N

        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-exit-door-24.png"))); // NOI18N
        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        mnuArquivo.setText("Arquivo");

        mitNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNovo.setText("Novo");
        mitNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNovoActionPerformed(evt);
            }
        });
        mnuArquivo.add(mitNovo);

        mitAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mitAbrir.setText("Abrir");
        mitAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAbrirActionPerformed(evt);
            }
        });
        mnuArquivo.add(mitAbrir);

        mitSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSalvar.setText("Salvar");
        mitSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSalvarActionPerformed(evt);
            }
        });
        mnuArquivo.add(mitSalvar);

        mitSalvarComo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mitSalvarComo.setText("Salvar como");
        mitSalvarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSalvarComoActionPerformed(evt);
            }
        });
        mnuArquivo.add(mitSalvarComo);
        mnuArquivo.add(jSeparator1);

        mitSair.setText("Sair");
        mitSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSairActionPerformed(evt);
            }
        });
        mnuArquivo.add(mitSair);

        jMenuBar1.add(mnuArquivo);

        mnuEditar.setText("Editar");

        mitDesfazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        mitDesfazer.setText("Desfazer");
        mitDesfazer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDesfazerActionPerformed(evt);
            }
        });
        mnuEditar.add(mitDesfazer);

        mitRefazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        mitRefazer.setText("Refazer");
        mitRefazer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRefazerActionPerformed(evt);
            }
        });
        mnuEditar.add(mitRefazer);

        jMenuBar1.add(mnuEditar);

        mnuVerificar.setText("Verificar");

        mitVerificarAlgoritmo.setText("Verificar algoritmo");
        mitVerificarAlgoritmo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitVerificarAlgoritmoActionPerformed(evt);
            }
        });
        mnuVerificar.add(mitVerificarAlgoritmo);

        mitExibirErros.setText("Exibir erros da última verificação");
        mitExibirErros.setEnabled(false);
        mitExibirErros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExibirErrosActionPerformed(evt);
            }
        });
        mnuVerificar.add(mitExibirErros);

        mitProximoPasso.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0));
        mitProximoPasso.setText("Próximo passo");
        mitProximoPasso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitProximoPassoActionPerformed(evt);
            }
        });
        mnuVerificar.add(mitProximoPasso);

        jMenuBar1.add(mnuVerificar);

        mnuAjuda.setText("Ajuda");

        mitSobre.setText("Sobre");
        mitSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSobreActionPerformed(evt);
            }
        });
        mnuAjuda.add(mitSobre);

        jMenuBar1.add(mnuAjuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPosCaret, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEntradaConfirma, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVariavelEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVerificar, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnProxPerc, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 94, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblLogo)
                        .addGap(18, 18, 18)
                        .addComponent(lblLogo1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(btnSair, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnProxPerc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                        .addComponent(btnVerificar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblLogo)
                    .addComponent(lblLogo1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPosCaret)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblVariavelEntrada))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEntradaConfirma, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
        
    private void btnProxPercActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProxPercActionPerformed
        boolean enabled;
        
        if (interpreter == null) {
            enabled = false;
        } else {
            interpreter.proxima();
            enabled = interpreter.existeProxima();
        }
        btnProxPerc.setEnabled(enabled && !btnEntradaConfirma.isEnabled());
        emExecucao = enabled;
        if (!enabled) {
            JOptionPane.showMessageDialog(this, "Execução finalizada", "Execução finalizada", JOptionPane.INFORMATION_MESSAGE);
            limpaSelecaoVariaveis();
            limpaTokensPercurso();
            txpSaida.setText("");
            txpSaida.setBackground(backgroundDisabled);
            tabVariaveis.setRowCount(0);
        }
        
    }//GEN-LAST:event_btnProxPercActionPerformed

    private void btnVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarActionPerformed
        if (!pararExecucao("Verificar e reiniciar")) return;
        
        oldText = txpIde.getText();
        
        if (oldText.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nada para verificar - algoritmo vazio", "Verificação concluída", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AnalisadorLexico scanner = new AnalisadorLexico(oldText);
        List<Token> tokens = scanner.listaTokens();
        
        if (scanner.getNumErros(TipoErro.DEVEL) > 0){
            JOptionPane.showMessageDialog(
                    this,
                    scanner.getNumErros(TipoErro.DEVEL) +
                            " erros encontrados - verifique seu algoritmo",
                    "Verificação concluída",
                    JOptionPane.ERROR_MESSAGE);
            formatacao = FORMAT_ERROR;
            System.out.println("SCANNER (Análise Léxica)");
            for (Erro e : scanner.getErros(TipoErro.DEVEL)){
                Token t = e.getToken();
                System.out.println(e.toString());
                docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleError, true);
            }
            
            btnProxPerc.setEnabled(false);

            mitExibirErros.setEnabled(true);
            frmListaErros.setListaErros(scanner.getErros(TipoErro.ALERTA));
            frmListaErros.setVisible(true);
            
        } else {
            AnalisadorSintatico parser = new AnalisadorSintatico(tokens);
            LinkedList<Instrucao> instrucoes = new LinkedList<>();

            instrucoes = parser.listaInstrucoes();

            if (parser.getNumErros(TipoErro.DEVEL) > 0){
                JOptionPane.showMessageDialog(this, parser.getNumErros(TipoErro.DEVEL) + " erros encontrados - verifique seu algoritmo", "Verificação concluída", JOptionPane.ERROR_MESSAGE);
                formatacao = FORMAT_ERROR;
                System.out.println("PARSER (Análise Sintática)");
                for (Erro e : parser.getErros(TipoErro.DEVEL)){
                    Token t = e.getToken();
                    docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleError, true);
                    System.out.println(e.toString());
                }
                btnProxPerc.setEnabled(false);
                
                mitExibirErros.setEnabled(true);
                frmListaErros.setListaErros(parser.getErros(TipoErro.ALERTA));
                frmListaErros.setVisible(true);
                
            } else {
                
                PreProcessador processor = new PreProcessador(instrucoes);
                processor.verificaPrograma();
                
                int nfl = processor.getNumErros(TipoErro.INFO);
                int nerrpp = processor.getNumErros(TipoErro.ERRO);
                int nalepp = nfl - nerrpp;
                
                if (nerrpp > 0){
                    JOptionPane.showMessageDialog(this, nerrpp + " erros encontrados - verifique seu algoritmo", "Verificação concluída", JOptionPane.ERROR_MESSAGE);
                    formatacao = FORMAT_ERROR;
                    System.out.println("PRE PROCESSOR (Análise Semântica)");
                    for (Erro e : processor.getErros(TipoErro.ERRO)){
                        Token t = e.getToken();
                        docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleError, true);
                    }
                    btnProxPerc.setEnabled(false);
                    mitExibirErros.setEnabled(true);
                    frmListaErros.setListaErros(processor.getErros(TipoErro.ALERTA));
                    frmListaErros.setVisible(true);
                } else {
                    if (nalepp > 0) {
                        for (Erro e : processor.getErros(TipoErro.ALERTA)){
                            Token t = e.getToken();
                            if (e.getTipo() == TipoErro.ALERTA) {
                                docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleWarn, true);
                            }
                            if (e.getTipo() == TipoErro.INFO) {
                                docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleInfo, true);
                            }
                        }
                        mitExibirErros.setEnabled(true);
                        frmListaErros.setListaErros(processor.getErros(TipoErro.INFO));
                        frmListaErros.setVisible(true);
                        JOptionPane.showMessageDialog(this, nalepp + " alertas detectados - pronto para execução", "Verificação concluída", JOptionPane.WARNING_MESSAGE);
                        mitExibirErros.setEnabled(false);
                    } else {
                        JOptionPane.showMessageDialog(this, "Nenhum erro encontrado - pronto para execução", "Verificação concluída", JOptionPane.INFORMATION_MESSAGE);
                        mitExibirErros.setEnabled(false);
                    }
                    
                    lblPosCaret.setText("Em execução");
                    emExecucao = true;

                    ConfigInterpreter configInterpr = new ConfigInterpreter();
                    configInterpr.set(ConfigInterpreter.RUNNEXT_LEIA_ATRIB, true);
                    configInterpr.set(ConfigInterpreter.RUNNEXT_ESCREVA_PILHA, true);
                    configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_EXEC_CONST, true);
                    configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_CONST, true);
                    configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_EXEC_VAR, true);
                    configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_VAR, true);
                    configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_UNARIA, true);
                    configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_OPBIN, true);
                    
                    interpreter = new Interpreter(this, processor.getPrograma());
                    interpreter.setConfigInterpreter(configInterpr);
                    btnProxPerc.setEnabled(interpreter.existeProxima());
                    variaveis = new LinkedList<>();
                    impressoraExpressao = null;
                }
            }
        }
    }//GEN-LAST:event_btnVerificarActionPerformed

    private void txpIdeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txpIdeKeyReleased
        String text = txpIde.getText();
        if (!text.equals(oldText)){
            btnVerificar.setEnabled(true);
            alteracoesProx.clear();
            // para garantir que não vai estourar a pilha
            if (alteracoesAnt.size() > 100) {
                alteracoesAnt.poll();
            }
            alteracoesAnt.push(oldText);
        }
        if (formatacao != FORMAT_PLAIN){
            docIde.setCharacterAttributes(0, docIde.getLength(), stylePlain, true);
        }
        oldText = text;
    }//GEN-LAST:event_txpIdeKeyReleased
    
    private void btnEntradaConfirmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntradaConfirmaActionPerformed
        interpreter.proxima();
        btnProxPerc.setEnabled(interpreter.existeProxima() && !btnEntradaConfirma.isEnabled());
        emExecucao = interpreter.existeProxima();
    }//GEN-LAST:event_btnEntradaConfirmaActionPerformed

    private void txpEntradaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txpEntradaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            evt.consume();
            btnEntradaConfirmaActionPerformed(null);
        }
    }//GEN-LAST:event_txpEntradaKeyPressed

    private void txpIdeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txpIdeKeyPressed
        switch (evt.getKeyCode()){
            case KeyEvent.VK_TAB: {
                evt.consume();
                if (!evt.isShiftDown()) {
                    int pos = txpIde.getCaretPosition();
                    String txta = txpIde.getText().substring(0, pos);
                    String txtb = txpIde.getText().substring(pos);
                    txpIde.setText(txta + "    " + txtb);
                    txpIde.setCaretPosition(pos + 4);
                } else {
                    int pos = txpIde.getCaretPosition();
                    String txta = txpIde.getText().substring(0, pos);
                    String txtb = txpIde.getText().substring(pos);
                    if (txta.length() >= 4 && txta.substring(pos - 4, pos).equals("    ")) {
                        txpIde.setText(txta.substring(0, pos - 4) + txtb);
                        txpIde.setCaretPosition(pos - 4);
                    }
                }
                break; 
            }
            case KeyEvent.VK_ENTER: {
                evt.consume();
                int pos = txpIde.getCaretPosition();
                String txta = txpIde.getText().substring(0, pos);
                String txtb = txpIde.getText().substring(pos);
                txpIde.setText(txta + "\n" + txtb);
                txpIde.setCaretPosition(pos + 1);
                break;
            } 
            case KeyEvent.VK_INSERT:
            case KeyEvent.VK_V: {
                if ((evt.getKeyCode() == KeyEvent.VK_V && evt.isControlDown()) || 
                    (evt.getKeyCode() == KeyEvent.VK_INSERT && evt.isShiftDown()) ) {
                    evt.consume();
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    try {
                        String text = clipboard.getData(DataFlavor.stringFlavor).toString();
                        int pos = txpIde.getCaretPosition();
                        String txta = txpIde.getText().substring(0, pos);
                        String txtb = txpIde.getText().substring(pos);
                        
                        String subst;
                        if (text.contains("\r")) {
                            if (text.contains("\n")) {
                                subst = "";
                            } else {
                                subst = "\n";
                            }
                            text = text.replace("\r", subst);
                        }
                        text = text.replace("\t", "    ");
                        txpIde.setText(txta + text + txtb);
                        txpIde.setCaretPosition(pos + text.length());
                    } catch (IOException | UnsupportedFlavorException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Conteúdo a ser colado não suportado",
                                "Erro ao colar texto",
                                JOptionPane.WARNING_MESSAGE);
                        System.err.println(ex.getClass().getName() + " - " + ex.getMessage());
                    }
                }
                break;
            }
            default:
                break;
        }
    }//GEN-LAST:event_txpIdeKeyPressed

    private void txpIdeCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txpIdeCaretPositionChanged
        
    }//GEN-LAST:event_txpIdeCaretPositionChanged

    private void txpIdeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txpIdeCaretUpdate
        int lin = 1, col = 1;
        for (char ch : txpIde.getText().substring(0, evt.getDot()).toCharArray()){
            if (ch == '\n'){
                lin ++;
                col = 1;
            } else {
                col ++;
            }
        }
        lblPosCaret.setText("Linha " + lin + ", Coluna " + col);
    }//GEN-LAST:event_txpIdeCaretUpdate

    private void mitSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSairActionPerformed
        encerra();
    }//GEN-LAST:event_mitSairActionPerformed

    private void mitAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAbrirActionPerformed
        if (!pararExecucao("Abrir arquivo")) return;
        
        JFileChooser fileChooser = new JFileChooser(arquivo);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "txt", "alg"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setSelectedFile(arquivo);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            arquivo = fileChooser.getSelectedFile();
            try {
                StringBuilder codigofonte = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "UTF-8"));
                String texto;
                while ((texto = br.readLine()) != null){
                    for (char ch : texto.toCharArray()){
                        if (ch == '\t'){
                            codigofonte.append("    ");
                        } else {
                            codigofonte.append(ch);
                        }
                    }
                    codigofonte.append("\n");
                }
                textoOrig = codigofonte.toString();
                txpIde.setText(textoOrig);
                br.close();
            } catch (IOException ex){
                JOptionPane.showMessageDialog(this, "Não foi possível carregar o arquivo especificado", "Erro ao abrir arquivo", JOptionPane.ERROR_MESSAGE);
                System.err.println(ex.toString()); 
            }
        }
    }//GEN-LAST:event_mitAbrirActionPerformed

    private void mitSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSalvarActionPerformed
        if (arquivo == null){
            String caminho = System.getProperty("user.home") + File.separator + "algoritmo.alg";
            arquivo = new File(caminho);
            
            mitSalvarComoActionPerformed(evt);
        } else {
            try {
                textoOrig = txpIde.getText();
                FileOutputStream fos = new FileOutputStream(arquivo, false);
                OutputStreamWriter wr = new OutputStreamWriter(fos,"UTF-8");
                wr.write(textoOrig);
                wr.close();
                fos.close();
            } catch (IOException ex){
                JOptionPane.showMessageDialog(this, "Não foi possível salvar o arquivo especificado", "Erro ao salvar arquivo", JOptionPane.ERROR_MESSAGE);
                System.err.println(ex.toString()); 
            }
        }
    }//GEN-LAST:event_mitSalvarActionPerformed

    private void mitSalvarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSalvarComoActionPerformed
        JFileChooser fileChooser = new JFileChooser(arquivo);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto", "alg"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setSelectedFile(arquivo);
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            arquivo = fileChooser.getSelectedFile();
            int pontoIndex = arquivo.getName().lastIndexOf(".");
            if (pontoIndex < 0) {
                arquivo = new File(arquivo.getPath() + ".alg");
            }
            mitSalvarActionPerformed(evt);
        }
    }//GEN-LAST:event_mitSalvarComoActionPerformed

    private void mitVerificarAlgoritmoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitVerificarAlgoritmoActionPerformed
        btnVerificarActionPerformed(evt);
    }//GEN-LAST:event_mitVerificarAlgoritmoActionPerformed

    private void mitExibirErrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExibirErrosActionPerformed
        frmListaErros.setVisible(true);
    }//GEN-LAST:event_mitExibirErrosActionPerformed

    private void mitSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSobreActionPerformed
        new FrmSobre().setVisible(true);
    }//GEN-LAST:event_mitSobreActionPerformed

    private void mitDesfazerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDesfazerActionPerformed
        if (!alteracoesAnt.isEmpty()) {
            // Se necessário caso a pilha de alterações estoure a memória...
            if (alteracoesProx.size() >= 100) {
                alteracoesProx.poll();
            } 
            String text = txpIde.getText();
            alteracoesProx.push(text);
            oldText = alteracoesAnt.pop();
            txpIde.setText(oldText);
        }
    }//GEN-LAST:event_mitDesfazerActionPerformed

    private void mitRefazerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRefazerActionPerformed
        if (!alteracoesProx.isEmpty()) {
            // Se necessário caso a pilha de alterações estoure a memória...
            if (alteracoesAnt.size() >= 100) {
                alteracoesAnt.poll();
            } 
            String text = txpIde.getText();
            alteracoesAnt.push(text);
            oldText = alteracoesProx.pop();
            txpIde.setText(oldText);
        }
    }//GEN-LAST:event_mitRefazerActionPerformed

    private void mitProximoPassoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitProximoPassoActionPerformed
        if (btnProxPerc.isEnabled()) {
            btnProxPercActionPerformed(evt);
        }
    }//GEN-LAST:event_mitProximoPassoActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        encerra();
    }//GEN-LAST:event_btnSairActionPerformed

    private void mitNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNovoActionPerformed
        if (!pararExecucao("Novo programa")) return;
        
        String atual = txpIde.getText();
        if (!textoOrig.equals(atual)) {
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                    this,
                    "Deseja salvar o seu código atual?",
                    "Novo programa",
                    JOptionPane.YES_NO_OPTION)) {
                mitSalvarActionPerformed(evt);
            }
        }
        docIde.setCharacterAttributes(0, atual.length(), stylePlain, true);
        textoOrig = "";
        txpIde.setText("");
        oldText = "";
    }//GEN-LAST:event_mitNovoActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        encerra();
    }//GEN-LAST:event_formWindowClosing
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntradaConfirma;
    private javax.swing.JButton btnProxPerc;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnVerificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblLogo1;
    private javax.swing.JLabel lblPosCaret;
    private javax.swing.JLabel lblVariavelEntrada;
    private javax.swing.JMenuItem mitAbrir;
    private javax.swing.JMenuItem mitDesfazer;
    private javax.swing.JMenuItem mitExibirErros;
    private javax.swing.JMenuItem mitNovo;
    private javax.swing.JMenuItem mitProximoPasso;
    private javax.swing.JMenuItem mitRefazer;
    private javax.swing.JMenuItem mitSair;
    private javax.swing.JMenuItem mitSalvar;
    private javax.swing.JMenuItem mitSalvarComo;
    private javax.swing.JMenuItem mitSobre;
    private javax.swing.JMenuItem mitVerificarAlgoritmo;
    private javax.swing.JMenu mnuAjuda;
    private javax.swing.JMenu mnuArquivo;
    private javax.swing.JMenu mnuEditar;
    private javax.swing.JMenu mnuVerificar;
    private javax.swing.JTable tblVariaveis;
    private javax.swing.JTextPane txpEntrada;
    private javax.swing.JTextPane txpIde;
    private javax.swing.JTextPane txpProcessamento;
    private javax.swing.JTextPane txpSaida;
    // End of variables declaration//GEN-END:variables
    private LinkedList<Variavel> variaveis;
    private LinkedList<Token> tokensAnt;
    private LinkedList<String> alteracoesAnt;
    private LinkedList<String> alteracoesProx;
    private DefaultStyledDocument docIde;
    private DefaultStyledDocument docProc;
    private int formatacao;
    private String oldText;
    
    private DefaultTableModel tabVariaveis;
    
    private File arquivo;
    
    private ExibidorExpressao impressoraExpressao;

    @Override
    public void atualizaInstrucao() {
        //impressoraExpressao = null;
        docProc.setCharacterAttributes(0, txpProcessamento.getText().length(), stylePlain, true);
        txpProcessamento.setText("");
        txpProcessamento.setBackground(backgroundDisabled);
    }

    @Override
    public void expressaoFinalizada() {
        //impressoraExpressao = null;
        docProc.setCharacterAttributes(0, txpProcessamento.getText().length(), stylePlain, true);
        txpProcessamento.setText("");
        txpProcessamento.setBackground(backgroundDisabled);
    }
    
    @Override
    public void atualizaPassoAtual(Token token) {
        limpaSelecaoVariaveis();
        limpaTokensPercurso();
        addTokenPercurso(token);
    }
    
    private void addTokenPercurso(Token token) {
        tokensAnt.push(token);
        docIde.setCharacterAttributes(token.getPosicao(), token.getTamanho(), stylePerc, true);
    }
    
    @Override
    public void atualizaPassoAtual(Token controle, Token token) {
        atualizaPassoAtual(token);
        tokensAnt.push(controle);
        docIde.setCharacterAttributes(controle.getPosicao(), controle.getTamanho(), styleRes, true);
    }
    
    @Override
    public void atualizaPassoAtual(Token controle, Token... tokens) {
        limpaSelecaoVariaveis();
        limpaTokensPercurso();
        for (Token token : tokens) {
            addTokenPercurso(token);
        }
        tokensAnt.push(controle);
        docIde.setCharacterAttributes(controle.getPosicao(), controle.getTamanho(), styleRes, true);
    }

    @Override
    public void atualizaExpressaoAtual(Expressao expressao) {
        
        docProc.setCharacterAttributes(0, txpProcessamento.getText().length(), stylePlain, true);
        txpProcessamento.setBackground(backgroundEnabled);
        if (impressoraExpressao == null) {
            impressoraExpressao = new ExibidorExpressao(expressao);
        } else {
            impressoraExpressao.setExpressaoAtual(expressao);
        }
        txpProcessamento.setText(impressoraExpressao.getTexto());
        /*
        Token tokenExpr = impressoraExpressao.getTokenExprAtual();
        Token tokenRes = impressoraExpressao.getTokenResultado();
        docProc.setCharacterAttributes(tokenExpr.getPosicao(), tokenExpr.getTamanho(), styleOper, true);
        docProc.setCharacterAttributes(tokenRes.getPosicao(), tokenRes.getTamanho(), styleRes, true);*/
        //System.out.println(expressao.imprimeExpressao());
    }

    @Override
    public void declaracaoVariavel(Variavel variavel) {
        variaveis.add(variavel);
        Collections.sort(variaveis, (v1, v2) -> {
            int comp = v1.getTipo().name().compareTo(v2.getTipo().name());
            return comp == 0 ? v1.getNome().compareToIgnoreCase(v2.getNome()) : comp;
        });
        tabVariaveis.setRowCount(0);
        for (Variavel v : variaveis){
            tabVariaveis.addRow(new String[]{v.getTipo().toString(),v.getNome(),v.getValor()});
        }
        selecionaVariavel(variavel);
    }

    @Override
    public void entradaDados(Variavel variavel) {
        lblVariavelEntrada.setText("Variável " + variavel.getNome() + " (tipo " + variavel.getTipo() + "):");
        btnProxPerc.setEnabled(false);
            
        txpEntrada.setBackground(backgroundEnabled);
        txpEntrada.setEditable(true);
        txpEntrada.setText("");
        btnEntradaConfirma.setEnabled(true);
        txpEntrada.requestFocus();
        Caret caret = txpEntrada.getCaret();
        caret.setDot(0);
        txpEntrada.setCaret(caret);
    }

    @Override
    public String entradaDadosRetorno() {
        String linha = txpEntrada.getText().replace("\n", "");
        
        lblVariavelEntrada.setText("");
        btnEntradaConfirma.setEnabled(false);
        txpEntrada.setText("");
        txpEntrada.setEditable(false);
        txpEntrada.setBackground(backgroundDisabled);
        btnProxPerc.setEnabled(interpreter.existeProxima());
        btnProxPerc.requestFocus();
        
        return linha;
    }

    @Override
    public void erroEntradaDados(Variavel variavel, Erro erro) {
        JOptionPane.showMessageDialog(this, erro.getMensagem(), "Valor inválido", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void saidaDados(String saida) {
        txpSaida.setBackground(backgroundEnabled);
        txpSaida.setText(txpSaida.getText() + saida);
        
        //impressoraExpressao = null;
        docProc.setCharacterAttributes(0, txpProcessamento.getText().length(), stylePlain, true);
        txpProcessamento.setText("");
        txpProcessamento.setBackground(backgroundDisabled);
    }

    @Override
    public void defineValorVariavel(Variavel variavel) {
        int i = variaveis.indexOf(variavel);
        variaveis.set(i, variavel);
        tblVariaveis.setValueAt(variavel.getValor(),i,2);
        tblVariaveis.setSelectionBackground(Color.GREEN);
        tblVariaveis.addRowSelectionInterval(i, i);
    }

    @Override
    public void defineValorVariavel(Token identificador, Variavel variavel) {
        defineValorVariavel(variavel);
        tokensAnt.push(identificador);
        docIde.setCharacterAttributes(identificador.getPosicao(), identificador.getTamanho(), styleVarS, true);
    }
    
    @Override
    public void selecionaVariavel(Variavel variavel) {
        tblVariaveis.setSelectionBackground(Color.YELLOW);
        int i = variaveis.indexOf(variavel);
        tblVariaveis.addRowSelectionInterval(i, i);
    }

    @Override
    public void erroFatal(Erro erro) {
        JOptionPane.showMessageDialog(this, erro.getMensagem(), "Erro na execução", JOptionPane.ERROR_MESSAGE);
        btnProxPerc.setEnabled(false);
        emExecucao = false;
    }
    
    @Override
    public void finalizado() {
        lblPosCaret.setText("Finalizado");
    }
    
    private void limpaSelecaoVariaveis() {
        tblVariaveis.clearSelection();
    }
    
    private void limpaTokensPercurso() {
        while (!tokensAnt.isEmpty()){
            Token tokenAnt = tokensAnt.pop();
            docIde.setCharacterAttributes(tokenAnt.getPosicao(), tokenAnt.getTamanho(), stylePlain, true);
        }
    }
    
    private boolean pararExecucao(String titulo) {
        if (emExecucao) {
            if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(
                    this,
                    "Deseja interromper a execução do programa atual?",
                    titulo,
                    JOptionPane.YES_NO_OPTION)) {
                return false;
            }
        }
        
        emExecucao = false;
        btnProxPerc.setEnabled(false);
        
        while (!tokensAnt.isEmpty()){
            Token tokenAnt = tokensAnt.pop();
            docIde.setCharacterAttributes(tokenAnt.getPosicao(), tokenAnt.getTamanho(), stylePlain, true);
        }
        
        tabVariaveis.setRowCount(0);

        txpEntrada.setText("");
        txpEntrada.setEditable(false);
        txpEntrada.setBackground(backgroundDisabled);

        docProc.setCharacterAttributes(0, docProc.getLength(), stylePlain, true);
        txpProcessamento.setText("");
        txpProcessamento.setBackground(backgroundDisabled);

        txpSaida.setText("");
        txpSaida.setBackground(backgroundDisabled);
        
        return true;
    }
    
    private void encerra() {
        String atual = txpIde.getText();
        if (!textoOrig.equals(atual)) {
            if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(
                    this,
                    "Deseja salvar o seu código atual antes de sair?",
                    "Fechar programa",
                    JOptionPane.YES_NO_OPTION)) {
                mitSalvarActionPerformed(null);
            }
        }
        System.exit(0);
    }
}
