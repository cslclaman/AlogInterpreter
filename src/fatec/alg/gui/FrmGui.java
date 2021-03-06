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
import fatec.alg.geral.programa.Programa;
import fatec.alg.geral.token.Token;
import fatec.alg.geral.variavel.Variavel;
import java.util.LinkedList;
import javax.swing.text.DefaultStyledDocument;
import fatec.alg.gui.componente.TextLineNumber;
import java.awt.Color;
import java.awt.Font;
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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private static final Logger logger = Logger.getLogger(FrmGui.class.getName());
    
    private static final int MAX_ALTERACOES = 150;
    
    private final Interpreter interpreter;
    private boolean emExecucao = false;
    
    StyleContext sc;
    private final Style stylePlain;
    private final int FORMAT_PLAIN = 0;
    
    private final Style stylePerc;
    private final Style styleVarS;
    private final Style styleOper;
    private final Style styleExpr;
    private final int FORMAT_PERC = 1;
    
    private final Style styleError;
    private final Style styleWarn;
    private final Style styleInfo;
    private final Style styleRes;
    private final int FORMAT_ERROR = 2;
    
    private final float defaultFontSize;
    
    private final Color backgroundDisabled = javax.swing.UIManager.getDefaults().getColor("FormattedTextField.disabledBackground");
    private final Color backgroundEnabled = javax.swing.UIManager.getDefaults().getColor("FormattedTextField.background");
    
    private final ConfigInterpreter configInterpr = new ConfigInterpreter();
    
    private FrmListaErros frmListaErros;
    private String textoOrig;
    
    /**
     * Creates new form FrmTeste
     */
    public FrmGui(String filename) {
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
        styleOper.addAttribute(StyleConstants.Foreground, Color.BLACK);
        styleOper.addAttribute(StyleConstants.Background, Color.CYAN);
        styleExpr = sc.addStyle("percursoExpressao", null);
        styleExpr.addAttribute(StyleConstants.Foreground, Color.BLUE);
        styleExpr.addAttribute(StyleConstants.Bold, true);
        styleExpr.addAttribute(StyleConstants.Underline, true);
        
        styleError = sc.addStyle("tokenErrado", null);
        styleError.addAttribute(StyleConstants.Background, Color.RED);
        styleWarn = sc.addStyle("tokenAlerta", null);
        styleWarn.addAttribute(StyleConstants.Background, Color.ORANGE);
        styleInfo = sc.addStyle("tokenInfo", null);
        styleInfo.addAttribute(StyleConstants.Background, Color.LIGHT_GRAY);
        
        configInterpr.set(ConfigInterpreter.RUNNEXT_LEIA_ATRIB, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_PILHA_CONST, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_EXEC_FUNC, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_EXEC_UNARIA, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_EXEC_OPBIN, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_CONST, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_VAR, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_FUNC, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_UNARIA, true);
        configInterpr.set(ConfigInterpreter.RUNNEXT_EXPR_RES_OPBIN, true);
        configInterpr.set(ConfigInterpreter.FORMAT_ESCREVA_ESPACO, true);
        
        initComponents();
        txpIde.setDocument(new DefaultStyledDocument());
        docIde = (DefaultStyledDocument)txpIde.getDocument();
        docProc = (DefaultStyledDocument)txpProcessamento.getDocument();
        formatacao = FORMAT_PLAIN;
        defaultFontSize = txpIde.getFont().getSize2D();
        
        tabVariaveis = (DefaultTableModel)tblVariaveis.getModel();
        tokensAnt = new LinkedList<>();
        
        frmListaErros = new FrmListaErros();
        
        alteracoesAnt = new LinkedList<>();
        alteracoesProx = new LinkedList<>();
        
        interpreter = new Interpreter(this);
        interpreter.setConfigInterpreter(configInterpr);
        
        this.setTitle(String.format("Interpretador de Algoritmos - versão %s do TG do aluno %s - %s",
                Principal.VERSAO_NUM, "Caíque de Souza Lima Siqueira", Principal.VERSAO_DATA));
        
        this.setIconImage(new javax.swing.ImageIcon(
                getClass().getResource("/fatec/alg/gui/imagens/logo-interpr-64.png"))
                .getImage()
        );
        
        if (filename != null && !filename.isEmpty()) {
            abreArquivo(filename);
        }
    }
    
    private void abreArquivo(String filename) {
        arquivo = new File(filename);
        abreArquivo();
    }
    
    private void abreArquivo() {
        String nomeArq = "Algoritmo";
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
            ideFontTamanhoRestaura();
            txpIde.setText(textoOrig);
            nomeArq = arquivo.getName();
            br.close();
        } catch (IOException ex){
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar o arquivo especificado",
                    "Erro ao abrir arquivo",
                    JOptionPane.ERROR_MESSAGE);
            
            logger.log(Level.WARNING, "{0}\n {1} - {2}",
                new Object[]{
                    "Não foi possível carregar o arquivo especificado",
                    ex.getClass().getName(),
                    ex.getMessage()
                });            
        }
        lblNomeArquivo.setText(nomeArq);
        lblNomeArquivo.setFont(lblNomeArquivo.getFont().deriveFont(Font.PLAIN));
        lblNomeArquivo.setToolTipText(null);
    }
    
    private void salvaArquivo() {
        try {
            textoOrig = txpIde.getText();
            FileOutputStream fos = new FileOutputStream(arquivo, false);
            OutputStreamWriter wr = new OutputStreamWriter(fos,"UTF-8");
            wr.write(textoOrig);
            
            lblNomeArquivo.setText(arquivo.getName());
            lblNomeArquivo.setFont(lblNomeArquivo.getFont().deriveFont(Font.PLAIN));
            lblNomeArquivo.setToolTipText(null);
            
            wr.close();
            fos.close();
        } catch (IOException ex){
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível salvar o arquivo especificado",
                    "Erro ao salvar arquivo",
                    JOptionPane.ERROR_MESSAGE);

            logger.log(Level.WARNING, "{0}\n {1} - {2}",
                new Object[]{
                    "Não foi possível salvar o arquivo especificado",
                    ex.getClass().getName(),
                    ex.getMessage()
                });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrTxpIde = new javax.swing.JScrollPane();
        txpIde = new javax.swing.JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth()
            {
                return getUI().getPreferredSize(this).width 
                <= getParent().getSize().width;
            }
        };
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
        jLabel5 = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        lblLogo1 = new javax.swing.JLabel();
        btnSair = new javax.swing.JButton();
        lblFontSize = new javax.swing.JLabel();
        btnPararExec = new javax.swing.JButton();
        pnlStatus = new javax.swing.JPanel();
        lblPosCaret = new javax.swing.JLabel();
        lblNomeArquivo = new javax.swing.JLabel();
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
        mnuExibir = new javax.swing.JMenu();
        mitAumentarZoom = new javax.swing.JMenuItem();
        mitDiminuirZoom = new javax.swing.JMenuItem();
        mitRestaurarZoom = new javax.swing.JMenuItem();
        mnuVerificar = new javax.swing.JMenu();
        mitVerificarAlgoritmo = new javax.swing.JMenuItem();
        mitExibirErros = new javax.swing.JMenuItem();
        mnuExecutar = new javax.swing.JMenu();
        mitProximoPasso = new javax.swing.JMenuItem();
        mitPararExec = new javax.swing.JMenuItem();
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

        scrTxpIde.setVerifyInputWhenFocusTarget(false);
        scrTxpIde.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                scrTxpIdeMouseWheelMoved(evt);
            }
        });

        txpIde.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        txpIde.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txpIdeCaretUpdate(evt);
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
        scrTxpIde.setViewportView(txpIde);

        scrTxpIde.setRowHeaderView(new TextLineNumber(txpIde, 3));

        btnProxPerc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-next-arrow-24.png"))); // NOI18N
        btnProxPerc.setText("Próximo passo");
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

        btnEntradaConfirma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-check-sign-16.png"))); // NOI18N
        btnEntradaConfirma.setText("Confirmar");
        btnEntradaConfirma.setEnabled(false);
        btnEntradaConfirma.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnEntradaConfirma.setMaximumSize(new java.awt.Dimension(115, 25));
        btnEntradaConfirma.setMinimumSize(new java.awt.Dimension(105, 25));
        btnEntradaConfirma.setPreferredSize(new java.awt.Dimension(110, 25));
        btnEntradaConfirma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntradaConfirmaActionPerformed(evt);
            }
        });

        btnVerificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-check1-24.png"))); // NOI18N
        btnVerificar.setText("Verificar algoritmo");
        btnVerificar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarActionPerformed(evt);
            }
        });

        lblVariavelEntrada.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

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

        lblFontSize.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lblFontSize.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        btnPararExec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-stop-sign-24.png"))); // NOI18N
        btnPararExec.setText("Parar");
        btnPararExec.setEnabled(false);
        btnPararExec.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnPararExec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPararExecActionPerformed(evt);
            }
        });

        pnlStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblPosCaret.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        lblPosCaret.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblPosCaret.setText("Linha 1, Coluna 1");

        lblNomeArquivo.setText("Algoritmo");

        javax.swing.GroupLayout pnlStatusLayout = new javax.swing.GroupLayout(pnlStatus);
        pnlStatus.setLayout(pnlStatusLayout);
        pnlStatusLayout.setHorizontalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStatusLayout.createSequentialGroup()
                .addComponent(lblNomeArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPosCaret, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlStatusLayout.setVerticalGroup(
            pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatusLayout.createSequentialGroup()
                .addGroup(pnlStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPosCaret)
                    .addComponent(lblNomeArquivo))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        mnuArquivo.setText("Arquivo");

        mitNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-new-doc-16.png"))); // NOI18N
        mitNovo.setText("Novo");
        mitNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNovoActionPerformed(evt);
            }
        });
        mnuArquivo.add(mitNovo);

        mitAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mitAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-open-folder-16.png"))); // NOI18N
        mitAbrir.setText("Abrir");
        mitAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAbrirActionPerformed(evt);
            }
        });
        mnuArquivo.add(mitAbrir);

        mitSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-save-diskette-16.png"))); // NOI18N
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

        mitSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-exit-door-16.png"))); // NOI18N
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
        mitDesfazer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-undo-arrow-16.png"))); // NOI18N
        mitDesfazer.setText("Desfazer");
        mitDesfazer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDesfazerActionPerformed(evt);
            }
        });
        mnuEditar.add(mitDesfazer);

        mitRefazer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        mitRefazer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-redo-arrow-16.png"))); // NOI18N
        mitRefazer.setText("Refazer");
        mitRefazer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRefazerActionPerformed(evt);
            }
        });
        mnuEditar.add(mitRefazer);

        jMenuBar1.add(mnuEditar);

        mnuExibir.setText("Exibir");

        mitAumentarZoom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, java.awt.event.InputEvent.CTRL_MASK));
        mitAumentarZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-magn-zoomin-16.png"))); // NOI18N
        mitAumentarZoom.setText("Aumentar fonte");
        mitAumentarZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAumentarZoomActionPerformed(evt);
            }
        });
        mnuExibir.add(mitAumentarZoom);

        mitDiminuirZoom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
        mitDiminuirZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-magn-zoomout-16.png"))); // NOI18N
        mitDiminuirZoom.setText("Diminuir fonte");
        mitDiminuirZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDiminuirZoomActionPerformed(evt);
            }
        });
        mnuExibir.add(mitDiminuirZoom);

        mitRestaurarZoom.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_0, java.awt.event.InputEvent.CTRL_MASK));
        mitRestaurarZoom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-magn-zoom-16.png"))); // NOI18N
        mitRestaurarZoom.setText("Restaurar tam. fonte");
        mitRestaurarZoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRestaurarZoomActionPerformed(evt);
            }
        });
        mnuExibir.add(mitRestaurarZoom);

        jMenuBar1.add(mnuExibir);

        mnuVerificar.setText("Verificar");

        mitVerificarAlgoritmo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-check1-16.png"))); // NOI18N
        mitVerificarAlgoritmo.setText("Verificar algoritmo");
        mitVerificarAlgoritmo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitVerificarAlgoritmoActionPerformed(evt);
            }
        });
        mnuVerificar.add(mitVerificarAlgoritmo);

        mitExibirErros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-error-alert-16.png"))); // NOI18N
        mitExibirErros.setText("Exibir erros da última verificação");
        mitExibirErros.setEnabled(false);
        mitExibirErros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExibirErrosActionPerformed(evt);
            }
        });
        mnuVerificar.add(mitExibirErros);

        jMenuBar1.add(mnuVerificar);

        mnuExecutar.setText("Executar");
        mnuExecutar.setEnabled(false);

        mitProximoPasso.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, 0));
        mitProximoPasso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-next-arrow-16.png"))); // NOI18N
        mitProximoPasso.setText("Próximo passo");
        mitProximoPasso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitProximoPassoActionPerformed(evt);
            }
        });
        mnuExecutar.add(mitProximoPasso);

        mitPararExec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mitPararExec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-stop-sign-16.png"))); // NOI18N
        mitPararExec.setText("Parar execução");
        mitPararExec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPararExecActionPerformed(evt);
            }
        });
        mnuExecutar.add(mitPararExec);

        jMenuBar1.add(mnuExecutar);

        mnuAjuda.setText("Ajuda");

        mitSobre.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fatec/alg/gui/imagens/icon-info-16.png"))); // NOI18N
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrTxpIde)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEntradaConfirma, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVariavelEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(lblFontSize)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVerificar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnProxPerc, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPararExec, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 75, Short.MAX_VALUE))
                    .addComponent(pnlStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(lblLogo)
                    .addComponent(lblLogo1)
                    .addComponent(btnVerificar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnPararExec, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                        .addComponent(btnProxPerc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblFontSize))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(scrTxpIde))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblVariavelEntrada))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
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
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
        
    private void btnProxPercActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProxPercActionPerformed
        boolean enabled;
        
        if (formatacao != FORMAT_PLAIN && formatacao != FORMAT_PERC){
            docIde.setCharacterAttributes(0, docIde.getLength(), stylePlain, true);
            formatacao = FORMAT_PERC;
        }
        if (interpreter == null) {
            enabled = false;
        } else {
            interpreter.proxima();
            enabled = interpreter.existeProxima();
        }
        btnProxPerc.setEnabled(enabled && !btnEntradaConfirma.isEnabled());
        btnPararExec.setEnabled(enabled);
        mnuExecutar.setEnabled(enabled);
        emExecucao = enabled;
        if (!enabled) {
            JOptionPane.showMessageDialog(this, "Execução finalizada", "Execução finalizada", JOptionPane.INFORMATION_MESSAGE);
            pararExecucao();
        }
        
    }//GEN-LAST:event_btnProxPercActionPerformed

    private void btnVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarActionPerformed
        if (!pararExecucao("Verificar e reiniciar")) return;
        pararExecucao();
        
        oldText = txpIde.getText();
        
        if (oldText.isEmpty()){
            JOptionPane.showMessageDialog(this, "Nada para verificar - algoritmo vazio", "Verificação concluída", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        AnalisadorLexico scanner = new AnalisadorLexico(oldText);
        List<Token> tokens = scanner.listaTokens();
        
        TipoErro erroNivelMax = TipoErro.getMax();
        
        int numErros = scanner.getNumErros(erroNivelMax);
        if (scanner.getNumErros(TipoErro.getMax()) > 0){
            JOptionPane.showMessageDialog(this,
                    String.format("%d erro%s encontrado%s - verifique seu algoritmo",
                        numErros,
                        numErros == 1 ? "" : "s",
                        numErros == 1 ? "" : "s"
                    ),
                    "Verificação - Análise Léxica", JOptionPane.ERROR_MESSAGE);
            formatacao = FORMAT_ERROR;
            logger.log(Level.WARNING, "SCANNER (Análise Léxica)");
            
            for (Erro e : scanner.getErros(TipoErro.getMax())){
                Token t = e.getToken();
                logger.log(Level.WARNING, e.toString());
                docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleError, true);
            }
            
            mitExibirErros.setEnabled(true);
            frmListaErros.setListaErros(scanner.getErros(TipoErro.ALERTA));
            frmListaErros.setVisible(true);
            
        } else {
            AnalisadorSintatico parser = new AnalisadorSintatico(tokens);
            LinkedList<Instrucao> instrucoes = parser.listaInstrucoes();

            numErros = parser.getNumErros(TipoErro.getMax());
            if (numErros > 0){
                JOptionPane.showMessageDialog(this,
                        String.format("%d erro%s encontrado%s - verifique seu algoritmo",
                            numErros,
                            numErros == 1 ? "" : "s",
                            numErros == 1 ? "" : "s"
                        ),
                        "Verificação - Análise Sintática", JOptionPane.ERROR_MESSAGE);
                formatacao = FORMAT_ERROR;
                logger.log(Level.WARNING, "PARSER (Análise Sintática)");
                for (Erro e : parser.getErros(TipoErro.getMax())){
                    Token t = e.getToken();
                    docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleError, true);
                    logger.log(Level.WARNING, e.toString());
                }
                
                mitExibirErros.setEnabled(true);
                frmListaErros.setListaErros(parser.getErros(TipoErro.getMax()));
                frmListaErros.setVisible(true);
                
            } else {
                
                PreProcessador processor = new PreProcessador(instrucoes);
                processor.verificaPrograma();
                
                numErros = processor.getNumErros(TipoErro.INFO);
                int numErrSemant = processor.getNumErros(TipoErro.ERRO);
                int numInfos = numErros - numErrSemant;
                
                if (numErrSemant > 0){
                    JOptionPane.showMessageDialog(this,
                        String.format("%d erro%s encontrado%s - verifique seu algoritmo",
                            numErrSemant,
                            numErrSemant == 1 ? "" : "s",
                            numErrSemant == 1 ? "" : "s"
                        ),
                        "Verificação - Análise Semântica", JOptionPane.ERROR_MESSAGE);
                    formatacao = FORMAT_ERROR;
                    logger.log(Level.WARNING, "PRE PROCESSOR (Análise Semântica)");
                    for (Erro e : processor.getErros(TipoErro.ERRO)){
                        Token t = e.getToken();
                        docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleError, true);
                        logger.log(Level.WARNING, e.toString());
                    }
                    mitExibirErros.setEnabled(true);
                    frmListaErros.setListaErros(processor.getErros(TipoErro.ALERTA));
                    frmListaErros.setVisible(true);
                } else {
                    if (numInfos > 0) {
                        for (Erro e : processor.getErros(TipoErro.INFO)){
                            Token t = e.getToken();
                            if (e.getTipo() == TipoErro.ALERTA) {
                                docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleWarn, true);
                            }
                            if (e.getTipo() == TipoErro.INFO) {
                                docIde.setCharacterAttributes(t.getPosicao(), t.getTamanho(), styleInfo, true);
                            }
                        }
                        formatacao = FORMAT_ERROR;
                        
                        boolean cancelar = false;
                        if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(this,
                                String.format("%d alerta%s encontrado%s."
                                        + "\nO programa pode ser executado, mas erros podem ocorrer."
                                        + "\nDeseja executar mesmo assim?",
                                    numInfos,
                                    numInfos == 1 ? "" : "s",
                                    numInfos == 1 ? "" : "s"
                                ),
                                "Verificação - Análise Semântica", 
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE)) {
                            
                            cancelar = true;
                        }
                        
                        mitExibirErros.setEnabled(true);
                        frmListaErros.setListaErros(processor.getErros(TipoErro.INFO));
                        frmListaErros.setVisible(true);
                        
                        if (cancelar) return;
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Nenhum erro encontrado - pronto para execução",
                                "Verificação concluída",
                                JOptionPane.INFORMATION_MESSAGE);
                        mitExibirErros.setEnabled(false);
                        if (frmListaErros.isVisible()) {
                            frmListaErros.setVisible(false);
                        }
                    }
                    
                    lblPosCaret.setText("Em execução");
                    emExecucao = true;

                    interpreter.setPrograma(processor.getPrograma());
                    btnProxPerc.setEnabled(interpreter.existeProxima());
                    btnPararExec.setEnabled(interpreter.existeProxima());
                    mnuExecutar.setEnabled(interpreter.existeProxima());
                    txpIde.setEditable(false);
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
            if (MAX_ALTERACOES > 0 && alteracoesAnt.size() >= MAX_ALTERACOES) {
                alteracoesAnt.poll();
            } 
            alteracoesAnt.push(oldText);
            
            String nomeArq = lblNomeArquivo.getText();
            if (!nomeArq.endsWith(" *")) {
                lblNomeArquivo.setText(nomeArq + " *");
                lblNomeArquivo.setFont(lblNomeArquivo.getFont().deriveFont(Font.BOLD));
                lblNomeArquivo.setToolTipText("Algoritmo com alterações não salvas");
            }
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
                    txpIde.paste();
                    String text = txpIde.getText();
                    int pos = txpIde.getCaretPosition();
                    int dif = text.length();

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
                    
                    txpIde.setText(text);
                    txpIde.setCaretPosition(pos + text.length() - dif);
                    
                }
                break;
            }
            default:
                break;
        }
    }//GEN-LAST:event_txpIdeKeyPressed

    private void txpIdeCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txpIdeCaretUpdate
        if (!emExecucao) {
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
        }
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
            abreArquivo();
        }
    }//GEN-LAST:event_mitAbrirActionPerformed

    private void mitSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSalvarActionPerformed
        if (arquivo == null){
            String caminho = System.getProperty("user.home") + File.separator + "algoritmo.alg";
            arquivo = new File(caminho);
            
            mitSalvarComoActionPerformed(evt);
        } else {
            salvaArquivo();
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
            if (MAX_ALTERACOES > 0 && alteracoesProx.size() >= MAX_ALTERACOES) {
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
            if (MAX_ALTERACOES > 0 && alteracoesAnt.size() >= MAX_ALTERACOES) {
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
        ideFontTamanhoRestaura();
        docIde.setCharacterAttributes(0, atual.length(), stylePlain, true);
        textoOrig = "";
        txpIde.setText("");
        oldText = "";
    }//GEN-LAST:event_mitNovoActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        encerra();
    }//GEN-LAST:event_formWindowClosing

    private void mitAumentarZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAumentarZoomActionPerformed
        ideFontTamanhoAltera(2.0F);
    }//GEN-LAST:event_mitAumentarZoomActionPerformed

    private void mitDiminuirZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDiminuirZoomActionPerformed
        ideFontTamanhoAltera(-2.0F);
    }//GEN-LAST:event_mitDiminuirZoomActionPerformed

    private void mitRestaurarZoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRestaurarZoomActionPerformed
        ideFontTamanhoRestaura();
    }//GEN-LAST:event_mitRestaurarZoomActionPerformed

    private void scrTxpIdeMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_scrTxpIdeMouseWheelMoved
        int pontos = evt.getWheelRotation();
        boolean wheelUp = false;
        if (pontos < 0) {
            wheelUp = true;
            pontos = -pontos;
        }
        if (evt.isControlDown()) {
            evt.consume();
            for (int pnt = 0; pnt < pontos; pnt ++) {
                
                if (wheelUp) {
                    ideFontTamanhoAltera(2.0F);
                } else {
                    ideFontTamanhoAltera(-2.0F);
                }
            } 
        }
    }//GEN-LAST:event_scrTxpIdeMouseWheelMoved

    private void mitPararExecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPararExecActionPerformed
        if (btnPararExec.isEnabled()) {
            btnPararExecActionPerformed(evt);
        }
    }//GEN-LAST:event_mitPararExecActionPerformed

    private void btnPararExecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPararExecActionPerformed
        if (pararExecucao("Parar execução")) {
            txpIde.requestFocus();
        }
    }//GEN-LAST:event_btnPararExecActionPerformed
    
    private void ideFontTamanhoAltera(float size) {
        Font font = txpIde.getFont();
        size = font.getSize2D() + size;
        font = font.deriveFont(size);
        txpIde.setFont(font);
        if ((int)size != (int)defaultFontSize) {
            int zoom = (int)(size * 100 / defaultFontSize);
            lblFontSize.setText("Tamanho da fonte: " + zoom + "%");
        } else {
            lblFontSize.setText("");
        }
    }
    
    private void ideFontTamanhoRestaura() {
        Font font = txpIde.getFont();
        font = font.deriveFont(defaultFontSize);
        lblFontSize.setText("");
        txpIde.setFont(font);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntradaConfirma;
    private javax.swing.JButton btnPararExec;
    private javax.swing.JButton btnProxPerc;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnVerificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel lblFontSize;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblLogo1;
    private javax.swing.JLabel lblNomeArquivo;
    private javax.swing.JLabel lblPosCaret;
    private javax.swing.JLabel lblVariavelEntrada;
    private javax.swing.JMenuItem mitAbrir;
    private javax.swing.JMenuItem mitAumentarZoom;
    private javax.swing.JMenuItem mitDesfazer;
    private javax.swing.JMenuItem mitDiminuirZoom;
    private javax.swing.JMenuItem mitExibirErros;
    private javax.swing.JMenuItem mitNovo;
    private javax.swing.JMenuItem mitPararExec;
    private javax.swing.JMenuItem mitProximoPasso;
    private javax.swing.JMenuItem mitRefazer;
    private javax.swing.JMenuItem mitRestaurarZoom;
    private javax.swing.JMenuItem mitSair;
    private javax.swing.JMenuItem mitSalvar;
    private javax.swing.JMenuItem mitSalvarComo;
    private javax.swing.JMenuItem mitSobre;
    private javax.swing.JMenuItem mitVerificarAlgoritmo;
    private javax.swing.JMenu mnuAjuda;
    private javax.swing.JMenu mnuArquivo;
    private javax.swing.JMenu mnuEditar;
    private javax.swing.JMenu mnuExecutar;
    private javax.swing.JMenu mnuExibir;
    private javax.swing.JMenu mnuVerificar;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JScrollPane scrTxpIde;
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
    public void finalizaExpressao() {
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
        txpProcessamento.setBackground(backgroundEnabled);
        if (impressoraExpressao == null) {
            impressoraExpressao = new ExibidorExpressao(expressao);
        } else {
            impressoraExpressao.setExpressaoAtual(expressao);
        }
        txpProcessamento.setText(impressoraExpressao.getTexto());
        docProc.setCharacterAttributes(0, txpProcessamento.getText().length(), stylePlain, true);

        Token tokenExpr = impressoraExpressao.getTokenExprAtual();
        Token tokenRes = impressoraExpressao.getTokenResultado();
        docProc.setCharacterAttributes(tokenExpr.getPosicao(), tokenExpr.getTamanho(), styleOper, true);
        if (impressoraExpressao.hasTokenResultado()) {
            docProc.setCharacterAttributes(tokenRes.getPosicao(), tokenRes.getTamanho(), styleExpr, false);
        }
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
        pararExecucao();
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
        
        return pararExecucao();
    }
    
    private boolean pararExecucao() {
        emExecucao = false;
        btnProxPerc.setEnabled(false);
        btnPararExec.setEnabled(false);
        mnuExecutar.setEnabled(false);
        btnEntradaConfirma.setEnabled(false);
        
        limpaTokensPercurso();
        
        tabVariaveis.setRowCount(0);

        txpEntrada.setText("");
        txpEntrada.setEditable(false);
        txpEntrada.setBackground(backgroundDisabled);
        lblVariavelEntrada.setText("");
        
        docProc.setCharacterAttributes(0, docProc.getLength(), stylePlain, true);
        formatacao = FORMAT_PLAIN;
        txpProcessamento.setText("");
        txpProcessamento.setBackground(backgroundDisabled);

        txpSaida.setText("");
        txpSaida.setBackground(backgroundDisabled);
        
        txpIde.setEditable(true);
        lblPosCaret.setText("Finalizado");
        
        interpreter.reinicia();
        return true;
    }
    
    private void encerra() {
        pararExecucao();
        
        String atual = txpIde.getText();
        if (!textoOrig.equals(atual)) {
            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja salvar o seu código atual antes de sair?",
                    "Fechar programa",
                    JOptionPane.YES_NO_OPTION);
            
            if (opcao == JOptionPane.OK_OPTION) {
                mitSalvarActionPerformed(null);
            } 
            if (opcao == JOptionPane.CANCEL_OPTION || opcao == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }
        System.exit(0);
    }
}
