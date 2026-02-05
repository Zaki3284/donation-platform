# Documentation du Projet - Donation Platform

## 1) Architecture Globale
Le projet suit une architecture en couches avec Spring Boot.

- **Controller**  
  Expose les endpoints REST et reçoit les requêtes HTTP.

- **Service**  
  Contient la logique métier (création de dons, règles d’accès, calculs).

- **Repository**  
  Accès aux données via Spring Data JPA.

- **Entity**  
  Modèles JPA représentant les tables de la base de données.

- **DTO**  
  Objets utilisés pour échanger des données propres avec le frontend.

- **Security**  
  JWT + rôles (ADMIN / DONATEUR) pour sécuriser l’accès aux endpoints.

---

## 2) Sécurité
Le système utilise JWT pour l’authentification et `@PreAuthorize` pour l’autorisation.

- Endpoints publics:
  - `/api/auth/**`
  - `/api/campaigns/**`

- Endpoints protégés:
  - `/api/donations/**` (DONATEUR)
  - `/api/users/**` (ADMIN)
  - `/api/statistics/**` (ADMIN)

---

## 3) Tests
Des tests d’intégration existent avec MockMvc et base H2 (profil `test`).

Tests couverts:
- Login et Register
- Accès public aux campagnes
- Création de dons par DONATEUR
- Création de campagnes par ADMIN
- Refus d’accès non autorisé
- Accès protégé avec JWT

---

## 4) Endpoints + Requêtes / Réponses

### Authentification

**POST** `/api/auth/login`

Requête:
```json
{
  "email": "test@example.com",
  "password": "test1234"
}
```

Réponse:
```json
{
  "user": {
    "id": "2",
    "name": "Test User",
    "email": "test@example.com",
    "role": "DONOR"
  },
  "token": "JWT_TOKEN"
}
```

**POST** `/api/auth/register`

Requête:
```json
{
  "name": "New User",
  "email": "newuser@example.com",
  "password": "newpass123"
}
```

Réponse:
```json
{
  "user": {
    "id": "3",
    "name": "New User",
    "email": "newuser@example.com",
    "role": "DONOR"
  },
  "token": "JWT_TOKEN"
}
```

---

### Campagnes

**GET** `/api/campaigns` (Public)

Réponse:
```json
[
  {
    "id": "1",
    "title": "Campagne 1",
    "description": "Description",
    "goalAmount": 1000.0,
    "currentAmount": 250.0,
    "startDate": "2026-02-01",
    "endDate": "2026-03-01",
    "imageUrl": "https://via.placeholder.com/400x300",
    "status": "ACTIVE"
  }
]
```

**GET** `/api/campaigns/active` (Public)

**GET** `/api/campaigns/{id}` (Public)

**POST** `/api/campaigns` (ADMIN)

Header:
```
Authorization: Bearer JWT_TOKEN
```

Requête:
```json
{
  "title": "Nouvelle Campagne",
  "description": "Aide sociale",
  "goalAmount": 5000,
  "startDate": "2026-02-01",
  "endDate": "2026-03-01",
  "status": "ACTIVE"
}
```

Réponse:
```json
{
  "id": "10",
  "title": "Nouvelle Campagne",
  "description": "Aide sociale",
  "goalAmount": 5000,
  "currentAmount": 0,
  "startDate": "2026-02-01",
  "endDate": "2026-03-01",
  "imageUrl": "https://via.placeholder.com/400x300",
  "status": "ACTIVE"
}
```

**DELETE** `/api/campaigns/{id}` (ADMIN)

Réponse:
```json
{ "message": "Deleted" }
```

---

### Dons

**POST** `/api/donations` (DONATEUR)

Header:
```
Authorization: Bearer JWT_TOKEN
```

Requête:
```json
{
  "campaignId": "1",
  "amount": 50.0
}
```

Réponse:
```json
{
  "id": "20",
  "amount": 50.0,
  "donorId": "2",
  "donorName": "Test User",
  "campaignId": "1",
  "campaignTitle": "Campagne 1",
  "date": "2026-02-05T18:00:00",
  "receiptId": "REC-20"
}
```

**GET** `/api/donations/my` (DONATEUR)

Réponse:
```json
[
  {
    "id": "20",
    "amount": 50.0,
    "donorId": "2",
    "donorName": "Test User",
    "campaignId": "1",
    "campaignTitle": "Campagne 1",
    "date": "2026-02-05T18:00:00",
    "receiptId": "REC-20"
  }
]
```

---

### Utilisateurs

**GET** `/api/users` (ADMIN)

Réponse:
```json
[
  { "id": "2", "name": "Test User", "email": "test@example.com", "role": "DONOR" }
]
```

---

### Statistiques

**GET** `/api/statistics` (ADMIN)

Réponse:
```json
{
  "totalFundsRaised": 1000.0,
  "totalDonationsCount": 5,
  "activeCampaignsCount": 2,
  "donationsByMonth": [
    { "month": "Jan", "amount": 300.0 }
  ],
  "campaignPerformance": [
    { "name": "Campagne 1", "percentage": 50.0 }
  ]
}
```

---

### Reçus

**GET** `/api/receipts/donation/{donationId}`

Réponse:
```json
{
  "id": "1",
  "dateGeneration": "2026-02-05T18:00:00",
  "filePath": "receipts/receipt_donation_20.pdf"
}
```
