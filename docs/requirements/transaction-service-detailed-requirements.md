# Transaction Service - Detailed Requirements Specification

## 1. Executive Summary

The Transaction Service is a critical microservice in the OpenBank application responsible for processing and managing all financial transactions. It provides comprehensive transaction lifecycle management including creation, processing, tracking, and reporting. The service integrates with the Account Service for balance validation and updates, implementing sophisticated business rules for various transaction types.

### 1.1 Service Overview

- **Service Name**: Transaction Service
- **Port**: 8093  
- **Database**: PostgreSQL with H2 in-memory for development
- **External Dependencies**: Account Service (port 8092), User Service (port 8091)
- **Authentication**: Basic Authentication implemented

## 2. Functional Requirements

### 2.1 Transaction Processing (FR-TRA-001 to FR-TRA-006)

#### FR-TRA-001: Transaction Creation and Validation

**Priority**: HIGH  
**Epic**: Transaction Lifecycle Management

**Description**: The system SHALL allow creation and validation of financial transactions with comprehensive business rule enforcement.

**Detailed Requirements**:

- System SHALL support transaction types: DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, REFUND
- System SHALL validate transaction requests against business rules
- System SHALL verify account existence via Account Service integration
- System SHALL check account eligibility for transaction operations
- System SHALL validate sufficient funds for debit operations
- System SHALL create transactions in PENDING status initially
- System SHALL support transaction descriptions and reference numbers
- System SHALL validate currency consistency across accounts

**Business Rules**:

- WITHDRAWAL transactions require sufficient account balance plus overdraft
- TRANSFER transactions require valid source and destination accounts
- PAYMENT transactions require merchant validation
- Minimum transaction amount is 0.01 in account currency
- Maximum transaction limits based on account type and customer profile
- Source and destination accounts cannot be identical for transfers

**API Endpoint**: `POST /api/transactions`

**Acceptance Criteria**:

- Given valid transaction request, when creating transaction, then transaction is created with PENDING status
- Given insufficient funds, when creating withdrawal, then InvalidTransactionOperationException is thrown
- Given invalid account, when creating transaction, then AccountNotFoundException is thrown
- Given same source/destination accounts, when creating transfer, then InvalidTransactionOperationException is thrown

#### FR-TRA-002: Transaction Processing Engine

**Priority**: HIGH  
**Epic**: Transaction Processing

**Description**: The system SHALL process transactions through a secure, atomic workflow ensuring data consistency.

**Detailed Requirements**:

- System SHALL process transactions in sequential order
- System SHALL update transaction status through defined states: PENDING → PROCESSING → COMPLETED/FAILED
- System SHALL update account balances via Account Service API calls
- System SHALL implement atomic operations for transfer transactions
- System SHALL handle concurrent transaction processing safely
- System SHALL implement retry mechanisms for failed transactions
- System SHALL support transaction reversal for error correction
- System SHALL maintain transaction processing audit trail

**Business Rules**:

- Account balance updates must be atomic with transaction status changes
- Failed transactions must not affect account balances
- Transaction processing must complete within 30 seconds or timeout
- Retry attempts limited to 3 times with exponential backoff

**Processing Flow**:
1. **Validation**: Request validation and business rule checks
2. **Account Verification**: Verify accounts exist and can participate  
3. **Transaction Creation**: Create transaction in PENDING state
4. **Processing**: Execute the financial operation
5. **Account Updates**: Update account balances via Account Service
6. **Completion**: Mark transaction as COMPLETED or FAILED

**API Endpoints**:

- `PUT /api/transactions/{id}/process` - Process pending transaction
- `POST /api/transactions/{id}/retry` - Retry failed transaction
- `POST /api/transactions/{id}/reverse` - Reverse completed transaction

**Acceptance Criteria**:

- Given PENDING transaction, when processing, then status changes to PROCESSING then COMPLETED
- Given processing failure, when transaction fails, then status changes to FAILED and balances unchanged
- Given failed transaction, when retrying, then transaction reprocesses with new attempt logged

