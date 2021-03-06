package com.lukaszpiskadlo.Controller;

import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/actors", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActorController {

    private final ActorService actorService;

    @Autowired
    ActorController(ActorService actorService) {
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Actor createActor(@Valid @RequestBody Actor actor) {
        return actorService.create(actor);
    }

    @DeleteMapping("/{actorId}")
    public Actor deleteActor(@PathVariable long actorId) {
        return actorService.delete(actorId);
    }

    @PutMapping(value = "/{actorId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Actor updateActor(@PathVariable long actorId, @Valid @RequestBody Actor actor) {
        return actorService.update(actorId, actor);
    }
}
