package com.skaveesh.geo.geoloadbalanced.service.repository;

import com.skaveesh.geo.geoloadbalanced.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
