package com.symplora.repository;

import com.symplora.model.LeaveTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTransactionRepository extends JpaRepository<LeaveTransaction, Long> {
}
