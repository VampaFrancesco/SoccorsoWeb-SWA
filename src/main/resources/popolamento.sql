-- ==============================================================================
-- Script di popolamento database SoccorsoWeb
-- ==============================================================================

-- Pulisci dati esistenti (opzionale, commentare se non vuoi cancellare)
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE aggiornamento_missione;
-- TRUNCATE TABLE missione_materiale;
-- TRUNCATE TABLE missione_mezzo;
-- TRUNCATE TABLE missione_operatore;
-- TRUNCATE TABLE missione;
-- TRUNCATE TABLE richiesta_soccorso;
-- TRUNCATE TABLE user_abilita;
-- TRUNCATE TABLE user_patente;
-- TRUNCATE TABLE user_role;
-- TRUNCATE TABLE materiale;
-- TRUNCATE TABLE mezzo;
-- TRUNCATE TABLE abilita;
-- TRUNCATE TABLE patente;
-- TRUNCATE TABLE user;
-- TRUNCATE TABLE role;
-- SET FOREIGN_KEY_CHECKS = 1;

-- ==============================================================================
-- 1. RUOLI
-- ==============================================================================
INSERT INTO role (id, name) VALUES
                                (1, 'ADMIN'),
                                (2, 'OPERATORE');

-- ==============================================================================
-- 2. PATENTI
-- ==============================================================================
INSERT INTO patente (id, tipo, descrizione) VALUES
                                                (1, 'A', 'Patente motocicli'),
                                                (2, 'B', 'Patente autovetture'),
                                                (3, 'C', 'Patente autocarri'),
                                                (4, 'D', 'Patente autobus'),
                                                (5, 'NAUTICA', 'Patente nautica entro 12 miglia');

-- ==============================================================================
-- 3. ABILITÀ
-- ==============================================================================
INSERT INTO abilita (id, nome, descrizione) VALUES
                                                (1, 'Infermiere', 'Competenze infermieristiche di base e avanzate'),
                                                (2, 'Elettricista', 'Installazione e riparazione impianti elettrici'),
                                                (3, 'Sommozzatore', 'Immersioni subacquee fino a 40m'),
                                                (4, 'Soccorritore Alpino', 'Operazioni di soccorso in montagna'),
                                                (5, 'Vigile del Fuoco', 'Addestramento antincendio completo'),
                                                (6, 'Medico', 'Laurea in medicina e chirurgia');

-- ==============================================================================
-- 4. UTENTI
-- Password per tutti: "Password123!"
-- Hash BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO
-- ==============================================================================
INSERT INTO user (id, email, password, nome, cognome, data_nascita, telefono, indirizzo, attivo, created_at) VALUES
-- Admin
(1, 'admin@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Mario', 'Rossi', '1980-05-15', '3331234567', 'Via Roma 1, L\'Aquila', TRUE, NOW()),

-- Operatori
(2, 'operatore1@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Luca', 'Bianchi', '1985-03-20', '3339876543', 'Via Garibaldi 10, L\'Aquila', TRUE, NOW()),
(3, 'operatore2@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Giulia', 'Verdi', '1990-07-12', '3335551234', 'Corso Vittorio 5, L\'Aquila', TRUE, NOW()),
(4, 'operatore3@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Marco', 'Neri', '1988-11-30', '3337778899', 'Piazza Duomo 3, L\'Aquila', TRUE, NOW()),
(5, 'operatore4@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Sara', 'Gialli', '1992-02-14', '3332223344', 'Via XX Settembre 8, L\'Aquila', TRUE, NOW()),
(6, 'operatore5@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Alessandro', 'Blu', '1987-09-25', '3334445566', 'Viale Gran Sasso 12, L\'Aquila', TRUE, NOW());

-- ==============================================================================
-- 5. ASSEGNAZIONE RUOLI
-- ==============================================================================
INSERT INTO user_role (user_id, role_id) VALUES
-- Admin
(1, 1),  -- Mario Rossi -> ADMIN

-- Operatori
(2, 2),  -- Luca Bianchi -> OPERATORE
(3, 2),  -- Giulia Verdi -> OPERATORE
(4, 2),  -- Marco Neri -> OPERATORE
(5, 2),  -- Sara Gialli -> OPERATORE
(6, 2);  -- Alessandro Blu -> OPERATORE

-- ==============================================================================
-- 6. ASSEGNAZIONE PATENTI
-- ==============================================================================
INSERT INTO user_patente (user_id, patente_id, conseguita_il) VALUES
-- Luca Bianchi
(2, 2, '2003-06-15'),  -- Patente B
(2, 3, '2010-03-20'),  -- Patente C

