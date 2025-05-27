package com.interswitch.user_management.repository;

import com.interswitch.user_management.model.entity.Biller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillerRepository extends JpaRepository<Biller, Long> {

     Biller findByBillerId (String billerId);
}
