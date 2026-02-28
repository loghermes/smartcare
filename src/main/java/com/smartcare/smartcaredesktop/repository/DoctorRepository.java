package com.smartcare.smartcaredesktop.repository;

import com.smartcare.smartcaredesktop.config.DatabaseConfig;
import com.smartcare.smartcaredesktop.model.Doctor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorRepository {

    private final Connection connection = DatabaseConfig.getConnection();

    // ================= INSERT =================
    public int insertDoctor(int userId,
                            String firstName,
                            String lastName,
                            String slug,
                            String gender,
                            String profilePhoto,
                            String specialization,
                            String licenseNumber,
                            String phone,
                            int yearsExperience,
                            String bio,
                            boolean isAvailable) {

        String sql = """
        INSERT INTO doctor
        (user_id, first_name, last_name, slug, gender,
         profile_photo, specialization, license_number,
         phone, years_experience, bio, is_available,
         created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
        RETURNING id
    """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, slug);
            ps.setString(5, gender);
            ps.setString(6, profilePhoto);
            ps.setString(7, specialization);
            ps.setString(8, licenseNumber);
            ps.setString(9, phone);
            ps.setInt(10, yearsExperience);
            ps.setString(11, bio);
            ps.setBoolean(12, isAvailable);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // ================= FIND ALL =================
    public List<Doctor> findAll() {

        List<Doctor> doctors = new ArrayList<>();

        String sql = "SELECT * FROM doctor ORDER BY id DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Timestamp createdTs = rs.getTimestamp("created_at");
                Timestamp updatedTs = rs.getTimestamp("updated_at");

                LocalDateTime createdAt = createdTs != null ? createdTs.toLocalDateTime() : null;
                LocalDateTime updatedAt = updatedTs != null ? updatedTs.toLocalDateTime() : null;

                Doctor doctor = new Doctor(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("slug"),
                        rs.getString("gender"),
                        rs.getString("profile_photo"),
                        rs.getString("specialization"),
                        rs.getString("license_number"),
                        rs.getString("phone"),
                        rs.getInt("years_experience"),
                        rs.getString("bio"),
                        rs.getBoolean("is_available"),
                        createdAt,
                        updatedAt
                );

                doctors.add(doctor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return doctors;
    }

    // ================= DELETE =================
    public void deleteDoctor(int id) {

        String sql = "DELETE FROM doctor WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}