package ru.practicum.shareit.request.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestSearch;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("select new ru.practicum.shareit.request.model.ItemRequestSearch(ir.id, ir.description, ir.created) " +
            "from ItemRequest as ir " +
            "where ir.userId = ?1 " +
            "order by ir.created ")
    List<ItemRequestSearch> findAllByUserId(long userId);

    @Query("select new ru.practicum.shareit.request.model.ItemRequestSearch(ir.id, ir.description, ir.created) " +
            "from ItemRequest as ir " +
            "where not ir.userId = ?1 ")
    List<ItemRequestSearch> findAllByUserIdNotOrderByCreated(long userId, Pageable pageable);

    @Query("select new ru.practicum.shareit.request.model.ItemRequestSearch(ir.id, ir.description, ir.created) " +
            "from ItemRequest as ir " +
            "where ir.id = ?1 ")
    Optional<ItemRequestSearch> findById(long requestId);

}
