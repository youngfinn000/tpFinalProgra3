package com.stretto.demo.features.product;

import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.product.domain.dto.ProductDTORequest;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import com.stretto.demo.features.product.domain.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    //CREAR PRODUCTO
    @Override
    @Transactional
    public ProductDTOResponse create(ProductDTORequest request) {
        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("A product with that name already exists");
        }

        ProductEntity entity = productMapper.toEntity(request);

        entity.setActive(true);  // siempre arranca activo

        ProductEntity saved = productRepository.save(entity);

        return productMapper.toResponse(saved);
    }

    //BUSCAR TODOS
    @Override
    public List<ProductDTOResponse> findAll() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    //BUSCAR POR ID
    @Override
    public ProductDTOResponse findById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return productMapper.toResponse(entity);
    }

    //ACTUALIZAR PRODUCTO
    @Override
    @Transactional
    public ProductDTOResponse update(Long id, ProductDTORequest request)
    {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        entity.setName(request.getName());
        entity.setPrice(request.getPrice());
        entity.setMaxFlavors(request.getMaxFlavors());
        entity.setStock(request.getStock());

        ProductEntity updated = productRepository.save(entity);

        return productMapper.toResponse(updated);
    }

    //DAR DE BAJA UN PRODUCTO
    @Override
    @Transactional
    public void delete(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        entity.setActive(false);

        productRepository.save(entity);
    }

    //DAR DE ALTA UN PRODUCTO
    @Override
    @Transactional
    public ProductDTOResponse activate(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        entity.setActive(true);

        ProductEntity updated = productRepository.save(entity);

        return productMapper.toResponse(updated);
    }

}

