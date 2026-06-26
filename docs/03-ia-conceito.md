# 03 — Conceito da IA

---

## 1. O que a IA é (e o que ela não é)

A IA do Hypertrophy OS **não é um chatbot genérico com acesso aos dados do usuário**. Essa distinção é importante porque muda a arquitetura: um chatbot responde perguntas quando perguntado; o Hypertrophy OS precisa de um sistema que **toma decisões proativamente**, com ou sem o usuário perguntar algo.

Na prática, isso significa que a IA tem dois modos de operação:

1. **Modo proativo (motor de decisão):** roda em background/cadência programada (ex: ao final de cada sessão de treino, semanalmente para revisão de plano, diariamente para sugestão de refeição) e gera recomendações sem que o usuário precise pedir.
2. **Modo reativo (assistente conversacional):** responde perguntas diretas do usuário ("o que eu como hoje?", "por que você sugeriu isso?"), mas sempre fundamentado no mesmo contexto de dados do modo proativo — nunca como um LLM "isolado" sem acesso ao histórico.

Tecnicamente, isso é melhor entendido como um **sistema de recomendação orientado a regras + LLM**, não um chatbot com RAG simples. Detalhes de implementação em §6.

---

## 2. Dados de entrada (o "contexto" da IA)

Toda decisão da IA deve, sempre que possível, considerar a combinação destes dados (não isoladamente):

| Categoria | Dados específicos |
|---|---|
| Treino | histórico de cargas, RPE por série, volume semanal por grupo muscular, aderência ao plano, exercícios evitados/preferidos |
| Corporal | peso (com média móvel), percentual de gordura, medidas, fotos com timestamp |
| Nutrição | ingestão de calorias/macros realizada vs. meta, itens disponíveis em Minha Cozinha, preferências/restrições |
| Recuperação | sono (duração, qualidade), hábitos de mobilidade/alongamento, autorrelato de fadiga |
| Contexto declarado | objetivo, experiência, disponibilidade de tempo/equipamento, limitações físicas, orçamento |
| Feedback explícito | aceitação/rejeição/ajuste de recomendações anteriores |

### 2.1 Princípio de design: nenhuma decisão "de variável única"

Uma regra de produto explícita: **a IA nunca deve tomar uma decisão de treino considerando apenas a variável "carga e repetições" sem checar recuperação**, nem decisão de dieta considerando só "macros" sem checar orçamento/disponibilidade. Isso é o que diferencia o produto de uma calculadora de progressão linear simples.

---

## 3. Lógica de decisão — Treino

### 3.1 Geração do plano inicial

Ao gerar o primeiro plano (ou um plano novo após mudança relevante de contexto), a IA segue esta ordem de decisão:

1. **Definir divisão de treino (split)** com base em frequência semanal disponível e experiência (ex: full body para 2-3x/semana, upper/lower para 4x, push/pull/legs para 5-6x — heurísticas de ponto de partida, refinadas por evidência de volume por grupo muscular).
2. **Alocar volume semanal por grupo muscular** dentro das faixas geralmente associadas a hipertrofia (aproximadamente 10–20 séries semanais por grupo muscular para praticantes intermediários, ajustado para baixo em iniciantes e ajustado por grupo muscular prioritário/atrasado declarado pelo usuário).
3. **Selecionar exercícios** compatíveis com equipamento disponível, respeitando exclusões por limitação física (RN-002 em 02-prd-requisitos.md), priorizando padrões de movimento fundamentais antes de variações de isolamento.
4. **Definir esquema de repetições e intensidade** alinhado a hipertrofia (faixas moderadas, tipicamente 6–15 repetições efetivas, sem exclusividade — variação de faixas é aceita e frequentemente desejável).
5. **Gerar explicação do plano completo**, articulando o porquê da divisão, do volume e dos exercícios escolhidos especificamente para aquele usuário.

### 3.2 Progressão semana a semana

