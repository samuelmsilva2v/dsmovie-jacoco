package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.utils.CustomUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CustomUserUtil  customUserUtil;

	private String existingUsername, nonExistingUsername;
	private UserEntity  userEntity;
	private UserDetailsProjection  userDetailsProjection;
	UserDetailsProjection projection = mock(UserDetailsProjection.class);

	@BeforeEach
	public void setUp() {
		existingUsername = "maria@gmail.com";
		nonExistingUsername = "bob@gmail.com";

		userEntity = new UserEntity(1L, "Maria", "maria@gmail.com", "12345");
	}

	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		when(customUserUtil.getLoggedUsername()).thenReturn(existingUsername);
		when(userRepository.findByUsername(existingUsername)).thenReturn(Optional.of(userEntity));

		UserEntity result = service.authenticated();

		assertNotNull(result);
		assertEquals(existingUsername, result.getUsername());
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		when(customUserUtil.getLoggedUsername()).thenReturn(nonExistingUsername);
		when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> service.authenticated());
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		when(projection.getUsername()).thenReturn(existingUsername);
		when(projection.getPassword()).thenReturn("12345");
		when(projection.getRoleId()).thenReturn(1L);
		when(projection.getAuthority()).thenReturn("ROLE_CLIENT");
		when(userRepository.searchUserAndRolesByUsername(existingUsername))
				.thenReturn(List.of(projection));

		UserDetails result = service.loadUserByUsername(existingUsername);

		assertNotNull(result);
		assertEquals(existingUsername, result.getUsername());
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		when(userRepository.searchUserAndRolesByUsername(nonExistingUsername)).thenReturn(List.of());

		assertThrows(UsernameNotFoundException.class,
				() -> service.loadUserByUsername(nonExistingUsername));
	}
}
