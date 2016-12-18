package com.lukaszpiskadlo.Exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(long movieId) {
        super("Could not find movie with id: " + movieId);
    }
}
