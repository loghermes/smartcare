package com.smartcare.smartcaredesktop.service;

import com.smartcare.smartcaredesktop.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardService {

    private final Connection connection = DatabaseConfig.getConnection();

    public int countDoctors() {
        return count("doctor");
    }

    public int countPatients() {
        return count("patient");
    }

    public int countAppointments() {
        return count("appointment");
    }

    private int count(String table) {

        // whitelist protection (security + stability)
        if (!table.equals("doctor") &&
                !table.equals("patient") &&
                !table.equals("appointment")) {
            return 0;
        }

        String sql = "SELECT COUNT(*) FROM " + table;

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}