#### FR-TRA-003: Transaction Retrieval and Search

**Priority**: HIGH  
**Epic**: Transaction Information Access

**Description**: The system SHALL provide comprehensive transaction retrieval and search capabilities.

**Detailed Requirements**:

- System SHALL allow transaction retrieval by transaction ID
- System SHALL support transaction search by account ID
- System SHALL support transaction search by user ID with account aggregation
- System SHALL provide transaction filtering by date range, amount range, type, status
- System SHALL support pagination for large result sets
- System SHALL provide sorting options (date, amount, type, status)
- System SHALL include related account information in transaction details
- System SHALL support real-time transaction status tracking

**API Endpoints**:

- `GET /api/transactions/{id}` - Get transaction by ID
- `GET /api/transactions/account/{accountId}` - Get account transactions
- `GET /api/transactions/user/{userId}` - Get user transactions across all accounts
- `GET /api/transactions/search` - Advanced transaction search with filters

**Acceptance Criteria**:

- Given valid transaction ID, when retrieving transaction, then complete transaction details returned
- Given account ID, when retrieving transactions, then all account transactions returned with pagination
- Given filter criteria, when searching transactions, then filtered results returned
- Given sort parameters, when retrieving transactions, then results sorted accordingly

#### FR-TRA-004: Transaction Status Management

**Priority**: MEDIUM  
**Epic**: Transaction Lifecycle Management

**Description**: The system SHALL support transaction status transitions and lifecycle management.

**Detailed Requirements**:

- System SHALL support transaction statuses: PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, REVERSED
- System SHALL allow transaction cancellation only in PENDING status
- System SHALL support transaction reversal for COMPLETED transactions
- System SHALL track status change history with timestamps
- System SHALL notify relevant parties of status changes
- System SHALL implement status-based business rule enforcement

**Business Rules**:

- Only PENDING transactions can be cancelled
- Only COMPLETED transactions can be reversed
- Status changes must be logged with reason and timestamp
- Reversed transactions create offsetting transactions

**API Endpoints**:

- `PUT /api/transactions/{id}/cancel` - Cancel pending transaction
- `PUT /api/transactions/{id}/status` - Update transaction status (admin only)
- `GET /api/transactions/{id}/history` - Get transaction status history

**Acceptance Criteria**:

- Given PENDING transaction, when cancelling, then status changes to CANCELLED
- Given COMPLETED transaction, when reversing, then offsetting transaction created
- Given status change, when updating transaction, then change logged with audit trail

#### FR-TRA-005: Batch Transaction Processing

**Priority**: MEDIUM  
**Epic**: Batch Operations

**Description**: The system SHALL support efficient batch processing of multiple transactions.

**Detailed Requirements**:

- System SHALL accept batch transaction creation requests
- System SHALL process batch transactions atomically
- System SHALL provide batch processing status tracking
- System SHALL support partial batch failure handling
- System SHALL generate batch processing reports
- System SHALL optimize database operations for batch processing

**Business Rules**:

- Batch size limited to 1000 transactions per request
- Batch processing must complete within 5 minutes
- Failed transactions in batch do not affect successful ones
- Batch processing requires elevated privileges

**API Endpoints**:

- `POST /api/transactions/batch` - Submit batch transactions
- `GET /api/transactions/batch/{batchId}` - Get batch status
- `GET /api/transactions/batch/{batchId}/report` - Get batch report

#### FR-TRA-006: Transaction Analytics and Statistics

**Priority**: LOW  
**Epic**: Reporting and Analytics

**Description**: The system SHALL provide transaction analytics and statistical reporting.

**Detailed Requirements**:

- System SHALL calculate account transaction statistics
- System SHALL provide transaction volume and value analytics
- System SHALL support date-range analysis
- System SHALL calculate net transaction amounts
- System SHALL provide transaction type distribution analytics
- System SHALL support export functionality for reports

**API Endpoints**:

- `GET /api/transactions/analytics/account/{accountId}` - Account transaction statistics
- `GET /api/transactions/analytics/volume` - Transaction volume analytics
- `GET /api/transactions/analytics/summary` - Overall transaction summary

