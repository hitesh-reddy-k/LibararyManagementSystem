package manager;

import models.Book;
import models.Book.BookStatus;
import models.Patron;
import storage.DataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all library operations including book and patron management,
 * and check-out/return transactions.
 */
public class LibraryManager {
    private List<Book> books;
    private List<Patron> patrons;
   
    public LibraryManager() {
        DataStorage.initializeDataDirectory();
        this.books = DataStorage.loadBooks();
        this.patrons = DataStorage.loadPatrons();
    }
    

    public String addBook(String bookId, String title, String author, int year) {
        
        if (findBookById(bookId) != null) {
            System.out.println("A book with this ID already exists: " + bookId);
            return "ERROR_ID: A book with this ID already exists.";
        }
        

        List<Book> existingBooks = searchBooksByTitle(title);
        for (Book existingBook : existingBooks) {
            if (existingBook.getTitle().equalsIgnoreCase(title)) {
                StringBuilder message = new StringBuilder();
                System.out.println("A book with this title already exists: " + title);
                message.append("ERROR_TITLE: A book with this title already exists:\n");
                message.append(existingBook.toString());
                return message.toString();
            }
        }
        
        Book book = new Book(bookId, title, author, year);
        books.add(book);
        saveBooks();
        return null;
    }

    public boolean updateBook(String bookId, String newTitle, String newAuthor, int newYear) {
        Book book = findBookById(bookId);
        if (book == null) {
            return false;
        }
        
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setYearOfPublication(newYear);
        saveBooks();
        return true;
    }
    

    public boolean deleteBook(String bookId) {
        Book book = findBookById(bookId);
        if (book == null) {
            return false;
        }
       
        if (book.getStatus() == BookStatus.CHECKED_OUT) {
            String patronId = book.getCheckedOutBy();
            Patron patron = findPatronById(patronId);
            if (patron != null) {
                patron.returnBook(bookId);
                savePatrons();
            }
        }
        
        books.remove(book);
        saveBooks();
        return true;
    }
    
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }
    
    
    public List<Book> searchBooksByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
    
   
    public List<Book> searchBooksByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }
    
   
    public List<Book> searchBooksByStatus(BookStatus status) {
        return books.stream()
                .filter(book -> book.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    
    public Book findBookById(String bookId) {
        return books.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }
    
   
    public boolean addPatron(String patronId, String name) {
        
        if (findPatronById(patronId) != null) {
            return false;
        }
        
        Patron patron = new Patron(patronId, name);
        patrons.add(patron);
        savePatrons();
        return true;
    }
    
   
    public boolean updatePatron(String patronId, String newName) {
        Patron patron = findPatronById(patronId);
        if (patron == null) {
            return false;
        }
        
        patron.setName(newName);
        savePatrons();
        return true;
    }
    
   
    public boolean deletePatron(String patronId) {
        Patron patron = findPatronById(patronId);
        if (patron == null) {
            return false;
        }
        
        List<String> checkedOutBooks = patron.getBooksCheckedOut();
        for (String bookId : checkedOutBooks) {
            Book book = findBookById(bookId);
            if (book != null) {
                book.setStatus(BookStatus.AVAILABLE);
                book.setCheckedOutBy(null);
                System.out.println("Book returned due to patron deletion: " + book.getTitle());
            }
        }
        
        patrons.remove(patron);
        savePatrons();
        saveBooks();
        return true;
    }
    
   
    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons);
    }
    
   
    public Patron findPatronById(String patronId) {
        return patrons.stream()
                .filter(patron -> patron.getPatronId().equals(patronId))
                .findFirst()
                .orElse(null);
    }
    
   
    public List<Patron> searchPatronsByName(String name) {
        return patrons.stream()
                .filter(patron -> patron.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
  
    public String checkOutBook(String bookId, String patronId) {
        Book book = findBookById(bookId);
        Patron patron = findPatronById(patronId);
        
        // Validate book exists
        if (book == null) {
            return "Error: Book not found.";
        }
        
        // Validate patron exists
        if (patron == null) {
            return "Error: Patron not found.";
        }
        
        // Check if book is available
        if (book.getStatus() == BookStatus.CHECKED_OUT) {
            return "Error: Book is already checked out.";
        }
        
        // Perform check-out
        book.setStatus(BookStatus.CHECKED_OUT);
        book.setCheckedOutBy(patronId);
        patron.checkOutBook(bookId);
        
        saveBooks();
        savePatrons();
        
        return "Success: Book checked out successfully.";
    }
    
  
    public String returnBook(String bookId, String patronId) {
        Book book = findBookById(bookId);
        Patron patron = findPatronById(patronId);
        
        if (book == null) {
            return "Error: Book not found.";
        }
        
        if (patron == null) {
            return "Error: Patron not found.";
        }
        
        
        if (book.getStatus() != BookStatus.CHECKED_OUT) {
            return "Error: Book is not checked out.";
        }
        
        if (!book.getCheckedOutBy().equals(patronId)) {
            return "Error: Book is checked out by a different patron.";
        }
        
        
        book.setStatus(BookStatus.AVAILABLE);
        book.setCheckedOutBy(null);
        patron.returnBook(bookId);
        
        saveBooks();
        savePatrons();
        
        return "Success: Book returned successfully.";
    }

    private void saveBooks() {
        DataStorage.saveBooks(books);
    }
    
    
    private void savePatrons() {
        DataStorage.savePatrons(patrons);
    }
    
   
    public String getStatistics() {
        int totalBooks = books.size();
        int availableBooks = (int) books.stream()
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .count();
        int checkedOutBooks = totalBooks - availableBooks;
        int totalPatrons = patrons.size();
        
        return String.format("\n=== LIBRARY STATISTICS ===\n" +
                "Total Books: %d\n" +
                "Available Books: %d\n" +
                "Checked Out Books: %d\n" +
                "Total Patrons: %d\n" +
                "==========================\n",
                totalBooks, availableBooks, checkedOutBooks, totalPatrons);
    }
}
