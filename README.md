# SoccorsoWeb Services

Progetto di fine corso di **Sviluppo Web Avanzato** - Dipartimento di Ingegneria e Scienze dell'Informazione e Matematica (DISIM), Università degli Studi dell'Aquila.

## Descrizione

SoccorsoWeb Services è un sistema di Web API RESTful per la gestione di un servizio di soccorso. Il progetto implementa le strutture dati e la logica necessaria per gestire richieste di soccorso, missioni e operatori, fornendo un'interfaccia REST completa e un client di test.

## Funzionalità Implementate

### API Endpoints

1. **Autenticazione**
    - Login/logout con username e password
    - Gestione token di autenticazione

2. **Richieste di Soccorso**
    - Inserimento nuova richiesta di soccorso
    - Convalida richiesta tramite link
    - Lista paginata con filtri (attive, in corso, chiuse, ignorate)
    - Lista richieste chiuse con successo parziale (livello < 5)
    - Dettagli richiesta specifica
    - Annullamento richiesta (amministratore)

3. **Missioni**
    - Creazione nuova missione
    - Chiusura missione in corso
    - Dettagli missione specifica
    - Lista missioni per operatore

4. **Operatori**
    - Lista operatori liberi
    - Dettagli operatore specifico

### Client di Test

Client Javascript che interagisce con le API principali:
- Login/logout
- Inserimento richiesta di soccorso
- Visualizzazione lista richieste (con filtri)
- Visualizzazione operatori liberi

## Tecnologie Utilizzate

### Server
- **Spring Boot** - Framework per applicazioni Java enterprise
- **Spring Web** - Per la creazione di REST API
- **Spring Security** - Per autenticazione e autorizzazione
- **Spring Data JPA** - Per la persistenza dei dati
- Database relazionale (MySQL/PostgreSQL/H2)
- Architettura RESTful seguendo il paradigma collection-item

### Client
- **Javascript** (con/senza jQuery)
- Interfaccia per test delle API principali

## Struttura del Progetto

```
SoccorsoWeb/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/univaq/soccorsoweb/
│   │   │       ├── controller/      # REST Controllers
│   │   │       ├── service/         # Business Logic
│   │   │       ├── repository/      # Data Access Layer
│   │   │       ├── model/           # Entity Classes
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── security/        # Security Configuration
│   │   │       └── SoccorsoWebApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/              # Client files
│   └── test/
│       └── java/                    # Test unitari
├── client/
│   └── [codice client Javascript]
├── docs/
│   └── openapi.yaml
├── pom.xml
└── README.md
```

## Documentazione API

La specifica completa delle API è disponibile in formato OpenAPI nella directory `docs/openapi.yaml`.

Per ogni endpoint sono documentati:
- URL e struttura dei path
- Parametri (path, query string)
- Verbi HTTP utilizzati
- Formato payload richiesta (JSON)
- Codici di stato HTTP
- Formato risposte (JSON)

## Installazione e Avvio

### Prerequisiti
- JDK 17 o superiore
- Maven 3.8+
- Database MariaDB (opzionale, è configurato H2 per sviluppo)

### Setup

1. Clona la repository:
```bash
git clone https://github.com/VampaFrancesco/SoccorsoWeb.git
cd SoccorsoWeb
```

2. Configura il database (opzionale):
    - Modifica `src/main/resources/application.properties`
    - Imposta le credenziali del database

3. Compila e avvia il progetto:
```bash
mvn spring-boot:run
```

Oppure crea il JAR ed eseguilo:
```bash
mvn clean package
java -jar target/soccorsoweb-0.0.1-SNAPSHOT.jar
```

4. L'applicazione sarà disponibile su:
```
http://localhost:8080
```

5. Accedi al client di test:
```
http://localhost:8080/client
```

6. Documentazione API interattiva (se Swagger UI è configurato):
```
http://localhost:8080/swagger-ui.html
```

## Configurazione

Le principali configurazioni sono disponibili in `application.properties`:
- Porta del server
- Configurazione database
- Impostazioni di sicurezza
- Livelli di logging

## Licenza

Questo progetto è distribuito sotto licenza Apache License 2.0.

## Autore

**Francesco Vampa** - [VampaFrancesco](https://github.com/VampaFrancesco)

---

*Università degli Studi dell'Aquila - A.A. 2024/2025*
