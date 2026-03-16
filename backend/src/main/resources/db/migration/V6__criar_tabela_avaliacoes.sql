ALTER TABLE ingressos DROP CONSTRAINT ck_ingressos_status;

ALTER TABLE ingressos
    ADD CONSTRAINT ck_ingressos_status
        CHECK (status IN ('CONFIRMADO', 'UTILIZADO', 'CANCELADO', 'AVALIADO'));

ALTER TABLE filmes
    ADD COLUMN media_avaliacao FLOAT DEFAULT 0.0,
    ADD COLUMN qtd_avaliacoes  INT           NOT NULL DEFAULT 0;

ALTER TABLE filmes
    ADD CONSTRAINT ck_filmes_media_avaliacao CHECK (media_avaliacao BETWEEN 0 AND 5),
    ADD CONSTRAINT ck_filmes_qtd_avaliacoes  CHECK (qtd_avaliacoes >= 0);
