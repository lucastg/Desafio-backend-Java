package com.challenge.backend.mapper;

import com.challenge.backend.dto.orders.Itens;
import com.challenge.backend.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<Itens> toResponse(List<ItemModel> itemModel) {
        var itensList = new ArrayList<Itens>();
        itemModel.forEach(item -> {
            var newItem = new Itens(item.getIdProduto(), item.getPreco(), item.getQuantidade(), item.getPrecoParcial());
            itensList.add(newItem);
        });
        return itensList;
    }
}
