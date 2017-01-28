package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.ActorAlreadyExistsException;
import com.lukaszpiskadlo.Exception.ActorNotFoundException;
import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository repository;

    @Autowired
    ActorServiceImpl(ActorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Actor create(Actor actor) {
        if (actor.getId() != null) {
            throw new DisallowedIdModificationException();
        }

        repository.findAll().stream()
                .filter(a -> a.getName().equals(actor.getName()))
                .filter(a -> a.getLastName().equals(actor.getLastName()))
                .findAny()
                .ifPresent(a -> { throw new ActorAlreadyExistsException(); });

        return repository.save(actor);
    }

    @Override
    public List<Actor> findAll() {
        return repository.findAll();
    }

    @Override
    public Actor findById(long id) {
        if (!repository.exists(id)) {
            throw new ActorNotFoundException();
        }
        return repository.findOne(id);
    }

    @Override
    public Actor update(long id, Actor actor) {
        if (actor.getId() != null) {
            throw new DisallowedIdModificationException();
        }

        if (!repository.exists(id)) {
            throw new ActorNotFoundException();
        }

        Actor updatedActor = new Actor.Builder()
                .id(id)
                .name(actor.getName())
                .lastName(actor.getLastName())
                .birthDate(actor.getBirthDate())
                .build();

        return repository.save(updatedActor);
    }

    @Override
    public Actor delete(long id) {
        if (!repository.exists(id)) {
            throw new ActorNotFoundException();
        }

        Actor removedActor = repository.findOne(id);
        repository.delete(id);

        return removedActor;
    }

    @Override
    public void deleteAllActors() {
        repository.deleteAllInBatch();
    }
}
