# 02 — PRD (Product Requirements Document)

---

## 1. Escopo deste documento

Este PRD cobre **todos os módulos funcionais** do Hypertrophy OS. Cada módulo tem: requisitos funcionais (RF), regras de negócio (RN), pelo menos uma user story representativa com critérios de aceitação, e referência aos requisitos não-funcionais (RNF) que se aplicam globalmente.

Numeração: RF-XXX e RN-XXX são globais e sequenciais por módulo (ex: RF-101 a RF-1xx são do módulo Treino). Isso facilita rastreabilidade sem precisar renumerar tudo a cada alteração.

---

## 2. Requisitos Não Funcionais (RNF) — aplicam-se a todo o produto

| ID | Requisito | Justificativa |
|---|---|---|
| RNF-01 | Qualquer fluxo de registro (série, peso, refeição rápida) deve ser completável em até 30 segundos, do toque inicial até a confirmação. | Fricção é a principal causa de abandono em apps de tracking diário. |
| RNF-02 | O app deve ser 100% operável com uma única mão em telas de iPhone padrão (a partir do iPhone SE até o Pro Max). | Usuário frequentemente segura halteres/barra com a outra mão. |
| RNF-03 | Tempo de carregamento do Dashboard (cold start) deve ser inferior a 2 segundos em conexão 4G. | Uso multidiário exige resposta quase instantânea. |
| RNF-04 | O app deve funcionar em modo degradado (leitura de dados já sincronizados, registro local) quando sem conexão, sincronizando ao reconectar. | Academias frequentemente têm sinal de internet ruim. |
| RNF-05 | Toda recomendação gerada pela IA deve ser persistida com o conjunto de dados que a originou (data lineage), para auditabilidade e explicabilidade. | Requisito central da filosofia de transparência do produto. |
| RNF-06 | O sistema deve suportar, sem mudança de arquitetura, o crescimento de 1 usuário (uso pessoal) para milhares de usuários concorrentes. | Requisito explícito de escalabilidade do projeto. |
| RNF-07 | Dados de saúde do usuário (peso, medidas, fotos corporais) devem ser armazenados com criptografia em repouso e em trânsito. | Dados sensíveis de saúde, aplicável mesmo a um único usuário. |
| RNF-08 | O app deve aderir à LGPD desde o design (privacy by design), mesmo operando inicialmente com um único usuário. | Mais barato implementar correto desde o início do que retrofitar. |
| RNF-09 | A interface deve suportar modo claro e modo escuro com paridade total de funcionalidade. | Uso em ambientes variados (academia, casa, manhã, noite). |
| RNF-10 | Toda tela deve ter estado de carregamento, vazio e erro explicitamente desenhado — nenhuma tela "branca". | Padrão de qualidade de produtos Apple/Notion/Linear. |
| RNF-11 | O app deve ser acessível (VoiceOver, Dynamic Type, contraste mínimo AA). | Padrão de qualidade esperado em produtos iOS de alto nível. |

---

## 3. Módulo: Onboarding e Perfil

### 3.1 Requisitos Funcionais

- **RF-001**: O sistema deve coletar, no onboarding, dados mínimos necessários para a primeira recomendação de treino: objetivo, experiência, frequência semanal disponível, equipamentos/academia, limitações físicas/lesões.
- **RF-002**: O onboarding deve ser interrompível e retomável — o usuário pode pular etapas não-críticas e completá-las depois pelo perfil.
- **RF-003**: O sistema deve permitir múltiplos perfis de "contexto" de treino (ex: "academia completa" vs. "treino em casa com halteres") associados ao mesmo usuário, selecionáveis por sessão.
- **RF-004**: O sistema deve registrar peso, altura e (opcionalmente) percentual de gordura inicial durante o onboarding para servir de baseline.

### 3.2 Regras de Negócio

- **RN-001**: Nenhuma recomendação de treino pode ser gerada sem que os campos mínimos obrigatórios (objetivo, frequência, equipamento) estejam preenchidos. Se ausentes, o sistema bloqueia a geração e direciona ao preenchimento.
- **RN-002**: Limitações físicas/lesões declaradas excluem automaticamente exercícios de contraindicação conhecida do banco de exercícios (ex: dor lombar declarada → levantamento terra convencional fica com flag de risco e é substituído por variante seguro, com explicação).

### 3.3 User Story representativa

> **US-001**: Como usuário novo, quero completar o onboarding em poucos minutos e já receber meu primeiro treino sugerido, para que eu sinta valor imediato do app sem precisar configurar tudo manualmente.

