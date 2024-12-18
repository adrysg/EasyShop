package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

// convert this class to a REST controller
@RestController
// only logged in users should have access to these actions
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProductDao productDao;



    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    public Map<String, Object> getCart(Principal principal)
    {
        try {
            User user = validateUser(principal);
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            int userId = user.getId();
            // use the ShoppingCartDao to get all items in the cart and return the cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            Map<String, Object> cartItems = new HashMap<>();
            cartItems.put("items", cart.getItems());
            cartItems.put("total", cart.getTotal());
            return cartItems;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{productId}")
    public void addToCart(@PathVariable int productId, Principal principal){
        try {
            User user = validateUser(principal);
            int userId = user.getId();
            int quantity = 1;

            shoppingCartDao.addItem(userId, productId, quantity);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{productId}")
    public void updateCart(@PathVariable int productId, @RequestBody ShoppingCartItem shoppingCartItem, Principal principal) {
        try {
            User user = validateUser(principal);
            int userId = user.getId();


            shoppingCartDao.updateCart(userId, productId, shoppingCartItem.getQuantity(), shoppingCartItem.getDiscountPercent());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping("/cart")
    public void removeItems(Principal principal){
        try {
            User user = validateUser(principal);
            int userId = user.getId();

            shoppingCartDao.removeAllItems(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    //helper method to validate user is logged in
    private User validateUser (Principal principal){
        if (principal == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String username = principal.getName();
        User user = userDao.getByUserName(username);
        if (user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return userDao.getByUserName(username);
    }
}
