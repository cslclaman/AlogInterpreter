# AlogInterpreter
Interpretador de linguagem de algoritmos - proposta de trabalho de graduação

## O que é esse algoritmo que ele executa
Uma pseudolinguagem levemente semelhante ao Portugol, porém também com semelhanças à linguagem C.
Confira [Exemplos de Algoritmos](https://github.com/cslclaman/AlogInterpreter/tree/master/exemplos_algoritmos)

Referência: FORBELLONE, A. L. V.; EBERSPACHER, H. F. __Lógica de Programação - A construção de algoritmos e estrutura de dados__. 3ª Ed. São Paulo: Makron Books, 2005.

## Que tipo de algoritmo ele executa
* Algoritmos sequenciais básicos (por enquanto)

## O que já foi implementado
* GUI básica para escrita de algoritmos, com tabela de variáveis e campos de entrada e saída.
* Analisador léxico (Scanner) - Gera tokens a partir de um código-fonte
* Analisador sintático (Parser) - Cria e confere expressões (conjuntos de tokens) a partir da lista de tokens gerada pelo Scanner
* Interpretador - Executa as expressões criadas pelo Parser
    * Criação de variáveis
    * Entrada de dados para uma variável (rotina Leia)

## Próximos passos
* Completar as rotinas do interpretador para algoritmos sequenciais. Falta:
    * Atribuição de valor a uma variável
    * Execução de expressões aritméticas
    * Saída de dados (rotina Escreva)
    * Funções (raiz quadrada e potência)
* Deixar a GUI mais amigável e prestativa
* Implementar estrutura condicional, incluindo execução de operações lógicas e relacionais
