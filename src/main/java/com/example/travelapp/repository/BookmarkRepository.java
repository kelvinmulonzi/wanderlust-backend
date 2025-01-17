package com.example.travelapp.repository;

import com.example.travelapp.models.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    // Find bookmarks by user ID
    List<Bookmark> findByUserId(Long userId);

    // Find bookmarks by location ID
    List<Bookmark> findByLocationId(Long locationId);

    // Find bookmark by user ID and location ID
    Optional<Bookmark> findByUserIdAndLocationId(Long userId, Long locationId);

    // Check if bookmark exists
    boolean existsByUserIdAndLocationId(Long userId, Long locationId);

    // Delete bookmark by user ID and location ID
    void deleteByUserIdAndLocationId(Long userId, Long locationId);

    // Count bookmarks by location ID
    long countByLocationId(Long locationId);

    // Find all bookmarks with location details
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.location WHERE b.user.id = :userId")
    List<Bookmark> findAllBookmarksWithLocationByUserId(@Param("userId") Long userId);

    // Find recent bookmarks
    List<Bookmark> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    // Find bookmarks by category
    List<Bookmark> findByUserIdAndLocation_Category(Long userId, String category);
}