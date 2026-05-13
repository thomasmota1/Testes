# Relatorio de atividades

**Data:** 27/04/2026 - 29/04/2026

---

## Aula 1 - Introdução a Testes e Manutenção de Software

## Atividade 1: Outros Casos Notáveis de Falhas em Software

### Pesquisa sobre casos de grande repercussão:

#### 1. Bug do Milênio (Y2K)
**O que aconteceu:** O Bug do Milênio foi uma falha de design onde sistemas computacionais representavam anos com apenas dois dígitos (ex: "99" para 1999). Com a virada para 2000, havia o risco de sistemas interpretarem "00" como 1900, causando falhas em cálculos de datas, sistemas financeiros, aeroportos, usinas e infraestrutura crítica.

**Impactos:** Estima-se que foram gastos mais de US$ 300 bilhões globalmente para corrigir o problema. Apesar disso, os danos foram minimizados com muito esforço na correção.

**Vilões:** Decisões de economia de memória nas décadas anteriores, falta de visão de longo prazo no design de sistemas.

#### 2. Queda do Sistema de Informática da British Airways (2017)
**O que aconteceu:** Uma falha no data center causou o cancelamento de mais de 400 voos em Heathrow e Gatwick, afetando cerca de 75.000 passageiros. Um técnico desligou acidentalmente uma fonte de energia e, ao religá-la, causou uma sobrecarga que danificou servidores.

**Impactos:** Prejuízo estimado em £80 milhões, danos à reputação, caos nos aeroportos por vários dias.

**Vilões:** Falta de redundância adequada, procedimentos de recuperação de desastres inadequados, terceirização da equipe de TI.

#### 3. Interrupção do Serviço AWS S3 da Amazon (2017)
**O que aconteceu:** Um comando de manutenção digitado incorretamente por um funcionário removeu mais servidores do que o pretendido do subsistema de índice do S3. Isso causou uma interrupção de 4 horas que afetou milhares de sites e serviços.

**Impactos:** Estimativa de US$ 150-160 milhões em perdas para empresas do S&P 500. Sites como Slack, Trello, Quora e serviços de IoT ficaram offline.

**Vilões:** Erro humano, ferramentas de administração sem validação adequada, falta de mecanismos de confirmação para comandos destrutivos.

#### 4. CrowdStrike e Microsoft - Tela Azul (2024)
**O que aconteceu:** Uma atualização defeituosa do software de segurança CrowdStrike Falcon causou "tela azul da morte" (BSOD) em milhões de computadores Windows globalmente. Aeroportos, hospitais, bancos e empresas foram afetados.

**Impactos:** Estimativa de prejuízo de mais de US$ 10 bilhões. Voos cancelados mundialmente, sistemas hospitalares comprometidos, operações bancárias suspensas.

**Vilões:** Falta de testes adequados antes do deploy, ausência de rollout gradual, atualização automática sem validação em ambiente de produção.

### Respostas das questões:

**Quais são os principais vilões nessas histórias?**
- Falta de testes adequados e cobertura insuficiente
- Decisões de design míopes (economia de recursos vs. sustentabilidade)
- Ausência de redundância e planos de contingência
- Erro humano combinado com falta de validações
- Excesso de confiança em sistemas automatizados

**O que poderia ser feito para evitar tais problemas?**
- Testes rigorosos em ambientes de staging antes de produção
- Implementação de rollout gradual (canary deployments)
- Redundância em sistemas críticos
- Validações e confirmações para comandos destrutivos
- Revisão de código e processos independentes
- Planos de recuperação de desastres testados regularmente

**Como você avalia a qualidade desses softwares?**
A qualidade foi comprometida em todos os casos por falhas em diferentes aspectos como: confiabilidade, manutenibilidade, e resiliência. Os sistemas não atendiam aos requisitos implícitos de segurança e continuidade de operação.

**Qual é a relação entre os casos anteriores?**
Todos envolvem falhas que poderiam ter sido evitadas com melhores práticas de engenharia de software, testes mais rigorosos e processos de qualidade bem definidos.

---

## Atividade 2: Características e Aspectos de Qualidade

### Características comuns aos softwares de baixa qualidade:
- Interface confusa ou mal projetada
- Mensagens de erro inadequadas ou inexistentes
- Falta de documentação
- Comportamento imprevisível
- Ausência de mecanismos de recuperação de falhas
- Falta de redundância em operações críticas

### Aspectos que definem questões de qualidade:
1. **Funcionalidade:** se o software faz o que deveria fazer
2. **Confiabilidade:** se o software funciona consistentemente
3. **Usabilidade:** se o software é fácil de usar e entender
4. **Eficiência:** se ele utiliza recursos de forma adequada
5. **Manutenibilidade:** se é fácil de corrigir e evoluir
6. **Portabilidade:** se funciona em diferentes ambientes

