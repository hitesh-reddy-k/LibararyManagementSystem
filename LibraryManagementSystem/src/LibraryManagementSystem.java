import manager.LibraryManager;
import models.Book;
import models.Book.BookStatus;
import models.Patron;

import java.util.List;
import java.util.Scanner;


public class LibraryManagementSystem {
    private static LibraryManager libraryManager;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        libraryManager = new LibraryManager();
        scanner = new Scanner(System.in);
        
        System.out.println("   Welcome to the Library Management System  ");
        
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        bookManagementMenu();
                        break;
                    case 2:
                        patronManagementMenu();
                        break;
                    case 3:
                        libraryOperationsMenu();
                        break;
                    case 4:
                        displayStatistics();
                        break;
                    case 5:
                        running = false;
                        System.out.println("\nThank you for using Library Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("\n wrong choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n wrong input Please enter a number.");
            }
        }
        
        scanner.close();
    }
    

    
    private static void displayMainMenu() {
        System.out.println("   ");
        System.out.println("        LIBRARY MANAGEMENT SYSTEM       ");
        System.out.println("    ");
        System.out.println(" 1. Book Management ");
        System.out.println(" 2. Patron Management ");
        System.out.println("3. Library Operations ");
        System.out.println("4. View Statistics ");
        System.out.println(" 5. Exit ");
        System.out.print("Enter your choice: ");
    }
    
    
    
    private static void bookManagementMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n");
            System.out.println("        BOOK MANAGEMENT MENU  ");
            System.out.println(" 1. Add New Book ");
            System.out.println(" 2. Update Book ");
            System.out.println(" 3. Delete Book ");
            System.out.println(" 4. List All Books ");
            System.out.println(" 5. Search Books by Title ");
            System.out.println(" 6. Search Books by Author ");
            System.out.println(" 7. Search Books by Status ");
            System.out.println(" 8. Back to Main Menu ");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        updateBook();
                        break;
                    case 3:
                        deleteBook();
                        break;
                    case 4:
                        listAllBooks();
                        break;
                    case 5:
                        searchBooksByTitle();
                        break;
                    case 6:
                        searchBooksByAuthor();
                        break;
                    case 7:
                        searchBooksByStatus();
                        break;
                    case 8:
                        back = true;
                        break;
                    default:
                        System.out.println("\n wrong choice . Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n wrong input try again. Please enter a number.");
            }
        }
    }
    
    private static void addBook() {
        System.out.println("\n ADD NEW BOOK");
        
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine().trim();
        
        if (bookId.isEmpty()) {
            System.out.println(" Book ID cannot be empty.");
            return;
        }
        
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println(" Title cannot be empty.");
            return;
        }
        
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();
        
        if (author.isEmpty()) {
            System.out.println(" Author cannot be empty.");
            return;
        }
        
        System.out.print("Enter Year of Publication: ");
        try {
            int year = Integer.parseInt(scanner.nextLine().trim());
            
            if (year < 1000 || year > 2100) {
                System.out.println("❌ Please enter a valid year.");
                return;
            }
            
            String result = libraryManager.addBook(bookId, title, author, year);
            if (result == null) {
                System.out.println("✅ Book added successfully!");
            } else if (result.startsWith("ERROR_ID")) {
                System.out.println("❌ A book with this ID already exists.");
            } else if (result.startsWith("ERROR_TITLE")) {
                System.out.println("❌ " + result.substring(12)); // Remove "ERROR_TITLE: " prefix
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid year. Please enter a number.");
        }
    }
    
    private static void updateBook() {
        System.out.println("\nUPDATE BOOK");
        
        System.out.print("Enter Book ID to update: ");
        String bookId = scanner.nextLine().trim();
        
        Book book = libraryManager.findBookById(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        
        System.out.println("Current details: " + book);
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        System.out.print("New Title [" + book.getTitle() + "]: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            title = book.getTitle();
        }
        
        System.out.print("New Author [" + book.getAuthor() + "]: ");
        String author = scanner.nextLine().trim();
        if (author.isEmpty()) {
            author = book.getAuthor();
        }
        
        System.out.print("New Year [" + book.getYearOfPublication() + "]: ");
        String yearStr = scanner.nextLine().trim();
        int year = book.getYearOfPublication();
        
        if (!yearStr.isEmpty()) {
            try {
                year = Integer.parseInt(yearStr);
                if (year < 1000 || year > 2100) {
                    System.out.println(" Invalid year. Update cancelled.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println(" Invalid year format. Update cancelled.");
                return;
            }
        }
        
        if (libraryManager.updateBook(bookId, title, author, year)) {
            System.out.println(" Book updated successfully!");
        } else {
            System.out.println(" Failed to update book.");
        }
    }
    
    private static void deleteBook() {
        System.out.println("\nDELETE BOOK");
        
        System.out.print("Enter Book ID to delete: ");
        String bookId = scanner.nextLine().trim();
        
        Book book = libraryManager.findBookById(bookId);
        if (book == null) {
            System.out.println(" Book not found.");
            return;
        }
        
        System.out.println("Book details: " + book);
        System.out.print("Are you sure you want to delete this book? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("yes") || confirmation.equals("y")) {
            if (libraryManager.deleteBook(bookId)) {
                System.out.println(" Book deleted successfully!");
            } else {
                System.out.println(" Failed to delete book.");
            }
        } else {
            System.out.println(" Deletion cancelled.");
        }
    }
    
    private static void listAllBooks() {
        System.out.println("\nALL BOOKS ===");
        List<Book> books = libraryManager.getAllBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            System.out.println("Total books: " + books.size() + "\n");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
    
    private static void searchBooksByTitle() {
        System.out.println("\n=== SEARCH BOOKS BY TITLE ===");
        System.out.print("Enter title to search: ");
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println(" Title cannot be empty.");
            return;
        }
        
        List<Book> books = libraryManager.searchBooksByTitle(title);
        
        if (books.isEmpty()) {
            System.out.println("No books found with title containing: " + title);
        } else {
            System.out.println("\nFound " + books.size() + " book(s):\n");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
    
    private static void searchBooksByAuthor() {
        System.out.println("\nSEARCH BOOKS BY AUTHOR");
        System.out.print("Enter author to search: ");
        String author = scanner.nextLine().trim();
        
        if (author.isEmpty()) {
            System.out.println(" Author cannot be empty.");
            return;
        }
        
        List<Book> books = libraryManager.searchBooksByAuthor(author);
        
        if (books.isEmpty()) {
            System.out.println("No books found by author containing: " + author);
        } else {
            System.out.println("\nFound " + books.size() + " book(s):\n");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }
    
    private static void searchBooksByStatus() {
        System.out.println("\nSEARCH BOOKS BY STATUS");
        System.out.println("1. Available");
        System.out.println("2. Checked Out");
        System.out.print("Enter your choice: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            BookStatus status;
            
            if (choice == 1) {
                status = BookStatus.AVAILABLE;
            } else if (choice == 2) {
                status = BookStatus.CHECKED_OUT;
            } else {
                System.out.println(" Invalid choice.");
                return;
            }
            
            List<Book> books = libraryManager.searchBooksByStatus(status);
            
            if (books.isEmpty()) {
                System.out.println("No books found with status: " + status);
            } else {
                System.out.println("\nFound " + books.size() + " book(s):\n");
                for (Book book : books) {
                    System.out.println(book);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input. Please enter a number.");
        }
    }
    

    
    private static void patronManagementMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("      PATRON MANAGEMENT MENU ");
            System.out.println("1. Add New Patron            ");
            System.out.println("2. Update Patron             ");
            System.out.println("3. Delete Patron             ");
            System.out.println("4. List All Patrons          ");
            System.out.println("5. Search Patrons by Name    ");
            System.out.println("6. View Patron Details       ");
            System.out.println("7. Back to Main Menu         ");

            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        addPatron();
                        break;
                    case 2:
                        updatePatron();
                        break;
                    case 3:
                        deletePatron();
                        break;
                    case 4:
                        listAllPatrons();
                        break;
                    case 5:
                        searchPatronsByName();
                        break;
                    case 6:
                        viewPatronDetails();
                        break;
                    case 7:
                        back = true;
                        break;
                    default:
                        System.out.println("\n Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n Invalid input. Please enter a number.");
            }
        }
    }
    
    private static void addPatron() {
        System.out.println("\nADD NEW PATRON");
        
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine().trim();
        
        if (patronId.isEmpty()) {
            System.out.println(" Patron ID cannot be empty.");
            return;
        }
        
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println(" Name cannot be empty.");
            return;
        }
        
        if (libraryManager.addPatron(patronId, name)) {
            System.out.println(" Patron added successfully!");
        } else {
            System.out.println(" A patron with this ID already exists.");
        }
    }
    
    private static void updatePatron() {
        System.out.println("\nUPDATE PATRON");
        
        System.out.print("Enter Patron ID to update: ");
        String patronId = scanner.nextLine().trim();
        
        Patron patron = libraryManager.findPatronById(patronId);
        if (patron == null) {
            System.out.println("Patron not found.");
            return;
        }
        
        System.out.println("Current details: " + patron);
        System.out.print("Enter new name [" + patron.getName() + "]: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            name = patron.getName();
        }
        
        if (libraryManager.updatePatron(patronId, name)) {
            System.out.println("Patron updated successfully!");
        } else {
            System.out.println(" Failed to update patron.");
        }
    }
    
    private static void deletePatron() {
        System.out.println("\nDELETE PATRON");
        
        System.out.print("Enter Patron ID to delete: ");
        String patronId = scanner.nextLine().trim();
        
        Patron patron = libraryManager.findPatronById(patronId);
        if (patron == null) {
            System.out.println(" Patron not found.");
            return;
        }
        
        System.out.println("Patron details: " + patron);
        
        if (patron.getNumberOfBooksCheckedOut() > 0) {
            System.out.println("Warning: This patron has " + patron.getNumberOfBooksCheckedOut() + 
                             " book(s) checked out. All books will be returned automatically.");
        }
        
        System.out.print("Are you sure you want to delete this patron? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("yes") || confirmation.equals("y")) {
            if (libraryManager.deletePatron(patronId)) {
                System.out.println("Patron deleted successfully!");
            } else {
                System.out.println(" Failed to delete patron.");
            }
        } else {
            System.out.println("eletion cancelled.");
        }
    }
    
    private static void listAllPatrons() {
        System.out.println("\n=== ALL PATRONS ===");
        List<Patron> patrons = libraryManager.getAllPatrons();
        
        if (patrons.isEmpty()) {
            System.out.println("No patrons in the library.");
        } else {
            System.out.println("Total patrons: " + patrons.size() + "\n");
            for (Patron patron : patrons) {
                System.out.println(patron);
            }
        }
    }
    
    private static void searchPatronsByName() {
        System.out.println("\n=== SEARCH PATRONS BY NAME ===");
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        
        List<Patron> patrons = libraryManager.searchPatronsByName(name);
        
        if (patrons.isEmpty()) {
            System.out.println("No patrons found with name containing: " + name);
        } else {
            System.out.println("\nFound " + patrons.size() + " patron(s):\n");
            for (Patron patron : patrons) {
                System.out.println(patron);
            }
        }
    }
    
    private static void viewPatronDetails() {
        System.out.println("\nVIEW PATRON DETAILS");
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine().trim();
        
        Patron patron = libraryManager.findPatronById(patronId);
        if (patron == null) {
            System.out.println(" Patron not found.");
            return;
        }
        
        System.out.println("\n" + patron.toDetailedString());
        
      
        List<String> checkedOutBooks = patron.getBooksCheckedOut();
        if (!checkedOutBooks.isEmpty()) {
            System.out.println("\nChecked Out Books Details");
            for (String bookId : checkedOutBooks) {
                Book book = libraryManager.findBookById(bookId);
                if (book != null) {
                    System.out.println(book);
                }
            }
        }
    }
    
   
    
    private static void libraryOperationsMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("      LIBRARY OPERATIONS MENU  ");
            System.out.println(" 1. Check Out Book");
            System.out.println(" 2. Return Book");
            System.out.println(" 3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        checkOutBook();
                        break;
                    case 2:
                        returnBook();
                        break;
                    case 3:
                        back = true;
                        break;
                    default:
                        System.out.println("\nInvalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter a number.");
            }
        }
    }
    
    private static void checkOutBook() {
        System.out.println("\nCHECK OUT BOOK");
        
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine().trim();
        
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine().trim();
        
        String result = libraryManager.checkOutBook(bookId, patronId);
        
        if (result.startsWith("Success")) {
            System.out.println("Success: " + result);
        } else {
            System.out.println("Error: " + result);
        }
    }
    
    private static void returnBook() {
        System.out.println("\nRETURN BOOK");
        
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine().trim();
        
        System.out.print("Enter Patron ID: ");
        String patronId = scanner.nextLine().trim();
        
        String result = libraryManager.returnBook(bookId, patronId);
        
        if (result.startsWith("Success")) {
            System.out.println("Success: " + result);
        } else {
            System.out.println("Error: " + result);
        }
    }
    
    
    
    private static void displayStatistics() {
        System.out.println(libraryManager.getStatistics());
    }
}
