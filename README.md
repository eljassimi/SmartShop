# SmartShop – API REST de Gestion Commerciale B2B

![YouCode](https://img.shields.io/badge/YouCode-Simplon%20Maghreb-red?style=flat-square)
![Java 17](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7%2B-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13%2B-0066cc)
![Liquibase](https://img.shields.io/badge/Liquibase-4.20-blue)

API REST complète développée pour **MicroTech Maroc** – Distributeur B2B de matériel informatique à Casablanca  
Gestion de 650+ clients avec fidélité progressive & paiements fractionnés multi-moyens

Aucun frontend – Tests via **Swagger UI** ou **Postman**

## Fonctionnalités Implémentées (100 % conformes au cahier des charges)

| Fonctionnalité                        | Statut  | Détails |
|--------------------------------------|--------|--------|
| Gestion Clients (CRUD + stats auto)   | Done    | Nombre de commandes, montant cumulé, dates 1ère/dernière commande |
| Système de fidélité automatique      | Done    | BASIC → SILVER → GOLD → PLATINUM avec seuils exacts |
| Remises progressives conditionnelles  | Done    | 5 % ≥500 DH | 10 % ≥800 DH | 15 % ≥1200 DH |
| Gestion Produits + soft delete        | Done    | Pagination & filtres avancés |
| Commandes multi-produits              | Done    | Vérification stock, calcul remise + TVA 20 % après remise |
| Codes promo PROMO-XXXX (usage unique) | Done    | +5 % supplémentaire |
| Paiements fractionnés multi-moyens    | Done    | Espèces / Chèque / Virement (Strategy Pattern) |
| Validation CONFIRMED uniquement si totalement payée | Done    | montant_restant = 0 |
| Authentification HTTP Session         | Done    | Login/Logout manuel – Aucun JWT ni Spring Security |
| Autorisations fines (ADMIN / CLIENT)  | Done    | Annotations personnalisées @RequireRole |
| Gestion centralisée des erreurs      | Done    | @ControllerAdvice → 400/401/403/404/422 avec JSON structuré |

## Architecture & Organisation du Code

```
src/main/java/io/github/tawdi/smartshop/
├── auth/               → Authentification Session + filtres + annotations @RequireRole
├── config/             → OpenApiConfig (Swagger)
├── controller/         → Tous les endpoints REST
├── domain/
│   ├── entity/         → JPA Entities (Client, Order, Payment, Product…)
│   └── repository/     → Repositories génériques (LongRepository / StringRepository)
├── dto/                → Request/Response DTOs organisés par domaine
├── enums/              → CustomerTier, OrderStatus, PaymentType, UserRole…
├── exception/          → Exceptions métier + GlobalExceptionHandler
├── mapper/             → MapStruct (conversion Entity ↔ DTO)
├── service/
│   ├── implementation/ → Implémentations concrètes
│   └── payment/strategy/ → Strategy Pattern (Cash, Cheque, Transfer)
├── specification/     → Spécifications JPA pour filtres dynamiques
└── util/               → TierHelper, PasswordUtil
```

## Technologies & Bonnes Pratiques

- Java 17 + Spring Boot 2.7+
- Spring Data JPA + Hibernate
- PostgreSQL + **Liquibase** (migrations versionnées)
- Lombok + MapStruct
- Strategy Pattern pour les moyens de paiement
- Filtres Servlet personnalisés (AuthFilter + AuthorizationFilter)
- Annotations @RequireRole + enum Role
- Tests unitaires (JUnit 5 + Mockito) en cours d’ajout
- Swagger UI intégré

## Lancement Rapide

```bash
# 1. Clone
git clone https://github.com/tawdi/smartshop.git
cd smartshop

# 2. Base de données PostgreSQL
createdb smartshop

# 3. Lancer
./mvnw spring-boot:run
```

Application disponible sur → `http://localhost:8080`

### Documentation API
- OpenAPI JSON : http://localhost:8080/v3/api-docs

### Comptes de Test

| Username | Password  | Rôle   |
|----------|-----------|--------|
| admin    | admin123  | ADMIN  |
| client1  | password  | CLIENT |
| client2  | password  | CLIENT |
| client3  | password  | CLIENT |
| client4  | password  | CLIENT |
| client5  | password  | CLIENT |


## Diagramme de Classes UML
![Diagramme UML](docs/smartshop.drawio)

## Captures Swagger (quelques exemples)

- POST   `/api/auth/login`
- GET    `/api/clients/me` (CLIENT)
- POST   `/api/orders` (création commande + calcul automatique)
- POST   `/api/payments` (paiement fractionné)
- PATCH  `/api/orders/{id}/confirm` (ADMIN uniquement)

## Livrables Fournis

- Code source complet (GitHub)
- Diagramme de classes UML (docs/)
- Migrations Liquibase versionnées
- README détaillé (celui-ci !)
- Base de données prête avec données de démonstration (5+ enregistrements par table)
