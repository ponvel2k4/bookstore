package com.app.bookstore.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public enum EDiscountByBookType {
	 	COMIC(0),
	    SCIENCE(3),
	    FICTION(10),
	    MATHS(15);
	    private final Integer discountPercentage;	    
	    
	    public static EDiscountByBookType findByType(String type) {
	    	EDiscountByBookType result = null;
	        for (EDiscountByBookType eDiscType : values()) {
	            if (eDiscType.name().equalsIgnoreCase(type)) {
	                result = eDiscType;
	                break;
	            }
	        }
	        return result;
	    }

}
