
---

### **Transaction Microservice (README)**

![image](https://github.com/user-attachments/assets/9d0b552f-e77d-4668-9a60-0b8a21c1acd5)


```markdown
# Transaction Microservice

## Description
Ce microservice gère les opérations bancaires telles que les virements, les retraits, les versements, et les paiements de factures. Il utilise Kafka pour communiquer avec le microservice Accounts.

## Fonctionnalités
- Gestion des virements et des paiements.
- Vérification des soldes des comptes.

## Dépendances
- Spring Boot
- Kafka
- Eureka Client

## Configuration
- Configurez Kafka dans `application.yml`.

## Lancer le service
```bash
mvn clean install
java -jar target/transaction-service.jar
