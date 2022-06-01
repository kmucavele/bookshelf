package bookdatabase;

import objects.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BooksDBConnection {
    final private List<Book> queriedBooks = new ArrayList<>();
    private Connection connection = getConnection();

    // create class obj
    private static BooksDBConnection instance = null;

    // make constructor inaccessible
    private BooksDBConnection() throws SQLException {
    }

    public static BooksDBConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new BooksDBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/books",
                        "root", "dataBahn28#");

        //test connection
        //System.out.println("1 - Connection is " + connection.isValid(0));
        return connection;
    }


//CRUD

// INSERT
    /**
     * Add book to a bookshelf.
     * @param book Book object.
     * @param bookshelf Bookshelf the book should be added to (db table name).
     * @throws SQLException
     */
    public void addBook(Book book, String bookshelf) throws SQLException {
        String insertOwnedBook = "INSERT INTO " + bookshelf + " (title, author, date_of_publication, publisher, genre" +
                ", done_reading)" +
                " VALUES(?,?,?,?,?,?)";

        String insertWishlist = "INSERT INTO books_wishlist (title, author, genre) VALUES(?,?,?)";

        PreparedStatement insertBook;

        if (bookshelf.equals("books_owned")) {
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

// SELECT
    /**
     * Selects all books from a given bookshelf.
     * @param bookshelf Bookshelf table name in db.
     * @return ArrayList of all Book objects given bookshelf.
     * @throws SQLException
     */

    protected List<Book> getBooks(String bookshelf) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf);
        ResultSet bookQueryResult = selectBooks.executeQuery();
        createBooks(bookQueryResult, bookshelf);
        return queriedBooks;
    }

    /**
     * Creates a book object based on a result ser (rows queried from database).
     * @param resultSet Result rows from database.
     * @param bookshelf Bookshelf table name in db.
     * @throws SQLException
     */
    private void createBooks(ResultSet resultSet, String bookshelf) throws SQLException {
        if (bookshelf.equals("books_owned")) {
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

    /**
     * Looks up a book. Only checks its existence!
     * @param bookshelf Bookshelf table name in db.
     * @param book Book object.
     * @return True if rows where returned from query, else false.
     * @throws SQLException
     */
    protected ResultSet searchBook(String bookshelf, Book book) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf +
                " WHERE title LIKE '%" + book.getTitle() + "%' AND author LIKE '%" + book.getAuthor() + "%'");

        return selectBooks.executeQuery();
    }

    /**
     * Looks up a book and returns search results.
     * @param bookshelf Bookshelf table name in db.
     * @param title Book title.
     * @param author Book Author.
     * @return ArrayList of all matching Books.
     * @throws SQLException
     */
    protected List<Book> searchBook(String bookshelf, String title, String author) throws SQLException {
        PreparedStatement selectBooks = connection.prepareStatement("SELECT * FROM " + bookshelf +
                " WHERE title LIKE '%" + title + "%' OR author LIKE '%" + author + "%'");

        ResultSet searchResults = selectBooks.executeQuery();
        createBooks(searchResults, bookshelf);
        return queriedBooks;
    }

//UPDATE

    protected void updateDoneReading(String bookshelf, Book book) throws SQLException{
        ResultSet resultID = searchBook(bookshelf, book);

        while (resultID.next()){
            int id = resultID.getInt("id");

            PreparedStatement updateDone = connection.prepareStatement("UPDATE " + bookshelf +
                    " SET done_reading = NOT done_reading WHERE id = " + id);
            updateDone.executeUpdate();
        }
    }
}
