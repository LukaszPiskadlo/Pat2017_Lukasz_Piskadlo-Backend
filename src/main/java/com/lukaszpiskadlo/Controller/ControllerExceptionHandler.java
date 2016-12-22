package com.lukaszpiskadlo.Controller;

import com.lukaszpiskadlo.Exception.ActorNotFoundException;
import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Exception.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final static String DISALLOWED_ID = "Disallowed ID property modification";
    private final static String ACTOR_NOT_FOUND = "Could not find actor";
    private final static String MOVIE_NOT_FOUND = "Could not find movie";

    @ExceptionHandler(DisallowedIdModificationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = DISALLOWED_ID)
    public void handleDisallowedIdModification(DisallowedIdModificationException e) {
    }

    @ExceptionHandler(ActorNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = ACTOR_NOT_FOUND)
    public void handleActorNotFound(ActorNotFoundException e) {
    }

    @ExceptionHandler(MovieNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = MOVIE_NOT_FOUND)
    public void handleMovieNotFound(MovieNotFoundException e) {
    }
}
