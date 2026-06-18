package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	private Long existingMovieId, nonExistingMovieId;
	private MovieEntity movie;
	private UserEntity user;
	private ScoreEntity score;
	private ScoreDTO scoreDTO;


	@BeforeEach
	public void setUp() {
		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		user = new UserEntity(1l, "Maria", "maria@gmail.com", "12345");
		movie = new MovieEntity(existingMovieId, "Test Movie", 0.0, 0, "https://img.com/test.jpg");

		scoreDTO = new ScoreDTO(existingMovieId, 4.0);

		score = new ScoreEntity();
		score.setMovie(movie);
		score.setUser(user);
		score.setValue(4.0);

		movie.getScores().add(score);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		when(userService.authenticated()).thenReturn(user);
		when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
		when(scoreRepository.saveAndFlush(any())).thenReturn(score);
		when(movieRepository.save(any())).thenReturn(movie);

		MovieDTO result = service.saveScore(scoreDTO);

		assertNotNull(result);
		assertEquals(scoreDTO.getMovieId(), result.getId());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		ScoreDTO scoreDTONonExisting = new ScoreDTO(nonExistingMovieId, 4.0);

		when(userService.authenticated()).thenReturn(user);
		when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class , () -> service.saveScore(scoreDTONonExisting));
	}
}
