package com.ecommerce.productservice.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.ecommerce.productservice.dto.ProductRequest;
import com.ecommerce.productservice.dto.ProductResponse;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	
	private final ProductRepository productRepository;
	
	
	public void createProduct(ProductRequest productRequest) {	
		
		Product product = Product.builder()
				.name(productRequest.getName())	
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		
		productRepository.save(product);
		//log.info("Product" + product.getId() + "is saved");  instead of this using Log4j
		log.info("Product {} is Saved", product.getId());
	}
	
	
		
		public List<ProductResponse> getAllProducts(){
			List<Product> products = productRepository.findAll();
			return products.stream().map(product -> mapToProductResponse(product)).toList();
			//return products.stream().map(this::mapToProductResponse).toList();
		}
		
		
		
		private ProductResponse mapToProductResponse(Product product) {
			return ProductResponse.builder()
					.Id(product.getId())
					.name(product.getName())
					.description(product.getDescription())
					.price(product.getPrice())
					.build();
		}
		
		
		
	    public ProductResponse getProductById(String id) {
	        Product product = productRepository.findById(id)
	            .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));

	        return mapToProductResponse(product);	
	    }
		
	

}
