package com.lukaszpiskadlo.Exception;

public class ActorNotFoundException extends RuntimeException {
    public ActorNotFoundException(long actorId) {
        super("Could not find actor with id: " + actorId);
    }
}
