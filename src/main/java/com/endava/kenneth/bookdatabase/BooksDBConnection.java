package com.endava.kenneth.bookdatabase;

import com.endava.kenneth.objects.Book;
import com.endava.util.BookshelfTable;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class BooksDBConnection {
    //protected modifier maybe not needed?
    private static BooksDBConnection instance = null;
    final private ArrayList<Book> queriedBooks = new ArrayList<>();

    // Hikari Datasource DB connection
    final private Connection connection = createDataSource().getConnection();

    // make constructor inaccessible
    private BooksDBConnection() throws SQLException {
    }

    protected boolean checkDBConnection() throws SQLException{
        return connection.isValid(0);
    }

    // Single db class access
    protected static BooksDBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new BooksDBConnection();
        }
        return instance;
    }

    private static DataSource createDataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/books");
        dataSource.setUsername("root");
        dataSource.setPassword("dataBahn28#");
        return dataSource;
    }

    // Add book to a bookshelf.
    protected void addBook(Book book, BookshelfTable bookshelf) throws SQLException {
        String insertOwnedBook = "INSERT INTO " + bookshelf.getTableName() + " (title, author, date_of_publication, publisher, genre" +
                ", done_reading)" +
                " VALUES(?,?,?,?,?,?)";

        String insertWishlist = "INSERT INTO books_wishlist (title, author, genre) VALUES(?,?,?)";

        PreparedStatement insertBook;

        if (bookshelf == BookshelfTable.OWNED) {
            insertBook = connection.prepareStatement(insertOwnedBook);

            insertBook.setString(1, book.getTitle());
            insertBook.setString(2, book.getAuthor());
            insertBook.setDate(3, book.getDateOfPublication());
            insertBook.setString(4, book.getPublisher());
            insertBook.setString(5, book.getGenre());
            insertBook.setBoolean(6, book.getDoneReading());
        } else {
            insertBook = connection.prepareStatement(insertWishlist);

            insertBook.setString(1, book.getTitle());
            insertBook.setString(2, book.getAuthor());
            insertBook.setString(3, book.getGenre());
        }

        insertBook.executeUpdate();

        System.out.printf("'%s' by '%s' was added to the '%s' Bookshelf.%n", book.getTitle(),
                book.getAuthor(), bookshelf);
    }

    // Selects all books from a given bookshelf.
    protected ArrayList<Book> getBooks(BookshelfTable bookshelf) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf.getTableName());
        ResultSet bookQueryResult = selectBooks.executeQuery();
        createBooksFromQuery(bookQueryResult, bookshelf);
        return queriedBooks;
    }


//CRUD

// INSERT

    // Creates a book object based on a result ser (rows queried from database).
    private void createBooksFromQuery(ResultSet resultSet, BookshelfTable bookshelf) throws SQLException {
        if (bookshelf == BookshelfTable.OWNED) {
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String dateOfPub = resultSet.getString("date_of_publication");
                String publisher = resultSet.getString("publisher");
                String genre = resultSet.getString("genre");
                boolean done = resultSet.getBoolean("done_reading");

                this.queriedBooks.add(new Book(title, author, dateOfPub, publisher, genre, done));
            }
        } else {
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String genre = resultSet.getString("genre");

                this.queriedBooks.add(new Book(title, author, genre));
            }
        }
    }

// SELECT

    // selects a book from the DB, returns a ResultSet!
    protected ResultSet searchBook(BookshelfTable bookshelf, Book book) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf.getTableName() +
                " WHERE title LIKE '%" + book.getTitle() + "%' AND author LIKE '%" + book.getAuthor() + "%'");

        return selectBooks.executeQuery();
    }

    // Looks up a book and returns search results.
    protected ArrayList<Book> freeBookSearch(BookshelfTable bookshelf, String title, String author) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf.getTableName() +
                " WHERE title LIKE '%" + title + "%' OR author LIKE '%" + author + "%'");

        ResultSet searchResults = selectBooks.executeQuery();
        createBooksFromQuery(searchResults, bookshelf);
        return queriedBooks;
    }

//UPDATE
    protected void updateDoneReading(BookshelfTable bookshelf, Book book) throws SQLException {
        ResultSet queryResult = searchBook(bookshelf, book);

        while (queryResult.next()) {
            int id = queryResult.getInt("id");
            boolean readStatus = queryResult.getBoolean("done_reading");

            PreparedStatement updateDone = connection.prepareStatement("UPDATE " + bookshelf.getTableName() +
                    " SET done_reading = NOT done_reading WHERE id = " + id);
            updateDone.executeUpdate();
            System.out.printf("Reading status for '%s' by '%s' was updated to %s.%n", book.getTitle(), book.getAuthor()
                    , readStatus ? "'Done'" : "'Not done'");
        }
    }

// DELETE
    protected void deleteBook(BookshelfTable bookshelf, Book book) throws SQLException {
        ResultSet queryResult = searchBook(bookshelf, book);

        while (queryResult.next()) {
            int id = queryResult.getInt("id");

            PreparedStatement deleteQuery = connection.prepareStatement("DELETE FROM " + bookshelf.getTableName() +
                    " WHERE id = ? LIMIT 1");
            deleteQuery.setInt(1, id);
            deleteQuery.executeUpdate();
            System.out.printf("'%s' by '%s' was deleted from '%s' bookshelf.%n", book.getTitle(),
                    book.getAuthor()
                    , bookshelf.getTableName());
        }
    }
}
