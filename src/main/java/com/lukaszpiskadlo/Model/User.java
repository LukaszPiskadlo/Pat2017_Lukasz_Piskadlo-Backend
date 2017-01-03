package com.lukaszpiskadlo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {

    private long id;
    @NotEmpty
    private String name;
    private String password;
    @Valid
    private List<Movie> rentedMovies;

    public User() {
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.password = builder.password;
        this.rentedMovies = builder.rentedMovies;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Movie> getRentedMovies() {
        return rentedMovies;
    }

    public void setRentedMovies(List<Movie> rentedMovies) {
        this.rentedMovies = rentedMovies;
    }

    public static class Builder {
        private long id;
        private String name;
        private String password;
        private List<Movie> rentedMovies;

        public Builder() {
        }

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder rentedMovies(List<Movie> rentedMovies) {
            this.rentedMovies = rentedMovies;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
