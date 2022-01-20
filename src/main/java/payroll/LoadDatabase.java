package payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    // Spring Boot will run ALL CommandLineRunner beans once the application context is loaded.
    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository,
                                   OrderRepository orderRepository) {

        return args -> {
            employeeRepository.deleteAll();
            Employee employee1 = new Employee("Bilbo", "Baggins", "burglar", new ArrayList<>());

            orderRepository.deleteAll();
            Order order1 = new Order("Xiaomi", Status.IN_PROGRESS);
            Order order3 = new Order("LG", Status.IN_PROGRESS);
            Iterable<Employee> orders = new ArrayList<>(List.of(
                    new Employee("1", "a", "z", new ArrayList<>()),
                    new Employee("2", "s", "z", new ArrayList<>()),
                    new Employee("3", "d", "z", new ArrayList<>()),
                    new Employee("4", "f", "z", new ArrayList<>()),
                    new Employee("5", "g", "b", new ArrayList<>()),
                    new Employee("6", "h", "n", new ArrayList<>())
            ));
            employeeRepository.saveAll(orders);

            Iterable<Order> orders2 = new ArrayList<>(List.of(
                    new Order("Samsung", Status.IN_PROGRESS),
                    new Order("Apple", Status.IN_PROGRESS),
                    new Order("Huawei", Status.IN_PROGRESS),
                    new Order("Nokia", Status.IN_PROGRESS)
            ));
            orderRepository.saveAll(orders2);

            List<Employee> firstPage = employeeRepository.findByRole("z", Sort.sort(Employee.class).by(Employee::getFirstName).descending());
            log.info("First page: " + firstPage);

            employee1.addOrder(order1);
            employee1.addOrder(order3);
            employeeRepository.save(employee1);
            Order order2 = orderRepository.findByDescription("Xiaomi").get(0);
            Employee employee2 = order2.getEmployee();
            employeeRepository.save(employee2);


            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));
        };
    }
}