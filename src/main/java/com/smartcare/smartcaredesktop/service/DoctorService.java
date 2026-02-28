package com.smartcare.smartcaredesktop.service;

import com.smartcare.smartcaredesktop.config.DatabaseConfig;
import com.smartcare.smartcaredesktop.model.Doctor;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {

    private final Connection connection = DatabaseConfig.getConnection();

    /* =========================
       CREATE DOCTOR
    ========================== */
    public void createDoctor(String email,
                             String username,
                             String rawPassword,
                             String firstName,
                             String lastName,
                             String gender,
                             String profilePhoto,
                             String specialization,
                             String license,
                             String phone,
                             int experience,
                             String bio,
                             String slug) {

        try {

            connection.setAutoCommit(false);

            String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));

            // 1️⃣ INSERT USER
            String userSql = """
                INSERT INTO "user"
                (email, username, password, roles, is_active, is_first_login, created_at, updated_at)
                VALUES (?, ?, ?, ?::jsonb, ?, ?, NOW(), NOW())
                RETURNING id
            """;

            PreparedStatement userStmt = connection.prepareStatement(userSql);

            userStmt.setString(1, email);
            userStmt.setString(2, username);
            userStmt.setString(3, hashedPassword);
            userStmt.setString(4, "[\"ROLE_DOCTOR\"]");
            userStmt.setBoolean(5, true);
            userStmt.setBoolean(6, true);

            ResultSet userRs = userStmt.executeQuery();
            userRs.next();
            int userId = userRs.getInt("id");

            // 2️⃣ INSERT DOCTOR (FIXED WITH is_available)
            String doctorSql = """
                INSERT INTO doctor
                (first_name, last_name, slug, gender, profile_photo,
                 specialization, license_number, phone,
                 years_experience, bio, is_available,
                 user_id, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
            """;

            PreparedStatement doctorStmt = connection.prepareStatement(doctorSql);

            doctorStmt.setString(1, firstName);
            doctorStmt.setString(2, lastName);
            doctorStmt.setString(3, slug);
            doctorStmt.setString(4, gender);
            doctorStmt.setString(5, profilePhoto);
            doctorStmt.setString(6, specialization);
            doctorStmt.setString(7, license);
            doctorStmt.setString(8, phone);
            doctorStmt.setInt(9, experience);
            doctorStmt.setString(10, bio);
            doctorStmt.setBoolean(11, true); // ✅ required by Symfony entity
            doctorStmt.setInt(12, userId);

            doctorStmt.executeUpdate();

            connection.commit();

        } catch (Exception e) {
            try { connection.rollback(); } catch (Exception ignored) {}
            e.printStackTrace();
        }
    }

    /* =========================
       READ ALL DOCTORS
    ========================== */
    public List<Doctor> findAll() {

        List<Doctor> doctors = new ArrayList<>();

        String sql = "SELECT * FROM doctor ORDER BY id DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Doctor d = new Doctor();

                d.setId(rs.getInt("id"));
                d.setFirstName(rs.getString("first_name"));
                d.setLastName(rs.getString("last_name"));
                d.setSlug(rs.getString("slug"));
                d.setGender(rs.getString("gender"));
                d.setProfilePhoto(rs.getString("profile_photo"));
                d.setSpecialization(rs.getString("specialization"));
                d.setLicenseNumber(rs.getString("license_number"));
                d.setPhone(rs.getString("phone"));
                d.setYearsExperience(rs.getInt("years_experience"));
                d.setBio(rs.getString("bio"));

                doctors.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return doctors;
    }

    /* =========================
       UPDATE DOCTOR
    ========================== */
    public void updateDoctor(Doctor doctor) {

        String sql = """
            UPDATE doctor SET
                first_name = ?,
                last_name = ?,
                slug = ?,
                specialization = ?,
                phone = ?,
                years_experience = ?,
                bio = ?,
                updated_at = NOW()
            WHERE id = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, doctor.getFirstName());
            ps.setString(2, doctor.getLastName());
            ps.setString(3, doctor.getSlug());
            ps.setString(4, doctor.getSpecialization());
            ps.setString(5, doctor.getPhone());
            ps.setInt(6, doctor.getYearsExperience());
            ps.setString(7, doctor.getBio());
            ps.setInt(8, doctor.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* =========================
       DELETE DOCTOR
    ========================== */
    public void deleteDoctor(int doctorId) {

        try {

            connection.setAutoCommit(false);

            String findUserSql = "SELECT user_id FROM doctor WHERE id = ?";
            PreparedStatement findStmt = connection.prepareStatement(findUserSql);
            findStmt.setInt(1, doctorId);
            ResultSet rs = findStmt.executeQuery();

            int userId = -1;
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }

            PreparedStatement deleteDoctorStmt =
                    connection.prepareStatement("DELETE FROM doctor WHERE id = ?");
            deleteDoctorStmt.setInt(1, doctorId);
            deleteDoctorStmt.executeUpdate();

            if (userId != -1) {
                PreparedStatement deleteUserStmt =
                        connection.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
                deleteUserStmt.setInt(1, userId);
                deleteUserStmt.executeUpdate();
            }

            connection.commit();

        } catch (Exception e) {
            try { connection.rollback(); } catch (Exception ignored) {}
            e.printStackTrace();
        }
    }
}