> **⚠️ Important Notice**
> 
> 🚧 **This project is currently under active development** 🚧
> 
> Thoth is not yet ready for production use. The API and features are subject to change without notice. We recommend against using this in production environments until a stable release is available.

# Thoth - Cloud Storage Solution

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://github.com/Mahmoud02/thoth/actions/workflows/build.yml/badge.svg)](https://github.com/Mahmoud02/thoth/actions)
[![Code Coverage](https://codecov.io/gh/Mahmoud02/thoth/branch/main/graph/badge.svg)](https://codecov.io/gh/Mahmoud02/thoth)

## 🏛️ Inspiration

Thoth is inspired by the ancient Egyptian god of wisdom, writing, and knowledge. Just as Thoth was the divine scribe and mediator of the gods, this project aims to be a reliable and intelligent storage solution for modern applications.

## 🚀 Overview

Thoth is an open-source, self-hosted storage solution designed for companies that want S3-like capabilities on their own infrastructure without the complexity of cloud services. Our goal is to provide a simple, no-code solution that lets organizations manage their files on-premise with minimal setup and maintenance.

### Key Capabilities:
- **Built-in File Validation**: Automatic checks for file size, type, and security
- **Pre-configured Rules**: Set up validation rules without writing any code
- **Custom Processing**: Define workflows for file processing on upload
- **Security First**: Built-in scanning and verification for safe file storage

Thoth offers the power of enterprise-grade object storage with the simplicity of a plug-and-play solution, making it perfect for businesses that need reliable, secure file storage without the overhead of cloud services or custom development.

## 🏗️ Architecture

Thoth is built using **Hexagonal Architecture** (Ports & Adapters) to ensure:

- **Maintainability**: Clear separation of concerns
- **Testability**: Easy to write unit and integration tests
- **Flexibility**: Swap components without affecting the core business logic
- **Scalability**: Designed to grow with your needs

### Core Components

- **API Layer**: RESTful API for client interactions
- **Domain Layer**: Core business logic and entities
- **Infrastructure Layer**: Implementation details (storage, database, etc.)
- **Ports**: Interfaces that define the application's boundaries
- **Adapters**: Concrete implementations of the ports

## 📚 Features

- **Object Storage**: Store and retrieve any type of file or binary data
- **Bucket Management**: Organize your data into logical containers
- **Namespace Support**: Multi-tenancy support for different organizations or teams
- **Extensible Functions**: Custom processing for stored objects
- **REST API**: Simple and consistent API for integration
- **Self-hosted**: Full control over your data

## 🛠️ Technology Stack

- **Language**: Java 17+
- **Framework**: Spring Boot 3.x
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Testing**: JUnit 5, Testcontainers, AssertJ, Mockito
- **Containerization**: Docker

## 🚀 Getting Started

### Development Setup

For local development, we provide a `development-dependencies.yml` file that sets up all necessary services using Docker Compose. This includes:

- **PostgreSQL 17.4**: The primary database
- **pgAdmin 4**: Web-based database management tool

To start the development environment:

```bash
docker-compose -f development-dependencies.yml up -d
```

Access the services at:
- PostgreSQL: `localhost:5432`
- pgAdmin: `http://localhost:5050` (email: admin@admin.com, password: admin)

### Testing with Testcontainers

Thoth leverages Testcontainers for reliable integration testing. This allows us to:

- Run tests against real database instances
- Ensure consistent test environments
- Test database migrations and queries with actual PostgreSQL
- Isolate tests using containerized dependencies

Testcontainers automatically manages the lifecycle of Docker containers, spinning up fresh instances for each test class and tearing them down afterward.

To run the tests:

```bash
mvn test
```

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 13+
- Docker (optional, for containerized deployment)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Mahmoud02/thoth.git
   cd thoth
   ```

2. Configure the database in `application.yml`

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Docker Setup

```bash
docker-compose up -d
```

## 📚 API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Docs: `http://localhost:8080/v3/api-docs`

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/mahmoud/thoth/
│   │       ├── api/                # API layer (controllers, DTOs)
│   │       ├── domain/             # Core business logic
│   │       │   ├── model/          # Domain models
│   │       │   ├── port/           # Ports (interfaces)
│   │       │   └── service/        # Domain services
│   │       └── infrastructure/     # Infrastructure implementations
│   │           ├── repository/     # Database repositories
│   │           └── config/         # Spring configurations
│   └── resources/                  # Configuration files
└── test/                          # Test files
```

## 🤝 Contributing

Contributions are welcome! Please read our [Contributing Guidelines](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by the wisdom of Thoth, the ancient Egyptian god of knowledge
- Built with the help of the open-source community
- Special thanks to all contributors and supporters
