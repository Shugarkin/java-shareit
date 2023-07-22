package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemSearch;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long userId, Pageable pageable);

    @Query("select new ru.practicum.shareit.item.dto.ItemSearch(it.id, it.name, it.description, it.available) " +
            "from Item as it " +
            "where it.available = true and (lower(it.name) like concat('%', lower(?1), '%') " +
            "            or lower(it.description) like concat('%', lower(?2), '%'))")
    List<ItemSearch> findItemSearch(String text, String text1, Pageable pageable);

    Optional<Item> findByIdAndOwnerId(Long itemId, Long userId);

    @Query("select it " +
            "from Item  as it " +
            "where it.requestId in :list")
    List<Item> findAllByRequestIds(@Param("list") List<Long> list);

    @Query("select it " +
            "from Item  as it " +
            "where it.requestId = ?1")
    List<Item> findAllByRequestId(long requestId);
}
