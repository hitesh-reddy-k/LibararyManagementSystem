package storage;

import models.Book;
import models.Patron;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class DataStorage {
    private static final String BOOKS_FILE = "data/books.csv";
    private static final String PATRONS_FILE = "data/patrons.csv";
   
    public static void saveBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            // Write header
            writer.write("BookID,Title,Author,Year,Status,CheckedOutBy");
            writer.newLine();
            
            // Write each book
            for (Book book : books) {
                writer.write(book.toCSV());
                writer.newLine();
            }
            System.out.println("Books saved successfully.");
            
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }
    
   
    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOKS_FILE);
        
        if (!file.exists()) {
            return books;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        books.add(Book.fromCSV(line));
                        System.out.println("Loaded book: " + line);
                    } catch (Exception e) {
                        System.err.println("Error parsing book line: " + line);
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        
        return books;
    }
    
   
    public static void savePatrons(List<Patron> patrons) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATRONS_FILE))) {
            writer.write("PatronID,Name,BooksCheckedOut");
            writer.newLine();
            
            for (Patron patron : patrons) {
                writer.write(patron.toCSV());
                writer.newLine();
            }
            
        } catch (IOException e) {
            System.err.println("Error saving patrons: " + e.getMessage());
        }
    }
    
   
    public static List<Patron> loadPatrons() {
        List<Patron> patrons = new ArrayList<>();
        File file = new File(PATRONS_FILE);
        
        if (!file.exists()) {
            return patrons;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Skip header
            String line = reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        patrons.add(Patron.fromCSV(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing patron line: " + line);
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error loading patrons: " + e.getMessage());
        }
        
        return patrons;
    }
    
    
    public static void initializeDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }
}
