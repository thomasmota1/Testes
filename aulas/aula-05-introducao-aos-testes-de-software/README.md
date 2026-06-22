# Diário de Atividades - TMS 2026/1
## Aula 5 - Introdução aos Testes de Software

**Data:** 25/05/2026 - 

---

## Atividade 1 - Conceitos sobre Testes (1)

### 1. Recapitulação de um projeto anterior

No projeto **Plataforma de Estudos**, desenvolvido na disciplina de Desenvolvimento Web I, implementei páginas de autenticação, dashboard, quizzes, flashcards, comunidades, calendário e caderno de notas. O projeto utiliza HTML, CSS e JavaScript e armazena dados no `localStorage`.

O repositório não contém uma suíte automatizada. Durante o desenvolvimento, as verificações foram principalmente **manuais e funcionais**, por exemplo:

- preencher o formulário com credenciais válidas e inválidas;
- verificar mensagens de validação;
- confirmar o redirecionamento após o login;
- criar quizzes e conferir o cálculo da pontuação;
- criar, importar e excluir baralhos de flashcards;
- recarregar a página e verificar a persistência no `localStorage`;
- alternar o tema visual e observar sua permanência entre páginas.

### 2. Descrição nos termos apresentados

Considere a funcionalidade de login, cuja especificação é aceitar o usuário cadastrado no protótipo e redirecioná-lo ao dashboard.

- **Especificação S(P):** dadas as credenciais cadastradas, autenticar o usuário, registrar a sessão e abrir `dashboard.html`; para qualquer outra combinação, permanecer na página e apresentar uma mensagem de erro.
- **Domínio de entrada D(P):** todos os pares `(email, senha)` que podem ser informados no formulário.
- **Domínio de saída R(P):** `{redirecionamento para o dashboard, mensagem de credenciais incorretas}`.
- **Dado de teste válido:** `("aluno@teste.com", "123")`.
- **Caso de teste válido:** `<("aluno@teste.com", "123"), redirecionar para "dashboard.html">`.
- **Caso de teste inválido:** `<("aluno@teste.com", "senha-errada"), exibir "Email ou senha incorretos.">`.
- **Conjunto de teste:** os dois casos anteriores mais casos de campos vazios e formatos de e-mail inválidos.

A inspeção também revelou um defeito real. `auth.js` grava as chaves `isLoggedIn` e `username`, enquanto `dashboard.js` e `main.js` consultam ou removem `estaLogado` e `nomeUsuario`.

- **Defeito (fault):** nomes incompatíveis para as chaves da sessão.
- **Erro (error):** após o login, o estado interno contém `isLoggedIn=true`, mas o dashboard procura outra chave e interpreta a sessão como inexistente.
- **Falha (failure):** se a proteção comentada do dashboard for ativada, um usuário autenticado poderá ser enviado novamente à página de login; além disso, o logout não remove as chaves realmente gravadas.
- **Correção indicada:** padronizar os nomes das chaves e criar testes automatizados para login, acesso protegido e logout.

---

## Atividade 2 - Conceitos sobre Testes (2)

### 3. Referências acadêmicas para erro, falha, defeito e bug

| Termo | Artigo acadêmico | Resumo e relação com o termo |
|---|---|---|
| **Erro** | Avizienis et al., *Basic Concepts and Taxonomy of Dependable and Secure Computing* (2004) | Define erro como a parte do estado do sistema capaz de provocar uma falha. O artigo organiza formalmente a cadeia defeito → erro → falha. |
| **Falha** | Avizienis et al., *Basic Concepts and Taxonomy of Dependable and Secure Computing* (2004) | Caracteriza falha como o evento em que o serviço entregue se desvia do serviço correto, distinguindo-a da causa interna. |
| **Defeito** | Chillarege et al., *Orthogonal Defect Classification - A Concept for In-Process Measurements* (1992) | Propõe a Classificação Ortogonal de Defeitos para relacionar tipos de defeito às etapas do desenvolvimento e apoiar melhoria de processo. |
| **Bug** | Herzig, Just e Zeller, *It's Not a Bug, It's a Feature: How Misclassification Impacts Bug Prediction* (2013) | Investiga registros classificados como bugs que, na realidade, representam outros tipos de alteração, e demonstra o impacto dessa classificação incorreta em modelos de predição. |

