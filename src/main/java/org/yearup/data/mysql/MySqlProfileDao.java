package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Category;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {
    public MySqlProfileDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getByUserId(int userId) {
        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement("""
                     SELECT * FROM profiles
                     WHERE user_id = ?;""")) {
            pStatement.setInt(1, userId);
            ResultSet results = pStatement.executeQuery();
            {
                if (results.next()) {
                    return new Profile(
                            results.getInt("user_id"),
                            results.getString("first_name"),
                            results.getString("last_name"),
                            results.getString("phone"),
                            results.getString("email"),
                            results.getString("address"),
                            results.getString("city"),
                            results.getString("state"),
                            results.getString("zip"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateProfile(Profile profile) {
        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement("""
                     UPDATE profiles SET first_name = ?, last_name = ?, phone = ?,
                     email = ?, address = ?, city = ?, state = ?, zip = ?
                     WHERE user_id = ?;"""))
                {

                    pStatement.setInt(1, profile.getUserId());
                    pStatement.setString(2, profile.getFirstName());
                    pStatement.setString(3, profile.getLastName());
                    pStatement.setString(4, profile.getPhone());
                    pStatement.setString(5, profile.getEmail());
                    pStatement.setString(6, profile.getAddress());
                    pStatement.setString(7, profile.getCity());
                    pStatement.setString(8, profile.getState());
                    pStatement.setString(9, profile.getZip());

                    pStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
