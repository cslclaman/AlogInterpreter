/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduacao para o curso de Analise e Desenvolvimento de Sistemas
 * Autor: Caique de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */

package fatec.alg;

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
     * Método principal de execução da aplicação.
     * Define o tema da interface do Swing.
     * 
     * @param args argumentos da linha de comando.
     */
    public static void main(String[] args) {
        boolean logInfo = false;
        
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.equals("-debuglog")) {
                    logInfo = true;
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
        java.awt.EventQueue.invokeLater(() -> {
            new FrmGui().setVisible(true);
        });
    }
    
}
