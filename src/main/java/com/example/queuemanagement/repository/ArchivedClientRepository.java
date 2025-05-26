package com.example.queuemanagement.repository;

import com.example.queuemanagement.model.ArchivedClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivedClientRepository extends JpaRepository<ArchivedClient, Long> {
}
