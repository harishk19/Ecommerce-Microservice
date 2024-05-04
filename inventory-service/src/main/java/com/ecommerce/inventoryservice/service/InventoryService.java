package com.ecommerce.inventoryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.inventoryservice.dto.InventoryResponse;
import com.ecommerce.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;
	
	//OLD
//	public boolean isInStock(String skuCode) {
//		
//		return inventoryRepository.findBySkuCode(skuCode).isPresent();
//		
//	}
	
	//NEW
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode) {
		
		return inventoryRepository.findBySkuCodeIn(skuCode).stream()
				.map(inventory -> InventoryResponse.builder()
					.skuCode(inventory.getSkuCode())
					.isInStock(inventory.getQuantity()>0)
					.build()
				).toList();
		
	}


}
