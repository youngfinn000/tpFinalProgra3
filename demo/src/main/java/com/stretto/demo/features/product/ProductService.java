package com.stretto.demo.features.product;

import com.stretto.demo.features.product.domain.dto.ProductDTORequest;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;

import java.util.List;

public interface ProductService {

    ProductDTOResponse create(ProductDTORequest request);

    List<ProductDTOResponse> findAll();

    ProductDTOResponse findById(Long id);

    ProductDTOResponse update(Long id, ProductDTORequest request);

    void delete(Long id);

    ProductDTOResponse activate(Long id);

    List<ProductDTOResponse> findByFlavor(Long flavorId);
}
