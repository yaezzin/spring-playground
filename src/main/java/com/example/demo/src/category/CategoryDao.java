package com.example.demo.src.category;

import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.src.category.model.PostCategoryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createCategory(PostCategoryReq postCategoryReq) {
        String createCategoryQuery = "insert into Category(name) values(?)";
        Object[] createCategoryParams = new Object[]{ postCategoryReq.getName() };
        this.jdbcTemplate.update(createCategoryQuery, createCategoryParams);

        String lastProductIdxQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastProductIdxQuery, int.class);
    }

    public List<GetCategoryRes> getCategories() {
        String getCategoryQuery = "select categoryIdx, name from Category";
        return this.jdbcTemplate.query(getCategoryQuery,
                (rs, rowNum) -> new GetCategoryRes(
                        rs.getInt("categoryIdx"),
                        rs.getString("name")
                )
        );
    }
}
