package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Exception.InvalidMovieGroupNameException;
import com.lukaszpiskadlo.Exception.MovieAlreadyExistsException;
import com.lukaszpiskadlo.Exception.MovieNotFoundException;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Repository.MainRepository;
import com.lukaszpiskadlo.Repository.MovieRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MovieServiceImplTest {

    private final static long ID = 99;
    private final static String TITLE = "Movie";
    private final static String DIRECTOR = "Director";

    private MovieServiceImpl movieService;

    @Before
    public void setUp() throws Exception {
        MovieRepository repository = new MainRepository();
        movieService = new MovieServiceImpl(repository);

        movieService.deleteAllMovies();
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

    @Test(expected = DisallowedIdModificationException.class)
    public void create_DisallowedIdModification() throws Exception {
        Movie movie = new Movie.Builder()
                .id(ID)
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        movieService.create(movie);
    }

    @Test(expected = DisallowedIdModificationException.class)
    public void update_DisallowedIdModification() throws Exception {
        Movie movie = new Movie.Builder()
                .id(ID)
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie created = movieService.create(movie);

        movie = new Movie.Builder()
                .id(ID)
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        movieService.update(created.getId(), movie);
    }

    @Test(expected = MovieAlreadyExistsException.class)
    public void create_MovieAlreadyExists() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        movieService.create(movie);
        movieService.create(movie);
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

    @Test(expected = InvalidMovieGroupNameException.class)
    public void findByGroup_InvalidMovieGroupName() throws Exception {
        movieService.findByGroup("InvalidGroupName");
    }

    @Test
    public void findByGroup() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .group(Movie.Group.NEW)
                .build();

        Movie created = movieService.create(movie);
        List<Movie> result = movieService.findByGroup("new");

        assertTrue(result.contains(created));
    }

    @Test
    public void findAvailable_Empty() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .amountAvailable(0)
                .build();

        movieService.create(movie);
        List<Movie> result = movieService.findAvailable();

        assertTrue(result.isEmpty());
    }

    @Test
    public void findAvailable() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .amountAvailable(4)
                .build();

        Movie created = movieService.create(movie);
        List<Movie> result = movieService.findAvailable();

        assertTrue(result.contains(created));
    }
}
