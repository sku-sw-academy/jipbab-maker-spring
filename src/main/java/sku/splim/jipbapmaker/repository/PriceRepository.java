package sku.splim.jipbapmaker.repository;

import sku.splim.jipbapmaker.domain.Price;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    Price findById(String id);
    Price findFirstByNameOrderByIdDesc(String name);
    List<Price> findByRegday(String regday);

    @Transactional
    void deleteAllByRegday(String regday);

    @Modifying
    @Query("DELETE FROM Price p WHERE p.regday <= :thresholdDate")
    void deleteAllByRegdayBefore(@Param("thresholdDate") String regday);

    @Query(value = "SELECT DISTINCT kind_name FROM Price WHERE item_code = :itemCode", nativeQuery = true)
    List<String> findDistinctKindNamesByItemCode(@Param("itemCode") int itemCode);

    @Query(value = "select distinct rank_name from price where kind_name = :kindName", nativeQuery = true)
    List<String> findDistinctRankNamesByKindName(@Param("kindName") String kind);

    @Query(value = "SELECT * FROM price WHERE regday = :regday ORDER BY value", nativeQuery = true)
    List<Price> findByRegdayOrderByValue(@Param("regday") String regday);

    @Query(value = "SELECT * FROM price WHERE regday = :regday ORDER BY value limit 3", nativeQuery = true)
    List<Price> findFirst3ByRegdayOrderByValue(@Param("regday") String regday);

}

