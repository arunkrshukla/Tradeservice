package com.db.tradestorage.exception;

public class InvalidTradeException extends RuntimeException {

    /**
	 * author : Arun Shukla
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String id;

    public InvalidTradeException(final String id) {
        super("Invalid Trade: " + id);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
