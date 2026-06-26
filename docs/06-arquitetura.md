# 06 — Arquitetura

---

## 1. Comparação de Plataformas de Cliente

> **Nota de revisão (alteração de premissa):** a versão original deste documento partia da premissa de acesso a Mac + Xcode + conta Apple Developer, e recomendava SwiftUI nativo. Essa premissa não se sustenta no contexto real do projeto: desenvolvimento solo, sem Mac disponível e sem orçamento para Apple Developer Program (US$99/ano) na fase inicial. Esta seção foi reescrita para refletir essa restrição como **fator decisório primário**, não como detalhe secundário. SwiftUI nativo permanece documentado como direção de migração futura (§1.5), não como decisão descartada por falta de mérito técnico.

### 1.1 Critérios de avaliação (revisados)

Critérios ponderados nesta ordem: (1) viabilidade real sem Mac/Apple Developer, (2) reuso do conhecimento e da stack já dominada pelo desenvolvedor (Java/Spring Boot, React/TypeScript — o mesmo par usado no Finwise), (3) custo de hospedagem compatível com uso pessoal, (4) caminho de evolução para app nativo iOS quando/se Mac e conta Apple Developer estiverem disponíveis.

### 1.2 SwiftUI nativo

**Vantagens:** nativismo total, acesso direto a HealthKit/WidgetKit/Live Activities (funcionalidades futuras do produto), performance nativa.

**Desvantagens (decisivas neste contexto):** **exige Mac para compilar** — não existe alternativa (não há toolchain Linux/Windows para SwiftUI). Exige conta Apple Developer para instalar em iPhone físico além de 7 dias (build via Xcode sem conta paga expira semanalmente) e é obrigatória para qualquer distribuição via TestFlight ou App Store. **Inviável agora, não por mérito técnico, mas por barreira de acesso.**

### 1.3 React Native / Flutter

Mesmo problema fundamental do SwiftUI para este contexto: ambos ainda exigem um **build final via Xcode em um Mac** para gerar o artefato iOS (mesmo escrevendo a maior parte do código em JS/Dart, a compilação e assinatura do app passam pela toolchain Apple). Não resolvem a restrição central. Descartados pelo mesmo motivo do SwiftUI, sem precisar entrar no mérito de produtividade ou ecossistema.

### 1.4 PWA (Progressive Web App)

**Vantagens:**
- **Nenhuma dependência de Mac, Xcode ou Apple Developer.** O "build" é só implantar um site; o "instalar no iPhone" é Safari → Compartilhar → Adicionar à Tela de Início — funcionalidade nativa do iOS desde sempre, sem loja, sem assinatura, sem custo.
- Reuso direto da stack que o desenvolvedor já domina e já usa em produção (Finwise): **React + TypeScript + Vite** no front, **Java/Spring Boot + MySQL** no back, deploy em **Vercel + Railway** — zero curva de aprendizado de plataforma nova.
- Funciona em qualquer dispositivo com navegador (iPhone, Android, desktop) sem código separado por plataforma.
- Suporta boa parte do que o produto precisa no MVP: ícone na tela de início, tela cheia (sem barra de navegador), notificações via Web Push (com ressalvas, ver §1.6), cache offline via Service Worker.

**Desvantagens (trade-offs assumidos conscientemente):**
- **Sem acesso a HealthKit, Live Activities, Widgets de tela de início ou Apple Watch.** Essas funcionalidades futuras (v3/v4 no roadmap original) ficam bloqueadas enquanto o cliente for PWA — não há contorno técnico para isso, é restrição de plataforma da própria Apple para apps web.
- Notificações push em iOS via Web Push exigem iOS 16.4+ e o PWA já instalado na tela de início (não funciona pelo navegador comum) — funcional, mas com mais atrito de configuração do que push nativo.
- Sem acesso a sensores/integrações de hardware mais profundas (NFC, por exemplo, citado em funcionalidades futuras) — também bloqueado nesta abordagem.
- Performance e fluidez de animação tendem a ficar um nível abaixo de SwiftUI nativo, embora suficiente para os fluxos do produto (registro de série, navegação entre telas) se a UI for bem otimizada.

### 1.5 Decisão e justificativa

**Decisão: PWA com React + Vite, consumindo uma API Java/Spring Boot.**

Justificativa consolidada:

