# Diário de Atividades - TMS 2026/1
## Aula 5 - Introdução aos Testes de Software

**Data:** 25/05/2026 - 

---

## Atividade 1: Conceitos sobre Testes 


Em projetos anteriores, os testes que realizei incluíram:

1. Testes Manuais: Verificação visual de interfaces e comportamentos esperados
2. Testes Unitários Básicos: Usando JUnit para validar métodos isolados
3. Testes de Integração: Verificando a comunicação entre módulos

**Exemplo de teste descrito nos termos apresentados:**
- Dado de teste: (username: "admin", password: "1234")
- Resultado esperado: Login bem-sucedido, redirecionamento para dashboard
- Caso de teste: `<("admin", "1234"), redirect("/dashboard")>`

---

## Atividade 2: Conceitos sobre Testes 

### Referências Acadêmicas

| Termo | Artigo | Resumo |
|-------|--------|--------|
| **Erro** | "A Classification of Software Errors" (Beizer, 1990) | Classifica erros de software em diferentes categorias baseadas em sua origem e impacto no sistema |
| **Falha** | "Software Reliability Engineering" (Musa, 1999) | Aborda modelos de confiabilidade e métricas para medir falhas em sistemas de software |
| **Defeito** | "Orthogonal Defect Classification" (IBM, 1992) | Propõe uma metodologia para classificar defeitos de software de forma sistemática |
| **Bug** | "Debugging: The 9 Indispensable Rules" (Agans, 2002) | Aborda técnicas práticas para identificação e correção de bugs em software |

---

## Atividade 3: Motivações para Testes

### Por que Testes são Importantes?

#### 1. Redução nos custos de correção, retrabalho e número de falhas
- Correções são mais fáceis no início do processo de desenvolvimento
- Falhas podem gerar prejuízos graves, até irreparáveis
- Quanto mais tarde o defeito é encontrado, maior o custo de correção

#### 2. Otimização no desenvolvimento
- Desenvolvimento e entregas podem atrasar pelo esforço empregado na correção de bugs
- A ausência de testes agrava a busca por problemas
- Testes não feitos podem ser postergados indefinidamente

#### 3. Facilitar a manutenção e evolução
- Software bem testado e bem construído é mais fácil de manter
- Evita-se o efeito cascata quando uma mudança impacta negativamente o restante do projeto

#### 4. Aumento da qualidade, confiabilidade e segurança
- Produtos testados tendem a falhar menos
- Mantêm-se operacionais por mais tempo
- Testes ajudam na identificação precoce de vulnerabilidades

#### 5. Mais satisfação e fidelidade dos usuários
- Usuários têm uma experiência de uso mais satisfatória
- Tendem a se manter fiéis diante de software de alta qualidade

### Casos Notáveis onde Falta de Testes Causou Problemas


#### Knight Capital Group (2012)
A empresa perdeu US$ 440 milhões em 45 minutos devido a um bug em software de trading. Uma atualização incompleta de código legado causou execução de ordens erradas no mercado de ações.

**Referência:** SEC. "In the Matter of Knight Capital Americas LLC", Administrative Proceeding File No. 3-15570, 2013.

#### Sistema de Defesa de Mísseis Patriot (1991)
Durante a Guerra do Golfo, uma bateria americana de mísseis Patriot em Dhahran, na Arábia Saudita, falhou ao interceptar um míssil Scud iraquiano, resultando na morte de 28 soldados. O problema raiz foi um erro de arredondamento no relógio interno do sistema, que piorava quanto mais tempo o sistema ficasse ligado. O software foi projetado originalmente para ser móvel e operar por curtos períodos, de modo que a equipe de engenharia nunca realizou testes de operação contínua e prolongada. No dia do ataque, o sistema estava ligado ininterruptamente por mais de 100 horas, resultando em um desvio de tempo de cerca de 0,34 segundos , o suficiente para o radar procurar o míssil iraquiano no lugar errado do céu.

**Referência:** U.S. General Accounting Office (GAO). (1992). Patriot Missile Defense: Software Problem Led to System Failure at Dhahran, Saudi Arabia. Report GAO/IMTEC-92-26.

#### O Apagão do Nordeste dos EUA e Canadá (2003)
Em agosto de 2003, cerca de 50 milhões de pessoas ficaram sem eletricidade na América do Norte. A causa primária foi atribuída a árvores encostando em linhas de alta tensão, mas o que transformou um problema local em um apagão em cascata foi uma falha de software no sistema de gerenciamento de energia (XA/21) da empresa FirstEnergy. Um erro de "condição de corrida" (race condition) fez com que o sistema de alarmes travasse silenciosamente, deixando os operadores completamente "cegos" para a sobrecarga que estava ocorrendo na rede. A falha ocorreu porque a equipe não executou testes de estresse e testes de concorrência adequados para simular situações de altíssima carga de dados e múltiplos processos competindo por recursos simultaneamente.

