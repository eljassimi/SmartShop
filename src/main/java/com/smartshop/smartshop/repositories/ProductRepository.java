package com.smartshop.smartshop.repositories;

import com.smartshop.smartshop.models.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    List<Product> findByDeletedFalse();
}

