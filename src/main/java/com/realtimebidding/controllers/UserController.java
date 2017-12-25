package com.realtimebidding.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.realtimebidding.dtos.EmailRequestDto;
import com.realtimebidding.dtos.Response;
import com.realtimebidding.dtos.ResponseTypeEnum;
import com.realtimebidding.model.User;
import com.realtimebidding.services.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="/{userId}", method = RequestMethod.GET)
	public Response<?> getUserById(@PathVariable("userId")String userId) {
		try {
			return new Response<User>(ResponseTypeEnum.SUCCESS,userService.getById(userId));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/email", method = RequestMethod.POST)
	public Response<?> getUserByEmailId(@RequestBody EmailRequestDto emailDto) {
		try {
			System.out.println(emailDto);
			return new Response<User>(ResponseTypeEnum.SUCCESS,userService.getByEmail(emailDto.getEmail()));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public Response<?> addNewUser(@RequestBody User user) {
		try {
			return new Response<User>(ResponseTypeEnum.SUCCESS,userService.addNewUser(user));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/{userId}", method = RequestMethod.PUT)
	public Response<?> updateUser(@PathVariable("userId")String userId,@RequestBody User user) {
		try {
			return new Response<User>(ResponseTypeEnum.SUCCESS,userService.updateUser(userId, user));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}
	
	@RequestMapping(value="/{userId}", method = RequestMethod.DELETE)
	public Response<?> deleteUserById(@PathVariable("userId")String userId) {
		try {
			return new Response<User>(ResponseTypeEnum.SUCCESS,userService.deleteUser(userId));
		}
		catch(Exception e) {
			return new Response<String>(ResponseTypeEnum.ERROR,e.getMessage());
		}
	}

}
