package com.example.simple_inventory.service;

import com.example.simple_inventory.entity.Inventory;
import com.example.simple_inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository repository;

    @Cacheable(value = "inventories", key = "#id")
    public Inventory getById(Long id) {
        log.info("Fetching inventory with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory tidak ditemukan"));
    }

    @CachePut(value = "inventories", key = "#result.id")
    public Inventory save(Inventory inventory) {
        Inventory saved = repository.save(inventory);
        log.info("Created inventory: {}", saved.getId());
        return saved;
    }

    @CachePut(value = "inventories", key = "#id")
    public Inventory update(Long id, Inventory inventory) {
        Inventory existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory tidak ditemukan"));

        existing.setProductName(inventory.getProductName());
        existing.setStock(inventory.getStock());

        Inventory saved = repository.save(existing);
        log.info("Updated inventory: {}", saved.getId());
        return saved;
    }

    @CacheEvict(value = "inventories", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
        log.info("Deleted inventory: {}", id);
    }
}

