package com.increff.project.util;

public class ConversionGeneric {

	public static <T,E> T convert(E e, Class<T> t){
	
		return (T) t;
	}
}
