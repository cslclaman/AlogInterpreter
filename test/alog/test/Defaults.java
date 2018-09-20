/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alog.test;

/**
 *
 * @author Caique
 */
public class Defaults {
    /**
     * String vazia.
     */
    public static final String CODIGO_VAZIO = "";
    
    /**
     * Código-fonte C.
     */
    public static final String CODIGO_INVALIDO_C = 
            "#include<stdio.h>"
            + "\nint main (){\n"
            + "\tint *x, y;\n"
            + "\tscanf(\"%d %d\", x, &y);"
            + "\tprintf(\"%d %d\", *x, y);"
            + "\treturn 0;"
            + "}";
    
    /**
     * Código-fonte Alog que poderia ser executado caso não passasse pelo Parser.
     */
    public static final String CODIGO_INVALIDO_ALOG = 
            "Início\n" +
            "\tCaracter Nome\n" +
            "\tInteiro Idade\n" +
            "\tReal IdadeDias\n" +
            "\tLeia Nome Idade\n" +
            "\tIdadeDias <- Idade * 365.25\n" +
            "\tEscreva Nome \" tem \" IdadeDias \" dias de vida\"\n" +
            "Fim";
    
    /**
     * Código-fonte Alog (sequencial) executável
     */
    public static final String CODIGO_VALIDO_ALOG_SEQUENCIAL = 
            "Inicio\n" +
            "\tCaracter: Nome;\n" +
            "\tInteiro: Idade;\n" +
            "\tReal: IdadeDias;\n" +
            "\tLeia(Nome, Idade);\n" +
            "\tIdadeDias <- Idade * 365.25;\n" +
            "\tEscreva(Nome,\" tem \",IdadeDias,\" dias de vida\");\n" +
            "Fim";
    
    /**
     * Código-fonte Alog executável com quase todos os tokens possíveis.
     */
    public static final String CODIGO_VALIDO_ALOG_COMPLETO = 
            "Função dobro (X: Inteiro) : Inteiro\n" +
            "Início\n" +
            "\tInteiro: Y;\n" +
            "\tY <- X * 2;\n" +
            "\tRetorna Y;\n" +
            "Fim\n" +
            "\n" +
            "Rotina imprimeMetade (X: Inteiro)\n" +
            "Início\n" +
            "\tInteiro: Y;\n" +
            "\tY <- X div 2;\n" +
            "\tEscreva (Y);\n" +
            "Fim\n" +
            "\n" +
            "Algoritmo principal\n" +
            "Início\n" +
            "\tCaracter: Opcao;\n" +
            "\tInteiro: Cont, Num;\n" +
            "\tReal: Media;\n" +
            "\tMatriz [1..5] de Inteiro: MatInt;\n" +
            "\t\n" +
            "\tOpcao <- \"N\";\n" +
            "\t\n" +
            "\tFaça\n" +
            "\tInício\n" +
            "\t\tPara Cont de 1 até 5 Faça\n" +
            "\t\t\tLeia (MatInt[Cont]);\n" +
            "\t\t\n" +
            "\t\tCont <- 1;\n" +
            "\t\tMedia <- 0;\n" +
            "\t\tEnquanto Cont <= 5 Faça\n" +
            "\t\tInício\n" +
            "\t\t\tNum <- MatInt[Cont];\n" +
            "\t\t\tMedia <- Media + Num;\n" +
            "\t\t\tCont <- Cont + 1;\n" +
            "\t\t\t\n" +
            "\t\t\tSe Num mod 2 = 0 Então\n" +
            "\t\t\t\timprimeMetade (Num);\n" +
            "\t\t\tSenão\n" +
            "\t\t\tInício\n" +
            "\t\t\t\tNum <- dobro(Num);\n" +
            "\t\t\t\tEscreva(Num);\n" +
            "\t\t\tFim\n" +
            "\t\tFim\n" +
            "\t\t\n" +
            "\t\tEscreva(\"Média = \", Media / 5);\n" +
            "\t\t\n" +
            "\t\tLeia (Opcao);\n" +
            "\tFim\n" +
            "\tEnquanto Opcao = \"S\" Ou Opcao = \"s\";\n" +
            "\t\n" +
            "Fim";
}
