package com.stretto.demo.features.product;

import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.product.domain.dto.ProductDTORequest;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductDTOResponse create(ProductDTORequest request);

    List<ProductDTOResponse> findAll();

    ProductDTOResponse findById(Long id);

    ProductDTOResponse update(Long id, ProductDTORequest request);

    void delete(Long id);

    ProductDTOResponse activate(Long id);

    List<ProductDTOResponse> searchByName(String name);

    List<ProductDTOResponse> getLowStock (Integer limit);

    List<ProductDTOResponse> getAvailable();

    ProductDTOResponse updatePrice (Long id, BigDecimal price);


}
