# Library Management System

A comprehensive console-based Library Management System built in Java that enables efficient management of books and library patrons.

## Features
- Add, remove, and search for books
- Register and manage library patrons
- Check out and return books
- Persistent data storage using CSV files
- User-friendly console interface
- Modular code structure for easy maintenance and scalability
## Setup Instructions
1. **Clone the Repository**: 
   ```bash
   git clone "my repository URL"
   ```
2. **Navigate to the Project Directory**:
   ```bash
    cd LibraryManagementSystem
    ```
3. **Compile the Java Files**:
    ```bash
    javac -d bin src/**/*.java
    ```
4. **Run the Application**:
    ```bash
    java -cp bin LibraryManagementSystem
    ```
## Dependencies
- Java Development Kit (JDK) 8 or higher
## Project Structure
- `src/`: Contains all the Java source files organized into packages.
  - `model/`: Contains classes representing the core entities (Book, Patron).
  - `storage/`: Contains classes responsible for data storage and retrieval.
  - `LibraryManagementSystem.java`: The main class to run the application.
- `data/`: Directory where CSV files for books and patrons are stored.
## Usage
- Follow the on-screen prompts to navigate through the library management options.
- Use the menu options to add books, register patrons, check out and return books.
- Data is automatically saved to CSV files in the `data/` directory.
