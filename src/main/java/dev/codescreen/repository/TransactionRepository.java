package dev.codescreen.repository;
import dev.codescreen.model.Transaction;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT COALESCE(SUM(CASE WHEN t.debitOrCredit = 'CREDIT' THEN t.amount ELSE -t.amount END), 0) FROM Transaction t WHERE t.userId = :userId")
    BigDecimal findBalanceByUserId(@Param("userId") String userId);


}