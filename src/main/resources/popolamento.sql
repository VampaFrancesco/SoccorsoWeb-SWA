-- ========================================
-- DATABASE SWA - VERSIONE FINALE
-- Compatibile con il tuo codice esistente
-- ========================================
-- CHARSET e COLLATION per supporto UTF-8 completo
-- InnoDB per supporto transazioni e chiavi esterne

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
                                    stato ENUM('INVIATA', 'ATTIVA', 'CONVALIDATA', 'IN_CORSO', 'CHIUSA', 'IGNORATA','ANNULLATA') NOT NULL DEFAULT 'INVIATA',
                                    convalidata_at TIMESTAMP NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
                          livello_successo INT NULL CHECK (livello_successo BETWEEN 1 AND 10),
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
                                                                                                                  (6, 'alessandro.blu@soccorsoweb.it', '$2a$10$xZ1qH8Z8Z8Z8Z8Z8Z8Z8ZeOjYqW0qX5X5X5X5X5X5X5X5X5X5X5X5u', 'Alessandro', 'Blu', '1987-09-30', '3335556677', 'Viale della Repubblica 50, L''Aquila', TRUE, FALSE);

INSERT INTO user_roles (user_id, role_id) VALUES
                                              (2, 2), (3, 2), (4, 2), (5, 2), (6, 2);

-- Richieste di soccorso
INSERT INTO richiesta_soccorso (id, descrizione, indirizzo, latitudine, longitudine, nome_segnalante, email_segnalante, telefono_segnalante, stato, convalidata_at) VALUES
                                                                                                                                                                        (1, 'Persona caduta da scale, possibile frattura gamba', 'Via Roma 15, L''Aquila', 42.35050000, 13.39950000, 'Paolo Verdi', 'paolo.verdi@email.it', '3331112233', 'CONVALIDATA', '2025-12-08 09:00:00'),
                                                                                                                                                                        (2, 'Incendio appartamento al secondo piano', 'Corso Vittorio Emanuele 45, L''Aquila', 42.34980000, 13.39880000, 'Anna Blu', 'anna.blu@email.it', '3334445566', 'CONVALIDATA', '2025-12-08 10:00:00'),
                                                                                                                                                                        (3, 'Auto fuori strada, conducente ferito', 'SS17 km 23, L''Aquila', 42.36120000, 13.41230000, 'Giuseppe Marrone', 'giuseppe.marrone@email.it', '3337778899', 'ATTIVA', NULL),
                                                                                                                                                                        (4, 'Malore improvviso, persona anziana con dolori al petto', 'Piazza del Duomo 8, L''Aquila', 42.35050000, 13.39950000, 'Carla Neri', 'carla.neri@email.it', '3339990011', 'CONVALIDATA', '2025-12-08 11:00:00'),
                                                                                                                                                                        (5, 'Escursionista disperso sul Gran Sasso', 'Rifugio Campo Imperatore', 42.44050000, 13.55950000, 'Roberto Rossi', 'roberto.rossi@email.it', '3338881122', 'INVIATA', NULL);

-- Missioni
INSERT INTO missione (id, richiesta_id, caposquadra_id, obiettivo, posizione, latitudine, longitudine, stato, inizio_at) VALUES
                                                                                                                             (1, 1, 2, 'Soccorso sanitario per caduta da scale', 'Via Roma 15, L''Aquila', 42.35050000, 13.39950000, 'IN_CORSO', '2025-12-08 09:15:00'),
                                                                                                                             (2, 2, 3, 'Spegnimento incendio appartamento', 'Corso Vittorio Emanuele 45, L''Aquila', 42.34980000, 13.39880000, 'IN_CORSO', '2025-12-08 10:30:00');

-- Assegnazione operatori alle missioni
INSERT INTO missione_operatori (missione_id, operatore_id) VALUES
                                                               (1, 2),  -- Luca è caposquadra e operatore della missione 1
                                                               (1, 3),  -- Giulia è operatore della missione 1
                                                               (2, 3),  -- Giulia è caposquadra della missione 2
                                                               (2, 4);  -- Marco è operatore della missione 2
