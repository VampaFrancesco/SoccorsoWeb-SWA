$(function() {
    // Controlla se c'è già un token salvato
    if (getToken()) {
        showMainSection();
    } else {
        showPublicSection();
    }

    // ========== NAVIGAZIONE TRA SEZIONI ==========

    $('#show-login-btn').on('click', function() {
        showLoginSection();
    });

    $('#back-to-public').on('click', function() {
        showPublicSection();
    });

    // ========== FORM PUBBLICO: RICHIESTA SOCCORSO ==========
    $('#public-request-form').on('submit', function(e) {
        e.preventDefault();

        const requestData = {
            descrizione: $('#pub-descrizione').val(),
            indirizzo: $('#pub-indirizzo').val(),
            nome_segnalante: $('#pub-nome').val(),
            email_segnalante: $('#pub-email').val(),
            telefono_segnalante: $('#pub-telefono').val() || null,
            latitudine: null,
            longitudine: null,
            foto_url: null
        };

        // Disabilita il bottone per evitare invii multipli
        const btn = $(this).find('button[type="submit"]');
        btn.prop('disabled', true).text('Invio in corso...');

        createRequest(requestData)
            .done(function(response) {
                alert('✅ Richiesta inviata con successo!\n\n' +
                    'Controlla la tua email per confermare la richiesta.\n' +
                    'ID Richiesta: ' + response.id);
                $('#public-request-form')[0].reset();
            })
            .fail(function(xhr) {
                const errorMsg = xhr.responseJSON?.detail || xhr.responseText || 'Errore sconosciuto';
                alert('❌ Errore invio richiesta:\n' + errorMsg);
            })
            .always(function() {
                btn.prop('disabled', false).text('Invia Richiesta di Soccorso');
            });
    });

    // ========== GESTIONE LOGIN ==========
    $('#login-form').on('submit', function(e) {
        e.preventDefault();
        const email = $('#username').val();
        const password = $('#password').val();

        login(email, password)
            .done(function(response) {
                saveToken(response.token);
                showMainSection();
                alert('✅ Login effettuato con successo!\nBenvenuto ' + response.nome + ' ' + response.cognome);
            })
            .fail(function(xhr) {
                const errorMsg = xhr.responseJSON?.detail || xhr.responseText || 'Errore sconosciuto';
                alert('❌ Errore login:\n' + errorMsg);
            });
    });

    // ========== GESTIONE LOGOUT ==========
    $('#logout-btn').on('click', function() {
        if (confirm('Sei sicuro di voler uscire?')) {
            logout()
                .always(function() {
                    clearToken();
                    showPublicSection();
                    alert('✅ Logout effettuato');
                });
        }
    });

    // ========== OPERAZIONE 4: CARICA RICHIESTE FILTRATE ==========
    $('#load-requests').on('click', function() {
        const stato = $('#filter-tipo').val();

        getRequests(stato)
            .done(function(data) {
                displayRequests(data);
            })
            .fail(function(xhr) {
                const errorMsg = xhr.responseJSON?.detail || xhr.responseText || 'Errore sconosciuto';
                alert('❌ Errore caricamento richieste:\n' + errorMsg);
            });
    });

    // ========== OPERAZIONE 6: CARICA OPERATORI LIBERI ==========
    $('#load-operators').on('click', function() {
        getFreeOperators()
            .done(function(data) {
                displayOperators(data);
            })
            .fail(function(xhr) {
                const errorMsg = xhr.responseJSON?.detail || xhr.responseText || 'Errore sconosciuto';
                alert('❌ Errore caricamento operatori:\n' + errorMsg);
            });
    });

    // ========== FUNZIONI DI NAVIGAZIONE ==========

    function showPublicSection() {
        $('#public-section').show();
        $('#login-section').hide();
        $('#main-section').hide();
    }

    function showLoginSection() {
        $('#public-section').hide();
        $('#login-section').show();
        $('#main-section').hide();
    }

    function showMainSection() {
        $('#public-section').hide();
        $('#login-section').hide();
        $('#main-section').show();
    }

    // ========== VISUALIZZAZIONE DATI ==========

    function displayRequests(requests) {
        const container = $('#requests-container');
        container.empty();

        if (!requests || requests.length === 0) {
            container.html('<p class="no-data">Nessuna richiesta trovata</p>');
            return;
        }

        requests.forEach(function(req) {
            const createdDate = req.created_at ? new Date(req.created_at).toLocaleString('it-IT') : 'N/A';
            const convalidataDate = req.convalidata_at ? new Date(req.convalidata_at).toLocaleString('it-IT') : 'Non convalidata';

            container.append(`
                <div class="request-item">
                    <h3>Richiesta #${req.id}</h3>
                    <p><strong>Descrizione:</strong> ${req.descrizione}</p>
                    <p><strong>Indirizzo:</strong> ${req.indirizzo}</p>
                    <p><strong>Segnalante:</strong> ${req.nome_segnalante} (${req.email_segnalante})</p>
                    ${req.telefono_segnalante ? `<p><strong>Telefono:</strong> ${req.telefono_segnalante}</p>` : ''}
                    <p><strong>Stato:</strong> <span class="status-${req.stato}">${req.stato}</span></p>
                    <p><strong>Creata il:</strong> ${createdDate}</p>
                    <p><strong>Convalidata:</strong> ${convalidataDate}</p>
                    ${req.missione_id ? `<p><strong>Missione:</strong> #${req.missione_id}</p>` : ''}
                </div>
            `);
        });
    }

    function displayOperators(operators) {
        const container = $('#operators-container');
        container.empty();

        if (!operators || operators.length === 0) {
            container.html('<p class="no-data">Nessun operatore disponibile</p>');
            return;
        }

        operators.forEach(function(op) {
            const roles = op.roles ? op.roles.map(r => r.name).join(', ') : 'N/A';
            const disponibile = op.disponibile ? '✅ Disponibile' : '❌ Non disponibile';

            container.append(`
                <div class="operator-item">
                    <h3>${op.nome} ${op.cognome}</h3>
                    <p><strong>ID:</strong> ${op.id}</p>
                    <p><strong>Email:</strong> ${op.email}</p>
                    ${op.telefono ? `<p><strong>Telefono:</strong> ${op.telefono}</p>` : ''}
                    ${op.indirizzo ? `<p><strong>Indirizzo:</strong> ${op.indirizzo}</p>` : ''}
                    <p><strong>Ruoli:</strong> ${roles}</p>
                    <p><strong>Stato:</strong> ${disponibile}</p>
                </div>
            `);
        });
    }
});
