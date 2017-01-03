package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.User;

public interface UserRepository {
    User addUser(User user);
    User getUser(long id);
}
