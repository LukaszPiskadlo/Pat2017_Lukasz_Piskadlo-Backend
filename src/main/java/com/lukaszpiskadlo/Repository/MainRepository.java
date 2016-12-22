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
        getAllActors().stream()
                .filter(a -> a.getName().equals(actor.getName()))
                .filter(a -> a.getLastName().equals(actor.getLastName()))
                .findAny()
                .ifPresent(a -> { throw new ActorAlreadyExistsException(); });

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
    public void removeAllActors() {
        actors.clear();
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
        getAllMovies().stream()
                .filter(m -> m.getTitle().equals(movie.getTitle()))
                .filter(m -> m.getDirector().equals(movie.getDirector()))
                .findAny()
                .ifPresent(m -> { throw new MovieAlreadyExistsException(); });

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

    @Override
    public void removeAllMovies() {
        movies.clear();
    }

    private List<Actor> addActorsFromMovie(List<Actor> actors) {
        if (actors == null)
            return null;

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
        return getAllActors().stream()
                .filter(a -> a.getName().equals(name))
                .filter(a -> a.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    private void updateActorFromMovie(Actor actor) {
        getAllMovies().stream()
                .flatMap(m -> m.getCast().stream())
                .filter(a -> a.getId() == actor.getId())
                .forEach(a -> {
                    a.setName(actor.getName());
                    a.setLastName(actor.getLastName());
                    a.setBirthDate(actor.getBirthDate());
                });
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
