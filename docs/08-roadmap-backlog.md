# 08 — Roadmap, Backlog e Métricas

---

## 1. Roadmap por Versão

### 1.1 MVP (uso pessoal, validação do núcleo do produto)

Objetivo do MVP: validar que o motor de decisão da IA (treino + nutrição básica) gera valor real e sustentado no uso diário próprio, antes de qualquer investimento em polimento visual avançado ou em features de expansão.

**Incluído:**
- Onboarding completo (RF-001 a RF-004)
- Geração de plano de treino + execução de sessão + progressão automática básica (RF-101 a RF-108, exceto técnicas avançadas)
- Registro de peso/medidas com gráficos e média móvel (RF-401 a RF-403)
- Nutrição: cálculo de metas + Minha Cozinha + sugestão de refeição (RF-201 a RF-204, RF-301 a RF-303)
- Dashboard com Evolution Score (RF-801 a RF-902)
- Histórico de Decisões da IA, mesmo que com UI simples (RF-1001 a RF-1003)
- Modo claro/escuro

**Explicitamente fora do MVP:**
- Fotos de evolução (RF-404) — adiado para v1.1, pois exige Storage e UI de comparação que não bloqueiam a validação do motor de decisão.
- Técnicas avançadas de treino (§3.5 de 03-ia-conceito.md) — exige histórico de uso que não existe ainda no dia 1.
- Hábitos com streaks (RF-601/602) — valioso, mas não crítico para validar a tese central de IA-como-treinador.
- Qualquer integração com Apple Health/Watch.

### 1.2 Versão 1.1

- Fotos de evolução + comparação lado a lado + linha do tempo (RF-404)
- Hábitos com streaks (RF-601, RF-602, RN-601)
- Sono — registro manual (RF-701)
- Refinamento do Evolution Score com pesos calibrados por dados reais de uso pessoal acumulado no MVP

### 1.3 Versão 2

- Técnicas avançadas de treino (§3.5 de 03-ia-conceito.md), agora com histórico suficiente para qualificar usuários
- Múltiplos contextos de treino com troca fluida (refinamento de RF-003, que no MVP pode ter UX mais simples)
- Lista de compras inteligente completa com orçamento (RF-302, RN-301) — versão MVP pode ter lista mais simples sem otimização de orçamento
- Exportar PDF de relatório de evolução (primeira feature da lista de "Funcionalidades Futuras" a ser puxada para o roadmap formal)
- Início de preparação para multi-usuário real (se a decisão de abrir o produto para terceiros for tomada): revisão da estratégia de isolamento por usuário para cenários de convite/compartilhamento (ver [09-seguranca-riscos.md](./09-seguranca-riscos.md) §1.2), *não* implementação de comunidade/social

### 1.4 Versão 3

> **Nota de revisão:** Apple Health e Widgets exigem app nativo iOS ([06-arquitetura.md](./06-arquitetura.md) §1.4) e ficam condicionados à disponibilidade futura de Mac/Apple Developer — ver detalhamento em [09-seguranca-riscos.md](./09-seguranca-riscos.md) §4. Scanner de código de barras e modo offline robusto permanecem viáveis no cliente PWA.

- 🔒 Integração com Apple Health (sono automático, possivelmente passos/atividade) — depende de migração para app nativo
- 🔒 Widgets (resumo do Dashboard na tela inicial do iOS) — depende de migração para app nativo
- Modo offline robusto via Service Worker (hoje é "degradado", v3 visa paridade quase completa dentro dos limites de um PWA — ver [06-arquitetura.md](./06-arquitetura.md) §3.3)
- Scanner de código de barras para cadastro rápido em Minha Cozinha/refeições, via câmera do navegador

### 1.5 Versão 4

> **Nota de revisão:** Apple Watch e Live Activities seguem a mesma ressalva da v3 — dependem de app nativo.

