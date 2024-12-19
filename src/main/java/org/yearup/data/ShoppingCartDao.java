package org.yearup.data;

import org.yearup.models.ShoppingCart;

import java.math.BigDecimal;


public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    void addItem(int userId, int productId, int quantity);
    void updateCart(int userId, int productId, int quantity);
    void removeAllItems(int userId);
    void updateCart(int userId, int productId, int quantity, BigDecimal discountPercent);


}
