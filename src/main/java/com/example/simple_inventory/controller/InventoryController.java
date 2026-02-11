package com.example.simple_inventory.controller;

import com.example.simple_inventory.entity.Inventory;
import com.example.simple_inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService service;

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> get(@PathVariable Long id) {
        log.info("Request get inventory id={}", id);
        Inventory inventory = service.getById(id);
        log.info("Fetched inventory id={}", inventory.getId());
        return ResponseEntity.ok(inventory);
    }

    @PostMapping
    public ResponseEntity<Inventory> create(@RequestBody @Valid Inventory inventory) {
        log.info("Request create inventory: {}", inventory);
        Inventory saved = service.save(inventory);
        log.info("Inventory created: {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> update(@PathVariable Long id,
                                            @RequestBody @Valid Inventory inventory) {
        log.info("Request update inventory id={}", id);
        Inventory updated = service.update(id, inventory);
        log.info("Updated inventory id={}", updated.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request delete inventory id={}", id);
        service.delete(id);
        log.info("Deleted inventory id={}", id);
        return ResponseEntity.noContent().build();
    }
}