### 2.2 Administrative Operations (FR-TRA-007 to FR-TRA-010)

#### FR-TRA-007: Transaction Monitoring and Management

**Priority**: MEDIUM  
**Epic**: Administrative Operations

**Description**: The system SHALL provide administrative tools for transaction monitoring and management.

**Detailed Requirements**:

- System SHALL provide transaction monitoring dashboard data
- System SHALL support administrative transaction operations
- System SHALL allow forced transaction state changes
- System SHALL provide transaction reconciliation tools
- System SHALL support transaction audit trail viewing

**API Endpoints**:

- `GET /api/transactions/admin/pending` - Get all pending transactions
- `GET /api/transactions/admin/failed` - Get all failed transactions
- `POST /api/transactions/admin/{id}/force-complete` - Force transaction completion

#### FR-TRA-008: Scheduled Transaction Processing

**Priority**: LOW  
**Epic**: Automation

**Description**: The system SHALL support scheduled and automated transaction processing.

**Detailed Requirements**:

- System SHALL process pending transactions automatically
- System SHALL clean up old pending transactions
- System SHALL generate periodic transaction reports
- System SHALL monitor transaction processing health

**Scheduled Jobs**:

- Process pending transactions every 1 minute
- Clean up old pending transactions daily  
- Generate daily transaction summary reports

#### FR-TRA-009: Transaction Validation Services

**Priority**: HIGH  
**Epic**: Service Integration

**Description**: The system SHALL provide validation services for transaction operations.

**Detailed Requirements**:

- System SHALL validate transaction eligibility before processing
- System SHALL check account transaction limits
- System SHALL validate transaction business rules
- System SHALL provide transaction pre-validation endpoint

#### FR-TRA-010: Transaction Reconciliation

**Priority**: MEDIUM  
**Epic**: Financial Operations

**Description**: The system SHALL support transaction reconciliation processes.

**Detailed Requirements**:

- System SHALL provide transaction reconciliation reports
- System SHALL identify discrepancies in transaction processing
- System SHALL support manual reconciliation adjustments
- System SHALL maintain reconciliation audit trails

## 3. Non-Functional Requirements

### 3.1 Performance Requirements

#### NFR-TRA-001: Response Time

- Transaction creation SHALL respond within 500ms for 95% of requests
- Transaction retrieval SHALL respond within 200ms for 95% of requests  
- Transaction processing SHALL complete within 2 seconds for 95% of requests
- Batch processing SHALL handle 1000 transactions within 5 minutes

#### NFR-TRA-002: Throughput

- System SHALL support minimum 500 concurrent transaction operations
- System SHALL handle peak load of 5,000 transactions per minute
- Database connection pool SHALL be optimized for transaction volume
- Account Service integration SHALL support required transaction volume

#### NFR-TRA-003: Scalability

- System SHALL support horizontal scaling across multiple instances
- Transaction processing SHALL be stateless to enable scaling
- Database queries SHALL be optimized with proper indexing
- Message queuing SHALL be considered for high-volume scenarios

### 3.2 Security Requirements

#### NFR-TRA-004: Authentication and Authorization

- All API endpoints SHALL require valid authentication (Basic Auth implemented)
- Administrative operations SHALL require elevated privileges
- Transaction operations SHALL validate user authorization for accounts
- Sensitive transaction data SHALL be protected with encryption

#### NFR-TRA-005: Data Protection

- Transaction amounts SHALL be stored with precision to prevent rounding errors
- Transaction data SHALL be encrypted at rest
- Financial data transmission SHALL use encrypted channels
- Transaction references SHALL be generated with sufficient entropy

#### NFR-TRA-006: Audit and Compliance

- All transaction operations SHALL be logged with complete audit trail
- Transaction processing SHALL maintain immutable transaction history
- System SHALL support regulatory reporting requirements
- Failed transactions SHALL be logged with detailed error information

### 3.3 Reliability Requirements

#### NFR-TRA-007: Availability

