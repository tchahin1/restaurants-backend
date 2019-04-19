package com.example.restaurants.services;

import com.example.restaurants.data.models.City;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CityService {

	public boolean isValid(City city) {
		return city != null &&
			!StringUtils.isEmpty(city.getName()) &&
			city.getCountry() != null &&
			city.getCountry().getId() != null;
	}
}
