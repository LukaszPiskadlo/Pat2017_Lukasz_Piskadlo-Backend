package com.lukaszpiskadlo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.User;
import com.lukaszpiskadlo.Service.MovieService;
import com.lukaszpiskadlo.Service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    private final static String PATH = "/users";
    private final static String NAME = "Name";
    private final static String LAST_NAME = "LastName";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
        userService.deleteAllUsers();
        movieService.deleteAllMovies();
    }

    @Test
    public void createUser() throws Exception {
        User user = new User.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(PATH)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void findRentedMovies() throws Exception {
        User user = new User.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        user = userService.create(user);
        mockMvc.perform(get(PATH + "/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void rentMovies() throws Exception {
        User user = new User.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        user = userService.create(user);

        Movie movie = new Movie.Builder()
                .title("Movie")
                .director("Director")
                .amountAvailable(10)
                .group(Movie.Group.NEW)
                .build();
        movie = movieService.create(movie);

        mockMvc.perform(post(PATH + "/" + user.getId() + "/rent")
                    .contentType(contentType)
                    .content(objectMapper.writeValueAsString(new long[] {movie.getId()})))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void returnMovies() throws Exception {
        User user = new User.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        user = userService.create(user);

        Movie movie = new Movie.Builder()
                .title("Movie")
                .director("Director")
                .amountAvailable(10)
                .group(Movie.Group.NEW)
                .build();
        movie = movieService.create(movie);

        HashSet<Long> rentIds = new HashSet<>();
        rentIds.add(movie.getId());
        userService.rentMovie(user.getId(), rentIds);

        List<Long> ids = new ArrayList<>();
        ids.add(movie.getId());

        mockMvc.perform(post(PATH + "/" + user.getId() + "/return")
                    .contentType(contentType)
                    .content(objectMapper.writeValueAsString(ids.toArray())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

}
