package com.challenge.backend.service;

import com.challenge.backend.dto.orders.CreateOrdersRequest;
import com.challenge.backend.dto.orders.UpdateItensOrderRequest;
import com.challenge.backend.dto.orders.UpdateStatusOrderRequest;
import com.challenge.backend.dto.products.ProductsDto;
import com.challenge.backend.dto.users.UserDto;
import com.challenge.backend.enums.OrderStatus;
import com.challenge.backend.model.ItemModel;
import com.challenge.backend.model.OrderModel;
import com.challenge.backend.repository.order.OrderRepository;
import com.challenge.backend.repository.product.ProductsRepository;
import com.challenge.backend.repository.user.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class OrdersService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ProductsRepository productsRepository;


    @Transactional
    public Optional<OrderModel> createOrder(CreateOrdersRequest createOrdersRequest) {

        UserDto user = usersRepository.getUserById(createOrdersRequest.getUserId()).block();

        OrderModel newOrder = new OrderModel();
        if (user != null && !createOrdersRequest.getProducts().isEmpty()) {

            var quantityItem = this.findRepeatedIds(createOrdersRequest);
            var itemsList = new ArrayList<ItemModel>();

            List<ProductsDto> products = productsRepository.getProducts().collectList().block();

            products.forEach(productsDto -> {

                for (Map<String, Integer> entry : quantityItem) {

                    int id = entry.get("id");
                    int count = entry.get("count");

                    if (id == productsDto.getId()) {
                        var pricePartial = productsDto.getPrice().multiply(BigDecimal.valueOf(count));

                        if (newOrder.getPrecoTotal() == null) {
                            newOrder.setPrecoTotal(pricePartial);
                        } else {
                            newOrder.setPrecoTotal(newOrder.getPrecoTotal().add(pricePartial));
                        }
                        itemsList.add(new ItemModel(productsDto.getId(), productsDto.getPrice(), count, pricePartial));
                    }
                }
            });

            newOrder.setUserId(user.getId());
            newOrder.setItens(itemsList);
            newOrder.setStatus(OrderStatus.PENDING);
            return Optional.of(this.orderRepository.save(newOrder));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<OrderModel> updateStatusOrder(UpdateStatusOrderRequest updateStatusOrderRequest) {

        Optional<OrderModel> order = orderRepository.findById(updateStatusOrderRequest.getId());

        if (order.isPresent()) {
            order.get().setStatus(OrderStatus.CONCLUDED);
            orderRepository.save(order.get());
            return order;
        }

        return Optional.empty();
    }

    public Optional<OrderModel> updateItensOrder(UpdateItensOrderRequest updateItensOrderRequest) {

        Optional<OrderModel> order = orderRepository.findById(updateItensOrderRequest.getId());

        if (order.isPresent()) {
            var quantityItem = this.findRepeatedIds(updateItensOrderRequest);
            var itemsList = order.get().getItens();

            List<ProductsDto> products = productsRepository.getProducts().collectList().block();

            products.forEach(productsDto -> {

                for (Map<String, Integer> entry : quantityItem) {

                    int id = entry.get("id");
                    int count = entry.get("count");

                    if (id == productsDto.getId()) {
                        var pricePartial = productsDto.getPrice().multiply(BigDecimal.valueOf(count));

                        if (order.get().getPrecoTotal() == null) {
                            order.get().setPrecoTotal(pricePartial);
                        } else {
                            order.get().setPrecoTotal(order.get().getPrecoTotal().add(pricePartial));
                        }
                        itemsList.add(new ItemModel(productsDto.getId(), productsDto.getPrice(), count, pricePartial));
                    }
                }
            });
            order.get().setItens(itemsList);
        }
        return Optional.of(order.get());
    }

    public List<Map<String, Integer>> findRepeatedIds(CreateOrdersRequest createOrdersRequest) {
        List<Map<String, Integer>> repeatedIds = new ArrayList<>();
        Map<Integer, Integer> idCountMap = new HashMap<>();
        var products = createOrdersRequest.products;

        for (var product : products) {
            int id = product.id;
            int count = idCountMap.getOrDefault(id, 0);
            idCountMap.put(id, count + 1);
        }

        for (Map.Entry<Integer, Integer> entry : idCountMap.entrySet()) {
            int id = entry.getKey();
            int count = entry.getValue();
            repeatedIds.add(Map.of("id", id, "count", count));
        }

        return repeatedIds;
    }

    public List<Map<String, Integer>> findRepeatedIds(UpdateItensOrderRequest updateItensOrderRequest) {
        List<Map<String, Integer>> repeatedIds = new ArrayList<>();
        Map<Integer, Integer> idCountMap = new HashMap<>();
        var items = updateItensOrderRequest.items;

        for (var item : items) {
            int id = item.id;
            int count = idCountMap.getOrDefault(id, 0);
            idCountMap.put(id, count + 1);
        }

        for (Map.Entry<Integer, Integer> entry : idCountMap.entrySet()) {
            int id = entry.getKey();
            int count = entry.getValue();
            repeatedIds.add(Map.of("id", id, "count", count));
        }

        return repeatedIds;
    }
}
