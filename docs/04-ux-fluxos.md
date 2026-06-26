# 04 — UX: Sitemap, Fluxos e Wireframes em Texto

---

## 1. Sitemap

```
Hypertrophy OS
│
├── Onboarding (pré-login funcional, fluxo único)
│   ├── Boas-vindas
│   ├── Objetivo e experiência
│   ├── Frequência e disponibilidade
│   ├── Equipamento/contexto de treino
│   ├── Limitações físicas
│   ├── Dados corporais iniciais (peso, altura, % gordura opcional)
│   └── Primeiro treino sugerido (tela de "valor imediato")
│
├── Tab Bar Principal (pós-onboarding)
│   │
│   ├── [1] Dashboard (Home)
│   │   ├── Evolution Score (card de destaque)
│   │   ├── Treino de Hoje (card)
│   │   ├── Nutrição do Dia (card)
│   │   ├── Hidratação (card)
│   │   ├── Sono recente (card)
│   │   └── Recomendações ativas da IA (lista)
│   │
│   ├── [2] Treino
│   │   ├── Plano da Semana
│   │   ├── Sessão Ativa (quando em treino)
│   │   ├── Histórico de Treinos
│   │   ├── Recordes Pessoais (PRs)
│   │   └── Detalhe de Exercício (histórico individual, gráfico de carga)
│   │
│   ├── [3] Nutrição
│   │   ├── Resumo do Dia (macros)
│   │   ├── Minha Cozinha (inventário)
│   │   ├── Lista de Compras
│   │   ├── Perguntar à IA ("o que eu como?")
│   │   └── Histórico de Refeições
│   │
│   ├── [4] Evolução
│   │   ├── Peso e Medidas (gráficos)
│   │   ├── Fotos (linha do tempo / comparação)
│   │   ├── Hábitos (streaks)
│   │   └── Sono (histórico)
│   │
│   └── [5] Perfil
│       ├── Dados Pessoais e Objetivo
│       ├── Contextos de Treino (academia/casa)
│       ├── Histórico de Decisões da IA
│       ├── Configurações (notificações, unidades, tema)
│       └── Privacidade e Dados (LGPD)
│
└── Telas Modais/Transversais (acessíveis de múltiplos pontos)
    ├── Registro Rápido de Peso
    ├── Registro Rápido de Refeição
    ├── Chat com a IA (overlay, acessível de qualquer tab)
    └── Detalhe de Recomendação (explicação completa de uma sugestão)
```

### 1.1 Justificativa da estrutura de 5 tabs

**Alternativa considerada:** estrutura de 4 tabs, absorvendo "Evolução" dentro de "Perfil".

**Por que foi descartada:** Evolução corporal (peso, fotos, hábitos, sono) é um dos três pilares de dados que a IA usa (junto com Treino e Nutrição) e o usuário precisa revisitá-lo com frequência suficiente para justificar um tab próprio, não um submenu de Perfil — submenu tende a ser visitado raramente, e o produto depende de revisitação frequente desses dados para reforçar a percepção de progresso (parte central da retenção). Cinco tabs ainda está dentro do limite recomendado de usabilidade para tab bars em iOS (Apple recomenda até 5 sem usar "Mais").

---

## 2. Fluxo de Navegação Principal

```
[Abrir App]
    │
    ├── Primeira vez? ──Sim──> [Onboarding] ──> [Dashboard]
    │                  └─Não──────────────────> [Dashboard]
    │
[Dashboard] ──toque em "Treino de Hoje"──> [Plano da Semana] ──"Iniciar"──> [Sessão Ativa]
    │                                                                            │
    │                                                                  [Resumo Pós-Treino]
    │                                                                            │
    │                                                                  [Dashboard] (atualizado)
    │
[Dashboard] ──toque em card de Nutrição──> [Resumo do Dia] ──> [Registro Rápido de Refeição]
    │
[Dashboard] ──toque em recomendação da IA──> [Detalhe de Recomendação] ──aceitar/ajustar/rejeitar──> [volta ao contexto de origem]
```

**Princípio de navegação:** o Dashboard é sempre o "hub" — qualquer fluxo profundo (sessão de treino, registro de refeição, detalhe de recomendação) tem caminho de volta direto ao Dashboard em no máximo 1 toque (botão "Concluir"/"Fechar"), nunca exigindo navegação reversa por múltiplas telas.

---

## 3. Wireframes em Texto

> Convenção: `[ ]` representa um elemento tocável (botão/card). `─` representa divisores visuais. Texto entre aspas é copy ilustrativo, não final.

### 3.1 Dashboard

