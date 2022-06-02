import bookdatabase.BookshelfController;
import objects.*;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        BookshelfTable owned = BookshelfTable.OWNED;
        BookshelfTable wishlist = BookshelfTable.WISHLIST;

        BookshelfController ikeaBookshelf = new BookshelfController();

        Book Luther_NCross = new Book("Luther. Die Drohung",
                "Neil Cross", "2012-02-13",
                "DuMont Buchverlag", "Thriller");


        ikeaBookshelf.getAllBooks(owned);
    }
}