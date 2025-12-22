const API_BASE_URL = '/swa';

// Utility per gestire il token
function getToken() {
    return localStorage.getItem('auth_token');
}

function saveToken(token) {
    localStorage.setItem('auth_token', token);
}

function clearToken() {
    localStorage.removeItem('auth_token');
}

// Utility per includere il token nelle richieste
function getAuthHeaders() {
    const token = getToken();
    return token ? { 'Authorization': `Bearer ${token}` } : {};
}

// ========== OPERAZIONE 1: LOGIN/LOGOUT ==========
function login(email, password) {
    return $.ajax({
        url: `${API_BASE_URL}/open/auth/login`,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            email: email,
            password: password
        }),
        dataType: 'json'
    });
}

function logout() {
    return $.ajax({
        url: `${API_BASE_URL}/open/auth/logout`,
        method: 'POST',
        headers: getAuthHeaders()
    });
}

// ========== OPERAZIONE 2: INSERIMENTO RICHIESTA ==========
function createRequest(requestData) {
    return $.ajax({
        url: `${API_BASE_URL}/open/richiesta/nuova-richiesta`,
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(requestData),
        dataType: 'json'
    });
}

// ========== OPERAZIONE 4: LISTA RICHIESTE FILTRATE ==========
function getRequests(stato) {
    return $.ajax({
        url: `${API_BASE_URL}/api/richieste/richieste-filtrate`,
        method: 'GET',
        headers: getAuthHeaders(),
        data: { stato: stato },
        dataType: 'json'
    });
}

// ========== OPERAZIONE 6: OPERATORI DISPONIBILI ==========
function getFreeOperators() {
    return $.ajax({
        url: `${API_BASE_URL}/api/operatori/disponibile`,
        method: 'GET',
        headers: getAuthHeaders(),
        dataType: 'json'
    });
}

// ========== FUNZIONI AGGIUNTIVE (opzionali) ==========

// Dettagli richiesta
function getRequestDetails(id) {
    return $.ajax({
        url: `${API_BASE_URL}/api/richieste/dettagli/${id}`,
        method: 'GET',
        headers: getAuthHeaders(),
        dataType: 'json'
    });
}

// Dettagli operatore
function getOperatorDetails(id) {
    return $.ajax({
        url: `${API_BASE_URL}/api/operatori/dettagli/${id}`,
        method: 'GET',
        headers: getAuthHeaders(),
        dataType: 'json'
    });
}

// Modifica stato richiesta
function modificaStatoRichiesta(id, nuovoStato) {
    return $.ajax({
        url: `${API_BASE_URL}/api/richieste/modifica-stato/${id}/${nuovoStato}`,
        method: 'PUT',
        headers: getAuthHeaders()
    });
}
