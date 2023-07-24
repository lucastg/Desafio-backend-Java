package com.challenge.backend.service;

import com.challenge.backend.dto.orders.CreateOrdersRequest;
import com.challenge.backend.dto.products.ProductsDto;
import com.challenge.backend.dto.users.UserDto;
import com.challenge.backend.enums.OrderStatus;
import com.challenge.backend.model.ItemModel;
import com.challenge.backend.model.OrderModel;
import com.challenge.backend.repository.ProductsRepository;
import com.challenge.backend.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class OrdersService {

    private UsersRepository usersRepository;
    private ProductsRepository productsRepository;

    @Transactional
    public Optional<OrderModel> createOrder(CreateOrdersRequest createOrdersRequest) {

        UserDto user = usersRepository.getUserById(createOrdersRequest.getUserId());

        OrderModel newOrder = new OrderModel();
        if (user != null && !createOrdersRequest.getProducts().isEmpty()) {
            var quantityItem = this.findRepeatedIds(createOrdersRequest);
            var itemsList = new ArrayList<ItemModel>();

            List<ProductsDto> products = productsRepository.getProducts();
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
        }
        return Optional.of(newOrder);
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

    public Optional<OrderModel> updateStatusOrder(OrderStatus orderStatus, CreateOrdersRequest createOrdersRequest){

        UserDto user = usersRepository.getUserById(createOrdersRequest.getUserId());
        OrderModel newOrder = new OrderModel();
        newOrder.setUserId(user.getId());
        newOrder.setStatus(orderStatus);


        return Optional.empty();
    }
}
