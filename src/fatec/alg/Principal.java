/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduacao para o curso de Analise e Desenvolvimento de Sistemas
 * Autor: Caique de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */

package fatec.alg;

import fatec.alg.gui.Console;
import fatec.alg.gui.FrmGui;
import fatec.alg.util.log.CustomLogHandler;
import fatec.alg.util.log.SilentFormatter;
import fatec.alg.util.log.VerboseFormatter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Classe de execução do projeto.
 * @author Caique Souza
 * @since 1.0.0 build 1
 */
public class Principal {

    /**
     * Número da versão atual
     */
    public static final String VERSAO_NUM = "1.0.1";
    
    /**
     * Data de lançamento da versão atual
     */
    public static final String VERSAO_DATA = "Out/2018";
    
    /**
     * Número identificador da compilação atual
     */
    public static final int VERSAO_BUILD = 4;
    
    private static final Logger logger = Logger.getLogger(Principal.class.getName());
    
    /**
     * Método principal de execução da aplicação.<br>
     * Permite os seguintes argumentos:
     * <ul>
     * <li><b>-debuglog</b>: log completo, incluindo classe e método que emitiu</li>
     * <li><b>-console</b>: execução via console (textual)</li>
     * <li><b>-filename="&lt;CAMINHO&gt;"</b>: passa um caminho de arquivo (txt/alg) a ser aberto</li>
     * </ul>
     * 
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        boolean logInfo = false;
        boolean console = false;
        String filename = "";
        
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.equals("-debuglog")) {
                    logInfo = true;
                }
                if (arg.equals("-console")) {
                    console = true;
                }
                if (arg.startsWith("-filename=")) {
                    filename = arg
                        .replace("-filename=", "")
                        .replace("'", "")
                        .replace("\"", "");
                }
            }
        }
        
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.setLevel(Level.INFO);
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }
        Formatter formatter;
        if (logInfo) {
            formatter = new VerboseFormatter();
        } else {
            formatter = new SilentFormatter();
        }
        rootLogger.addHandler(new CustomLogHandler(formatter));
        
        logger.log(Level.INFO, "Interpretador de Algoritmos - Versao {0}, build {1} ({2}) - INICIADO",
                new Object[]{VERSAO_NUM, VERSAO_BUILD, VERSAO_DATA});
        
        if (console) {
            if (!logInfo) {
                for (Handler h : rootLogger.getHandlers()) {
                    h.setFormatter(new Formatter() {
                        @Override
                        public String format(LogRecord record) {
                            return "";
                        }
                    });
                }
            }
            Console cmd = new Console(filename);
            java.awt.EventQueue.invokeLater(() -> {
                cmd.executa();
            });
        } else {
            try {
                String theme;
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    theme = "Windows";
                } else {
                    theme = "Metal";
                }

                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if (info.getName().equals(theme)) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (
                    ClassNotFoundException | 
                    InstantiationException | 
                    IllegalAccessException | 
                    javax.swing.UnsupportedLookAndFeelException ex) {

                logger.log(java.util.logging.Level.SEVERE, null, ex);
            }
            FrmGui frmGui = new FrmGui(filename);
            java.awt.EventQueue.invokeLater(() -> {
                frmGui.setVisible(true);
            });
        }
    }
    
}
