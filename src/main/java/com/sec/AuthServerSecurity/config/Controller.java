package com.sec.AuthServerSecurity.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sec.AuthServerSecurity.entity.Role;
//http://34.225.16.50:3000
//35.236.198.199
//@CrossOrigin(origins = "http://${ip}:3000")
import com.sec.AuthServerSecurity.entity.User;
import com.sec.AuthServerSecurity.entity.Users;
import com.sec.AuthServerSecurity.repo.RoleRepository;
import com.sec.AuthServerSecurity.repo.UserRepository;
import com.sec.AuthServerSecurity.repo.UsersRepository;

@RestController
@RequestMapping("/superadmin")
public class Controller {

	@Autowired
	UserRepository userrepo;

	@Autowired
	UsersRepository usersrepo;

	@Autowired
	RoleRepository rolesrepo;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String toFhirServer() {
    	   return "Successfully Running...";
	}


	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "/adduser/{roleid}", method = RequestMethod.POST)
	public Integer addUser(@Valid @RequestBody User user, @PathVariable("roleid") String roleid) {

		String email = user.getEmail();

		User userObj = userrepo.findByEmail(email);
		if (userObj != null)
			return 301;

		Role roleObj = rolesrepo.findById(roleid).get();

		if (roleObj == null)
			return 302;

		Set<Role> roles = new HashSet<Role>();

		roles.add(roleObj);

		String password = user.getPassword();
		String hashedPassword = new BCryptPasswordEncoder().encode(password);
		user.setPassword(hashedPassword.trim());
		user.setRoles(roles);
		user.setEmail(user.getEmail().trim());
		userrepo.save(user);
		return 200;
	}

	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "/changerole", method = RequestMethod.GET)
	public Integer changeRole(@RequestParam(name = "roleid") String id, @RequestParam(name = "userid") String userid) {

		Role roleObj = rolesrepo.findById(id).get();

		if (roleObj == null)
			return 302;

		User userObj = userrepo.findById(userid).get();
		if (userObj == null)
			return 303;

		Set<Role> roles = new HashSet<Role>();

		roles.add(roleObj);

		userObj.setRoles(roles);

		System.out.println(userObj.toString());
		userrepo.save(userObj);

		return 200;
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "/getroles", method = RequestMethod.GET)
	public List<Role> getroleslist() {
		
		List<Role> rolelist = rolesrepo.findAll();

		System.out.println(rolelist);
		

		return rolelist;
	}
	
	

	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "/resetpass", method = RequestMethod.GET)
	public Integer resetPassword(@RequestParam(name = "pass") String password,
			@RequestParam(name = "userid") String userid) {

		User userObj = userrepo.findById(userid).get();
		if (userObj == null)
			return 303;

		String hashedPassword = new BCryptPasswordEncoder().encode(password);
		userObj.setPassword(hashedPassword);

		System.out.println(userObj.toString());
		userrepo.save(userObj);

		return 200;
	}

	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "/changestatus", method = RequestMethod.GET)
	public Integer resetPassword(@RequestParam(name = "userid") String userid) {
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		System.out.println(auth.getPrincipal());
		System.out.println(auth.getName());
		User userObj = userrepo.findById(userid).get();
		if (userObj == null)
			return 303;

		userObj.setEnabled(!userObj.isEnabled());

		System.out.println(userObj.toString());
		userrepo.save(userObj);

		return 200;
	}

	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<Users> getAllUsers(@RequestParam(name = "email") String email) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		System.out.println(auth.getPrincipal());
		System.out.println(auth.getName());
		
		List<Users> users = usersrepo.findAll();

		List<Users> usersExceptSuperAdmin = users.stream().filter(f -> !f.getEmail().equals(email))
				.collect(Collectors.toList());

		return usersExceptSuperAdmin;

	}

}
