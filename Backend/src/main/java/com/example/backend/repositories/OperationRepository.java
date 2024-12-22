package com.example.backend.repositories;

import com.example.backend.model.Operations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operations, Long> {


}
