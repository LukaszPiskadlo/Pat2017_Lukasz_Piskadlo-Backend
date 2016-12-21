package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.ActorInvalidException;
import com.lukaszpiskadlo.Exception.ActorIsEmptyException;
import com.lukaszpiskadlo.Exception.ActorNotFoundException;
import com.lukaszpiskadlo.Model.Actor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ActorServiceImpl implements ActorService {

    private final AtomicLong id;
    private Map<Long, Actor> actors;

    ActorServiceImpl() {
        id = new AtomicLong();
        actors = new HashMap<>();
    }

    @Override
    public Actor create(Actor actor) {
        if (!isActorValid(actor))
            throw new ActorInvalidException();

        long actorId = id.getAndIncrement();
        actor.setId(actorId);
        actors.put(actorId, actor);
        return actors.get(actorId);
    }

    @Override
    public List<Actor> findAll() {
        if (actors.isEmpty())
            throw new ActorIsEmptyException();

        return new ArrayList<>(actors.values());
    }

    @Override
    public Actor findById(long id) {
        Actor actor = actors.get(id);
        if (actor == null) {
            throw new ActorNotFoundException(id);
        }
        return actor;
    }

    @Override
    public Actor update(long id, Actor actor) {
        if (!isActorValid(actor))
            throw new ActorInvalidException();

        Actor oldActor = actors.get(id);
        if (oldActor == null) {
            throw new ActorNotFoundException(id);
        }
        actors.replace(id, actor);
        return actors.get(id);
    }

    @Override
    public Actor delete(long id) {
        Actor actor = actors.get(id);
        if (actor == null) {
            throw new ActorNotFoundException(id);
        }
        return actors.remove(id);
    }

    private boolean isActorValid(Actor actor) {
        return !(actor.getName() == null || actor.getName().isEmpty()
                || actor.getLastName() == null || actor.getLastName().isEmpty());
    }
}