- Service SHALL maintain 99.9% uptime during business hours
- System SHALL implement graceful degradation for Account Service outages
- Transaction processing SHALL be resumable after system restart
- Critical transaction data SHALL be backed up continuously

#### NFR-TRA-008: Data Consistency

- Transaction processing SHALL be ACID compliant
- Account balance updates SHALL be atomic with transaction status
- System SHALL prevent double-spending and race conditions
- Transaction state SHALL be consistent across service restarts

#### NFR-TRA-009: Error Handling

- System SHALL implement comprehensive error handling for all scenarios
- Failed transactions SHALL be automatically retried with exponential backoff
- System SHALL provide meaningful error messages for troubleshooting
- Circuit breaker pattern SHALL prevent cascade failures

### 3.4 Integration Requirements

#### NFR-TRA-010: Service Dependencies

- System SHALL gracefully handle Account Service temporary unavailability
- Service discovery SHALL automatically configure Account Service endpoints
- Circuit breaker pattern SHALL be implemented for external service calls
- Configuration management SHALL support multiple environment deployments

#### NFR-TRA-011: API Compatibility

- RESTful API SHALL follow OpenAPI 3.0.4 specification
- Backward compatibility SHALL be maintained for API versions
- Error responses SHALL follow consistent format across all endpoints
- API rate limiting SHALL be implemented to prevent abuse

## 4. Technical Specifications

### 4.1 Technology Stack

- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL (production), H2 (development/testing)
- **ORM**: JPA/Hibernate
- **Security**: Spring Security with Basic Authentication
- **Documentation**: OpenAPI 3.0.4
- **Testing**: JUnit 5, Mockito, TestContainers
- **Integration**: RestTemplate for Account Service communication

### 4.2 Database Schema

```sql
-- Transaction Entity
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    transaction_type VARCHAR(20) NOT NULL, -- DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, REFUND
    amount DECIMAL(19,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    description VARCHAR(500),
    reference_number VARCHAR(100) UNIQUE,
    from_account_id BIGINT,
    to_account_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED, REVERSED
    fee_amount DECIMAL(19,2) DEFAULT 0.00,
    exchange_rate DECIMAL(19,6) DEFAULT 1.000000,
    processed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0
);

-- Indexes for performance  
CREATE INDEX idx_transactions_from_account ON transactions(from_account_id);
CREATE INDEX idx_transactions_to_account ON transactions(to_account_id);
CREATE INDEX idx_transactions_status ON transactions(status);
CREATE INDEX idx_transactions_type ON transactions(transaction_type);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);
CREATE INDEX idx_transactions_reference ON transactions(reference_number);

-- Combined index for common queries
CREATE INDEX idx_transactions_account_date ON transactions(from_account_id, created_at);
CREATE INDEX idx_transactions_to_account_date ON transactions(to_account_id, created_at);
```

### 4.3 Configuration Management

```yaml
# Service Configuration
server:
  port: 8093

# External Service URLs  
app:
  services:
    account-service:
      url: ${ACCOUNT_SERVICE_URL:http://localhost:8092}  # Fixed from 8090
    user-service:
      url: ${USER_SERVICE_URL:http://localhost:8091}

# Security Configuration
spring:
  security:
    user:
      name: ${SECURITY_USERNAME:admin}
      password: ${SECURITY_PASSWORD:password}

# Database Configuration
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/transactiondb}
    username: ${DATABASE_USERNAME:postgres}  
    password: ${DATABASE_PASSWORD:postgres}
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
```

### 4.4 Account Service Integration

The Transaction Service integrates with Account Service through AccountServiceClient:

**Key Integration Methods**:

- `getAccount(Long accountId)` - Fetch account details
- `canDebit(Long accountId, BigDecimal amount)` - Validate debit capability  
- `canCredit(Long accountId)` - Validate credit capability
- `debitAccount(Long accountId, BigDecimal amount, String description)` - Perform debit
- `creditAccount(Long accountId, BigDecimal amount, String description)` - Perform credit
- `validateTransferAccounts(Long fromId, Long toId, BigDecimal amount)` - Transfer validation

