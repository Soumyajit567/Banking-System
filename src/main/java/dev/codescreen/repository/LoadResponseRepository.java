package dev.codescreen.repository;

import dev.codescreen.model.LoadResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoadResponseRepository extends JpaRepository<LoadResponse, Long> {
}