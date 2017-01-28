package com.lukaszpiskadlo.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
public class Movie {

    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String director;
    @Valid
    @ManyToMany(cascade = {CascadeType.PERSIST,  CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "movie_cast",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id")
    )
    private List<Actor> cast;
    private String releaseDate;
    private int duration;

    private int amountAvailable;
    @Enumerated(EnumType.STRING)
    @Column(name = "price_group")
    private Group group;

    @ManyToMany(mappedBy = "rentedMovies",
            cascade = {CascadeType.PERSIST,  CascadeType.MERGE})
    private List<User> users;

    public Movie() {
    }

    private Movie(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.director = builder.director;
        this.cast = builder.cast;
        this.releaseDate = builder.releaseDate;
        this.duration = builder.duration;
        this.amountAvailable = builder.amountAvailable;
        this.group = builder.group;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public List<Actor> getCast() {
        return cast;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @PreRemove
    private void removeFromUser() {
        users.forEach(user -> {
            List<Movie> movies = user.getRentedMovies();
            movies.remove(this);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (id != null ? !id.equals(movie.id) : movie.id != null) return false;
        if (!title.equals(movie.title)) return false;
        return director.equals(movie.director);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + title.hashCode();
        result = 31 * result + director.hashCode();
        return result;
    }

    public static class Builder {

        private Long id;
        private String title;
        private String director;
        private List<Actor> cast;
        private String releaseDate;
        private int duration;
        private int amountAvailable;
        private Group group;

        public Builder() {
            cast = new ArrayList<>();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder director(String director) {
            this.director = director;
            return this;
        }

        public Builder cast(List<Actor> cast) {
            this.cast = cast;
            return this;
        }

        public Builder releaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder amountAvailable(int amountAvailable) {
            this.amountAvailable = amountAvailable;
            return this;
        }

        public Builder group(Group group) {
            this.group = group;
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }

    public enum Group {
        NEW("12.50"),
        HIT("10"),
        OTHER("7.30");

        private BigDecimal price;

        Group(String price) {
            this.price = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
}
