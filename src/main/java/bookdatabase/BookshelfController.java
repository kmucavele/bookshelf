package bookdatabase;

import objects.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class BookshelfController {

    // Bookshelf tables
    BookshelfTable BOOKS_OWNED = BookshelfTable.OWNED;
    BookshelfTable BOOKS_WISHLIST = BookshelfTable.WISHLIST;

    // Database connection
    BooksDBConnection dbConnection = BooksDBConnection.getInstance();

    public BookshelfController() throws SQLException {
    }

    public void showBookList(BookshelfTable bookshelfTableName, ArrayList<Book> bookList) {
        System.out.printf("Books in '%s' bookshelf:%n", bookshelfTableName.getTableName());
        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                //System.out.println(book, bookList.indexOf(book));
                System.out.printf("Book %d:%n" + book, bookList.indexOf(book) + 1);
            }
            return;
        }
        System.out.printf("'%s' bookshelf is empty!%n", bookshelfTableName.getTableName());
    }

    // CREATE
    public void addToBookshelf(Book book) throws SQLException {
        if (checkIfInShelf(BOOKS_OWNED, book)) {
            System.out.printf("'%s' by '%s' already exists in this Bookshelf.%n"
                    , book.getTitle(), book.getAuthor());
            return;
        }
        dbConnection.addBook(book, BOOKS_OWNED);
    }

    public void addToWishlist(Book book) throws SQLException {
        if (checkIfInShelf(BOOKS_WISHLIST, book)) {
            System.out.printf("'%s' by '%s' was already added to the book wishlist.%n"
                    , book.getTitle(), book.getAuthor());
            return;
        }
        dbConnection.addBook(book, BOOKS_WISHLIST);
    }

    public void moveBookFromWishlist(Book book) throws SQLException {
        addToBookshelf(book);
        deleteBook(BOOKS_WISHLIST, book);
    }

    //READ

    public void getAllBooks(BookshelfTable bookshelfTableName) throws SQLException {
        ArrayList<Book> books = dbConnection.getBooks(bookshelfTableName);
        showBookList(bookshelfTableName, books);
    }

    public void freeBookSearch(BookshelfTable bookshelfTableName, String title, String author) throws SQLException {
        if (!(title.equals("") && author.equals(""))) {
            showBookList(bookshelfTableName, dbConnection.freeBookSearch(bookshelfTableName, title, author));
            return;
        }

        System.out.println("No Results for title = '" + title + "', author = '" + author + "'"
                + "\nMake sure to enter search values");
    }

    private boolean checkIfInShelf(BookshelfTable bookshelfTableName, Book book) throws SQLException {
        return dbConnection.searchBook(bookshelfTableName, book).next();
    }

    // UPDATE
    public void updateReadingStatus(BookshelfTable bookshelfTableName, Book book) throws SQLException {
        dbConnection.updateDoneReading(bookshelfTableName, book);
    }

    //DELETE
    public void deleteBook(BookshelfTable bookshelf, Book book) throws SQLException {
        if (checkIfInShelf(bookshelf, book)) {
            dbConnection.deleteBook(bookshelf, book);
        } else {
            System.out.println("Book does not exist in bookshelf!");
        }
    }

}
