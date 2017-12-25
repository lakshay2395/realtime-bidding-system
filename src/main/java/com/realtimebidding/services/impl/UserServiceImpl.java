package com.realtimebidding.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.realtimebidding.model.User;
import com.realtimebidding.repository.UserRepository;
import com.realtimebidding.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User getById(String id) throws Exception {
		return userRepository.findOne(id);
	}

	@Override
	public User addNewUser(User user) throws Exception {
		return userRepository.insert(user);
	}

	@Override
	public User updateUser(String id, User user) throws Exception {
		if(userRepository.exists(id))
			return userRepository.save(user);
		throw new RuntimeException("No such user exists with user id -> "+id);
	}

	@Override
	public User deleteUser(String id) throws Exception {
		if(userRepository.exists(id)) {
			User u = userRepository.findOne(id);
			userRepository.delete(id);
			return u;
		}
		throw new RuntimeException("No such user exists with user id -> "+id);
	}

	@Override
	public User getByEmail(String email) throws Exception {
		System.out.println("email -> "+email);
		return userRepository.findByEmail(email);
	}

}
