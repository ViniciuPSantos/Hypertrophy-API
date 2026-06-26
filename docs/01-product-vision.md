# 01 — Visão de Produto

---

## 1. Visão (Product Vision Statement)

> Para praticantes de musculação sérios sobre seu progresso, que estão cansados de adivinhar se estão treinando e comendo certo, o **Hypertrophy OS** é um sistema operacional de evolução física que substitui a adivinhação por decisões guiadas por dados e ciência. Diferente de registradores de treino (Hevy, Strong) ou contadores de macro (MyFitnessPal), o Hypertrophy OS não apenas registra — ele **decide com você**, explicando o porquê de cada recomendação usando o seu próprio histórico.

### 1.1 Visão de longo prazo (3 a 5 anos)

O Hypertrophy OS se torna o "segundo cérebro" da evolução física do usuário: ele sabe, antes do usuário perceber consciamente, quando um deload é necessário, quando um músculo está sendo subtreinado por semanas consecutivas, ou quando a aderência alimentar caiu por causa de uma mudança de rotina. Ele não substitui um personal trainer ou nutricionista humano — ele é o assistente que torna o tempo desses profissionais (quando existem) mais eficiente, e que sozinho já é suficiente para a grande maioria dos praticantes intermediários que treinam por conta própria.

---

## 2. O Problema

### 2.1 Problema central

Pessoas que treinam hipertrofia por conta própria (sem coach ou com coach de baixo acompanhamento) tomam decisões de treino e dieta com informação incompleta e sem ajuste contínuo. Os sintomas observáveis desse problema:

- **Progressão estagnada sem causa aparente** — a pessoa treina, mas não sabe se o problema é volume insuficiente, fadiga acumulada, sono ruim, ou proteína baixa, porque nenhuma dessas variáveis está conectada às outras em um único sistema.
- **Planilhas e apps fragmentados** — um app para treino, outro para dieta, outro para sono (ou nenhum), nenhum conversando entre si. A pessoa é o "integrador manual" dos próprios dados, e isso é trabalho cognitivo que ninguém quer fazer todos os dias.
- **Recomendações genéricas demais ou ausentes** — conteúdo de academia hoje é "assista um vídeo de YouTube e aplique", sem personalização real ao histórico daquela pessoa específica.
- **Falta de explicação gera desconfiança e abandono** — quando um app ou treinador diz "faça assim" sem justificar, a pessoa não internaliza o aprendizado e abandona o método na primeira dificuldade.

### 2.2 Por que agora

Modelos de linguagem maduros tornam viável, por um custo operacional baixo, ter algo equivalente a um treinador experiente disponível 24/7, capaz de processar contexto multivariado (treino + dieta + sono + recuperação + histórico) e gerar explicações em linguagem natural — algo que até poucos anos atrás exigiria um sistema de regras extremamente rígido e caro de manter.

### 2.3 Não-problemas (escopo negativo explícito)

Para deixar claro o que o produto **não** resolve, mesmo que pareça relacionado:

- Não é uma rede social fitness (sem feed, sem seguir pessoas, sem likes).
- Não compete em ter o maior catálogo de exercícios ou na velocidade de registro de séries comparado a apps especializados nisso (Hevy/Strong fazem isso muito bem).
- Não é um substituto de diagnóstico médico ou de fisioterapia para lesões.
- Não é (nesta fase) uma ferramenta para perda de peso ou estética geral — o foco é hipertrofia.

---

## 3. Personas

### 3.1 Persona primária — "O Praticante Orientado a Dados"

- **Quem é:** 24–38 anos, treina há 1–5 anos, já passou da fase de "qualquer treino funciona", quer otimizar. Provavelmente já usou planilhas ou um app de registro antes.
- **Frustração principal:** sabe que tem informação suficiente (treinos antigos, peso, fotos) mas não sabe interpretá-la de forma agregada.
- **O que valoriza:** explicações baseadas em evidência, não querer "se sentir enganado" por modismos fitness.
- **Cenário de uso típico:** abre o app antes do treino para ver o que fazer hoje, registra rapidamente durante o treino, e — esse é o ponto-chave — confia o suficiente na recomendação para não precisar pesquisar "será que devo aumentar a carga" no Google.
- **Esta é a persona inicial e principal do MVP**, dado que o app nasce do uso pessoal do fundador, que se encaixa neste perfil.

### 3.2 Persona secundária — "O Iniciante Comprometido"

- **Quem é:** primeiros 12 meses de treino, motivado mas inseguro sobre técnica, volume e progressão.
- **Frustração principal:** excesso de informação contraditória na internet; medo de "treinar errado".
- **O que valoriza:** ser guiado passo a passo, sem precisar tomar decisões complexas sozinho.
- **Implicação de produto:** a IA precisa adaptar o nível de detalhe da explicação — um iniciante quer confiança, não um artigo científico.

### 3.3 Persona terciária (visão futura, pós-v2) — "O Treinador com Múltiplos Clientes"

