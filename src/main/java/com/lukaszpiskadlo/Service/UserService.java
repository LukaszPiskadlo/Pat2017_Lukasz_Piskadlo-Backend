package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.Order;
import com.lukaszpiskadlo.Model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User create(User user);
    List<Movie> findRentedMovies(long id);
    Order rentMovie(long userId, Set<Long> movieIds);
    List<Movie> returnMovie(long userId, Set<Long> movieIds);
    void deleteAllUsers();
}
