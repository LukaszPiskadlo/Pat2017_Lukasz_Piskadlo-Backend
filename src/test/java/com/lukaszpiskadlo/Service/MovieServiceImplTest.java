package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.MovieInvalidException;
import com.lukaszpiskadlo.Exception.MovieIsEmptyException;
import com.lukaszpiskadlo.Exception.MovieNotFoundException;
import com.lukaszpiskadlo.Model.Movie;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovieServiceImplTest {

    private final static long ID = 12;
    private final static String TITLE = "Movie";
    private final static String DIRECTOR = "Director";

    private MovieServiceImpl movieService;

    @Before
    public void setUp() throws Exception {
        movieService = new MovieServiceImpl();
    }

    @Test(expected = MovieIsEmptyException.class)
    public void findAll_MovieIsEmpty() throws Exception {
        movieService.findAll();
    }

    @Test(expected = MovieNotFoundException.class)
    public void findById_MovieNotFound() throws Exception {
        movieService.findById(ID);
    }

    @Test(expected = MovieNotFoundException.class)
    public void delete_MovieNotFound() throws Exception {
        movieService.delete(ID);
    }

    @Test(expected = MovieNotFoundException.class)
    public void update_MovieNotFound() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        movieService.update(ID, movie);
    }

    @Test(expected = MovieInvalidException.class)
    public void create_MovieInvalid() throws Exception {
        Movie movie = new Movie.Builder()
                .director(DIRECTOR)
                .build();

        movieService.create(movie);
    }

    @Test(expected = MovieInvalidException.class)
    public void update_MovieInvalid() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        movieService.create(movie);

        Movie updated = new Movie.Builder()
                .title(TITLE)
                .build();

        movieService.update(movie.getId(), updated);
    }

    @Test
    public void create() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie result = movieService.create(movie);

        assertEquals(movie.getTitle(), result.getTitle());
        assertEquals(movie.getDirector(), result.getDirector());
    }

    @Test
    public void findAll() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie created = movieService.create(movie);
        List<Movie> result = movieService.findAll();

        assertTrue(result.contains(created));
    }

    @Test
    public void findById() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie created = movieService.create(movie);
        Movie result = movieService.findById(created.getId());

        assertEquals(created.getTitle(), result.getTitle());
        assertEquals(created.getDirector(), result.getDirector());
    }

    @Test
    public void update() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie created = movieService.create(movie);

        Movie newMovie = new Movie.Builder()
                .title("New Title")
                .director("New Director")
                .build();

        Movie updated = movieService.update(created.getId(), newMovie);

        assertEquals(newMovie.getTitle(), updated.getTitle());
        assertEquals(newMovie.getDirector(), updated.getDirector());
    }

    @Test
    public void delete() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie created = movieService.create(movie);
        Movie result = movieService.delete(created.getId());

        assertEquals(created.getTitle(), result.getTitle());
        assertEquals(created.getDirector(), result.getDirector());
    }
}
