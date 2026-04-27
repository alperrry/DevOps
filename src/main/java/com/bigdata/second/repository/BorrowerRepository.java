package com.bigdata.second.repository;

import com.bigdata.second.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    // Aktif ödünç var mı kontrolleri
    boolean existsByMemberIdAndReturnDateIsNull(Long memberId);
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);

    // Üyenin kaç aktif ödüncü var (limit kontrolü için)
    long countByMemberIdAndReturnDateIsNull(Long memberId);

    // Silme işlemleri için
    void deleteByMemberId(Long memberId);
    void deleteByBookId(Long bookId);
}
