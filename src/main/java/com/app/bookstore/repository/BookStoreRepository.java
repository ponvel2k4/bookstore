package com.app.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.bookstore.model.Book;

@Repository
public interface BookStoreRepository extends JpaRepository<Book, Long> {
		Book findById(long id);	
}
