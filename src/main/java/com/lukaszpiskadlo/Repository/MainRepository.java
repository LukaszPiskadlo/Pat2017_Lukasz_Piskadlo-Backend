package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Exception.ActorAlreadyExistsException;
import com.lukaszpiskadlo.Exception.MovieAlreadyExistsException;
import com.lukaszpiskadlo.Exception.UserAlreadyExistsException;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class MainRepository implements ActorRepository, MovieRepository, UserRepository {

    private final static AtomicLong actorId = new AtomicLong();
    private final static AtomicLong movieId = new AtomicLong();
    private final static AtomicLong userId = new AtomicLong();
    private final static Map<Long, Actor> actors = new HashMap<>();
    private final static Map<Long, Movie> movies = new HashMap<>();
    private final static Map<Long, User> users = new HashMap<>();

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

        long id = actorId.incrementAndGet();
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

        long id = movieId.incrementAndGet();
        Movie newMovie = new Movie.Builder()
                .id(id)
                .title(movie.getTitle())
                .director(movie.getDirector())
                .cast(cast)
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .amountAvailable(movie.getAmountAvailable())
                .group(movie.getGroup())
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
        toUpdate.setAmountAvailable(movie.getAmountAvailable());
        toUpdate.setGroup(movie.getGroup());

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

    @Override
    public List<Movie> getMoviesByGroup(Movie.Group group) {
        return getAllMovies().stream()
                .filter(m -> m.getGroup() == group)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getAvailableMovies() {
        return getAllMovies().stream()
                .filter(m -> m.getAmountAvailable() > 0)
                .collect(Collectors.toList());
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

    @Override
    public User addUser(User user) {
        users.values().stream()
                .filter(u -> u.getName().equals(user.getName()))
                .findAny()
                .ifPresent(u -> { throw new UserAlreadyExistsException(); });

        long id = userId.incrementAndGet();
        User newUser = new User.Builder()
                .id(id)
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        users.put(id, newUser);
        return getUser(id);
    }

    @Override
    public User getUser(long id) {
        return users.get(id);
    }

    @Override
    public List<Movie> getUserRentedMovies(long id) {
        return getUser(id).getRentedMoviesIds().stream()
                .map(this::getMovie)
                .collect(Collectors.toList());
    }

    @Override
    public Movie.Group getMovieGroup(Movie movie) {
        return getMovie(movie.getId()).getGroup();
    }

    @Override
    public Movie addRentedMovie(long userId, Movie movie) {
        return null;
    }

    @Override
    public Movie removeRentedMovie(long userId, Movie movie) {
        return null;
    }
}