1. **A restrição de acesso (sem Mac, sem orçamento Apple Developer) é, neste momento, mais determinante do que qualquer critério de qualidade de experiência.** Um app nativo SwiftUI perfeito que não pode ser compilado nem instalado não é uma opção real — é uma opção teórica. PWA é a única alternativa das avaliadas que roda no iPhone do desenvolvedor hoje, sem custo e sem hardware adicional.
2. **Reuso de stack é alto valor aqui:** o desenvolvedor já tem Java/Spring Boot + React/TypeScript + MySQL rodando em produção (Finwise, deployado em Railway + Vercel). Replicar esse padrão para o Hypertrophy OS significa que o tempo de desenvolvimento vai para a lógica de produto (motor de regras da IA, fluxos de treino/nutrição), não para aprender uma stack nova.
3. **O motor de regras da IA (a parte mais importante do produto) é backend puro** — ele não depende em nada da escolha de cliente. Toda a lógica determinística descrita em [03-ia-conceito.md](./03-ia-conceito.md) §6.2 vive igualmente bem atrás de uma API Spring Boot quanto atrás de uma Edge Function Supabase. A escolha de cliente não compromete a parte conceitualmente central do produto.
4. **Caminho de migração para nativo fica aberto, não fechado.** Se/quando houver Mac e conta Apple Developer disponíveis, a migração para SwiftUI nativo reaproveita 100% do backend Java/Spring Boot e do modelo de dados — apenas a camada de apresentação seria reescrita, exatamente como a versão original deste documento já previa para o cenário SwiftUI → Android (§1.2 antiga). A diferença agora é que o "cliente provisório" é PWA em vez de "cliente único nativo".

### 1.6 Notas técnicas de implementação do PWA

- Use `vite-plugin-pwa` (ou configuração manual de `manifest.json` + Service Worker) para gerar o manifest com ícone, splash screen e modo `standalone` (remove a barra de navegador ao abrir pela tela de início — essencial para a sensação de "app de verdade", alinhado ao princípio de produto de uso multidiário).
- Teste o fluxo de instalação real no Safari do iPhone (Compartilhar → Adicionar à Tela de Início) desde as primeiras semanas de desenvolvimento — o comportamento de PWA no Safari tem particularidades (cache mais agressivo, Web Push com requisitos específicos de versão de iOS) que é melhor descobrir cedo.
- Web Push no iOS exige HTTPS (Vercel já fornece por padrão) e o app já estar instalado na tela de início — documentar esse requisito na própria UI de onboarding ("para receber notificações, adicione o app à tela de início").

---

## 2. Backend

### 2.1 Decisão (revisada): Java/Spring Boot + MySQL, em vez de BaaS

> **Nota de revisão:** a versão original avaliava Supabase vs. Firebase vs. AWS Amplify (todos BaaS). Essa avaliação fica registrada em §2.4 como referência histórica, mas a decisão atual é diferente: **API própria em Java/Spring Boot com MySQL**, replicando o padrão já validado em produção pelo desenvolvedor no projeto Finwise (Spring Boot + MySQL + Docker, deploy em Railway). Motivo da mudança: nenhum critério técnico mudou — o motivo é 100% de contexto (reuso de stack já dominada, sem necessidade de aprender Postgres/RLS/Edge Functions/Deno do zero quando Java/Spring já resolve o mesmo problema de modelagem relacional).

### 2.2 Por que ainda é a escolha certa tecnicamente (não é só "o que eu já sei")

O argumento estrutural da versão original continua de pé: o modelo de dados do produto é **fundamentalmente relacional** (séries → sessões → treinos → planos, com agregações cruzadas constantes — volume por grupo muscular, médias móveis de peso, correlação sono×performance). Isso pede SQL relacional, não um banco de documentos. MySQL entrega isso com a mesma adequação estrutural que Postgres entregaria — a escolha entre os dois é majoritariamente uma questão de familiaridade e ecossistema, não de capacidade (ambos são RDBMS maduros com suporte total a joins, agregação, transações). Como o Finwise já roda em MySQL com sucesso, manter o mesmo motor de banco reduz superfície de novidade sem perda técnica relevante para este produto.

### 2.3 O que o Spring Boot assume no lugar do BaaS

| Responsabilidade (antes do BaaS) | Quem assume agora |
|---|---|
| Postgres/RLS para isolamento de dados por usuário | **Spring Security** + filtro de `user_id` em toda query/repositório (autorização a nível de aplicação, não a nível de banco — ver nota de risco em §2.5) |
| Auth (Supabase Auth) | **Spring Security + JWT**, no mesmo padrão já usado no Finwise |
| Storage de fotos de evolução | Armazenamento de arquivo (ex: disco do servidor + backup, ou bucket S3-compatible barato — avaliar ao chegar nessa feature; não é bloqueador do MVP, que não inclui fotos) |
| Edge Functions (motor de regras da IA) | **Services/Components dentro do próprio Spring Boot** — endpoints REST normais, não funções serverless separadas |

### 2.4 Comparação original (mantida como referência histórica)

