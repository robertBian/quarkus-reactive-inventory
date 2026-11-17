package dev.robertbian.inventory;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "inventory_item")
public class InventoryItem extends PanacheEntity {
    @Column(nullable = false, unique = true, length = 64)
    public String sku;
    @Column(nullable = false)
    public int available;
    @Column(nullable = false)
    public int reserved;
    @Version
    public long version;

    protected InventoryItem() {}

    public InventoryItem(String sku, int available) {
        if (sku == null || sku.isBlank() || available < 0) {
            throw new IllegalArgumentException("Invalid item details");
        }
        this.sku = sku;
        this.available = available;
        this.reserved = 0;
    }

    /** Enforced in Java; @Version prevents silent concurrent lost updates on flush. */
    public void reserve(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (amount > available) throw new IllegalStateException("Insufficient available inventory");
        available -= amount;
        reserved += amount;
    }
}
