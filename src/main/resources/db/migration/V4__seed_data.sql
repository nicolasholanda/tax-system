INSERT INTO products (name, category) VALUES
    ('Laptop', 'Electronics'),
    ('Rice', 'Food'),
    ('Aspirin', 'Health');

INSERT INTO states (name, abbreviation) VALUES
    ('São Paulo', 'SP'),
    ('Rio de Janeiro', 'RJ'),
    ('Minas Gerais', 'MG');

INSERT INTO tax_rates (product_id, state_id, tax_year, rate) VALUES
    (1, 1, 2026, 18.50),
    (1, 2, 2026, 17.00),
    (1, 3, 2026, 18.00),
    (2, 1, 2026, 7.00),
    (2, 2, 2026, 7.00),
    (2, 3, 2026, 6.00),
    (3, 1, 2026, 0.00),
    (3, 2, 2026, 0.00),
    (3, 3, 2026, 0.00);
