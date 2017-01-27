package com.lukaszpiskadlo.Controller;

import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> findAllMovies() {
        return movieService.findAll();
    }

    @GetMapping("/{movieId}")
    public Movie findMovieById(@PathVariable long movieId) {
        return movieService.findById(movieId);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@Valid @RequestBody Movie movie) {
        return movieService.create(movie);
    }

    @DeleteMapping("/{movieId}")
    public Movie deleteMovie(@PathVariable long movieId) {
        return movieService.delete(movieId);
    }

    @PutMapping(value = "/{movieId}", consumes = "application/json")
    public Movie updateMovie(@PathVariable long movieId, @Valid @RequestBody Movie movie) {
        return movieService.update(movieId, movie);
    }

    @PostMapping(value = "/{movieId}/actors", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addActorToMovie(@PathVariable long movieId, @Valid @RequestBody Actor actor) {
        return movieService.addActorToMovie(movieId, actor);
    }

    @GetMapping("/group/{groupName}")
    public List<Movie> findMoviesByGroup(@PathVariable String groupName) {
        return movieService.findByGroup(groupName);
    }

    @GetMapping("/available")
    public List<Movie> findAvailableMovies() {
        return movieService.findAvailable();
    }
}
