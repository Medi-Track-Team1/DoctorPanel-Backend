package com.kce.repository;

import com.kce.entity.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface DepartmentRepository extends MongoRepository<Department, String> {
    Optional<Department> findByName(String name); // name = specialty
    Optional<Department> findByDepartmentId(String departmentId);
    Optional<Department> findByNameIgnoreCase(String name);

}
