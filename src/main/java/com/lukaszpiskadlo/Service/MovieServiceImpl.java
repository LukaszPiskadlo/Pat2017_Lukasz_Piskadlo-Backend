package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.MovieInvalidException;
import com.lukaszpiskadlo.Exception.MovieIsEmptyException;
import com.lukaszpiskadlo.Exception.MovieNotFoundException;
import com.lukaszpiskadlo.Model.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MovieServiceImpl implements MovieService {

    private AtomicLong id;
    private Map<Long, Movie> movies;

    MovieServiceImpl() {
        id = new AtomicLong();
        movies = new HashMap<>();
    }

    @Override
    public Movie create(Movie movie) {
        if (!isMovieValid(movie))
            throw new MovieInvalidException();

        long movieId = id.getAndIncrement();
        movie.setId(movieId);
        movies.put(movieId, movie);
        return movies.get(movieId);
    }

    @Override
    public List<Movie> findAll() {
        if (movies.isEmpty())
            throw new MovieIsEmptyException();

        return new ArrayList<>(movies.values());
    }

    @Override
    public Movie findById(long id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            throw new MovieNotFoundException(id);
        }
        return movie;
    }

    @Override
    public Movie update(long id, Movie movie) {
        if (!isMovieValid(movie))
            throw new MovieInvalidException();

        Movie oldMovie = movies.get(id);
        if (oldMovie == null) {
            throw new MovieNotFoundException(id);
        }

        movie.setId(oldMovie.getId());

        movies.replace(id, movie);
        return movies.get(id);
    }

    @Override
    public Movie delete(long id) {
        Movie movie = movies.get(id);
        if (movie == null) {
            throw new MovieNotFoundException(id);
        }
        return movies.remove(id);
    }

    private boolean isMovieValid(Movie movie) {
        return !(movie.getTitle() == null || movie.getTitle().isEmpty()
                || movie.getDirector() == null || movie.getDirector().isEmpty());
    }
}
