package com.albo.exception.list;

public class ModelNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ModelNotFoundException(String mensaje) {
		super(mensaje);
	}

}