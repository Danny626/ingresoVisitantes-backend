package com.albo.exception.list;

public class AlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyExistException(String mensaje) {
		super(mensaje);
	}

}
