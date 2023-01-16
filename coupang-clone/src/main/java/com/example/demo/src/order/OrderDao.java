package com.example.demo.src.order;

import com.example.demo.src.order.model.CreateOrderCartReq;
import com.example.demo.src.order.model.DeleteOrderCartReq;
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

    public int deleteCart(DeleteOrderCartReq deleteOrderCartReq) {
        String deleteCartQuery = "update Cart set status = 'N' where userIdx =? and productIdx =? and status = 'Y'";
        Object[] deleteCartParam = new Object[] {deleteOrderCartReq.getUserIdx(), deleteOrderCartReq.getProductIdx()};
        return this.jdbcTemplate.update(deleteCartQuery, deleteCartParam);
    }

    public int checkCartExist(int userIdx, int productIdx) {
        String checkCartExistQuery = "select exists(select * from Cart where userIdx =? and productIdx = ? and status = 'Y')";
        Object[] checkCartExistParam =  new Object[]{userIdx, productIdx};
        return this.jdbcTemplate.queryForObject(checkCartExistQuery, int.class, checkCartExistParam);
    }
}
