package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    Movie create(Movie movie);
    List<Movie> findAll();
    Page<Movie> findAll(Pageable pageable);
    Movie findById(long id);
    Movie update(long id, Movie movie);
    Movie delete(long id);
    Movie addActorToMovie(long id, Actor actor);
    void deleteAllMovies();
    List<Movie> findByGroup(String groupName);
    List<Movie> findAvailable();
    List<Movie> findUnavailable();
}
