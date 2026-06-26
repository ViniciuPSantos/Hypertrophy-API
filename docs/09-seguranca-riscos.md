# 09 — Segurança, Riscos, Monetização Futura e Funcionalidades Futuras

---

## 1. Segurança

### 1.1 Autenticação

> **Nota de revisão:** Sign in with Apple e Supabase Auth pressupunham app nativo iOS distribuído via App Store. No cliente PWA atual ([06-arquitetura.md](./06-arquitetura.md) §1), esse requisito não se aplica — não há App Store envolvida.

- **Spring Security + JWT** como mecanismo de autenticação, no mesmo padrão já usado e validado no Finwise.
- E-mail/senha como método principal, com política de senha mínima (comprimento, não composição arbitrária complexa — alinhado a recomendações modernas de segurança que priorizam comprimento sobre regras de caractere especial obrigatório).
- Login social (ex: Google) pode ser avaliado como conveniência futura, mas não é requisito — diferente do cenário App Store, nenhuma loja está exigindo isso aqui.

### 1.2 Autorização

> **Nota de revisão:** sem Postgres/RLS nativa neste contexto (MySQL não oferece o mesmo mecanismo — ver [06-arquitetura.md](./06-arquitetura.md) §2.5), o isolamento de dados por usuário passa a ser responsabilidade explícita da camada de aplicação.

- Toda query/repositório (`@Repository` do Spring Data JPA) que acessa tabela com `user_id` deve filtrar explicitamente pelo usuário autenticado (extraído do JWT), **sem exceção**, desde o dia 1 — mesmo operando com um único usuário. Custo de implementar desde o início é baixo; custo de retrofitar com dados de produção e múltiplos usuários reais seria alto.
- Recomendação concreta de implementação: criar uma camada de teste de integração dedicada que tenta acessar dado de um usuário B autenticado como usuário A para cada entidade sensível, falhando o build se algum endpoint não isolar corretamente — compensa, via testes, a ausência de uma garantia automática a nível de banco.
- Catálogos compartilhados (`exercicios`, `alimentos`) são somente-leitura para usuários finais; escrita restrita a um papel administrativo (o próprio desenvolvedor, na fase atual, via role do Spring Security).

### 1.3 Criptografia

- Dados em trânsito: HTTPS obrigatório em toda comunicação cliente↔backend (Vercel fornece TLS por padrão no frontend; Railway fornece TLS por padrão no backend).
- Dados em repouso: avaliar criptografia de campos especialmente sensíveis a nível de aplicação (ex: usando recursos do próprio JPA/Hibernate ou bibliotecas de criptografia em campos específicos) já que não há um equivalente direto de "criptografia nativa gerenciada" como a do Postgres do Supabase — MySQL gerenciado em provedores como Railway oferece criptografia em repouso da infraestrutura, mas não há controle de acesso por linha (RLS) embutido.

### 1.4 Armazenamento seguro de fotos de evolução

- Fotos (feature não incluída no MVP, ver [08-roadmap-backlog.md](./08-roadmap-backlog.md)) nunca devem ser publicamente acessíveis por URL direta; ao implementar, todo acesso deve passar por endpoint autenticado do próprio backend (que verifica o `user_id` antes de servir o arquivo) ou por URL assinada de curta duração, caso a escolha de armazenamento seja um bucket S3-compatible — consistente com o tratamento de dado sensível de saúde, independente de onde o arquivo fisicamente reside.

### 1.5 LGPD

Mesmo com um único usuário (o próprio desenvolvedor) na fase inicial, os princípios de LGPD são adotados desde o design (privacy by design, RNF-08) porque: (a) é o padrão profissional correto, (b) evita retrabalho quando/se o produto abrir para terceiros, (c) dados de saúde são categoria especialmente sensível sob a LGPD (art. 11), exigindo tratamento mais cuidadoso mesmo em uso pessoal.

Práticas adotadas:
- **Minimização de dados:** nenhum dado é coletado "porque pode ser útil algum dia" sem um RF que o justifique.
- **Finalidade explícita:** cada categoria de dado tem propósito claro documentado neste conjunto de documentos (ex: peso → Evolution Score e progressão; localização nunca é coletada, pois não há RF que a exija).
- **Direito de exportação e exclusão:** mesmo no MVP, deve existir (ainda que via suporte manual do desenvolvedor, dado o contexto single-user) um caminho para exportar todos os dados do usuário e para exclusão completa de conta — tecnicamente simples de implementar como um endpoint próprio no Spring Boot (ex: `GET /api/v1/usuario/exportar-dados` retornando um JSON/ZIP com tudo) desde o início, e que se torna obrigatório operacionalmente quando o produto tiver terceiros como usuários.
- **Dados de saúde nunca vão para ferramentas de analytics de terceiros:** eventos de analytics (§4 de 08-roadmap-backlog.md) carregam apenas metadados de uso (ex: "uma série foi registrada", tempo de registro), nunca os valores em si (peso, carga, percentual de gordura). Essa é uma regra de arquitetura de dados, não apenas de política.

