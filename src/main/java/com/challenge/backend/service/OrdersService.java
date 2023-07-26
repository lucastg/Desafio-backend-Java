package com.challenge.backend.service;

import com.challenge.backend.dto.orders.request.CreateOrdersRequest;
import com.challenge.backend.dto.orders.request.UpdateItensOrderRequest;
import com.challenge.backend.dto.orders.request.UpdateStatusOrderRequest;
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

    @Transactional
    public Optional<OrderModel> updateItensOrder(UpdateItensOrderRequest updateItensOrderRequest) {

        Optional<OrderModel> orderOptional = orderRepository.findById(updateItensOrderRequest.getId());

        if (orderOptional.isPresent()) {
            OrderModel order = orderOptional.get();
            var quantityItem = this.findRepeatedIds(updateItensOrderRequest);
            var itemsList = order.getItens();

            List<ProductsDto> products = productsRepository.getProducts().collectList().block();

            for (Map<String, Integer> entry : quantityItem) {
                int id = entry.get("id");
                int count = entry.get("count");

                Optional<ItemModel> existingItem = itemsList.stream()
                        .filter(item -> item.getIdProduto().equals((long) id))
                        .findFirst();

                if (existingItem.isPresent()) {
                    ItemModel itemModel = existingItem.get();
                    itemModel.setQuantidade(itemModel.getQuantidade() + count);
                    itemModel.setPrecoParcial(itemModel.getPreco().multiply(BigDecimal.valueOf(itemModel.getQuantidade())));
                } else {
                    // Item não existe na lista, então adicionamos um novo item
                    products.stream()
                            .filter(product -> product.getId() == id)
                            .findFirst()
                            .ifPresent(product -> {
                                BigDecimal pricePartial = product.getPrice().multiply(BigDecimal.valueOf(count));
                                itemsList.add(new ItemModel(product.getId(), product.getPrice(), count, pricePartial));
                            });
                }
            }

            BigDecimal totalPrice = itemsList.stream()
                    .map(ItemModel::getPrecoParcial)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setPrecoTotal(totalPrice);

            orderRepository.save(order);
            return orderOptional;
        }
        return Optional.empty();
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
