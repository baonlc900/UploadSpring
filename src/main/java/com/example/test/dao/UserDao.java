package com.example.test.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.test.model.User;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
	
}
