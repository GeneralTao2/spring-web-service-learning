package payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//  the assembler will be automatically created when the app starts.
@Component
class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        /*List<Link> links = employee
                .getOrders()
                .stream()
                .map(o -> linkTo(methodOn(OrderController.class).one(o.getId())).withSelfRel())
                .collect(Collectors.toList());
        links.add(linkTo(methodOn(OrderController.class).all()).withRel("orders"));
        links.add(linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel());
        links.add(linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));

        return EntityModel.of(employee, links);*/
        return EntityModel.of(employee, //
                // asks that Spring HATEOAS build a link to the EmployeeController 's one()
                // method, and flag it as a self link.
                linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                // asks Spring HATEOAS to build a link to the aggregate root, all(), and call it "employees".
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));

    }
}