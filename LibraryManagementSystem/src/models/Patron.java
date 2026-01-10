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
        sb.append(escapeCSV(patronId)).append(",");
        
        sb.append(escapeCSV(name)).append(",");
        
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
    
    private static String escapeCSV(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
   
    public static Patron fromCSV(String csv) {
        String[] parts = parseCSVLine(csv);
        Patron patron = new Patron(parts[0], parts[1]);
        
        if (parts.length > 2 && !parts[2].isEmpty()) {
            String[] books = parts[2].split(";");
            for (String bookId : books) {
                patron.checkOutBook(bookId);
            }
        }
        
        return patron;
    }
    
    private static String[] parseCSVLine(String line) {
        java.util.List<String> fields = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        int i = 0;
        
        while (i < line.length()) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
            i++;
        }
        
        fields.add(current.toString());
        return fields.toArray(new String[0]);
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
