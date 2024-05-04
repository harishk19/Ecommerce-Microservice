package com.ecommerce.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.ecommerce.orderservice.dto.InventoryResponse;
import com.ecommerce.orderservice.dto.OrderLineItemsDto;
import com.ecommerce.orderservice.dto.OrderRequest;
import com.ecommerce.orderservice.model.Order;
import com.ecommerce.orderservice.model.OrderLineItems;
import com.ecommerce.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	
	private final OrderRepository orderRepository;
	
	//Invoking the webclient from Config
	private final WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
	
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
				.map(this::mapToDto).toList();
		
		order.setOrderLineItemsList(orderLineItems);
		//or
		//orderRequest.getOrderListItemsDto().stream().map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList(); 
		
//		List<String> skuCodes = order.getOrderLineItemsList().stream()
//					.map(OrderLineItems::getSkuCode)
//					.toList();
		//or
		
		//Getting all the SKU codes as a list
		List<String> skuCodes  = order.getOrderLineItemsList().stream()
				.map(orderLineItem -> orderLineItem.getSkuCode()).toList();
		
		//Call Inventory Service and place the order if product is in Stock
		
		
		//Building the uri to get the sku codes as an array.
		InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get().uri("http://inventory-service/api/inventory", 
				uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
		.retrieve().bodyToMono(InventoryResponse[].class)
		.block();
		
		
		boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
		.allMatch(InventoryResponse::isInStock);
		
		
		if(allProductsInStock) {
		orderRepository.save(order);
		}
		else {
			throw new IllegalArgumentException("Product is not in stock, please try again Later");
		}
		
		
		//done checking the stock 
		
	}

	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
			OrderLineItems orderLineItems = new OrderLineItems();
			orderLineItems.setPrice(orderLineItemsDto.getPrice());
			orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
			orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
			
			return orderLineItems;

	}
	
	public List<Order> getAllOrders() {
	    return orderRepository.findAll();
	}

	
	
	
	
	
}
