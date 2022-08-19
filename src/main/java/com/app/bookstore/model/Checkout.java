package com.app.bookstore.model;


import java.util.List;

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
public class Checkout {	
	
	@JsonProperty("Cart")
	private List<CartItem> cartItem;
	
	@JsonProperty("PromotionCode")
	private String promotionCode;
	
}
