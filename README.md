# AlogInterpreter
Interpretador visual de pseudolinguagem de algoritmos - trabalho de graduação
para o curso de Análise e Desenvolvimento de Sistemas da Fatec Sorocaba 
(não confundir com [VisualG](http://visualg3.com.br/), é outro programa para outra pseudolinguagem)

## O que é esse algoritmo que ele executa
Uma pseudolinguagem levemente semelhante ao Portugol, porém também com semelhanças à linguagem C.
Confira [Exemplos de Algoritmos](https://github.com/cslclaman/AlogInterpreter/tree/master/exemplos_algoritmos)

Referência: FORBELLONE, A. L. V.; EBERSPACHER, H. F. __Lógica de Programação - A construção de algoritmos e estrutura de dados__. 3ª Ed. São Paulo: Makron Books, 2005.

## Que tipo de algoritmo ele executa (por enquanto)
* Algoritmos sequenciais básicos [(arquivos de exemplo 01 a 06a)](https://github.com/cslclaman/AlogInterpreter/tree/master/exemplos_algoritmos).
* Algoritmos condicionais [(arquivos de exemplo 06b a 09)](https://github.com/cslclaman/AlogInterpreter/tree/master/exemplos_algoritmos).

## O que já foi implementado
* Sistema interpretador via console/arquivo de texto (rode com argumentos: -console <NOME DO ARQUIVO>)
* Analisador léxico (Scanner) - Gera tokens a partir de um código-fonte
* Analisador sintático (Parser) - Cria e confere expressões (conjuntos de tokens) a partir da lista de tokens gerada pelo Scanner
* Interpretador - Executa as expressões criadas pelo Parser
    * Criação de variáveis
    * Entrada de dados para uma variável (rotina Leia)
    * Atribuição de valor para uma variável
    * Execução de expressões aritméticas
    * Saída de dados (rotina Escreva)
    * Funções matemáticas (POT, equivalente à pow() e RAIZ, equivalente a sqrt() )
    * Estrutura condicional (SE...ENTÃO/SENÃO)
* GUI para execução passo a passo e depuração
    * Executa tudo o que o interpretador executa, só que de maneira interativa.

## Próximos passos
* Operador "-" unário
* Implementar estruturas de repetição (PARA, FAÇA e ENQUANTO)
* Resolução de expressões aritméticas em funções (ESCREVA/RAIZ/POT) e em expressões lógicas/relacionais
* Melhorar apresentação de erros (principalmente os exibidos pelo parser), evitando mostrar tudo no console
* Análise semântica antes da interpretação (verificação de nomes e tipos de variáveis, chamadas a funções, etc)

## Erros e bugs conhecidos
* Veja as [issues do projeto](https://github.com/cslclaman/AlogInterpreter/issues)
