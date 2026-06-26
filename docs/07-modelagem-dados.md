# 07 — Modelagem de Dados e APIs

---

## 1. Visão Geral do Modelo

O modelo é relacional (MySQL via Spring Data JPA, conforme decisão revisada em [06-arquitetura.md](./06-arquitetura.md) §2). Todas as tabelas incluem, por convenção (omitido nas listagens abaixo para legibilidade): `id` (BIGINT auto-increment, PK — padrão JPA/Hibernate, mesmo usado no Finwise), `created_at`, `updated_at`. Tabelas com dados de usuário incluem `user_id` (FK) e o isolamento de acesso é garantido na camada de aplicação (Service/Repository do Spring Boot filtrando por `user_id` do usuário autenticado via JWT) — não há RLS nativa de banco neste contexto (MySQL não oferece RLS no mesmo nível que Postgres); ver nota de risco e mitigação em [06-arquitetura.md](./06-arquitetura.md) §2.5 e [09-seguranca-riscos.md](./09-seguranca-riscos.md) §1.2.

---

## 2. Entidades e Atributos

### 2.1 `users`
Gerenciada pelo Spring Security (autenticação via JWT, mesmo padrão do Finwise); tabela de perfil estendido (`profiles`) referencia o `id` do usuário autenticado.

### 2.2 `profiles`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK → auth.users) | PK |
| nome | text | |
| data_nascimento | date | nullable |
| altura_cm | numeric | |
| objetivo | enum(`hipertrofia`) | MVP suporta apenas hipertrofia; campo já é enum extensível |
| nivel_experiencia | enum(`iniciante`,`intermediario`,`avancado`) | |
| onboarding_completo | boolean | |

### 2.3 `contextos_treino`
Representa RF-003 (múltiplos contextos: academia/casa).
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| nome | text | ex: "Academia", "Casa" |
| equipamento_disponivel | jsonb | lista estruturada de equipamentos |
| ativo | boolean | contexto atualmente selecionado |

### 2.4 `limitacoes_fisicas`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| regiao | enum(`lombar`,`joelho`,`ombro`,`cotovelo`,`punho`,`outro`) | |
| descricao | text | nullable, campo livre |
| ativo | boolean | permite "desativar" sem perder histórico |

### 2.5 `exercicios` (catálogo, não específico de usuário)
| Campo | Tipo | Notas |
|---|---|---|
| nome | text | |
| grupo_muscular_primario | enum | peito, dorsal, ombro, biceps, triceps, quadriceps, posterior_coxa, gluteo, panturrilha, abdomen, etc. |
| grupos_musculares_secundarios | jsonb | array de enums |
| equipamento_necessario | enum | barra, halteres, maquina, peso_corporal, cabo, etc. |
| padrao_movimento | enum | empurrar_horizontal, empurrar_vertical, puxar_horizontal, puxar_vertical, dominante_quadril, dominante_joelho, isolamento, etc. |
| contraindicacoes | jsonb | array de `regiao` (referencia mesmo enum de limitacoes_fisicas) — usado por RN-002 |

### 2.6 `planos_treino`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| contexto_treino_id | UUID (FK) | |
| nome | text | ex: "Upper/Lower — Semana 3" |
| split_tipo | enum | full_body, upper_lower, push_pull_legs, etc. |
| ativo | boolean | |
| gerado_por_ia | boolean | distingue plano gerado automaticamente de criado/editado manualmente |
| versao | integer | incrementado a cada revisão automática do plano (RF-106) |

### 2.7 `plano_treino_exercicios`
Liga um plano a exercícios com prescrição-alvo (não execução real).
| Campo | Tipo | Notas |
|---|---|---|
| plano_treino_id | UUID (FK) | |
| exercicio_id | UUID (FK) | |
| dia_da_semana | integer | 1–7, ou referência a um "dia de treino" abstrato (A/B/C) |
| series_alvo | integer | |
| repeticoes_alvo_min | integer | |
| repeticoes_alvo_max | integer | |
| ordem | integer | ordem de execução dentro do dia |

### 2.8 `sessoes_treino`
Execução real de um treino (instância).
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| plano_treino_id | UUID (FK) | nullable — permite treino livre sem plano |
| data_inicio | timestamp | |
| data_fim | timestamp | nullable até finalizar |
| status | enum(`em_andamento`,`finalizada`,`abandonada`) | |
| duracao_segundos | integer | calculado ao finalizar |

