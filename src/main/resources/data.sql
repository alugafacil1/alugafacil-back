INSERT INTO users (user_id, name, email, password_hash, user_type) 
VALUES (
    'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a',
    'Usuario Teste', 
    'teste@alugafacil.com', 
    '123',
    'OWNER'
);

-- 1. Apartamento Heliópolis (Caro, Mobiliado)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000001', 'Apto Luxo Heliópolis', 'Vista privilegiada, perto de tudo.', 250000, 'APARTMENT', 'ACTIVE', 5, 2, 2, true, false, true, true, 'Garanhuns', 'Heliópolis', 'Av. Rui Barbosa', '100', '55295-000', -8.8825, -36.4710, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 2. Casa Magano (Barata, Pet Friendly)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000002', 'Casa Simples Magano', 'Quintal amplo para cães.', 60000, 'HOUSE', 'ACTIVE', 4, 2, 1, false, true, false, true, 'Garanhuns', 'Magano', 'Rua da Serra', '20', '55294-100', -8.8850, -36.5000, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 3. Kitnet Centro (Para estudantes)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000003', 'Kitnet Econômica Centro', 'Ideal para estudantes, perto da AESGA.', 45000, 'APARTMENT', 'ACTIVE', 2, 1, 1, false, false, false, true, 'Garanhuns', 'Centro', 'Rua Dantas Barreto', '55', '55290-000', -8.8907, -36.4958, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 4. Casa Grande Boa Vista (Família)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000004', 'Casa 3 Quartos Boa Vista', 'Garagem para 2 carros.', 120000, 'HOUSE', 'ACTIVE', 7, 3, 2, true, true, true, true, 'Garanhuns', 'Boa Vista', 'Rua José Bonifácio', '300', '55292-000', -8.8800, -36.4800, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 5. Flat Aloísio Pinto (Novo)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000005', 'Flat Moderno', 'Primeira locação.', 90000, 'APARTMENT', 'ACTIVE', 3, 1, 1, true, false, true, false, 'Garanhuns', 'Aloísio Pinto', 'Rua Projetada', '12', '55296-000', -8.8950, -36.4900, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 6. Casa Cohab 1 (Popular)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000006', 'Casa Cohab 1', 'Perto do mercado.', 50000, 'HOUSE', 'ACTIVE', 4, 2, 1, false, true, false, true, 'Garanhuns', 'Cohab 1', 'Rua A', '44', '55298-000', -8.9000, -36.5100, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 7. Apartamento Severiano Moraes Filho
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000007', 'Apto Térreo', 'Bem ventilado.', 75000, 'APARTMENT', 'ACTIVE', 4, 2, 1, false, false, true, true, 'Garanhuns', 'Severiano Moraes', 'Rua B', '10', '55297-000', -8.8880, -36.4850, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 8. Mansão Heliópolis (Alto Padrão)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000008', 'Casa de Alto Padrão', 'Piscina e área gourmet.', 450000, 'HOUSE', 'ACTIVE', 10, 4, 4, true, true, true, false, 'Garanhuns', 'Heliópolis', 'Rua das Flores', '99', '55295-100', -8.8830, -36.4720, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 9. Loja/Comercial Centro (Exemplo de tipo diferente se suportado, senão HOUSE)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000009', 'Ponto Comercial/Casa', 'Pode ser usado como escritório.', 110000, 'HOUSE', 'ACTIVE', 3, 1, 1, false, false, false, true, 'Garanhuns', 'Centro', 'Rua 7 de Setembro', '200', '55290-100', -8.8910, -36.4960, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 10. Casa Brasília (Pausada - Não deve aparecer se filtrar por ACTIVE)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000010', 'Casa em Reforma', 'Indisponível no momento.', 70000, 'HOUSE', 'PAUSED', 5, 2, 1, false, true, true, true, 'Garanhuns', 'Brasília', 'Rua Brasília', '500', '55293-000', -8.8700, -36.4900, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 11. Apartamento São José
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000011', 'Apto Familiar', 'Condomínio fechado.', 85000, 'APARTMENT', 'ACTIVE', 5, 3, 2, false, true, true, true, 'Garanhuns', 'São José', 'Rua São José', '88', '55291-000', -8.8920, -36.4980, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 12. Casa Indiano (Barata)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000012', 'Casa Pequena', 'Ótimo custo benefício.', 40000, 'HOUSE', 'ACTIVE', 3, 1, 1, false, true, false, true, 'Garanhuns', 'Indiano', 'Rua Índio', '12', '55299-000', -8.8750, -36.5050, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 13. Apto Heliópolis (Semi-mobiliado)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000013', 'Apto com Cozinha Planejada', 'Fica armários.', 160000, 'APARTMENT', 'ACTIVE', 5, 2, 2, true, false, true, true, 'Garanhuns', 'Heliópolis', 'Av. Caruaru', '200', '55295-200', -8.8840, -36.4700, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 14. Casa Francisco Figueira (Cohab 2)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000014', 'Casa Reformada', 'Piso novo.', 65000, 'HOUSE', 'ACTIVE', 4, 2, 1, false, true, true, true, 'Garanhuns', 'Cohab 2', 'Rua Principal', '45', '55298-500', -8.9050, -36.5150, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- 15. Apto Centro (Luxo)
INSERT INTO property (id, title, description, price_in_cents, type, status, number_of_rooms, number_of_bedrooms, number_of_bathrooms, furnished, pet_friendly, garage, is_owner, city, neighborhood, street, number, postal_code, latitude, longitude, created_at, updated_at, user_id) VALUES 
('a0000000-0000-0000-0000-000000000015', 'Cobertura no Centro', 'Vista panorâmica.', 300000, 'APARTMENT', 'ACTIVE', 8, 3, 3, true, true, true, false, 'Garanhuns', 'Centro', 'Av. Santo Antônio', '1000', '55290-000', -8.8900, -36.4950, NOW(), NOW(), 'd2f4d3c0-5a1b-4e3f-8c2a-1b2c3d4e5f6a');

