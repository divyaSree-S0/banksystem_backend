# Banking System Backend Schema and some helping instructions

## Database Configuration

### PostgreSQL Setup
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/banksystem
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show.sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Data Model

### 1. Customer Entity
**Table**: `customers`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| customer_id | BIGINT | PK, AUTO_INCREMENT | Unique customer identifier |
| customer_name | VARCHAR(150) | NOT NULL | Full name of the customer |
| phone_no | VARCHAR(15) | NOT NULL | Customer's phone number |
| address | VARCHAR(250) | NOT NULL | Customer's address |
| email | VARCHAR | NOT NULL, UNIQUE | Customer's email address |
| username | VARCHAR | NOT NULL, UNIQUE | Login username |
| password | VARCHAR | NOT NULL | Plain text password (for demo) |
| role | VARCHAR | NOT NULL, DEFAULT 'USER' | User role (USER/ADMIN) |

**Relationships**:
- One-to-Many with Account (via customer_id)

### 2. Account Entity
**Table**: `accounts`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| account_id | BIGINT | PK, AUTO_INCREMENT | Unique account identifier |
| account_number | VARCHAR | NOT NULL, UNIQUE | Account number (system generated) |
| balance | BIGINT | NOT NULL | Account balance in smallest currency unit |
| account_type | ENUM | NOT NULL | Account type (SAVINGS/CURRENT) |
| customer_id | BIGINT | FK, NOT NULL | Reference to customer |

**Relationships**:
- Many-to-One with Customer
- One-to-Many with Transaction

### 3. Transaction Entity
**Table**: `transactions`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| transaction_id | BIGINT | PK, AUTO_INCREMENT | Unique transaction identifier |
| account_id | BIGINT | FK, NOT NULL | Reference to account |
| amount | DECIMAL | NOT NULL | Transaction amount |
| type | VARCHAR | NOT NULL | Transaction type |
| date_time | TIMESTAMP | NOT NULL | Transaction timestamp |
| description | VARCHAR | NULLABLE | Transaction description |

**Transaction Types**:
- `DEPOSIT` - Money deposited to account
- `WITHDRAW` - Money withdrawn from account
- `TRANSFER_OUT` - Money transferred out to another account
- `TRANSFER_IN` - Money received from another account

**Relationships**:
- Many-to-One with Account

### 4. AccountType Enum
```java
public enum AccountType {
    SAVINGS, CURRENT
}
```

## API Endpoints

### Customer Management

#### Create Customer
- **POST** `/api/customers`
- **Body**: CreateCustomerRequest
- **Response**: CustomerDto
- **Access**: Public (for registration)

#### Create Admin User
- **POST** `/api/customers/admin`
- **Body**: CreateCustomerRequest
- **Response**: CustomerDto
- **Access**: Admin only

#### Login
- **POST** `/api/customers/login`
- **Body**: LoginRequest
- **Response**: LoginResponse with token and user data
- **Access**: Public

#### Get All Customers
- **GET** `/api/customers`
- **Response**: List&lt;CustomerDto&gt;
- **Access**: Admin only

#### Get Customer by ID
- **GET** `/api/customers/{id}`
- **Response**: CustomerDto
- **Access**: Admin or Owner

#### Update Customer
- **PUT** `/api/customers/{id}`
- **Body**: CreateCustomerRequest
- **Response**: CustomerDto
- **Access**: Admin or Owner

#### Delete Customer
- **DELETE** `/api/customers/{id}`
- **Response**: 204 No Content
- **Access**: Admin only

### Account Management

#### Create Account
- **POST** `/api/accounts`
- **Body**: CreateAccountRequest
- **Response**: AccountDto
- **Access**: Admin or Customer (for own account)

#### Get All Accounts
- **GET** `/api/accounts`
- **Response**: List&lt;AccountDto&gt;
- **Access**: Admin only

#### Get Accounts by Customer ID
- **GET** `/api/accounts/customer/{customerId}`
- **Response**: List&lt;AccountDto&gt;
- **Access**: Admin or Owner

#### Get Account by ID
- **GET** `/api/accounts/{id}`
- **Response**: AccountDto
- **Access**: Admin or Owner

#### Update Account Balance
- **PUT** `/api/accounts/{id}/balance?newBalance={amount}`
- **Response**: AccountDto
- **Access**: System/Admin only

#### Delete Account
- **DELETE** `/api/accounts/{id}`
- **Response**: 204 No Content
- **Access**: Admin only

### Transaction Management

#### Deposit Money
- **POST** `/api/transactions/deposit`
- **Body**: DepositRequest
- **Response**: TransactionDto
- **Access**: Admin or Account Owner

#### Withdraw Money
- **POST** `/api/transactions/withdraw`
- **Body**: WithdrawRequest
- **Response**: TransactionDto
- **Access**: Admin or Account Owner

#### Transfer Money
- **POST** `/api/transactions/transfer`
- **Body**: TransferRequest
- **Response**: TransactionDto
- **Access**: Admin or Source Account Owner

#### Get All Transactions
- **GET** `/api/transactions`
- **Response**: List&lt;TransactionDto&gt;
- **Access**: Admin only

#### Get Transactions by Account ID
- **GET** `/api/transactions/account/{accountId}`
- **Response**: List&lt;TransactionDto&gt;
- **Access**: Admin or Account Owner

#### Get Transactions by Customer ID
- **GET** `/api/transactions/customer/{customerId}`
- **Response**: List&lt;TransactionDto&gt;
- **Access**: Admin or Customer Owner

