package com.lukaszpiskadlo.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Movie {

    private long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String director;
    private List<Actor> cast;
    private String releaseDate;
    private int duration;

    public Movie() {
    }

    private Movie(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.director = builder.director;
        this.cast = builder.cast;
        this.releaseDate = builder.releaseDate;
        this.duration = builder.duration;
    }

    public long getId() {
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

    public static class Builder {

        private long id;
        private String title;
        private String director;
        private List<Actor> cast;
        private String releaseDate;
        private int duration;

        public Builder() {
        }

        public Builder id(long id) {
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

        public Movie build() {
            return new Movie(this);
        }
    }
}
