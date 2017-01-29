package com.lukaszpiskadlo.Controller;

import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Service.MovieService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

    @Value("${cache.max.age}")
    private int cacheTime;
    private final MovieService movieService;

    @Autowired
    MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int",
                    value = "Page number, starting from 0"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int",
                    value = "Number of movies per page")
    })
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<PagedResources<Resource<Movie>>> findAllMovies(
            Pageable pageable, PagedResourcesAssembler<Movie> assembler) {

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(cacheTime, TimeUnit.MINUTES))
                .body(assembler.toResource(movieService.findAll(pageable)));
    }

    @GetMapping("/{movieId}")
    public Movie findMovieById(@PathVariable long movieId) {
        return movieService.findById(movieId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Movie createMovie(@Valid @RequestBody Movie movie) {
        return movieService.create(movie);
    }

    @DeleteMapping("/{movieId}")
    public Movie deleteMovie(@PathVariable long movieId) {
        return movieService.delete(movieId);
    }

    @PutMapping(value = "/{movieId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Movie updateMovie(@PathVariable long movieId, @Valid @RequestBody Movie movie) {
        return movieService.update(movieId, movie);
    }

    @PostMapping(value = "/{movieId}/actors", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addActorToMovie(@PathVariable long movieId, @Valid @RequestBody Actor actor) {
        return movieService.addActorToMovie(movieId, actor);
    }

    @GetMapping(params = "group")
    public List<Movie> findMoviesByGroup(@RequestParam String group) {
        return movieService.findByGroup(group);
    }

    @GetMapping(params = "available")
    public List<Movie> findAvailableMovies(@RequestParam boolean available) {

        if (available) {
            return movieService.findAvailable();
        } else {
            return movieService.findUnavailable();
        }
    }
}
