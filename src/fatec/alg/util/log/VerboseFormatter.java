/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduacao do curso de Analise e Desenvolvimento de Sistemas
 * Autor: Caique de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */
package fatec.alg.util.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Caique Souza
 */
public class VerboseFormatter extends SimpleFormatter {
    private static final String format = "[%-11s - %19s] %s - %s\n%s\n";
        
    @Override
    public synchronized String format(LogRecord lr) {
        return String.format(format,
                lr.getLevel().getLocalizedName(),
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date(lr.getMillis())),
                lr.getSourceClassName(),
                lr.getSourceMethodName(),
                formatMessage(lr)
        );
    }
}
