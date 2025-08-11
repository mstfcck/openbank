# Account Service - Detailed Requirements Specification

## 1. Executive Summary

The Account Service is a core microservice in the OpenBank application responsible for managing customer bank accounts. It provides comprehensive account lifecycle management including creation, retrieval, balance operations, and account administration. The service integrates with the User Service for customer validation and serves as a critical dependency for the Transaction Service.

### 1.1 Service Overview
- **Service Name**: Account Service
- **Port**: 8092
- **Database**: PostgreSQL with H2 in-memory for development
- **External Dependencies**: User Service (port 8091)
- **Dependent Services**: Transaction Service (port 8093)

## 2. Functional Requirements

### 2.1 Account Management (FR-ACC-001 to FR-ACC-005)

#### FR-ACC-001: Account Creation
**Priority**: HIGH
**Epic**: Account Lifecycle Management

**Description**: The system SHALL allow creation of new bank accounts for verified users.

**Detailed Requirements**:
- System SHALL validate user existence via User Service before account creation
- System SHALL generate unique account numbers using format: ACC-YYYYMMDD-XXXX-XXXX
- System SHALL support account types: CHECKING, SAVINGS, BUSINESS, INVESTMENT
- System SHALL set initial account status to ACTIVE upon creation
- System SHALL support initial credit deposit during account creation
- System SHALL default currency to USD if not specified
- System SHALL handle overdraft limit configuration per account type

**Business Rules**:
- User must exist in User Service to create account
- Account number must be unique across the system
- Initial credit must be non-negative
- Business accounts may have higher overdraft limits

**API Endpoint**: `POST /api/accounts`

**Acceptance Criteria**:
- Given a valid user ID and account type, when creating an account, then account is created with ACTIVE status
- Given an invalid user ID, when creating an account, then UserNotFoundException is thrown
- Given duplicate account number generation, when creating account, then system regenerates unique number
- Given initial credit amount, when creating account, then account balance equals initial credit

#### FR-ACC-002: Account Retrieval
**Priority**: HIGH
**Epic**: Account Information Access

**Description**: The system SHALL provide multiple methods to retrieve account information.

**Detailed Requirements**:
- System SHALL allow account retrieval by account ID
- System SHALL allow account retrieval by account number
- System SHALL allow retrieval of all accounts for a specific user
- System SHALL support paginated account listing for administrative purposes
- System SHALL validate account existence before returning data
- System SHALL include all account details in response (balance, status, limits, etc.)

**API Endpoints**:
- `GET /api/accounts/{id}` - Get account by ID
- `GET /api/accounts/number/{accountNumber}` - Get account by number
- `GET /api/accounts/user/{userId}` - Get user accounts
- `GET /api/accounts` - Get all accounts (paginated)

**Acceptance Criteria**:
- Given a valid account ID, when retrieving account, then complete account details are returned
- Given an invalid account ID, when retrieving account, then AccountNotFoundException is thrown
- Given a valid user ID, when retrieving user accounts, then all user's accounts are returned
- Given pagination parameters, when retrieving accounts, then paginated response is returned

#### FR-ACC-003: Balance Operations
**Priority**: HIGH
**Epic**: Account Balance Management

**Description**: The system SHALL provide secure balance inquiry and modification operations.

**Detailed Requirements**:
- System SHALL allow balance inquiry for specific accounts
- System SHALL support credit operations (deposits) to accounts
- System SHALL support debit operations (withdrawals) from accounts
- System SHALL validate account status before balance operations
- System SHALL check sufficient funds for debit operations (balance + overdraft)
- System SHALL maintain audit trail for all balance changes
- System SHALL prevent operations on CLOSED or FROZEN accounts

**Business Rules**:
- Debit operations require sufficient available balance (balance + overdraft limit)
- Credit operations only allowed on ACTIVE accounts
- All balance operations must include descriptive text for audit purposes
- Overdraft protection based on account type and customer profile

**API Endpoints**:
- `GET /api/accounts/{id}/balance` - Get account balance
- `POST /api/accounts/{id}/credit` - Credit account
- `POST /api/accounts/{id}/debit` - Debit account

**Acceptance Criteria**:
- Given a valid account ID, when requesting balance, then current balance is returned
- Given sufficient funds, when debiting account, then balance is reduced by amount
- Given insufficient funds, when debiting account, then InvalidAccountOperationException is thrown
- Given active account, when crediting account, then balance is increased by amount

#### FR-ACC-004: Account Status Management
**Priority**: MEDIUM
**Epic**: Account Lifecycle Management

**Description**: The system SHALL support account status transitions and lifecycle management.

