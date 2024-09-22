-- Inserta permisos
INSERT INTO permissions (name) VALUES
                                   ('CREATE'),
                                   ('READ'),
                                   ('DELETE'),
                                   ('SUSCRIBE');

-- Inserta roles
INSERT INTO roles (role_name) VALUES
                                  ('USUARIO'),
                                  ('ADMIN');

-- Asigna permisos a roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE role_name = 'USUARIO') AS role_id, id AS permission_id FROM permissions WHERE name IN ('READ', 'SUSCRIBE');

INSERT INTO role_permissions (role_id, permission_id)
SELECT (SELECT id FROM roles WHERE role_name = 'ADMIN') AS role_id, id AS permission_id FROM permissions WHERE name IN ('READ', 'SUSCRIBE', 'CREATE', 'DELETE');

-- Inserta usuarios
INSERT INTO users (username, password, is_enabled, account_no_expired, account_no_locked, credential_no_expired) VALUES
                                                                                                                     ('master', '$2a$10$cTxbkyr.IN.hn/m5iraiCeIo4XogBvctfbEFcWDdgFx/35HX5ynAC', true, true, true, true),
                                                                                                                     ('usuario', '$2a$10$mi4iWTtebYuKT.jApPXKcuBeYI3yiftz3hIFskaJ8VXMbsT0Wn7R2', true, true, true, true);

-- Asigna roles a usuarios
INSERT INTO user_roles (user_id, role_id)
SELECT (SELECT id FROM users WHERE username = 'master') AS user_id, (SELECT id FROM roles WHERE role_name = 'ADMIN') AS role_id;

INSERT INTO user_roles (user_id, role_id)
SELECT (SELECT id FROM users WHERE username = 'usuario') AS user_id, (SELECT id FROM roles WHERE role_name = 'USUARIO') AS role_id;




INSERT INTO Categories (nombre_categoria) VALUES ('Figuras de Acción');
INSERT INTO Categories (nombre_categoria) VALUES ('Cómics y Mangas');
INSERT INTO Categories (nombre_categoria) VALUES ('Monedas y Billetes');
INSERT INTO Categories (nombre_categoria) VALUES ('Autógrafos');
INSERT INTO Categories (nombre_categoria) VALUES ('Juguetes Vintage');
INSERT INTO Categories (nombre_categoria) VALUES ('Cartas Coleccionables');
INSERT INTO Categories (nombre_categoria) VALUES ('Arte');
INSERT INTO Categories (nombre_categoria) VALUES ('Antiguedades');
INSERT INTO Categories (nombre_categoria) VALUES ('Música y Vinilos');
INSERT INTO Categories (nombre_categoria) VALUES ('Libros Raros');
INSERT INTO Categories (nombre_categoria) VALUES ('Relojes y Joyería');
INSERT INTO Categories (nombre_categoria) VALUES ('Fotografía');


-- Insertar 25 coleccionables adicionales, asegurando que user_creador_id = 1

INSERT INTO Products(precio, stock, id_Categoria, user_creador_id, descripcion, detalles, imagen,destacado) VALUES
                                                                                                      (25.99, 5, 8, 1, 'Espada de Hierro', 'Espada de un guerrero antiguo, encontrada en una cueva perdida.', 'imagen1.url', false),
                                                                                                      (50.00, 2, 3, 1, 'Moneda de Oro', 'Moneda de oro del siglo XVIII, muy rara y valiosa.', 'imagen2.url', false),
                                                                                                      (15.75, 8, 1, 1, 'Figura de Dragón', 'Figura de dragón hecha a mano por un artista local.', 'imagen3.url', false),
                                                                                                      (100.00, 1, 8, 1, 'Mapa del Tesoro', 'Mapa del tesoro que muestra la ubicación de una isla perdida.', 'imagen4.url', false),
                                                                                                      (12.50, 20, 11, 1, 'Anillo Misterioso', 'Anillo con inscripciones en un idioma desconocido.', 'imagen5.url', false),
                                                                                                      (85.99, 3, 1, 1, 'Casco de Caballero', 'Casco de un caballero medieval, en buen estado.', 'imagen6.url', false),
                                                                                                      (40.00, 7, 1, 1, 'Espada Samurai', 'Espada samurai auténtica del Japón feudal.', 'imagen7.url', false),
                                                                                                      (55.55, 6, 10, 1, 'Libro de Magia', 'Libro antiguo con hechizos y rituales de magia negra.', 'imagen8.url', false),
                                                                                                      (75.75, 4, 7, 1, 'Cáliz de Plata', 'Cáliz usado en ceremonias religiosas hace siglos.', 'imagen9.url', false),
                                                                                                      (22.22, 15, 3, 1, 'Medalla de Bronce', 'Medalla de bronce de los Juegos Olímpicos de 1936.', 'imagen10.url', false),
                                                                                                      (110.00, 1, 7, 1, 'Pintura Renacentista', 'Pintura renacentista original del siglo XV.', 'imagen11.url', false),
                                                                                                      (13.30, 12, 12, 1, 'Estatuilla de Buda', 'Estatuilla de Buda hecha de jade.', 'imagen12.url', false),
                                                                                                      (45.50, 10, 1, 1, 'Escudo Vikingo', 'Escudo vikingo auténtico con marcas de batalla.', 'imagen13.url', false),
                                                                                                      (18.75, 9, 11, 1, 'Collar de Perlas', 'Collar de perlas blancas cultivadas.', 'imagen14.url', false),
                                                                                                      (95.00, 3, 8, 1, 'Relicario Medieval', 'Relicario medieval con joyas incrustadas.', 'imagen15.url', false),
                                                                                                      (20.00, 25, 4, 1, 'Pluma de Avestruz', 'Pluma de avestruz usada por escritores del siglo XVII.', 'imagen16.url', false),
                                                                                                      (65.00, 5, 11, 1, 'Reloj de Bolsillo', 'Reloj de bolsillo de plata con inscripción.', 'imagen17.url', false),
                                                                                                      (29.99, 12, 5, 1, 'Lámpara de Aceite', 'Lámpara de aceite de la era victoriana.', 'imagen18.url', false),
                                                                                                      (120.00, 2, 9, 1, 'Instrumento Musical Antiguo', 'Lira griega antigua en perfecto estado.', 'imagen19.url', false),
                                                                                                      (9.99, 30, 8, 1, 'Jarrón Chino', 'Jarrón chino con detalles en oro.', 'imagen20.url', false),
                                                                                                      (37.75, 11, 6, 1, 'Máscara de Carnaval', 'Máscara de carnaval veneciana hecha a mano.', 'imagen21.url', false),
                                                                                                      (56.80, 7, 11, 1, 'Anillo de Claddagh', 'Anillo de Claddagh irlandés con corazón de rubí.', 'imagen22.url', false),
                                                                                                      (27.49, 10, 4, 1, 'Billetera de Cuero', 'Billetera de cuero grabada a mano.', 'imagen23.url', false),
                                                                                                      (89.99, 4, 12, 1, 'Telescopio Náutico', 'Telescopio náutico antiguo de latón.', 'imagen24.url', false),
                                                                                                      (14.99, 13, 8, 1, 'Llave Misteriosa', 'Llave forjada con extrañas inscripciones.', 'imagen25.url', false);