### 2.9 `series_executadas`
| Campo | Tipo | Notas |
|---|---|---|
| sessao_treino_id | UUID (FK) | |
| exercicio_id | UUID (FK) | |
| numero_serie | integer | |
| carga_kg | numeric | |
| repeticoes_realizadas | integer | |
| rpe | numeric(2,1) | escala 1–10, permite décimos (ex: 7.5) |
| observacao | text | nullable |
| versao_anterior_id | UUID (FK, self) | nullable — preserva histórico em caso de edição (RN-104) |

### 2.10 `registros_peso_medidas`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| data_hora | timestamp | |
| peso_kg | numeric | nullable |
| percentual_gordura | numeric | nullable |
| medidas | jsonb | objeto flexível (braço, cintura, coxa, etc.) — flexibilidade deliberada para adicionar medidas sem migration |

### 2.11 `fotos_evolucao`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| storage_path | text | referência ao local de armazenamento do arquivo (disco do servidor com backup ou bucket S3-compatible — a definir ao implementar, ver [06-arquitetura.md](./06-arquitetura.md) §4; feature não incluída no MVP) |
| data_hora | timestamp | |
| peso_kg_no_momento | numeric | nullable, snapshot |

### 2.12 `alimentos` (catálogo, não específico de usuário)
| Campo | Tipo | Notas |
|---|---|---|
| nome | text | |
| categoria | enum | proteina, carboidrato, gordura, vegetal, fruta, outro |
| calorias_por_100g | numeric | |
| proteina_por_100g | numeric | |
| carboidrato_por_100g | numeric | |
| gordura_por_100g | numeric | |
| fibra_por_100g | numeric | nullable |

### 2.13 `estoque_cozinha`
Implementa "Minha Cozinha" (RF-301).
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| alimento_id | UUID (FK) | |
| quantidade_disponivel | numeric | nullable — pode ser apenas booleano de presença se usuário não quiser detalhar quantidade |
| unidade | enum | g, ml, unidade |
| consumo_medio_diario | numeric | calculado, usado para detectar "acabando" (RN-301) |

### 2.14 `lista_compras`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| alimento_id | UUID (FK) | |
| quantidade_sugerida | numeric | nullable |
| origem | enum(`acabando`,`receita_sugerida`,`manual`) | |
| comprado | boolean | |

### 2.15 `refeicoes_registradas`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| data_hora | timestamp | |
| tipo | enum(`cafe_da_manha`,`almoco`,`jantar`,`lanche`,`pre_treino`,`pos_treino`) | |
| origem | enum(`sugestao_ia`,`manual`) | |
| total_calorias | numeric | calculado |
| total_proteina | numeric | calculado |
| total_carboidrato | numeric | calculado |
| total_gordura | numeric | calculado |

### 2.16 `refeicao_itens`
| Campo | Tipo | Notas |
|---|---|---|
| refeicao_id | UUID (FK) | |
| alimento_id | UUID (FK) | |
| quantidade_g | numeric | |

### 2.17 `metas_nutricionais`
Histórico versionado (recalculado conforme RN-203).
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| data_vigencia_inicio | date | |
| calorias_alvo | numeric | |
| proteina_alvo_g | numeric | |
| carboidrato_alvo_g | numeric | |
| gordura_alvo_g | numeric | |
| motivo_recalculo | text | ex: "atualização de peso corporal" |

### 2.18 `habitos`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| nome | text | inclui pré-definidos (creatina, vitaminas, etc.) e customizados |
| tipo | enum(`pre_definido`,`customizado`) | |
| ativo | boolean | |

### 2.19 `habito_registros`
| Campo | Tipo | Notas |
|---|---|---|
| habito_id | UUID (FK) | |
| data | date | |
| concluido | boolean | |

### 2.20 `registros_sono`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| data | date | data de referência (dia em que a pessoa acordou) |
| horario_dormir | timestamp | |
| horario_acordar | timestamp | |
| duracao_minutos | integer | calculado |
| qualidade_percebida | integer | escala 1–5 |
| origem | enum(`manual`,`apple_health`) | preparado para integração futura |

