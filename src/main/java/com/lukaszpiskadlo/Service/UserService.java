package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.User;

import java.util.List;

public interface UserService {
    User create(User user);
    List<Movie> findRentedMovies(long id);
}
