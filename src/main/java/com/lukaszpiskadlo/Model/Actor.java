package com.lukaszpiskadlo.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
public class Actor {

    @Id
    @GeneratedValue
    @ApiModelProperty(readOnly = true)
    private Long id;
    @NotEmpty
    @ApiModelProperty(required = true)
    private String name;
    @NotEmpty
    @ApiModelProperty(required = true)
    private String lastName;
    private String birthDate;
    @ManyToMany(mappedBy = "cast", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,  CascadeType.MERGE})
    private List<Movie> movies;

    public Actor() {
    }

    private Actor(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.birthDate = builder.birthDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    @PreRemove
    private void removeFromMovies() {
        movies.forEach(movie -> {
            List<Actor> cast = movie.getCast();
            cast.remove(this);
        });
    }

    public static class Builder {

        private Long id;
        private String name;
        private String lastName;
        private String birthDate;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder birthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Actor build() {
            return new Actor(this);
        }
    }
}
