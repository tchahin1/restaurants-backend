package com.example.restaurants;

import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.UsersRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	UsersRepository userRepository;

	@Test
	public void testRegisterValid() {
		Users user = new Users();
		user.setEmail("tarek@mail.com");
		user.setPassword("testPassword");
		user.setName("tarek");
		user.setLastName("chahin");
		user.setPhoneNumber("000 000 000");

		entityManager.persist(user);

		Optional<Users> found = Optional.ofNullable(userRepository.findByEmail("tarek@mail.com"));
		assertEquals(user.getEmail(), found.get().getEmail());
	}

	@Test(expected = Exception.class)
	public void testRegisterInvalid() {
		Users user = new Users();
		user.setEmail("tarek@mail.com");
		user.setPassword("testPassword");
		user.setName("tarek");
		user.setLastName("chahin");
		user.setPhoneNumber("000 000 000");

		entityManager.persist(user);

		Users user2 = new Users();
		user2.setEmail("tarek@mail.com");
		user2.setPassword("testPassword");
		user2.setName("tarek");
		user2.setLastName("chahin");
		user2.setPhoneNumber("000 000 000");

		entityManager.persist(user2);
		Optional<Users> found = Optional.ofNullable(userRepository.findByEmail("tarek@mail.com"));
	}
}
