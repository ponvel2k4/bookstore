package com.app.bookstore.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.app.bookstore.exception.BookNotFoundException;
import com.app.bookstore.model.Book;
import com.app.bookstore.model.CartItem;
import com.app.bookstore.model.Checkout;
import com.app.bookstore.repository.BookStoreRepository;

@ExtendWith(MockitoExtension.class)
class BookStoreServiceTest {

	@Mock
	private BookStoreRepository bookStoreRepository;

	@InjectMocks
	private BookStoreService bookStoreService;

	private Book book;

	private List<Book> bookList;

	@BeforeEach
	public void setUp() throws Exception {

		book = Book.builder().author("Richard").classification("GENERAL").description("story book")
				.isbn("8735-88-453-98").name("The Patriot").build();
		bookList = new ArrayList<Book>();
		bookList.add(book);

	}

	@Test
	public void getBookByIdTest() throws BookNotFoundException {
		when(bookStoreRepository.findById(anyLong())).thenReturn(book);
		Book bookObj = this.bookStoreService.getBookById(1);
		assertNotNull(bookObj);
	}

	@Test
	public void throwExceptionForBookIdNotFound() throws BookNotFoundException {
		when(bookStoreRepository.findById(anyLong())).thenReturn(null);
		assertThrows(BookNotFoundException.class, () -> {
			this.bookStoreService.getBookById(1);
		});
	}

	@Test
	public void addBookDetailsTest() {
		when(bookStoreRepository.save(book)).thenReturn(book);
		assertNotNull(this.bookStoreService.addBookDetails(book));
	}

	@Test
	public void addAllBooksTest() {
		when(bookStoreRepository.saveAll(bookList)).thenReturn(bookList);
		assertNotNull(this.bookStoreService.addAllBookDetails(bookList));
	}

	@Test
	public void updateBookDetailsTest() throws BookNotFoundException {
		Book bookToUpdate = Book.builder().author("Peter").classification("GENERAL").description("story book")
				.isbn("8735-88-453-98").id(2).price(Double.valueOf(600)).name("The Troy").build();
		when(bookStoreRepository.findById(anyLong())).thenReturn(book);
		assertNotNull(this.bookStoreService.updateBookDetails(1, bookToUpdate));
	}

	@Test
	public void updateNonExistingBookTest() throws BookNotFoundException {
		when(bookStoreRepository.findById(anyLong())).thenReturn(null);
		assertThrows(BookNotFoundException.class, () -> {
			this.bookStoreService.updateBookDetails(1, book);
		});
	}

	@Test
	public void deleteBookTest() {
		this.bookStoreService.deleteBookDetails(1L);
		verify(bookStoreRepository, times(1)).deleteById(1L);
	}

	@Test
	public void getAllBooksInStoreTest() throws BookNotFoundException {
		List<Book> bookList = new ArrayList<Book>();
		bookList.add(book);
		when(bookStoreRepository.findAll()).thenReturn(bookList);
		assertEquals(this.bookStoreService.getAllBooksInStore(), bookList);
	}

	@Test
	public void getBooksFromStoreWhenNoBooksTest() throws BookNotFoundException {
		when(bookStoreRepository.findAll()).thenReturn(new ArrayList<Book>());
		assertThrows(BookNotFoundException.class, () -> {
			this.bookStoreService.getAllBooksInStore();
		});
	}

	@Test
	public void testCheckoutOperation() {
		// No promotion code provided in request, so no discount applied
		List<CartItem> cartItem = List.of(new CartItem(1L, 1), new CartItem(2L, 3), new CartItem(3L, 2),
				new CartItem(4L, 5));
		Checkout checkoutRequest = new Checkout();
		checkoutRequest.setCartItem(cartItem);

		Book book1 = Book.builder().id(1L).price(Double.valueOf("200")).classification("SCIENCE").build();
		Book book2 = Book.builder().id(2L).price(Double.valueOf("100")).classification("FICTION").build();
		Book book3 = Book.builder().id(3L).price(Double.valueOf("500")).classification("GENERAL").build();
		Book book4 = Book.builder().id(4L).price(Double.valueOf("600")).classification("MATHS").build();
		Book book5 = Book.builder().id(5L).price(Double.valueOf("700")).classification("COMIC").build();

		List<Book> booksInStore = List.of(book1, book2, book3, book4, book5);
		when(bookStoreRepository.findAll()).thenReturn(booksInStore);
		assertEquals(this.bookStoreService.checkoutBooks(checkoutRequest), 4500);

		// Apply promotion code. Now discounts will be calculated based on classification
		checkoutRequest.setPromotionCode("DISC");
		assertEquals(this.bookStoreService.checkoutBooks(checkoutRequest), 4014);
	}
}
