package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);
    User getUser(long id);
    List<Movie> getUserRentedMovies(long id);
    Movie.Group getMovieGroup(Movie movie);
    Movie addRentedMovie(long userId, Movie movie);
    Movie removeRentedMovie(long userId, Movie movie);
}