-- Giulia Verdi
(3, 2, '2008-08-10'),  -- Patente B
(3, 5, '2015-05-12'),  -- Patente Nautica

-- Marco Neri
(4, 2, '2006-04-18'),  -- Patente B
(4, 1, '2005-09-22'),  -- Patente A

-- Sara Gialli
(5, 2, '2010-11-05'),  -- Patente B
(5, 4, '2018-06-30'),  -- Patente D

-- Alessandro Blu
(6, 2, '2005-07-14'),  -- Patente B
(6, 3, '2012-02-28');  -- Patente C

-- ==============================================================================
-- 7. ASSEGNAZIONE ABILITÀ
-- ==============================================================================
INSERT INTO user_abilita (user_id, abilita_id, livello) VALUES
-- Luca Bianchi
(2, 5, 'Avanzato'),    -- Vigile del Fuoco
(2, 2, 'Base'),        -- Elettricista

-- Giulia Verdi
(3, 1, 'Avanzato'),    -- Infermiere
(3, 6, 'Esperto'),     -- Medico

-- Marco Neri
(4, 4, 'Esperto'),     -- Soccorritore Alpino
(4, 5, 'Base'),        -- Vigile del Fuoco

-- Sara Gialli
(5, 1, 'Intermedio'),  -- Infermiere
(5, 3, 'Base'),        -- Sommozzatore

-- Alessandro Blu
(6, 2, 'Esperto'),     -- Elettricista
(6, 5, 'Avanzato');    -- Vigile del Fuoco

-- ==============================================================================
-- 8. MEZZI
-- ==============================================================================
INSERT INTO mezzo (id, nome, descrizione, tipo, targa, disponibile, created_at) VALUES
                                                                                    (1, 'Ambulanza A1', 'Ambulanza completamente attrezzata', 'Ambulanza', 'AQ123AB', TRUE, NOW()),
                                                                                    (2, 'Ambulanza A2', 'Ambulanza di supporto', 'Ambulanza', 'AQ456CD', TRUE, NOW()),
                                                                                    (3, 'Autopompa P1', 'Autopompa con serbatoio 5000L', 'Autopompa', 'AQ789EF', TRUE, NOW()),
                                                                                    (4, 'Fuoristrada F1', 'Fuoristrada 4x4 per terreni difficili', 'Fuoristrada', 'AQ321GH', TRUE, NOW()),
                                                                                    (5, 'Elicottero E1', 'Elicottero da soccorso', 'Elicottero', 'I-SOCC', TRUE, NOW()),
                                                                                    (6, 'Auto Pattuglia AP1', 'Auto di pattuglia standard', 'Auto', 'AQ654IJ', TRUE, NOW());

-- ==============================================================================
-- 9. MATERIALI
-- ==============================================================================
INSERT INTO materiale (id, nome, descrizione, tipo, quantita, disponibile, created_at) VALUES
                                                                                           (1, 'Kit Medico Avanzato', 'Kit completo per emergenze mediche', 'Medico', 5, TRUE, NOW()),
                                                                                           (2, 'Defibrillatore DAE', 'Defibrillatore automatico esterno', 'Medico', 3, TRUE, NOW()),
                                                                                           (3, 'Estintore 6kg', 'Estintore a polvere 6kg', 'Antincendio', 10, TRUE, NOW()),
                                                                                           (4, 'Scala Telescopica 10m', 'Scala telescopica professionale', 'Attrezzatura', 2, TRUE, NOW()),
                                                                                           (5, 'Set Soccorso Alpino', 'Corde, moschettoni, imbracature', 'Alpinismo', 4, TRUE, NOW()),
                                                                                           (6, 'Barella Spinale', 'Barella con supporto spinale', 'Medico', 6, TRUE, NOW()),
                                                                                           (7, 'Generatore Elettrico', 'Generatore portatile 3kW', 'Attrezzatura', 2, TRUE, NOW());

-- ==============================================================================
-- 10. RICHIESTE DI SOCCORSO
-- ==============================================================================
INSERT INTO richiesta_soccorso (id, descrizione, indirizzo, latitudine, longitudine, nome_segnalante, email_segnalante, telefono_segnalante, ip_origine, token_convalida, stato, convalidata_at, created_at) VALUES
-- Richieste ATTIVE (convalidate ma non ancora in missione)
(1, 'Incendio appartamento al terzo piano, fumo denso', 'Via Castello 45, L\'Aquila', 42.3498, 13.3995, 'Paolo Verdi', 'paolo.verdi@email.it', '3338889990', '192.168.1.10', NULL, 'ATTIVA', NOW(), DATE_SUB(NOW(), INTERVAL 30 MINUTE)),

