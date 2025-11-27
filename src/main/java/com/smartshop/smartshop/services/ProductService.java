package com.smartshop.smartshop.services;

import com.smartshop.smartshop.dto.product.ProductRequest;
import com.smartshop.smartshop.dto.product.ProductResponse;
import com.smartshop.smartshop.exceptions.BusinessException;
import com.smartshop.smartshop.exceptions.ResourceNotFoundException;
import com.smartshop.smartshop.mappers.ProductMapper;
import com.smartshop.smartshop.models.product.Product;
import com.smartshop.smartshop.repositories.ProductRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(boolean includeDeleted) {
        List<Product> products = includeDeleted ? productRepository.findAll() : productRepository.findByDeletedFalse();
        return productMapper.toResponseList(products);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        return productMapper.toResponse(findProductById(id));
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new BusinessException("SKU already exists");
        }
        Product product = productMapper.toEntity(request);
        OffsetDateTime now = OffsetDateTime.now();
        product.setDeleted(false);
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProductById(id);
        if (!product.getSku().equalsIgnoreCase(request.getSku())
                && productRepository.existsBySku(request.getSku())) {
            throw new BusinessException("SKU already exists");
        }
        productMapper.updateEntityFromRequest(request, product);
        product.setUpdatedAt(OffsetDateTime.now());
        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void softDeleteProduct(Long id) {
        Product product = findProductById(id);
        product.setDeleted(true);
        product.setUpdatedAt(OffsetDateTime.now());
        productRepository.save(product);
    }

    Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }
}

