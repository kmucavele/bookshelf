package reading_status_enums;

public enum BookshelfReadingStatusTable {
    OWNED("books_owned"),
    WISHLIST("books_wishlist");

    final String shelfTable;

    BookshelfReadingStatusTable(String shelfTable) {
        this.shelfTable = shelfTable;
    }

    public String getTableName() {
        return shelfTable;
    }
}
