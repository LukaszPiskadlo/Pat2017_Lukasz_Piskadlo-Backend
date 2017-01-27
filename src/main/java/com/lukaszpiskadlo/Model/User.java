package com.lukaszpiskadlo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
public class User {

    @Id
    @GeneratedValue
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastName;
    @Email
    private String email;
    private String password;
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST,  CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_movies",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id")
    )
    private List<Movie> rentedMovies;

    public User() {
        rentedMovies = new ArrayList<>();
    }

    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.email = builder.email;
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

    public List<Movie> getRentedMovies() {
        return rentedMovies;
    }

    public void setRentedMovies(List<Movie> rentedMovies) {
        this.rentedMovies = rentedMovies;
    }

    public void addRentedMovie(Movie movie) {
        rentedMovies.add(movie);
    }

    public void addAllRentedMovies(List<Movie> movies) {
        rentedMovies.addAll(movies);
    }

    public void removeRentedMovie(Movie movie) {
        rentedMovies.remove(movie);
    }

    public void removeAllRentedMovies(List<Movie> movies) {
        rentedMovies.removeAll(movies);
    }

    public int getAmountOfRentedMovies() {
        return rentedMovies.size();
    }

    public static class Builder {
        private long id;
        private String name;
        private String lastName;
        private String email;
        private String password;
        private List<Movie> rentedMovies;

        public Builder() {
            rentedMovies = new ArrayList<>();
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

        public Builder rentedMovies(List<Movie> rentedMovies) {
            this.rentedMovies = rentedMovies;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