**Configuration Issue Identified**: Default Account Service URL in Transaction Service is configured as http://localhost:8090, but Account Service actually runs on port 8092. This requires configuration update.

## 5. API Endpoints Summary

| Method | Endpoint | Description | Priority |
|--------|----------|-------------|----------|
| POST | /api/transactions | Create new transaction | HIGH |
| GET | /api/transactions/{id} | Get transaction by ID | HIGH |
| GET | /api/transactions/account/{accountId} | Get account transactions | HIGH |
| GET | /api/transactions/user/{userId} | Get user transactions | HIGH |
| GET | /api/transactions/search | Search transactions | MEDIUM |
| PUT | /api/transactions/{id}/process | Process transaction | HIGH |
| POST | /api/transactions/{id}/retry | Retry failed transaction | MEDIUM |
| PUT | /api/transactions/{id}/cancel | Cancel pending transaction | MEDIUM |
| POST | /api/transactions/{id}/reverse | Reverse transaction | MEDIUM |
| GET | /api/transactions/admin/pending | Get pending transactions | MEDIUM |
| GET | /api/transactions/admin/failed | Get failed transactions | MEDIUM |
| POST | /api/transactions/batch | Batch transaction processing | MEDIUM |
| GET | /api/transactions/analytics/account/{accountId} | Account statistics | LOW |
| PUT | /api/transactions/{id}/status | Update status (admin) | MEDIUM |
| GET | /api/transactions/{id}/history | Get status history | LOW |

## 6. Error Handling

### 6.1 Exception Types

- **TransactionNotFoundException**: Transaction not found (404)
- **AccountNotFoundException**: Account not found (400)
- **InvalidTransactionOperationException**: Invalid operation (400)
- **InsufficientFundsException**: Insufficient account balance (400)
- **ExternalServiceException**: External service error (503)
- **TransactionProcessingException**: Processing error (500)

### 6.2 Error Response Format

```json
{
  "timestamp": "2023-12-01T10:30:00Z",
  "status": 400,
  "error": "Bad Request", 
  "message": "Insufficient funds for withdrawal",
  "path": "/api/transactions",
  "details": {
    "accountId": 123,
    "requestedAmount": 1000.00,
    "availableBalance": 500.00
  }
}
```

## 7. Quality Assurance

### 7.1 Testing Strategy

- **Unit Tests**: Minimum 80% code coverage for service and controller layers
- **Integration Tests**: Database and Account Service integration testing
- **Contract Tests**: API contract validation with Account Service
- **Performance Tests**: Load testing for transaction processing under peak load
- **Security Tests**: Authentication and authorization validation

### 7.2 Monitoring and Observability

- **Health Checks**: /actuator/health endpoint with Account Service dependency check
- **Metrics**: Transaction processing metrics via Micrometer
- **Logging**: Structured logging with transaction correlation IDs
- **Alerting**: Transaction failure rate and processing time alerts
- **Tracing**: Distributed tracing for transaction processing flow

## 8. Deployment and Operations

### 8.1 Environment Configuration

- **Development**: H2 in-memory database, mock Account Service
- **Testing**: PostgreSQL TestContainer, stubbed Account Service  
- **Staging**: Production-like PostgreSQL, integrated Account Service
- **Production**: Optimized PostgreSQL, full service mesh integration

### 8.2 Configuration Issues Identified

- **Issue**: Transaction Service default Account Service URL uses port 8090, but Account Service runs on port 8092
- **Impact**: Service integration failures in default configuration
- **Resolution**: Update app.services.account-service.url default to http://localhost:8092

### 8.3 Operational Procedures

- **Transaction Monitoring**: Monitor pending transaction queue and processing times
- **Failed Transaction Handling**: Automated retry with manual intervention capabilities
- **Reconciliation**: Daily transaction reconciliation with account balances
- **Backup and Recovery**: Transaction data backup and point-in-time recovery procedures

This requirement specification provides comprehensive guidance for development, testing, and deployment of the Transaction Service based on actual implementation analysis.
