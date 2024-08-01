package com.safefoodtruck.sft.favorites.repository;

import com.safefoodtruck.sft.favorites.domain.Favorites;
import com.safefoodtruck.sft.favorites.dto.MemberFavoritesDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoritesRepository extends JpaRepository<Favorites, Integer> {

    @Query(value = "SELECT new com.safefoodtruck.sft.favorites.dto.MemberFavoritesDto(f.favoriteId, f.store.id) " +
            "FROM Favorites f " +
            "WHERE f.member.email = :email " +
            "ORDER BY f.favoriteId desc")
    List<MemberFavoritesDto> findByMember(@Param("email") String email);
}
