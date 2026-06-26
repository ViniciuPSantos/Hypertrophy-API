# Hypertrophy OS — Documentação de Produto

**Versão:** 0.2 (Revisão de stack — ver nota abaixo)
**Data:** Junho de 2026
**Status:** Documento vivo — espera-se revisão contínua à medida que decisões de produto e arquitetura forem validadas na prática

> **Nota de revisão (v0.2):** a versão 0.1 partia da premissa de Mac + Xcode + Apple Developer disponíveis e recomendava SwiftUI + Supabase. Essa premissa não correspondia à realidade do desenvolvedor (sem Mac, sem orçamento para Apple Developer). A stack foi revisada para: **cliente PWA (React + Vite + TypeScript)** e **backend Java/Spring Boot + MySQL**, reaproveitando o padrão já usado em produção no projeto Finwise do próprio desenvolvedor. Detalhes e justificativa completa em [06-arquitetura.md](./06-arquitetura.md) §1 e §2. A lógica de produto, IA e modelagem de dados (docs 01 a 04, 07 a 09) permanece majoritariamente inalterada — a mudança é de veículo técnico, não de visão de produto.

---

## O que é este conjunto de documentos

Esta é a especificação completa do **Hypertrophy OS**, um aplicativo mobile que funciona como um "Sistema Operacional da Evolução Física" — não um simples registrador de treinos, mas um assistente pessoal de hipertrofia cujo núcleo é uma IA que toma decisões contínuas sobre treino, nutrição, recuperação e hábitos, sempre justificando suas recomendações com dados do próprio usuário e evidência científica.

O projeto nasce para uso pessoal (uma única pessoa, você), mas é desenhado desde o primeiro dia para escalar a milhares de usuários sem reescrita arquitetural. Isso significa: decisões simplificadas onde o custo de simplificar é baixo (ex: sem multi-tenancy complexo agora), mas fundamentos corretos onde reescrever depois seria caro (modelagem de dados, separação de camadas, versionamento de API).

## Como navegar

| Arquivo | Conteúdo | Quando consultar |
|---|---|---|
| [01-product-vision.md](./01-product-vision.md) | Visão de produto, problema, personas, diferenciais, filosofia do produto | Antes de decidir se uma feature nova "pertence" ao produto |
| [02-prd-requisitos.md](./02-prd-requisitos.md) | PRD completo, requisitos funcionais/não-funcionais, regras de negócio, casos de uso, user stories, critérios de aceitação | Ao especificar ou revisar qualquer funcionalidade |
| [03-ia-conceito.md](./03-ia-conceito.md) | Conceito da IA, banco de conhecimento científico, lógica de treino/nutrição/Evolution Score | Ao implementar qualquer lógica de recomendação |
| [04-ux-fluxos.md](./04-ux-fluxos.md) | Sitemap, jornadas, wireframes em texto, estados vazios/erro/loading, onboarding | Ao construir qualquer tela |
| [05-design-system.md](./05-design-system.md) | Design system, tipografia, cores, modo claro/escuro, componentes, acessibilidade | Ao estilizar qualquer componente |
| [06-arquitetura.md](./06-arquitetura.md) | Comparação de frameworks e backends, stack final, padrões arquiteturais, CI/CD | Antes de iniciar o projeto técnico ou ao decidir uma dependência nova |
| [07-modelagem-dados.md](./07-modelagem-dados.md) | Modelo de dados completo, entidades, relacionamentos, contratos de API | Ao criar/alterar schema ou endpoint |
| [08-roadmap-backlog.md](./08-roadmap-backlog.md) | MVP → v4, backlog priorizado, métricas de sucesso, KPIs, eventos de analytics | Ao planejar o que construir a seguir |
| [09-seguranca-riscos.md](./09-seguranca-riscos.md) | Segurança, LGPD, riscos técnicos, monetização futura, funcionalidades futuras (Apple Watch, etc.) | Antes de lidar com dados sensíveis ou avaliar riscos de uma decisão |

## Princípios que atravessam todos os documentos

1. **A IA é o produto, não um recurso.** Toda funcionalidade existe para alimentar ou expressar a inteligência do sistema. Se uma feature não muda o que a IA sabe ou recomenda, ela é secundária.
2. **Transparência sempre.** Nenhuma recomendação da IA é uma caixa-preta — toda decisão deve ser rastreável a um dado real do usuário e a um princípio científico.
3. **Velocidade de registro acima de tudo.** Se registrar algo leva mais de 30 segundos, a feature está mal desenhada. Fricção mata consistência, e consistência é o único preditor real de hipertrofia.
4. **Simplicidade pessoal, fundação escalável.** Onde a decisão for reversível e de baixo custo, simplifique para uso pessoal. Onde for estrutural (dados, contratos de API, autenticação), projete como se já houvesse 10 mil usuários.
5. **iPhone primeiro, via PWA por restrição de acesso atual.** Toda decisão de UX e design prioriza a experiência no iPhone do usuário, hoje entregue como PWA instalável (sem Mac/Apple Developer disponíveis — ver [06-arquitetura.md](./06-arquitetura.md) §1). Migração para SwiftUI nativo é o plano de longo prazo, não uma decisão definitiva contra o nativo.

## Convenções usadas nestes documentos

- **RF-XX**: Requisito Funcional, numerado e referenciável.
- **RNF-XX**: Requisito Não Funcional.
- **US-XX**: User Story.
- **RN-XX**: Regra de Negócio.
- Trade-offs são sempre apresentados antes de uma recomendação — nunca uma escolha é feita sem mostrar a alternativa descartada e o motivo.