(2, 'Persona caduta durante escursione sul Gran Sasso, possibile frattura', 'Sentiero Campo Imperatore', 42.4467, 13.5561, 'Anna Rossi', 'anna.rossi@email.it', '3331112233', '192.168.1.11', NULL, 'ATTIVA', NOW(), DATE_SUB(NOW(), INTERVAL 1 HOUR)),

-- Richieste INVIATA (in attesa di convalida)
(3, 'Auto fuori strada, conducente cosciente ma impossibilitato a uscire', 'SS17 km 45', 42.2856, 13.5742, 'Marco Bianchi', 'marco.bianchi@email.it', '3335556677', '192.168.1.12', 'token-abc123xyz789', 'INVIATA', NULL, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),

-- Richieste IN_CORSO (con missione attiva)
(4, 'Malore improvviso, persona anziana con dolori al petto', 'Piazza del Duomo 8, L\'Aquila', 42.3505, 13.3995, 'Carla Neri', 'carla.neri@email.it', '3334445566', '192.168.1.13', NULL, 'IN_CORSO', NOW(), DATE_SUB(NOW(), INTERVAL 2 HOUR)),

(5, 'Perdita di gas in condominio, evacuazione in corso', 'Via Garibaldi 22, L\'Aquila', 42.3512, 13.4001, 'Giuseppe Gialli', 'giuseppe.gialli@email.it', '3337778899', '192.168.1.14', NULL, 'IN_CORSO', NOW(), DATE_SUB(NOW(), INTERVAL 3 HOUR)),

-- Richieste CHIUSA (missione completata)
(6, 'Gatto bloccato su albero a 5 metri di altezza', 'Parco del Castello, L\'Aquila', 42.3489, 13.3989, 'Elena Blu', 'elena.blu@email.it', '3339990011', '192.168.1.15', NULL, 'CHIUSA', NOW(), DATE_SUB(NOW(), INTERVAL 1 DAY)),

(7, 'Allagamento scantinato per rottura tubatura', 'Via Roma 102, L\'Aquila', 42.3501, 13.3998, 'Roberto Viola', 'roberto.viola@email.it', '3332223344', '192.168.1.16', NULL, 'CHIUSA', NOW(), DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- Richieste IGNORATA (falso allarme)
(8, 'Segnalazione incendio (risultato essere barbecue)', 'Via Parco 15, L\'Aquila', 42.3495, 13.3992, 'Simone Grigi', 'simone.grigi@email.it', '3336667788', '192.168.1.17', NULL, 'IGNORATA', NOW(), DATE_SUB(NOW(), INTERVAL 3 DAY));

-- ==============================================================================
-- 11. MISSIONI
-- ==============================================================================
INSERT INTO missione (id, richiesta_id, obiettivo, posizione, latitudine, longitudine, caposquadra_id, inizio_at, fine_at, livello_successo, commenti_finali, stato, created_at) VALUES
-- Missione IN_CORSO #1
(1, 4, 'Soccorso sanitario urgente per malore con dolori al petto', 'Piazza del Duomo 8, L\'Aquila', 42.3505, 13.3995, 2, DATE_SUB(NOW(), INTERVAL 2 HOUR), NULL, NULL, NULL, 'IN_CORSO', DATE_SUB(NOW(), INTERVAL 2 HOUR)),

-- Missione IN_CORSO #2
(2, 5, 'Intervento per perdita di gas, messa in sicurezza area', 'Via Garibaldi 22, L\'Aquila', 42.3512, 13.4001, 4, DATE_SUB(NOW(), INTERVAL 3 HOUR), NULL, NULL, NULL, 'IN_CORSO', DATE_SUB(NOW(), INTERVAL 3 HOUR)),

-- Missione CHIUSA #1 (Successo Pieno)
(3, 6, 'Recupero animale bloccato su albero', 'Parco del Castello, L\'Aquila', 42.3489, 13.3989, 3, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY) + INTERVAL 45 MINUTE, 5, 'Gatto recuperato in sicurezza, restituito al proprietario. Nessun danno.', 'CHIUSA', DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- Missione CHIUSA #2 (Successo Parziale)
(4, 7, 'Intervento per allagamento scantinato', 'Via Roma 102, L\'Aquila', 42.3501, 13.3998, 6, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY) + INTERVAL 3 HOUR, 3, 'Acqua pompata via, ma danni strutturali significativi. Necessario intervento idraulico specializzato.', 'CHIUSA', DATE_SUB(NOW(), INTERVAL 2 DAY));

-- ==============================================================================
-- 12. ASSEGNAZIONE OPERATORI ALLE MISSIONI
-- ==============================================================================
INSERT INTO missione_operatore (missione_id, operatore_id, notificato_at, assegnato_at) VALUES
-- Missione 1 (Malore)
(1, 2, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),  -- Caposquadra
(1, 3, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR)),  -- Operatore (Medico)

