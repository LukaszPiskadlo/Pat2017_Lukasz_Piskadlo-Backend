package com.lukaszpiskadlo.Exception;

public class MovieInvalidException extends RuntimeException {
    public MovieInvalidException() {
        super("Movie requires title and director");
    }
}
