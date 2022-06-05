package com.endava.kenneth.bookdatabase;

import com.endava.kenneth.objects.Book;
import com.endava.util.BookshelfTable;

import java.sql.SQLException;
import java.util.ArrayList;

public class BookshelfController {

    // Bookshelf tables
    final private BookshelfTable BOOKS_OWNED = BookshelfTable.OWNED;
    final private BookshelfTable BOOKS_WISHLIST = BookshelfTable.WISHLIST;

    // Database connection
    final private BooksDBConnection dbConnection = BooksDBConnection.getInstance();

    public BookshelfController() throws SQLException {
    }

    public boolean showConnectionStatus() throws SQLException {
        return dbConnection.checkDBConnection();
    }

    public void showBookList(ArrayList<Book> bookList) {
        if (!bookList.isEmpty()) {
            for (Book book : bookList) {
                //System.out.println(book, bookList.indexOf(book));
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

    public ArrayList<Book> getAllBooks(BookshelfTable bookshelfTableName) throws SQLException {
         return dbConnection.getBooks(bookshelfTableName);
    }

    public ArrayList<Book>  freeBookSearch(BookshelfTable bookshelfTableName, String title, String author) throws SQLException {
        if (!(title.equals("") && author.equals(""))) {
            return dbConnection.freeBookSearch(bookshelfTableName, title, author);
        }
        return new ArrayList<>();
    }

    public boolean checkIfInShelf(BookshelfTable bookshelfTableName, Book book) throws SQLException {
        return dbConnection.searchBook(bookshelfTableName, book).next();
    }

    // UPDATE
    public boolean updateReadingStatus(BookshelfTable bookshelfTableName, Book book) throws SQLException {
        if (checkIfInShelf(bookshelfTableName, book)) {
            dbConnection.updateDoneReading(bookshelfTableName, book);
            return true;
        }

        System.out.println("Book does not exist in bookshelf!");
        return false;
    }

    //DELETE
    public boolean deleteBook(BookshelfTable bookshelfTableName, Book book) throws SQLException {
        if (checkIfInShelf(bookshelfTableName, book)) {
            dbConnection.deleteBook(bookshelfTableName, book);
            return true;
        }

        System.out.println("Book does not exist in bookshelf!");
        return false;
    }

}
