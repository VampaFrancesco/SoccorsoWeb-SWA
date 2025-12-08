-- ========================================
-- 1. ROLES
-- ========================================
DELETE FROM role;
INSERT INTO role (id, name) VALUES
                                (1, 'ADMIN'),
                                (2, 'OPERATORE');

-- ========================================
-- 2. USERS
-- Password per tutti: "password123" (BCrypt hash)
-- ========================================
DELETE FROM user;
INSERT INTO user (id, email, password, nome, cognome, data_nascita, telefono, indirizzo, attivo, created_at, updated_at) VALUES
                                                                                                                             (1, 'admin@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Mario', 'Rossi', '1980-05-15', '3331234567', 'Via Roma 1, L\'Aquila', 1, NOW(), NOW()),
                                                                                                                             (2, 'operatore1@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Luca', 'Bianchi', '1985-03-20', '3339876543', 'Via Garibaldi 10, L\'Aquila', 1, NOW(), NOW()),
                                                                                                                             (3, 'operatore2@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Giulia', 'Verdi', '1990-07-12', '3335551234', 'Corso Vittorio 5, L\'Aquila', 1, NOW(), NOW()),
                                                                                                                             (4, 'operatore3@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Marco', 'Neri', '1988-11-30', '3337778899', 'Piazza Duomo 3, L\'Aquila', 1, NOW(), NOW()),
                                                                                                                             (5, 'operatore4@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Sara', 'Gialli', '1992-02-14', '3332223344', 'Via XX Settembre 8, L\'Aquila', 1, NOW(), NOW()),
                                                                                                                             (6, 'operatore5@soccorsoweb.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhkO', 'Alessandro', 'Blu', '1987-09-25', '3334445566', 'Viale Gran Sasso 12, L\'Aquila', 1, NOW(), NOW());

-- ========================================
-- 3. USER_ROLES
-- ========================================
DELETE FROM user_roles;
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1), -- admin@soccorsoweb.it → ADMIN
                                              (2, 2), -- operatore1 → OPERATORE
                                              (3, 2), -- operatore2 → OPERATORE
                                              (4, 2), -- operatore3 → OPERATORE
                                              (5, 2), -- operatore4 → OPERATORE
                                              (6, 2); -- operatore5 → OPERATORE

-- ========================================
-- 4. ABILITA
-- ========================================
DELETE FROM abilita;
INSERT INTO abilita (id, nome, descrizione) VALUES
                                                (1, 'Infermiere', 'Competenze infermieristiche di base e avanzate'),
                                                (2, 'Elettricista', 'Installazione e riparazione impianti elettrici'),
                                                (3, 'Sommozzatore', 'Immersioni subacquee fino a 40m'),
                                                (4, 'Soccorritore Alpino', 'Operazioni di soccorso in montagna'),
                                                (5, 'Vigile del Fuoco', 'Addestramento antincendio completo'),
                                                (6, 'Medico', 'Laurea in medicina e chirurgia');

-- ========================================
-- 5. USER_ABILITA
-- ========================================
DELETE FROM user_abilita;
INSERT INTO user_abilita (user_id, abilita_id, livello) VALUES
                                                            (2, 2, 'Base'),       -- Luca: Elettricista Base
                                                            (2, 5, 'Avanzato'),   -- Luca: Vigile del Fuoco Avanzato
                                                            (3, 1, 'Avanzato'),   -- Giulia: Infermiere Avanzato
                                                            (3, 6, 'Esperto'),    -- Giulia: Medico Esperto
                                                            (4, 4, 'Esperto'),    -- Marco: Soccorritore Alpino Esperto
                                                            (4, 5, 'Base'),       -- Marco: Vigile del Fuoco Base
                                                            (5, 1, 'Intermedio'), -- Sara: Infermiere Intermedio
                                                            (5, 3, 'Base'),       -- Sara: Sommozzatore Base
                                                            (6, 2, 'Esperto'),    -- Alessandro: Elettricista Esperto
                                                            (6, 5, 'Avanzato');   -- Alessandro: Vigile del Fuoco Avanzato

