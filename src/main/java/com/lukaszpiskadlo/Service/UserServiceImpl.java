package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Exception.UserNotFoundException;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.User;
import com.lukaszpiskadlo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        if (user.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        return repository.addUser(user);
    }

    @Override
    public List<Movie> findRentedMovies(long id) {
        User user = repository.getUser(id);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return repository.getUserRentedMovies(id);
    }

    @Override
    public double rentMovie(long userId, List<Movie> movies) {
        return 0;
    }

    @Override
    public List<Movie> returnMovie(long userId, List<Movie> movies) {
        return null;
    }
}
