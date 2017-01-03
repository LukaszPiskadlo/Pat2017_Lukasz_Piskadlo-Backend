package com.lukaszpiskadlo.Controller;

import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.User;
import com.lukaszpiskadlo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/{userId}")
    public List<Movie> findRentedMovies(@PathVariable long userId) {
        return userService.findRentedMovies(userId);
    }
}
