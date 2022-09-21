package com.example.demo.src.order;

import com.example.demo.src.order.model.CreateOrderCartReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class OrderDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createCart(CreateOrderCartReq createOrderCartReq) {
        String createCartQuery = "insert into Cart(userIdx, productIdx) values(?, ?);";
        Object[] createCartParams = new Object[] {createOrderCartReq.getUserIdx(), createOrderCartReq.getProductIdx()};
        return this.jdbcTemplate.update(createCartQuery, createCartParams);
    }
}
