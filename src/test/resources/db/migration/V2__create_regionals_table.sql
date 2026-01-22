CREATE TABLE regionals (
    id BIGSERIAL PRIMARY KEY,
    original_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para performance na sincronização
CREATE INDEX idx_regionals_original_id ON regionals(original_id);
CREATE INDEX idx_regionals_active ON regionals(active);