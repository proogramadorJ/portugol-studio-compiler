# Portugol Studio Compilador e Máquina Virtual - ANDROID

#### Compilador e Runtime(VM) da linguagem portugol do 'Portugol Studio' - Univali.

### Sobre

Esta é minha própria implementação para <strong>ANDROID</strong> da linguagem portugol(do portugol studio). Implementado durante meus estudos sobre
compiladores.
Tudo nesse repositorio está sendo implementado sem o uso de ferramentas geradoras de scanner, parser e etc.

### O que foi implementado até agora?

#### Analisador léxico

- [x] Scan de tokens a partir de texto

#### Analisador sintático

- [x] Expressões
- [x] Declaração de variaveis de tipos básicos
- [ ] Declaração de arrays e matrizes
- [x] Atribuição de variavies
- [x] Estrutura condicional 'Se' 'Senao'
- [x] Estrutura de repetição 'Enquanto'
- [x] Estrutura de repetição 'Faca-Enquanto'
- [x] Estrutura de repetição 'Para'
- [x] Declaração de funções
- [X] Chamada de funções

#### Analisador semântico
- [x] Resolução de escopo
- [ ] Resolução de tipos
- [ ] Verificação de retorno de funções
- [x] Tabela de simbolos

#### Geração de código objeto

- [x] Expressões
- [x] Declaração de variaveis de tipos básicos
- [ ] Declaração de arrays e matrizes
- [x] Atribuição de variavies
- [x] Estrutura condicional 'Se' 'Senao'
- [x] Estrutura de repetição 'Enquanto'
- [x] Estrutura de repetição 'Faca-Enquanto'
- [X] Estrutura de repetição 'Para'
- [x] Declaração de funções
- [X] Chamada de funções

#### Runtime/VM

- [x] Expressões
- [x] Declaração de variaveis de tipos básicos
- [ ] Declaração de arrays e matrizes
- [x] Atribuição de variavies
- [x] Estrutura condicional 'Se' 'Senao'
- [x] Estrutura de repetição 'Enquanto'
- [x] Estrutura de repetição 'Faca-Enquanto'
- [X] Estrutura de repetição 'Para'
- [x] Declaração de funções
- [x] Chamada de funções

### Funcões da linguagem suportada

- [x] Operações Aritimeticas
- [x] Suporte a variaveis
- [ ] Suporte constantes
- [ ] Suporte a vetores e matrizes
- [x] Se Senao (if, else)
- [x] Enquanto (while)
- [x] Faca-Enquanto (do-while)
- [X] Para  (for)
- [ ] Caso Escolha(switch case)
- [x] Procedimentos (funções)

### Funcões nativas da linguagem implementadas até o momento
- leia
- escreva
- NumeroCaracteres

### Exemplo de programa - fibonnaci
```
    funcao inteiro fibonacci(inteiro n){
        se(n <= 1){
            retorne n
        }
        retorne fibonacci(n - 1) + fibonacci(n - 2)

    }
    
    funcao inicio(){
        escreva(fibonacci(10))
}

```
### Bytecode gerado do programa acima
```
1 -  LOAD_LOCAL 0
2 -  LOAD_CONST 1
3 -  LE 
4 -  JMP_IF_FALSE 7
5 -  LOAD_LOCAL 0
6 -  RETURN 
7 -  LOAD_LOCAL 0
8 -  LOAD_CONST 2
9 -  SUB 
10 -  CALL 0
11 -  LOAD_LOCAL 0
12 -  LOAD_CONST 3
13 -  SUB 
14 -  CALL 0
15 -  ADD 
16 -  RETURN 
17 -  LOAD_CONST 5
18 -  CALL 0
19 -  CALL_NATIVE 0
20 -  HALT 
21 -  HALT 
```

### Capturas de Tela
<img src="/androidApp/screenshoots/fibonacci_code.png" alt="Screenshot do App" width="300">
<img src="/androidApp/screenshoots/fibonacci_console.png" alt="Screenshot do App" width="300">

### Referências

- Sintaxe da linguagem: https://portugol.dev/

### Bibliotecas de terceiros
- Sora-editor https://github.com/Rosemoe/sora-editor