- 🔒 Apple Watch (registro de série e RPE direto do pulso, cronômetro de descanso no pulso) — depende de migração para app nativo
- 🔒 Live Activities (cronômetro de descanso na tela de bloqueio/Dynamic Island) — depende de migração para app nativo
- Reconhecimento corporal por IA a partir das fotos de evolução (análise automática de composição visual, mencionada como visão de longo prazo desde o briefing original)
- Avaliação de evolução do banco de conhecimento da IA com dados agregados (§6.3 de 03-ia-conceito.md), **somente se houver base de usuários suficiente para isso ser estatisticamente significativo** — não faz sentido com um único usuário.

---

## 2. Backlog Priorizado (dentro do MVP)

Priorização usando critério simples e explícito: **valor para validar a tese central da IA** dividido por **custo de implementação**, com itens de fundação (sem os quais nada funciona) sempre primeiro independente do score.

| Prioridade | Item | Por quê |
|---|---|---|
| P0 — Fundação | Modelo de dados completo + projeto Spring Boot configurado (Security/JWT, JPA, Flyway) + MySQL provisionado no Railway | Nada funciona sem isso |
| P0 — Fundação | Onboarding mínimo (objetivo, experiência, frequência, equipamento, limitações) | Sem isso não há geração de plano |
| P0 — Núcleo | Geração de plano de treino inicial + explicação | É o "momento aha" do produto |
| P0 — Núcleo | Execução de sessão (registro de série, RPE, cronômetro) | Sem isso não há dado para a IA aprender |
| P0 — Núcleo | Progressão automática semana a semana | Esta é a tese central do produto sendo testada |
| P1 — Núcleo | Evolution Score (versão inicial, pesos não calibrados) | Reforça percepção de progresso, motor de retenção |
| P1 — Núcleo | Minha Cozinha + sugestão de refeição básica | Segundo pilar de dados (nutrição) que a IA precisa para decisões cruzadas (ex: deload considerando proteína) |
| P1 — Suporte | Registro de peso/medidas + gráfico com média móvel | Dado de evolução corporal, input do Evolution Score |
| P2 — Suporte | Histórico de Decisões da IA (UI de consulta) | Importante para confiança, mas não bloqueia o uso diário básico |
| P2 — Suporte | Detecção de platô e deload (RN-101, RN-102) | Depende de várias semanas de histórico acumulado — não é útil nos primeiros dias de uso, então pode ser entregue um pouco depois do P0/P1 sem prejuízo |

---

## 3. Métricas de Sucesso e KPIs

### 3.1 Métricas de Ativação

| Métrica | Definição | Por que importa |
|---|---|---|
| Tempo até primeiro treino | Minutos entre início do onboarding e finalização da primeira sessão de treino | Mede se o "momento aha" (treino + explicação) é entregue rápido o suficiente |
| Taxa de conclusão do onboarding | % de onboardings iniciados que chegam à tela de valor imediato | Mede fricção do fluxo |

### 3.2 Métricas de Engajamento e Retenção

| Métrica | Definição | Por que importa |
|---|---|---|
| Sessões de treino por semana vs. planejado | Aderência real ao plano gerado | Proxy direto de "o plano é seguível na prática" |
| Frequência de abertura do app por dia | Quantas vezes o app é aberto/dia | Valida a premissa de produto "será usado várias vezes por dia" |
| Tempo médio de registro de série | Do início do toque até confirmação | Valida RNF-01 (meta de <30s) na prática, não só em design |
| Taxa de retenção D7/D30 | % de uso ativo 7 e 30 dias após início | Métrica padrão de retenção, especialmente relevante quando o produto crescer além do uso pessoal |

### 3.3 Métricas de Confiança na IA

