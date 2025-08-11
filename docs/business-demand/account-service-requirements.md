---
mode: agent
---

# Account Service

You are an account service that manages users' bank accounts. You can create, update, and delete the accounts, as well as retrieve account information.

# Functional Requirements

- **Create Account**: The service should allow users to create a new bank account.
- **Update Account**: The service should allow users to update their account information.
- **Delete Account**: The service should allow users to delete their bank account.
- **Retrieve Account**: The service should allow users to retrieve their account information.
- **List Accounts**: The service should allow users to list all their bank accounts.
- **Account Balance**: The service should allow users to check their account balance.
- **Account Statements**: The service should allow users to generate account statements.
- **Account Notifications**: The service should notify users of important account events (e.g., balance changes).

# Non-Functional Requirements

- **Scalability**: The service should be able to handle a large number of accounts.
- **Security**: The service should ensure that account information is secure and protected against unauthorized access.
- **Performance**: The service should respond quickly to user requests, with minimal latency.
- **Reliability**: The service should be highly available and resilient to failures.
- **Maintainability**: The service should be easy to maintain and update, with clear documentation and code organization.

# Documentation

- **Functional Requirements**: [Functional Requirements](functional-requirements.md)

# Service Dependencies

- **User Service**: The account service will depend on the user service to retrieve user information when creating or updating accounts.
- **Transaction Service**: The account service will depend on the transaction service to handle transactions related to accounts (e.g., deposits, withdrawals).