**Detailed Requirements**:
- System SHALL support account status types: ACTIVE, INACTIVE, CLOSED, FROZEN
- System SHALL allow account closure only with zero balance
- System SHALL allow account activation/deactivation by administrators
- System SHALL track status change history for audit purposes
- System SHALL prevent transactions on non-ACTIVE accounts

**Business Rules**:
- Account closure requires zero balance
- FROZEN accounts cannot perform any transactions
- INACTIVE accounts can only receive credits
- Status changes must be logged with timestamp and reason

**API Endpoints**:
- `DELETE /api/accounts/{id}` - Close account (soft delete)
- `PUT /api/accounts/{id}/status` - Update account status

**Acceptance Criteria**:
- Given zero balance account, when closing account, then status changes to CLOSED
- Given non-zero balance, when closing account, then InvalidAccountOperationException is thrown
- Given account status change, when updating status, then change is logged with audit trail

#### FR-ACC-005: Account Search and Filtering
**Priority**: MEDIUM
**Epic**: Account Information Access

**Description**: The system SHALL provide advanced search and filtering capabilities for account management.

**Detailed Requirements**:
- System SHALL support account search by multiple criteria
- System SHALL allow filtering by account type, status, balance range
- System SHALL support pagination for search results
- System SHALL provide sorting options (by balance, creation date, account number)
- System SHALL return search results with performance optimization

**API Endpoints**:
- `GET /api/accounts/search` - Search accounts with filters
- `GET /api/accounts/user/{userId}/paginated` - Paginated user accounts

### 2.2 Account Statistics and Reporting (FR-ACC-006 to FR-ACC-008)

#### FR-ACC-006: Account Statistics
**Priority**: LOW
**Epic**: Reporting and Analytics

**Description**: The system SHALL provide statistical information about accounts.

**Detailed Requirements**:
- System SHALL calculate total number of accounts by status
- System SHALL calculate total balance across all accounts
- System SHALL provide account distribution by type
- System SHALL support date-range filtering for statistics

**API Endpoints**:
- `GET /api/accounts/statistics` - Get account statistics

#### FR-ACC-007: Account Analytics
**Priority**: LOW
**Epic**: Reporting and Analytics

**Description**: The system SHALL support analytical reporting for business intelligence.

**Detailed Requirements**:
- System SHALL provide account growth trends
- System SHALL calculate average account balances
- System SHALL support export functionality for reports

#### FR-ACC-008: Account Validation Services
**Priority**: HIGH
**Epic**: Service Integration

**Description**: The system SHALL provide validation services for other microservices.

**Detailed Requirements**:
- System SHALL expose account existence validation
- System SHALL validate account eligibility for transactions
- System SHALL provide account balance verification
- System SHALL support bulk account validation

## 3. Non-Functional Requirements

### 3.1 Performance Requirements

#### NFR-ACC-001: Response Time
- Account retrieval operations SHALL respond within 200ms for 95% of requests
- Account creation operations SHALL complete within 500ms for 95% of requests
- Balance operations SHALL complete within 300ms for 95% of requests

#### NFR-ACC-002: Throughput
- System SHALL support minimum 1000 concurrent account operations
- System SHALL handle peak load of 10,000 requests per minute
- Database connection pool SHALL be optimized for high concurrency (20 max connections)

#### NFR-ACC-003: Scalability
- System SHALL support horizontal scaling across multiple instances
- Database queries SHALL be optimized with proper indexing
- Caching strategy SHALL be implemented for frequently accessed accounts

### 3.2 Security Requirements

#### NFR-ACC-004: Authentication and Authorization
- All API endpoints SHALL require valid authentication
- Administrative operations SHALL require elevated privileges
- User account access SHALL be restricted to account owners and authorized personnel

#### NFR-ACC-005: Data Protection
- Account numbers SHALL be generated with sufficient entropy to prevent guessing
- Sensitive account information SHALL be encrypted at rest
- Account balance information SHALL be transmitted over encrypted channels

#### NFR-ACC-006: Audit and Compliance
- All account operations SHALL be logged with timestamps and user identification
- System SHALL maintain immutable audit trail for regulatory compliance
- Account balance changes SHALL include transaction references

### 3.3 Reliability Requirements

#### NFR-ACC-007: Availability
- Service SHALL maintain 99.9% uptime during business hours
- System SHALL implement graceful degradation for User Service outages
- Database connections SHALL be monitored and automatically restored

#### NFR-ACC-008: Data Consistency
- Account balance operations SHALL be ACID compliant
- System SHALL prevent concurrent modification conflicts
- Database transactions SHALL ensure data integrity

