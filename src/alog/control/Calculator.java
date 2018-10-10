/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.expressao.Operando;
import alog.model.TipoDado;
import alog.model.Variavel;

/**
 *
 * @author Caique Souza
 */
public class Calculator {
    public static final String TRUE = "verdadeiro";
    public static final String FALSE = "falso";
    
    private TipoDado tipo;
    private String valor;
    private String caracter;
    private Long inteiro;
    private Double real;
    private Boolean logico;

    public Calculator(TipoDado tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
    
    public Calculator(Variavel var) {
        this.tipo = var.getTipo();
        this.valor = var.getValor();
    }
    
    public Calculator positivo() {
        try {
            switch (this.tipo) {
                case INTEIRO: {
                    long a = Long.parseLong(this.valor);
                    return new Calculator(this.tipo, String.valueOf( (+a) ));
                }
                case REAL: {
                    double a = Double.parseDouble(valor);
                    return new Calculator(this.tipo, String.valueOf( (+a) ));
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator negativo() {
        try {
            switch (this.tipo) {
                case INTEIRO: {
                    long a = Long.parseLong(this.valor);
                    return new Calculator(this.tipo, String.valueOf( (-a) ));
                }
                case REAL: {
                    double a = Double.parseDouble(valor);
                    return new Calculator(this.tipo, String.valueOf( (-a) ));
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator soma(Calculator c) {
        TipoDado tipoR;
        try {
            switch (this.tipo) {
                case INTEIRO: {
                    long a = Long.parseLong(this.valor);
                    if (c.tipo == TipoDado.INTEIRO) {
                        tipoR = TipoDado.INTEIRO;
                        long b = Long.parseLong(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a + b) ));
                    } else {
                        tipoR = TipoDado.REAL;
                        double b = Double.parseDouble(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a + b) ));
                    }
                }
                case REAL: {
                    tipoR = TipoDado.REAL;
                    double a = Double.parseDouble(valor);
                    if (c.tipo == TipoDado.INTEIRO) {
                        long b = Long.parseLong(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a + b) ));
                    } else {
                        double b = Double.parseDouble(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a + b) ));
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator subtr(Calculator c) {
        TipoDado tipoR;
        try {
            switch (this.tipo) {
                case INTEIRO: {
                    long a = Long.parseLong(this.valor);
                    if (c.tipo == TipoDado.INTEIRO) {
                        tipoR = TipoDado.INTEIRO;
                        long b = Long.parseLong(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a - b) ));
                    } else {
                        tipoR = TipoDado.REAL;
                        double b = Double.parseDouble(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a - b) ));
                    }
                }
                case REAL: {
                    tipoR = TipoDado.REAL;
                    double a = Double.parseDouble(valor);
                    if (c.tipo == TipoDado.INTEIRO) {
                        long b = Long.parseLong(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a - b) ));
                    } else {
                        double b = Double.parseDouble(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a - b) ));
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator mult(Calculator c) {
        TipoDado tipoR;
        try {
            switch (this.tipo) {
                case INTEIRO: {
                    long a = Long.parseLong(this.valor);
                    if (c.tipo == TipoDado.INTEIRO) {
                        tipoR = TipoDado.INTEIRO;
                        long b = Long.parseLong(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a * b) ));
                    } else {
                        tipoR = TipoDado.REAL;
                        double b = Double.parseDouble(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a * b) ));
                    }
                }
                case REAL: {
                    tipoR = TipoDado.REAL;
                    double a = Double.parseDouble(valor);
                    if (c.tipo == TipoDado.INTEIRO) {
                        long b = Long.parseLong(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a * b) ));
                    } else {
                        double b = Double.parseDouble(c.valor);
                        return new Calculator(tipoR, String.valueOf( (a * b) ));
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator divReal(Calculator c) {
        TipoDado tipoR = TipoDado.REAL;
        try {
            switch (this.tipo) {
                case INTEIRO: 
                case REAL: {
                    double a = Double.parseDouble(this.valor);
                    if (c.tipo == TipoDado.INTEIRO || c.tipo == TipoDado.REAL) {
                        double b = Double.parseDouble(c.valor);
                        if (b == 0) {
                            return null;
                        } else {
                            return new Calculator(tipoR, String.valueOf( (a / b) ));
                        }
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator divInteira(Calculator c) {
        TipoDado tipoR = TipoDado.INTEIRO;
        try {
            switch (this.tipo) {
                case INTEIRO: {
                    long a = Long.parseLong(this.valor);
                    if (c.tipo == TipoDado.INTEIRO) {
                        long b = Long.parseLong(c.valor);
                        if (b == 0) {
                            return null;
                        } else {
                            return new Calculator(tipoR, String.valueOf( (a / b) ));
                        }
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator mod(Calculator c) {
        TipoDado tipoR = TipoDado.INTEIRO;
        try {
            switch (this.tipo) {
                case INTEIRO: {
                    long a = Long.parseLong(this.valor);
                    if (c.tipo == TipoDado.INTEIRO) {
                        long b = Long.parseLong(c.valor);
                        if (b == 0) {
                            return null;
                        } else {
                            return new Calculator(tipoR, String.valueOf( (a % b) ));
                        }
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator maior(Calculator c) {
        TipoDado tipoR = TipoDado.LOGICO;
        try {
            switch (this.tipo) {
                case INTEIRO: 
                case REAL: {
                    double a = Double.parseDouble(this.valor);
                    if (c.tipo == TipoDado.INTEIRO || c.tipo == TipoDado.REAL) {
                        double b = Double.parseDouble(c.valor);
                        String result = a > b ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                case CARACTER: {
                    if (c.tipo == TipoDado.CARACTER) {
                        String result = this.valor.compareTo(c.valor) > 0 ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator menor(Calculator c) {
        TipoDado tipoR = TipoDado.LOGICO;
        try {
            switch (this.tipo) {
                case INTEIRO: 
                case REAL: {
                    double a = Double.parseDouble(this.valor);
                    if (c.tipo == TipoDado.INTEIRO || c.tipo == TipoDado.REAL) {
                        double b = Double.parseDouble(c.valor);
                        String result = a < b ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                case CARACTER: {
                    if (c.tipo == TipoDado.CARACTER) {
                        String result = this.valor.compareTo(c.valor) < 0 ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator maiorIgual(Calculator c) {
        TipoDado tipoR = TipoDado.LOGICO;
        try {
            switch (this.tipo) {
                case INTEIRO: 
                case REAL: {
                    double a = Double.parseDouble(this.valor);
                    if (c.tipo == TipoDado.INTEIRO || c.tipo == TipoDado.REAL) {
                        double b = Double.parseDouble(c.valor);
                        String result = a >= b ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                case CARACTER: {
                    if (c.tipo == TipoDado.CARACTER) {
                        String result = this.valor.compareTo(c.valor) >= 0 ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator menorIgual(Calculator c) {
        TipoDado tipoR = TipoDado.LOGICO;
        try {
            switch (this.tipo) {
                case INTEIRO: 
                case REAL: {
                    double a = Double.parseDouble(this.valor);
                    if (c.tipo == TipoDado.INTEIRO || c.tipo == TipoDado.REAL) {
                        double b = Double.parseDouble(c.valor);
                        String result = a <= b ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                case CARACTER: {
                    if (c.tipo == TipoDado.CARACTER) {
                        String result = this.valor.compareTo(c.valor) <= 0 ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator igual(Calculator c) {
        TipoDado tipoR = TipoDado.LOGICO;
        try {
            switch (this.tipo) {
                case INTEIRO: 
                case REAL: {
                    double a = Double.parseDouble(this.valor);
                    if (c.tipo == TipoDado.INTEIRO || c.tipo == TipoDado.REAL) {
                        double b = Double.parseDouble(c.valor);
                        String result = a == b ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                case CARACTER: {
                    if (c.tipo == TipoDado.CARACTER) {
                        String result = this.valor.equals(c.valor) ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                case LOGICO: {
                    if (c.tipo == TipoDado.LOGICO) {
                        String result = this.valor.equals(c.valor) ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
        return null;
    }
    
    public Calculator diferente(Calculator c) {
        TipoDado tipoR = TipoDado.LOGICO;
        try {
            switch (this.tipo) {
                case INTEIRO: 
                case REAL: {
                    double a = Double.parseDouble(this.valor);
                    if (c.tipo == TipoDado.INTEIRO || c.tipo == TipoDado.REAL) {
                        double b = Double.parseDouble(c.valor);
                        String result = a != b ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                case CARACTER: {
                    if (c.tipo == TipoDado.CARACTER) {
                        String result = !this.valor.equals(c.valor) ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                case LOGICO: {
                    if (c.tipo == TipoDado.LOGICO) {
                        String result = !this.valor.equals(c.valor) ? TRUE : FALSE;
                        return new Calculator(tipoR, result);
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        } catch (NumberFormatException ex) {
            return null;
        }
    }
    
    public Calculator e(Calculator c) {
        TipoDado tipoR = TipoDado.LOGICO;
        if (this.tipo == TipoDado.LOGICO && c.tipo == this.tipo) {
            boolean res = this.valor.equals(TRUE) && c.valor.equals(TRUE);
            return new Calculator(tipoR, (res ? TRUE : FALSE) );
        } else{
            return null;
        }
    }
    
    public Calculator ou(Calculator c) {
        TipoDado tipoR = TipoDado.LOGICO;
        if (this.tipo == TipoDado.LOGICO && c.tipo == this.tipo) {
            boolean res = this.valor.equals(TRUE) || c.valor.equals(TRUE);
            return new Calculator(tipoR, (res ? TRUE : FALSE) );
        } else{
            return null;
        }
    }
    
    public Calculator nao() {
        TipoDado tipoR = TipoDado.LOGICO;
        if (this.tipo == TipoDado.LOGICO) {
            String res = this.valor.equals(TRUE) ? FALSE : TRUE;
            return new Calculator(tipoR, res);
        } else{
            return null;
        }
    }
}
