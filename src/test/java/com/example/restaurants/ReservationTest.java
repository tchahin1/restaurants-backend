package com.example.restaurants;

import com.example.restaurants.controllers.ReservationsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class ReservationTest {

	@Autowired
	ReservationsService reservationsService;

	@Test(expected = NullPointerException.class)
	public void getDeletedTemplateStepIdsTest() {
		boolean b = reservationsService.addCorrespondingTime("07/05/2019", 3);
		assertFalse(b);
	}
}
