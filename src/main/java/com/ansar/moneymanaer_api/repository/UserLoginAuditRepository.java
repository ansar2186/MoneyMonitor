package com.ansar.moneymanaer_api.repository;

import com.ansar.moneymanaer_api.entity.UserLoginAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginAuditRepository extends JpaRepository<UserLoginAudit,Long> {
}
