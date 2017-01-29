package com.lukaszpiskadlo.Controller;

import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.Order;
import com.lukaszpiskadlo.Model.User;
import com.lukaszpiskadlo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/{userId}")
    public List<Movie> findRentedMovies(@PathVariable long userId) {
        return userService.findRentedMovies(userId);
    }

    @PostMapping(value = "/{userId}/rent", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Order rentMovies(@PathVariable long userId, @RequestBody Set<Long> movieIds) {
        return userService.rentMovie(userId, movieIds);
    }

    @PostMapping(value = "{userId}/return", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> returnMovies(@PathVariable long userId, @RequestBody Set<Long> movieIds) {
        return userService.returnMovie(userId, movieIds);
    }
}
