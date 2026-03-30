package com.interswitch.first_api.repositories;

import com.interswitch.first_api.entities.Department;
import com.interswitch.first_api.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Employee> findByDepartment(Department department, Pageable pageable);

    boolean existsByDepartment_Id(Long id);
}