A cada sessão registrada, a IA reavalia o exercício específico (não o plano inteiro) com a seguinte lógica:

```
SE todas as séries planejadas foram completadas
E RPE médio reportado está abaixo do limiar de esforço máximo esperado
ENTÃO sugerir progressão de carga (incremento pequeno, ex: 2,5–5%)//
   OU progressão de repetição (se incremento de carga não for praticável, ex: halteres com saltos grandes)

SE séries não foram completadas OU RPE está no limite máximo
ENTÃO manter carga atual na próxima sessão, sem progressão

SE RPE está consistentemente baixo (sub-esforço) por múltiplas sessões
ENTÃO sugerir incremento maior que o padrão, sinalizando que a carga pode estar conservadora
```

Esta lógica é a aplicação direta do princípio de **progressão de sobrecarga progressiva** orientado por esforço percebido (RPE), e não apenas por porcentagem fixa de carga — abordagem mais adaptativa e mais alinhada à literatura de hipertrofia do que tabelas de progressão lineares fixas.

### 3.3 Detecção de platô e troca de exercício

Platô é detectado quando, para um exercício específico, não há progressão de carga nem de repetição por um número sustentado de sessões consecutivas (parâmetro inicial sugerido: 3–4 sessões sem progresso, ajustável). Resposta da IA, em ordem de preferência (menos disruptivo primeiro):

1. Ajustar variável de treino (tempo de descanso, técnica de execução, leve ajuste de volume) antes de trocar o exercício.
2. Se o platô persistir, sugerir substituição por exercício do mesmo padrão de movimento (ex: trocar supino reto por supino inclinado, não abandonar "peito" como grupo).
3. Sempre explicar a troca citando o histórico específico que a motivou.

### 3.4 Detecção de necessidade de deload

Conforme RN-102 (02-prd-requisitos.md), deload exige **sinais combinados**, nunca um único indicador:

- Queda de performance (carga ou repetições) em múltiplos exercícios na mesma semana, **e**
- RPE subindo para a mesma carga relativa (sinal de fadiga acumulada), **e**
- Pelo menos um sinal de recuperação comprometida (sono abaixo da média do próprio usuário, ou autorrelato de fadiga alta, ou queda de aderência a hábitos de recuperação).

Quando os três sinais coincidem, a IA sugere uma semana de deload (redução de volume e/ou intensidade, mantendo frequência), sempre explicando quais sinais especificamente motivaram a sugestão — para que o usuário aprenda a reconhecer os próprios sinais de fadiga ao longo do tempo.

### 3.5 Técnicas avançadas

Sugestões de técnicas avançadas (drop-set, rest-pause, cluster sets, etc.) só são apresentadas quando: (a) o usuário já demonstra histórico de execução consistente e completude de série padrão, e (b) o objetivo/contexto justifica a técnica (ex: quebra de platô, ou economia de tempo quando disponibilidade caiu). Nunca apresentadas a usuários classificados como iniciantes na primeira fase de uso — a complexidade adicional teria custo de aprendizado maior que o benefício.

---

## 4. Lógica de decisão — Nutrição

### 4.1 Cálculo de necessidades

Ponto de partida: estimativa de gasto energético com base em peso, altura, idade (quando disponível), nível de atividade declarado e ajuste por objetivo (superávit moderado para ganho de massa, manutenção, ou déficit moderado — sempre dentro de faixas conservadoras, nunca extremas). Proteína é tratada como a variável mais crítica e menos negociável do plano nutricional para hipertrofia, com meta diária calculada por kg de peso corporal e priorizada nas sugestões de refeição mesmo quando isso exige flexibilizar carboidrato/gordura.

### 4.2 Geração de refeições — algoritmo conceitual