| Métrica | Definição | Por que importa |
|---|---|---|
| Taxa de aceitação de recomendações | % de recomendações em `recomendacoes_ia` com `status_resposta = aceita` | Indicador central de confiança — mencionado já na visão de produto ([01-product-vision.md](./01-product-vision.md) §7) |
| Taxa de rejeição com motivo | % rejeitadas e, quando capturado, padrão de motivo | Sinaliza onde a lógica de regras precisa de ajuste |
| Consultas a "Ver por quê" | Frequência com que o usuário aprofunda na explicação | Mede se a transparência está sendo genuinamente usada, não apenas exibida |

### 3.4 Métricas de Evolução Real (outcome, não apenas uso)

| Métrica | Definição | Por que importa |
|---|---|---|
| Progressão média de carga por exercício ao longo do tempo | Tendência de carga nos exercícios principais | É o resultado físico que o produto promete entregar |
| Tendência do Evolution Score ao longo de meses | Média móvel mensal do score | Indicador agregado de progresso sustentado, não só de uso do app |

---

## 4. Eventos de Analytics

Lista de eventos a instrumentar (nomenclatura sugerida em `snake_case`, sem dados sensíveis de saúde no payload do evento — apenas metadados, conforme regra de privacidade detalhada em [09-seguranca-riscos.md](./09-seguranca-riscos.md)):

| Evento | Quando disparado | Propriedades relevantes |
|---|---|---|
| `onboarding_started` | Início do onboarding | — |
| `onboarding_step_completed` | Cada etapa concluída | `step_name` |
| `onboarding_completed` | Tela de valor imediato exibida | `duration_seconds` |
| `treino_sessao_iniciada` | Início de sessão de treino | `plano_treino_id`, `origem` (plano/livre) |
| `treino_serie_registrada` | Cada série confirmada | `duration_seconds_to_confirm` (para validar RNF-01) |
| `treino_sessao_finalizada` | Fim de sessão | `duracao_total`, `numero_prs` |
| `treino_sessao_abandonada` | Sessão iniciada mas não finalizada após período de inatividade | — |
| `recomendacao_ia_exibida` | Card de recomendação aparece na UI | `tipo` |
| `recomendacao_ia_ver_por_que` | Usuário toca em "Ver por quê" | `tipo` |
| `recomendacao_ia_respondida` | Usuário aceita/ajusta/rejeita | `tipo`, `status_resposta` |
| `refeicao_sugestao_solicitada` | Usuário pede sugestão (botão ou linguagem natural) | `via_linguagem_natural` (boolean) |
| `refeicao_registrada` | Refeição confirmada/registrada | `origem` (sugestao_ia/manual) |
| `evolution_score_visualizado` | Dashboard renderizado com score visível | `score_total`, `componente_mais_fraco` |
| `minha_cozinha_item_adicionado` | Item adicionado ao estoque | — |
| `lista_compras_item_marcado_comprado` | Item marcado como comprado | — |

---

## 5. Sugestões de Melhorias (pós-MVP, candidatas a avaliação contínua)

Itens que não entraram na lista de "Funcionalidades Futuras" original mas emergem naturalmente da análise deste documento como candidatos razoáveis para avaliação após o MVP validar a tese central:

- **Calibração assistida do Evolution Score:** após algumas semanas de uso real, oferecer ao usuário a opção de revisar/ajustar manualmente os pesos dos componentes do score, tornando o índice mais pessoal (com cuidado para não comprometer a comparabilidade ao longo do tempo se os pesos mudarem).
- **Resumo semanal automático (texto gerado pela IA):** uma "carta" semanal resumindo o que mudou, o que funcionou e o que será ajustado — reaproveitando a mesma camada de explicação textual já construída para recomendações individuais, mas em formato agregado.
- **Modo "diário de bordo" para observações livres:** campo de texto livre vinculado à sessão de treino ou ao dia, que a IA pode usar como sinal qualitativo adicional (ex: "treinei mal, dormi pouco ontem") — complementar aos dados estruturados de sono/RPE, capturando contexto que os campos estruturados não cobrem.
