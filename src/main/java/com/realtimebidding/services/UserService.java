package com.realtimebidding.services;

import com.realtimebidding.model.User;

public interface UserService {
	
	User getById(String id)throws Exception;
	
	User getByEmail(String email)throws Exception;
	
	User addNewUser(User user)throws Exception;
	
	User updateUser(String id,User user)throws Exception;
	
	User deleteUser(String id)throws Exception; 

}