```
ENTRADA: macro-alvo restante do dia, itens disponíveis em Minha Cozinha,
         preferências/restrições, horário/contexto declarado pelo usuário

1. Filtrar alimentos disponíveis compatíveis com restrições declaradas
2. Priorizar combinações que atinjam a meta de proteína do macro restante
3. Ajustar porções para aproximar carboidrato/gordura da meta sem extrapolar
4. SE nenhuma combinação satisfatória existir com o estoque atual:
     a. Identificar o menor conjunto de itens faltantes que resolveria o problema
     b. Sugerir a refeição mais próxima possível MESMO ASSIM, sinalizando a lacuna
     c. Adicionar item(s) faltantes à Lista de Compras sugerida
5. Retornar sugestão + explicação (ex: "Esta combinação cobre 80% da sua meta
   de proteína do dia usando o que você já tem em casa")
```

### 4.3 Respostas a perguntas contextuais (linguagem natural)

Perguntas como "o que almoço hoje", "estou sem tempo", "tenho só ovos e arroz", "quero economizar essa semana" são tratadas como **variações de parâmetros de entrada** do mesmo algoritmo de §4.2, não como casos especiais com lógica própria:

| Pergunta | Parâmetro afetado |
|---|---|
| "Estou sem tempo" | Filtra por tempo de preparo declarado (baixo) |
| "Tenho só ovos e arroz" | Restringe Minha Cozinha temporariamente a esse subconjunto para esta consulta |
| "Quero economizar essa semana" | Prioriza itens já em estoque, evita sugerir compras na Lista |
| "O que posso comer antes do treino?" | Filtra por digestibilidade/timing pré-treino (menor gordura/fibra, carboidrato moderado) |

Esse desenho é importante para a arquitetura (ver [06-arquitetura.md](./06-arquitetura.md)): significa que o componente de linguagem natural (LLM) atua como uma **camada de extração de parâmetros** sobre um algoritmo determinístico de geração de refeição — não como um gerador de texto livre que "inventa" a refeição sem garantia de aderência real ao estoque e à meta de macro. Essa separação é o que garante que a IA nunca sugira algo nutricionalmente incoerente, mesmo usando um LLM na camada conversacional.

---

## 5. Evolution Score — especificação do índice

### 5.1 Composição

O Evolution Score é um índice de 0 a 100, recalculado diariamente, composto por (pesos iniciais sugeridos, ajustáveis com dados reais de uso):

| Componente | Peso sugerido | O que mede |
|---|---|---|
| Aderência ao treino | 25% | % de sessões planejadas realizadas na semana corrente |
| Proteína | 15% | % da meta diária de proteína atingida (média dos últimos dias) |
| Sono | 15% | Duração e qualidade relativa à própria média do usuário |
| Hidratação | 10% | % da meta diária atingida |
| Recuperação percebida | 15% | Combinação de RPE médio recente + autorrelato, indicando se a fadiga está sob controle |
| Consistência geral | 10% | Regularidade de registro/uso do app (proxy de engajamento saudável com o processo) |
| Evolução corporal | 10% | Tendência (média móvel) de peso/medidas em direção à meta declarada |

> **Nota de implementação:** os pesos acima são um ponto de partida razoável, não um valor científico fixo. Devem ser tratados como configuráveis (ver [07-modelagem-dados.md](./07-modelagem-dados.md) — tabela de configuração de score) e revisados após acúmulo de dados reais de uso pessoal.

### 5.2 Regra de explicabilidade (RN-901, já citada no PRD)

O score nunca aparece isolado. A UI sempre mostra: o número, a tendência (subindo/estável/caindo nos últimos dias), e o componente de menor desempenho relativo com uma ação concreta sugerida (ex: "Sua hidratação está baixando seu score. Beber mais 500ml hoje já ajudaria.").

### 5.3 Por que um índice composto (trade-off)

**Alternativa considerada:** mostrar cada métrica separadamente (7 gráficos), sem agregação.