A comparação Supabase vs. Firebase vs. AWS Amplify, feita quando a premissa era usar um BaaS, permanece válida como leitura caso o projeto algum dia reavalie essa direção (ex: ao escalar para milhares de usuários e quiser delegar mais infraestrutura) — mas não é a decisão ativa hoje.

<details>
<summary>Comparação Supabase vs. Firebase vs. AWS Amplify (histórico, não aplicável à decisão atual)</summary>

**Supabase:** Postgres real, RLS nativa, Edge Functions em Deno/TypeScript, custo baixo inicial.
**Firebase:** Firestore é NoSQL — exigiria desnormalização forçada para o modelo relacional deste produto.
**AWS Amplify:** poder de escala alto, mas complexidade operacional desproporcional para um desenvolvedor único no início.

Se essa rota fosse escolhida, Supabase venceria pelo mesmo motivo estrutural de §2.2: alinhamento com modelo relacional.
</details>

### 2.5 Nota de risco: isolamento de dados sem RLS nativa

Sem um banco com Row Level Security nativa (como o Postgres do Supabase oferecia), o isolamento de dados por usuário em MySQL/Spring Boot depende inteiramente de **disciplina de código** — toda query precisa filtrar por `user_id` explicitamente na camada de repositório/service, sem exceção. Isso é o mesmo padrão que o Finwise já usa (e funciona bem em escala pequena/média), mas é uma responsabilidade que migra do banco para a aplicação. Recomendação: criar um teste automatizado dedicado que verifique, para cada entidade de usuário, que um usuário A nunca consegue ler/escrever dado de um usuário B — isso compensa a ausência de RLS nativa com uma rede de segurança em testes. Ver detalhamento em [09-seguranca-riscos.md](./09-seguranca-riscos.md) §1.2 (atualizado).

---

## 3. Padrão Arquitetural da Aplicação

### 3.1 Clean Architecture com separação de camadas

```
┌─────────────────────────────────────┐
│  Apresentação (React Components)     │  <- depende de hooks/state, nunca de chamadas de API diretamente
├─────────────────────────────────────┤
│  Hooks / State (ex: custom hooks,     │  <- orquestram chamadas à API, sem lógica de negócio própria
│  React Query ou equivalente)         │
├─────────────────────────────────────┤
│  Camada de API (cliente HTTP         │  <- único lugar que conhece os contratos REST do backend
│  tipado, ex: serviços TS por domínio)│
└─────────────────────────────────────┘
                    │
                    ▼ HTTP/REST (JSON)
┌─────────────────────────────────────┐
│  Controllers (Spring @RestController)│  <- validação de request, sem lógica de negócio
├─────────────────────────────────────┤
│  Services (lógica de negócio,        │  <- motor de regras da IA vive aqui (RF-101 a RF-902)
│  incluindo motor de regras da IA)    │
├─────────────────────────────────────┤
│  Repositories (Spring Data JPA)      │  <- abstrai acesso ao MySQL
└─────────────────────────────────────┘
```

**Por que isso importa especificamente para este produto:** o motor de regras da IA ([03-ia-conceito.md](./03-ia-conceito.md) §6.2) precisa ser testável de forma isolada — testes unitários de Service no Spring Boot cobrem exatamente isso, no mesmo padrão que o desenvolvedor já aplicaria a qualquer regra de negócio de domínio financeiro no Finwise. Como o motor de regras vive inteiramente no backend (nunca no cliente, ver §3.2), a troca de cliente SwiftUI → PWA não afeta em nada essa camada — ela já nasceu desacoplada da apresentação.

### 3.2 Onde vive o "motor de regras" da IA

Decisão (mantida, apenas o veículo muda): a lógica determinística (Opção B de [03-ia-conceito.md](./03-ia-conceito.md) §6.2 — cálculo de progressão, deload, macro, Evolution Score) vive em **Services do Spring Boot**, não no cliente React. Motivos:
- Garante que a mesma lógica produza o mesmo resultado independente do dispositivo/navegador.
- Centraliza a chamada ao LLM externo (explicação textual, interpretação de linguagem natural) em um único ponto controlado no backend, nunca exposta direto do cliente PWA (chave de API do provedor de LLM nunca deve estar no bundle JavaScript do frontend — requisito de segurança não-negociável, ver [09-seguranca-riscos.md](./09-seguranca-riscos.md)).
- Atualizar a lógica de regras é um deploy de backend (Railway), sem qualquer fricção de "nova versão de app" — vantagem que o PWA preserva tão bem quanto Edge Functions preservariam.

### 3.3 Funcionamento offline / cache no PWA

**Abordagem:** Service Worker (via `vite-plugin-pwa` ou Workbox) para cache de assets estáticos e de respostas de API de leitura frequente (plano de treino ativo, inventário de Minha Cozinha), com fila de sincronização para requisições de escrita feitas offline (ex: registro de série durante instabilidade de sinal na academia — alinhado a RNF-04).

