package com.careerhub.dao;

import java.util.List;

import com.careerhub.entity.User;

public interface UserDao {
	//CRUD operations
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userID);
    User getUserByID(int userID);
    User getUserByEmail(String email);
    
    //admin purpose
    List<User> getAllUsers();
}