**Critérios de aceitação:**
- [ ] Onboarding completo em no máximo 6 etapas (telas).
- [ ] Ao final do onboarding, o sistema apresenta automaticamente o primeiro treino sugerido pela IA, com explicação textual do porquê daquele treino.
- [ ] Usuário pode editar qualquer resposta do onboarding depois, em Perfil, sem precisar refazer o fluxo completo.

---

## 4. Módulo: Treino

### 4.1 Requisitos Funcionais

- **RF-101**: O sistema deve gerar planos de treino personalizados considerando: objetivo, experiência, frequência semanal, tempo disponível por sessão, equipamento, limitações, músculos prioritários/atrasados, volume semanal já realizado, fadiga acumulada, RPE histórico e aderência.
- **RF-102**: O sistema deve permitir iniciar uma sessão de treino a partir do plano sugerido ou um treino livre (sem plano prévio).
- **RF-103**: Durante a sessão, o sistema deve permitir, por série: editar carga, editar repetições, registrar RPE (1–10), adicionar observação textual.
- **RF-104**: O sistema deve oferecer cronômetro de descanso configurável por exercício, com notificação sonora/háptica ao fim do tempo.
- **RF-105**: Ao finalizar a sessão, o sistema deve apresentar resumo: volume total, novos recordes pessoais (PRs), carga total levantada, duração, grupos musculares trabalhados.
- **RF-106**: O sistema deve ajustar automaticamente o plano nas semanas seguintes com base em: progressão de carga sugerida, ajuste de volume, troca de exercícios (ex: platô), indicação de deload, mudança de frequência, e — quando apropriado ao nível do usuário — sugestão de técnicas avançadas (ex: drop-set, rest-pause).
- **RF-107**: Toda alteração automática do plano (RF-106) deve vir acompanhada de explicação textual gerada pela IA, referenciando os dados que motivaram a mudança.
- **RF-108**: O sistema deve permitir ao usuário aceitar, ajustar ou rejeitar uma sugestão de mudança de plano, registrando o feedback para aprendizado futuro.

### 4.2 Regras de Negócio

- **RN-101**: Progressão de carga só é sugerida quando RPE médio das últimas sessões daquele exercício estiver abaixo do limiar configurado (ver lógica detalhada em [03-ia-conceito.md](./03-ia-conceito.md) §Treino) **e** todas as séries planejadas tiverem sido completadas.
- **RN-102**: Deload é sugerido automaticamente quando há sinais combinados de fadiga acumulada (queda de performance em múltiplos exercícios + RPE subindo para mesma carga + autorrelato de sono/recuperação ruim por período sustentado). Um único sinal isolado não é suficiente para disparar a sugestão.
- **RN-103**: Um exercício só pode ser sugerido se compatível com o equipamento declarado no contexto de treino ativo (RF-003).
- **RN-104**: O sistema nunca apaga histórico de séries já registradas — edições geram novo registro versionado, preservando o dado original para fins de auditoria e aprendizado da IA.

### 4.3 Casos de Uso

**UC-101: Executar treino do dia**
1. Usuário abre o app → Dashboard mostra "Treino de Hoje".
2. Usuário toca em "Iniciar Treino".
3. Sistema exibe primeira série do primeiro exercício, com carga/repetições sugeridas (baseadas na última execução + progressão).
4. Usuário executa, registra carga real, repetições reais e RPE.
5. Sistema inicia cronômetro de descanso automaticamente.
6. Repete para todas as séries/exercícios.
7. Usuário finaliza treino → sistema exibe resumo com PRs e volume.
8. (Fluxo alternativo) Se usuário abandona o treino pela metade, sistema salva progresso parcial e permite retomar ou descartar na próxima abertura.

### 4.4 User Story representativa

> **US-101**: Como praticante intermediário, quero que o app ajuste automaticamente minha carga com base no meu desempenho anterior, para que eu não precise decidir manualmente quanto progredir a cada treino.

**Critérios de aceitação:**
- [ ] Ao iniciar um exercício já executado anteriormente, o sistema pré-popula carga/repetições sugeridas.
- [ ] A sugestão é acompanhada de uma frase explicativa (ex: "Sugerimos +2,5kg porque seu RPE médio nas últimas 2 sessões foi 7/10, indicando margem de progressão").
- [ ] Usuário pode sobrescrever a sugestão livremente sem fricção adicional.

---

## 5. Módulo: Nutrição

### 5.1 Requisitos Funcionais

