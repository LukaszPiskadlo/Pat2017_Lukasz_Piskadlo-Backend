package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;

import java.util.List;

public interface MovieService {
    Movie create(Movie movie);
    List<Movie> findAll();
    Movie findById(long id);
    Movie update(long id, Movie movie);
    Movie delete(long id);
    Movie addActorToMovie(long id, Actor actor);
    void deleteAllMovies();
}
