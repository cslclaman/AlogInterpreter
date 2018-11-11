/*
 * Fatec Sorocaba - Jose Crespo Gonzales
 * Interpretador de Algoritmos
 * Trabalho de Graduacao do curso de Analise e Desenvolvimento de Sistemas
 * Autor: Caique de Souza Lima Siqueira
 * (caique.siqueira@fatec.sp.gov.br)
 * Orientador: Prof. Dimas Ferreira Cardoso
 */
package fatec.alg.geral.config;

import java.util.HashMap;

/**
 * Classe de gerenciamento de configurações para componentes do sistema.
 * @author Caique Souza
 */
public abstract class Configuracao {

    protected HashMap<String, Object> configs;
    
    protected Configuracao() {
        configs = new HashMap<>();
    }
    
    /**
     * Retorna o valor de um parâmetro booleano de configuração.
     * @param key Chave do valor a obter
     * @return valor, ou FALSE caso não exista ou não seja boolean.
     */
    public boolean getBoolean(String key){
        if (configs.containsKey(key) && configs.get(key) instanceof Boolean ) {
            return (boolean) configs.get(key);
        } else {
            return false;
        }
    }
    
    /**
     * Retorna o valor de um parâmetro texto de configuração.
     * @param key Chave do valor a obter
     * @return valor, ou "" (String vazia) caso não exista ou não seja String.
     */
    public String getString(String key){
        if (configs.containsKey(key) && configs.get(key) instanceof String ) {
            return (String) configs.get(key);
        } else {
            return "";
        }
    }
    
    /**
     * Define o valor associado a uma chave de identificação.
     * Se não existir, cria. Se existir, substitui. Se o valor for nulo,
     * deleta a propriedade.
     * @param key Chave
     * @param valor Objeto com o valor a definir.
     */
    public final void set (String key, Object valor) {
        if (valor != null) {
            configs.put(key, valor);
        } else {
            configs.remove(key);
        }
    }
    
}