- **RF-201**: O sistema deve calcular necessidades calóricas e de macronutrientes (proteína, carboidrato, gordura, fibra) com base em objetivo, peso, atividade e preferências.
- **RF-202**: O sistema deve gerar sugestões de refeições realistas, considerando rotina, horários disponíveis, preferências alimentares, orçamento declarado e — quando aplicável — os alimentos disponíveis em "Minha Cozinha" (RF-301).
- **RF-203**: O sistema deve responder a perguntas em linguagem natural sobre o que comer dado um contexto específico (ex: "tenho só ovos e arroz", "estou sem tempo", "quero economizar essa semana") — ver detalhamento em [03-ia-conceito.md](./03-ia-conceito.md) §Nutrição.
- **RF-204**: O sistema deve permitir registro rápido de refeições (busca de alimento + quantidade, ou seleção de refeição pré-sugerida com um toque).

### 5.2 Regras de Negócio

- **RN-201**: Sugestões de refeição priorizam, em ordem: (1) alimentos disponíveis em "Minha Cozinha", (2) preferências/restrições alimentares declaradas, (3) ajuste fino de macro para a meta diária.
- **RN-202**: O sistema nunca sugere uma refeição com ingrediente não disponível sem sinalizar explicitamente "você precisará comprar: [item]" — nunca assume disponibilidade silenciosamente.
- **RN-203**: Cálculo de macro-alvo é recalculado automaticamente a cada atualização relevante de peso corporal (ver cadência em [03-ia-conceito.md](./03-ia-conceito.md)), não apenas manualmente.

### 5.3 User Story representativa

> **US-201**: Como usuário sem tempo para planejar, quero perguntar "o que eu posso comer agora com o que eu tenho" e receber uma sugestão prática, para não recorrer a opções não saudáveis por falta de planejamento.

**Critérios de aceitação:**
- [ ] Resposta gerada em até poucos segundos, usando exclusivamente os itens marcados como disponíveis em "Minha Cozinha".
- [ ] Resposta inclui estimativa de macro da refeição sugerida.
- [ ] Se nenhuma combinação satisfatória existir com o estoque atual, o sistema admite a limitação e sugere o item mínimo a comprar.

---

## 6. Módulo: Minha Cozinha e Lista de Compras

### 6.1 Requisitos Funcionais

- **RF-301**: O sistema deve permitir ao usuário registrar e manter atualizado um inventário de alimentos disponíveis em casa ("Minha Cozinha"), com adição/remoção rápida.
- **RF-302**: O sistema deve gerar automaticamente uma lista de compras sugerida com base em: consumo médio histórico, dieta/meta atual, itens com estoque baixo ou esgotado, e orçamento declarado.
- **RF-303**: O usuário deve poder marcar itens da lista de compras como comprados, atualizando automaticamente o estoque de "Minha Cozinha".

### 6.2 Regras de Negócio

- **RN-301**: Um item é considerado "acabando" quando o consumo médio projetado indica esgotamento dentro de poucos dias (parâmetro configurável, ver §Banco de Conhecimento em 03-ia-conceito.md). Itens "acabando" entram automaticamente na lista de compras sugerida.

---

## 7. Módulo: Acompanhamento Corporal (Peso, Medidas, Fotos)

### 7.1 Requisitos Funcionais

- **RF-401**: O sistema deve permitir registro de peso, percentual de gordura e medidas corporais (com horário do registro).
- **RF-402**: O sistema deve gerar gráficos de evolução semanal, mensal e anual para peso e medidas.
- **RF-403**: O sistema deve calcular e exibir tendências via média móvel, evitando que o usuário interprete uma única flutuação diária como sinal de progresso ou retrocesso.
- **RF-404**: O sistema deve permitir registro de fotos de evolução, com comparação lado a lado entre duas datas e visualização em linha do tempo.

### 7.2 Regras de Negócio

- **RN-401**: Gráficos de peso devem exibir, por padrão, a média móvel (ex: 7 dias) como linha principal, com os pontos diários exibidos de forma visualmente secundária — nunca o inverso.
- **RN-402**: Fotos de evolução são armazenadas com metadado de data e, opcionalmente, peso/medida do dia, mas nunca expostas fora do dispositivo/conta do próprio usuário sem ação explícita de exportação (ver [09-seguranca-riscos.md](./09-seguranca-riscos.md)).

---

## 8. Módulo: Sessão de Treino (UI/Runtime)

Já coberto majoritariamente em §4. Requisitos adicionais de runtime:

