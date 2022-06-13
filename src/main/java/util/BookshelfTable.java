package util;

public enum BookshelfTable {
    OWNED("books_owned"),
    WISHLIST("books_wishlist");

    final String shelfTable;

    BookshelfTable(String shelfTable) {
        this.shelfTable = shelfTable;
    }

    public String getTableName() {
        return shelfTable;
    }
}
