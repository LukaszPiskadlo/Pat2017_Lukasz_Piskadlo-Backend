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
        if (user.getId() != 0) {
            throw new DisallowedIdModificationException();
        }

        return userRepository.addUser(user);
    }

    @Override
    public List<Movie> findRentedMovies(long id) {
        User user = userRepository.getUser(id);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return userRepository.getUserRentedMovies(id);
    }

    @Override
    public Order rentMovie(long userId, Set<Long> movieIds) {
        List<Long> idList = new ArrayList<>(movieIds);

        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }

        List<Movie> movies = getMoviesFromIds(idList);
        checkMaxRentedMoviesAmount(user, movies.size());
        checkMoviesAvailability(movies);

        movies.forEach(movie -> movie.setAmountAvailable(movie.getAmountAvailable() - 1));

        BigDecimal price = calculatePrice(movies);

        userRepository.addRentedMovies(userId, idList);

        return new Order.Builder()
                .user(user)
                .price(price)
                .rentedMovies(movies)
                .build();
    }

    @Override
    public List<Movie> returnMovie(long userId, Set<Long> movieIds) {
        List<Long> idList = new ArrayList<>(movieIds);

        User user = userRepository.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }

        List<Movie> movies = getMoviesFromIds(idList);
        List<Movie> rentedMovies = userRepository.getUserRentedMovies(userId);

        if (!rentedMovies.containsAll(movies)) {
            throw new MovieNotRentedException();
        }

        movies.forEach(movie -> movie.setAmountAvailable(movie.getAmountAvailable() + 1));

        userRepository.removeRentedMovie(userId, idList);

        return movies;
    }

    @Override
    public void deleteAllUsers() {
        userRepository.removeAllUsers();
    }

    private List<Movie> getMoviesFromIds(List<Long> movieIds) {
        List<Movie> movies = new ArrayList<>();
        for (Long id : movieIds) {
            Movie movie = movieRepository.getMovie(id);
            if (movie == null) {
                throw new MovieNotFoundException();
            }
            movies.add(movie);
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
}
