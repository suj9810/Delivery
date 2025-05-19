package com.example.delivery.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.user.CustomPasswordEncoder;
import com.example.delivery.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.example.delivery.domain.user.dto.request.UserUpdateRequestDto;
import com.example.delivery.domain.user.dto.response.UserResponseDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final CustomPasswordEncoder passwordEncoder;
	private final EntityManager entityManager;
	// private final RedisTemplate<String, String> redisTemplate;

	/**
	 * 회원 조회
	 * @param userId
	 * @return
	 */
	@Transactional(readOnly = true)
	public UserResponseDto getUserById(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		return UserResponseDto.builder()
			.userId(user.getId())
			.userEmail(user.getEmail())
			.userNum(user.getUserNumber())
			.nickName(user.getNickname())
			.build();
	}

	/**
	 * 회원 수정
	 * @param user
	 * @param requestDto
	 * @return
	 */
	@Transactional
	public UserUpdateRequestDto updateUserInfo(User user, UserUpdateRequestDto requestDto) {
		user.updateUserInfo(requestDto.getNickname(), requestDto.getUserNumber());

		return requestDto;
	}

	/**
	 * 비밀번호 수정
	 * @param user
	 * @param requestDto
	 */
	@Transactional
	public void updatePassword(User user, UserPasswordUpdateRequestDto requestDto) {

		// 현재 비밀번호 != 입력한 비밀번호
		if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
		}

		// 현재 비밀번호 == 새 비밀번호
		if (passwordEncoder.matches(requestDto.getNewPassword(), user.getPassword())) {
			throw new CustomException(ErrorCode.SAME_AS_PASSWORD);
		}

		// 새 비밀번호 암호화
		String encodedNewPassword = passwordEncoder.encode(requestDto.getNewPassword());
		user.updatePassword(encodedNewPassword);

		userRepository.save(user);
	}

}
