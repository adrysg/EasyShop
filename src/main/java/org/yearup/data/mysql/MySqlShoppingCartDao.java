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
import java.util.Map;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        List<ShoppingCartItem> items = new ArrayList<>();

        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    SELECT * FROM shopping_cart
                    WHERE user_id = ?;""")){

            pStatement.setInt(1, userId);
            ResultSet results = pStatement.executeQuery();

            while (results.next()){
                int productId = results.getInt("product_id");
                int quantity = results.getInt("quantity");
                items.add(new ShoppingCartItem(productId, quantity));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ShoppingCart();
    }

    @Override
    public void addItem(int userId, int productId, int quantity) {
        BigDecimal productPrice = null;
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    SELECT price FROM products
                    WHERE product_id = ?;"""))
        {
            pStatement.setInt(1, productId);
            ResultSet results = pStatement.executeQuery();

            if (results.next()){
                productPrice = results.getBigDecimal("price");
            }
            else {
                throw new RuntimeException();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        //adding items into cart
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    INSERT INTO shopping_cart (user_id, product_id, quantity)
                    VALUES (?, ?, ?);"""))
        {
            pStatement.setInt(1, userId);
            pStatement.setInt(2, productId);
            pStatement.setInt(3, quantity);
            pStatement.setBigDecimal(4, productPrice);
            pStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateCart(int userId, int productId, int quantity, BigDecimal discountPercent) {
        //update items in cart
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    UPDATE shopping_cart SET quantity = ?
                    WHERE user_id = ?, AND product_id = ?;"""))
        {
            pStatement.setInt(1, quantity);
            pStatement.setBigDecimal(2, discountPercent);
            pStatement.setInt(2, userId);
            pStatement.setInt(3, productId);
            pStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAllItems(int userId) {

        //delete items from cart
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    DELETE FROM shopping_cart
                    WHERE user_id = ?;"""))
        {
            pStatement.setInt(2, userId);
            pStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

}
