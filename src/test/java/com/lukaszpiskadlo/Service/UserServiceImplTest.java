package com.lukaszpiskadlo.Service;

import com.lukaszpiskadlo.Exception.DisallowedIdModificationException;
import com.lukaszpiskadlo.Exception.UserNotFoundException;
import com.lukaszpiskadlo.Model.User;
import com.lukaszpiskadlo.Repository.MovieRepository;
import com.lukaszpiskadlo.Repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private final static String NAME = "User";
    private final static String LAST_NAME = "LastName";

    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MovieRepository movieRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userService = new UserServiceImpl(userRepository, movieRepository);
    }

    @Test(expected = DisallowedIdModificationException.class)
    public void create_DisallowedIdModification() throws Exception {
        long id = 99;
        User user = new User.Builder()
                .id(id)
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        when(userRepository.getUser(id)).thenReturn(null);

        userService.create(user);
    }

    @Test
    public void create() throws Exception {
        User user = new User.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();

        userService.create(user);
        verify(userRepository).addUser(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void findRentedMovies_UserNotFound() throws Exception {
        userService.findRentedMovies(anyLong());
    }

    @Test
    public void findRentedMovies() throws Exception {
        User user = new User.Builder()
                .name(NAME)
                .lastName(LAST_NAME)
                .build();
        when(userRepository.getUser(anyLong())).thenReturn(user);

        userService.findRentedMovies(anyLong());
        verify(userRepository).getUserRentedMovies(anyLong());
    }

    @Test(expected = UserNotFoundException.class)
    public void rentMovie_UserNotFound() throws Exception {
        userService.rentMovie(anyLong(), new HashSet<>());
    }

    @Test(expected = UserNotFoundException.class)
    public void returnMovie_UserNotFound() throws Exception {
        userService.returnMovie(anyLong(), new HashSet<>());
    }
}
