package payroll;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// To get all this free functionality, all we had to do was declare an interface which
// extends Spring Data JPA’s JpaRepository, specifying the domain type as Employee and the id type as Long.
interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByRole(String lastname, Sort sort);
}