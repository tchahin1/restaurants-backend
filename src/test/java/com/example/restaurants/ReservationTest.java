package com.example.restaurants;

import com.example.restaurants.controllers.ReservationsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertFalse;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class ReservationTest {

	@Autowired
	ReservationsService reservationsService;

	@Test
	public void add_CorrespondingTimeForAdequateNumberOfPeople_ReturnsTrueIfTimeIsInRange() {

		boolean expected = true;

		String dateFrom = "05/07/2019 12:30";
		LocalDateTime from = LocalDateTime.parse(dateFrom, DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
		reservationsService.setFrom(from);
		reservationsService.setTo(from);

		boolean actual = reservationsService.addCorrespondingTime("05/07/2019", 3);

		Assert.assertEquals(actual, expected);
	}

	@Test
	public void findNearestTable_IfExists_ReturnsTables() {

		Integer expected = 4;

		Integer actual = reservationsService.findNearest(3).getType();

		Assert.assertEquals(actual, expected);
	}
}
