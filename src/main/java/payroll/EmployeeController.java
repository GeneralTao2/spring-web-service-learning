package payroll;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.aspectj.weaver.ast.Or;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

// indicates that the data returned by each method will be written straight
// into the response body instead of rendering a template.
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final OrderRepository orderRepository;

    private final EmployeeModelAssembler assembler;

    private final RestTemplate restTemplate;

    EmployeeController(EmployeeRepository employeeRepository, OrderRepository orderRepository, EmployeeModelAssembler assembler) {

        this.employeeRepository = employeeRepository;
        this.orderRepository = orderRepository;
        this.assembler = assembler;
        this.restTemplate = new RestTemplate();
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {
        //System.out.println(orderRepository.findAll());
        List<EntityModel<Employee>> employees = employeeRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }
    // end::get-aggregate-root[]

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
        Employee savedEmployee = employeeRepository.save(newEmployee);

        EntityModel<Employee> entityModel = assembler.toModel(savedEmployee);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @PostMapping("/employees/{id}/orders")
    ResponseEntity<?> newOrderInEmployee(@RequestBody Order newOrder, @PathVariable Long id) {
        Employee employee = employeeRepository.findById(id) //
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        employee.addOrder(newOrder);

        EntityModel<Employee> entityModel = assembler.toModel(employeeRepository.save(employee));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @GetMapping("/employees/{employeeId}/orders/{orderId}")
    ResponseEntity<?> newOrderInEmployee(@PathVariable Long employeeId, @PathVariable Long orderId) {
        Employee employee = employeeRepository.findById(employeeId) //
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
        Order order = orderRepository.findById(orderId) //
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        employee.addOrder(order);

        EntityModel<Employee> entityModel = assembler.toModel(employeeRepository.save(employee));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    // Single item

    @GetMapping("/employees/{id}")
    // EntityModel<T> is a generic container from Spring HATEOAS
    // that includes not only the data but a collection of links.
    EntityModel<Employee> one(@PathVariable Long id) {

        Employee employee = employeeRepository.findById(id) //
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        Employee updatedEmployee = employeeRepository.findById(id) //
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return employeeRepository.save(employee);
                }) //
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return employeeRepository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }

    @DeleteMapping("/employees/{id}/orders/{orderIndex}")
    ResponseEntity<?> removeOrder(@PathVariable Long id, @PathVariable int orderIndex) {

        Employee employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        /* TODO: index out of bounds */
        Order order = employee.getOrders().get(orderIndex);
        employee.removeOrder(order);
        employeeRepository.save(employee);
        orderRepository.save(order);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

        employeeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}