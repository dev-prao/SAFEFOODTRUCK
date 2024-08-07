package com.safefoodtruck.sft.review.dto.response;

import com.safefoodtruck.sft.reply.dto.response.ReplyResponseDto;
import com.safefoodtruck.sft.review.domain.Review;
import com.safefoodtruck.sft.review.dto.ReviewImageDto;
import java.util.List;
import java.util.Optional;
import lombok.Builder;

@Builder
public record ReviewResponseDto(Integer id, String email, String nickname, Integer storeId, Boolean isVisible, Integer star, String content, List<ReviewImageDto> reviewImageDtos, Optional<ReplyResponseDto> replyResponseDto) {
	public static ReviewResponseDto fromEntity(Review review, ReplyResponseDto replyResponseDto) {
		List<ReviewImageDto> reviewImageDtos = review.getReviewImages().stream()
			.map(ReviewImageDto::fromEntity)
			.toList();

		return ReviewResponseDto.builder()
			.id(review.getId())
			.email(review.getCustomer().getEmail())
			.nickname(review.getCustomerNickname())
			.storeId(review.getOrder().getStoreId())
			.isVisible(review.getIsVisible())
			.star(review.getStar())
			.content(review.getContent())
			.reviewImageDtos(reviewImageDtos)
			.replyResponseDto(Optional.ofNullable(replyResponseDto))
			.build();
	}
}
