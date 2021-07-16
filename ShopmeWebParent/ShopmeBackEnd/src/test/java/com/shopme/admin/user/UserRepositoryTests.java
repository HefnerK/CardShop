package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userKyle = new User("kylethefner@gmail.com", "password", "Kyle", "Hefner");
		userKyle.addRole(roleAdmin);
		
		User savedUser = repo.save(userKyle);
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userMatt = new User("Matt@gmail.com", "password1", "Matt", "Stone");
		Role roleEditor = new Role(2);
		Role roleAssistant = new Role(5);

		userMatt.addRole(roleEditor);
		userMatt.addRole(roleAssistant);
		
		User savedUser = repo.save(userMatt);
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userKyle = repo.findById(1).get();
		System.out.println(userKyle);
		assertThat(userKyle).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userKyle = repo.findById(1).get();
		userKyle.setEnabled(true);
		userKyle.setEmail("kk@gmail.com");
		
		repo.save(userKyle);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userKyle = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userKyle.getRoles().remove(roleEditor);
		userKyle.addRole(roleSalesperson);
		
		User savedUser = repo.save(userKyle);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);;
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "Matt@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
	Integer id = 1;
	Long countById = repo.countById(id);
	
	assertThat(countById).isNotNull().isGreaterThan(0);
		
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 4;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 4;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "matt";
		
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword,pageable);
		List<User> listUsers = page.getContent();
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
	
}
