package fatec.alg.geral.log;

/**
 * Classe de enumeração de tipos de registros (logs) possíveis.
 * @author Caique Souza
 */
public enum TipoErro {
    
    /**
     * Erro de análise que impede que o código seja executado totalmente.
     */
    ERRO ("Erro", 1),
    
    /**
     * Alertas que podem causar resultados incorretos ou comandos ignorados,
     * mas não impedem a execução do código como um todo.
     */
    ALERTA ("Alerta", 2),
    
    /**
     * Informação básica que não indica nenhum problema de análise, ou pequenos
     * avisos que não causam interrupção alguma no código.
     */
    INFO ("Info", 3);
    
    
    /**
     * Retorna o tipo de registro padrão.
     * @return ALERTA
     */
    public static TipoErro getDefault() {
        return ALERTA;
    }
    
    /**
     * Retorna o tipo de registro com nível maior (menor gravidade).
     * @return 
     */
    public static TipoErro getMax() {
        TipoErro max = null;
        for (TipoErro tipo : TipoErro.values()) {
            if (max == null) {
                max = tipo;
            } else {
                if (tipo.nivel > max.nivel) {
                    max = tipo;
                }
            }
        }
        return max;
    }
    
    
    private final String nome;
    private final int nivel;

    private TipoErro(String nome, int nivel) {
        this.nivel = nivel;
        this.nome = nome;
    }

    /**
     * Retorna o nome (descrição) do tipo de registro.
     * @return Nome.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o nível do tipo de registro. Esse nível indica a prioridade
     * ou gravidade do registro - quanto mais próximo de zero, mais grave.
     * @return 
     */
    public int getNivel() {
        return nivel;
    }
    
    /**
     * Retorna o nome em maiúsculas.
     * @return 
     */
    @Override
    public String toString() {
        return nome.toUpperCase();
    }
}
