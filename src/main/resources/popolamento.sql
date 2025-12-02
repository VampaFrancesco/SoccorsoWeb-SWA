-- ==============================================================================
-- Script Database SoccorsoWeb - MariaDB
-- ==============================================================================

DROP DATABASE IF EXISTS soccorsodb;
CREATE DATABASE soccorsodb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE soccorsodb;

-- ==============================================================================
-- CREAZIONE TABELLE
-- ==============================================================================

-- Tabella Role
CREATE TABLE role (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      INDEX idx_role_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella User
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
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      INDEX idx_email (email),
                      INDEX idx_attivo (attivo)
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

-- Tabella Patente
CREATE TABLE patente (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         tipo VARCHAR(50) NOT NULL UNIQUE,
                         descrizione TEXT NULL,
                         INDEX idx_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella User_Patenti (Many-to-Many)
CREATE TABLE user_patenti (
                              user_id BIGINT NOT NULL,
                              patente_id BIGINT NOT NULL,
                              conseguita_il DATE NULL,
                              PRIMARY KEY (user_id, patente_id),
                              FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                              FOREIGN KEY (patente_id) REFERENCES patente(id) ON DELETE CASCADE,
                              INDEX idx_patente_id (patente_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Abilita
CREATE TABLE abilita (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL UNIQUE,
                         descrizione TEXT NULL,
                         INDEX idx_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella User_Abilita (Many-to-Many)
CREATE TABLE user_abilita (
                              user_id BIGINT NOT NULL,
                              abilita_id BIGINT NOT NULL,
                              livello VARCHAR(50) NULL,
                              PRIMARY KEY (user_id, abilita_id),
                              FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                              FOREIGN KEY (abilita_id) REFERENCES abilita(id) ON DELETE CASCADE,
                              INDEX idx_abilita_id (abilita_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Richiesta_Soccorso
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
                                    stato ENUM('INVIATA', 'ATTIVA', 'IN_CORSO', 'CHIUSA', 'IGNORATA') NOT NULL DEFAULT 'INVIATA',
                                    convalidata_at TIMESTAMP NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    INDEX idx_stato (stato),
                                    INDEX idx_email_segnalante (email_segnalante),
                                    INDEX idx_token (token_convalida)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Mezzo
CREATE TABLE mezzo (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       nome VARCHAR(100) NOT NULL,
                       descrizione TEXT NULL,
                       tipo VARCHAR(50) NULL,
                       targa VARCHAR(20) NULL,
                       disponibile BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       INDEX idx_disponibile (disponibile),
                       INDEX idx_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Materiale
CREATE TABLE materiale (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(100) NOT NULL,
                           descrizione TEXT NULL,
                           tipo VARCHAR(50) NULL,
                           quantita INT NOT NULL DEFAULT 0,
                           disponibile BOOLEAN NOT NULL DEFAULT TRUE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           INDEX idx_disponibile (disponibile),
                           INDEX idx_tipo (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Missione
CREATE TABLE missione (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          richiesta_id BIGINT NOT NULL UNIQUE,
                          obiettivo TEXT NOT NULL,
                          posizione VARCHAR(255) NULL,
                          latitudine DECIMAL(10, 8) NULL,
                          longitudine DECIMAL(11, 8) NULL,
                          caposquadra_id BIGINT NOT NULL,
                          stato ENUM('IN_CORSO', 'CHIUSA', 'FALLITA') NOT NULL DEFAULT 'IN_CORSO',
                          inizio_at TIMESTAMP NULL,
                          fine_at TIMESTAMP NULL,
                          livello_successo INT NULL CHECK (livello_successo BETWEEN 1 AND 5),
                          commenti_finali TEXT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (richiesta_id) REFERENCES richiesta_soccorso(id) ON DELETE CASCADE,
                          FOREIGN KEY (caposquadra_id) REFERENCES user(id) ON DELETE RESTRICT,
                          INDEX idx_stato (stato),
                          INDEX idx_caposquadra (caposquadra_id),
                          INDEX idx_richiesta (richiesta_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Missione_Operatori (Many-to-Many)
CREATE TABLE missione_operatori (
                                    missione_id BIGINT NOT NULL,
                                    operatore_id BIGINT NOT NULL,
                                    notificato_at TIMESTAMP NULL,
                                    assegnato_at TIMESTAMP NULL,
                                    PRIMARY KEY (missione_id, operatore_id),
                                    FOREIGN KEY (missione_id) REFERENCES missione(id) ON DELETE CASCADE,
                                    FOREIGN KEY (operatore_id) REFERENCES user(id) ON DELETE CASCADE,
                                    INDEX idx_operatore (operatore_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Missione_Mezzi (Many-to-Many)
CREATE TABLE missione_mezzi (
                                missione_id BIGINT NOT NULL,
                                mezzo_id BIGINT NOT NULL,
                                assegnato_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                PRIMARY KEY (missione_id, mezzo_id),
                                FOREIGN KEY (missione_id) REFERENCES missione(id) ON DELETE CASCADE,
                                FOREIGN KEY (mezzo_id) REFERENCES mezzo(id) ON DELETE CASCADE,
                                INDEX idx_mezzo (mezzo_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Missione_Materiali (Many-to-Many)
CREATE TABLE missione_materiali (
                                    missione_id BIGINT NOT NULL,
                                    materiale_id BIGINT NOT NULL,
                                    quantita_usata INT NOT NULL DEFAULT 1,
                                    assegnato_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (missione_id, materiale_id),
                                    FOREIGN KEY (missione_id) REFERENCES missione(id) ON DELETE CASCADE,
                                    FOREIGN KEY (materiale_id) REFERENCES materiale(id) ON DELETE CASCADE,
                                    INDEX idx_materiale (materiale_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabella Aggiornamento_Missione
CREATE TABLE aggiornamento_missione (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        missione_id BIGINT NOT NULL,
                                        admin_id BIGINT NOT NULL,
                                        descrizione TEXT NOT NULL,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (missione_id) REFERENCES missione(id) ON DELETE CASCADE,
                                        FOREIGN KEY (admin_id) REFERENCES user(id) ON DELETE RESTRICT,
                                        INDEX idx_missione (missione_id),
                                        INDEX idx_admin (admin_id),
                                        INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==============================================================================
-- POPOLAMENTO DATABASE
-- ==============================================================================

-- 1. RUOLI
INSERT INTO role (id, name) VALUES
                                (1, 'ADMIN'),
                                (2, 'OPERATORE');

-- 2. PATENTI
INSERT INTO patente (id, tipo, descrizione) VALUES
                                                (1, 'A', 'Patente motocicli'),
                                                (2, 'B', 'Patente autovetture'),
                                                (3, 'C', 'Patente autocarri'),
                                                (4, 'D', 'Patente autobus'),
                                                (5, 'NAUTICA', 'Patente nautica entro 12 miglia');

-- 3. ABILITÀ
INSERT INTO abilita (id, nome, descrizione) VALUES
                                                (1, 'Infermiere', 'Competenze infermieristiche di base e avanzate'),
                                                (2, 'Elettricista', 'Installazione e riparazione impianti elettrici'),
                                                (3, 'Sommozzatore', 'Immersioni subacquee fino a 40m'),
                                                (4, 'Soccorritore Alpino', 'Operazioni di soccorso in montagna'),
                                                (5, 'Vigile del Fuoco', 'Addestramento antincendio completo'),
                                                (6, 'Medico', 'Laurea in medicina e chirurgia');

-- 4. UTENTI
-- Password: "Password123!"
-- Hash BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO
INSERT INTO user (id, email, password, nome, cognome, data_nascita, telefono, indirizzo, attivo) VALUES
                                                                                                     (1, 'admin@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Mario', 'Rossi', '1980-05-15', '3331234567', 'Via Roma 1, L\'Aquila', TRUE),
                                                                                                     (2, 'operatore1@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Luca', 'Bianchi', '1985-03-20', '3339876543', 'Via Garibaldi 10, L\'Aquila', TRUE),
                                                                                                     (3, 'operatore2@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Giulia', 'Verdi', '1990-07-12', '3335551234', 'Corso Vittorio 5, L\'Aquila', TRUE),
                                                                                                     (4, 'operatore3@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Marco', 'Neri', '1988-11-30', '3337778899', 'Piazza Duomo 3, L\'Aquila', TRUE),
                                                                                                     (5, 'operatore4@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Sara', 'Gialli', '1992-02-14', '3332223344', 'Via XX Settembre 8, L\'Aquila', TRUE),
                                                                                                     (6, 'operatore5@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Alessandro', 'Blu', '1987-09-25', '3334445566', 'Viale Gran Sasso 12, L\'Aquila', TRUE);

-- 5. ASSEGNAZIONE RUOLI
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),  -- Mario Rossi -> ADMIN
                                              (2, 2),  -- Luca Bianchi -> OPERATORE
                                              (3, 2),  -- Giulia Verdi -> OPERATORE
                                              (4, 2),  -- Marco Neri -> OPERATORE
                                              (5, 2),  -- Sara Gialli -> OPERATORE
                                              (6, 2);  -- Alessandro Blu -> OPERATORE

-- 6. ASSEGNAZIONE PATENTI
INSERT INTO user_patenti (user_id, patente_id, conseguita_il) VALUES
                                                                  (2, 2, '2003-06-15'), (2, 3, '2010-03-20'),
                                                                  (3, 2, '2008-08-10'), (3, 5, '2015-05-12'),
                                                                  (4, 2, '2006-04-18'), (4, 1, '2005-09-22'),
                                                                  (5, 2, '2010-11-05'), (5, 4, '2018-06-30'),
                                                                  (6, 2, '2005-07-14'), (6, 3, '2012-02-28');

-- 7. ASSEGNAZIONE ABILITÀ
INSERT INTO user_abilita (user_id, abilita_id, livello) VALUES
                                                            (2, 5, 'Avanzato'), (2, 2, 'Base'),
                                                            (3, 1, 'Avanzato'), (3, 6, 'Esperto'),
                                                            (4, 4, 'Esperto'), (4, 5, 'Base'),
                                                            (5, 1, 'Intermedio'), (5, 3, 'Base'),
                                                            (6, 2, 'Esperto'), (6, 5, 'Avanzato');

-- 8. MEZZI
INSERT INTO mezzo (id, nome, descrizione, tipo, targa, disponibile) VALUES
                                                                        (1, 'Ambulanza A1', 'Ambulanza completamente attrezzata', 'Ambulanza', 'AQ123AB', TRUE),
                                                                        (2, 'Ambulanza A2', 'Ambulanza di supporto', 'Ambulanza', 'AQ456CD', TRUE),
                                                                        (3, 'Autopompa P1', 'Autopompa con serbatoio 5000L', 'Autopompa', 'AQ789EF', TRUE),
                                                                        (4, 'Fuoristrada F1', 'Fuoristrada 4x4 per terreni difficili', 'Fuoristrada', 'AQ321GH', TRUE),
                                                                        (5, 'Elicottero E1', 'Elicottero da soccorso', 'Elicottero', 'I-SOCC', TRUE),
                                                                        (6, 'Auto Pattuglia AP1', 'Auto di pattuglia standard', 'Auto', 'AQ654IJ', TRUE);

-- 9. MATERIALI
INSERT INTO materiale (id, nome, descrizione, tipo, quantita, disponibile) VALUES
                                                                               (1, 'Kit Medico Avanzato', 'Kit completo per emergenze mediche', 'Medico', 5, TRUE),
                                                                               (2, 'Defibrillatore DAE', 'Defibrillatore automatico esterno', 'Medico', 3, TRUE),
                                                                               (3, 'Estintore 6kg', 'Estintore a polvere 6kg', 'Antincendio', 10, TRUE),
                                                                               (4, 'Scala Telescopica 10m', 'Scala telescopica professionale', 'Attrezzatura', 2, TRUE),
                                                                               (5, 'Set Soccorso Alpino', 'Corde, moschettoni, imbracature', 'Alpinismo', 4, TRUE),
                                                                               (6, 'Barella Spinale', 'Barella con supporto spinale', 'Medico', 6, TRUE),
                                                                               (7, 'Generatore Elettrico', 'Generatore portatile 3kW', 'Attrezzatura', 2, TRUE);

-- 10. RICHIESTE DI SOCCORSO
INSERT INTO richiesta_soccorso (id, descrizione, indirizzo, latitudine, longitudine, nome_segnalante, email_segnalante, telefono_segnalante, ip_origine, token_convalida, stato, convalidata_at) VALUES
-- ATTIVE
(1, 'Incendio appartamento al terzo piano, fumo denso', 'Via Castello 45, L\'Aquila', 42.3498, 13.3995, 'Paolo Verdi', 'paolo.verdi@email.it', '3338889990', '192.168.1.10', NULL, 'ATTIVA', NOW()),
(2, 'Persona caduta durante escursione sul Gran Sasso', 'Sentiero Campo Imperatore', 42.4467, 13.5561, 'Anna Rossi', 'anna.rossi@email.it', '3331112233', '192.168.1.11', NULL, 'ATTIVA', NOW()),
-- INVIATA
(3, 'Auto fuori strada, conducente impossibilitato a uscire', 'SS17 km 45', 42.2856, 13.5742, 'Marco Bianchi', 'marco.bianchi@email.it', '3335556677', '192.168.1.12', 'token-abc123xyz789', 'INVIATA', NULL),
-- IN_CORSO
(4, 'Malore improvviso, persona anziana con dolori al petto', 'Piazza del Duomo 8, L\'Aquila', 42.3505, 13.3995, 'Carla Neri', 'carla.neri@email.it', '3334445566', '192.168.1.13', NULL, 'IN_CORSO', NOW()),
(5, 'Perdita di gas in condominio, evacuazione in corso', 'Via Garibaldi 22, L\'Aquila', 42.3512, 13.4001, 'Giuseppe Gialli', 'giuseppe.gialli@email.it', '3337778899', '192.168.1.14', NULL, 'IN_CORSO', NOW()),
-- CHIUSA
(6, 'Gatto bloccato su albero a 5 metri', 'Parco del Castello, L\'Aquila', 42.3489, 13.3989, 'Elena Blu', 'elena.blu@email.it', '3339990011', '192.168.1.15', NULL, 'CHIUSA', NOW()),
(7, 'Allagamento scantinato per rottura tubatura', 'Via Roma 102, L\'Aquila', 42.3501, 13.3998, 'Roberto Viola', 'roberto.viola@email.it', '3332223344', '192.168.1.16', NULL, 'CHIUSA', NOW()),
-- IGNORATA
(8, 'Segnalazione incendio (risultato barbecue)', 'Via Parco 15, L\'Aquila', 42.3495, 13.3992, 'Simone Grigi', 'simone.grigi@email.it', '3336667788', '192.168.1.17', NULL, 'IGNORATA', NOW());

-- 11. MISSIONI
INSERT INTO missione (id, richiesta_id, obiettivo, posizione, latitudine, longitudine, caposquadra_id, inizio_at, fine_at, livello_successo, commenti_finali, stato) VALUES
-- IN_CORSO
(1, 4, 'Soccorso sanitario urgente per malore', 'Piazza del Duomo 8, L\'Aquila', 42.3505, 13.3995, 2, NOW(), NULL, NULL, NULL, 'IN_CORSO'),
(2, 5, 'Intervento perdita gas, messa in sicurezza', 'Via Garibaldi 22, L\'Aquila', 42.3512, 13.4001, 4, NOW(), NULL, NULL, NULL, 'IN_CORSO'),
-- CHIUSA
(3, 6, 'Recupero animale bloccato su albero', 'Parco del Castello, L\'Aquila', 42.3489, 13.3989, 3, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY) + INTERVAL 45 MINUTE, 5, 'Gatto recuperato con successo. Nessun danno.', 'CHIUSA'),
(4, 7, 'Intervento per allagamento scantinato', 'Via Roma 102, L\'Aquila', 42.3501, 13.3998, 6, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY) + INTERVAL 3 HOUR, 3, 'Acqua pompata via, ma danni strutturali significativi.', 'CHIUSA');

-- 12. ASSEGNAZIONE OPERATORI
INSERT INTO missione_operatori (missione_id, operatore_id, notificato_at, assegnato_at) VALUES
                                                                                            (1, 2, NOW(), NOW()), (1, 3, NOW(), NOW()),
                                                                                            (2, 4, NOW(), NOW()), (2, 6, NOW(), NOW()),
                                                                                            (3, 3, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
                                                                                            (4, 6, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
                                                                                            (4, 5, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 13. ASSEGNAZIONE MEZZI
INSERT INTO missione_mezzi (missione_id, mezzo_id) VALUES
                                                       (1, 1), (2, 3), (2, 6), (3, 4), (4, 3);

-- 14. ASSEGNAZIONE MATERIALI
INSERT INTO missione_materiali (missione_id, materiale_id, quantita_usata) VALUES
                                                                               (1, 1, 1), (1, 2, 1), (1, 6, 1),
                                                                               (2, 3, 2), (2, 7, 1),
                                                                               (3, 4, 1),
                                                                               (4, 7, 1);

-- 15. AGGIORNAMENTI MISSIONI
INSERT INTO aggiornamento_missione (missione_id, admin_id, descrizione) VALUES
                                                                            (1, 1, 'Squadra arrivata sul posto. Paziente cosciente.'),
                                                                            (1, 1, 'Parametri vitali rilevati. Trasporto in ospedale in corso.'),
                                                                            (2, 1, 'Area evacuata. Tecnici del gas allertati.'),
                                                                            (2, 1, 'Perdita localizzata. Chiusura valvola in corso.'),
                                                                            (3, 1, 'Scala posizionata. Recupero animale in corso.'),
                                                                            (3, 1, 'Gatto recuperato con successo. Nessun ferito.');

-- ==============================================================================
-- VERIFICA POPOLAMENTO
-- ==============================================================================
SELECT 'Ruoli' AS Tabella, COUNT(*) AS Totale FROM role
UNION ALL SELECT 'Utenti', COUNT(*) FROM user
UNION ALL SELECT 'Patenti', COUNT(*) FROM patente
UNION ALL SELECT 'Abilità', COUNT(*) FROM abilita
UNION ALL SELECT 'Mezzi', COUNT(*) FROM mezzo
UNION ALL SELECT 'Materiali', COUNT(*) FROM materiale
UNION ALL SELECT 'Richieste', COUNT(*) FROM richiesta_soccorso
UNION ALL SELECT 'Missioni', COUNT(*) FROM missione
UNION ALL SELECT 'Aggiornamenti', COUNT(*) FROM aggiornamento_missione;

-- ==============================================================================
-- FINE SCRIPT
-- ==============================================================================

CREATE TABLE abilita
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    descrizione LONGTEXT              NULL,
    nome        VARCHAR(100)          NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE aggiornamento_missione
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NULL,
    descrizione LONGTEXT              NOT NULL,
    admin_id    BIGINT                NOT NULL,
    missione_id BIGINT                NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE materiale
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NULL,
    descrizione LONGTEXT              NULL,
    disponibile BIT(1)                NOT NULL,
    nome        VARCHAR(100)          NOT NULL,
    quantita    INT                   NOT NULL,
    tipo        VARCHAR(50)           NULL,
    updated_at  datetime              NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE mezzo
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime              NULL,
    descrizione LONGTEXT              NULL,
    disponibile BIT(1)                NOT NULL,
    nome        VARCHAR(100)          NOT NULL,
    targa       VARCHAR(20)           NULL,
    tipo        VARCHAR(50)           NULL,
    updated_at  datetime              NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE missione
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    commenti_finali  LONGTEXT              NULL,
    created_at       datetime              NULL,
    fine_at          datetime              NULL,
    inizio_at        datetime              NULL,
    latitudine       DECIMAL(10, 8)        NULL,
    livello_successo INT                   NULL,
    longitudine      DECIMAL(11, 8)        NULL,
    obiettivo        LONGTEXT              NOT NULL,
    posizione        VARCHAR(255)          NULL,
    stato            ENUM                  NOT NULL,
    updated_at       datetime              NULL,
    caposquadra_id   BIGINT                NOT NULL,
    richiesta_id     BIGINT                NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE patente
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    descrizione LONGTEXT              NULL,
    tipo        VARCHAR(50)           NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE richiesta_soccorso
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    convalidata_at      datetime              NULL,
    created_at          datetime              NULL,
    descrizione         LONGTEXT              NOT NULL,
    email_segnalante    VARCHAR(255)          NOT NULL,
    foto_url            VARCHAR(255)          NULL,
    indirizzo           VARCHAR(255)          NOT NULL,
    ip_origine          VARCHAR(45)           NULL,
    latitudine          DECIMAL(10, 8)        NULL,
    longitudine         DECIMAL(11, 8)        NULL,
    nome_segnalante     VARCHAR(100)          NOT NULL,
    stato               ENUM                  NOT NULL,
    telefono_segnalante VARCHAR(20)           NULL,
    token_convalida     VARCHAR(255)          NULL,
    updated_at          datetime              NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE `role`
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(50)           NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE user
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    attivo       BIT(1)                NOT NULL,
    cognome      VARCHAR(100)          NOT NULL,
    created_at   datetime              NULL,
    data_nascita date                  NULL,
    email        VARCHAR(255)          NOT NULL,
    indirizzo    VARCHAR(255)          NULL,
    nome         VARCHAR(100)          NOT NULL,
    password     VARCHAR(255)          NOT NULL,
    telefono     VARCHAR(20)           NULL,
    updated_at   datetime              NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE user_abilita
(
    livello    VARCHAR(50) NOT NULL,
    abilita_id BIGINT      NOT NULL,
    user_id    BIGINT      NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (abilita_id, user_id)
);

ALTER TABLE richiesta_soccorso
    ADD CONSTRAINT UK2e684yf9x4cplq4lrlfpruius UNIQUE (token_convalida);

ALTER TABLE abilita
    ADD CONSTRAINT UK4qaqv34lnn8q5fjh3enxd8372 UNIQUE (nome);

ALTER TABLE `role`
    ADD CONSTRAINT UK8sewwnpamngi6b1dwaa88askk UNIQUE (name);

ALTER TABLE missione
    ADD CONSTRAINT UKgiqcqelqyrn355yrxo0b2wu8o UNIQUE (richiesta_id);

ALTER TABLE patente
    ADD CONSTRAINT UKlkp2xiav5weffyx3tpoglropf UNIQUE (tipo);

ALTER TABLE user
    ADD CONSTRAINT UKob8kqyqqgmefl0aco34akdtpe UNIQUE (email);

CREATE INDEX idx_email_segnalante ON richiesta_soccorso (email_segnalante);

CREATE INDEX idx_stato ON richiesta_soccorso (stato);

CREATE INDEX idx_stato ON richiesta_soccorso (stato);

ALTER TABLE missione
    ADD CONSTRAINT FK275j9uxcwitr55gi5v4i8gmcl FOREIGN KEY (caposquadra_id) REFERENCES user (id) ON DELETE NO ACTION;

CREATE INDEX idx_caposquadra ON missione (caposquadra_id);

ALTER TABLE aggiornamento_missione
    ADD CONSTRAINT FK5tmuyc12enusnlyewhgu736je FOREIGN KEY (missione_id) REFERENCES missione (id) ON DELETE NO ACTION;

CREATE INDEX FK5tmuyc12enusnlyewhgu736je ON aggiornamento_missione (missione_id);

ALTER TABLE user_abilita
    ADD CONSTRAINT FK8ope65172kq7pihw8piihyxaw FOREIGN KEY (abilita_id) REFERENCES abilita (id) ON DELETE NO ACTION;

ALTER TABLE missione
    ADD CONSTRAINT FKah2le7wd4ba13jordnspl5vau FOREIGN KEY (richiesta_id) REFERENCES richiesta_soccorso (id) ON DELETE NO ACTION;

ALTER TABLE user_abilita
    ADD CONSTRAINT FKbnv88vad437cfwxbedte7suy9 FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE NO ACTION;

CREATE INDEX FKbnv88vad437cfwxbedte7suy9 ON user_abilita (user_id);

ALTER TABLE aggiornamento_missione
    ADD CONSTRAINT FKd9p3rsw0g2436e63ensl2006h FOREIGN KEY (admin_id) REFERENCES user (id) ON DELETE NO ACTION;

CREATE INDEX FKd9p3rsw0g2436e63ensl2006h ON aggiornamento_missione (admin_id);