**Trade-off assumido (mais acentuado que no cenário SwiftData nativo):** PWAs têm controle mais limitado sobre armazenamento local persistente em iOS comparado a um app nativo (o Safari pode, em cenários de pouco espaço, limpar dados de cache de PWAs mais agressivamente que um app nativo jamais sofreria). Mitigação: tratar o cache local como otimização de performance e suporte a instabilidade momentânea de rede, nunca como fonte de verdade — a fonte de verdade é sempre o MySQL via API, e nenhuma tela deve depender de dado que só existe no cache do navegador por longos períodos sem sincronizar.

---

## 4. Componentes de Infraestrutura

| Componente | Escolha | Justificativa breve |
|---|---|---|
| Banco de dados | MySQL | Mesmo motor já usado em produção no Finwise; relacional, adequado ao modelo de dados (ver §2.2) |
| Backend / API | Java + Spring Boot | Stack já dominada pelo desenvolvedor; mesma base do Finwise (Security, JPA, Flyway) |
| Hospedagem do backend | Railway | Mesmo provedor já usado para o Finwise — processo de deploy já conhecido |
| Frontend (PWA) | React + Vite + TypeScript | Mesma stack de frontend do Finwise-Front; reuso direto de padrões (Axios, Tailwind) |
| Hospedagem do frontend | Vercel | Mesmo provedor já usado para o Finwise-Front |
| Autenticação | Spring Security + JWT | Mesmo padrão já validado no Finwise |
| Migrations de banco | Flyway | Já usado no Finwise; mantém consistência de versionamento de schema |
| Storage (fotos de evolução, v1.1+) | A definir ao chegar na feature — opção mais simples: disco do servidor com backup, ou bucket S3-compatible de baixo custo | Não é bloqueador do MVP (fotos não estão no MVP, ver [08-roadmap-backlog.md](./08-roadmap-backlog.md)) |
| Notificações | Web Push (via Service Worker) | Único caminho de notificação nativo viável em PWA no iOS — ver §1.6 |
| Analytics | A definir entre opções privacy-friendly | Mantém alinhamento com LGPD/privacy by design (RNF-08) |
| Crash/erro no frontend | Logging próprio + console estruturado na fase inicial; reavaliar ferramenta dedicada (ex: Sentry) ao escalar | Suficiente para fase de uso pessoal/MVP, sem custo prematuro |
| CI/CD | GitHub Actions | Mesmo padrão replicável do Finwise — build/test do Spring Boot e deploy automático |

> **Nota sobre Analytics:** a escolha final de ferramenta de analytics deve ser revisitada no momento de implementação considerando custo em escala e conformidade LGPD para dados de uso (distintos de dados de saúde, que nunca devem trafegar para uma ferramenta de analytics de terceiros — ver regra explícita em [09-seguranca-riscos.md](./09-seguranca-riscos.md)).

---

## 5. Versionamento de API e Observabilidade

- **Versionamento:** todos os endpoints REST do Spring Boot devem seguir versionamento explícito no path (ex: `/api/v1/treino/progressao`), mesmo na fase single-user — o custo de adicionar isso desde o início é mínimo e evita breaking changes silenciosos quando o app já estiver em uso (próprio ou de terceiros).
- **Observabilidade:** logging estruturado em todos os Services que envolvem o motor de regras da IA (entrada, saída, tempo de execução, e — criticamente — qual chamada de LLM externo foi feita e seu custo/latência), permitindo depurar tanto bugs de lógica quanto custos de operação da IA à medida que o uso crescer.

---

## 6. Resumo da Stack Final

| Camada | Tecnologia |
|---|---|
| Cliente | PWA — React + Vite + TypeScript, instalável na tela de início do iPhone via Safari |
| Backend / API | Java + Spring Boot |
| Banco de dados | MySQL + Flyway (migrations) |
| Autenticação | Spring Security + JWT |
| Lógica de IA — decisão | Motor de regras determinístico em Services do Spring Boot |
| Lógica de IA — linguagem | LLM externo (via API), chamado exclusivamente do backend, nunca direto do cliente |
| Notificações | Web Push via Service Worker |
| Hospedagem | Backend e banco no Railway; frontend no Vercel |
| CI/CD | GitHub Actions |

**Migração futura (se Mac + Apple Developer ficarem disponíveis):** apenas a camada "Cliente" mudaria, de PWA para SwiftUI nativo — todo o restante da stack (backend, banco, motor de regras, autenticação) permanece exatamente como está, sem necessidade de reescrita. Essa é a mesma garantia que a versão original deste documento já previa para uma eventual expansão a Android: a arquitetura em camadas isola a decisão de cliente da decisão de backend.