Referências:

- AVIZIENIS, A. et al. *Basic Concepts and Taxonomy of Dependable and Secure Computing*. IEEE Transactions on Dependable and Secure Computing, 2004. [DOI 10.1109/TDSC.2004.2](https://doi.org/10.1109/TDSC.2004.2).
- CHILLAREGE, R. et al. *Orthogonal Defect Classification - A Concept for In-Process Measurements*. IEEE Transactions on Software Engineering, 1992. [DOI 10.1109/32.177364](https://doi.org/10.1109/32.177364).
- HERZIG, K.; JUST, S.; ZELLER, A. *It's Not a Bug, It's a Feature: How Misclassification Impacts Bug Prediction*. ICSE, 2013. [DOI 10.1109/ICSE.2013.6606585](https://doi.org/10.1109/ICSE.2013.6606585).

---

## Atividade 3 - Motivações para Testes

### 1. Casos graves relacionados a testes insuficientes

#### Ariane 5 - voo 501 (1996)

O Ariane 5 foi destruído aproximadamente 40 segundos após o início do voo. Um valor de velocidade horizontal, representado em ponto flutuante de 64 bits, foi convertido para um inteiro de 16 bits e provocou uma exceção. O software do sistema de referência inercial havia sido reutilizado do Ariane 4, mas o comportamento sob a trajetória do Ariane 5 não havia sido adequadamente validado. O caso evidencia o risco de reutilizar software sem testar os novos limites operacionais.

**Referência:** ESA. *Ariane 5 Flight 501 Failure - Report by the Inquiry Board*, 1996. [Relatório oficial](https://esamultimedia.esa.int/docs/esa-x-1819eng.pdf).

#### Knight Capital (2012)

Em 1º de agosto de 2012, a Knight Capital enviou milhões de ordens incorretas ao mercado e perdeu mais de US$ 460 milhões. Segundo a SEC, a implantação deixou código antigo ativo em um servidor, não havia revisão independente adequada e os procedimentos de teste e implantação eram insuficientes. O episódio demonstra que testes de regressão, implantação uniforme, monitoramento e mecanismos automáticos de interrupção são essenciais em sistemas financeiros.

**Referência:** U.S. SEC. *In the Matter of Knight Capital Americas LLC*, Release No. 70694, 2013. [Decisão administrativa](https://www.sec.gov/files/litigation/admin/2013/34-70694.pdf).

### 2. Relato de melhoria obtida com boas práticas de testes

#### Firmware HP LaserJet

Gruver, Young e Fulghum descrevem a transformação do desenvolvimento do firmware HP LaserJet FutureSmart. A equipe reduziu grandes ciclos de integração manual ao adotar uma arquitetura comum, integração contínua e ampla automação de testes executados em simuladores e hardware real. A experiência associa essas práticas à redução do tempo gasto em estabilização e à ampliação da capacidade de entregar funcionalidades. O ponto central não é apenas “testar mais”, mas obter retorno frequente e reproduzível durante todo o desenvolvimento.

**Referência:** GRUVER, G.; YOUNG, M.; FULGHUM, P. *A Practical Approach to Large-Scale Agile Development: How HP Transformed LaserJet FutureSmart Firmware*. Addison-Wesley, 2012. ISBN 978-0-321-82272-8.

---

## Atividade 4 - Atores em Testes

### 1. Vagas e habilidades requeridas

Pesquisa realizada em **22/06/2026** em listagens públicas do LinkedIn. Como vagas podem ser encerradas ou alteradas, os links e a data de consulta são registrados para permitir a verificação.

| Nível e vaga consultada | Principais responsabilidades e requisitos observados |
|---|---|
| **Júnior - Analista de QA Júnior, Clude Saúde** | Planejamento e execução de testes manuais e automatizados; documentação e acompanhamento de bugs; testes funcionais, regressivos e exploratórios; Jira/TestRail; noções de Selenium ou Cypress; APIs, integração e CI/CD como diferenciais. [Vaga](https://www.linkedin.com/jobs/view/4416601808) |
| **Pleno - Analista de Testes QA Pleno, 4DF Connect** | Criação e revisão de casos manuais e automatizados; Cypress, Selenium e Postman; testes de API; SQL; Git; integração com CI/CD; validação de correções e registro de evidências. [Vaga](https://www.linkedin.com/jobs/view/4426381574) |
| **Sênior - Analista de Testes de Software Sênior, Instituto de Pesquisas ELDORADO** | Automação de testes de backend e APIs; experiência em projetos ágeis; domínio de frameworks de automação; levantamento de requisitos de qualidade; acompanhamento de indicadores; autonomia, colaboração e inglês avançado. [Vaga](https://www.linkedin.com/jobs/view/4409796805) |

Comparando os níveis, a vaga júnior enfatiza execução, registro e fundamentos; a plena exige autonomia para planejar e automatizar; a sênior acrescenta profundidade técnica, decisões de qualidade, comunicação com clientes e coordenação com outros profissionais.

### 2. Síntese dos vídeos sugeridos

Foram considerados os quatro vídeos indicados no slide:

- [Meet Test Engineers at Google](https://www.youtube.com/watch?v=C7OLZf5099Y), Life at Google;
- [Jim Nicholson: Importance of Software Testing](https://www.youtube.com/watch?v=-bqOond5E-w), NASA;
- [O que faz um analista de testes?](https://www.youtube.com/watch?v=O7rB5XoakEc), Código Fonte TV;
- [Como ser um Analista QA de Sucesso!](https://www.youtube.com/watch?v=eCmIALYTqPk), Código Fonte TV.

**Como grandes empresas abordam testes:** qualidade é tratada como responsabilidade compartilhada, iniciada ainda no projeto da solução. Automação, integração contínua, revisão de código, ambientes reproduzíveis e testes em diferentes níveis fornecem retorno rápido. Testes manuais continuam importantes em atividades exploratórias, usabilidade e investigação de riscos.

**O que se espera do profissional:** raciocínio analítico, comunicação clara, capacidade de transformar requisitos e riscos em casos de teste, conhecimento de programação e automação, registro preciso de defeitos e colaboração com desenvolvimento, produto e usuários.

**Perspectivas futuras:** o trabalho tende a exigir mais engenharia de qualidade, observabilidade, automação integrada a CI/CD, testes de APIs e sistemas distribuídos, segurança e avaliação de sistemas com IA. Ferramentas de IA podem apoiar geração e priorização de testes, mas ainda exigem supervisão humana para avaliar riscos, oráculos e resultados.

---

## Atividade 5 - Tipos de Teste

### 1 e 2. Técnicas, ferramentas e artefatos

| Tipo visto no slide | Técnicas | Exemplos de ferramentas | Artefatos produzidos |
|---|---|---|---|
| **Exploratório** | Sessões com charter, heurísticas, tours e anotações | Jira, Xray, gravador de tela | Charter, notas da sessão, evidências e relatos de defeito |
| **Unidade** | Particionamento de equivalência, valores-limite, cobertura de decisões | JUnit, pytest, Jest | Código de teste, XML de resultados e relatório de cobertura |
| **Integração** | Top-down, bottom-up, teste de contrato e uso de doubles | Testcontainers, WireMock, Spring Test | Logs de integração, contratos e relatório de execução |
| **API** | Testes funcionais, contrato, schema e autorização | Postman/Newman, REST Assured, SoapUI | Coleção de requisições, ambiente, relatório JSON/HTML |
| **Sistema** | Fluxos ponta a ponta e cenários baseados em requisitos | Playwright, Cypress, Selenium | Scripts E2E, capturas, vídeos, traces e logs |
| **Aceitação** | UAT, critérios de aceitação e BDD | Cucumber, SpecFlow | Cenários Gherkin, evidências de aceite e relatório |
| **Usabilidade** | Observação, think aloud, entrevistas e avaliação heurística | UserTesting, Lookback, Hotjar | Gravações, mapa de problemas e relatório de usabilidade |
| **Desempenho** | Medição de latência, throughput e percentis | JMeter, k6, Gatling | Séries temporais, gráficos e relatório de percentis |
| **Carga** | Aumento controlado de usuários/transações até a carga esperada | JMeter, k6, Gatling | Curva carga × resposta, throughput e taxa de erros |
| **Estresse** | Carga além do limite, spike e teste de exaustão | JMeter, k6, Gatling | Ponto de saturação, sintomas da falha e tempo de recuperação |
| **Segurança** | SAST, DAST, análise de dependências e teste de penetração | OWASP ZAP, Burp Suite, Semgrep | Relatório de vulnerabilidades, severidade e evidências |
| **Confiabilidade** | Endurance/soak, injeção de falhas e análise de MTBF/MTTR | JMeter, Chaos Mesh, Prometheus | Disponibilidade, MTBF, MTTR, incidentes e séries temporais |
| **Regressão** | Reteste, seleção e priorização de casos | JUnit/pytest, Playwright, GitHub Actions | Histórico de builds, comparação de resultados e tendências |
| **Recuperação** | Falha e restauração de serviço, backup/restore e disaster recovery | LitmusChaos, Chaos Mesh, scripts de infraestrutura | RTO/RPO observado, logs e relatório de recuperação |
| **Configuração** | Matriz de SO, navegador, versão e hardware | Selenium Grid, BrowserStack | Matriz de compatibilidade, capturas e relatório por ambiente |

Referências técnicas principais:

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Postman - Test APIs](https://learning.postman.com/docs/tests-and-scripts/write-scripts/test-scripts/)
- [Playwright - Test reports](https://playwright.dev/docs/test-reporters)
- [Apache JMeter User Manual](https://jmeter.apache.org/usermanual/)
- [OWASP Web Security Testing Guide](https://owasp.org/www-project-web-security-testing-guide/)
- [Chaos Mesh Documentation](https://chaos-mesh.org/docs/)
- [Selenium Grid Documentation](https://www.selenium.dev/documentation/grid/)

### 3. Exemplos de relatórios de execução

Os trechos abaixo são **modelos de estrutura**, e não resultados de testes executados no projeto WEBI.

#### a) Teste de unidade - JUnit XML

```xml
<testsuite name="CalculadoraTest" tests="4" failures="1" errors="0" skipped="0" time="0.031">
  <testcase name="deveSomarDoisInteiros" time="0.004" />
  <testcase name="deveRejeitarDivisaoPorZero" time="0.003">
    <failure message="Era esperada uma exceção" />
  </testcase>
</testsuite>
```

O relatório informa suíte, quantidade de testes, falhas, erros, ignorados e duração. Esse formato pode ser consumido por ferramentas de CI.

#### b) Teste de API - Postman/Newman

```text
Collection: API de Usuários
Requests: 12 | Failed requests: 0
Assertions: 36 | Failed assertions: 1
Average response time: 184 ms
Failure: POST /users - expected status 201, received 400
```

O Newman pode gerar saída de terminal, JSON e JUnit, permitindo registrar requisições, asserções, tempos e falhas.

#### c) Teste de desempenho - JMeter

```text
Label          Samples  Average  P95   Error %  Throughput
Login             1000   210 ms  430 ms   0.20%   48.5/s
Listar produtos   1000   315 ms  710 ms   1.10%   39.2/s
```

O dashboard do JMeter apresenta latência, percentis, throughput, erros e evolução temporal, permitindo comparar o comportamento do sistema sob carga.

Referências dos formatos: [JUnit Platform reporting](https://docs.junit.org/current/user-guide/#running-tests-build-maven), [Newman reporters](https://learning.postman.com/docs/collections/using-newman-cli/newman-built-in-reporters/) e [JMeter dashboard](https://jmeter.apache.org/usermanual/generating-dashboard.html).

---

## Atividade 6 - Histórico e Evolução

### 1. Primeiras iniciativas de teste de software

Nos primeiros computadores, teste e depuração eram atividades pouco separadas: o programador executava o código, inspecionava resultados e corrigia o problema encontrado. Com o crescimento dos sistemas, a área passou a distinguir a execução planejada para revelar falhas da depuração usada para localizar e corrigir suas causas.

Marcos relevantes incluem a discussão inicial sobre verificação de rotinas na década de 1940, a separação progressiva entre *testing* e *debugging* nos anos 1950 e a consolidação de princípios e técnicas nas décadas seguintes. Em 1979, Glenford Myers publicou *The Art of Software Testing*, defendendo que um caso bem-sucedido é aquele com alta probabilidade de revelar um erro. Gelperin e Hetzel sistematizaram essa evolução histórica em 1988.

**Referência:** GELPERIN, D.; HETZEL, B. *The Growth of Software Testing*. Communications of the ACM, 1988. [DOI 10.1145/62959.62965](https://doi.org/10.1145/62959.62965).

### 2. Como são feitos testes no Google

O modelo descrito em *How Google Tests Software* distribui a responsabilidade pela qualidade entre desenvolvimento e especialistas em teste. Historicamente, o livro descreve papéis como SWE, SET e TE, mas não estabelece uma proporção fixa de um SET e um TE para cada desenvolvedor.

Na prática divulgada pelo Google:

- desenvolvedores são responsáveis por testes do próprio código;
- testes pequenos, rápidos e isolados formam a base, enquanto testes maiores são usados de forma seletiva;
- automação, revisão de código e infraestrutura compartilhada fornecem retorno frequente;
- o guia 70/20/10 é uma heurística, não uma regra universal: aproximadamente 70% testes pequenos, 20% médios e 10% grandes;
- cobertura é um indicador útil, mas não prova ausência de defeitos nem substitui bons oráculos e casos de teste.

Referências: WHITTAKER, J.; ARBON, J.; CAROLLO, J. *How Google Tests Software*. Addison-Wesley, 2012; Google Testing Blog, [Just Say No to More End-to-End Tests](https://testing.googleblog.com/2015/04/just-say-no-to-more-end-to-end-tests.html), 2015.

### 3. Autor de referência

**Glenford J. Myers** é uma referência histórica em testes de software. Em *The Art of Software Testing* (1979), ajudou a consolidar a visão de que o objetivo do teste é revelar defeitos, e não demonstrar que o programa está correto. O livro também difundiu técnicas como particionamento de equivalência e análise de valores-limite.

**Referência:** MYERS, G. J.; SANDLER, C.; BADGETT, T. *The Art of Software Testing*. 3. ed. Wiley, 2011. ISBN 978-1-118-03196-4.

### 4. Conteúdo de um plano de testes

Um plano deve definir, no mínimo:

1. identificador, objetivo e referências do plano;
2. escopo, itens e versões que serão testados;
3. funcionalidades incluídas e excluídas, com justificativas;
4. riscos do produto e prioridades;
5. abordagem, níveis, tipos e técnicas de teste;
6. casos, dados, ambiente, ferramentas e automações;
7. critérios de entrada, aprovação, suspensão, retomada e encerramento;
8. resultados e evidências que serão produzidos;
9. papéis, responsabilidades e necessidades de treinamento;
10. cronograma, estimativas, dependências e contingências;
11. processo de registro, classificação e acompanhamento de incidentes;
12. métricas e forma de comunicação dos resultados.

O IEEE 829 é uma referência histórica para documentação de testes, mas foi substituído. Uma referência atual é a **ISO/IEC/IEEE 29119-3**, que trata da documentação de teste e inclui modelos para planos, especificações, relatórios e incidentes.

**Referência:** ISO. [ISO/IEC/IEEE 29119-3 - Test Documentation](https://www.iso.org/standard/79429.html).

---

## Conferência de atendimento ao slide

- [x] Projetos anteriores e testes realizados.
- [x] Testes descritos com os conceitos apresentados e exemplos práticos.
- [x] Artigos acadêmicos para erro, falha, defeito e bug.
- [x] Casos graves e relato de melhoria, com referências.
- [x] Vagas júnior, plena e sênior, com habilidades e referências.
- [x] Abordagem das empresas, perfil profissional e perspectivas futuras.
- [x] Técnicas, ferramentas e artefatos para todos os tipos vistos.
- [x] Três exemplos de relatórios de execução.
- [x] Histórico, Google, autor de referência e plano de testes.
