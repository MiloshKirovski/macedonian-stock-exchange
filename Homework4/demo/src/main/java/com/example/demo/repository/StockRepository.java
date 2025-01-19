package com.example.demo.repository;

import com.example.demo.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, String> {
    List<Stock> findByCompanyNameAndDate(String companyName, String date);

    @Query("SELECT s FROM Stock s WHERE s.companyName = :companyName AND s.date_new BETWEEN :dateFrom AND :dateTo ORDER BY s.date_new")
    List<Stock> findByCompanyNameAndDateRange(
            @Param("companyName") String companyName,
            @Param("dateFrom") Date dateFrom,
            @Param("dateTo") Date dateTo
    );
}
