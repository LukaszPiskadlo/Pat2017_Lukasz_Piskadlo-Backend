package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.ActorAlreadyExistsException;
import com.lukaszpiskadlo.Exception.ActorNotFoundException;
import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ActorServiceImplTest {

    private final static long ID = 99;
    private final static String NAME = "Name";
    private final static String LAST_NAME = "LastName";

    private ActorServiceImpl actorService;

    @Mock
    private ActorRepository repository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        actorService = new ActorServiceImpl(repository);

        actorService.deleteAllActors();
    }

    @Test(expected = ActorNotFoundException.class)
    public void findById_ActorNotFound() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        actorService.findById(anyLong());
    }

    @Test(expected = ActorNotFoundException.class)
    public void delete_ActorNotFound() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        actorService.delete(anyLong());
    }

    @Test(expected = ActorNotFoundException.class)
    public void update_ActorNotFound() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        when(repository.exists(anyLong())).thenReturn(false);
        actorService.update(anyLong(), actor);
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
                .id(ID)
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        when(repository.exists(ID)).thenReturn(false);
        actorService.update(ID, actor);
    }

    @Test(expected = ActorAlreadyExistsException.class)
    public void create_ActorAlreadyExists() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        List<Actor> actors = new ArrayList<>();
        actors.add(actor);
        when(repository.findAll()).thenReturn(actors);

        actorService.create(actor);
    }

    @Test
    public void create() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        actorService.create(actor);
        verify(repository).save(actor);
    }

    @Test
    public void findById() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        actorService.findById(anyLong());
        verify(repository).findOne(anyLong());
    }

    @Test
    public void delete() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        actorService.delete(anyLong());
        verify(repository).delete(anyLong());
    }

    @Test
    public void findAll() throws Exception {
        actorService.findAll();
        verify(repository).findAll();
    }

    @Test
    public void update() throws Exception {
        Actor actor = new Actor.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        when(repository.exists(anyLong())).thenReturn(true);
        actorService.update(anyLong(), actor);
        verify(repository).save(any(Actor.class));
    }
}
