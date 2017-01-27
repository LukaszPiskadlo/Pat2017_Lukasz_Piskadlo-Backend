package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Exception.InvalidMovieGroupNameException;
import com.lukaszpiskadlo.Exception.MovieAlreadyExistsException;
import com.lukaszpiskadlo.Exception.MovieNotFoundException;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Repository.ActorRepository;
import com.lukaszpiskadlo.Repository.MovieRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovieServiceImplTest {

    private final static long ID = 99;
    private final static String TITLE = "Movie";
    private final static String DIRECTOR = "Director";

    private MovieServiceImpl movieService;

    @Mock
    private MovieRepository repository;

    @Mock
    private ActorRepository actorRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        movieService = new MovieServiceImpl(repository, actorRepository);

        movieService.deleteAllMovies();
    }

    @Test(expected = MovieNotFoundException.class)
    public void findById_MovieNotFound() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        movieService.findById(anyLong());
    }

    @Test(expected = MovieNotFoundException.class)
    public void delete_MovieNotFound() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        movieService.delete(anyLong());
    }

    @Test(expected = MovieNotFoundException.class)
    public void update_MovieNotFound() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        when(repository.exists(anyLong())).thenReturn(false);
        movieService.update(anyLong(), movie);
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

        when(repository.exists(ID)).thenReturn(false);
        movieService.update(ID, movie);
    }

    @Test(expected = MovieAlreadyExistsException.class)
    public void create_MovieAlreadyExists() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        when(repository.findAll()).thenReturn(movies);

        movieService.create(movie);
    }

    @Test
    public void create() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        movieService.create(movie);
        verify(repository).save(movie);
    }

    @Test
    public void findAll() throws Exception {
        movieService.findAll();
        verify(repository).findAll();
    }

    @Test
    public void findById() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        movieService.findById(anyLong());
        verify(repository).findOne(anyLong());
    }

    @Test
    public void update() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        when(repository.exists(anyLong())).thenReturn(true);
        movieService.update(anyLong(), movie);
        verify(repository).save(any(Movie.class));
    }

    @Test
    public void delete() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        movieService.delete(anyLong());
        verify(repository).delete(anyLong());
    }

    @Test(expected = InvalidMovieGroupNameException.class)
    public void findByGroup_InvalidMovieGroupName() throws Exception {
        movieService.findByGroup("InvalidGroupName");
    }

    @Test
    public void findByGroup_New() throws Exception {
        movieService.findByGroup("new");
        verify(repository).findByGroup(Movie.Group.NEW);
    }

    @Test
    public void findByGroup_Hit() throws Exception {
        movieService.findByGroup("hit");
        verify(repository).findByGroup(Movie.Group.HIT);
    }

    @Test
    public void findByGroup_Other() throws Exception {
        movieService.findByGroup("other");
        verify(repository).findByGroup(Movie.Group.OTHER);
    }

    @Test
    public void findAvailable_Empty() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .amountAvailable(0)
                .build();

        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        when(repository.findAll()).thenReturn(movies);

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

        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        when(repository.findAll()).thenReturn(movies);

        List<Movie> result = movieService.findAvailable();
        assertTrue(!result.isEmpty());
    }
}
