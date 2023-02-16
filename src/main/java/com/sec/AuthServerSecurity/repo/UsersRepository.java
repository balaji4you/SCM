/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sec.AuthServerSecurity.repo;

import java.util.List;
 
import org.springframework.data.mongodb.repository.MongoRepository;

import com.sec.AuthServerSecurity.entity.Users;
 

 

public interface UsersRepository extends MongoRepository<Users, String> {

	//@Query("{email:'?0'}")
	 List<Users> findAll();
 
}
