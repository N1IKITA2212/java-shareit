package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorIdOrderByCreatedAsc(Long requestorId);

    @Query("""
            SELECT ir
            FROM ItemRequest AS ir
            WHERE ir.requestor.id <> ?1
            ORDER BY ir.created ASC
            """)
    List<ItemRequest> findAllOthers(Long userId);
}
