# Transaction Service

Transaction Service for the OpenBank microservices application. This service handles all financial transactions including deposits, withdrawals, transfers, payments, and refunds.

## Features

- **Transaction Types**: Support for DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, and REFUND operations
- **Transaction Status Management**: Track transactions through PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, and REVERSED states
- **Account Integration**: Seamless integration with Account Service for balance validation and updates
- **Comprehensive Validation**: Business rule validation for all transaction types
- **Transaction History**: Complete audit trail with timestamps and user tracking
- **Statistics and Reporting**: Transaction statistics and account-specific reports
- **Error Handling**: Robust error handling with detailed error messages
- **Retry Mechanism**: Automatic retry for failed transactions
- **Admin Operations**: Administrative endpoints for transaction management

## Architecture

The service follows a layered architecture:

- **Controller Layer**: REST endpoints for transaction operations
- **Service Layer**: Business logic and transaction processing
- **Repository Layer**: Data access using Spring Data JPA
- **Entity Layer**: JPA entities with audit fields
- **DTO Layer**: Data Transfer Objects for API communication
- **Client Layer**: Integration with external services (Account Service)

## Key Components

### Transaction Entity
Core entity representing financial transactions with:
- Amount and currency
- Transaction type and status
- Source and destination accounts
- Fee handling
- Audit trail (creation, modification timestamps and users)
- Business validation methods

### Transaction Types
- **DEPOSIT**: Money added to an account from external source
- **WITHDRAWAL**: Money removed from an account to external destination  
- **TRANSFER**: Money moved between accounts within the system
- **PAYMENT**: Payment made from account to merchant/service
- **REFUND**: Refund of a previous transaction

### Transaction Status
- **PENDING**: Transaction initiated but not processed
- **PROCESSING**: Transaction currently being processed
- **COMPLETED**: Transaction successfully completed
- **FAILED**: Transaction failed due to business rules or errors
- **CANCELLED**: Transaction cancelled before completion
- **REVERSED**: Transaction reversed due to error or fraud

## API Endpoints

### Core Operations
- `POST /api/transactions` - Create new transaction
- `GET /api/transactions/{id}` - Get transaction by ID
- `GET /api/transactions/reference/{reference}` - Get transaction by reference
- `GET /api/transactions` - Get all transactions with pagination

### Account-Specific Operations
- `GET /api/transactions/account/{accountId}` - Get transactions for account
- `GET /api/transactions/account/{accountId}/paged` - Get account transactions with pagination
- `GET /api/transactions/account/{accountId}/date-range` - Get account transactions in date range
- `GET /api/transactions/account/{accountId}/statistics` - Get account transaction statistics

### Query Operations
- `GET /api/transactions/status/{status}` - Get transactions by status
- `GET /api/transactions/type/{type}` - Get transactions by type
- `GET /api/transactions/date-range` - Get transactions in date range

### Management Operations
- `POST /api/transactions/{id}/cancel` - Cancel pending transaction
- `POST /api/transactions/{id}/retry` - Retry failed transaction
- `GET /api/transactions/statistics` - Get overall transaction statistics

### Administrative Operations
- `POST /api/transactions/admin/process-pending` - Process pending transactions
- `POST /api/transactions/admin/cleanup-old-pending` - Clean up old pending transactions

## Configuration

### Database
- Uses H2 in-memory database for development
- JPA/Hibernate for data persistence
- Automatic schema generation and SQL logging

### External Services
- **Account Service**: http://localhost:8092 (configurable via ACCOUNT_SERVICE_URL)
- **User Service**: http://localhost:8091 (configurable via USER_SERVICE_URL)

### Server
- Default port: 8093 (configurable via PORT environment variable)
- Context path: /
- Actuator endpoints enabled for monitoring

## Transaction Processing Flow

1. **Validation**: Request validation and business rule checks
2. **Account Verification**: Verify accounts exist and can participate
3. **Transaction Creation**: Create transaction in PENDING state
4. **Processing**: Execute the financial operation
5. **Account Updates**: Update account balances via Account Service
6. **Completion**: Mark transaction as COMPLETED or FAILED

## Business Rules

### Deposits
- Must have destination account
- Cannot have source account
- Amount must be positive

### Withdrawals  
- Must have source account
- Cannot have destination account
- Source account must have sufficient funds (including overdraft)

### Transfers
- Must have both source and destination accounts
- Accounts must be different
- Source account must have sufficient funds
- Both accounts must be active

### Payments
- Must have source account
- Source account must have sufficient funds
- Optional destination account for tracking

### Refunds
- Must have destination account
- Destination account must be active

## Error Handling

The service provides comprehensive error handling:

- **Validation Errors**: Invalid request data or business rule violations
- **Account Errors**: Account not found or insufficient funds
- **Service Errors**: External service communication failures
- **Processing Errors**: Transaction processing failures

All errors include detailed messages and appropriate HTTP status codes.

## Monitoring and Observability

- **Health Checks**: `/actuator/health` endpoint
- **Metrics**: Prometheus metrics via `/actuator/metrics`
- **Logging**: Structured logging with correlation IDs
- **OpenAPI Documentation**: Swagger UI available at `/swagger-ui.html`

## Security

- Input validation using Bean Validation
- SQL injection prevention through JPA/JPQL
- Cross-site scripting (XSS) protection
- Secure error messages without sensitive data exposure

## Development

### Prerequisites
- Java 17+
- Maven 3.6+
- Running Account Service on port 8092

### Running the Service
```bash
./mvnw spring-boot:run
```

### Running Tests
```bash
./mvnw test
```

### Building
```bash
./mvnw clean package
```

## Database Schema

The service creates the following main table:

### transactions
- `id` (Primary Key)
- `from_account_id` (Source account)
- `to_account_id` (Destination account)  
- `amount` (Transaction amount)
- `transaction_type` (Type enum)
- `status` (Status enum)
- `description` (Description text)
- `reference` (Unique reference)
- `currency` (Currency code)
- `fee` (Transaction fee)
- `processed_at` (Processing timestamp)
- `error_message` (Error details if failed)
- Audit fields from BaseEntity (created_at, updated_at, created_by, updated_by, version)

### Indexes
- from_account_id, to_account_id (for account queries)
- reference (unique, for lookups)
- status, transaction_type (for filtering)
- created_at (for date range queries)

## Environment Variables

- `PORT` - Server port (default: 8093)
- `SPRING_PROFILES_ACTIVE` - Active profile (default: dev)
- `ACCOUNT_SERVICE_URL` - Account service URL
- `USER_SERVICE_URL` - User service URL

## Troubleshooting

### Common Issues

1. **Service won't start**: Check if port 8093 is available
2. **Account service communication failed**: Verify Account Service is running on correct port
3. **Transaction processing errors**: Check account service logs for balance/validation issues
4. **Database connection issues**: Verify H2 configuration and memory settings

### Logging

Enable debug logging for troubleshooting:
```yaml
logging:
  level:
    '[com.openbank.transactionservice]': DEBUG
```

## Contributing

1. Follow existing code patterns and architecture
2. Add unit tests for new functionality  
3. Update API documentation
4. Ensure proper error handling
5. Add appropriate logging
