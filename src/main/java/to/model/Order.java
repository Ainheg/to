package to.model;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "employee_id", insertable = false, nullable = false)
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "shipment_id", nullable = true)
    private Shipment shipment;

    @Column(name = "placing_date")
    private Timestamp placingDate;

    @OneToMany
    @JoinColumn(name = "order_id")
    private Set<Item> items;

    public Order() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Timestamp getPlacingDate() {
        return placingDate;
    }

    public void setPlacingDate(Timestamp placingDate) {
        this.placingDate = placingDate;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}