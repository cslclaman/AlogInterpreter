/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduacao do curso de Analise e Desenvolvimento de Sistemas
 * Autor: Caique de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */
package fatec.alg.util.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 *
 * @author Caique Siqueira
 */
public class CustomLogHandler extends StreamHandler {
    
    public CustomLogHandler(Formatter formatter) {
        super(System.out, formatter);
    }
        
    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }
}
