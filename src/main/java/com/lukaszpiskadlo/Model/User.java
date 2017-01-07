package com.lukaszpiskadlo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {

    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastName;
    private String email;
    private String password;
    @JsonIgnore
    private List<Long> rentedMoviesIds;

    public User() {
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.password = builder.password;
        this.rentedMoviesIds = builder.rentedMoviesIds;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getRentedMoviesIds() {
        return rentedMoviesIds;
    }

    public void setRentedMoviesIds(List<Long> rentedMoviesIds) {
        this.rentedMoviesIds = rentedMoviesIds;
    }

    public void addRentedMovieId(long id) {
        rentedMoviesIds.add(id);
    }

    public void addAllRentedMoviesIds(List<Long> ids) {
        rentedMoviesIds.addAll(ids);
    }

    public void removeRentedMovieId(long id) {
        rentedMoviesIds.remove(id);
    }

    public void removeAllRentedMoviesIds(List<Long> ids) {
        rentedMoviesIds.removeAll(ids);
    }

    public int getAmountOfRentedMovies() {
        return rentedMoviesIds.size();
    }

    public static class Builder {
        private long id;
        private String name;
        private String lastName;
        private String email;
        private String password;
        private List<Long> rentedMoviesIds;

        public Builder() {
            rentedMoviesIds = new ArrayList<>();
        }

        public Builder id(long id) {
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

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder rentedMoviesIds(List<Long> rentedMoviesIds) {
            this.rentedMoviesIds = rentedMoviesIds;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
