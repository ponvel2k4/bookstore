package com.app.bookstore.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum EPromotionCodes {
	
	EID(5),
	CHRISTMAS(7),
	NEWYEAR(10);
	private final Integer promotionPercentage;

}
