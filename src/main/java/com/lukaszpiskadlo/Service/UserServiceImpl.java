package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.*;
import com.lukaszpiskadlo.Model.Movie;
import com.lukaszpiskadlo.Model.Order;
import com.lukaszpiskadlo.Model.User;
import com.lukaszpiskadlo.Repository.MovieRepository;
import com.lukaszpiskadlo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final static int MAX_MOVIES_AMOUNT = 10;
    private final static double DISCOUNT_FOR_NEW_MOVIES = 0.25;
    private final static int NEW_MOVIES_AMOUNT_FOR_DISCOUNT = 2;
    private final static int AMOUNT_OF_MOVIES_FOR_BONUS = 3;
    private final static int AMOUNT_OF_BONUS_OTHER_MOVIES = 1;

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public User create(User user) {
        if (user.getId() != null) {
            throw new DisallowedIdModificationException();
        }

        userRepository.findAll().stream()
                .filter(u -> u.getName().equals(user.getName()))
                .findAny()
                .ifPresent(u -> { throw new UserAlreadyExistsException(); });

        return userRepository.save(user);
    }

    @Override
    public List<Movie> findRentedMovies(long id) {
        if (!userRepository.exists(id)) {
            throw new UserNotFoundException();
        }

        return userRepository.findOne(id).getRentedMovies();
    }

    @Override
    public Order rentMovie(long userId, Set<Long> movieIds) {
        List<Long> idList = new ArrayList<>(movieIds);

        if (!userRepository.exists(userId)) {
            throw new UserNotFoundException();
        }

        User user = userRepository.findOne(userId);
        List<Movie> movies = getMoviesFromIds(idList);
        checkMaxRentedMoviesAmount(user, movies.size());
        checkMoviesAvailability(movies);
        checkUserRentedMovies(user, movies);

        movies.forEach(movie -> movie.setAmountAvailable(movie.getAmountAvailable() - 1));

        BigDecimal price = calculatePrice(movies);

        user.addAllRentedMovies(movies);
        userRepository.save(user);

        return new Order.Builder()
                .user(user)
                .price(price)
                .rentedMovies(movies)
                .build();
    }

    @Override
    public List<Movie> returnMovie(long userId, Set<Long> movieIds) {
        List<Long> idList = new ArrayList<>(movieIds);

        if (!userRepository.exists(userId)) {
            throw new UserNotFoundException();
        }

        List<Movie> movies = getMoviesFromIds(idList);
        List<Movie> rentedMovies = userRepository.findOne(userId).getRentedMovies();

        if (!rentedMovies.containsAll(movies)) {
            throw new MovieNotRentedException();
        }

        movies.forEach(movie -> movie.setAmountAvailable(movie.getAmountAvailable() + 1));

        User user = userRepository.findOne(userId);
        user.removeAllRentedMovies(movies);
        userRepository.save(user);

        return movies;
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAllInBatch();
    }

    private List<Movie> getMoviesFromIds(List<Long> movieIds) {
        List<Movie> movies = new ArrayList<>();
        for (Long id : movieIds) {
            if (!movieRepository.exists(id)) {
                throw new MovieNotFoundException();
            }
            movies.add(movieRepository.findOne(id));
        }

        return movies;
    }

    private void checkMaxRentedMoviesAmount(User user, int movieAmount) {
        if (user.getAmountOfRentedMovies() + movieAmount > MAX_MOVIES_AMOUNT){
            throw new TooManyRentedMoviesException();
        }
    }

    private BigDecimal calculatePrice(List<Movie> movies) {
        BigDecimal price = new BigDecimal(0);
        int newMovieAmount = 0;
        int otherMovieAmount = 0;

        for (Movie movie : movies) {
            Movie.Group group = movie.getGroup();

            price = price.add(group.getPrice());

            if (group == Movie.Group.NEW) {
                newMovieAmount++;
            } else if (group == Movie.Group.OTHER) {
                otherMovieAmount++;
            }
        }

        if (movies.size() > AMOUNT_OF_MOVIES_FOR_BONUS
                && otherMovieAmount >= AMOUNT_OF_BONUS_OTHER_MOVIES) {
            price = price.subtract(Movie.Group.OTHER.getPrice());
        }

        if (newMovieAmount >= NEW_MOVIES_AMOUNT_FOR_DISCOUNT) {
            BigDecimal discount = new BigDecimal(1 - DISCOUNT_FOR_NEW_MOVIES);
            price = price.multiply(discount);
        }

        return price;
    }

    private void checkMoviesAvailability(List<Movie> movies) {
        movies.stream()
                .filter(m -> m.getAmountAvailable() <= 0)
                .findAny()
                .ifPresent(m -> { throw new MovieNotAvailableException(); });
    }

    private void checkUserRentedMovies(User user, List<Movie> movies) {
        List<Movie> rentedMovies = userRepository.findOne(user.getId()).getRentedMovies();
        if (!Collections.disjoint(movies, rentedMovies)) {
            throw new MovieAlreadyRentedException();
        }
    }
}
