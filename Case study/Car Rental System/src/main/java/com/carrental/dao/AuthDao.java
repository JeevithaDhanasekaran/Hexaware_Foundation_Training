package com.carrental.dao;

import java.util.List;

import com.carrental.entity.Auth;
import com.carrental.exception.DatabaseConnectionException;

public interface  AuthDao {
	//add
	boolean addAuth(Auth auth) throws DatabaseConnectionException;
	
	//update 
    boolean updateAuth(Auth auth) throws DatabaseConnectionException;
    
    //delete
    boolean deleteAuth(int authId) throws DatabaseConnectionException;
    
    //delete by customer id
    boolean deleteAuthByCustomerId(int CustomerId) throws DatabaseConnectionException;
    
    //get by auth id
    boolean getAuthById(int authId) throws DatabaseConnectionException;
    
    //get auth by customer id
    Auth getAuthByCustomerId(int customerId) throws DatabaseConnectionException;
    
    //get auth by username
    Auth getAuthByUsername(String username) throws DatabaseConnectionException;
    
    //list all auth
    List<Auth> getAllAuths() throws DatabaseConnectionException;
    
    //login purpose -> return auth if matched
    Auth findByUsernameAndPassword(String username, String password) throws DatabaseConnectionException;

    //update passwords
	boolean updatePassword(int customerId, String newPassword);
}
