package com.smartcare.smartcaredesktop.service;

import com.smartcare.smartcaredesktop.model.User;
import com.smartcare.smartcaredesktop.config.DatabaseConfig;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserService {

    private final Connection connection = DatabaseConfig.getConnection();

    public User login(String email, String password) {

        String sql = """
            SELECT * FROM "user"
            WHERE email = ? AND is_active = true
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String storedHash = rs.getString("password");

                if (!BCrypt.checkpw(password, storedHash)) {
                    return null;
                }

                updateLastLogin(rs.getInt("id"));
                return mapUser(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updatePassword(int userId, String newPassword) {

        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

        String sql = """
            UPDATE "user"
            SET password = ?, is_first_login = false, updated_at = NOW()
            WHERE id = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, hashed);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLastLogin(int userId) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE \"user\" SET last_login_at = NOW() WHERE id = ?")) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (Exception ignored) {}
    }

    private User mapUser(ResultSet rs) throws SQLException {

        return new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("roles"),
                rs.getString("password"),
                rs.getBoolean("is_active"),
                rs.getBoolean("is_first_login"),
                rs.getTimestamp("last_login_at") != null ? rs.getTimestamp("last_login_at").toLocalDateTime() : null,
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                rs.getObject("doctor_id") != null ? rs.getInt("doctor_id") : null,
                rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null
        );
    }
}