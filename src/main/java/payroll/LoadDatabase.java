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
            //orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            //orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));
            employeeRepository.deleteAll();
            Employee employee1 = new Employee("Bilbo", "Baggins", "burglar", new ArrayList<>());
            //employeeRepository.save(employee1);

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

            //Pageable firstPageWithTwoElements = PageRequest.of(0, 3);
            //List<Employee> firstPage = employeeRepository.findAll(Sort.by("firstName").ascending());
            List<Employee> firstPage = employeeRepository.findByRole("z", Sort.sort(Employee.class).by(Employee::getFirstName).descending());
            log.info("First page: " + firstPage);
            //System.out.println(new ArrayList<>(List.of('q','w','e','r','t','y')).stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()));
            //orderRepository.save(order1);
            //order1 = orderRepository.findAll().get(0);
            employee1.addOrder(order1);
            employee1.addOrder(order3);
            employeeRepository.save(employee1);
            Order order2 = orderRepository.findByDescription("Xiaomi").get(0);
            Employee employee2 = order2.getEmployee();
            //employee2.removeOrder(order2);
            employeeRepository.save(employee2);
            //employeeRepository.flush();
           // orderRepository.delete(order2);
            //System.out.println(order2);
            //employee1.removeOrder(order1);
            //employeeRepository.flush();
            //orderRepository.flush();
            //employee1.removeOrder(order1);
            //employeeRepository.save(employee1);
            /*employee1.addOrder(order1);
            employeeRepository.save(employee1);
            //employee1.removeOrder(order1);
            System.out.println(employee1.getOrders().remove(order1));
            System.out.println(employee1.getOrders().remove(order1));
            order1.setEmployee(null);
            orderRepository.save(order1);
            employeeRepository.save(employee1);*/
            //employee1.addOrder(order1);
            //employeeRepository.save(employee1);
            //employee1.getOrders().add(order1);
            //order1.setEmployee(employee1);
            //employeeRepository.save(employee1);
            //orderRepository.save(order1);
            //employeeRepository.save(new Employee("Frodo", "Baggins", "thief"));
            //Long id1 = 9L;
            //Employee employee1 = employeeRepository.findAll().get(0);
            //System.out.println(employee1.getOrders().remove(0));
            //System.out.println(employee1.getOrders());
           //employee1.getOrders().remove(0);
            /*employee1.getOrders().clear();
            employeeRepository.save(employee1);
            orderRepository.deleteAll();
            //orderRepository.flush();
            //employeeRepository.saveAndFlush(employee1);
            //employeeRepository.co
            //orderRepository.deleteAll();
            Order order1 = new Order("Xiaomi", Status.IN_PROGRESS);
            //orderRepository.save(order1);
            //Order order1 = orderRepository.findAll().get(0);
            orderRepository.save(order1);
            order1 = orderRepository.findAll().get(0);
            System.out.println(order1.getId());
            employee1.getOrders().add(order1);
            //employee1.getOrders().remove(0);
            employeeRepository.save(employee1);*/

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));
        };
    }
}