**Referência:** U.S.-Canada Power System Outage Task Force. (2004). Final Report on the August 14, 2003 Blackout in the United States and Canada.


### Relatos de Sucesso com Boas Práticas de Testes

#### Microsoft Windows:
A adoção de práticas rigorosas de testes automatizados e integração contínua no desenvolvimento do Windows 10 resultou em uma redução significativa de bugs críticos em comparação com versões anteriores.

**Referência:** Microsoft Engineering Stories, "How we test Windows 10", 2015.

#### Transformação do Firmware das Impressoras HP LaserJet (2008):
No início dos anos 2000, a divisão de firmware da HP estava estagnada: os ciclos de desenvolvimento eram lentos, e apenas 5% do tempo da equipe era gasto em inovação, enquanto o restante era consumido corrigindo bugs manuais e lidando com integrações complexas. A empresa decidiu reestruturar completamente seu processo, adotando a Integração Contínua (CI) com um conjunto massivo de testes automatizados executados em simuladores de hardware e impressoras reais. Essa mudança erradicou semanas de testes manuais integrados, reduziu o tempo de lançamento de novos recursos e aumentou a capacidade de desenvolvimento da equipe em cerca de 140%.

**Referência:** Gruver, G., Young, M., & Fulghum, P. (2012). A Practical Approach to Large-Scale Agile Development: How HP Transformed LaserJet FutureSmart Firmware. Pearson Education.
---

## Atividade 4: Atores em Testes

### Papéis Importantes

| Papel | Descrição |
|-------|-----------|
| **QA (Quality Assurance)** | Engenheiro/Analista de Qualidade responsável por planos de teste, automação de atividades e testes de API |
| **Testador de Software** | Responsável pela codificação e execução de testes diversos, scripts e códigos para automação |

### Atividades Típicas

| QA | Ambos | Testador |
|----|-------|----------|
| Planos de Teste | Testes Manuais com Feedback | Codificação de testes |
| Automação de Atividades | Relatório de bugs | Execução de testes diversos |
| Scripts para busca e acompanhamento | Feedback sobre UI/UX | Scripts e códigos para automação |
| Testes de API | Reuniões | |

### Pesquisa de Vagas em Testes de Software

#### Análise de Mercado: Qualidade de Software e QA

#### 1 - Vagas Abertas e Habilidades Requeridas por Nível

Com base em pesquisas recentes em portais de emprego como Indeed e Jooble, o mercado brasileiro para a área de testes de software apresenta as seguintes características de escopo e exigências técnicas, divididas por nível de senioridade:

**Nível Júnior (Ex: Analista de QA Jr, Analista de Testes Automatizados)**
*   **Escopo:** Foco em execução de roteiros já definidos, documentação de bugs e apoio na criação de casos de teste sob supervisão.
*   **Habilidades/Tecnologias:** Conhecimento teórico em metodologias ágeis e de testes (unitário, integrado, e2e). Noções de testes de API (uso de REST, JSON e ferramentas como Postman) e comandos básicos de banco de dados (SQL). Para vagas com viés técnico, pede-se familiaridade introdutória com ferramentas de automação (Cypress ou Selenium) e escrita de cenários em BDD.
*   **Referências:** Vagas levantadas no Indeed e Jooble (Ex: Analista de QA Jr na NEXTI; QA Automação Jr no Grupo CM).

**Nível Pleno (Ex: Analista de QA Pleno, QA Engineer)**
*   **Escopo:** Profissional com autonomia para definir planos de teste, atuar preventivamente junto aos desenvolvedores e liderar a criação de scripts automatizados para garantir a qualidade contínua da aplicação.
*   **Habilidades/Tecnologias:** Sólida experiência com testes de aplicações web, mobile e APIs. Exige-se domínio prático na construção e manutenção de automações utilizando frameworks de mercado (Selenium, Robot Framework, Cypress, Playwright). Conhecimento de controle de versão (Git) e rastreamento de defeitos (Jira).
*   **Referências:** Vagas no Indeed (Ex: QA Engineer Pleno na Wave by Bemobi; Analista QA Pleno/ na TOTVS).

