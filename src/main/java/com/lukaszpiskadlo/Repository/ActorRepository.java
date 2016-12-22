package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.Actor;

import java.util.List;

public interface ActorRepository {
    Actor getActor(long id);
    List<Actor> getAllActors();
    Actor addActor(Actor actor);
    Actor updateActor(long id, Actor actor);
    Actor removeActor(long id);
}
