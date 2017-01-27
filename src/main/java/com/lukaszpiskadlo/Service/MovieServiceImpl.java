package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Exception.InvalidMovieGroupNameException;
import com.lukaszpiskadlo.Exception.MovieAlreadyExistsException;
import com.lukaszpiskadlo.Exception.MovieNotFoundException;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Repository.ActorRepository;
import com.lukaszpiskadlo.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository repository;
    private final ActorRepository actorRepository;

    @Autowired
    MovieServiceImpl(MovieRepository repository, ActorRepository actorRepository) {
        this.repository = repository;
        this.actorRepository = actorRepository;
    }

    @Override
    public Movie create(Movie movie) {
        if (movie.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        repository.findAll().stream()
                .filter(m -> m.getTitle().equals(movie.getTitle()))
                .filter(m -> m.getDirector().equals(movie.getDirector()))
                .findAny()
                .ifPresent(m -> { throw new MovieAlreadyExistsException(); });

        Movie newMovie = new Movie.Builder()
                .title(movie.getTitle())
                .director(movie.getDirector())
                .cast(getActors(movie.getCast()))
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .amountAvailable(movie.getAmountAvailable())
                .group(movie.getGroup())
                .build();

        return repository.save(newMovie);
    }

    @Override
    public List<Movie> findAll() {
        return repository.findAll();
    }

    @Override
    public Movie findById(long id) {
        if (!repository.exists(id)) {
            throw new MovieNotFoundException();
        }
        return repository.findOne(id);
    }

    @Override
    public Movie update(long id, Movie movie) {
        if (movie.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        if (!repository.exists(id)) {
            throw new MovieNotFoundException();
        }

        Movie updatedMovie = new Movie.Builder()
                .id(id)
                .title(movie.getTitle())
                .director(movie.getDirector())
                .cast(getActors(movie.getCast()))
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .amountAvailable(movie.getAmountAvailable())
                .group(movie.getGroup())
                .build();

        return repository.save(updatedMovie);
    }

    @Override
    public Movie delete(long id) {
        if (!repository.exists(id)) {
            throw new MovieNotFoundException();
        }

        Movie removedMovie = repository.findOne(id);
        repository.delete(id);

        return removedMovie;
    }

    @Override
    public Movie addActorToMovie(long id, Actor actor) {
        if (actor.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        if (!repository.exists(id)) {
            throw new MovieNotFoundException();
        }

        Movie movie = repository.findOne(id);
        Actor existingActor = actorRepository.findByNameAndLastName(actor.getName(), actor.getLastName());

        if (existingActor == null) {
            movie.getCast().add(actor);
        } else {
            movie.getCast().add(existingActor);
        }

        return repository.save(movie);
    }

    @Override
    public void deleteAllMovies() {
        repository.deleteAllInBatch();
    }

    @Override
    public List<Movie> findByGroup(String groupName) {
        switch (groupName.toLowerCase()) {
            case "new":
                return repository.findByGroup(Movie.Group.NEW);
            case "hit":
                return repository.findByGroup(Movie.Group.HIT);
            case "other":
                return repository.findByGroup(Movie.Group.OTHER);
            default:
                throw new InvalidMovieGroupNameException();
        }
    }

    @Override
    public List<Movie> findAvailable() {
        return repository.findAll().stream()
                .filter(movie -> movie.getAmountAvailable() > 0)
                .collect(Collectors.toList());
    }

    private List<Actor> getActors(List<Actor> cast) {
        List<Actor> actors = new ArrayList<>();

        for (Actor actor : cast) {
            Actor existingActor = actorRepository.findByNameAndLastName(actor.getName(), actor.getLastName());

            if (existingActor == null) {
                actors.add(actor);
            } else {
                actors.add(existingActor);
            }
        }

        return actors;
    }
}
