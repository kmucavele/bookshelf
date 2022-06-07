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

    String[] SEARCH_TEST_BOOK_AUTHORS = {LUTHER__N_C.getTitle(), AUGENSAMMLER__S_F.getTitle()};

    // start state books_owned = {LUTHER__N_C}
    // start state books_wishlist = {AUGENSAMMLER__S_F}


    BookshelfController bookshelfController = new BookshelfController();


    @Test
    void database_connection_test_should_be_true() throws SQLException {
        assertThat(bookshelfController.showConnectionStatus()).isTrue();
    }

    @Test
    void add_book_to_bookshelf_should_be_true() throws SQLException {
        assertThat(bookshelfController.addToBookshelf(SCAR__J_K)).isTrue();
    }

    @Test
    void add_book_to_bookshelf_should_be_false() throws SQLException {
        assertThat(bookshelfController.addToBookshelf(SCAR__J_K)).isFalse();
    }

    @Test
    void add_book_to_wishlist_should_be_true() throws SQLException {
        assertThat(bookshelfController.addToWishlist(AUSGELOESCHT__C_M)).isTrue();
    }

    @Test
    void add_book_to_wishlist_should_be_false() throws SQLException {
        assertThat(bookshelfController.addToWishlist(AUSGELOESCHT__C_M)).isFalse();
    }

   @Test
    void check_if_book_was_moved_should_be_true() throws SQLException {
        bookshelfController.moveBookFromWishlist(AUGENSAMMLER__S_F);
        assertThat(bookshelfController.checkIfInShelf(BookshelfTable.OWNED, AUGENSAMMLER__S_F)).isTrue();
        assertThat(bookshelfController.checkIfInShelf(BookshelfTable.WISHLIST, AUGENSAMMLER__S_F)).isFalse();
   }


    @Test
    void books_owned_bookshelf_should_have_3_books() throws SQLException {
        assertThat(bookshelfController.getAllBooks(BookshelfTable.OWNED).size()).isEqualTo(3);
    }

    @Test
    void books_wishlist_bookshelf_should_have_1_book() throws SQLException {
        assertThat(bookshelfController.getAllBooks(BookshelfTable.WISHLIST).size()).isEqualTo(1);
    }

    @Test
    void should_return_results_for_title_er_author_n() throws SQLException {
        for (int i = 0; i < SEARCH_TEST_BOOK_AUTHORS.length; i++){
            assertThat(bookshelfController.freeBookSearch(BookshelfTable.OWNED, "er", "n").get(i).getTitle()
            ).isEqualTo(SEARCH_TEST_BOOK_AUTHORS[i]);
        }
    }


    @Test
    void check_if_book_is_in_bookshelf_should_be_true() throws SQLException {
        assertThat(bookshelfController.checkIfInShelf(BookshelfTable.OWNED, LUTHER__N_C)).isTrue();
    }

    @Test
    void check_if_book_is_not_in_bookshelf_should_be_false() throws SQLException {
        assertThat(bookshelfController.checkIfInShelf(BookshelfTable.OWNED, STILLE_BESTIE__C_C)).isFalse();
    }

    @Test
    void reading_status_update_should_be_true() throws SQLException {
        assertThat(bookshelfController.updateReadingStatus(BookshelfTable.OWNED, LUTHER__N_C)).isTrue();
    }

    @Test
    void deleted_book_should_be_found() throws SQLException{
        // check if deletion was successful
        assertThat(bookshelfController.deleteBook(BookshelfTable.OWNED, AUGENSAMMLER__S_F)).isTrue();

        // look for deleted book in bookshelf
        assertThat(bookshelfController.checkIfInShelf(BookshelfTable.OWNED, AUGENSAMMLER__S_F)).isFalse();
    }
}
