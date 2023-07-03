import model.Book;
import bookdatabase.BookshelfDBService;
import reading_status_enums.BookshelfReadingStatusTable;

import java.sql.SQLException;

public class Main {



    public static void main(String[] args) throws SQLException {
        BookshelfReadingStatusTable owned = BookshelfReadingStatusTable.OWNED;
        BookshelfReadingStatusTable wishlist = BookshelfReadingStatusTable.WISHLIST;

        BookshelfDBService ikeaBookshelf = new BookshelfDBService();

        // Book <title>__<f. name>_<l. name>
        Book LUTHER__N_C = new Book("Luther. Die Drohung",
                "Neil Cross", "2012-02-13",
                "DuMont Buchverlag", "Thriller");

        Book SCAR__J_K = new Book("Scar",
                "Jack Ketchum", "2017-04-10",
                "Heyne", "Novel");

        Book AUSGELOESCHT__C_M = new Book("Ausgelöscht",
                "Cody McFadyen", "2013-04-19",
                "Bastei Lübbe", "Thriller");

        Book STILLE_BESTIE__C_C = new Book("Die stille Bestie",
                "Chris Carter", "2015-09-11",
                "Ullstein Taschenbuch Verlag", "Thriller");

        Book AUGENSAMMLER__S_F = new Book("Der Augensammler",
                "Sebastian Fitzek", "2011-06-01",
                "Knaur Taschenbuch", "Psychothriller");


        ikeaBookshelf.showBookList(ikeaBookshelf.getAllBooks(owned));
    }
}