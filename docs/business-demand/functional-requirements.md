# Functional Requirements

## Overview

- The project will be a simple banking service.
- The project is divided into three domains (service): Account, Transaction, and User.
- Each domain projects must have the same folder structure and must be created a domain object for each service with the implementation, with the CRUD endpoints using RESTful best practices.

### Basic Requirements

- The API will expose an endpoint which accepts the user information (customerID, initialCredit).
- Once the endpoint is called, a new account will be opened connected to the user whose ID is customerID.
- Also, if initialCredit is not 0, a transaction will be sent to the new account.
- Another Endpoint will output the user information showing FirstName, LastName, balance, and transactions of the accounts.

## Account Service

### Overview

The "Account" application will be an application that contains business logic related to users' (customers') bank accounts.

### Account Entity

```
- Id
- UserId
```

## Transaction Service

### Overview

The "Transaction" application will be an application that includes the business logic related to deposits, withdrawals and account-to-account money transfers made by users (customers) in their bank accounts.

### Transaction Entity

```
- Id
- AccountId
- Amount
- Type (TransactionType)
- Data
```

### Transaction Type Enum

```
- Deposit = 1
- Withdraw = 2
```

## User Service

### Overview

The "User" application will be an application that contains business logic related to users (customers).

### User Entity

```
- Id
- FirstName
- LastName
```

## Shared

### Overview

The "Shared" application will be an application that includes all common code implementations to use across services as a package.

## Gateway

### Overview

The "Gateway" application will be an application that manages all API calls across the services.