/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.control;

import alog.analise.Erro;
import alog.test.Defaults;
import alog.token.FuncaoToken;
import alog.token.Token;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Caique
 */
public class ScannerTest {
    
    public ScannerTest() {
        
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of existeProximo method, of class Scanner.
     */
    @Test
    public void testExisteProximo() {
        System.out.println("existeProximo");
        Scanner scanner;
        
        scanner = new Scanner(Defaults.CODIGO_VAZIO);
        assertEquals("Resultado diferente do esperado",scanner.existeProximo(), false);
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_SEQUENCIAL);
        assertEquals("Resultado diferente do esperado",scanner.existeProximo(), true);
    }

    /**
     * Test of proximo method, of class Scanner.
     */
    @Test
    public void testProximo() {
        System.out.println("proximo");
        Scanner scanner;
        
        scanner = new Scanner(Defaults.CODIGO_VAZIO);
        assertEquals("Resultado diferente do esperado",scanner.proximo().getPalavra(), "");
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_SEQUENCIAL);
        assertEquals("Resultado diferente do esperado",scanner.proximo().getFuncaoToken(), FuncaoToken.RES_BLOCO_INICIO);
    }

    /**
     * Test of listaTokens method, of class Scanner.
     */
    @Test
    public void testListaTokens() {
        System.out.println("listaTokens");
        Scanner scanner;
        List<Token> tokens;
        
        scanner = new Scanner(Defaults.CODIGO_INVALIDO_C);
        tokens = scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",
                tokens.size(), 42);
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_SEQUENCIAL);
        tokens = scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",
                tokens.size(), 40);
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_COMPLETO);
        tokens = scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",
                tokens.size(), 184);
    }

    /**
     * Test of getNumErros method, of class Scanner.
     */
    @Test
    public void testGetNumErros() {
        System.out.println("getNumErros");
        Scanner scanner;
        
        scanner = new Scanner(Defaults.CODIGO_INVALIDO_C);
        scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",scanner.getNumErros(), 4);
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_SEQUENCIAL);
        scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",scanner.getNumErros(), 0);
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_COMPLETO);
        scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",scanner.getNumErros(), 0);
    }

    /**
     * Test of getListaErros method, of class Scanner.
     */
    @Test
    public void testGetListaErros() {
        System.out.println("getListaErros");
        Scanner scanner;
        
        List<String> erros = new LinkedList<>();
        erros.add("Linha 1, Coluna 1: Caractere '#' (unicode 35) inválido");
        erros.add("Linha 2, Coluna 12: Caractere '{' (unicode 123) inválido");
        erros.add("Linha 4, Coluna 17: Caractere '&' (unicode 38) inválido");
        erros.add("Linha 4, Coluna 55: Caractere '}' (unicode 125) inválido");
        
        scanner = new Scanner(Defaults.CODIGO_INVALIDO_C);
        scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",scanner.getErros(), erros);
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_SEQUENCIAL);
        scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",scanner.getErros(), new LinkedList<>());
    }

    /**
     * Test of imprimeErros method, of class Scanner.
     */
    @Test
    public void testImprimeErros() {
        System.out.println("imprimeErros");
        Scanner scanner;
        
        scanner = new Scanner(Defaults.CODIGO_INVALIDO_C);
        scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",
                scanner.printErros(), 
                "Linha 1, Coluna 1: Caractere '#' (unicode 35) inválido\n" +
                "Linha 2, Coluna 12: Caractere '{' (unicode 123) inválido\n" +
                "Linha 4, Coluna 17: Caractere '&' (unicode 38) inválido\n" +
                "Linha 4, Coluna 55: Caractere '}' (unicode 125) inválido");
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_SEQUENCIAL);
        scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",scanner.getErros(), "");
        
    }

    /**
     * Test of retornaErros method, of class Scanner.
     */
    @Test
    public void testRetornaErros() {
        System.out.println("retornaErros");
        Scanner scanner;
        
        String[] invChars = {"#", "{", "&", "}"};
        scanner = new Scanner(Defaults.CODIGO_INVALIDO_C);
        scanner.listaTokens();
        List<Erro> erros = scanner.getErros();
        assertEquals("Resultado diferente do esperado",erros.size(), 4);
        for (Erro t : erros){
            assertEquals("Resultado diferente do esperado",t.getToken().getPalavra(), invChars[erros.indexOf(t)]);
        }
        
        scanner = new Scanner(Defaults.CODIGO_VALIDO_ALOG_SEQUENCIAL);
        scanner.listaTokens();
        assertEquals("Resultado diferente do esperado",scanner.getErros(), new LinkedList<>());
    }
    
}
