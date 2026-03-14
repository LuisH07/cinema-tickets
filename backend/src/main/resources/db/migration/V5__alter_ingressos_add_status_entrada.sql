ALTER TABLE ingressos
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADO',
    ADD COLUMN data_hora_entrada TIMESTAMP NULL;

ALTER TABLE ingressos
    ADD CONSTRAINT ck_ingressos_status
        CHECK (status IN ('CONFIRMADO', 'UTILIZADO', 'CANCELADO'));

CREATE INDEX idx_ingressos_status ON ingressos(status);
