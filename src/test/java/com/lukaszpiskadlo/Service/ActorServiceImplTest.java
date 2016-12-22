package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.ActorAlreadyExistsException;
import com.lukaszpiskadlo.Exception.ActorNotFoundException;
import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Repository.ActorRepository;
import com.lukaszpiskadlo.Repository.MainRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class ActorServiceImplTest {

    private final static long ID = 99;
    private final static String NAME = "Name";
    private final static String LAST_NAME = "LastName";

    private ActorServiceImpl actorService;

    @Before
    public void setUp() throws Exception {
        ActorRepository repository = new MainRepository();
        actorService = new ActorServiceImpl(repository);

        actorService.deleteAllActors();
    }

    @Test(expected = ActorNotFoundException.class)
    public void findById_ActorNotFound() throws Exception {
        actorService.findById(ID);
    }

    @Test(expected = ActorNotFoundException.class)
    public void delete_ActorNotFound() throws Exception {
        actorService.delete(ID);
    }

    @Test(expected = ActorNotFoundException.class)
    public void update_ActorNotFound() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        actorService.update(ID, actor);
    }

    @Test(expected = DisallowedIdModificationException.class)
    public void create_DisallowedIdModification() throws Exception {
        Actor actor = new Actor.Builder()
                .id(ID)
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        actorService.create(actor);
    }

    @Test(expected = DisallowedIdModificationException.class)
    public void update_DisallowedIdModification() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor created = actorService.create(actor);

        actor = new Actor.Builder()
                .id(ID)
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        actorService.update(created.getId(), actor);
    }

    @Test(expected = ActorAlreadyExistsException.class)
    public void create_ActorAlreadyExists() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        actorService.create(actor);
        actorService.create(actor);
    }

    @Test
    public void create() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor result = actorService.create(actor);

        assertEquals(actor.getName(), result.getName());
        assertEquals(actor.getLastName(), result.getLastName());
    }

    @Test
    public void findById() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor created = actorService.create(actor);
        Actor result = actorService.findById(created.getId());

        assertEquals(created.getName(), result.getName());
        assertEquals(created.getLastName(), result.getLastName());
    }

    @Test
    public void delete() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor created = actorService.create(actor);
        Actor result = actorService.delete(created.getId());

        assertEquals(created.getName(), result.getName());
        assertEquals(created.getLastName(), result.getLastName());
    }

    @Test
    public void findAll() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor created = actorService.create(actor);
        List<Actor> result = actorService.findAll();

        assertTrue(result.contains(created));
    }

    @Test
    public void update() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        Actor created = actorService.create(actor);

        Actor newActor = new Actor.Builder()
                .name("New Name")
                .lastName("New Last Name")
                .build();

        Actor updated = actorService.update(created.getId(), newActor);

        assertEquals(newActor.getName(), updated.getName());
        assertEquals(newActor.getLastName(), updated.getLastName());
    }
}
