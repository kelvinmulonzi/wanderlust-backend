package com.example.travelapp.services;

import com.example.travelapp.models.Bookmark;
import com.example.travelapp.repository.BookmarkRepository;
import org.springframework.stereotype.Service;
import java.util.List;public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public List<Bookmark> getUserBookmarks(Long userId) {
        return bookmarkRepository.findByUserId(userId);
    }

    public boolean toggleBookmark(Long userId, Long locationId) {
        if (bookmarkRepository.existsByUserIdAndLocationId(userId, locationId)) {
            bookmarkRepository.deleteByUserIdAndLocationId(userId, locationId);
            return false;
        } else {
            Bookmark bookmark = new Bookmark(userId, locationId);
            bookmarkRepository.save(bookmark);
            return true;
        }
    }
}
