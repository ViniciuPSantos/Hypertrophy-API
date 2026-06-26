# 05 — Design System

---

## 1. Princípios de Design

Inspiração declarada: produtos Apple (clareza, profundidade tipográfica, uso generoso de espaço negativo), Linear (densidade de informação sem poluição visual) e Notion (hierarquia clara, modo escuro bem executado). Três princípios guiam toda decisão visual:

1. **Clareza antes de estética.** Nenhum elemento decorativo que não comunique informação ou estado.
2. **Hierarquia tipográfica forte.** Em telas escaneáveis em segundos (Dashboard), o tamanho e peso da fonte fazem o trabalho de organização visual, não bordas ou caixas excessivas.
3. **Cor com significado, não decoração.** Cor é usada para comunicar estado (progresso, alerta, sucesso), nunca apenas para "deixar bonito" sem propósito funcional.

---

## 2. Tipografia

Fonte do sistema: **SF Pro** (fonte nativa do iOS), respeitando Dynamic Type do usuário — nunca tamanhos de fonte fixos que ignorem a configuração de acessibilidade do sistema.

| Estilo | Uso | Tamanho base (pt) | Peso |
|---|---|---|---|
| Display | Evolution Score (número grande no Dashboard) | 48 | Bold |
| Title 1 | Títulos de tela | 28 | Bold |
| Title 2 | Títulos de seção/card | 20 | Semibold |
| Body | Texto corrido, explicações da IA | 17 | Regular |
| Callout | Texto secundário em cards | 15 | Regular |
| Caption | Metadados, timestamps, labels | 13 | Regular, cor secundária |

**Regra:** explicações da IA (texto que justifica recomendações) sempre usam o estilo Body, nunca Caption — é conteúdo central do produto, não uma nota de rodapé, e deve ter peso visual proporcional à sua importância na filosofia do produto.

---

## 3. Cores

### 3.1 Paleta semântica (modo claro e escuro)

| Token | Uso | Modo Claro | Modo Escuro |
|---|---|---|---|
| `accent.primary` | Ações primárias, Evolution Score positivo | `#0A84FF` (azul iOS) | `#0A84FF` |
| `accent.success` | Progresso positivo, PRs, streaks | `#34C759` | `#30D158` |
| `accent.warning` | Fadiga, deload sugerido, atenção | `#FF9F0A` | `#FF9F0A` |
| `accent.danger` | Erros, exclusões por limitação física | `#FF3B30` | `#FF453A` |
| `background.primary` | Fundo de tela | `#FFFFFF` | `#000000` |
| `background.secondary` | Cards, superfícies elevadas | `#F2F2F7` | `#1C1C1E` |
| `text.primary` | Texto principal | `#000000` | `#FFFFFF` |
| `text.secondary` | Texto de apoio, captions | `#6E6E73` | `#98989D` |

### 3.2 Justificativa: por que seguir a paleta semântica do iOS em vez de uma identidade de marca proprietária total

**Alternativa considerada:** paleta de marca proprietária forte (ex: um verde ou roxo de destaque exclusivo do produto, distinto do sistema).

**Por que a paleta segue as cores semânticas nativas do iOS (com adaptação):** o produto é usado várias vezes por dia, em contextos de baixa atenção (durante o treino, com pressa). Cores que já carregam significado universal no ecossistema iOS (azul = ação, verde = sucesso, vermelho = erro/atenção) reduzem a carga cognitiva de interpretação. Uma paleta de marca distinta teria custo de aprendizado para o usuário sem benefício proporcional — e contradiz o princípio de "cor com significado, não decoração" (§1). Identidade visual proprietária pode (e deve) vir de tipografia, espaçamento e dos componentes específicos do produto (ex: o card de Evolution Score), não da reinvenção da linguagem de cor do sistema.

---

## 4. Modo Claro e Modo Escuro

Paridade funcional total exigida (RNF-09) — nenhuma funcionalidade exclusiva de um modo. Modo escuro não é "modo claro invertido": usa preto verdadeiro (`#000000`) como fundo primário (não cinza-escuro), seguindo a convenção de OLED do iOS, com elevação de superfícies via tons de cinza escuro (`background.secondary`), não via sombras (sombras são pouco perceptíveis em fundo preto).

---

## 5. Componentes-chave

### 5.1 Card de Evolution Score
- Número em estilo Display, cor dinâmica conforme faixa (verde >80, azul 60–80, laranja <60 — nunca vermelho puro, para não gerar ansiedade desproporcional a um índice de bem-estar).
- Seta de tendência (↗ ↘ →) com cor semântica correspondente.
- Linha de texto curta com o componente mais fraco — sempre presente, nunca omitida.

### 5.2 Card de Recomendação da IA
- Ícone/indicador visual de "origem IA" consistente (para o usuário sempre identificar visualmente o que é gerado pelo sistema vs. dado bruto).
- Dois CTAs sempre visíveis: ação primária ("Aplicar"/"Aceitar") e ação secundária de transparência ("Ver por quê").
- Nunca um terceiro CTA de "Ignorar" com peso visual igual aos outros dois — descartar uma recomendação é possível, mas não deve competir visualmente com entendê-la.

### 5.3 Stepper de Carga/Repetição (Sessão de Treino)
- Botões de incremento/decremento grandes (mínimo 44×44pt, padrão de área tocável Apple), considerando uso com mãos potencialmente úmidas/com luvas de treino.
- Valor numérico central editável por toque direto (abre teclado numérico) para ajustes maiores sem múltiplos toques no stepper.

### 5.4 Streak de Hábitos
- Representação visual de sequência (ex: dots ou pequenos blocos preenchidos por dia), nunca apenas um número — reforça percepção visual de consistência ao longo do tempo, alinhado ao princípio RN-601 de nunca apagar histórico de streaks anteriores.

---

## 6. Ícones e Animações

- **Ícones:** biblioteca de ícones web consistente (ex: Lucide ou Heroicons — ambas com estilo geométrico limpo, compatível com a estética inspirada em Apple/Linear/Notion buscada pelo produto), usada de forma consistente em todo o app. SF Symbols era a escolha natural no cenário SwiftUI nativo (ver nota em [06-arquitetura.md](./06-arquitetura.md) §1); no cliente PWA atual, a biblioteca escolhida deve ao menos visualmente se aproximar do peso/traço de SF Symbols para preservar a identidade visual pretendida.
- **Animações:** transições suaves e curtas (200–300ms), nunca decorativas a ponto de atrasar a percepção de resposta do app. Exceção deliberada: a atualização do Evolution Score pode ter uma animação de contagem (número subindo/descendo) de até 600ms — é um dos poucos momentos do produto onde um pequeno "delay perceptual deliberado" reforça a sensação de progresso sendo calculado, similar ao padrão usado em apps de saúde da Apple.

---

## 7. Acessibilidade (RNF-11)

- Suporte completo a Dynamic Type (texto deve re-fluir, nunca truncar com "...", em telas críticas como explicações da IA).
- Contraste mínimo AA (WCAG) em todas as combinações de texto/fundo, validado em ambos os modos.
- Suporte a VoiceOver em todos os elementos interativos, com labels descritivos (ex: stepper de carga anunciado como "Carga, 60 quilos, botão de aumentar" — não apenas "+").
- Nenhuma informação comunicada exclusivamente por cor (ex: tendência do Evolution Score usa seta + cor, nunca cor isolada) — essencial para usuários com daltonismo.
