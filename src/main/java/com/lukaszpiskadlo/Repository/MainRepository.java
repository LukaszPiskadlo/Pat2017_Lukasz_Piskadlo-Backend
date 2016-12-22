package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MainRepository implements ActorRepository, MovieRepository {

    private final static AtomicLong actorId = new AtomicLong();
    private final static AtomicLong movieId = new AtomicLong();
    private static Map<Long, Actor> actors;
    private static Map<Long, Movie> movies;

    public MainRepository() {
        actors = new HashMap<>();
        movies = new HashMap<>();
    }

    @Override
    public Actor getActor(long id) {
        return actors.get(id);
    }

    @Override
    public List<Actor> getAllActors() {
        return new ArrayList<>(actors.values());
    }

    @Override
    public Actor addActor(Actor actor) {
        long id = actorId.getAndIncrement();
        Actor newActor = new Actor.Builder()
                .id(id)
                .name(actor.getName())
                .lastName(actor.getLastName())
                .birthDate(actor.getBirthDate())
                .build();

        actors.put(id, newActor);
        return getActor(id);
    }

    @Override
    public Actor updateActor(long id, Actor actor) {
        Actor toUpdate = getActor(id);
        toUpdate.setName(actor.getName());
        toUpdate.setLastName(actor.getLastName());
        toUpdate.setBirthDate(actor.getBirthDate());

        return getActor(id);
    }

    @Override
    public Actor removeActor(long id) {
        return actors.remove(id);
    }

    @Override
    public Movie getMovie(long id) {
        return movies.get(id);
    }

    @Override
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies.values());
    }

    @Override
    public Movie addMovie(Movie movie) {
        long id = movieId.getAndIncrement();
        Movie newMovie = new Movie.Builder()
                .id(id)
                .title(movie.getTitle())
                .director(movie.getDirector())
                .cast(movie.getCast())
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .build();

        movies.put(id, newMovie);
        return getMovie(id);
    }

    @Override
    public Movie updateMovie(long id, Movie movie) {
        Movie toUpdate = movies.get(id);
        toUpdate.setTitle(movie.getTitle());
        toUpdate.setDirector(movie.getDirector());
        toUpdate.setCast(movie.getCast());
        toUpdate.setReleaseDate(movie.getReleaseDate());
        toUpdate.setDuration(movie.getDuration());

        return getMovie(id);
    }

    @Override
    public Movie removeMovie(long id) {
        return movies.remove(id);
    }
}
