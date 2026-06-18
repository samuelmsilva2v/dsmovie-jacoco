package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;

	@Mock
	private MovieRepository repository;

	private Long existingMovieId, nonExistingMovieId, dependentMovieId;
	private MovieEntity movie;
	private MovieDTO movieDTO;
	private Page<MovieEntity> page;

	@BeforeEach
	public void setUp() {
		existingMovieId = 1L;
		nonExistingMovieId = 2L;
		dependentMovieId = 3L;

		movie = new MovieEntity(existingMovieId, "Test Movie", 4.5, 10, "https://img.com/test.jpg");
		movieDTO = new MovieDTO(movie);
		page = new PageImpl<>(List.of(movie));
	}
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		when(repository.searchByTitle(any(), any(Pageable.class))).thenReturn(page);
		Pageable pageable = PageRequest.of(0, 12);

		Page<MovieDTO> result = service.findAll("Test", pageable);

		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(1, result.getTotalElements());
		assertEquals("Test Movie", result.getContent().get(0).getTitle());
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		when(repository.findById(existingMovieId)).thenReturn(Optional.of(movie));

		MovieDTO result = service.findById(existingMovieId);

		assertNotNull(result);
		assertEquals(existingMovieId, result.getId());
		assertEquals("Test Movie", result.getTitle());
		verify(repository, times(1)).findById(existingMovieId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		when(repository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingMovieId));
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
		when(repository.save(any())).thenReturn(movie);

		MovieDTO result = service.insert(movieDTO);

		assertNotNull(result);
		assertEquals(result.getId(), movie.getId());
		assertEquals(result.getTitle(), movieDTO.getTitle());
		verify(repository).save(any(MovieEntity.class));
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
		when(repository.getReferenceById(existingMovieId)).thenReturn(movie);
		when(repository.save(any())).thenReturn(movie);

		MovieDTO result = service.update(existingMovieId, movieDTO);

		assertNotNull(result);
		assertEquals(result.getId(), movie.getId());
		assertEquals(result.getTitle(), movieDTO.getTitle());
		verify(repository).getReferenceById(existingMovieId);
		verify(repository).save(any(MovieEntity.class));
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		when(repository.getReferenceById(nonExistingMovieId)).thenThrow(ResourceNotFoundException.class);

		assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistingMovieId, movieDTO));
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		when(repository.existsById(existingMovieId)).thenReturn(true);
		doNothing().when(repository).deleteById(existingMovieId);

		assertDoesNotThrow(() -> service.delete(existingMovieId));

		verify(repository).existsById(existingMovieId);
		verify(repository).deleteById(existingMovieId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		when(repository.existsById(nonExistingMovieId)).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingMovieId));
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		when(repository.existsById(dependentMovieId)).thenReturn(true);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentMovieId);

		assertThrows(DatabaseException.class, () -> service.delete(dependentMovieId));
	}
}
