package to.model;

import javax.persistence.*;

@Entity
@Table(name = "SHIPMENTS")
public class Shipment {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "order_id", insertable = false, nullable = false)
    private Order order;

    @Column(name = "address")
    private String address;

    public Shipment() {
    }

    public Order getOrder() {
        return order;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