-- ========================================
-- 6. PATENTI
-- ========================================
DELETE FROM patente;
INSERT INTO patente (id, tipo, descrizione) VALUES
                                                (1, 'A', 'Patente motocicli'),
                                                (2, 'B', 'Patente autovetture'),
                                                (3, 'C', 'Patente autocarri'),
                                                (4, 'D', 'Patente autobus'),
                                                (5, 'NAUTICA', 'Patente nautica entro 12 miglia');

-- ========================================
-- 7. USER_PATENTI
-- ========================================
DELETE FROM user_patenti;
INSERT INTO user_patenti (user_id, patente_id, conseguita_il) VALUES
                                                                  (2, 2, '2003-06-15'), -- Luca: B
                                                                  (2, 3, '2010-03-20'), -- Luca: C
                                                                  (3, 2, '2008-08-10'), -- Giulia: B
                                                                  (3, 5, '2015-05-12'), -- Giulia: NAUTICA
                                                                  (4, 1, '2005-09-22'), -- Marco: A
                                                                  (4, 2, '2006-04-18'), -- Marco: B
                                                                  (5, 2, '2010-11-05'), -- Sara: B
                                                                  (5, 4, '2018-06-30'), -- Sara: D
                                                                  (6, 2, '2005-07-14'), -- Alessandro: B
                                                                  (6, 3, '2012-02-28'); -- Alessandro: C

-- ========================================
-- 8. MATERIALI
-- ========================================
DELETE FROM materiale;
INSERT INTO materiale (id, nome, descrizione, tipo, quantita, disponibile, created_at, updated_at) VALUES
                                                                                                       (1, 'Kit Medico Avanzato', 'Kit completo per emergenze mediche', 'Medico', 5, 1, NOW(), NOW()),
                                                                                                       (2, 'Defibrillatore DAE', 'Defibrillatore automatico esterno', 'Medico', 3, 1, NOW(), NOW()),
                                                                                                       (3, 'Estintore 6kg', 'Estintore a polvere 6kg', 'Antincendio', 10, 1, NOW(), NOW()),
                                                                                                       (4, 'Scala Telescopica 10m', 'Scala telescopica professionale', 'Attrezzatura', 2, 1, NOW(), NOW()),
                                                                                                       (5, 'Set Soccorso Alpino', 'Corde, moschettoni, imbracature', 'Alpinismo', 4, 1, NOW(), NOW()),
                                                                                                       (6, 'Barella Spinale', 'Barella con supporto spinale', 'Medico', 6, 1, NOW(), NOW()),
                                                                                                       (7, 'Generatore Elettrico', 'Generatore portatile 3kW', 'Attrezzatura', 2, 1, NOW(), NOW());

-- ========================================
-- 9. MEZZI
-- ========================================
DELETE FROM mezzo;
INSERT INTO mezzo (id, nome, descrizione, tipo, targa, disponibile, created_at, updated_at) VALUES
                                                                                                (1, 'Ambulanza A1', 'Ambulanza completamente attrezzata', 'Ambulanza', 'AQ123AB', 1, NOW(), NOW()),
                                                                                                (2, 'Ambulanza A2', 'Ambulanza di supporto', 'Ambulanza', 'AQ456CD', 1, NOW(), NOW()),
                                                                                                (3, 'Autopompa P1', 'Autopompa con serbatoio 5000L', 'Autopompa', 'AQ789EF', 1, NOW(), NOW()),
                                                                                                (4, 'Fuoristrada F1', 'Fuoristrada 4x4 per terreni difficili', 'Fuoristrada', 'AQ321GH', 1, NOW(), NOW()),
                                                                                                (5, 'Elicottero E1', 'Elicottero da soccorso', 'Elicottero', 'I-SOCC', 1, NOW(), NOW()),
                                                                                                (6, 'Auto Pattuglia AP1', 'Auto di pattuglia standard', 'Auto', 'AQ654IJ', 1, NOW(), NOW());

