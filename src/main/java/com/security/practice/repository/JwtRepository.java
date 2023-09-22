package com.security.practice.repository;

import com.security.practice.Jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface JwtRepository extends CrudRepository<RefreshToken, String> {
}
