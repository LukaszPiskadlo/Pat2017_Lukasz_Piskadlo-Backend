package com.lukaszpiskadlo;

import com.lukaszpiskadlo.Model.Actor;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieRepositoryInitializer implements ApplicationRunner {

    private MovieRepository movieRepository;

    @Autowired
    public MovieRepositoryInitializer(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        for (Movie.Group group : Movie.Group.values()) {
            for (int i = 1; i <= 10; i++) {
                Actor actor = new Actor.Builder()
                        .name("Actor " + group + " " + i)
                        .lastName("Actor")
                        .build();

                List<Actor> actors = new ArrayList<>();
                actors.add(actor);

                Movie movie = new Movie.Builder()
                        .title("Movie " + group + " " + i)
                        .director("Director")
                        .cast(actors)
                        .duration(120 - i)
                        .amountAvailable(i)
                        .group(group)
                        .build();

                movieRepository.save(movie);
            }
        }
    }
}
