package payroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDescription(String description);
}