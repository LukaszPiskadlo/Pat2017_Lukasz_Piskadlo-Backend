package com.lukaszpiskadlo.Controller;

import com.lukaszpiskadlo.Exception.ActorNotFoundException;
import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actors")
public class ActorController {

    private ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public List<Actor> findAllActors() {
        return actorService.findAll();
    }

    @GetMapping("/{actorId}")
    public Actor findActorById(@PathVariable long actorId) {
        return actorService.findById(actorId);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Actor createActor(@RequestBody Actor actor) {
        return actorService.create(actor);
    }

    @DeleteMapping("/{actorId}")
    public Actor deleteActor(@PathVariable long actorId) {
        return actorService.delete(actorId);
    }

    @PutMapping(value = "/{actorId}", consumes = "application/json")
    public Actor updateActor(@PathVariable long actorId, @RequestBody Actor actor) {
        return actorService.update(actorId, actor);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleActorNotFound(ActorNotFoundException e) {
        return e.getMessage();
    }
}
