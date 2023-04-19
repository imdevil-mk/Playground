package com.imdevil.playground.service.aidl;

import com.imdevil.playground.service.aidl.Book;

interface IBookChangeListener {
    void onBookChanged(in List<Book> books);
}