package com.lukaszpiskadlo.Controller;

import com.lukaszpiskadlo.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final static String DISALLOWED_ID = "Disallowed ID property modification";
    private final static String ACTOR_NOT_FOUND = "Could not find actor";
    private final static String MOVIE_NOT_FOUND = "Could not find movie";
    private final static String ACTOR_ALREADY_EXISTS = "Actor already exists";
    private final static String MOVIE_ALREADY_EXISTS = "Movie already exists";

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

    @ExceptionHandler(ActorAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = ACTOR_ALREADY_EXISTS)
    public void handleActorAlreadyExists(ActorAlreadyExistsException e) {
    }

    @ExceptionHandler(MovieAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = MOVIE_ALREADY_EXISTS)
    public void handleMovieAlreadyExists(MovieAlreadyExistsException e) {
    }
}
