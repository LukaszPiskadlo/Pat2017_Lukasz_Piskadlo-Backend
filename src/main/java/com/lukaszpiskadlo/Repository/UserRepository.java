package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);
    User getUser(long id);
    List<Movie> getUserRentedMovies(long userId);
    void addRentedMovies(long userId, List<Long> moviesIds);
    void removeRentedMovie(long userId, List<Long> moviesIds);
}