### 3.4 Integration Requirements

#### NFR-ACC-009: Service Dependencies
- System SHALL gracefully handle User Service unavailability
- Service discovery SHALL automatically configure User Service endpoints
- Circuit breaker pattern SHALL be implemented for external service calls

#### NFR-ACC-010: API Compatibility
- RESTful API SHALL follow OpenAPI 3.0.4 specification
- Backward compatibility SHALL be maintained for API versions
- Error responses SHALL follow consistent format across all endpoints

## 4. Technical Specifications

### 4.1 Technology Stack
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL (production), H2 (development/testing)
- **ORM**: JPA/Hibernate
- **Security**: Spring Security
- **Documentation**: OpenAPI 3.0.4
- **Testing**: JUnit 5, Mockito, TestContainers

### 4.2 Database Schema
```sql
-- Account Entity
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_number VARCHAR(50) UNIQUE NOT NULL,
    account_type VARCHAR(20) NOT NULL, -- CHECKING, SAVINGS, BUSINESS, INVESTMENT
    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, CLOSED, FROZEN
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    overdraft_limit DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    opened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 0
);

-- Indexes for performance
CREATE INDEX idx_accounts_user_id ON accounts(user_id);
CREATE INDEX idx_accounts_account_number ON accounts(account_number);
CREATE INDEX idx_accounts_status ON accounts(status);
CREATE INDEX idx_accounts_created_at ON accounts(created_at);
```

### 4.3 Configuration Management
```yaml
# Service Configuration
server:
  port: 8092

# External Service URLs
app:
  services:
    user-service:
      url: ${USER_SERVICE_URL:http://localhost:8091}

# Database Configuration
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/accountdb}
    username: ${DATABASE_USERNAME:postgres}
    password: ${DATABASE_PASSWORD:postgres}
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
```

## 5. API Endpoints Summary

| Method | Endpoint | Description | Priority |
|--------|----------|-------------|----------|
| POST | /api/accounts | Create new account | HIGH |
| GET | /api/accounts/{id} | Get account by ID | HIGH |
| GET | /api/accounts/number/{accountNumber} | Get account by number | HIGH |
| GET | /api/accounts/user/{userId} | Get user accounts | HIGH |
| GET | /api/accounts | Get all accounts (paginated) | MEDIUM |
| GET | /api/accounts/{id}/balance | Get account balance | HIGH |
| POST | /api/accounts/{id}/credit | Credit account | HIGH |
| POST | /api/accounts/{id}/debit | Debit account | HIGH |
| DELETE | /api/accounts/{id} | Close account | MEDIUM |
| GET | /api/accounts/search | Search accounts | MEDIUM |
| GET | /api/accounts/user/{userId}/paginated | Paginated user accounts | MEDIUM |
| GET | /api/accounts/statistics | Account statistics | LOW |
| PUT | /api/accounts/{id}/status | Update account status | MEDIUM |

## 6. Error Handling

### 6.1 Exception Types
- **AccountNotFoundException**: Account not found (404)
- **UserNotFoundException**: User not found (400)
- **InvalidAccountOperationException**: Invalid operation (400)
- **ExternalServiceException**: External service error (503)

### 6.2 Error Response Format
```json
{
  "timestamp": "2023-12-01T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Account not found with ID: 123",
  "path": "/api/accounts/123"
}
```

## 7. Quality Assurance

### 7.1 Testing Strategy
- **Unit Tests**: Minimum 80% code coverage
- **Integration Tests**: Database and external service integration
- **Contract Tests**: API contract validation
- **Performance Tests**: Load testing for critical endpoints

### 7.2 Monitoring and Observability
- **Health Checks**: /actuator/health endpoint
- **Metrics**: Account operation metrics via Micrometer
- **Logging**: Structured logging with correlation IDs
- **Alerting**: Critical error and performance threshold alerts

## 8. Deployment and Operations

### 8.1 Environment Configuration
- **Development**: H2 in-memory database, mock User Service
- **Testing**: PostgreSQL TestContainer, stubbed external services
- **Staging**: Production-like PostgreSQL, integrated User Service
- **Production**: Optimized PostgreSQL, full service mesh

### 8.2 Configuration Issues Identified
- **Issue**: Transaction Service default URL for Account Service is configured as port 8090, but Account Service runs on port 8092
- **Impact**: Service discovery and integration failures in default configuration
- **Resolution**: Update configuration management to ensure consistent service URLs

This requirement specification is based on the actual implementation analysis and provides comprehensive guidance for development, testing, and deployment of the Account Service.
