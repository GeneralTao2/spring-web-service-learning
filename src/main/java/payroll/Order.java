package payroll;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_ORDER")
class Order {
    @Id
    @GeneratedValue
    private Long id;

    private String description;
    private Status status;

    @JsonBackReference
    /* TODO: for some reason this doesn't work
    *   ...Invalid definition for property : Cannot refine serialization type [simple type, class ] into java.lang.Long;...*/
    //@JsonSerialize(using = EmployeeToEmployeeIdSerializer.class, as=Long.class)
    @ManyToOne
    private Employee employee;

    @JsonProperty
    public Long getEmployeeId() {
        if(this.employee == null) {
            return null;
        } else {
            return employee.getId();
        }
    }
    Order() {}

    Order(String description, Status status) {

        this.description = description;
        this.status = status;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Order))
            return false;
        Order order = (Order) o;
        return Objects.equals(this.id, order.id) && Objects.equals(this.description, order.description)
                && this.status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.description, this.status);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + this.id + ", description='" + this.description + '\'' + ", status=" + this.status + '}';
    }
}