-- ========================================
-- 10. RICHIESTE SOCCORSO
-- ⭐ AGGIORNATO: Aggiunti livello_successo e valutata_at
-- ========================================
DELETE FROM richiesta_soccorso;
INSERT INTO richiesta_soccorso (
    id, descrizione, indirizzo, latitudine, longitudine,
    nome_segnalante, email_segnalante, telefono_segnalante,
    foto_url, ip_origine, token_convalida, stato, convalidata_at,
    livello_successo, valutata_at, created_at, updated_at
) VALUES
-- Richieste ATTIVE (senza valutazione)
(1, 'Incendio appartamento al terzo piano, fumo denso', 'Via Castello 45, L\'Aquila', 42.34980000, 13.39950000,
 'Paolo Verdi', 'paolo.verdi@email.it', '3338889990', NULL, '192.168.1.10', NULL, 'ATTIVA',
 '2025-12-05 23:25:02', NULL, NULL, '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

(2, 'Persona caduta durante escursione sul Gran Sasso', 'Sentiero Campo Imperatore', 42.44670000, 13.55610000,
 'Anna Rossi', 'anna.rossi@email.it', '3331112233', NULL, '192.168.1.11', NULL, 'ATTIVA',
 '2025-12-05 23:25:02', NULL, NULL, '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

-- Richieste INVIATE (in attesa di convalida)
(3, 'Auto fuori strada, conducente impossibilitato a uscire', 'SS17 km 45', 42.28560000, 13.57420000,
 'Marco Bianchi', 'marco.bianchi@email.it', '3335556677', NULL, '192.168.1.12', 'token-abc123xyz789', 'INVIATA',
 NULL, NULL, NULL, '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

-- Richieste IN_CORSO (senza valutazione)
(4, 'Malore improvviso, persona anziana con dolori al petto', 'Piazza del Duomo 8, L\'Aquila', 42.35050000, 13.39950000,
 'Carla Neri', 'carla.neri@email.it', '3334445566', NULL, '192.168.1.13', NULL, 'IN_CORSO',
 '2025-12-05 23:25:02', NULL, NULL, '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

(5, 'Perdita di gas in condominio, evacuazione in corso', 'Via Garibaldi 22, L\'Aquila', 42.35120000, 13.40010000,
 'Giuseppe Gialli', 'giuseppe.gialli@email.it', '3337778899', NULL, '192.168.1.14', NULL, 'IN_CORSO',
 '2025-12-05 23:25:02', NULL, NULL, '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

-- Richieste CHIUSE con valutazione 5 stelle ⭐⭐⭐⭐⭐ (NON torna in API 5)
(6, 'Gatto bloccato su albero a 5 metri', 'Parco del Castello, L\'Aquila', 42.34890000, 13.39890000,
 'Elena Blu', 'elena.blu@email.it', '3339990011', NULL, '192.168.1.15', NULL, 'CHIUSA',
 '2025-12-05 23:25:02', 5, '2025-12-05 23:30:00', '2025-12-05 23:25:02', '2025-12-05 23:30:00'),

-- ⭐ Richieste CHIUSE con valutazione < 5 (per testare API 5)
(7, 'Allagamento scantinato per rottura tubatura', 'Via Roma 102, L\'Aquila', 42.35010000, 13.39980000,
 'Roberto Viola', 'roberto.viola@email.it', '3332223344', NULL, '192.168.1.16', NULL, 'CHIUSA',
 '2025-12-05 23:25:02', 3, '2025-12-05 23:35:00', '2025-12-05 23:25:02', '2025-12-05 23:35:00'),  -- ⭐⭐⭐

-- Richieste IGNORATE (senza valutazione)
(8, 'Segnalazione incendio (risultato barbecue)', 'Via Parco 15, L\'Aquila', 42.34950000, 13.39920000,
 'Simone Grigi', 'simone.grigi@email.it', '3336667788', NULL, '192.168.1.17', NULL, 'IGNORATA',
 '2025-12-05 23:25:02', NULL, NULL, '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

-- Richieste CONVALIDATE (senza valutazione)
(9, 'Albero caduto sulla strada', 'Via Abruzzo 89, L\'Aquila', 42.35200000, 13.40100000,
 'Francesco Vampa', 'francesco.vampa@email.it', '3338887766', NULL, '127.0.0.1', '5f0d88f2-f6de-44b7-a019-4213636b48eb', 'CONVALIDATA',
 '2025-12-06 00:25:29', NULL, NULL, '2025-12-06 00:25:29', '2025-12-06 00:25:29'),

(10, 'Perdita d\'acqua da contatore', 'Via Castello 34, L\'Aquila', 42.34960000, 13.39960000,
 'Lucia Bianchi', 'lucia.bianchi@email.it', '3335554444', NULL, '192.168.1.18', '398af046-9a45-4669-ba4f-2e15f3b239ec', 'CONVALIDATA',
 '2025-12-06 00:39:11', NULL, NULL, '2025-12-06 00:34:13', '2025-12-06 00:39:11'),

-- ⭐ Richieste CHIUSE aggiuntive con valutazioni < 5 (per testare API 5)
(11, 'Incendio cucina, fumo intenso', 'Via Napoli 45, L\'Aquila', 42.35000000, 13.40000000,
 'Maria Rossi', 'maria.rossi@email.it', '3331112222', NULL, '192.168.1.20', NULL, 'CHIUSA',
 '2025-12-06 10:00:00', 2, '2025-12-06 12:00:00', '2025-12-06 09:45:00', '2025-12-06 12:00:00'),  -- ⭐⭐

(12, 'Soccorso persona ferita in caduta', 'Piazza Italia 10, L\'Aquila', 42.35100000, 13.39900000,
 'Luigi Verdi', 'luigi.verdi@email.it', '3334445555', NULL, '192.168.1.21', NULL, 'CHIUSA',
 '2025-12-06 14:00:00', 4, '2025-12-06 16:00:00', '2025-12-06 13:50:00', '2025-12-06 16:00:00'),  -- ⭐⭐⭐⭐

(13, 'Intervento per black-out elettrico', 'Via Duca degli Abruzzi 78, L\'Aquila', 42.35150000, 13.40050000,
 'Giorgio Neri', 'giorgio.neri@email.it', '3336669988', NULL, '192.168.1.22', NULL, 'CHIUSA',
 '2025-12-07 08:00:00', 1, '2025-12-07 10:00:00', '2025-12-07 07:30:00', '2025-12-07 10:00:00');  -- ⭐

-- ========================================
-- 11. MISSIONI
-- ========================================
DELETE FROM missione;
INSERT INTO missione (id, richiesta_id, obiettivo, posizione, latitudine, longitudine, caposquadra_id, stato, inizio_at, fine_at, livello_successo, commenti_finali, created_at, updated_at) VALUES
-- Missioni IN_CORSO
(1, 4, 'Soccorso sanitario urgente per malore', 'Piazza del Duomo 8, L\'Aquila', 42.35050000, 13.39950000, 2, 'IN_CORSO',
 '2025-12-05 23:25:02', NULL, NULL, NULL, '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

(2, 5, 'Intervento perdita gas, messa in sicurezza', 'Via Garibaldi 22, L\'Aquila', 42.35120000, 13.40010000, 4, 'IN_CORSO',
 '2025-12-05 23:25:02', NULL, NULL, NULL, '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

-- Missioni CHIUSE
(3, 6, 'Recupero animale bloccato su albero', 'Parco del Castello, L\'Aquila', 42.34890000, 13.39890000, 3, 'CHIUSA',
 '2025-12-04 23:25:02', '2025-12-05 00:10:02', 5, 'Gatto recuperato con successo. Nessun danno.', '2025-12-05 23:25:02', '2025-12-05 23:25:02'),

(4, 7, 'Intervento per allagamento scantinato', 'Via Roma 102, L\'Aquila', 42.35010000, 13.39980000, 6, 'CHIUSA',
 '2025-12-03 23:25:02', '2025-12-04 02:25:02', 3, 'Acqua pompata via, ma danni strutturali significativi.', '2025-12-05 23:25:02', '2025-12-05 23:25:02');

-- ========================================
-- 12. AGGIORNAMENTI MISSIONE
-- ========================================
DELETE FROM aggiornamento_missione;
INSERT INTO aggiornamento_missione (id, missione_id, admin_id, descrizione, created_at) VALUES
                                                                                            (1, 1, 1, 'Squadra arrivata sul posto. Paziente cosciente.', '2025-12-05 23:25:02'),
                                                                                            (2, 1, 1, 'Parametri vitali rilevati. Trasporto in ospedale in corso.', '2025-12-05 23:26:00'),
                                                                                            (3, 2, 1, 'Area evacuata. Tecnici del gas allertati.', '2025-12-05 23:25:02'),
                                                                                            (4, 2, 1, 'Perdita localizzata. Chiusura valvola in corso.', '2025-12-05 23:27:00'),
                                                                                            (5, 3, 1, 'Scala posizionata. Recupero animale in corso.', '2025-12-04 23:30:00'),
                                                                                            (6, 3, 1, 'Gatto recuperato con successo. Nessun ferito.', '2025-12-05 00:10:00');

-- ========================================
-- 13. MISSIONE_MATERIALI
-- ========================================
DELETE FROM missione_materiali;
INSERT INTO missione_materiali (missione_id, materiale_id, quantita_usata, assegnato_at) VALUES
                                                                                             (1, 1, 1, '2025-12-05 23:25:02'), -- Missione 1: Kit Medico
                                                                                             (1, 2, 1, '2025-12-05 23:25:02'), -- Missione 1: Defibrillatore
                                                                                             (1, 6, 1, '2025-12-05 23:25:02'), -- Missione 1: Barella Spinale
                                                                                             (2, 3, 2, '2025-12-05 23:25:02'), -- Missione 2: Estintore (x2)
                                                                                             (2, 7, 1, '2025-12-05 23:25:02'), -- Missione 2: Generatore
                                                                                             (3, 4, 1, '2025-12-04 23:25:02'), -- Missione 3: Scala Telescopica
                                                                                             (4, 7, 1, '2025-12-03 23:25:02'); -- Missione 4: Generatore

-- ========================================
-- 14. MISSIONE_MEZZI
-- ========================================
DELETE FROM missione_mezzi;
INSERT INTO missione_mezzi (missione_id, mezzo_id, assegnato_at) VALUES
                                                                     (1, 1, '2025-12-05 23:25:02'), -- Missione 1: Ambulanza A1
                                                                     (2, 3, '2025-12-05 23:25:02'), -- Missione 2: Autopompa P1
                                                                     (2, 6, '2025-12-05 23:25:02'), -- Missione 2: Auto Pattuglia AP1
                                                                     (3, 4, '2025-12-04 23:25:02'), -- Missione 3: Fuoristrada F1
                                                                     (4, 3, '2025-12-03 23:25:02'); -- Missione 4: Autopompa P1

-- ========================================
-- 15. MISSIONE_OPERATORI
-- ========================================
DELETE FROM missione_operatori;
INSERT INTO missione_operatori (missione_id, operatore_id, notificato_at, assegnato_at) VALUES
                                                                                            (1, 2, '2025-12-05 23:25:02', '2025-12-05 23:25:02'), -- Missione 1: Luca (caposquadra)
                                                                                            (1, 3, '2025-12-05 23:25:02', '2025-12-05 23:25:02'), -- Missione 1: Giulia
                                                                                            (2, 4, '2025-12-05 23:25:02', '2025-12-05 23:25:02'), -- Missione 2: Marco (caposquadra)
                                                                                            (2, 6, '2025-12-05 23:25:02', '2025-12-05 23:25:02'), -- Missione 2: Alessandro
                                                                                            (3, 3, '2025-12-04 23:25:02', '2025-12-04 23:25:02'), -- Missione 3: Giulia (caposquadra)
                                                                                            (4, 5, '2025-12-03 23:25:02', '2025-12-03 23:25:02'), -- Missione 4: Sara
                                                                                            (4, 6, '2025-12-03 23:25:02', '2025-12-03 23:25:02'); -- Missione 4: Alessandro (caposquadra)