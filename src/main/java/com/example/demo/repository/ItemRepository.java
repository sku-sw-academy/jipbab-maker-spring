package com.example.demo.repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByItemCode(int code);
    Item findByItemName(String name);

    @Query(value = "SELECT item_name FROM Item WHERE category_code = :code", nativeQuery = true)
    List<String> findItemNamesByCategoryCode(@Param("code") int code);
}
