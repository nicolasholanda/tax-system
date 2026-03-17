CREATE TABLE tax_rates (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    state_id BIGINT NOT NULL REFERENCES states(id),
    year INTEGER NOT NULL,
    rate NUMERIC(5, 2) NOT NULL,
    UNIQUE (product_id, state_id, year)
);