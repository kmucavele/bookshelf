package bookdatabase;

import objects.*;

import java.sql.*;
import java.util.ArrayList;

public class BooksDBConnection {
    // instance
    private static BooksDBConnection instance = null;
    final private ArrayList<Book> queriedBooks = new ArrayList<>();    private Connection connection = getConnection();

    // make constructor inaccessible
    private BooksDBConnection() throws SQLException {
    }

    // Single db class access
    protected static BooksDBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new BooksDBConnection();
        }
        return instance;
    }

    // jdbc connection
    protected Connection getConnection() throws SQLException {
        connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/books",
                        "root", "dataBahn28#");

        //test connection
        //System.out.println("1 - Connection is " + connection.isValid(0));
        return connection;
    }

    /**
     * Add book to a bookshelf.
     *
     * @param book      Book object.
     * @param bookshelf Bookshelf the book should be added to (db table name).
     * @throws SQLException throws SQLException
     */
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

    /**
     * Selects all books from a given bookshelf.
     *
     * @param bookshelf Bookshelf table name in db.
     * @return ArrayList of all Book objects given bookshelf.
     * @throws SQLException throws SQLException
     */

    protected ArrayList<Book> getBooks(BookshelfTable bookshelf) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf.getTableName());
        ResultSet bookQueryResult = selectBooks.executeQuery();
        createBooksFromQuery(bookQueryResult, bookshelf);
        return queriedBooks;
    }


//CRUD

// INSERT

    protected void moveBookFromWishlist(Book book) throws SQLException {
        searchBook(BookshelfTable.WISHLIST, book);
    }

    /**
     * Creates a book object based on a result ser (rows queried from database).
     *
     * @param resultSet Result rows from database.
     * @param bookshelf Bookshelf table name in db.
     * @throws SQLException throws SQLException
     */
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

    /**
     * Looks up a book. Only checks its existence!
     *
     * @param bookshelf Bookshelf table name in db.
     * @param book      Book object.
     * @return ResultSet from search query.
     * @throws SQLException throws SQLException
     */
    protected ResultSet searchBook(BookshelfTable bookshelf, Book book) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf.getTableName() +
                " WHERE title LIKE '%" + book.getTitle() + "%' AND author LIKE '%" + book.getAuthor() + "%'");

        return selectBooks.executeQuery();
    }

    /**
     * Looks up a book and returns search results.
     *
     * @param bookshelf Bookshelf table name in db.
     * @param title     Book title.
     * @param author    Book Author.
     * @return ArrayList of all matching Books.
     * @throws SQLException throws SQLException
     */
    protected ArrayList<Book> freeBookSearch(BookshelfTable bookshelf, String title, String author) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf.getTableName() +
                " WHERE title LIKE '%" + title + "%' OR author LIKE '%" + author + "%'");

        ResultSet searchResults = selectBooks.executeQuery();
        createBooksFromQuery(searchResults, bookshelf);
        return queriedBooks;
    }

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

//UPDATE

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
