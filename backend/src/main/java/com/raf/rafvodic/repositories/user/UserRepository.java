package com.raf.rafvodic.repositories.user;


import com.raf.rafvodic.entities.User;

import java.util.List;

public interface UserRepository {
    public User addUser(User user);
    public List<User> getAllUsers(int offset, int limit);
    public User findUserByEmail(String email);
    public User findUserById(int id);
    public User updateUser(User user);
    public void deleteUser(int id);

}