### 2.21 `metas`
Metas declaradas pelo usuário (peso-alvo, etc.), distintas de metas nutricionais (que são derivadas/calculadas).
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| tipo | enum(`peso_alvo`,`forca_alvo`,`outro`) | |
| valor_alvo | numeric | |
| data_alvo | date | nullable |
| ativo | boolean | |

### 2.22 `recomendacoes_ia`
Implementa RF-1001 (Histórico de Decisões da IA).
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| tipo | enum(`progressao_carga`,`deload`,`troca_exercicio`,`ajuste_volume`,`sugestao_refeicao`,`alerta_evolution_score`) | |
| dados_entrada_snapshot | jsonb | snapshot dos dados considerados |
| recomendacao_estruturada | jsonb | a recomendação em formato estruturado (não apenas texto) |
| explicacao_texto | text | texto apresentado ao usuário |
| status_resposta | enum(`pendente`,`aceita`,`ajustada`,`rejeitada`) | |
| ajuste_detalhes | jsonb | nullable, preenchido se `ajustada` |
| data_hora | timestamp | |

### 2.23 `evolution_score_historico`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| data | date | |
| score_total | numeric | 0–100 |
| componente_treino | numeric | |
| componente_proteina | numeric | |
| componente_sono | numeric | |
| componente_hidratacao | numeric | |
| componente_recuperacao | numeric | |
| componente_consistencia | numeric | |
| componente_evolucao_corporal | numeric | |
| componente_mais_fraco | text | identifica qual componente motivou a dica do dia |

### 2.24 `registros_hidratacao`
| Campo | Tipo | Notas |
|---|---|---|
| user_id | UUID (FK) | |
| data_hora | timestamp | |
| quantidade_ml | numeric | |

---

## 3. Diagrama de Relacionamentos (texto)

```
profiles (1) ──── (N) contextos_treino
profiles (1) ──── (N) limitacoes_fisicas
profiles (1) ──── (N) planos_treino ──── (N) plano_treino_exercicios ──── (1) exercicios
profiles (1) ──── (N) sessoes_treino ──── (N) series_executadas ──── (1) exercicios
                         sessoes_treino (N) ──── (1) planos_treino [nullable]

profiles (1) ──── (N) registros_peso_medidas
profiles (1) ──── (N) fotos_evolucao

profiles (1) ──── (N) estoque_cozinha ──── (1) alimentos
profiles (1) ──── (N) lista_compras ──── (1) alimentos
profiles (1) ──── (N) refeicoes_registradas ──── (N) refeicao_itens ──── (1) alimentos
profiles (1) ──── (N) metas_nutricionais

profiles (1) ──── (N) habitos ──── (N) habito_registros
profiles (1) ──── (N) registros_sono
profiles (1) ──── (N) registros_hidratacao
profiles (1) ──── (N) metas

profiles (1) ──── (N) recomendacoes_ia
profiles (1) ──── (N) evolution_score_historico
```

**Observação de design:** `exercicios` e `alimentos` são catálogos compartilhados (não duplicados por usuário), o que já antecipa o cenário multi-usuário futuro sem necessidade de migração de dados — um catálogo só precisa de uma tabela de "favoritos/preferências por usuário" associada, não de duplicação por usuário.

---

## 4. Contratos de API (Spring Boot REST Controllers)

> **Nota de revisão:** os endpoints abaixo eram descritos originalmente como Edge Functions (Supabase/Deno). A decisão de backend mudou para Java/Spring Boot (ver [06-arquitetura.md](./06-arquitetura.md) §2), mas os contratos — inputs, outputs, e a regra de que o LLM nunca é chamado direto do cliente — permanecem os mesmos. Apenas o prefixo de path muda de `/v1/` para `/api/v1/` (convenção comum em projetos Spring Boot) e a implementação passa a ser um `@RestController` chamando um `@Service`, no mesmo padrão já usado no Finwise.

Todas seguem o padrão de versionamento definido em [06-arquitetura.md](./06-arquitetura.md) §5: prefixo `/api/v1/`.

