package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Exception.ActorAlreadyExistsException;
import com.lukaszpiskadlo.Exception.MovieAlreadyExistsException;
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
    private final static Map<Long, Actor> actors = new HashMap<>();
    private final static Map<Long, Movie> movies = new HashMap<>();

    public MainRepository() {
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
        for (Actor existing : getAllActors()) {
            if (actor.getName().equals(existing.getName())
                    && actor.getLastName().equals(existing.getLastName())) {
                throw new ActorAlreadyExistsException();
            }
        }

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

        updateActorFromMovie(toUpdate);

        return getActor(id);
    }

    @Override
    public Actor removeActor(long id) {
        Actor actor = getActor(id);
        if (actor != null) {
            removeActorFromMovie(actor);
        }
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
        for (Movie existing : getAllMovies()) {
            if (movie.getTitle().equals(existing.getTitle())
                    && movie.getDirector().equals(existing.getDirector())) {
                throw new MovieAlreadyExistsException();
            }
        }

        List<Actor> cast = addActorsFromMovie(movie.getCast());

        long id = movieId.getAndIncrement();
        Movie newMovie = new Movie.Builder()
                .id(id)
                .title(movie.getTitle())
                .director(movie.getDirector())
                .cast(cast)
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .build();

        movies.put(id, newMovie);
        return getMovie(id);
    }

    @Override
    public Movie updateMovie(long id, Movie movie) {
        List<Actor> cast = addActorsFromMovie(movie.getCast());

        Movie toUpdate = movies.get(id);
        toUpdate.setTitle(movie.getTitle());
        toUpdate.setDirector(movie.getDirector());
        toUpdate.setCast(cast);
        toUpdate.setReleaseDate(movie.getReleaseDate());
        toUpdate.setDuration(movie.getDuration());

        return getMovie(id);
    }

    @Override
    public Movie removeMovie(long id) {
        return movies.remove(id);
    }

    @Override
    public Movie addActorToMovie(long id, Actor actor) {
        Movie movie = getMovie(id);
        Actor existingActor = findActorByFullName(actor.getName(), actor.getLastName());
        if (existingActor == null) {
            movie.getCast().add(addActor(actor));
        } else {
            movie.getCast().add(existingActor);
        }
        return movie;
    }

    private List<Actor> addActorsFromMovie(List<Actor> actors) {
        List<Actor> cast = new ArrayList<>();
        for (Actor actor : actors) {
            Actor existingActor = findActorByFullName(actor.getName(), actor.getLastName());
            if (existingActor == null) {
                cast.add(addActor(actor));
            } else {
                cast.add(existingActor);
            }
        }
        return cast;
    }

    private Actor findActorByFullName(String name, String lastName) {
        for (Actor actor : getAllActors()) {
            if (actor.getName().equals(name)
                    && actor.getLastName().equals(lastName)) {
                return actor;
            }
        }
        return null;
    }

    private void updateActorFromMovie(Actor actor) {
        for (Movie movie : getAllMovies()) {
            for (Actor current : movie.getCast()) {
                if (current.getId() == actor.getId()) {
                    current.setName(actor.getName());
                    current.setLastName(actor.getLastName());
                    current.setBirthDate(actor.getBirthDate());
                }
            }
        }
    }

    private void removeActorFromMovie(Actor actor) {
        for (Movie movie : getAllMovies()) {
            for (Actor current : movie.getCast()) {
                if (current.getId() == actor.getId()) {
                    movie.getCast().remove(current);
                    break;
                }
            }
        }
    }
}
