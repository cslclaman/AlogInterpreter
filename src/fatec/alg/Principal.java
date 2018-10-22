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
    public static final String VERSAO_NUM = "1.0.0";
    
    /**
     * Data de lançamento da versão atual
     */
    public static final String VERSAO_DATA = "Out/2018";
    
    /**
     * Número identificador da compilação atual
     */
    public static final int VERSAO_BUILD = 3;
    
    /**
     * Método principal de execução da aplicação.
     * Define o tema da interface do Swing.
     * 
     * @param args argumentos da linha de comando.
     */
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Principal.class.getName());
        
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