### 4.1 `POST /api/v1/treino/gerar-plano`
Gera ou regenera um plano de treino completo (RF-101).
- **Input:** `contextoTreinoId`
- **Output:** `planoTreino` (estruturado) + `explicacaoTexto`
- **Efeito colateral:** cria registro em `recomendacoes_ia` (tipo `ajuste_volume` ou equivalente quando é regeneração; primeira geração não conta como "recomendação" no mesmo sentido de ajuste).

### 4.2 `POST /api/v1/treino/sugestao-proxima-sessao`
Calcula a sugestão de carga/repetição para um exercício específico antes da execução (RF-106, RN-101).
- **Input:** `exercicioId`, `planoTreinoExercicioId`
- **Output:** `cargaSugeridaKg`, `repeticoesSugeridas`, `explicacaoTexto`

### 4.3 `POST /api/v1/treino/finalizar-sessao`
Processa o fim de uma sessão: calcula volume, detecta PRs, dispara avaliação de platô/deload (RN-102, §3.3–3.4 de 03-ia-conceito.md) de forma assíncrona (ex: `@Async` do Spring, ou um job agendado leve — não precisa de fila de mensagens dedicada na escala do MVP).
- **Input:** `sessaoTreinoId`
- **Output:** `resumo` (volume, PRs, duração) — avaliação de deload/platô retorna como recomendação separada via `recomendacoes_ia`, não bloqueia a resposta desta chamada.

### 4.4 `POST /api/v1/nutricao/sugestao-refeicao`
Implementa o algoritmo de §4.2 de [03-ia-conceito.md](./03-ia-conceito.md), incluindo parsing de linguagem natural (§4.3).
- **Input:** `tipoRefeicao` (opcional), `perguntaLivre` (texto livre opcional, ex: "tenho só ovos e arroz")
- **Output:** `refeicaoSugerida` (itens + quantidades), `coberturaMacroPercentual`, `itensFaltantes` (nullable), `explicacaoTexto`

### 4.5 `POST /api/v1/nutricao/recalcular-metas`
Disparado automaticamente por trigger de atualização de peso (RN-203) ou manualmente.
- **Input:** nenhum explícito — usuário identificado via JWT no header `Authorization`
- **Output:** novo registro em `metas_nutricionais`

### 4.6 `GET /api/v1/evolution-score/hoje`
- **Output:** `scoreTotal`, componentes individuais, `componenteMaisFraco`, `recomendacaoAcao` (texto)

### 4.7 `POST /api/v1/ia/chat`
Endpoint conversacional de propósito geral (modo reativo, §1 de 03-ia-conceito.md), usado pelo overlay de Chat com a IA.
- **Input:** `mensagem`, `contextoConversa` (histórico recente da sessão de chat)
- **Output:** resposta em streaming (Server-Sent Events via Spring WebFlux, ou polling simples no MVP se streaming completo não for prioridade imediata — ver nota abaixo) para UX de "digitando...", ver RF de Estados de Loading em 04-ux-fluxos.md
- **Nota de implementação:** o Finwise atual não usa WebFlux; streaming de resposta de LLM é a primeira necessidade real de reatividade do projeto. Avaliar se vale introduzir um único endpoint reativo (WebFlux) coexistindo com o restante da API em Spring MVC tradicional (ambos coexistem no mesmo projeto Spring Boot sem conflito), ou se uma resposta não-streamed (aguarda o LLM completo, com indicador de loading no frontend) é aceitável para o MVP — a segunda opção é mais simples e razoável até o produto provar que o streaming faz diferença perceptível de UX.
- **Nota de segurança:** este é o único endpoint que faz chamada direta a um LLM externo com texto livre do usuário; deve ter rate-limiting e sanitização de input mais rigorosos que os demais (ver [09-seguranca-riscos.md](./09-seguranca-riscos.md)).

---

## 5. Estratégia de Cache

| Dado | Estratégia |
|---|---|
| Catálogo de exercícios/alimentos | Cache local de longa duração (raramente muda), invalidado por versão/timestamp do catálogo |
| Plano de treino ativo | Cache local, sincronizado a cada alteração, disponível offline |
| Evolution Score | Calculado uma vez por dia (job agendado), cacheado; recálculo on-demand é exceção, não padrão |
| Recomendações da IA | Geradas e persistidas no momento do evento que as dispara (fim de sessão, etc.), nunca recalculadas a cada visualização |
