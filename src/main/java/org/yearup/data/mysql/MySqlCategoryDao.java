package org.yearup.data.mysql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
                List<Category> categories = new ArrayList<>();

                try(Connection connection = getConnection();
                    PreparedStatement pStatement = connection.prepareStatement("""
                    SELECT * FROM categories;""");
                    ResultSet results = pStatement.executeQuery())
                {
                    while (results.next()){
                        int categoryId = results.getInt(1);
                        String categoryName = results.getString("name");
                        String description = results.getString("description");
                        categories.add(new Category(categoryId, categoryName, description));
                    }
                } catch (SQLException e){
                    throw new RuntimeException(e);
                }
                return categories;

    }

    @Override
    public Category getById(int categoryId)
    {
        // get category by id
        Category category = null;
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    SELECT *
                    FROM categories
                    WHERE category_id = ?;""");
            ResultSet results = pStatement.executeQuery())
        {
            while (results.next()){
                int id = results.getInt(1);
                String categoryName = results.getString("name");
                String description = results.getString("description");
                category = (new Category(id, categoryName, description));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public Category create(Category category)
    {
        // create a new category
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    INSERT INTO categories
                    (name, description)
                    VALUES (?, ?);"""))
        {
           pStatement.setString(1, category.getName());
           pStatement.setString(2, category.getDescription());
           pStatement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        // update category
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    UPDATE categories
                    SET name = ?, description = ?
                    WHERE category_id = ?;"""))
        {
            pStatement.setString(1, category.getName());
            pStatement.setString(2, category.getDescription());
            pStatement.setInt(3, categoryId);
            pStatement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int categoryId)
    {
        // delete category
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement("""
                    DELETE FROM categories
                    WHERE category_id = ?;"""))
        {
           pStatement.setInt(1, categoryId);
           pStatement.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