### 1.6 Backups e recuperação de conta

- Backups automáticos do MySQL gerenciados pela infraestrutura do Railway (verificar periodicidade/retenção do plano contratado); para a fase de uso pessoal, considerar também um backup manual periódico adicional (`mysqldump` agendado) como camada extra de segurança, já que o volume de dados é pequeno e o custo dessa prática é mínimo.
- Recuperação de conta via fluxo padrão de redefinição de senha (e-mail + token, mesmo padrão já usado no Finwise); não há necessidade de fluxo customizado complexo na fase atual.

---

## 2. Riscos Técnicos

| Risco | Probabilidade | Impacto | Mitigação |
|---|---|---|---|
| LLM externo gera explicação textual incoerente com a recomendação numérica determinística | Média | Alto (mina a confiança, viola a filosofia de transparência) | Separação estrita Opção B (motor de regras gera o número, LLM apenas explica o número já decidido) — ver [03-ia-conceito.md](./03-ia-conceito.md) §6.2. Validar com testes que a explicação sempre referencia os dados reais do snapshot salvo em `recomendacoes_ia`. |
| Custo de chamadas a LLM externo cresce de forma não-linear com uso (especialmente o endpoint de chat livre, §4.7 de 07-modelagem-dados.md) | Média | Médio (afeta viabilidade econômica em escala, não a fase pessoal) | Logging de custo por chamada desde o início (§5 de 06-arquitetura.md); cache de respostas para perguntas funcionalmente equivalentes; rate-limiting no endpoint de chat. |
| Detecção de deload/platô gera falso positivo ou falso negativo por dados insuficientes nas primeiras semanas | Alta nas primeiras semanas, decrescente depois | Médio (recomendação ruim cedo pode minar confiança antes mesmo dela se estabelecer) | RN-102 já exige sinais combinados (não isolados); adicionar declaração explícita de "baixa confiança por pouco histórico" na UI nos primeiros dias (§4.1 de 04-ux-fluxos.md, jornada de ativação). |
| Sincronização offline gera conflito em cenário multi-dispositivo (mesmo o desenvolvedor pode usar iPhone + algum outro dispositivo eventualmente) | Baixa no MVP, crescente com o tempo | Médio | Estratégia simplificada (last-write-wins) aceita conscientemente no MVP (ver [06-arquitetura.md](./06-arquitetura.md) §3.3); revisar antes de v2 se uso multi-dispositivo se tornar real. |
| Catálogo de exercícios/alimentos incompleto trava a geração de plano/refeição por falta de opções compatíveis | Média no início (catálogo pequeno) | Médio | Processo deliberado de popular catálogo inicial com cobertura suficiente para os contextos de treino e alimentos do próprio uso pessoal antes do lançamento do MVP; não depende de IA para isso — é trabalho de dado estruturado comum. |
| Modelo de dados de `medidas` (jsonb flexível em `registros_peso_medidas`) dificulta queries agregadas fortes (ex: comparação estruturada de "braço" ao longo do tempo entre todos os registros) | Baixa a média | Baixo a médio | Aceito deliberadamente pela flexibilidade que oferece (adicionar nova medida sem migration); se agregações sobre medidas específicas se tornarem frequentes e custosas, normalizar essas medidas em colunas/tabela dedicada é uma migração localizada, não uma reescrita. |
| Dependência de disponibilidade/preço do provedor de LLM externo | Baixa a média | Alto (camada de explicação para de funcionar) | Abstrair a chamada ao LLM por trás de uma interface no backend (um `Service` dedicado no Spring Boot), permitindo troca de provedor sem alterar o cliente PWA nem o motor de regras determinístico. |

---

## 3. Monetização Futura (não implementar agora — apenas possibilidades documentadas)

Conforme instrução explícita do briefing original, monetização não é implementada nesta fase. As possibilidades abaixo são documentadas apenas para informar decisões arquiteturais que não devem fechar essas portas (ex: o modelo de dados já suporta a noção de "plano" e "metas" por usuário de forma que diferenciar tiers futuramente não exigiria reestruturação):

- **Premium (assinatura):** desbloqueio de funcionalidades avançadas (ex: técnicas avançadas de treino mais sofisticadas, relatórios avançados).
- **IA ilimitada:** possível modelo onde o uso básico do motor de regras é gratuito (já que é determinístico e de baixo custo computacional), mas o uso do endpoint de chat conversacional livre (que tem custo direto de LLM) tem limite no tier gratuito.
- **Apple Watch:** funcionalidade de hardware companion como diferencial de tier pago.
- **Coach personalizado:** possível camada híbrida humano+IA no futuro distante, fora do escopo de qualquer versão atualmente planejada.
- **Relatórios avançados:** exportação e análises mais profundas (ex: comparação de períodos, análises de correlação entre variáveis) como funcionalidade premium.
- **Nutrição avançada:** funcionalidades como reconhecimento de alimento por foto (mencionado em Funcionalidades Futuras, §4) como diferencial pago, dado o custo computacional mais alto dessas features.

---

