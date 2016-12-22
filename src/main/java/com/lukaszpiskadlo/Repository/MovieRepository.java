package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;

import java.util.List;

public interface MovieRepository {
    Movie getMovie(long id);
    List<Movie> getAllMovies();
    Movie addMovie(Movie movie);
    Movie updateMovie(long id, Movie movie);
    Movie removeMovie(long id);
    Movie addActorToMovie(long id, Actor actor);
    void removeAllMovies();
}
