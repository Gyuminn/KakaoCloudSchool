package com.example.batch_web.dto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCCustomerRowMapper implements RowMapper<JDBCCustomerDTO> {
    @Override
    public JDBCCustomerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        JDBCCustomerDTO customer = new JDBCCustomerDTO();
        customer.setId(rs.getLong("id"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setFirstName(rs.getString("firstName"));
        customer.setLastName(rs.getString("lastName"));
        customer.setMiddleInitial(rs.getString("middleInitial"));
        customer.setState(rs.getString("state"));
        customer.setZipCode(rs.getString("zipCode"));

        return customer;
    }
}
