# AlogInterpreter - Interpretador de Algoritmos
Interpretador visual de pseudolinguagem de algoritmos - trabalho de graduação
para o curso de Análise e Desenvolvimento de Sistemas da Fatec Sorocaba 
(não confundir com [VisualG](http://visualg3.com.br/), é outro programa para outra pseudolinguagem)

## Versão atual
Veja os [releases do projeto](https://github.com/cslclaman/AlogInterpreter/releases).

## O que é esse algoritmo que ele executa
Uma pseudolinguagem levemente semelhante ao Portugol, porém também com semelhanças à linguagem C.
Confira [Exemplos de Algoritmos](https://github.com/cslclaman/AlogInterpreter/tree/master/exemplos_algoritmos)

Referência: FORBELLONE, A. L. V.; EBERSPACHER, H. F. __Lógica de Programação - A construção de algoritmos e estrutura de dados__. 3ª Ed. São Paulo: Makron Books, 2005.

## Que tipo de algoritmo ele executa
* Algoritmos sequenciais básicos [(arquivos de exemplo 01 a 06a)](https://github.com/cslclaman/AlogInterpreter/tree/master/exemplos_algoritmos).
* Algoritmos condicionais [(arquivos de exemplo 06b a 09)](https://github.com/cslclaman/AlogInterpreter/tree/master/exemplos_algoritmos).
* Algoritmos repetitivos

## O que já foi implementado
* Sistema interpretador via console/arquivo de texto (rode com argumentos: -console <NOME DO ARQUIVO>)
* Analisador léxico (Scanner) - Gera tokens a partir de um código-fonte
* Analisador sintático (Parser) - Cria e confere expressões (conjuntos de tokens) a partir da lista de tokens gerada pelo Scanner
* Analisador semântico (Pre-Processor) - Analisa uso e inicialização de variáveis e tipos de operandos de expressões
* Interpretador - Executa o programa verificado pelo analisador semântico
    * Declaração de variáveis (tipos: Inteiro, Real, Caracter)
    * Entrada de dados para uma variável (rotina Leia)
    * Atribuição de valor para uma variável
    * Execução de expressões aritméticas (+ - * / div mod), relacionais (> < >= <= = <>) e lógicas (E OU NÃO)
    * Saída de dados (rotina Escreva)
    * Funções matemáticas (POT, equivalente à pow() e RAIZ, equivalente a sqrt() )
    * Estrutura condicional (SE...ENTÃO/SENÃO)
    * Estrutura repetitiva com teste no início (ENQUANTO...FAÇA)
    * Estrutura repetitiva com teste no final (FAÇA ... ENQUANTO...;)
    * Estrutura repetitiva com número de passos definido (PARA ... DE ... ATÉ ... FAÇA)
* GUI para execução passo a passo e depuração 
    * Executa tudo o que o interpretador executa, só que de maneira interativa.

## Erros e bugs conhecidos
* Veja as [issues do projeto](https://github.com/cslclaman/AlogInterpreter/issues)