**Por que foi descartada como visão principal do Dashboard:** o objetivo do Dashboard é ser compreendido em menos de 5 segundos (RF-801). Sete métricas separadas exigem que o usuário faça a agregação mentalmente — exatamente o trabalho cognitivo que o produto promete eliminar. O índice composto resolve isso, **desde que sempre acompanhado da decomposição** (§5.2) para não se tornar uma "caixa-preta" que contradiria a filosofia de transparência do produto. As métricas individuais continuam disponíveis em detalhe, apenas não são a visão padrão do Dashboard.

---

## 6. Banco de Conhecimento Científico

### 6.1 Papel do banco de conhecimento

A IA nunca deve gerar uma recomendação de treino ou nutrição "do zero" via geração livre de texto do LLM. O LLM atua sobre uma **base de conhecimento estruturada** que codifica princípios estabelecidos de:

- Volume, intensidade e frequência de treino para hipertrofia
- Recuperação e periodização (incluindo lógica de deload)
- Biomecânica básica de exercícios (para exclusões por limitação física)
- Nutrição esportiva (necessidades de proteína, timing, déficit/superávit conservador)

### 6.2 Por que não usar o LLM "puro" para gerar recomendações (trade-off central da arquitetura de IA)

**Opção A — LLM gera a recomendação diretamente a partir do histórico do usuário (prompt com contexto, sem regras intermediárias).**
- Vantagem: implementação inicial mais rápida, flexibilidade textual alta.
- Risco: LLMs podem "alucinar" parâmetros numéricos (cargas, percentuais, calorias) de forma inconsistente entre execuções, e é mais difícil garantir auditabilidade e consistência das regras de negócio críticas (ex: nunca sugerir deload sem os 3 sinais combinados de RN-102).

**Opção B — Sistema de regras/heurísticas determinísticas calcula a recomendação numérica; o LLM é usado apenas para gerar a explicação em linguagem natural e para interpretar perguntas em linguagem natural do usuário (extração de parâmetros, como em §4.3).**
- Vantagem: garante consistência e auditabilidade total das decisões numéricas (essencial para confiança e para RF-1002 — toda recomendação deve ter explicação rastreável); o LLM brilha exatamente onde é mais forte (linguagem) e não onde é mais fraco (aritmética e consistência de regra determinística).
- Custo: exige desenhar e manter as regras/heurísticas explicitamente (este documento já é o início desse trabalho, em §3 e §4).

**Decisão: Opção B.** Esta é a decisão arquitetural mais importante do conceito de IA do produto. O LLM é a "camada de linguagem" (explica, conversa, interpreta perguntas abertas); o motor de regras é a "camada de decisão" (calcula volume, carga, macro, score). Essa separação é o que permite ao produto cumprir sua promessa central de transparência e justificativa baseada em evidência — uma recomendação numérica gerada por regra explícita é, por definição, auditável; uma gerada por texto livre de LLM não é, com a tecnologia atual.

### 6.3 Evolução futura do banco de conhecimento

Para v2+, o banco de conhecimento pode evoluir de heurísticas codificadas manualmente para um sistema que ajusta seus próprios parâmetros com base em dados agregados de aderência e resultado (ex: "para este perfil de usuário, incrementos de carga de X% têm taxa de sucesso maior que Y%") — mas isso é explicitamente fora do escopo do MVP e da v1, e depende de volume de dados que só existe com base de usuários maior (ver [08-roadmap-backlog.md](./08-roadmap-backlog.md)).

---

## 7. Histórico de Decisões da IA (auditoria)

Toda saída do motor de decisão (treino ou nutrição) gera um registro no Histórico de Decisões da IA contendo:
- Snapshot dos dados de entrada relevantes no momento da decisão
- A recomendação gerada (estruturada, não apenas o texto)
- A explicação textual apresentada ao usuário
- Resposta do usuário (aceitou / ajustou — com o que foi ajustado / rejeitou)

Esse histórico tem três funções: (1) permitir ao usuário revisar decisões passadas, (2) permitir depuração e melhoria do motor de regras pelo desenvolvedor, (3) constituir a base de dados que, no futuro, alimentaria qualquer evolução do banco de conhecimento citada em §6.3.