-- ==================================================================================
-- FOTOS (ADICIONANDO PELO MENOS UMA PARA CADA)
-- ==================================================================================

INSERT INTO property_photo_urls (property_id, photo_urls) VALUES 
('a0000000-0000-0000-0000-000000000001', 'https://placehold.co/600x400/png?text=Apto+Heliopolis'),
('a0000000-0000-0000-0000-000000000002', 'https://placehold.co/600x400/orange?text=Casa+Magano'),
('a0000000-0000-0000-0000-000000000003', 'https://placehold.co/600x400/blue?text=Kitnet+Centro'),
('a0000000-0000-0000-0000-000000000004', 'https://placehold.co/600x400/green?text=Casa+BoaVista'),
('a0000000-0000-0000-0000-000000000005', 'https://placehold.co/600x400/red?text=Flat'),
('a0000000-0000-0000-0000-000000000006', 'https://placehold.co/600x400/purple?text=Casa+Cohab'),
('a0000000-0000-0000-0000-000000000007', 'https://placehold.co/600x400/yellow?text=Apto+Severiano'),
('a0000000-0000-0000-0000-000000000008', 'https://placehold.co/600x400/black?text=Mansao'),
('a0000000-0000-0000-0000-000000000009', 'https://placehold.co/600x400/grey?text=Comercial'),
('a0000000-0000-0000-0000-000000000010', 'https://placehold.co/600x400/white?text=Pausado'),
('a0000000-0000-0000-0000-000000000011', 'https://placehold.co/600x400/cyan?text=Apto+SaoJose'),
('a0000000-0000-0000-0000-000000000012', 'https://placehold.co/600x400/brown?text=Casa+Indiano'),
('a0000000-0000-0000-0000-000000000013', 'https://placehold.co/600x400/pink?text=Apto+Semi'),
('a0000000-0000-0000-0000-000000000014', 'https://placehold.co/600x400/lime?text=Casa+Cohab2'),
('a0000000-0000-0000-0000-000000000015', 'https://placehold.co/600x400/olive?text=Cobertura');