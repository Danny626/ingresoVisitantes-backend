package com.albo.exception.list;

import java.util.Set;

public class ModelUnSupportedFieldPatchException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public ModelUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}
