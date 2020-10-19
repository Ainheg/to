package to.model;

import javax.persistence.*;

@Entity
@Table(name = "ITEMS")
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, nullable = false)
    private Product product;

    @Column(name = "quantity")
    private Double quantity;

    public Item() {

    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
