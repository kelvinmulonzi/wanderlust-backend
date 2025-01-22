package com.example.travelapp.repository;

import com.example.travelapp.models.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("SELECT b FROM Bookmark b WHERE b.userId = :userId AND b.location IS NOT NULL")
    List<Bookmark> findAllBookmarksWithLocationByUserId(@Param("userId") Long userId);
}