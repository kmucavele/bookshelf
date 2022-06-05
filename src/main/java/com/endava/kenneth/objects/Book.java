package com.endava.kenneth.objects;

import java.sql.Date;

public class Book {
    final private String title, author, genre;
    private String publisher;
    private Date dateOfPublication;
    private boolean doneReading;

    // normal Book constructor
    public Book(String title, String author, String dateOfPublication, String publisher, String genre,
                boolean doneReading) {
        this.title = title;
        this.author = author;
        this.dateOfPublication = Date.valueOf(dateOfPublication);
        this.publisher = publisher;
        this.genre = genre;
        this.doneReading = doneReading;
    }

    public Book(String title, String author, String dateOfPublication, String publisher, String genre) {
        this.title = title;
        this.author = author;
        this.dateOfPublication = Date.valueOf(dateOfPublication);
        this.publisher = publisher;
        this.genre = genre;
    }

    // Constructor for the wishlist
    public Book(String title, String author, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getGenre() {
        return genre;
    }

    public Date getDateOfPublication() {
        return dateOfPublication;
    }


    @Override
    public String toString() {
        return "\tTitle = '" + title + "'\n" +
                "\tAuthor = '" + author + "'\n" +
                "\tGenre = '" + genre + "'\n" +
                "\tPublisher = '" + publisher + "'\n" +
                "\tDate of publication = '" + dateOfPublication + "'\n" +
                "\tDoneReading = '" + (doneReading ? "Yes" : "No") + "'\n\n";
    }

    public boolean getDoneReading() {
        return doneReading;
    }

}
