package com.hypertrophy.hypertrophy_api.entity;

import com.hypertrophy.hypertrophy_api.entity.enums.StatusResposta;
import com.hypertrophy.hypertrophy_api.entity.enums.TipoRecomendacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "recomendacoes_ia")
public class RecomendacaoIA extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoRecomendacao tipo;

    // Snapshot dos dados que geraram a recomendação — RF-1001/RNF-05
    @Column(name = "dados_entrada_snapshot", nullable = false, columnDefinition = "JSON")
    private String dadosEntradaSnapshot;

    @Column(name = "recomendacao_estruturada", nullable = false, columnDefinition = "JSON")
    private String recomendacaoEstruturada;

    // RF-1002: obrigatório — nunca exibir recomendação sem texto explicativo
    @Column(name = "explicacao_texto", nullable = false, columnDefinition = "TEXT")
    private String explicacaoTexto;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_resposta", nullable = false, length = 20)
    private StatusResposta statusResposta = StatusResposta.PENDENTE;

    @Column(name = "ajuste_detalhes", columnDefinition = "JSON")
    private String ajusteDetalhes;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;
}