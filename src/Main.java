import objects.Book;
import bookdatabase.Bookshelf;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Bookshelf whiteBookshelf = new Bookshelf("White Bookshelf");

        Book Luther_NCross = new Book("Luther. Die Drohung",
                "Neil Cross", "2012-02-13",
                "DuMont Buchverlag", "Thriller");

        Book GoneGirl_GFlynn = new Book("Gone Girl",
                "Gillian Flynn", "2012-02-13",
                "DuMont Buchverlag", "Novel");

        whiteBookshelf.getAllBooks("books_owned");

    }
}