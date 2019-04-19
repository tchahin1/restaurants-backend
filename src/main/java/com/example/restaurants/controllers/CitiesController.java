package com.example.restaurants.controllers;

import com.example.restaurants.data.commons.Response;
import com.example.restaurants.data.models.City;
import com.example.restaurants.data.models.Country;
import com.example.restaurants.data.models.Restaurant;
import com.example.restaurants.data.models.Users;
import com.example.restaurants.repositories.CityRepository;
import com.example.restaurants.repositories.CountryRepository;
import com.example.restaurants.repositories.RestaurantRepository;
import com.example.restaurants.repositories.UserRepository;
import com.example.restaurants.services.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cities")
public class CitiesController {

	private final CityRepository cityRepository;

	private final CountryRepository countryRepository;

	private final UserRepository userRepository;

	private final RestaurantRepository restaurantRepository;

	private final CityService cityService;

	public CitiesController(CityRepository cityRepository, CountryRepository countryRepository, UserRepository userRepository, RestaurantRepository restaurantRepository, CityService cityService) {
		this.cityRepository = cityRepository;
		this.countryRepository = countryRepository;
		this.userRepository = userRepository;
		this.restaurantRepository = restaurantRepository;
		this.cityService = cityService;
	}

	@GetMapping()
	public ResponseEntity getAll() {
		Iterable<City> all = cityRepository.findAll();
		List<City> cities = new ArrayList<>();
		all.forEach(cities::add);

		Response response = Response.builder()
			.success(true)
			.data(cities)
			.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/byId")
	public ResponseEntity getById(@RequestParam Long id) {
		Optional<City> city = cityRepository.findById(id);

		if (!city.isPresent()) {
			return new ResponseEntity<>(
				Response.builder()
					.success(false)
					.message("City not found.")
					.build(), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(
			Response.builder()
				.success(true)
				.data(city)
				.build(), HttpStatus.OK);
	}

	@GetMapping("/byName")
	public ResponseEntity getByName(@RequestParam String name) {
		List<City> cities = cityRepository.findByNameContainingIgnoreCase(name);

		return new ResponseEntity<>(
			Response.builder()
				.success(true)
				.data(cities)
				.build(), HttpStatus.OK);
	}

	@GetMapping("/byCountry")
	public ResponseEntity getByCountry(@RequestParam Long countryId) {
		List<City> cities = cityRepository.findByCountryId(countryId);

		Response response = Response.builder()
			.success(true)
			.data(cities)
			.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/count")
	public long getCount() {
		return cityRepository.count();
	}

	@PostMapping()
	public ResponseEntity create(@RequestBody City city) {
		if (!cityService.isValid(city)) {
			return new ResponseEntity<>(
				Response.builder()
					.success(false)
					.message("City not valid.")
					.build(), HttpStatus.BAD_REQUEST);
		}

		Optional<Country> country = countryRepository.findById(city.getCountry().getId());
		if (!country.isPresent()) {
			return new ResponseEntity<>(
				Response.builder()
					.success(false)
					.message("Country not found.")
					.build(), HttpStatus.BAD_REQUEST);
		}

		City c = new City();
		c.setName(city.getName());
		c.setCountry(country.get());
		cityRepository.save(c);

		return new ResponseEntity<>(
			Response.builder()
				.success(true)
				.data(c)
				.message("New city successfully added.")
				.build(), HttpStatus.OK);
	}

	@PutMapping()
	public ResponseEntity edit(@RequestBody City city) {
		if (!cityService.isValid(city)) {
			return new ResponseEntity<>(
				Response.builder()
					.success(false)
					.message("City not valid.")
					.build(), HttpStatus.BAD_REQUEST);
		}

		Optional<City> c = cityRepository.findById(city.getId());
		if (!c.isPresent()) {
			return new ResponseEntity<>(
				Response.builder()
					.success(false)
					.message("City not found.")
					.build(), HttpStatus.BAD_REQUEST);
		}

		Optional<Country> country = countryRepository.findById(city.getCountry().getId());
		if (!country.isPresent()) {
			return new ResponseEntity<>(
				Response.builder()
					.success(false)
					.message("Country not found.")
					.build(), HttpStatus.BAD_REQUEST);
		}

		c.get().setName(city.getName());
		c.get().setCountry(country.get());
		cityRepository.save(c.get());

		return new ResponseEntity<>(
			Response.builder()
				.success(true)
				.data(city)
				.message("City successfully updated.")
				.build(), HttpStatus.OK);
	}

	@DeleteMapping()
	public ResponseEntity delete(@RequestParam Long id) {
		Optional<City> city = cityRepository.findById(id);

		if (!city.isPresent()) {
			return new ResponseEntity<>(
				Response.builder()
					.success(false)
					.message("City not found.")
					.build(), HttpStatus.BAD_REQUEST);
		}

		List<Users> users = userRepository.findByCityId(city.get().getId());
		users.forEach(user -> {
			user.setCountry(null);
			user.setCity(null);
		});

		List<Restaurant> restaurants = restaurantRepository.findByCityId(city.get().getId());
		restaurants.forEach(restaurant -> {
			restaurant.setCity(null);
			restaurant.setAddress(null);
		});

		cityRepository.delete(city.get());

		return new ResponseEntity<>(
			Response.builder()
				.success(true)
				.message("City successfully deleted.")
				.build(), HttpStatus.OK);
	}
}
