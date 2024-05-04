package com.ecommerce.inventoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.inventoryservice.model.Inventory;

public interface InventoryRepository  extends JpaRepository<Inventory, Long>{



	 //OLD  Optional<Inventory> findBySkuCode(String skuCode);

	List<Inventory> findBySkuCodeIn(List<String> skuCode);
	 

}
