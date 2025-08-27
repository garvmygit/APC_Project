package org.GG.apcProject.controller;

import java.util.List;

import org.GG.apcProject.model.Product;
import org.GG.apcProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    // PUBLIC: List all products
    @GetMapping
    public List<Product> list() {
        return productRepo.findAll();
    }

    // PUBLIC: Get single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> one(@PathVariable Long id) {
        return productRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ADMIN ONLY: Create a product
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product create(@RequestBody Product p) {
        return productRepo.save(p);
    }

    // ADMIN ONLY: Update a product
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product newP) {
        return productRepo.findById(id).map(p -> {
            p.setName(newP.getName());
            p.setDescription(newP.getDescription());
            p.setPrice(newP.getPrice());
            p.setStock(newP.getStock());
            return ResponseEntity.ok(productRepo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    // OPTIONAL: Delete a product (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return productRepo.findById(id).map(p -> {
            productRepo.delete(p);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
