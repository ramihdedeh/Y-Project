
# Y-Platform

## Introduction

Y-Platform is a robust application built using a client-server architecture. The client side, developed in JavaFX, provides a graphical user interface (GUI) designed for a user-friendly experience. On the server side, implemented in Java, the platform manages user accounts, posts, and facilitates communication between clients through following/followed interactions.
## Features

- Integration with JavaFX for building rich client applications.
- Support for controls and forms using ControlsFX and FormsFX libraries.
- Validation using ValidatorFX.
- Database integration with SQLite.
- JSON processing with Jackson and org.json.
- JUnit for testing.

## Technologies Used

- **Java**: Core language for building the application.
- **JavaFX**: Used for creating the user interface.
- **Maven**: Dependency management and build automation.
- **SQLite**: Database integration for data persistence.
- **FormsFX**: Forms framework for JavaFX.
- **ValidatorFX**: Validation framework for JavaFX.
- **JUnit**: Testing framework for unit tests.
- **Jackson**: Library for processing JSON data.


## Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/ramihdedeh/Y-Project.git
   ```

2. **Navigate to the project directory**:
   ```bash
   cd Y-Platform
   ```

3. **Install dependencies**:
   ```bash
   mvn install
   ```

### Running the Application

To run the application, use the following Maven command:

```bash
mvn javafx:run
```

### Running Tests

To run the unit tests, use the following command:

```bash
mvn test
```

### Packaging the Application

To package the application into a deployable format:

```bash
mvn package
```

## Project Structure

- **pom.xml**: Contains the Maven configuration and dependencies.
- **src/main/java**: Contains the Java source code.
- **src/main/resources**: Contains the resources like FXML files, CSS, etc.
- **src/test/java**: Contains unit tests.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
