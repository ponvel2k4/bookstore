package com.app.bookstore.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.bookstore.constants.EDiscountByBookType;
import com.app.bookstore.exception.BookNotFoundException;
import com.app.bookstore.model.Book;
import com.app.bookstore.model.Checkout;
import com.app.bookstore.repository.BookStoreRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class BookStoreService {
	
	@Autowired
	BookStoreRepository bookStoreRepository;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws BookNotFoundException
	 */
	public Book getBookById(long id) throws BookNotFoundException {
		Book bookInfo = bookStoreRepository.findById(id);
		 if(Objects.isNull(bookInfo)){
			 log.info("Unable to find book id from the book store");
			throw new BookNotFoundException("No matching book id found in book store");
		}
	
		return bookInfo;
	}

	public Book addBookDetails(Book book) {		
		return bookStoreRepository.save(book);
	}
	
	public List<Book> addAllBookDetails(List<Book> bookList) {
		return bookStoreRepository.saveAll(bookList);
	}
	
	/**
	 * 
	 * @param id
	 * @param book
	 * @return
	 * @throws BookNotFoundException
	 */
	public Book updateBookDetails(long id, Book book) throws BookNotFoundException {
		Book bookInfo = bookStoreRepository.findById(id);
		 if(Objects.isNull(bookInfo)){
			 log.info("Unable to find book id from the book store");
			throw new BookNotFoundException("No matching book id found in book store to update");
		}
		else {
			Book updatedBookInfo = Book.builder()
			.author(StringUtils.isNotBlank(book.getAuthor())?book.getAuthor():bookInfo.getAuthor())
			.classification(StringUtils.isNotBlank(book.getClassification())?book.getClassification():bookInfo.getClassification())
			.description(StringUtils.isNotBlank(book.getDescription())?book.getDescription():bookInfo.getDescription())
			.isbn(StringUtils.isNotBlank(book.getIsbn())?book.getIsbn():bookInfo.getIsbn())
			.name(StringUtils.isNotBlank(book.getName())?book.getName():bookInfo.getName())
			.id(bookInfo.getId())
			.price(Objects.isNull(book.getPrice())?bookInfo.getPrice():book.getPrice()).build();
			bookStoreRepository.save(updatedBookInfo);
		}
		return bookInfo;
	}

	public void deleteBookDetails(long id) {
		bookStoreRepository.deleteById(id);		
	}

	/**
	 * 
	 * @return
	 * @throws BookNotFoundException
	 */
	public List<Book> getAllBooksInStore() throws BookNotFoundException {	
		List<Book> allBooksList = bookStoreRepository.findAll();
		if (allBooksList.isEmpty()) {
			 log.info("Book store is empty");
			throw new BookNotFoundException("There are no books in book store currently. Please add!");
		}
		return allBooksList;
	}

	/**
	 * 
	 * @param checkoutRequest
	 * @return
	 */
	public Double checkoutBooks(Checkout checkoutRequest) {
		double totalPrice = 0;
		HashMap<Long, Integer> bookAndQuantity = new HashMap<Long, Integer>();
		
		//Collect all BookIds for checkout and keep track of quantities for each book
		List<Long> allBookIdsForCheckout = new ArrayList<Long>();
		checkoutRequest.getCartItem().stream().forEach(cartItem -> 
				{
					allBookIdsForCheckout.add(cartItem.getBookId());
					bookAndQuantity.put(cartItem.getBookId(), cartItem.getQuantity());
				}
		);
		//Checkout Books that only exists in book store
		List<Book> allBooksDbList = bookStoreRepository.findAll();
		List<Book> booksToCheckout = allBooksDbList.stream()
				.filter(book -> allBookIdsForCheckout.contains(book.getId())).collect(Collectors.toList());
		
		//If any promotion code received in request, then apply the discount as per the book classification, otherwise proceed normally
		if (StringUtils.isBlank(checkoutRequest.getPromotionCode())) {
			totalPrice = booksToCheckout.stream().mapToDouble(book -> (bookAndQuantity.get(book.getId()) * book.getPrice())).sum();
		} else { 
			totalPrice = getTotalPriceWithDiscount(booksToCheckout, bookAndQuantity);
		}
		return totalPrice;
	}
	
	private double getTotalPriceWithDiscount(List<Book> booksToCheckout, HashMap<Long, Integer> bookAndQuantity) {
		double totalPriceAfterDisc = booksToCheckout.stream()
				.mapToDouble(
						book -> { 
							String bookType = book.getClassification().toUpperCase();
							int quantity = bookAndQuantity.get(book.getId());
							double price = book.getPrice();	
							int discPercentage = EDiscountByBookType.findByType(bookType) != null?EDiscountByBookType.findByType(bookType).getDiscountPercentage():0;
							Double result = discPercentage==0?quantity * price: quantity *  (price - (price * discPercentage)/ 100);							
							return result;
						}
					)
				.sum();
		return totalPriceAfterDisc;
	}

}
