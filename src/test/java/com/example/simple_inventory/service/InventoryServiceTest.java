package com.example.simple_inventory.service;

import com.example.simple_inventory.entity.Inventory;
import com.example.simple_inventory.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository repository;

    @Test
    void getById_returnInventory_whenExists() {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProductName("Item A");

        when(repository.findById(1L)).thenReturn(Optional.of(inventory));

        Inventory result = inventoryService.getById(1L);

        assertEquals("Item A", result.getProductName());
    }

    @Test
    void getById_throwException_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> inventoryService.getById(1L));
    }

    @Test
    void save_returnInventory_whenSaved() {
        Inventory inventory = new Inventory();
        inventory.setProductName("Item A");

        when(repository.save(inventory)).thenReturn(inventory);

        Inventory result = inventoryService.save(inventory);

        assertEquals("Item A", result.getProductName());
    }

    @Test
    void update_returnUpdatedInventory_whenExists() {
        Inventory existing = new Inventory();
        existing.setId(1L);
        existing.setProductName("Old Item");

        Inventory updated = new Inventory();
        updated.setProductName("New Item");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Inventory result = inventoryService.update(1L, updated);

        assertEquals("New Item", result.getProductName());
    }

    @Test
    void update_throwException_whenNotFound() {
        Inventory updated = new Inventory();
        updated.setProductName("New Item");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> inventoryService.update(1L, updated));
    }

    @Test
    void delete_invokesRepositoryDeleteById() {
        inventoryService.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}