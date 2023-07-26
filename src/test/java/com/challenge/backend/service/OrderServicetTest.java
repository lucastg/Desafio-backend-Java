package com.challenge.backend.service;

import com.challenge.backend.dto.orders.request.CreateOrdersRequest;
import com.challenge.backend.dto.orders.request.UpdateItensOrderRequest;
import com.challenge.backend.dto.orders.request.UpdateStatusOrderRequest;
import com.challenge.backend.dto.products.ProductsDto;
import com.challenge.backend.dto.users.UserDto;
import com.challenge.backend.enums.OrderStatus;
import com.challenge.backend.model.ItemModel;
import com.challenge.backend.model.OrderModel;
import com.challenge.backend.repository.OrderRepository;
import com.challenge.backend.repository.impl.ProductsRepository;
import com.challenge.backend.repository.impl.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class OrderServicetTest {

    @InjectMocks
    OrdersService ordersService = new OrdersServiceImpl();

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UsersRepository usersRepository;

    @MockBean
    private ProductsRepository productsRepository;
    private List<ProductsDto> productsList;
    private UserDto user;

    @BeforeEach
    public void setUp() {

        user = new UserDto(2L);
        lenient().when(usersRepository.getUserById(any(Long.class))).thenReturn(Mono.just(user));

        productsList = Arrays.asList(
                new ProductsDto(1L, new BigDecimal("109.95")),
                new ProductsDto(2L, new BigDecimal("22.30"))
        );

        lenient().when(productsRepository.getProducts()).thenReturn(Flux.fromIterable(productsList));
    }

    @Test
    void createOrder() {
        //Arange
        CreateOrdersRequest request = new CreateOrdersRequest();
        request.setUserId(user.getId());
        List<CreateOrdersRequest.ProductsRequest> products = Arrays.asList(
                new CreateOrdersRequest.ProductsRequest(2),
                new CreateOrdersRequest.ProductsRequest(1)
        );
        request.setProducts(products);

        OrderModel newOrder = new OrderModel();
        newOrder.setId(UUID.randomUUID());
        newOrder.setUserId(request.getUserId());
        newOrder.setStatus(OrderStatus.PENDING);
        List<ItemModel> items = new ArrayList<>();
        productsList.forEach(product -> {
            items.add(new ItemModel(product.getId(), product.getPrice(), 1, product.getPrice()));
        });
        BigDecimal precoTotal = items.stream()
                .map(ItemModel::getPrecoParcial)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        newOrder.setItens(items);
        newOrder.setPrecoTotal(precoTotal);
        when(orderRepository.save(any(OrderModel.class))).thenReturn(newOrder);

        // Act
        Optional<OrderModel> createdOrderOptional = ordersService.createOrder(request);

        // Assert
        assertTrue(createdOrderOptional.isPresent());
        OrderModel createdOrder = createdOrderOptional.get();
        assertNotNull(createdOrder.getId());
        assertEquals(user.getId(), createdOrder.getUserId());
        assertEquals(OrderStatus.PENDING, createdOrder.getStatus());
        assertEquals(precoTotal, createdOrder.getPrecoTotal());
        assertEquals(items.size(), createdOrder.getItens().size());
    }

    @Test
    void updateStatusOrder() {
        // Configuração do mock do OrderRepository para o método findById
        OrderModel existingOrder = new OrderModel();
        existingOrder.setId(UUID.randomUUID());
        existingOrder.setUserId(user.getId());
        existingOrder.setStatus(OrderStatus.PENDING);
        existingOrder.setPrecoTotal(new BigDecimal("242.20"));
        existingOrder.setItens(new ArrayList<>());
        var existingOrderOptional = Optional.of(existingOrder);

        when(orderRepository.findById(existingOrder.getId())).thenReturn(existingOrderOptional);

        UpdateStatusOrderRequest updateStatusOrderRequest = new UpdateStatusOrderRequest();
        updateStatusOrderRequest.setId(existingOrder.getId());

        // Executa o método a ser testado
        Optional<OrderModel> updatedOrder = ordersService.updateStatusOrder(updateStatusOrderRequest);

        // Verifica o resultado
        assertTrue(updatedOrder.isPresent());
        assertEquals(OrderStatus.CONCLUDED, updatedOrder.get().getStatus());
    }

    @Test
    void updateItensOrder() {
        //Arange
        List<ProductsDto> productsListUpdated = Arrays.asList(
                new ProductsDto(1L, new BigDecimal("109.95")),
                new ProductsDto(2L, new BigDecimal("22.30")),
                new ProductsDto(3L, new BigDecimal("30.00"))
        );
        when(productsRepository.getProducts()).thenReturn(Flux.fromIterable(productsListUpdated));

        OrderModel existingOrder = new OrderModel();
        existingOrder.setId(UUID.randomUUID());
        existingOrder.setUserId(user.getId());
        existingOrder.setStatus(OrderStatus.CONCLUDED);
        List<ItemModel> items = new ArrayList<>();
        productsList.forEach(product -> {
            items.add(new ItemModel(product.getId(), product.getPrice(), 1, product.getPrice()));
        });
        existingOrder.setItens(items);
        when(orderRepository.findById(existingOrder.getId())).thenReturn(Optional.of(existingOrder));

        UpdateItensOrderRequest updateRequest = new UpdateItensOrderRequest();
        updateRequest.setId(existingOrder.getId());
        updateRequest.setUserId(user.getId());
        UpdateItensOrderRequest.UpdateItem newItem = new UpdateItensOrderRequest.UpdateItem(3, new BigDecimal("30.00"));
        updateRequest.setItems(new ArrayList<>());
        updateRequest.getItems().add(newItem);

        OrderModel retunedOrder = new OrderModel();
        retunedOrder.setId(existingOrder.getId());
        retunedOrder.setUserId(existingOrder.getUserId());
        retunedOrder.setStatus(existingOrder.getStatus());
        List<ItemModel> expectedOrderItems = new ArrayList<>();
        productsList.forEach(product -> {
            expectedOrderItems.add(new ItemModel(product.getId(), product.getPrice(), 1, product.getPrice()));
        });
        expectedOrderItems.add(new ItemModel((long) newItem.getId(), newItem.getPrice(), 1, newItem.getPrice()));
        retunedOrder.setItens(expectedOrderItems);

        BigDecimal precoTotal = expectedOrderItems.stream()
                .map(ItemModel::getPrecoParcial)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Act
        Optional<OrderModel> orderModelOptional = ordersService.updateItensOrder(updateRequest);

        // Assert
        assertTrue(orderModelOptional.isPresent());
        var orderModel = orderModelOptional.get();
        assertEquals(orderModel.getId(), existingOrder.getId());
        assertEquals(orderModel.getUserId(), existingOrder.getUserId());
        assertEquals(orderModel.getStatus(), existingOrder.getStatus());
        assertEquals(orderModel.getItens(), expectedOrderItems);
        assertEquals(orderModel.getPrecoTotal(), precoTotal);

    }

}
