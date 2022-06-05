package com.endava.kenneth.bookdatabase;

import com.endava.kenneth.objects.Book;
import com.endava.util.BookshelfTable;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BookshelfDatabaseTest {
    public BookshelfDatabaseTest() throws SQLException {
    }

    // Test Books
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
            "Sebastian Fitzek", "Psychothriller");

    Book[] TEST_BOOKS = {LUTHER__N_C, SCAR__J_K, AUSGELOESCHT__C_M, STILLE_BESTIE__C_C, AUGENSAMMLER__S_F};



    BookshelfController bookshelfController = new BookshelfController();


    //TODO: checkDBConnection - check boolean (from DBconnection class)
    @Test
    void database_connection_test_should_be_true() throws SQLException {
        assertThat(bookshelfController.showConnectionStatus()).isTrue();
    }

    // TODO: addToBookshelf - check if successful
    @Test
    void add_book_to_bookshelf_should_be_true() throws SQLException {
        assertThat(bookshelfController.addToBookshelf(AUGENSAMMLER__S_F)).isTrue();
        assertThat(bookshelfController.addToBookshelf(AUGENSAMMLER__S_F)).isFalse();
    }

    // TODO: addToWishlist - check if successful
    @Test
    void add_book_to_wishlist_should_be_true() throws SQLException {
        assertThat(bookshelfController.addToWishlist(AUGENSAMMLER__S_F)).isTrue();
        assertThat(bookshelfController.addToWishlist(AUGENSAMMLER__S_F)).isFalse();
    }

    // TODO: moveBookFromWishlist =
    //      checkIfInShelf for books_wishlist == false
    //      checkIfInShelf for books_owned == true
   @Test
    void check_if_book_was_moved_should_be_true() throws SQLException {
        bookshelfController.moveBookFromWishlist(AUGENSAMMLER__S_F);
        assertThat(bookshelfController.checkIfInShelf(BookshelfTable.OWNED, AUGENSAMMLER__S_F)).isTrue();
        assertThat(bookshelfController.checkIfInShelf(BookshelfTable.WISHLIST, AUGENSAMMLER__S_F)).isFalse();
   }

    //
    // TODO: getAllBooks - check

    // TODO: freeBookSearch -
    //      check if result list contains searched book or empty
    //
    // TODO: checkIfInShelf =
    //     check existence/non-existence of given book == true or false
    //
    // TODO: updateDoneReading =
    //   check if update-done was changed == true/false
    //
    // TODO: deleteBook =
    //   search for deleted book == true/false
}
