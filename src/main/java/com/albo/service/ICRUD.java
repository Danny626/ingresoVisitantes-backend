package com.albo.service;

import java.util.List;

public interface ICRUD<T> {

	T registrar(T t);

	T modificar(T t);

	void eliminar(String id);

	T listarId(String id);

	List<T> listar();
}