```
┌─────────────────────────────────┐
│  Olá, [Nome]            [⚙]     │  <- header minimalista, ícone de config à direita
│                                  │
│  ┌───────────────────────────┐  │
│  │   EVOLUTION SCORE          │  │
│  │        87  ↗               │  │  <- número grande, seta de tendência
│  │   "Hidratação está         │  │
│  │    baixando seu score"     │  │  <- componente mais fraco + dica
│  │   [Ver detalhes]            │  │
│  └───────────────────────────┘  │
│                                  │
│  [ TREINO DE HOJE            ]  │
│  [ Peito e Tríceps · 45min   ]  │
│  [        Iniciar Treino →   ]  │
│                                  │
│  ┌─────────────┐ ┌────────────┐ │
│  │ Nutrição     │ │ Hidratação │ │
│  │ 1840/2400kcal│ │ 1.2/3.0L   │ │
│  │ Proteína 70% │ │ [+250ml]   │ │
│  └─────────────┘ └────────────┘ │
│                                  │
│  RECOMENDAÇÕES DA IA             │
│  ┌───────────────────────────┐  │
│  │ "Detectamos fadiga          │  │
│  │  acumulada. Sugerimos       │  │
│  │  deload nesta semana."      │  │
│  │  [Ver por quê] [Aplicar]    │  │
│  └───────────────────────────┘  │
└─────────────────────────────────┘
```

### 3.2 Sessão Ativa (Treino)

```
┌─────────────────────────────────┐
│  ← Supino Reto       Série 2/4  │
│                                  │
│         60 kg   x   10 reps      │  <- valores grandes, editáveis com toque
│      [ - ]              [ + ]    │  <- steppers rápidos
│                                  │
│  Sugestão: 60kg (RPE médio       │
│  últimas 2 sessões: 7/10)        │  <- explicação inline, discreta
│                                  │
│  Como foi o esforço? (RPE)       │
│  [1] [2] ... [7●] ... [10]       │  <- seleção rápida
│                                  │
│       [ Concluir Série ]         │
│                                  │
│  ──────── Descanso: 90s ──────── │  <- aparece após concluir série
│            01:24                 │
│       [Pular] [+30s]             │
└─────────────────────────────────┘
```

### 3.3 Minha Cozinha

```
┌─────────────────────────────────┐
│  Minha Cozinha          [+ Add] │
│                                  │
│  🔍 Buscar alimento...           │
│                                  │
│  PROTEÍNAS                      │
│  [ Frango       ] [ Ovos      ] │
│  [ Patinho      ]               │
│                                  │
│  CARBOIDRATOS                   │
│  [ Arroz        ] [ Aveia     ] │
│  [ Pão          ]               │
│                                  │
│  ⚠ ACABANDO                      │
│  [ Banana — 1 unidade ]          │
│  [ Adicionar à lista de compras]│
└─────────────────────────────────┘
```

### 3.4 Chat com a IA (overlay, perguntas contextuais)

```
┌─────────────────────────────────┐
│  ✕                Assistente IA │
│                                  │
│  Você: "tenho só ovos e arroz"  │
│                                  │
│  IA: "Com ovos e arroz você     │
│  pode fazer um arroz com ovos   │
│  mexidos — cobre ~35g de        │
│  proteína. Falta um pouco para  │
│  sua meta do almoço (55g).      │
│  Quer que eu sugira o que       │
│  comprar para complementar?"     │
│                                  │
│  [Sim, sugerir] [Está bom assim]│
│                                  │
│  ┌─────────────────────────┐    │
│  │ Digite sua pergunta...   │    │
│  └─────────────────────────┘    │
└─────────────────────────────────┘
```

---

## 4. Jornadas do Usuário

### 4.1 Jornada: Primeira semana de uso (ativação)

| Etapa | Ação do usuário | Resposta do sistema | Risco de abandono |
|---|---|---|---|
| Dia 0 | Completa onboarding | Recebe primeiro treino + explicação | Onboarding longo demais |
| Dia 0 | Executa primeiro treino | Resumo pós-treino com PR (mesmo que "PR" por ser o primeiro registro) | Fluxo de registro de série confuso |
| Dia 1–2 | Registra refeições, configura Minha Cozinha | Recebe primeira sugestão de refeição personalizada | Cadastro de Minha Cozinha sentido como trabalho manual excessivo |
| Dia 3–4 | Segundo treino da semana | Vê progressão sugerida (ainda mínima, dado histórico curto) | Sugestão genérica demais por falta de dados ainda |
| Dia 7 | Primeira visualização do Evolution Score "maduro" | Score com pelo menos uma semana de dados reais, com explicação de componente fraco | Score sentido como arbitrário sem explicação clara |

