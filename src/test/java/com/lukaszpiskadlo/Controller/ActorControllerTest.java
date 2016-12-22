package com.lukaszpiskadlo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Service.ActorService;
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
public class ActorControllerTest {

    private final static String PATH = "/actors";
    private final static String NAME = "Name";
    private final static String LAST_NAME = "LastName";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private ActorService service;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new ActorController(service)).build();

        service.deleteAllActors();
    }

    @Test
    public void findAllActors() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        service.create(actor);

        mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void findActorById() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor created = service.create(actor);

        mockMvc.perform(get(PATH + "/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int)created.getId())));
    }

    @Test
    public void createActor() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        mockMvc.perform(post(PATH)
                    .contentType(contentType)
                    .content(objectMapper.writeValueAsString(actor)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void deleteActor() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor created = service.create(actor);

        mockMvc.perform(delete(PATH + "/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is((int)created.getId())));
    }

    @Test
    public void updateActor() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor created = service.create(actor);

        Actor updated = new Actor.Builder()
                .name(NAME + "2")
                .lastName(LAST_NAME + "2")
                .build();

        mockMvc.perform(put(PATH + "/" + created.getId())
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is(updated.getName())));
    }
}
