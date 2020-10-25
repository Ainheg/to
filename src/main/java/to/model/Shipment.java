package to.model;

import javax.persistence.*;

@Entity
@Table(name = "SHIPMENTS")
public class Shipment {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "address", nullable = false)
    private String address;

    public Shipment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