**Implicação de design:** nos primeiros dias, com dados históricos limitados, a IA deve **declarar explicitamente que está calibrando** (ex: "Ainda estou aprendendo seu padrão — esta sugestão é um ponto de partida") em vez de apresentar recomendações com a mesma confiança de um usuário com 6 meses de histórico. Isso evita a percepção de que o sistema é "burro" quando na realidade está corretamente subdimensionado para os dados disponíveis.

### 4.2 Jornada: Detecção e resposta a um platô

1. Usuário treina supino reto por 4 sessões sem progressão de carga ou repetição.
2. Sistema detecta o padrão (RN da seção de Treino) na 4ª sessão.
3. Dashboard mostra recomendação: "Notamos que seu supino reto está estável há 4 sessões. Que tal ajustar o tempo de descanso antes de mudar o exercício?"
4. Usuário toca "Ver por quê" → tela de Detalhe de Recomendação mostra o histórico de carga das 4 sessões em gráfico simples, com a explicação completa.
5. Usuário aceita → próxima sessão já vem com o ajuste aplicado automaticamente.

---

## 5. Estados Vazios

| Tela | Estado vazio | Copy/ação sugerida |
|---|---|---|
| Treino → Histórico | Nenhum treino registrado ainda | "Seu histórico aparece aqui depois do primeiro treino. [Iniciar Treino]" |
| Nutrição → Minha Cozinha | Nenhum alimento cadastrado | "Adicione o que você tem em casa para receber sugestões de refeição personalizadas. [+ Adicionar alimento]" |
| Evolução → Fotos | Nenhuma foto enviada | "Fotos de evolução ajudam a ver progresso que a balança não mostra. [Adicionar primeira foto]" |
| Dashboard → Recomendações da IA | Sem recomendações ativas (early days) | "Continue registrando — em breve a IA terá dados suficientes para recomendações personalizadas." |

---

## 6. Estados de Erro

| Cenário | Tratamento |
|---|---|
| Falha de sincronização (sem internet) | Banner discreto não-bloqueante: "Sem conexão — seus dados serão sincronizados automaticamente." Dados continuam editáveis localmente. |
| Falha ao gerar recomendação da IA (erro de backend/timeout) | Card de recomendação substituído por estado de erro com retry manual: "Não conseguimos gerar uma recomendação agora. [Tentar novamente]" — nunca falha silenciosa que implique ausência de recomendação como se fosse intencional. |
| Conflito de edição (mesma série editada em dois dispositivos, pós-multiusuário) | Fora de escopo do MVP (uso single-device); ver [09-seguranca-riscos.md](./09-seguranca-riscos.md) para nota de risco futuro. |

---

## 7. Estados de Loading

- **Cold start do Dashboard:** skeleton screens (placeholders de card cinza com shimmer) em vez de spinner central — alinhado a padrões modernos (Linear, Notion) e percebido como mais rápido pelo usuário mesmo com tempo real idêntico.
- **Geração de recomendação da IA (quando síncrona, ex: resposta de chat):** indicador de "digitando..." com os primeiros tokens da resposta sendo exibidos progressivamente (streaming), nunca um spinner bloqueante de tela inteira — reforça a sensação de "conversa com um assistente", não "carregando um relatório".

---

## 8. Onboarding — fluxo detalhado

1. **Boas-vindas:** breve, 1 tela, sem formulário. Define expectativa: "Vamos te conhecer para montar seu primeiro treino personalizado. Leva poucos minutos."
2. **Objetivo e experiência:** seleção única (hipertrofia é o padrão/único objetivo no MVP — mas a pergunta de experiência, sim, é central: iniciante/intermediário/avançado).
3. **Frequência e disponibilidade:** quantos dias por semana, quanto tempo por sessão.
4. **Equipamento/contexto:** academia completa / casa com equipamento limitado / apenas peso corporal — gera o primeiro "Contexto de Treino" (RF-003).
5. **Limitações físicas:** lista de checkboxes comuns (lombar, joelho, ombro, etc.) + campo livre opcional.
6. **Dados corporais iniciais:** peso, altura, % gordura (opcional, com explicação de por que é opcional e por que ajuda se informado).
7. **Tela de valor imediato:** apresenta o primeiro treino já gerado, com explicação — este é o "momento aha" do produto e não deve ser pulado nem enfraquecido.

**Princípio:** cada etapa deve ser pulável exceto a etapa 7 (que não é um formulário, é a entrega de valor). Etapas puladas ficam como pendência visível e gentil no Perfil, não como bloqueio.
