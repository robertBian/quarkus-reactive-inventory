package dev.robertbian.inventory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {
    @Test void reservationPreservesInventoryInvariant() {
        InventoryItem item = new InventoryItem("SKU-123", 10);
        item.reserve(4);
        assertEquals(6, item.available);
        assertEquals(4, item.reserved);
        assertEquals(10, item.available + item.reserved);
    }

    @Test void preventsOversellingAndBadQuantities() {
        InventoryItem item = new InventoryItem("SKU-456", 3);
        assertThrows(IllegalStateException.class, () -> item.reserve(4));
        assertThrows(IllegalArgumentException.class, () -> item.reserve(0));
        assertEquals(3, item.available);
    }

    @Test void refusesInvalidStartingStock() {
        assertThrows(IllegalArgumentException.class, () -> new InventoryItem("", -1));
    }
}
