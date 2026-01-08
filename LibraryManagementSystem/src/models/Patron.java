package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Patron implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String patronId;
    private String name;
    private List<String> booksCheckedOut; 
    
    
    public Patron(String patronId, String name) {
        this.patronId = patronId;
        this.name = name;
        this.booksCheckedOut = new ArrayList<>();
    }
    
    public String getPatronId() {
        return patronId;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getBooksCheckedOut() {
        return new ArrayList<>(booksCheckedOut);
    }
    
    public void setName(String name) {
        this.name = name;
    }
    

    public void checkOutBook(String bookId) {
        if (!booksCheckedOut.contains(bookId)) {
            booksCheckedOut.add(bookId);
        }
    }
    
   
    public void returnBook(String bookId) {
        booksCheckedOut.remove(bookId);
    }
    
  
    public int getNumberOfBooksCheckedOut() {
        return booksCheckedOut.size();
    }
    
  
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(patronId).append(",").append(name).append(",");
        
        // Add checked out books as semicolon-separated list
        if (!booksCheckedOut.isEmpty()) {
            for (int i = 0; i < booksCheckedOut.size(); i++) {
                sb.append(booksCheckedOut.get(i));
                if (i < booksCheckedOut.size() - 1) {
                    sb.append(";");
                }
            }
        }
        
        return sb.toString();
    }
    
   
    public static Patron fromCSV(String csv) {
        String[] parts = csv.split(",", 3);
        Patron patron = new Patron(parts[0], parts[1]);
        
        if (parts.length > 2 && !parts[2].isEmpty()) {
            String[] books = parts[2].split(";");
            for (String bookId : books) {
                patron.checkOutBook(bookId);
            }
        }
        
        return patron;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Books Checked Out: %d",
                patronId, name, booksCheckedOut.size());
    }
    
    
    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("ID: %s | Name: %s\n", patronId, name));
        sb.append(String.format("Books Checked Out: %d\n", booksCheckedOut.size()));
        
        if (!booksCheckedOut.isEmpty()) {
            sb.append("Book IDs: ");
            sb.append(String.join(", ", booksCheckedOut));
        }
        
        return sb.toString();
    }
}
