// IBookManager.aidl
package com.imdevil.playground.service.aidl;

import com.imdevil.playground.service.aidl.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBookIn(in Book book);
    int addBookOut(out Book book);
    void addBookInOut(inout Book book);
}