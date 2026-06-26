-- Hypertrophy OS — Schema inicial
-- Nomenclatura: tabelas e campos em português snake_case (convenção do projeto)

CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    senha_hash  VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE profiles (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id               BIGINT       NOT NULL UNIQUE,
    nome                  VARCHAR(255) NOT NULL,
    data_nascimento       DATE,
    altura_cm             DECIMAL(5, 2),
    objetivo              ENUM('HIPERTROFIA') NOT NULL DEFAULT 'HIPERTROFIA',
    nivel_experiencia     ENUM('INICIANTE','INTERMEDIARIO','AVANCADO'),
    onboarding_completo   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_profiles_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE contextos_treino (
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                  BIGINT       NOT NULL,
    nome                     VARCHAR(255) NOT NULL,
    equipamento_disponivel   JSON,
    ativo                    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_contextos_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE limitacoes_fisicas (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    regiao      ENUM('LOMBAR','JOELHO','OMBRO','COTOVELO','PUNHO','OUTRO') NOT NULL,
    descricao   TEXT,
    ativo       BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_limitacoes_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Catálogo global de exercícios (não específico de usuário)
CREATE TABLE exercicios (
    id                              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome                            VARCHAR(255) NOT NULL,
    grupo_muscular_primario         ENUM('PEITO','DORSAL','OMBRO','BICEPS','TRICEPS','QUADRICEPS',
                                        'POSTERIOR_COXA','GLUTEO','PANTURRILHA','ABDOMEN',
                                        'TRAPEZIO','ANTEBRACO') NOT NULL,
    grupos_musculares_secundarios   JSON,
    equipamento_necessario          ENUM('BARRA','HALTERES','MAQUINA','PESO_CORPORAL','CABO',
                                        'KETTLEBELL','BARRA_FIXA','PARALELAS','BANCO') NOT NULL,
    padrao_movimento                ENUM('EMPURRAR_HORIZONTAL','EMPURRAR_VERTICAL',
                                        'PUXAR_HORIZONTAL','PUXAR_VERTICAL',
                                        'DOMINANTE_QUADRIL','DOMINANTE_JOELHO',
                                        'ISOLAMENTO','CORE') NOT NULL,
    contraindicacoes                JSON,
    created_at                      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE planos_treino (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT       NOT NULL,
    contexto_treino_id  BIGINT       NOT NULL,
    nome                VARCHAR(255) NOT NULL,
    split_tipo          ENUM('FULL_BODY','UPPER_LOWER','PUSH_PULL_LEGS','BRO_SPLIT','ARNOLD_SPLIT') NOT NULL,
    ativo               BOOLEAN      NOT NULL DEFAULT TRUE,
    gerado_por_ia       BOOLEAN      NOT NULL DEFAULT TRUE,
    versao              INT          NOT NULL DEFAULT 1,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_planos_user    FOREIGN KEY (user_id)            REFERENCES users (id),
    CONSTRAINT fk_planos_ctx     FOREIGN KEY (contexto_treino_id) REFERENCES contextos_treino (id)
);

CREATE TABLE plano_treino_exercicios (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    plano_treino_id      BIGINT NOT NULL,
    exercicio_id         BIGINT NOT NULL,
    dia_da_semana        INT    NOT NULL,
    series_alvo          INT    NOT NULL,
    repeticoes_alvo_min  INT    NOT NULL,
    repeticoes_alvo_max  INT    NOT NULL,
    ordem                INT    NOT NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_pte_plano    FOREIGN KEY (plano_treino_id) REFERENCES planos_treino (id),
    CONSTRAINT fk_pte_exercicio FOREIGN KEY (exercicio_id)   REFERENCES exercicios (id)
);

CREATE TABLE sessoes_treino (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT NOT NULL,
    plano_treino_id   BIGINT,
    data_inicio       TIMESTAMP NOT NULL,
    data_fim          TIMESTAMP,
    status            ENUM('EM_ANDAMENTO','FINALIZADA','ABANDONADA') NOT NULL DEFAULT 'EM_ANDAMENTO',
    duracao_segundos  INT,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_sessoes_user  FOREIGN KEY (user_id)         REFERENCES users (id),
    CONSTRAINT fk_sessoes_plano FOREIGN KEY (plano_treino_id) REFERENCES planos_treino (id)
);

CREATE TABLE series_executadas (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    sessao_treino_id      BIGINT         NOT NULL,
    exercicio_id          BIGINT         NOT NULL,
    numero_serie          INT            NOT NULL,
    carga_kg              DECIMAL(6, 2)  NOT NULL,
    repeticoes_realizadas INT            NOT NULL,
    rpe                   DECIMAL(3, 1),
    observacao            TEXT,
    versao_anterior_id    BIGINT,
    created_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_series_sessao         FOREIGN KEY (sessao_treino_id)   REFERENCES sessoes_treino (id),
    CONSTRAINT fk_series_exercicio      FOREIGN KEY (exercicio_id)        REFERENCES exercicios (id),
    CONSTRAINT fk_series_versao_ant     FOREIGN KEY (versao_anterior_id) REFERENCES series_executadas (id)
);

CREATE TABLE registros_peso_medidas (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT        NOT NULL,
    data_hora            TIMESTAMP     NOT NULL,
    peso_kg              DECIMAL(5, 2),
    percentual_gordura   DECIMAL(5, 2),
    medidas              JSON,
    created_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_peso_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE fotos_evolucao (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id               BIGINT     NOT NULL,
    storage_path          TEXT       NOT NULL,
    data_hora             TIMESTAMP  NOT NULL,
    peso_kg_no_momento    DECIMAL(5, 2),
    created_at            TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_fotos_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Catálogo global de alimentos
CREATE TABLE alimentos (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome                 VARCHAR(255)  NOT NULL,
    categoria            ENUM('PROTEINA','CARBOIDRATO','GORDURA','VEGETAL','FRUTA','OUTRO') NOT NULL,
    calorias_por_100g    DECIMAL(7, 2) NOT NULL,
    proteina_por_100g    DECIMAL(6, 2) NOT NULL,
    carboidrato_por_100g DECIMAL(6, 2) NOT NULL,
    gordura_por_100g     DECIMAL(6, 2) NOT NULL,
    fibra_por_100g       DECIMAL(6, 2),
    created_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE estoque_cozinha (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                BIGINT         NOT NULL,
    alimento_id            BIGINT         NOT NULL,
    quantidade_disponivel  DECIMAL(10, 2),
    unidade                ENUM('G','ML','UNIDADE') NOT NULL,
    consumo_medio_diario   DECIMAL(10, 2),
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_estoque_user     FOREIGN KEY (user_id)    REFERENCES users (id),
    CONSTRAINT fk_estoque_alimento FOREIGN KEY (alimento_id) REFERENCES alimentos (id)
);

CREATE TABLE lista_compras (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT        NOT NULL,
    alimento_id         BIGINT        NOT NULL,
    quantidade_sugerida DECIMAL(10, 2),
    origem              ENUM('ACABANDO','RECEITA_SUGERIDA','MANUAL') NOT NULL,
    comprado            BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_lista_user     FOREIGN KEY (user_id)    REFERENCES users (id),
    CONSTRAINT fk_lista_alimento FOREIGN KEY (alimento_id) REFERENCES alimentos (id)
);

CREATE TABLE refeicoes_registradas (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id           BIGINT        NOT NULL,
    data_hora         TIMESTAMP     NOT NULL,
    tipo              ENUM('CAFE_DA_MANHA','ALMOCO','JANTAR','LANCHE','PRE_TREINO','POS_TREINO') NOT NULL,
    origem            ENUM('SUGESTAO_IA','MANUAL') NOT NULL,
    total_calorias    DECIMAL(8, 2),
    total_proteina    DECIMAL(7, 2),
    total_carboidrato DECIMAL(7, 2),
    total_gordura     DECIMAL(7, 2),
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_refeicoes_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE refeicao_itens (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    refeicao_id   BIGINT        NOT NULL,
    alimento_id   BIGINT        NOT NULL,
    quantidade_g  DECIMAL(8, 2) NOT NULL,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_itens_refeicao  FOREIGN KEY (refeicao_id)  REFERENCES refeicoes_registradas (id),
    CONSTRAINT fk_itens_alimento  FOREIGN KEY (alimento_id)  REFERENCES alimentos (id)
);

CREATE TABLE metas_nutricionais (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                BIGINT        NOT NULL,
    data_vigencia_inicio   DATE          NOT NULL,
    calorias_alvo          DECIMAL(8, 2) NOT NULL,
    proteina_alvo_g        DECIMAL(7, 2) NOT NULL,
    carboidrato_alvo_g     DECIMAL(7, 2) NOT NULL,
    gordura_alvo_g         DECIMAL(7, 2) NOT NULL,
    motivo_recalculo       TEXT,
    created_at             TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_metas_nut_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE habitos (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    nome        VARCHAR(255) NOT NULL,
    tipo        ENUM('PRE_DEFINIDO','CUSTOMIZADO') NOT NULL,
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_habitos_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE habito_registros (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    habito_id   BIGINT    NOT NULL,
    data        DATE      NOT NULL,
    concluido   BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_habito_reg_habito FOREIGN KEY (habito_id) REFERENCES habitos (id),
    UNIQUE KEY uq_habito_data (habito_id, data)
);

CREATE TABLE registros_sono (
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id              BIGINT    NOT NULL,
    data                 DATE      NOT NULL,
    horario_dormir       TIMESTAMP NOT NULL,
    horario_acordar      TIMESTAMP NOT NULL,
    duracao_minutos      INT       NOT NULL,
    qualidade_percebida  INT       NOT NULL,
    origem               ENUM('MANUAL','APPLE_HEALTH') NOT NULL DEFAULT 'MANUAL',
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_sono_user FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE KEY uq_sono_user_data (user_id, data)
);

CREATE TABLE metas (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT         NOT NULL,
    tipo        ENUM('PESO_ALVO','FORCA_ALVO','OUTRO') NOT NULL,
    valor_alvo  DECIMAL(10, 2) NOT NULL,
    data_alvo   DATE,
    ativo       BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_metas_user FOREIGN KEY (user_id) REFERENCES users (id)
);

-- RF-1001/1002: toda recomendação da IA persiste snapshot + explicação obrigatória
CREATE TABLE recomendacoes_ia (
    id                       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                  BIGINT     NOT NULL,
    tipo                     ENUM('PROGRESSAO_CARGA','DELOAD','TROCA_EXERCICIO','AJUSTE_VOLUME',
                                  'SUGESTAO_REFEICAO','ALERTA_EVOLUTION_SCORE') NOT NULL,
    dados_entrada_snapshot   JSON       NOT NULL,
    recomendacao_estruturada JSON       NOT NULL,
    explicacao_texto         TEXT       NOT NULL,
    status_resposta          ENUM('PENDENTE','ACEITA','AJUSTADA','REJEITADA') NOT NULL DEFAULT 'PENDENTE',
    ajuste_detalhes          JSON,
    data_hora                TIMESTAMP  NOT NULL,
    created_at               TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_recom_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE evolution_score_historico (
    id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                     BIGINT        NOT NULL,
    data                        DATE          NOT NULL,
    score_total                 DECIMAL(5, 2) NOT NULL,
    componente_treino           DECIMAL(5, 2) NOT NULL,
    componente_proteina         DECIMAL(5, 2) NOT NULL,
    componente_sono             DECIMAL(5, 2) NOT NULL,
    componente_hidratacao       DECIMAL(5, 2) NOT NULL,
    componente_recuperacao      DECIMAL(5, 2) NOT NULL,
    componente_consistencia     DECIMAL(5, 2) NOT NULL,
    componente_evolucao_corporal DECIMAL(5, 2) NOT NULL,
    componente_mais_fraco       VARCHAR(100),
    created_at                  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_evo_user FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE KEY uq_evo_user_data (user_id, data)
);

CREATE TABLE registros_hidratacao (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT        NOT NULL,
    data_hora     TIMESTAMP     NOT NULL,
    quantidade_ml DECIMAL(8, 2) NOT NULL,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_hidrat_user FOREIGN KEY (user_id) REFERENCES users (id)
);
