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
    private final static String INVALID_MOVIE_GROUP_NAME = "Invalid movie group name";
    private final static String USER_ALREADY_EXISTS = "User already exists";
    private final static String USER_NOT_FOUND = "Could not find user";
    private final static String TOO_MANY_RENTED_MOVIES = "Amount of rented movies exceeds 10";
    private final static String MOVIE_NOT_AVAILABLE = "Movie not available";
    private final static String MOVIE_NOT_RENTED = "Movie was not rented by user";

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

    @ExceptionHandler(InvalidMovieGroupNameException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = INVALID_MOVIE_GROUP_NAME)
    public void handleInvalidMovieGroupName(InvalidMovieGroupNameException e) {
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = USER_ALREADY_EXISTS)
    public void handleUserAlreadyExists(UserAlreadyExistsException e) {
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = USER_NOT_FOUND)
    public void handleUserNotFound(UserNotFoundException e) {
    }

    @ExceptionHandler(TooManyRentedMoviesException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = TOO_MANY_RENTED_MOVIES)
    public void handleTooManyRentedMovies(TooManyRentedMoviesException e) {
    }

    @ExceptionHandler(MovieNotAvailableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = MOVIE_NOT_AVAILABLE)
    public void handleMovieNotAvailable(MovieNotAvailableException e) {
    }

    @ExceptionHandler(MovieNotRentedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = MOVIE_NOT_RENTED)
    public void handleMovieNotRented(MovieNotRentedException e) {
    }
}
