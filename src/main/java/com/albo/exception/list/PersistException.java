package com.albo.exception.list;

public class PersistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PersistException(String mensaje) {
		super(mensaje);
	}
	
}