**Nível Sênior (Ex: QA Automation Sênior, Tech Lead QA)**
*   **Escopo:** Papel estratégico focado em arquitetura de qualidade. Define as ferramentas, implementa métricas de qualidade, constrói os pipelines e atua como mentor técnico para os profissionais mais juniores.
*   **Habilidades/Tecnologias:** Arquitetura de testes e integração contínua (CI/CD) usando GitHub Actions, GitLab ou Azure DevOps. Forte capacidade de programação (Java, C#, Python ou JavaScript) voltada para automação complexa. Experiência com testes de carga/performance (stress) e, crescentemente, validação de segurança e modelagem para testes envolvendo Inteligência Artificial.
*   **Referências:** Vagas no Indeed (Ex: Analista de Qualidade Sênior na Saipos; SR QA na Zallpy Digital).

---

#### 2- Práticas de QA em Grandes Empresas


**Como grandes empresas têm abordado a área de testes?**
*   **Cultura de Shift-Left:** Os testes não são mais a "última etapa" antes de colocar o produto no ar. As empresas antecipam a análise de qualidade para as fases de planejamento arquitetural e concepção do código. Encontrar uma falha antes mesmo de o código ser escrito é o cenário ideal.
*   **Automação como Regra (CI/CD):** A execução manual é restrita a cenários críticos, exploratórios ou focados na experiência do usuário. A esmagadora maioria dos testes de regressão roda automaticamente na nuvem assim que um desenvolvedor submete uma nova alteração, barrando o código defeituoso imediatamente (Quality Gates).
*   **Qualidade Compartilhada ( Quality Assistance ):** Em vez de um departamento isolado de "Testadores" ser o único responsável por aprovar o software, o papel da área evoluiu. O QA moderno fornece ferramentas, infraestrutura e mentoria para que os próprios desenvolvedores criem testes e assumam a responsabilidade sobre a qualidade daquilo que entregam.

**O que se espera de um profissional nesta área?**
*   **Capacidade de Engenharia (SDET):** O papel do "testador manual" está diminuindo. Espera-se que o profissional seja um Software Development Engineer in Test , ou seja, alguém que saiba programar e desenvolver código especificamente para testar o sistema.
*   **Pensamento Sistêmico:** É fundamental entender a arquitetura do software (como o banco de dados se comunica com o backend, que por sua vez alimenta o frontend via microsserviços em nuvem) para mapear exatamente onde o sistema pode quebrar.
*   **Comunicação Analítica:** O QA atua como a ponte vital entre a área de Negócios (Produto) e a Engenharia. Espera-se que o profissional saiba traduzir requisitos complexos em cenários validáveis, garantindo que o time tenha clareza sobre os riscos envolvidos antes de qualquer lançamento no mercado.

#### Vaga Júnior - QA Jr.
**Requisitos:**
- Conhecimento básico em testes de software
- Familiaridade com Selenium ou Cypress
- Conhecimento em SQL básico
- Metodologias ágeis (Scrum)

#### Vaga Pleno - Analista de Testes Pleno
**Requisitos:**
- 2-4 anos de experiência em testes
- Automação com Selenium, Cypress ou Playwright
- Conhecimento em API testing (Postman, RestAssured)
- CI/CD (Jenkins, GitLab CI)
- Banco de dados SQL e NoSQL

#### Vaga Sênior - QA Engineer Sênior
**Requisitos:**
- 5+ anos de experiência
- Liderança técnica de equipes de QA
- Arquitetura de frameworks de teste
- Performance testing (JMeter, Gatling)
- Security testing
- Métricas de qualidade e cobertura de código

### Resumo dos Vídeos Sugeridos

**Meet Test Engineers at Google:**
- O Google investe fortemente em automação de testes
- Engenheiros de teste são considerados desenvolvedores de software
- Foco em testabilidade do código desde o design

**O que se espera de um profissional nesta área:**
- Mentalidade analítica e atenção aos detalhes
- Conhecimento técnico em programação
- Capacidade de comunicação para reportar bugs
- Proatividade na identificação de problemas

---


## Registro de Progresso

### O que foi feito:
- [x] Pesquisa sobre casos notáveis de falhas por falta de testes
- [x] Estudo dos papéis profissionais em testes (QA, Testador)
- [x] Pesquisa sobre vagas de emprego na área
- [x] Compreensão das etapas e tipos de testes


### O que ficou pendente:
- [ ] Dar continuidade ao conteúdo da aula.

### O que impediu:
- Tempo e ainda nao foi apresentado

### O que será feito na sequência:
- Iniciar estudos sobre frameworks de automação de testes (JUnit)
- Praticar escrita de casos de teste

---
