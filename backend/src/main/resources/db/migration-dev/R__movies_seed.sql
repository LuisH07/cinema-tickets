-- Limpa os dados antigos e reinicia as contagens de ID para evitar duplicidade
TRUNCATE TABLE sessoes, salas, filmes RESTART IDENTITY CASCADE;

INSERT INTO filmes
(titulo, sinopse, duracao, status, classificacao, poster, backdrop)
VALUES
    (
        'Um Sonho de Liberdade',
        'Em 1946, Andy Dufresne... controla o mercado negro da instituição.',
        142,
        'EM_CARTAZ',
        '16',
        '/images/um-sonho-de-liberdade-278.jpg',
        '/images/um-sonho-de-liberdade-278-backdrop.jpg'
    );

INSERT INTO filmes
(titulo, sinopse, duracao, status, classificacao, poster, backdrop)
VALUES
    (
        'O Poderoso Chefão',
        'Em 1945, Don Corleone é o chefe de uma mafiosa família italiana...',
        175,
        'EM_CARTAZ',
        '14',
        '/images/o-poderoso-chefao-238.jpg',
        '/images/o-poderoso-chefao-238-backdrop.jpg'
    );

INSERT INTO filmes
(titulo, sinopse, duracao, status, classificacao, poster, backdrop)
VALUES
    (
        'O Poderoso Chefão: Parte II',
        'Após a máfia matar sua família, o jovem Vito foge da Sicília...',
        202,
        'EM_CARTAZ',
        '14',
        '/images/o-poderoso-chefao-parte-ii-240.jpg',
        '/images/o-poderoso-chefao-parte-ii-240-backdrop.jpg'
    );

INSERT INTO filmes
(titulo, sinopse, duracao, status, classificacao, poster, backdrop)
VALUES
    (
        'A Lista de Schindler',
        'O alemão Oskar Schindler viu na mão de obra judia uma solução barata...',
        195,
        'EM_CARTAZ',
        '14',
        '/images/a-lista-de-schindler-424.jpg',
        '/images/a-lista-de-schindler-424-backdrop.jpg'
    );

INSERT INTO filmes
(titulo, sinopse, duracao, status, classificacao, poster, backdrop)
VALUES
    (
        '12 Homens e uma Sentença',
        'Um jovem porto-riquenho é acusado de ter matado o próprio pai...',
        96,
        'EM_CARTAZ',
        'L',
        '/images/12-homens-e-uma-sentenca-389.jpg',
        '/images/12-homens-e-uma-sentenca-389-backdrop.jpg'
    );

INSERT INTO filmes
(titulo, sinopse, duracao, status, classificacao, poster, backdrop)
VALUES
    (
        'A Viagem de Chihiro',
        'Chihiro e seus pais estão se mudando para uma cidade diferente...',
        126,
        'EM_CARTAZ',
        'L',
        '/images/a-viagem-de-chihiro-129.jpg',
        '/images/a-viagem-de-chihiro-129-backdrop.jpg'
    );

INSERT INTO filmes
(titulo, sinopse, duracao, status, classificacao, poster, backdrop)
VALUES
    (
        'Batman: O Cavaleiro das Trevas',
        'Após dois anos desde o surgimento do Batman...',
        152,
        'EM_CARTAZ',
        '12',
        '/images/batman-o-cavaleiro-das-trevas-155.jpg',
        '/images/batman-o-cavaleiro-das-trevas-155-backdrop.jpg'
    );

INSERT INTO filmes
(titulo, sinopse, duracao, status, classificacao, poster, backdrop)
VALUES
    (
        'À Espera de um Milagre',
        'Milagres acontecem em lugares inesperados...',
        189,
        'EM_CARTAZ',
        '14',
        '/images/a-espera-de-um-milagre-497.jpg',
        '/images/a-espera-de-um-milagre-497-backdrop.jpg'
    );

INSERT INTO filme_generos (filme_id, genero) VALUES
     (1, 'Drama'), (1, 'Crime'),
     (2, 'Drama'), (2, 'Crime'),
     (3, 'Drama'), (3, 'Crime'),
     (4, 'Drama'), (4, 'História'), (4, 'Guerra'),
     (5, 'Drama'),
     (6, 'Animação'), (6, 'Família'), (6, 'Fantasia'),
     (7, 'Ação'), (7, 'Crime'), (7, 'Thriller'),
     (8, 'Fantasia'), (8, 'Drama'), (8, 'Crime');

