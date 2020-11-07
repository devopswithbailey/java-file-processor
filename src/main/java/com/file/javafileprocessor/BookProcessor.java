package com.file.javafileprocessor;

import org.springframework.batch.item.ItemProcessor;


public class BookProcessor implements ItemProcessor<Book, Book> {
    public Book process(Book book) throws Exception {
        book.setAuthor(book.getAuthor()+"-author");
        book.setTitle(book.getTitle()+"-title");
        return book;
    }
}
