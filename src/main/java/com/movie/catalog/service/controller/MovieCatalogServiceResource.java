package com.movie.catalog.service.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.movie.catalog.service.model.CataLogItem;
import com.movie.types.model.Movie;
import com.movie.types.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogServiceResource {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@GetMapping("/{userId}")
	public List<CataLogItem> getCataLog(@PathVariable("userId") String userId) {

		// List<Rating> ratings = Arrays.asList(new Rating("1234", 4), new
		// Rating("5678", 3));

		UserRating userRating = restTemplate.getForObject("http://EAI-1014-RATING-DATA-SERVICE/ratingsData/user/" + userId,
				UserRating.class);

		return userRating.getUserRating().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://EAI-1016-MOVIE-INFO-SERVICE/movies/" + rating.getMovieId(), Movie.class);
			/*
			 * Movie movie = webClientBuilder.build() .get()
			 * .uri("http://localhost:8081/movies/" + rating.getMovieId()) .retrieve()
			 * .bodyToMono(Movie.class) .block();
			 */
			return new CataLogItem(movie.getName(), "Desc", rating.getRating());
		}).collect(Collectors.toList());

	}

}
