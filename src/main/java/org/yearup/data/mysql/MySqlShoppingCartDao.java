package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();


        try(Connection connection = getConnection();
         PreparedStatement pStatement = connection.prepareStatement("""
                    SELECT * FROM shopping_cart
                    WHERE user_id = ?;""")){
            pStatement.setInt(1, userId);
            ResultSet results = pStatement.executeQuery();
            while (results.next()){
                ShoppingCartItem items = new ShoppingCartItem();
                Product product = new Product();
                product.setProductId(results.getInt("product_id"));
                items.setProduct(product);
                items.setQuantity(results.getInt("quantity"));
                shoppingCart.add(items);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    @Override
    public void addItem(int userId, int productId) {

    }

    @Override
    public void updateCart(int userId, int productId, int quantity, BigDecimal discountPercent) {

    }

    @Override
    public void removeAllItems(int userId) {

    }

}
