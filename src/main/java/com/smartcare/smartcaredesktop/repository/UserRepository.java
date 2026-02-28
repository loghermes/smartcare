package com.smartcare.smartcaredesktop.repository;

import com.smartcare.smartcaredesktop.config.DatabaseConfig;
import com.smartcare.smartcaredesktop.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class UserRepository {

    private final Connection connection = DatabaseConfig.getConnection();

    // ================= CREATE USER =================
    public int createUser(String email,
                          String username,
                          String password,
                          String role) {

        String sql = """
            INSERT INTO "user"
            (email, username, password, roles,
             is_active, is_first_login,
             created_at, updated_at)
            VALUES (?, ?, ?, ?::json, true, true, NOW(), NOW())
            RETURNING id
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, password);

            // Store role as JSON array
            ps.setString(4, "[\"" + role + "\"]");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ================= FIND BY EMAIL =================
    public User findByEmail(String email) {

        String sql = "SELECT * FROM \"user\" WHERE email = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Timestamp lastLoginTs = rs.getTimestamp("last_login_at");
                Timestamp createdTs = rs.getTimestamp("created_at");
                Timestamp updatedTs = rs.getTimestamp("updated_at");

                return new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("username"),
                        rs.getString("roles"),
                        rs.getString("password"),
                        rs.getBoolean("is_active"),
                        rs.getBoolean("is_first_login"),
                        lastLoginTs != null ? lastLoginTs.toLocalDateTime() : null,
                        createdTs != null ? createdTs.toLocalDateTime() : null,
                        updatedTs != null ? updatedTs.toLocalDateTime() : null,
                        (Integer) rs.getObject("doctor_id"),
                        (Integer) rs.getObject("patient_id")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}