- Considerada apenas para fundamentar decisões arquiteturais que não fechem essa porta no futuro (ex: modelo de dados que já suporte um "treinador" vinculado a múltiplos "usuários"), mas **não é construída nem desenhada em UX nesta fase**.

---

## 4. Diferenciais (Why Us)

| Diferencial | Por que importa | Concorrente que não faz isso |
|---|---|---|
| IA que decide e explica, não apenas registra | Quebra o ciclo de "treinar no escuro" | Hevy, Strong (são registradores puros) |
| Integração nativa treino + nutrição + sono + recuperação em uma única fonte de verdade | Decisões de treino sem contexto de recuperação são incompletas | MyFitnessPal (só dieta), Hevy (só treino) |
| "Minha Cozinha" — planejamento de refeições com o que a pessoa **realmente tem em casa** | Resolve o problema real ("não vou comprar 15 ingredientes pra uma receita") | Apps de dieta genéricos assumem acesso irrestrito a qualquer alimento |
| Evolution Score como índice único e proprietário | Dá ao usuário uma "pontuação de saúde do progresso" sem precisar interpretar 7 gráficos separados | Nenhum concorrente direto tem um índice composto equivalente |
| Toda recomendação rastreável a dado + evidência | Constrói confiança de longo prazo, evita abandono por desconfiança | Apps de IA fitness genéricos tendem a ser "caixa-preta" |

---

## 5. Filosofia do Produto (princípios de decisão)

Estes princípios servem como **filtro de decisão** para qualquer funcionalidade nova proposta no futuro. Antes de adicionar algo ao roadmap, ele deve passar pelo teste abaixo.

### 5.1 O teste de toda funcionalidade

> "O que vai ajudar o usuário a evoluir mais rapidamente e manter consistência?"

Se uma feature não responde claramente a essa pergunta, ela não entra no produto — não importa quão "legal" pareça (ex: gamificação social, badges, rankings entre amigos).

### 5.2 Três coisas que o produto explicitamente não é

1. **Não é um registrador de treinos.** Registrar é um meio, nunca o fim. A interface de registro existe só para alimentar a IA com dados — ela deve ser a parte mais rápida e menos "pensada" do app.
2. **Não é uma rede social fitness.** Comparação social é um motivador frágil e às vezes tóxico (ansiedade, comparação descendente/ascendente prejudicial). O motivador central do produto é o progresso individual versus o próprio histórico.
3. **Não compete de frente com apps especializados.** Hevy/Strong otimizam para velocidade de registro; MyFitnessPal otimiza para banco de dados de alimentos. O Hypertrophy OS não tenta vencer nesses eixos — ele vence ao **conectar** o que esses apps deixam fragmentado, e ao decidir, não apenas registrar.

### 5.3 A IA como centro do produto, não como recurso adicional

Isso tem implicações concretas de arquitetura e roadmap (detalhadas em [03-ia-conceito.md](./03-ia-conceito.md) e [06-arquitetura.md](./06-arquitetura.md)):

- Toda tela de registro de dado (peso, treino, refeição, sono) deve ter, como contrapartida, um caminho claro para a IA **usar** aquele dado em uma recomendação dentro de, no máximo, alguns dias — caso contrário, o usuário sente que está "alimentando um sistema que não retribui".
- Nenhuma recomendação pode ser puramente estática/regra fixa de marketing (ex: "beba 3 litros de água" sem relação ao peso, clima, ou volume de treino do usuário). Mesmo quando o sistema usa heurísticas simples no MVP, elas devem ser parametrizadas pelos dados do usuário.

---

## 6. Princípios de plataforma e UX que decorrem da visão

Como o app será usado **várias vezes por dia**, decisões de produto priorizam:

- **Registro em menos de 30 segundos** como meta de UX não-negociável (detalhado em [02-prd-requisitos.md](./02-prd-requisitos.md), RNF de performance/UX).
- **Interface de uma mão só** — a pessoa está literalmente no meio de uma série de exercício quando interage com o app.
- **Foco de uso é o iPhone do usuário**, entregue via PWA instalável na tela de início (ver [06-arquitetura.md](./06-arquitetura.md) §1) enquanto Mac/Apple Developer não estiverem disponíveis para um app nativo; a experiência (visual, fluidez, fricção mínima) é desenhada para se aproximar o máximo possível de um app nativo iOS dentro dessa restrição, com migração para SwiftUI planejada para quando essa restrição deixar de existir.

---

## 7. Métricas de sucesso da visão (alto nível)

Detalhamento completo de KPIs e eventos de analytics está em [08-roadmap-backlog.md](./08-roadmap-backlog.md). Em nível de visão, o produto é bem-sucedido se:

1. O usuário confia o suficiente nas recomendações da IA para segui-las sem precisar validar externamente (proxy: taxa de aceitação de recomendações de treino).
2. O tempo entre "abrir o app" e "começar a registrar o treino" tende a zero.
3. A consistência (aderência semanal ao plano) aumenta ao longo do tempo de uso, não apenas nas primeiras semanas (efeito de novidade).
