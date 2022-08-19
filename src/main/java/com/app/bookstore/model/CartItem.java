package com.app.bookstore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel
public class CartItem {
	
	@JsonProperty("BookId")
	private Long bookId;
	
	@JsonProperty("Quantity")
	private Integer quantity=1;

}