## 4. Funcionalidades Futuras (catálogo de ideias, fora do roadmap formal até serem priorizadas)

Estas ideias são mantidas como catálogo de possibilidades, não compromissos de roadmap. Algumas já foram puxadas para versões específicas em [08-roadmap-backlog.md](./08-roadmap-backlog.md) §1; as demais permanecem aqui até serem avaliadas pelo teste de filosofia de produto ([01-product-vision.md](./01-product-vision.md) §5.1).

> **Nota de revisão (PWA):** as ideias marcadas com 🔒 abaixo dependem de capacidades exclusivas de app nativo iOS (HealthKit, WidgetKit, Live Activities, integração de hardware) e **não são alcançáveis pelo cliente PWA atual** ([06-arquitetura.md](./06-arquitetura.md) §1.4). Elas permanecem no catálogo como visão de longo prazo, condicionadas à migração futura para SwiftUI nativo quando Mac e conta Apple Developer estiverem disponíveis — não foram removidas do roadmap, apenas têm um pré-requisito adicional agora.

| Ideia | Nota de viabilidade/risco |
|---|---|
| 🔒 Apple Watch | Já planejado para v4; alto valor para a tese de "uso multidiário com fricção mínima". Exige app nativo — ver nota acima. |
| 🔒 Apple Health | Já planejado para v3; baixo risco técnico, alto valor para sono e possivelmente atividade. Exige app nativo — ver nota acima. |
| 🔒 Widgets | Já planejado para v3 no roadmap original. WidgetKit não existe para PWA — exige app nativo. |
| 🔒 Live Activities | Já planejado para v4 (cronômetro de descanso); valor claro, complexidade moderada. Exige app nativo — ver nota acima. |
| Scanner de alimentos (reconhecimento por foto) | Custo computacional/IA mais alto; candidato a feature premium futura (§3). Viável em PWA via input de câmera do navegador (`<input type="file" accept="image/*" capture>`), sem exigir app nativo. |
| Scanner de código de barras | Já planejado para v3; viável em PWA via APIs de câmera do navegador (ex: bibliotecas JS de leitura de barcode), embora com experiência um pouco menos fluida que uma API nativa de câmera — risco baixo, não bloqueado pela escolha de cliente. |
| Reconhecimento corporal por IA (evolução visual automática) | Mencionado desde a visão original como aspiração de longo prazo; tecnicamente complexo e sensível (análise de imagem corporal exige cuidado redobrado de privacidade e precisão) — não deve ser priorizado antes de v4, e mesmo assim com avaliação cuidadosa de risco/benefício antes da implementação |
| Chat por voz | Extensão natural do endpoint de chat (§4.7 de 07-modelagem-dados.md) já existente; viável em PWA via Web Speech API (suporte variável no Safari iOS — validar antes de priorizar) |
| Registro por voz (séries, refeições) | Alto valor potencial para RNF-01 (registro rápido) se executado bem; mesma ressalva de suporte de Web Speech API no Safari iOS que o item acima; risco adicional de precisão em ambiente ruidoso de academia |
| Modo offline (completo) | Já planejado para v3 como evolução do modo degradado do MVP; em PWA, depende de Service Worker bem implementado (ver [06-arquitetura.md](./06-arquitetura.md) §3.3) |
| Backup automático | Já coberto pela infraestrutura do Railway (§1.6 acima); não é uma feature de produto separada |
| Compartilhar evolução | Tensão direta com a filosofia de "não é rede social" ([01-product-vision.md](./01-product-vision.md) §5.2) — se implementado, deve ser export pontual e privado (ex: gerar uma imagem para enviar a um amigo/treinador específico), nunca um feed ou mecanismo de comparação social embutido no produto |
| Exportar PDF | Já planejado para v2 |
| Importar dados (de outros apps, ex: Hevy/Strong) | Alto valor para reduzir fricção de migração de usuários vindos de outros apps; complexidade depende do formato de exportação desses concorrentes |
| 🔒 NFC para academia | Caso de uso (ex: check-in automático ao entrar na academia) interessante mas de valor incerto frente ao custo de implementação. Web NFC tem suporte praticamente inexistente no Safari iOS — na prática, exige app nativo. |
| Comunidade | Risco filosófico direto (rede social) — **não recomendado** a menos que haja revisão deliberada da filosofia de produto; mantido aqui apenas porque estava no briefing original, não porque é recomendado |
| Marketplace de treinos | Mesma ressalva da Comunidade — introduziria dinâmica de conteúdo de terceiros/UGC que contradiz a proposta central de IA-como-treinador-pessoal-individualizado; manter como nota, não como plano |

> **Nota editorial:** "Comunidade" e "Marketplace de treinos" estão listados porque apareciam no briefing original de funcionalidades futuras, mas o documento de Visão de Produto ([01-product-vision.md](./01-product-vision.md)) estabelece explicitamente que o produto **não é** uma rede social fitness. Essas duas ideias estão em tensão direta com esse princípio fundador e devem passar por revisão de filosofia de produto explícita — não apenas priorização de roadmap — antes de qualquer avanço.
