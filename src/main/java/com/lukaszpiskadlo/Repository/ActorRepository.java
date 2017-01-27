package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    Actor findByNameAndLastName(String name, String lastName);
}