## Data Transfer Objects (DTOs)

### CustomerDto
```java
{
    "customerId": Long,
    "customerName": String,
    "phoneNo": String,
    "address": String,
    "email": String,
    "username": String,
    "role": String
}
```

### AccountDto
```java
{
    "accountId": Long,
    "accountNumber": String,
    "balance": Long,
    "accountType": "SAVINGS|CURRENT",
    "customerId": Long,
    "customerName": String
}
```

### TransactionDto
```java
{
    "transactionId": Long,
    "accountId": Long,
    "accountNumber": String,
    "amount": BigDecimal,
    "type": String,
    "dateTime": LocalDateTime,
    "description": String
}
```

## Request Objects

### CreateCustomerRequest
```java
{
    "customerName": String,
    "phoneNo": String,
    "address": String,
    "email": String,
    "username": String,
    "password": String,
    "role": String (optional, defaults to "USER")
}
```

### CreateAccountRequest
```java
{
    "customerId": Long,
    "accountType": "SAVINGS|CURRENT",
    "initialBalance": Long (optional, defaults to 0)
}
```

### LoginRequest
```java
{
    "username": String, // Can be username or email
    "password": String
}
```

### DepositRequest
```java
{
    "accountId": Long,
    "amount": BigDecimal,
    "description": String (optional)
}
```

### WithdrawRequest
```java
{
    "accountId": Long,
    "amount": BigDecimal,
    "description": String (optional)
}
```

### TransferRequest
```java
{
    "fromAccountId": Long,
    "toAccountId": Long,
    "amount": BigDecimal,
    "description": String (optional)
}
```

## Role-Based Access Control (RBAC)

### User Roles
- **USER**: Regular customer with limited access
- **ADMIN**: System administrator with full access

### Access Permissions

#### ADMIN Role
- View all customers, accounts, and transactions
- Create/update/delete any customer
- Create/delete accounts for any customer
- Perform transactions on any account
- Access all management endpoints

#### USER Role
- View only their own customer profile
- View only their own accounts
- View only their own transactions
- Create accounts for themselves only
- Perform transactions only on their own accounts
- Cannot access other customers' data

## Business Rules

### Account Creation
- Account numbers are system-generated (format: ACC + timestamp + random)
- Default balance is 0
- Customer must exist before creating account
- Account type must be SAVINGS or CURRENT

### Transactions
- Withdrawal requires sufficient balance
- Transfer requires sufficient balance in source account
- All transactions are recorded with timestamp
- Transfer creates two transaction records (OUT and IN)
- Balance updates are atomic

### Security
- Passwords are stored in plain text (for demo only)
- Simple token-based authentication
- CORS enabled for frontend origins
- Input validation on all endpoints

## Error Handling

### Custom Exceptions
- `ResourceNotFoundException`: Entity not found
- `InsufficientFundsException`: Insufficient account balance
- `ValidationException`: Input validation errors

### HTTP Status Codes
- `200 OK`: Successful GET/PUT requests
- `201 Created`: Successful POST requests
- `204 No Content`: Successful DELETE requests
- `400 Bad Request`: Invalid input data
- `401 Unauthorized`: Authentication failure
- `403 Forbidden`: Authorization failure
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: System errors

## Database Setup Instructions

### 1. Install PostgreSQL
```bash
# Ubuntu/Debian
sudo apt-get install postgresql postgresql-contrib

# macOS with Homebrew
brew install postgresql
```

### 2. Create Database
```sql
-- Connect to PostgreSQL as superuser
psql -U postgres

-- Create database
CREATE DATABASE banksystem;

-- Create user (optional)
CREATE USER your_username WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE banksystem TO your_username;
```

### 3. Update Application Properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/banksystem
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL 12+

### Build and Run
```bash
# Clone the repository
git clone <repository-url>
cd banksystem

# Build the application
mvn clean compile

# Run the application
mvn spring-boot:run

# Or run the JAR file
mvn clean package
java -jar target/banksystem-0.0.1-SNAPSHOT.jar
```

### Verify Installation
- Application will run on `http://localhost:8080`
- Health check: `http://localhost:8080/actuator/health`
- API documentation: Test endpoints with Postman or curl

## Sample Data

### Create Admin User
```bash
curl -X POST http://localhost:8080/api/customers/admin \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "System Administrator",
    "phoneNo": "1234567890",
    "address": "System Address",
    "email": "admin@bank.com",
    "username": "admin",
    "password": "admin123"
  }'
```

### Create Regular User
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "phoneNo": "9876543210",
    "address": "123 Main St",
    "email": "john@example.com",
    "username": "john",
    "password": "password123"
  }'
```

## API Testing

### Login Test
```bash
curl -X POST http://localhost:8080/api/customers/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### Create Account Test
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "accountType": "SAVINGS",
    "initialBalance": 1000
  }'
```

## Production Considerations

### Security Enhancements
- Implement JWT authentication
- Hash passwords with bcrypt
- Add rate limiting
- Implement proper authorization
- Use HTTPS
- Add input sanitization

### Performance Optimizations
- Add database indexing
- Implement caching
- Add connection pooling
- Optimize queries
- Add pagination

### Monitoring
- Add logging framework
- Implement metrics collection
- Add health checks
- Configure alerts

This schema provides a complete overview of the banking system backend architecture, making it easy for other developers to understand and work with the codebase.