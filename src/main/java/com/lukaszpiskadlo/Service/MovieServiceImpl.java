package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Exception.InvalidMovieGroupNameException;
import com.lukaszpiskadlo.Exception.MovieNotFoundException;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private MovieRepository repository;

    @Autowired
    MovieServiceImpl(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Movie create(Movie movie) {
        if (movie.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        return repository.addMovie(movie);
    }

    @Override
    public List<Movie> findAll() {
        return repository.getAllMovies();
    }

    @Override
    public Movie findById(long id) {
        Movie movie = repository.getMovie(id);
        if (movie == null) {
            throw new MovieNotFoundException();
        }
        return movie;
    }

    @Override
    public Movie update(long id, Movie movie) {
        if (movie.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        Movie oldMovie = repository.getMovie(id);
        if (oldMovie == null) {
            throw new MovieNotFoundException();
        }

        return repository.updateMovie(id, movie);
    }

    @Override
    public Movie delete(long id) {
        Movie movie = repository.getMovie(id);
        if (movie == null) {
            throw new MovieNotFoundException();
        }
        return repository.removeMovie(id);
    }

    @Override
    public Movie addActorToMovie(long id, Actor actor) {
        if (actor.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        Movie movie = repository.getMovie(id);
        if (movie == null) {
            throw new MovieNotFoundException();
        }

        return repository.addActorToMovie(id, actor);
    }

    @Override
    public void deleteAllMovies() {
        repository.removeAllMovies();
    }

    @Override
    public List<Movie> findByGroup(String groupName) {
        switch (groupName.toLowerCase()) {
            case "new":
                return repository.getMoviesByGroup(Movie.Group.NEW);
            case "hit":
                return repository.getMoviesByGroup(Movie.Group.HIT);
            case "other":
                return repository.getMoviesByGroup(Movie.Group.OTHER);
            default:
                throw new InvalidMovieGroupNameException();
        }
    }

    @Override
    public List<Movie> findAvailable() {
        return repository.getAvailableMovies();
    }
}
