# Small Digital Bookshelf Project

## Description
This is a simple digital book database app. 

The idea behind the bookshelf app was to have two main bookshelves.
One where you store already owned books and another with books that you want to read in the future but not own yet
(wishlist).

The project was made in the first year of my apprenticeship and servers for demonstrative purposes.

__A second project build with the Spring Boot Framework is based on this project__

### Functionalities
* Users can:
  * Add a new book to the owned-books or wishlist bookshelf
  * Move a book from the wishlist- to the owned-books bookshelf
  * Return a list of all books
  * Check if a book is in a bookshelf (owned-books or wishlist)
  * Search both bookshelves by author or/and title
  * Update the reading status of a book
  * Delete a book


* The goal was to get familiar with:
  * JDBC
  * Connecting to a databases in Java
  * Database connection pools
  * Writing tests with JUnit

## Usage

### Prerequisites
* Install MySQL
* Install the maven dependency manager

### Install
1. Clone this repository
2. Run `mvn install` to install the dependencies from the pom file
2. Create a database (e.g.: named `books`)
3. Read in the mysqldump with the following command:
  ```shell
    mysqldump -u <your username> -p <your database name> < books_localhost-dump.sql
  ```
  * The mysqldump contains the table schemas 
3. Create an `.env` file and fill it with your database credentials using these environment keys:
  ```shell
    DATABASE_NAME=""
    DATABASE_USER=""
    DATABASE_PASSWORD=""
  ```
  * In case your username or password values have special characters, put them in quotes
4. Optionally test if the database connection works by running this _inside of_ [**_the test file_**](src/test/java/com/me/kenneth/bookdatabase/BookshelfDBServiceTest.java):
```java
    @Test
    void database_connection_test_should_be_true() throws SQLException {
        assertThat(bookshelfDBService.showConnectionStatus()).isTrue();
    }
```
---
## Build With
* Java (POJO)
* MySQL
* HikariCP (for connection pools)
* JUnit 5
* Maven