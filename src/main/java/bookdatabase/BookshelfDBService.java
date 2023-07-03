package bookdatabase;

import model.Book;
import reading_status_enums.BookshelfReadingStatusTable;

import java.sql.SQLException;
import java.util.ArrayList;

public class BookshelfDBService {

    // Bookshelf tables
    final private BookshelfReadingStatusTable BOOKS_OWNED = BookshelfReadingStatusTable.OWNED;
    final private BookshelfReadingStatusTable BOOKS_WISHLIST = BookshelfReadingStatusTable.WISHLIST;

    // Database connection
    final private BooksDBConnection dbConnection = BooksDBConnection.getInstance();

    public BookshelfDBService() throws SQLException {
    }

    public boolean showConnectionStatus() throws SQLException {
        return dbConnection.checkDBConnection();
    }

    public void showBookList(ArrayList<Book> bookList) {
        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                System.out.printf("Book %d:%n" + book, bookList.indexOf(book) + 1);
            }
            return;
        }
        System.out.println("Query returned no results!%n");
    }

    // CREATE

    // Add book to books_owned bookshelf
    // returns true if insert was successful, else false
    public boolean addToBookshelf(Book book) throws SQLException {
        if (checkIfInShelf(BOOKS_OWNED, book)) {
            System.out.printf("'%s' by '%s' already exists in this Bookshelf.%n"
                    , book.getTitle(), book.getAuthor());
            return false;
        }
        dbConnection.addBook(book, BOOKS_OWNED);
        return true;
    }

    public boolean addToWishlist(Book book) throws SQLException {
        if (checkIfInShelf(BOOKS_WISHLIST, book)) {
            System.out.printf("'%s' by '%s' was already added to the book wishlist.%n"
                    , book.getTitle(), book.getAuthor());
            return false;
        }
        dbConnection.addBook(book, BOOKS_WISHLIST);
        return true;
    }

    public void moveBookFromWishlist(Book book) throws SQLException {
        addToBookshelf(book);
        deleteBook(BOOKS_WISHLIST, book);
    }

    //READ

    public ArrayList<Book> getAllBooks(BookshelfReadingStatusTable bookshelfReadingStatusTableName) throws SQLException {
         return dbConnection.getBooks(bookshelfReadingStatusTableName);
    }

    public ArrayList<Book>  freeBookSearch(BookshelfReadingStatusTable bookshelfReadingStatusTableName, String title, String author) throws SQLException {
        if (!(title.equals("") && author.equals(""))) {
            return dbConnection.freeBookSearch(bookshelfReadingStatusTableName, title, author);
        }
        return new ArrayList<>();
    }

    public boolean checkIfInShelf(BookshelfReadingStatusTable bookshelfReadingStatusTableName, Book book) throws SQLException {
        return dbConnection.searchBook(bookshelfReadingStatusTableName, book).next();
    }

    // UPDATE
    public boolean updateReadingStatus(BookshelfReadingStatusTable bookshelfReadingStatusTableName, Book book) throws SQLException {
        if (checkIfInShelf(bookshelfReadingStatusTableName, book)) {
            dbConnection.updateDoneReading(bookshelfReadingStatusTableName, book);
            return true;
        }

        System.out.println("Book does not exist in bookshelf!");
        return false;
    }

    //DELETE
    public boolean deleteBook(BookshelfReadingStatusTable bookshelfReadingStatusTableName, Book book) throws SQLException {
        if (checkIfInShelf(bookshelfReadingStatusTableName, book)) {
            dbConnection.deleteBook(bookshelfReadingStatusTableName, book);
            return true;
        }

        System.out.println("Book does not exist in bookshelf!");
        return false;
    }

}
