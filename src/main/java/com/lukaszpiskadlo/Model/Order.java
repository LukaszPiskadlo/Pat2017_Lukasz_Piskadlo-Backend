package com.lukaszpiskadlo.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Order {

    private double price;
    private User user;
    private List<Movie> rentedMovies;

    public Order() {
    }

    private Order(Builder builder) {
        this.price = builder.price;
        this.user = builder.user;
        this.rentedMovies = builder.rentedMovies;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Movie> getRentedMovies() {
        return rentedMovies;
    }

    public void setRentedMovies(List<Movie> rentedMovies) {
        this.rentedMovies = rentedMovies;
    }

    public static class Builder {
        private double price;
        private User user;
        private List<Movie> rentedMovies;

        public Builder() {
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder rentedMovies(List<Movie> rentedMovies) {
            this.rentedMovies = rentedMovies;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
