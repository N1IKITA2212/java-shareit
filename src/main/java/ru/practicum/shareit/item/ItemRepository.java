package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = """
            SELECT it
            FROM Item as it
            WHERE it.isAvailable = true AND
            (
            LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%')) OR
            LOWER(it.description) LIKE LOWER(CONCAT('%', :text, '%'))
            )
            """)
    List<Item> searchAvailableItems(@Param("text") String text);

    List<Item> findByOwnerId(Long ownerId);
}