-- Missione 2 (Perdita gas)
(2, 4, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),  -- Caposquadra
(2, 6, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR)),  -- Operatore (Elettricista)

-- Missione 3 (Gatto - CHIUSA)
(3, 3, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- Missione 4 (Allagamento - CHIUSA)
(4, 6, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(4, 5, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

-- ==============================================================================
-- 13. ASSEGNAZIONE MEZZI ALLE MISSIONI
-- ==============================================================================
INSERT INTO missione_mezzo (missione_id, mezzo_id, assegnato_at) VALUES
-- Missione 1 (Malore)
(1, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),  -- Ambulanza A1

-- Missione 2 (Perdita gas)
(2, 3, DATE_SUB(NOW(), INTERVAL 3 HOUR)),  -- Autopompa P1
(2, 6, DATE_SUB(NOW(), INTERVAL 3 HOUR)),  -- Auto Pattuglia AP1

-- Missione 3 (Gatto - CHIUSA)
(3, 4, DATE_SUB(NOW(), INTERVAL 1 DAY)),   -- Fuoristrada F1

-- Missione 4 (Allagamento - CHIUSA)
(4, 3, DATE_SUB(NOW(), INTERVAL 2 DAY));   -- Autopompa P1

-- ==============================================================================
-- 14. ASSEGNAZIONE MATERIALI ALLE MISSIONI
-- ==============================================================================
INSERT INTO missione_materiale (missione_id, materiale_id, quantita_usata, assegnato_at) VALUES
-- Missione 1 (Malore)
(1, 1, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),  -- Kit Medico Avanzato
(1, 2, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),  -- Defibrillatore DAE
(1, 6, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR)),  -- Barella Spinale

-- Missione 2 (Perdita gas)
(2, 3, 2, DATE_SUB(NOW(), INTERVAL 3 HOUR)),  -- Estintore 6kg
(2, 7, 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),  -- Generatore Elettrico

-- Missione 3 (Gatto - CHIUSA)
(3, 4, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),   -- Scala Telescopica 10m

-- Missione 4 (Allagamento - CHIUSA)
(4, 7, 1, DATE_SUB(NOW(), INTERVAL 2 DAY));   -- Generatore Elettrico

-- ==============================================================================
-- 15. AGGIORNAMENTI MISSIONI
-- ==============================================================================
INSERT INTO aggiornamento_missione (missione_id, admin_id, descrizione, created_at) VALUES
-- Missione 1 (Malore)
(1, 1, 'Squadra arrivata sul posto. Paziente cosciente, dolori al petto persistenti.', DATE_SUB(NOW(), INTERVAL 1 HOUR 50 MINUTE)),
(1, 1, 'Parametri vitali rilevati. Trasporto in ospedale in corso.', DATE_SUB(NOW(), INTERVAL 1 HOUR 30 MINUTE)),

-- Missione 2 (Perdita gas)
(2, 1, 'Area evacuata. Tecnici del gas allertati.', DATE_SUB(NOW(), INTERVAL 2 HOUR 45 MINUTE)),
(2, 1, 'Perdita localizzata. Intervento in corso per chiusura valvola principale.', DATE_SUB(NOW(), INTERVAL 2 HOUR 15 MINUTE)),

-- Missione 3 (Gatto - CHIUSA)
(3, 1, 'Scala posizionata. Recupero animale in corso.', DATE_SUB(NOW(), INTERVAL 1 DAY) + INTERVAL 15 MINUTE),
(3, 1, 'Gatto recuperato con successo. Nessun ferito.', DATE_SUB(NOW(), INTERVAL 1 DAY) + INTERVAL 40 MINUTE);

-- ==============================================================================
-- FINE POPOLAMENTO DATABASE
-- ==============================================================================

-- Verifica conteggi
SELECT 'Ruoli' AS Tabella, COUNT(*) AS Totale FROM role
UNION ALL SELECT 'Utenti', COUNT(*) FROM user
UNION ALL SELECT 'Patenti', COUNT(*) FROM patente
UNION ALL SELECT 'Abilità', COUNT(*) FROM abilita
UNION ALL SELECT 'Mezzi', COUNT(*) FROM mezzo
UNION ALL SELECT 'Materiali', COUNT(*) FROM materiale
UNION ALL SELECT 'Richieste', COUNT(*) FROM richiesta_soccorso
UNION ALL SELECT 'Missioni', COUNT(*) FROM missione
UNION ALL SELECT 'Aggiornamenti', COUNT(*) FROM aggiornamento_missione;
