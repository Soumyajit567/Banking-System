package dev.codescreen.repository;

import dev.codescreen.model.LoadRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoadRequestRepository extends JpaRepository<LoadRequest, Long> {
}