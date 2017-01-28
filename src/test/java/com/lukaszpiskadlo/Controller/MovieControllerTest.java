package com.lukaszpiskadlo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Service.MovieService;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerTest {

    private final static String PATH = "/movies";
    private final static String TITLE = "Movie";
    private final static String DIRECTOR = "Director";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private MovieService service;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new MovieController(service)).build();
        service.deleteAllMovies();
    }

    @Test
    public void findAllMovies() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        service.create(movie);

        mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void findMovieById() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie created = service.create(movie);

        mockMvc.perform(get(PATH + "/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(created.getId().intValue())));
    }

    @Test
    public void createMovie() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        mockMvc.perform(post(PATH)
                    .contentType(contentType)
                    .content(objectMapper.writeValueAsString(movie)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void deleteMovie() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie created = service.create(movie);

        mockMvc.perform(delete(PATH + "/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(created.getId().intValue())));
    }

    @Test
    public void updateMovie() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .build();

        Movie created = service.create(movie);

        Movie updated = new Movie.Builder()
                .title(TITLE + "2")
                .director(DIRECTOR + "2")
                .build();

        mockMvc.perform(put(PATH + "/" + created.getId())
                    .contentType(contentType)
                    .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.title", is(updated.getTitle())));
    }

    @Test
    public void findMoviesByGroup() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .group(Movie.Group.NEW)
                .build();

        service.create(movie);

        mockMvc.perform(get(PATH + "?group=" + Movie.Group.NEW))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void findAvailableMovies() throws Exception {
        Movie movie = new Movie.Builder()
                .title(TITLE)
                .director(DIRECTOR)
                .amountAvailable(2)
                .build();

        service.create(movie);

        mockMvc.perform(get(PATH + "?available=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }
}
