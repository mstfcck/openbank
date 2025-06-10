# Functional Requirements

## Overview

- The project will be a simple banking service.
- The project is divided into three domains (service): Account, Transaction, and User.
- Each domain projects must have the same folder structure and must be created a domain object for each service with the implementation, with the CRUD endpoints using RESTful best practices.
- The API will expose an endpoint which accepts the user information (customerID,
initialCredit).
- Once the endpoint is called, a new account will be opened connected to the user whose ID is customerID.
- Also, if initialCredit is not 0, a transaction will be sent to the new account.
- Another Endpoint will output the user information showing FirstName, LastName, balance,
and transactions of the accounts.

## Account Service

### Account Entity

```
- Id
- UserId
```

## Transaction Service

## Transaction Entity

```
- Id
- AccountId
- Amount
- Type (TransactionType)
- Data
```

## Transaction Type Enum

```
- Deposit = 1
- Withdraw = 2
```

## User Service

### User Entity

```
- Id
- FirstName
- LastName
```

## Shared

## Gateway