INSERT INTO filme_diretores (filme_id, diretor) VALUES
    (1, 'Frank Darabont'),
    (2, 'Francis Ford Coppola'),
    (3, 'Francis Ford Coppola'),
    (4, 'Steven Spielberg'),
    (5, 'Sidney Lumet'),
    (6, 'Hayao Miyazaki'),
    (7, 'Christopher Nolan'),
    (8, 'Frank Darabont');

INSERT INTO filme_elenco (filme_id, ator) VALUES
      (1, 'Tim Robbins'), (1, 'Morgan Freeman'), (1, 'Bob Gunton'), (1, 'William Sadler'), (1, 'Clancy Brown'), (1, 'Gil Bellows'),
      (2, 'Marlon Brando'), (2, 'Al Pacino'), (2, 'James Caan'), (2, 'Robert Duvall'), (2, 'Richard S. Castellano'), (2, 'Diane Keaton'),
      (3, 'Al Pacino'), (3, 'Robert Duvall'), (3, 'Diane Keaton'), (3, 'Robert De Niro'), (3, 'John Cazale'), (3, 'Talia Shire'),
      (4, 'Liam Neeson'), (4, 'Ben Kingsley'), (4, 'Ralph Fiennes'), (4, 'Caroline Goodall'), (4, 'Jonathan Sagall'), (4, 'Embeth Davidtz'),
      (5, 'Martin Balsam'), (5, 'John Fiedler'), (5, 'Lee J. Cobb'), (5, 'E.G. Marshall'), (5, 'Jack Klugman'), (5, 'Edward Binns'),
      (6, 'Rumi Hiiragi'), (6, 'Miyu Irino'), (6, 'Mari Natsuki'), (6, 'Takashi Naitô'), (6, 'Yasuko Sawaguchi'), (6, '我修院達也'),
      (7, 'Christian Bale'), (7, 'Heath Ledger'), (7, 'Aaron Eckhart'), (7, 'Michael Caine'), (7, 'Maggie Gyllenhaal'), (7, 'Gary Oldman'),
      (8, 'Tom Hanks'), (8, 'David Morse'), (8, 'Bonnie Hunt'), (8, 'Michael Clarke Duncan'), (8, 'James Cromwell'), (8, 'Michael Jeter');

INSERT INTO salas (nome, capacidade) VALUES ('Sala IMAX', 120);
INSERT INTO salas (nome, capacidade) VALUES ('Sala 02 - VIP', 45);
INSERT INTO salas (nome, capacidade) VALUES ('Sala 03 - 3D', 80);
INSERT INTO salas (nome, capacidade) VALUES ('Sala 04 - Standard', 100);

-- Um Sonho de Liberdade (Filme 1) na Sala IMAX (Sala 1)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-19 14:00:00', 'Legendado', 1, 1);
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-19 18:00:00', 'Legendado', 1, 1);

-- O Poderoso Chefão (Filme 2) na Sala VIP (Sala 2)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-19 15:30:00', 'Legendado', 2, 2);
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-19 19:30:00', 'Legendado', 2, 2);

-- A Viagem de Chihiro (Filme 6) na Sala 3D (Sala 3)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-19 13:00:00', 'Dublado 3D', 6, 3);
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-19 16:00:00', 'Dublado 3D', 6, 3);

-- Batman: O Cavaleiro das Trevas (Filme 7) na Sala Standard (Sala 4)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-19 20:00:00', 'Dublado', 7, 4);

-- A Lista de Schindler (Filme 4) na Sala IMAX (Sala 1)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-20 15:00:00', 'Legendado', 4, 1);
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-20 19:00:00', 'Legendado', 4, 1);

-- 12 Homens e uma Sentença (Filme 5) na Sala VIP (Sala 2)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-20 14:00:00', 'Legendado', 5, 2);
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-20 16:30:00', 'Legendado', 5, 2);

-- À Espera de um Milagre (Filme 8) na Sala 3D (Sala 3) - (Exibição normal na sala 3D)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-20 18:30:00', 'Dublado', 8, 3);
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-20 22:00:00', 'Legendado', 8, 3);

-- Maratona O Poderoso Chefão na Sala VIP (Sala 2)
-- Parte I (Filme 2)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-21 14:00:00', 'Legendado', 2, 2);
-- Parte II (Filme 3)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-21 18:00:00', 'Legendado', 3, 2);

-- Batman (Filme 7) na Sala IMAX (Sala 1)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-21 16:00:00', 'Legendado', 7, 1);
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-21 20:30:00', 'Legendado', 7, 1);

-- Chihiro (Filme 6) na Sala Standard (Sala 4)
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-21 10:00:00', 'Dublado', 6, 4);
INSERT INTO sessoes (inicio, tipo, filme_id, sala_id) VALUES ('2026-02-21 13:30:00', 'Dublado', 6, 4);
