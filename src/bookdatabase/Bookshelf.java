package bookdatabase;

import bookdatabase.BooksDBConnection;
import objects.Book;

import java.sql.SQLException;
import java.util.List;

public class Bookshelf {
    final private String BOOKSHELF_NAME;
    BooksDBConnection dbConnection = BooksDBConnection.getInstance();

    public Bookshelf(String name) throws SQLException {
        this.BOOKSHELF_NAME = name;
    }

    public String getNAME(){
        return BOOKSHELF_NAME;
    }

    public void showBookList(List<Book> bookList){
        try {
            for (Book book : bookList) {
                System.out.println(book);
            }
        } catch (NullPointerException e) {
            System.out.println("Bookshelf is empty");
        }
    }

    // Create Ops
    public void addToBookshelf(Book book) throws SQLException {
        if(checkIfInShelf("books_owned", book)){
            System.out.printf("'%s' by '%s' already exists in this Bookshelf."
                    , book.getTitle(), book.getAuthor());
        }
        dbConnection.addBook(book, "books_owned");
    }

    public void addToWishlist(Book book) throws SQLException{
        if(checkIfInShelf("books_wishlist", book)){
            System.out.printf("'%s' by '%s' was already added to the book wishlist."
                    , book.getTitle(), book.getAuthor());
        }
        dbConnection.addBook(book, "books_wishlist");
    }

    //Read Ops

    public void getAllBooks(String bookshelfTableName) throws SQLException{
        List<Book> books = dbConnection.getBooks(bookshelfTableName);
        showBookList(books);
    }

    public void searchBook(String bookshelfTableName, String title, String author) throws SQLException{
        if(!(title.equals("") && author.equals(""))){
            showBookList(dbConnection.searchBook(bookshelfTableName, title, author));
        }

        System.out.println("No Results for title = '" + title  + "', author = '" + author + "'"
                + "\nMake sure to enter search values");
    }

    private boolean checkIfInShelf(String bookshelfTableName, Book book) throws SQLException{
            return dbConnection.searchBook(bookshelfTableName, book);
    }



}
