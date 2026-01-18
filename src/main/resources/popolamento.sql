-- ========================================
-- DATABASE SWA - VERSIONE FINALE
-- ========================================

DROP DATABASE IF EXISTS soccorsodb;
CREATE DATABASE soccorsodb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE soccorsodb;

-- ========================================
-- CORE: Autenticazione e Utenti
-- ========================================

-- Tabella Role
CREATE TABLE role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      INDEX idx_role_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella User (Admin + Operatori)
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      nome VARCHAR(100) NOT NULL,
                      cognome VARCHAR(100) NOT NULL,
                      data_nascita DATE NULL,
                      telefono VARCHAR(20) NULL,
                      indirizzo VARCHAR(255) NULL,
                      attivo BOOLEAN NOT NULL DEFAULT TRUE,
                      disponibile BOOLEAN NOT NULL DEFAULT TRUE,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      INDEX idx_email (email),
                      INDEX idx_attivo (attivo),
                      INDEX idx_disponibile (disponibile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella User_Roles (Many-to-Many)
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
                            INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- RICHIESTE SOCCORSO
-- ========================================

CREATE TABLE richiesta_soccorso (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    descrizione TEXT NOT NULL,
                                    indirizzo VARCHAR(255) NOT NULL,
                                    latitudine DECIMAL(10, 8) NULL,
                                    longitudine DECIMAL(11, 8) NULL,
                                    nome_segnalante VARCHAR(100) NOT NULL,
                                    email_segnalante VARCHAR(255) NOT NULL,
                                    telefono_segnalante VARCHAR(20) NULL,
                                    foto_url VARCHAR(255) NULL,
                                    ip_origine VARCHAR(45) NULL,
                                    token_convalida VARCHAR(255) NULL UNIQUE,
                                    stato ENUM('INVIATA', 'ATTIVA', 'CONVALIDATA', 'IN_CORSO', 'CHIUSA', 'IGNORATA','ANNULLATA', 'TUTTE') NOT NULL DEFAULT 'INVIATA',
                                    convalidata_at TIMESTAMP NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    livello_successo INT NULL CHECK (livello_successo BETWEEN 1 AND 10),
                                    INDEX idx_stato (stato),
                                    INDEX idx_email_segnalante (email_segnalante),
                                    INDEX idx_token (token_convalida)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- MISSIONI
-- ========================================

CREATE TABLE missione (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          richiesta_id BIGINT NOT NULL UNIQUE,
                          caposquadra_id BIGINT NOT NULL,
                          obiettivo TEXT NOT NULL,
                          posizione VARCHAR(255) NULL,
                          latitudine DECIMAL(10, 8) NULL,
                          longitudine DECIMAL(11, 8) NULL,
                          stato ENUM('IN_CORSO', 'CHIUSA', 'FALLITA', 'ANNULLATA') NOT NULL DEFAULT 'IN_CORSO',
                          inizio_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          fine_at TIMESTAMP NULL,
                          commenti_finali TEXT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (richiesta_id) REFERENCES richiesta_soccorso(id) ON DELETE CASCADE,
                          FOREIGN KEY (caposquadra_id) REFERENCES user(id) ON DELETE RESTRICT,
                          INDEX idx_stato (stato),
                          INDEX idx_caposquadra (caposquadra_id),
                          INDEX idx_richiesta (richiesta_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Missione_Operatori
CREATE TABLE missione_operatori (
                                    missione_id BIGINT NOT NULL,
                                    operatore_id BIGINT NOT NULL,
                                    PRIMARY KEY (missione_id, operatore_id),
                                    FOREIGN KEY (missione_id) REFERENCES missione(id) ON DELETE CASCADE,
                                    FOREIGN KEY (operatore_id) REFERENCES user(id) ON DELETE CASCADE,
                                    INDEX idx_operatore (operatore_id),
                                    INDEX idx_missione (missione_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================================
-- POPOLAMENTO DATI INIZIALI
-- ========================================

-- Ruoli
INSERT INTO role (id, name) VALUES
                                (1, 'ADMIN'),
                                (2, 'OPERATORE');

-- Admin (password: admin123 con BCrypt)
INSERT INTO user (id, email, password, nome, cognome, attivo, disponibile) VALUES
    (1, 'admin@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Mario', 'Rossi', TRUE, TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

-- Operatori (password: operatore123 con BCrypt)
INSERT INTO user (id, email, password, nome, cognome, data_nascita, telefono, indirizzo, attivo, disponibile) VALUES
                                                                                                                  (2, 'luca.bianchi@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Luca', 'Bianchi', '1985-03-15', '3331234567', 'Via Roma 10, L''Aquila', TRUE, TRUE),
                                                                                                                  (3, 'giulia.verdi@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Giulia', 'Verdi', '1990-07-22', '3337654321', 'Corso Vittorio 25, L''Aquila', TRUE, TRUE),
                                                                                                                  (4, 'marco.neri@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Marco', 'Neri', '1988-11-08', '3339876543', 'Piazza Duomo 3, L''Aquila', TRUE, TRUE),
                                                                                                                  (5, 'sara.gialli@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Sara', 'Gialli', '1992-02-14', '3332223344', 'Via XX Settembre 8, L''Aquila', TRUE, TRUE),
                                                                                                                  (6, 'alessandro.blu@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Alessandro', 'Blu', '1987-09-30', '3335556677', 'Viale della Repubblica 50, L''Aquila', TRUE, FALSE),
                                                                                                                  (7, 'francesca.russo@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Francesca', 'Russo', '1991-05-20', '3338889900', 'Via Garibaldi 12, L''Aquila', TRUE, TRUE),
                                                                                                                  (8, 'davide.ferrari@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Davide', 'Ferrari', '1986-12-03', '3331110022', 'Via Mazzini 7, L''Aquila', TRUE, TRUE),
                                                                                                                  (9, 'chiara.conti@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Chiara', 'Conti', '1993-08-17', '3334445588', 'Piazza Battaglione 5, L''Aquila', TRUE, TRUE),
                                                                                                                  (10, 'simone.marino@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Simone', 'Marino', '1989-01-25', '3336667788', 'Via Dante 33, L''Aquila', TRUE, TRUE),
                                                                                                                  (11, 'elena.greco@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Elena', 'Greco', '1994-04-11', '3339998877', 'Corso Umberto 18, L''Aquila', TRUE, FALSE),
                                                                                                                  (12, 'roberto.fontana@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Roberto', 'Fontana', '1984-06-28', '3332224455', 'Via Napoli 22, L''Aquila', TRUE, TRUE),
                                                                                                                  (13, 'valentina.moretti@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Valentina', 'Moretti', '1992-10-09', '3337773322', 'Via Milano 40, L''Aquila', TRUE, TRUE),
                                                                                                                  (14, 'andrea.lombardi@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Andrea', 'Lombardi', '1987-03-14', '3335554433', 'Piazza San Marco 2, L''Aquila', TRUE, TRUE),
                                                                                                                  (15, 'martina.ricci@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Martina', 'Ricci', '1995-11-30', '3338886655', 'Via Torino 15, L''Aquila', TRUE, TRUE);

INSERT INTO user_roles (user_id, role_id) VALUES
                                              (2, 2), (3, 2), (4, 2), (5, 2), (6, 2), (7, 2), (8, 2), (9, 2), (10, 2), (11, 2), (12, 2), (13, 2), (14, 2), (15, 2);

-- Richieste di soccorso
INSERT INTO richiesta_soccorso (id, descrizione, indirizzo, latitudine, longitudine, nome_segnalante, email_segnalante, telefono_segnalante, stato, convalidata_at, livello_successo) VALUES
                                                                                                                                                                                             (1, 'Persona caduta da scale, possibile frattura gamba', 'Via Roma 15, L''Aquila', 42.35050000, 13.39950000, 'Paolo Verdi', 'paolo.verdi@email.it', '3331112233', 'CHIUSA', '2025-12-08 09:00:00', 8),
                                                                                                                                                                                             (2, 'Incendio appartamento al secondo piano', 'Corso Vittorio Emanuele 45, L''Aquila', 42.34980000, 13.39880000, 'Anna Blu', 'anna.blu@email.it', '3334445566', 'CHIUSA', '2025-12-08 10:00:00', 9),
                                                                                                                                                                                             (3, 'Auto fuori strada, conducente ferito', 'SS17 km 23, L''Aquila', 42.36120000, 13.41230000, 'Giuseppe Marrone', 'giuseppe.marrone@email.it', '3337778899', 'IN_CORSO', '2025-12-10 14:30:00', NULL),
                                                                                                                                                                                             (4, 'Malore improvviso, persona anziana con dolori al petto', 'Piazza del Duomo 8, L''Aquila', 42.35050000, 13.39950000, 'Carla Neri', 'carla.neri@email.it', '3339990011', 'CHIUSA', '2025-12-08 11:00:00', 7),
                                                                                                                                                                                             (5, 'Escursionista disperso sul Gran Sasso', 'Rifugio Campo Imperatore', 42.44050000, 13.55950000, 'Roberto Rossi', 'roberto.rossi@email.it', '3338881122', 'CONVALIDATA', '2025-12-15 08:00:00', NULL),
                                                                                                                                                                                             (6, 'Incidente stradale con tre veicoli coinvolti', 'Via Nazionale km 15, L''Aquila', 42.35500000, 13.40200000, 'Maria Bianchi', 'maria.bianchi@email.it', '3331239876', 'CHIUSA', '2025-12-09 16:45:00', 6),
                                                                                                                                                                                             (7, 'Perdita gas in condominio, evacuazione necessaria', 'Via Cavour 32, L''Aquila', 42.35120000, 13.39870000, 'Luigi Verdi', 'luigi.verdi@email.it', '3337654987', 'CHIUSA', '2025-12-10 07:20:00', 3),
                                                                                                                                                                                             (8, 'Bambino caduto da bicicletta, trauma cranico', 'Parco del Castello, L''Aquila', 42.35300000, 13.39600000, 'Angela Rossi', 'angela.rossi@email.it', '3339871234', 'CHIUSA', '2025-12-11 15:10:00', 2),
                                                                                                                                                                                             (9, 'Allagamento garage sotterraneo', 'Via Benedetto Croce 18, L''Aquila', 42.34890000, 13.39750000, 'Franco Neri', 'franco.neri@email.it', '3335558899', 'CHIUSA', '2025-12-12 09:30:00', 8),
                                                                                                                                                                                             (10, 'Persone bloccate in ascensore', 'Viale Gran Sasso 55, L''Aquila', 42.35600000, 13.40100000, 'Silvia Gialli', 'silvia.gialli@email.it', '3332228877', 'CHIUSA', '2025-12-13 12:00:00', 9),
                                                                                                                                                                                             (11, 'Crollo parziale muro di recinzione', 'Via San Bernardino 8, L''Aquila', 42.35180000, 13.39920000, 'Antonio Blu', 'antonio.blu@email.it', '3336669988', 'CHIUSA', '2025-12-14 10:15:00', 4),
                                                                                                                                                                                             (12, 'Animale selvatico ferito sulla carreggiata', 'SS80 km 8, L''Aquila', 42.36500000, 13.42000000, 'Paola Russo', 'paola.russo@email.it', '3338887766', 'IGNORATA', NULL, NULL),
                                                                                                                                                                                             (13, 'Fuga di acqua da tubazione principale', 'Corso Federico II 40, L''Aquila', 42.35080000, 13.39880000, 'Massimo Ferrari', 'massimo.ferrari@email.it', '3331115544', 'CHIUSA', '2025-12-16 08:45:00', 7),
                                                                                                                                                                                             (14, 'Principio di incendio in cantina', 'Via Sallustio 25, L''Aquila', 42.34950000, 13.39700000, 'Laura Conti', 'laura.conti@email.it', '3334443322', 'CHIUSA', '2025-12-17 19:20:00', 8),
                                                                                                                                                                                             (15, 'Blackout elettrico in zona residenziale', 'Via Castello 12, L''Aquila', 42.35280000, 13.39580000, 'Giorgio Marino', 'giorgio.marino@email.it', '3337776655', 'CHIUSA', '2025-12-18 21:30:00', 6),
                                                                                                                                                                                             (16, 'Caduta albero su strada pubblica', 'Viale della Croce Rossa 30, L''Aquila', 42.35450000, 13.40050000, 'Stefania Greco', 'stefania.greco@email.it', '3339992211', 'CHIUSA', '2025-12-19 06:00:00', 9),
                                                                                                                                                                                             (17, 'Intossicazione alimentare gruppo turistico', 'Hotel Centrale, Piazza Palazzo 1, L''Aquila', 42.35010000, 13.39930000, 'Michele Fontana', 'michele.fontana@email.it', '3332225588', 'CHIUSA', '2025-12-20 20:15:00', 5),
                                                                                                                                                                                             (18, 'Ritrovamento pacco sospetto in stazione', 'Stazione FS L''Aquila, Piazza della Stazione', 42.35700000, 13.40300000, 'Daniela Moretti', 'daniela.moretti@email.it', '3335557799', 'ANNULLATA', NULL, NULL),
                                                                                                                                                                                             (19, 'Sciatore fuori pista, possibile valanga', 'Piste Campo Felice, L''Aquila', 42.21000000, 13.43000000, 'Claudio Lombardi', 'claudio.lombardi@email.it', '3338884433', 'IN_CORSO', '2025-12-25 14:00:00', NULL),
                                                                                                                                                                                             (20, 'Scontro tra autobus e auto privata', 'Viale Corrado IV 88, L''Aquila', 42.35350000, 13.39850000, 'Federica Ricci', 'federica.ricci@email.it', '3331116688', 'CHIUSA', '2025-12-22 17:40:00', 1),
                                                                                                                                                                                             (21, 'Emergenza sanitaria in scuola elementare', 'Via Giordano Bruno 5, L''Aquila', 42.35220000, 13.39680000, 'Alessandra Galli', 'alessandra.galli@email.it', '3334449922', 'CHIUSA', '2025-12-23 11:25:00', 8),
                                                                                                                                                                                             (22, 'Caduta calcinacci da facciata palazzo', 'Corso Principe Umberto 14, L''Aquila', 42.35150000, 13.39910000, 'Emanuele Costa', 'emanuele.costa@email.it', '3337778844', 'CHIUSA', '2025-12-24 09:50:00', 7),
                                                                                                                                                                                             (23, 'Persona scomparsa, anziano con Alzheimer', 'Via Fortebraccio 22, L''Aquila', 42.35400000, 13.39770000, 'Barbara Ferretti', 'barbara.ferretti@email.it', '3339995511', 'CHIUSA', '2025-12-26 18:00:00', 10),
                                                                                                                                                                                             (24, 'Incidente con sostanze chimiche in laboratorio', 'Università L''Aquila, Via Vetoio', 42.36800000, 13.35000000, 'Riccardo Mancini', 'riccardo.mancini@email.it', '3332223377', 'CHIUSA', '2025-12-27 10:30:00', 4),
                                                                                                                                                                                             (25, 'Rapina in corso in gioielleria', 'Corso Vittorio Emanuele 78, L''Aquila', 42.34990000, 13.39890000, 'Serena Barbieri', 'serena.barbieri@email.it', '3335556622', 'ATTIVA', NULL, NULL);

-- Missioni
INSERT INTO missione (id, richiesta_id, caposquadra_id, obiettivo, posizione, latitudine, longitudine, stato, inizio_at, fine_at, commenti_finali) VALUES
                                                                                                                                                        (1, 1, 2, 'Soccorso sanitario per caduta da scale', 'Via Roma 15, L''Aquila', 42.35050000, 13.39950000, 'CHIUSA', '2025-12-08 09:15:00', '2025-12-08 10:30:00', 'Intervento riuscito. Paziente trasportato in ospedale con frattura composta.'),
                                                                                                                                                        (2, 2, 3, 'Spegnimento incendio appartamento', 'Corso Vittorio Emanuele 45, L''Aquila', 42.34980000, 13.39880000, 'CHIUSA', '2025-12-08 10:30:00', '2025-12-08 13:00:00', 'Incendio domato con successo. Nessun ferito, danni materiali limitati.'),
                                                                                                                                                        (3, 3, 4, 'Soccorso stradale con feriti', 'SS17 km 23, L''Aquila', 42.36120000, 13.41230000, 'IN_CORSO', '2025-12-10 14:45:00', NULL, NULL),
                                                                                                                                                        (4, 4, 5, 'Emergenza cardiologica', 'Piazza del Duomo 8, L''Aquila', 42.35050000, 13.39950000, 'CHIUSA', '2025-12-08 11:15:00', '2025-12-08 12:00:00', 'Paziente stabilizzato e trasportato in terapia intensiva.'),
                                                                                                                                                        (5, 5, 2, 'Ricerca escursionista disperso', 'Rifugio Campo Imperatore', 42.44050000, 13.55950000, 'IN_CORSO', '2025-12-15 08:30:00', NULL, NULL),
                                                                                                                                                        (6, 6, 7, 'Gestione incidente stradale multiplo', 'Via Nazionale km 15, L''Aquila', 42.35500000, 13.40200000, 'CHIUSA', '2025-12-09 17:00:00', '2025-12-09 19:30:00', 'Tre veicoli coinvolti, due feriti lievi trasportati in ospedale.'),
                                                                                                                                                        (7, 7, 8, 'Evacuazione per fuga gas', 'Via Cavour 32, L''Aquila', 42.35120000, 13.39870000, 'CHIUSA', '2025-12-10 07:35:00', '2025-12-10 11:00:00', 'Evacuazione completata, perdita riparata. Situazione risolta ma con ritardi dovuti a difficoltà tecniche.'),
                                                                                                                                                        (8, 8, 9, 'Soccorso pediatrico trauma cranico', 'Parco del Castello, L''Aquila', 42.35300000, 13.39600000, 'CHIUSA', '2025-12-11 15:25:00', '2025-12-11 16:45:00', 'Intervento critico, bambino arrivato in ospedale fuori tempo massimo per complicazioni stradali.'),
                                                                                                                                                        (9, 9, 10, 'Svuotamento garage allagato', 'Via Benedetto Croce 18, L''Aquila', 42.34890000, 13.39750000, 'CHIUSA', '2025-12-12 09:45:00', '2025-12-12 14:00:00', 'Garage svuotato, danni limitati grazie all''intervento rapido.'),
                                                                                                                                                        (10, 10, 12, 'Liberazione persone bloccate', 'Viale Gran Sasso 55, L''Aquila', 42.35600000, 13.40100000, 'CHIUSA', '2025-12-13 12:15:00', '2025-12-13 13:00:00', 'Cinque persone liberate, nessun ferito. Intervento efficiente.'),
                                                                                                                                                        (11, 11, 13, 'Messa in sicurezza muro crollato', 'Via San Bernardino 8, L''Aquila', 42.35180000, 13.39920000, 'CHIUSA', '2025-12-14 10:30:00', '2025-12-14 15:00:00', 'Zona messa in sicurezza, ma necessari ulteriori lavori di consolidamento.'),
                                                                                                                                                        (12, 13, 14, 'Riparazione perdita acqua', 'Corso Federico II 40, L''Aquila', 42.35080000, 13.39880000, 'CHIUSA', '2025-12-16 09:00:00', '2025-12-16 12:30:00', 'Tubazione riparata, servizio ripristinato.'),
                                                                                                                                                        (13, 14, 15, 'Spegnimento incendio cantina', 'Via Sallustio 25, L''Aquila', 42.34950000, 13.39700000, 'CHIUSA', '2025-12-17 19:35:00', '2025-12-17 21:00:00', 'Focolaio spento prima che si propagasse.'),
                                                                                                                                                        (14, 15, 2, 'Ripristino energia elettrica', 'Via Castello 12, L''Aquila', 42.35280000, 13.39580000, 'CHIUSA', '2025-12-18 21:45:00', '2025-12-18 23:30:00', 'Servizio ripristinato per l''intera zona.'),
                                                                                                                                                        (15, 16, 3, 'Rimozione albero caduto', 'Viale della Croce Rossa 30, L''Aquila', 42.35450000, 13.40050000, 'CHIUSA', '2025-12-19 06:20:00', '2025-12-19 09:00:00', 'Strada liberata e messa in sicurezza rapidamente.'),
                                                                                                                                                        (16, 17, 4, 'Assistenza intossicazione alimentare', 'Hotel Centrale, Piazza Palazzo 1, L''Aquila', 42.35010000, 13.39930000, 'CHIUSA', '2025-12-20 20:30:00', '2025-12-21 01:00:00', 'Otto turisti soccorsi, tre ricoverati per osservazione. Intervento complesso.'),
                                                                                                                                                        (17, 19, 5, 'Ricerca sciatore disperso', 'Piste Campo Felice, L''Aquila', 42.21000000, 13.43000000, 'IN_CORSO', '2025-12-25 14:20:00', NULL, NULL),
                                                                                                                                                        (18, 20, 7, 'Gestione incidente autobus', 'Viale Corrado IV 88, L''Aquila', 42.35350000, 13.39850000, 'CHIUSA', '2025-12-22 17:55:00', '2025-12-22 21:30:00', 'Intervento gravemente ritardato per problemi di coordinamento. Cinque feriti gravi.'),
                                                                                                                                                        (19, 21, 8, 'Emergenza sanitaria scolastica', 'Via Giordano Bruno 5, L''Aquila', 42.35220000, 13.39680000, 'CHIUSA', '2025-12-23 11:40:00', '2025-12-23 12:30:00', 'Studente con crisi epilettica assistito tempestivamente.'),
                                                                                                                                                        (20, 22, 9, 'Messa in sicurezza facciata', 'Corso Principe Umberto 14, L''Aquila', 42.35150000, 13.39910000, 'CHIUSA', '2025-12-24 10:05:00', '2025-12-24 13:00:00', 'Zona transennata e calcinacci rimossi.'),
                                                                                                                                                        (21, 23, 10, 'Ricerca persona scomparsa', 'Via Fortebraccio 22, L''Aquila', 42.35400000, 13.39770000, 'CHIUSA', '2025-12-26 18:15:00', '2025-12-26 22:45:00', 'Anziano ritrovato in ottima salute. Intervento perfetto con coordinamento tra squadre.'),
                                                                                                                                                        (22, 24, 12, 'Bonifica sostanze chimiche', 'Università L''Aquila, Via Vetoio', 42.36800000, 13.35000000, 'CHIUSA', '2025-12-27 10:45:00', '2025-12-27 14:30:00', 'Laboratorio messo in sicurezza ma con difficoltà iniziali nell''identificazione sostanze.');

-- Assegnazione operatori alle missioni
INSERT INTO missione_operatori (missione_id, operatore_id) VALUES
                                                               -- Missione 1: Caduta da scale (Caposquadra: Luca)
                                                               (1, 2),  -- Luca (caposquadra)
                                                               (1, 3),  -- Giulia
                                                               (1, 5),  -- Sara
                                                               -- Missione 2: Incendio (Caposquadra: Giulia)
                                                               (2, 3),  -- Giulia (caposquadra)
                                                               (2, 4),  -- Marco
                                                               (2, 7),  -- Francesca
                                                               (2, 8),  -- Davide
                                                               -- Missione 3: Auto fuori strada (Caposquadra: Marco)
                                                               (3, 4),  -- Marco (caposquadra)
                                                               (3, 2),  -- Luca
                                                               (3, 9),  -- Chiara
                                                               -- Missione 4: Malore (Caposquadra: Sara)
                                                               (4, 5),  -- Sara (caposquadra)
                                                               (4, 10), -- Simone
                                                               -- Missione 5: Escursionista disperso (Caposquadra: Luca)
                                                               (5, 2),  -- Luca (caposquadra)
                                                               (5, 4),  -- Marco
                                                               (5, 12), -- Roberto
                                                               (5, 13), -- Valentina
                                                               (5, 14), -- Andrea
                                                               -- Missione 6: Incidente multiplo (Caposquadra: Francesca)
                                                               (6, 7),  -- Francesca (caposquadra)
                                                               (6, 8),  -- Davide
                                                               (6, 9),  -- Chiara
                                                               (6, 10), -- Simone
                                                               -- Missione 7: Fuga gas (Caposquadra: Davide)
                                                               (7, 8),  -- Davide (caposquadra)
                                                               (7, 3),  -- Giulia
                                                               (7, 12), -- Roberto
                                                               -- Missione 8: Trauma cranico bambino (Caposquadra: Chiara)
                                                               (8, 9),  -- Chiara (caposquadra)
                                                               (8, 5),  -- Sara
                                                               -- Missione 9: Allagamento (Caposquadra: Simone)
                                                               (9, 10), -- Simone (caposquadra)
                                                               (9, 2),  -- Luca
                                                               (9, 7),  -- Francesca
                                                               -- Missione 10: Ascensore bloccato (Caposquadra: Roberto)
                                                               (10, 12), -- Roberto (caposquadra)
                                                               (10, 13), -- Valentina
                                                               (10, 14), -- Andrea
                                                               -- Missione 11: Crollo muro (Caposquadra: Valentina)
                                                               (11, 13), -- Valentina (caposquadra)
                                                               (11, 4),  -- Marco
                                                               (11, 8),  -- Davide
                                                               (11, 15), -- Martina
                                                               -- Missione 12: Perdita acqua (Caposquadra: Andrea)
                                                               (12, 14), -- Andrea (caposquadra)
                                                               (12, 3),  -- Giulia
                                                               (12, 10), -- Simone
                                                               -- Missione 13: Incendio cantina (Caposquadra: Martina)
                                                               (13, 15), -- Martina (caposquadra)
                                                               (13, 7),  -- Francesca
                                                               (13, 9),  -- Chiara
                                                               -- Missione 14: Blackout (Caposquadra: Luca)
                                                               (14, 2),  -- Luca (caposquadra)
                                                               (14, 12), -- Roberto
                                                               (14, 14), -- Andrea
                                                               -- Missione 15: Albero caduto (Caposquadra: Giulia)
                                                               (15, 3),  -- Giulia (caposquadra)
                                                               (15, 4),  -- Marco
                                                               (15, 10), -- Simone
                                                               (15, 13), -- Valentina
                                                               -- Missione 16: Intossicazione alimentare (Caposquadra: Marco)
                                                               (16, 4),  -- Marco (caposquadra)
                                                               (16, 2),  -- Luca
                                                               (16, 5),  -- Sara
                                                               (16, 9),  -- Chiara
                                                               (16, 15), -- Martina
                                                               -- Missione 17: Sciatore disperso (Caposquadra: Sara)
                                                               (17, 5),  -- Sara (caposquadra)
                                                               (17, 7),  -- Francesca
                                                               (17, 8),  -- Davide
                                                               (17, 12), -- Roberto
                                                               (17, 14), -- Andrea
                                                               -- Missione 18: Incidente autobus (Caposquadra: Francesca)
                                                               (18, 7),  -- Francesca (caposquadra)
                                                               (18, 3),  -- Giulia
                                                               (18, 10), -- Simone
                                                               (18, 13), -- Valentina
                                                               -- Missione 19: Emergenza scolastica (Caposquadra: Davide)
                                                               (19, 8),  -- Davide (caposquadra)
                                                               (19, 9),  -- Chiara
                                                               -- Missione 20: Calcinacci (Caposquadra: Chiara)
                                                               (20, 9),  -- Chiara (caposquadra)
                                                               (20, 4),  -- Marco
                                                               (20, 14), -- Andrea
                                                               -- Missione 21: Ricerca anziano (Caposquadra: Simone)
                                                               (21, 10), -- Simone (caposquadra)
                                                               (21, 2),  -- Luca
                                                               (21, 7),  -- Francesca
                                                               (21, 12), -- Roberto
                                                               (21, 15), -- Martina
                                                               -- Missione 22: Sostanze chimiche (Caposquadra: Roberto)
                                                               (22, 12), -- Roberto (caposquadra)
                                                               (22, 3),  -- Giulia
                                                               (22, 5),  -- Sara
                                                               (22, 13); -- Valentina
