package com.app.bookstore.controller;

import static com.app.bookstore.constants.ApiConstants.BOOK;
import static com.app.bookstore.constants.ApiConstants.VERSION_1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bookstore.exception.BookNotFoundException;
import com.app.bookstore.model.Book;
import com.app.bookstore.model.Checkout;
import com.app.bookstore.service.BookStoreService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(VERSION_1 + BOOK)
@Log4j2
@Api(value = "Book Store")
public class StoreController {

	@Autowired
	BookStoreService bookStoreService;

	/**
	 * 
	 * @return
	 * @throws BookNotFoundException
	 */
	@ApiOperation(value = "Get all the books in store")
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Book>> getAllBooksInfo() throws BookNotFoundException {
		log.info("Received request for retrieving all books");
		return ResponseEntity.ok().body(bookStoreService.getAllBooksInStore());
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws BookNotFoundException
	 */
	@ApiOperation(value = "Get a book detail by its book id")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> getBookInfo(@PathVariable(value = "id") long id) throws BookNotFoundException {
		log.info("Received Book information request for Book Id: " + id);
		return ResponseEntity.ok().body(bookStoreService.getBookById(id));
	}

	/**
	 * 
	 * @param book
	 * @return
	 */
	@ApiOperation(value = "Add a book to the book store")
	@PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> addBookInfo(@RequestBody Book book) {
		log.info("Received request for adding a book");
		Book newBookInfo = bookStoreService.addBookDetails(book);
		return new ResponseEntity<Book>(newBookInfo, HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param bookList
	 * @return
	 */
	@ApiOperation(value = "Add all the books to the book store")
	@PostMapping(value = "/addAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Book>> addAllBooks(@RequestBody List<Book> bookList) {
		log.info("Received request for adding all the books");
		List<Book> books = bookStoreService.addAllBookDetails(bookList);
		return new ResponseEntity<List<Book>>(books, HttpStatus.CREATED);
	}

	/**
	 * 
	 * @param id
	 * @param book
	 * @return
	 * @throws BookNotFoundException
	 */
	@ApiOperation(value = "Update book information using book id")
	@PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Book> updateBookInfo(@PathVariable("id") long id, @RequestBody Book book)
			throws BookNotFoundException {
		log.info("Received update book request for id: "+id);
		Book newBookInfo = bookStoreService.updateBookDetails(id, book);
		return new ResponseEntity<Book>(newBookInfo, HttpStatus.OK);
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "Delete a book by using book id")
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<HttpStatus> deleteBookInfo(@PathVariable("id") long id) {
		log.info("Received delete book request for id: "+id);
		bookStoreService.deleteBookDetails(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * 
	 * @param checkoutRequest
	 * @return
	 */
	@ApiOperation(value = "Checkout books from store")
	@PostMapping(value = "/checkout")
	public ResponseEntity<String> checkoutBooks(@RequestBody Checkout checkoutRequest) {
		Double totalPayableAmount = bookStoreService.checkoutBooks(checkoutRequest);
		return new ResponseEntity<String>("Total Payable Amount: " + totalPayableAmount, HttpStatus.OK);
	}

}
