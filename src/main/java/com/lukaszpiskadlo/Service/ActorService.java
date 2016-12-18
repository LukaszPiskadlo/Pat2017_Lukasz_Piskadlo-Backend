package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Model.Actor;

import java.util.List;

public interface ActorService {
    Actor create(Actor actor);
    List<Actor> findAll();
    Actor findById(long id);
    Actor update(long id, Actor actor);
    Actor delete(long id);
}
