package models;

import java.io.Serializable;


public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bookId;
    private String title;
    private String author;
    private int yearOfPublication;
    private BookStatus status;
    private String checkedOutBy; 
    
   
    public enum BookStatus {
        AVAILABLE,
        CHECKED_OUT
    }
    
   
    public Book(String bookId, String title, String author, int yearOfPublication) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.yearOfPublication = yearOfPublication;
        this.status = BookStatus.AVAILABLE;
        this.checkedOutBy = null;
    }
    
    // Getters
    public String getBookId() {
        return bookId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public int getYearOfPublication() {
        return yearOfPublication;
    }
    
    public BookStatus getStatus() {
        return status;
    }
    
    public String getCheckedOutBy() {
        return checkedOutBy;
    }
    

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }
    
    public void setStatus(BookStatus status) {
        this.status = status;
    }
    
    public void setCheckedOutBy(String patronId) {
        this.checkedOutBy = patronId;
    }
    
 
    public String toCSV() {
        StringBuilder csv = new StringBuilder();
        csv.append(escapeCSV(bookId)).append(",");
        csv.append(escapeCSV(title)).append(",");
        csv.append(escapeCSV(author)).append(",");
        csv.append(escapeCSV(String.valueOf(yearOfPublication))).append(",");
        csv.append(escapeCSV(status.toString())).append(",");
        csv.append(escapeCSV(checkedOutBy != null ? checkedOutBy : ""));
        return csv.toString();
    }
    
    private static String escapeCSV(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    public static Book fromCSV(String csv) {
        String[] parts = parseCSVLine(csv);
        
        String bookId = parts[0];
        String title = parts[1];
        String author = parts[2];
        int year = Integer.parseInt(parts[3]);
        
        Book book = new Book(bookId, title, author, year);
        book.setStatus(BookStatus.valueOf(parts[4]));
        
        if (parts.length > 5 && !parts[5].isEmpty()) {
            book.setCheckedOutBy(parts[5]);
            System.out.println("Loaded checked out book: " + book.getTitle() + " by Patron ID: " + parts[5]);
        }
        return book;
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
        return String.format("ID: %s | Title: %s | Author: %s | Year: %d | Status: %s%s",
                bookId, title, author, yearOfPublication, status,
                (checkedOutBy != null ? " (Checked out by: " + checkedOutBy + ")" : ""));
    }
}
