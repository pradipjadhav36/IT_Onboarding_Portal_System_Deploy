package in.sp.pro.repository;

import java.util.Optional;

import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties.Apiversion.Use;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.pro.entities.AssetAllocation;
import in.sp.pro.entities.User;

@Repository
public interface AssetAllocationRepository
        extends JpaRepository<AssetAllocation, Long> {

    Optional<AssetAllocation> findByEmployeeName(String employeeName);

   
}