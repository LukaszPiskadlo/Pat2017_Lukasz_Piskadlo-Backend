package com.lukaszpiskadlo.Exception;

public class ActorInvalidException extends RuntimeException {
    public ActorInvalidException() {
        super("Actor requires name nad lastName");
    }
}
