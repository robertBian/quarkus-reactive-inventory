package dev.robertbian.inventory;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class InventoryService {
    @WithTransaction
    public Uni<ItemView> create(String sku, int available) {
        var item = new InventoryItem(sku, available);
        return item.persistAndFlush().map(ignored -> ItemView.from(item));
    }

    public Uni<ItemView> get(String sku) {
        return InventoryItem.<InventoryItem>find("sku", sku)
                .firstResult()
                .onItem().ifNull().failWith(() -> new NotFoundException("Unknown SKU"))
                .map(ItemView::from);
    }

    @WithTransaction
    public Uni<ItemView> reserve(String sku, int count) {
        return InventoryItem.<InventoryItem>find("sku", sku)
                .firstResult()
                .onItem().ifNull().failWith(() -> new NotFoundException("Unknown SKU"))
                .invoke(item -> item.reserve(count))
                .call(InventoryItem::flush)
                .map(ItemView::from);
    }

    public record ItemView(String sku, int available, int reserved, long version) {
        static ItemView from(InventoryItem item) {
            return new ItemView(item.sku, item.available, item.reserved, item.version);
        }
    }
}
