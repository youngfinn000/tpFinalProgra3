package com.stretto.demo.features.product;

import com.stretto.demo.common.exception.AlreadyExistsException;
import com.stretto.demo.common.exception.InvalidStateException;
import com.stretto.demo.common.exception.NotFoundException;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.product.domain.dto.ProductDTORequest;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import com.stretto.demo.features.product.domain.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
            throw new AlreadyExistsException("A product with that name already exists");
        }

        ProductEntity entity = productMapper.toEntity(request);

        entity.setActive(true);

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
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(entity);
    }

    //ACTUALIZAR PRODUCTO
    @Override
    @Transactional
    public ProductDTOResponse update(Long id, ProductDTORequest request)
    {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        if (!entity.getName().equalsIgnoreCase(request.getName()) &&
                productRepository.existsByNameIgnoreCase(request.getName())) {

            throw new AlreadyExistsException("A product with that name already exists");
        }

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
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        entity.setActive(false);

        productRepository.save(entity);
    }

    //DAR DE ALTA UN PRODUCTO
    @Override
    @Transactional
    public ProductDTOResponse activate(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        if(entity.isActive()){
            throw new InvalidStateException("Product is already active");
        }

        entity.setActive(true);

        ProductEntity updated = productRepository.save(entity);

        return productMapper.toResponse(updated);
    }

    //BUSCAR PRODUCTO POR NOMBRE
    @Override
    public List<ProductDTOResponse> searchByName (String name)
    {
        return  productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    //STOCK BAJO
    @Override
    public List<ProductDTOResponse> getLowStock (Integer limit)
    {
        return productRepository.findByStockLessThanEqual(limit)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    //STOCK DISPONIBLE
    @Override
    public List<ProductDTOResponse> getAvailable()
    {
        return productRepository.findByStockGreaterThan(0)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    //ACTUALIZAR SOLO PRECIO
    @Override
    @Transactional
    public ProductDTOResponse updatePrice (Long id, BigDecimal price)
    {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product not found with id: " + id));

        entity.setPrice(price);

        return productMapper.toResponse(productRepository.save(entity));
    }
}