- **RF-501**: O sistema deve persistir o estado da sessão de treino em curso localmente, sobrevivendo a fechamento acidental do app.
- **RF-502**: O cronômetro de descanso deve continuar contando em background (notificação local) mesmo se o usuário sair do app momentaneamente.

---

## 9. Módulo: Hábitos

### 9.1 Requisitos Funcionais

- **RF-601**: O sistema deve permitir acompanhar hábitos configuráveis (creatina, vitaminas, cardio, alongamento, mobilidade, hidratação, sono) com registro de streak (sequência de dias consecutivos).
- **RF-602**: O sistema deve permitir ao usuário criar hábitos customizados além dos pré-definidos.

### 9.2 Regras de Negócio

- **RN-601**: Quebra de streak é registrada mas não apagada do histórico — o sistema mostra histórico completo de streaks (atual e melhores anteriores), nunca apenas o streak corrente, para evitar que uma quebra pontual pareça "perda total de progresso".

---

## 10. Módulo: Sono

### 10.1 Requisitos Funcionais

- **RF-701**: O sistema deve permitir registro manual de horário de dormir, horário de acordar, qualidade percebida (escala) e duração calculada automaticamente.
- **RF-702**: (Futuro, v2+) O sistema deve permitir importação automática de dados de sono via Apple Health, quando disponível, mantendo a opção de registro manual como fallback.

---

## 11. Módulo: Dashboard

### 11.1 Requisitos Funcionais

- **RF-801**: O Dashboard deve apresentar, de forma escaneável em menos de 5 segundos: status do treino de hoje, alimentação do dia, hidratação, sono recente, progresso em relação a metas, e recomendações ativas da IA.
- **RF-802**: O Dashboard deve exibir o Evolution Score (ver §12) com indicação visual de tendência (subindo/estável/caindo).
- **RF-803**: O Dashboard deve ser o ponto de entrada padrão do app ao abrir (tela inicial pós-login/onboarding).

---

## 12. Módulo: Evolution Score

### 12.1 Requisitos Funcionais

- **RF-901**: O sistema deve calcular um índice composto (Evolution Score, 0–100) a partir de: aderência ao treino, recuperação percebida, ingestão de proteína, hidratação, sono, consistência geral e evolução corporal mensurável.
- **RF-902**: O sistema deve explicar, a qualquer momento, quais componentes estão "puxando o score para baixo" e o que fazer para melhorá-los — nunca apresentar apenas o número isolado.

(Fórmula, pesos e lógica de cálculo detalhados em [03-ia-conceito.md](./03-ia-conceito.md) §Evolution Score — este PRD define o requisito funcional, não a fórmula.)

### 12.2 Regras de Negócio

- **RN-901**: O Evolution Score nunca é exibido sem ao menos uma recomendação acionável associada a seu componente mais baixo.

---

## 13. Módulo: IA e Recomendações (requisitos transversais)

Esta seção define requisitos que se aplicam à IA como sistema, independente do domínio (treino/nutrição/etc). A lógica de conteúdo da IA está em [03-ia-conceito.md](./03-ia-conceito.md); aqui ficam os requisitos de **produto e auditabilidade**.

- **RF-1001**: Toda recomendação gerada pela IA deve ser persistida no "Histórico de Decisões da IA" (entidade dedicada, ver [07-modelagem-dados.md](./07-modelagem-dados.md)), incluindo: dado(s) de entrada considerados, recomendação gerada, explicação textual, timestamp, e (quando aplicável) resposta do usuário (aceitou/ajustou/rejeitou).
- **RF-1002**: Nenhuma recomendação pode ser apresentada ao usuário sem explicação textual associada — este é um requisito de bloqueio de release, não uma melhoria desejável.
- **RF-1003**: O usuário deve poder consultar o histórico de recomendações passadas e seus desfechos (útil tanto para confiança no sistema quanto para auditoria pessoal).

### 13.1 Regras de Negócio

- **RN-1001**: Recomendações conflitantes (ex: IA sugere aumentar volume de treino na mesma semana em que detecta sinais de fadiga alta) devem ser resolvidas com prioridade para recuperação sobre performance — a regra de negócio padrão do produto é **conservadora em relação a lesão/overtraining**, mesmo que isso signifique progressão mais lenta.

---

## 14. Rastreabilidade

Toda nova funcionalidade proposta após a v1 deve, antes de ser aceita no backlog (ver [08-roadmap-backlog.md](./08-roadmap-backlog.md)), receber: um ID de RF, ao menos uma RN se aplicável, e passar pelo teste de filosofia de produto descrito em [01-product-vision.md](./01-product-vision.md) §5.
