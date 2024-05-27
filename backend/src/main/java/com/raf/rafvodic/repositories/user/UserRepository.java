package com.raf.rafvodic.repositories.user;


import com.raf.rafvodic.entities.User;

public interface UserRepository {
    public User addUser(User user);
    public User findUserByEmail(String email);
}
