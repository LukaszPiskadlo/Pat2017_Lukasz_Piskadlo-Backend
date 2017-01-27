package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGroup(Movie.Group group);
}
