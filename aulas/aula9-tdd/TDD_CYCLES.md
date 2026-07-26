# Registro dos ciclos TDD

Este documento registra os ciclos Red-Green-Refactor aplicados nas atividades da Aula 9.

## Calculadora

1. **Red:** testes para soma simples, valores negativos, decimais e limites.
2. **Green:** implementação de `add` com validação de faixa.
3. **Refactor:** testes parametrizados, repetidos e Hamcrest; inclusão de divisão e raiz quadrada com seus casos inválidos.

## Gerenciador de voos

1. **Red:** testes para adicionar/remover passageiros e regras Economy/Premium.
2. **Green:** implementação das classes de voo e das regras de passageiros.
3. **Refactor:** extração das regras comuns para `Flight`, controle de duplicidade, limite de passageiros e criação de `FlightManager`.

## Conversores e validador

Cada funcionalidade foi conduzida a partir de testes de comportamento válido e inválido, seguida da implementação mínima e posterior refatoração dos testes com parametrização, mocks, agrupamento e mensagens descritivas.

## Katas

Os katas String Calculator, Prime Factors e Bowling Game seguiram ciclos incrementais para casos básicos, regras especiais e entradas inválidas.

> Como este registro foi criado após a implementação, ele documenta os ciclos realizados, mas não substitui um histórico Git criado durante o desenvolvimento.
