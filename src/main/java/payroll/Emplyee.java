package payroll;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

// is a JPA annotation to make this object ready for storage in a JPA-based data store.
@Entity
class Employee {
    // JPA annotations to indicate it’s the primary key and automatically populated by the JPA provider
    @Id
    @GeneratedValue
    private Long id;
    private String firstName ="";
    private String lastName;
    private String role;

    @JsonManagedReference
    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;



    Employee() {}

    Employee(String firstName, String lastName, String role, List<Order> orders) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.orders = orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setEmployee(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setEmployee(null);
    }

    /*public void removeOrder(Long id) {
        orders.remove(id);
        order.setEmployee(null);
    }*/

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return this.id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return this.firstName + ' ' + this.lastName;
    }

    public String getRole() {
        return this.role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        String[] tmp = name.split(" ");
        this.firstName = tmp[0];
        this.lastName = tmp[1];
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Employee))
            return false;
        Employee employee = (Employee) o;
        return Objects.equals(this.id, employee.id)
                && Objects.equals(this.firstName, employee.firstName)
                && Objects.equals(this.lastName, employee.lastName)
                && Objects.equals(this.role, employee.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.firstName, this.lastName, this.role);
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + this.id + ", firstName='" + this.firstName + '\'' +
                ", lastName='" + this.lastName + '\'' + ", role='" + this.role + '\'' + '}';
    }
}