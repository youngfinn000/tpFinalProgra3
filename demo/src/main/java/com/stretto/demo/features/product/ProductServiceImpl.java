package com.stretto.demo.features.product;

import com.stretto.demo.features.flavors.FlavorsRepository;
import com.stretto.demo.features.flavors.domain.FlavorsEntity;
import com.stretto.demo.features.product.domain.ProductEntity;
import com.stretto.demo.features.product.domain.dto.ProductDTORequest;
import com.stretto.demo.features.product.domain.dto.ProductDTOResponse;
import com.stretto.demo.features.product.domain.mapper.ProductMapper;
import com.stretto.demo.features.stock.StockEntity;
import com.stretto.demo.features.stock.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final FlavorsRepository flavorsRepository;
    private final StockRepository stockRepository;

    @Override
    public ProductDTOResponse create(ProductDTORequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new RuntimeException("A product with that name already exists");
        }

        StockEntity stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + request.getStockId()));

        List<FlavorsEntity> flavors = flavorsRepository.findAllById(request.getFlavorIds());

        ProductEntity entity = ProductMapper.toEntity(request, stock, flavors);
        entity.setActive(true);  // siempre arranca activo

        ProductEntity saved = productRepository.save(entity);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public List<ProductDTOResponse> findAll() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductDTOResponse findById(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return ProductMapper.toResponse(entity);
    }

    @Override
    public ProductDTOResponse update(Long id, ProductDTORequest request) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        StockEntity stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new RuntimeException("Stock not found with id: " + request.getStockId()));

        List<FlavorsEntity> flavors = flavorsRepository.findAllById(request.getFlavorIds());

        entity.setName(request.getName());
        entity.setPrice(request.getPrice());
        entity.setStock(stock);
        entity.setFlavors(flavors);

        ProductEntity updated = productRepository.save(entity);
        return ProductMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        entity.setActive(false);
        productRepository.save(entity);
    }

    @Override
    public ProductDTOResponse activate(Long id) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        entity.setActive(true);
        ProductEntity updated = productRepository.save(entity);
        return ProductMapper.toResponse(updated);
    }

    @Override
    public List<ProductDTOResponse> findByFlavor(Long flavorId) {
        return productRepository.findByFlavors_Id(flavorId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }
}

