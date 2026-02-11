package com.example.simple_inventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "inventories")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama produk wajib diisi")
    private String productName;

    @NotNull(message = "Stock wajib diisi")
    @Min(value = 0, message = "Stock minimal 0")
    private Integer stock;
}
