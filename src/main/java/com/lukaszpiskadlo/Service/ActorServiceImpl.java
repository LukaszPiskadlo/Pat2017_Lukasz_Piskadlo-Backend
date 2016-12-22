package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.ActorNotFoundException;
import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorServiceImpl implements ActorService {

    private ActorRepository repository;

    @Autowired
    ActorServiceImpl(ActorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Actor create(Actor actor) {
        if (actor.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        return repository.addActor(actor);
    }

    @Override
    public List<Actor> findAll() {
        return repository.getAllActors();
    }

    @Override
    public Actor findById(long id) {
        Actor actor = repository.getActor(id);
        if (actor == null) {
            throw new ActorNotFoundException();
        }
        return actor;
    }

    @Override
    public Actor update(long id, Actor actor) {
        if (actor.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        Actor oldActor = repository.getActor(id);
        if (oldActor == null) {
            throw new ActorNotFoundException();
        }

        return repository.updateActor(id, actor);
    }

    @Override
    public Actor delete(long id) {
        Actor actor = repository.getActor(id);
        if (actor == null) {
            throw new ActorNotFoundException();
        }
        return repository.removeActor(id);
    }
}