### O que poderia ser feito para melhorar:
- Implementar processos de revisão de código
- Aumentar cobertura de testes
- Adotar padrões de desenvolvimento
- Documentar adequadamente
- Realizar testes com usuários reais

### Onde os pontos falhos poderiam ser corrigidos:
- Na fase de requisitos (definição clara de necessidades)
- No design (arquitetura robusta e resiliente)
- Na implementação (boas práticas de codificação)
- Nos testes (cobertura adequada)
- Na manutenção (correção proativa de problemas)

---

## Atividade 3: Conceito de Qualidade de Software

### Comparação: Pressman vs. Hirama



Ambos destacam a importancia de atender necessidades e gerar valor. No entanto, é possivel destacar como diferenças:
- **Foco principal:** Pressman foca em gestão de qualidade e valor mensuravel, já Hirama foca nas expectativas dos stakeholders.
- **Perspectiva:** o Pressman é mais técnico, defendendo que a qualidade vem do equilíbrio entre um processo bem feito e um produto sólido ("como fazer" (método) e "o que entregar" (resultado)). O Hirama vai por outro caminho e foca no lado social, priorizando a visão das pessoas que fazem o projeto acontecer "quem" (as pessoas e suas expectativas).
- **Abordagem:** de Pressman é mais tecnica e gerencial, ja Hirama é mais centrada nos stakeholders.

### Minha definição de Qualidade de Software:
Qualidade de Software é a capacidade de um sistema atender as necessidades explícitas e implícitas dos seus stakeholders (usuários, desenvolvedores, clientes e gerentes), sendo confiável, fácil de usar, eficiente, manutenível e produzido por meio de processos bem definidos que minimizam defeitos e maximizam o valor entregue.

### Aplicação prática:

**Para avaliar software existente:**
1. Verificar se atende aos requisitos funcionais
2. Medir métricas de qualidade (bugs, performance, cobertura de testes)
3. Avaliar satisfação dos usuários
4. Analisar facilidade de manutenção do código
5. Verificar conformidade com padrões

**Para construir software com qualidade:**
1. Definir requisitos claros
2. Adotar metodologias ágeis com foco na qualidade
3. Implementar CI/CD com testes automatizados
4. Realizar revisões de código
5. Seguir padrões e boas práticas
6. Documentar bem

---

## Atividade 4: Normas de Qualidade

### Tabela de Correspondência de Termos

| Português | Inglês | Definição |
|-----------|--------|-----------|
| Funcionalidade | Functionality | Capacidade do software de prover funções que atendam necessidades declaradas e implícitas |
| Confiabilidade | Reliability | Capacidade de manter nível de desempenho sob condições estabelecidas por período de tempo |
| Usabilidade | Usability | Capacidade de ser compreendido, aprendido, operado e atraente ao usuário |
| Eficiência | Efficiency/Performance | Relação entre nível de desempenho e quantidade de recursos utilizados |
| Manutenibilidade | Maintainability | Capacidade de ser modificado, incluindo correções, melhorias e adaptações |
| Portabilidade | Portability | Capacidade de ser transferido de um ambiente para outro |
| Segurança | Security | Proteção de informações e dados contra acesso não autorizado |
| Compatibilidade | Compatibility | Capacidade de coexistir e trocar informações com outros sistemas |

### Evolução das Normas:
- **ISO/IEC 9126 (1991):** Primeira norma, 6 características principais
- **ISO/IEC 25010 (2011):** Adicionou Segurança e Compatibilidade, separou qualidade de produto e de uso
- **Família SQuaRE (2023-2024):** Reorganização completa, maior granularidade, novos conceitos como Capacidade de Interação e Flexibilidade

### Por que existem diferentes categorias na Família SQuaRE:
A família SQuaRE foi dividida em categorias (Modelos, Requisitos, Medidas, Avaliação, Gerenciamento) porque cada aspecto da qualidade requer tratamento específico. Isso permite que organizações adotem partes relevantes conforme sua necessidade e facilita a manutenção e evolução de cada norma independentemente.

---

## Atividade 5: Modelos de Maturidade de Processos

### CMMI - Níveis de Maturidade:

| Nível | Nome | Descrição |
|-------|------|-----------|
| 1 | Inicial | Processos imprevisíveis, reativos e caóticos |
| 2 | Gerenciado | Processos caracterizados por projeto, frequentemente reativos |
| 3 | Definido | Processos caracterizados pela organização, proativos |
| 4 | Quantitativamente Gerenciado | Processos medidos e controlados |
| 5 | Otimizado | Foco em melhoria contínua de processos |

### MPS-BR - Níveis:

| Nível | Nome | Processos |
|-------|------|-----------|
| G | Parcialmente Gerenciado | Gerência de Projetos, Gerência de Requisitos |
| F | Gerenciado | Aquisição, Garantia da Qualidade, Gerência de Configuração, Gerência de Portfólio, Medição |
| E | Parcialmente Definido | Avaliação e Melhoria do Processo Organizacional, Definição do Processo Organizacional, Gerência de Recursos Humanos, Gerência de Reutilização |
| D | Largamente Definido | Desenvolvimento de Requisitos, Integração do Produto, Projeto e Construção do Produto, Validação, Verificação |
| C | Definido | Gerência de Decisões, Gerência de Riscos, Desenvolvimento para Reutilização |
| B | Gerenciado Quantitativamente | Desempenho do Processo Organizacional, Gerência Quantitativa do Projeto |
| A | Em Otimização | Análise de Causas e Resolução, Inovação e Implantação na Organização |

### Como uma empresa pode se capacitar:
1. Avaliação inicial do estado atual dos processos
2. Contratação de consultoria especializada
3. Treinamento das equipes
4. Implementação gradual das práticas
5. Avaliação oficial por avaliadores credenciados
6. Manutenção e melhoria contínua

### Benefícios:
- Melhoria na qualidade dos produtos
- Maior previsibilidade de prazos e custos
- Redução de retrabalho
- Vantagem competitiva em licitações
- Reconhecimento no mercado

### Empresas certificadas (exemplos):
- Stefanini (CMMI nível 5)
- TOTVS (MPS-BR nível A)
- Serpro (MPS-BR)
- Dataprev (MPS-BR)

---

## Atividade 6: Boas Práticas

### Associações Profissionais:

**IEEE (Institute of Electrical and Electronics Engineers)**
- Maior organização técnica profissional do mundo
- Publica padrões como IEEE 830 (requisitos) e IEEE 1012 (V&V)
- Biblioteca: IEEE Xplore

**ACM (Association for Computing Machinery)**
- Maior sociedade educacional e científica de computação
- Publica revistas como Communications of the ACM
- Biblioteca: ACM Digital Library

**SBC (Sociedade Brasileira de Computação)**
- Principal associação brasileira na área
- Eventos: CBSOFT, SBES, SBQS
- Biblioteca: SOL

### Autores e Contribuições:

| Autor | Principais Contribuições |
|-------|-------------------------|
| **E.W. Dijkstra** | Programação estruturada, algoritmo de caminho mínimo, conceito de semáforos |
| **C.A.R. Hoare** | Lógica de Hoare, Quicksort, CSP (Communicating Sequential Processes) |
| **Barry Boehm** | Modelo COCOMO, modelo espiral, economia de engenharia de software |
| **Watts Humphrey** | PSP/TSP, fundador do programa de qualidade de software do SEI |
| **David Parnas** | Modularidade, ocultamento de informação, documentação de software |
| **Martin Fowler** | Refatoração, padrões de arquitetura empresarial, design ágil |
| **Robert C. Martin** | Princípios SOLID, Clean Code, Clean Architecture |
| **Kent Beck** | TDD, Extreme Programming, padrões de implementação |
| **Michael Feathers** | Working Effectively with Legacy Code, técnicas de refatoração |

---

## Registro de Progresso

### O que foi feito:
- [x] Pesquisa sobre casos reais de falhas em software
- [x] Analise das características de qualidade
- [x] Analise das definições de Pressman e Hirama
- [x] Pesquisa sobre normas ISO de qualidade
- [x] Estudo dos modelos CMMI e MPS-BR
- [x] Pesquisa sobre associações profissionais e autores

### O que ficou pendente:
- [ ] Baixar artigos específicos das bibliotecas digitais
- [ ] Pesquisar mais sobre empresas certificadas na região

### O que impediu:
- Acesso limitado a algumas bibliotecas digitais (necessário VPN ou assinatura institucional)

### O que será feito na sequência:
- Acessar bibliotecas via portal CAPES para baixar artigos
- Iniciar atividades da próxima aula
- Preparar material para apresentação de artigos

---

## Referências


- ISO/IEC 25010:2011 - Systems and software engineering — Systems and software Quality Requirements and Evaluation (SQuaRE). Disponível em: https://www.iso.org/standard/35733.html
- ISO/IEC 25000:2014 - SQuaRE — Guide to SQuaRE. Disponível em: https://www.iso.org/standard/64764.html
- CMMI Institute. **CMMI Development V2.0**. Disponível em: https://cmmiinstitute.com/cmmi
- SOFTEX. **MPS.BR - Melhoria de Processo do Software Brasileiro**. Disponível em: https://softex.br/mpsbr/
- BOEHM, B. W. **Software Engineering Economics**. IEEE Transactions on Software Engineering, v. SE-10, n. 1, p. 4-21, 1984. DOI: 10.1109/TSE.1984.5010193
- DIJKSTRA, E. W. **Go To Statement Considered Harmful**. Communications of the ACM, v. 11, n. 3, p. 147-148, 1968. DOI: 10.1145/362929.362947
- PARNAS, D. L. **On the Criteria To Be Used in Decomposing Systems into Modules**. Communications of the ACM, v. 15, n. 12, p. 1053-1058, 1972. DOI: 10.1145/361598.361623
