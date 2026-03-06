
ALTER TABLE pedidos DROP COLUMN IF EXISTS ingresso_id;

CREATE TABLE pedido_ingressos (
    pedido_id BIGINT NOT NULL,
    ingresso_codigo VARCHAR(100) NOT NULL,
    
    CONSTRAINT fk_pedido_ingressos_pedido 
        FOREIGN KEY (pedido_id) 
        REFERENCES pedidos (id) 
        ON DELETE CASCADE
);

CREATE INDEX idx_pedido_ingressos_pedido_id ON pedido_ingressos(pedido_id);