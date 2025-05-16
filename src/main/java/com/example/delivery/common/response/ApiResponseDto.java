package com.example.delivery.common.response;

import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.micrometer.common.lang.Nullable;
import lombok.Builder;
import org.antlr.v4.runtime.misc.NotNull;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
public record ApiResponseDto<T>(
        int statusCode,                                // HTTP 상태 코드 숫자 (예: 400)
        @NotNull String message,                       // 응답 메시지 (ex: "가게 생성 성공", "잘못된 요청입니다")
        T data// 실제 응답 데이터 (nullable), null이면 JSON에서 제외됨
) {

    public static <T> ApiResponseDto<T> success(final SuccessCode successCode,
                                                @Nullable final T data
    ) {
        return new ApiResponseDto<>(
                successCode.getHttpStatus().value(),
                successCode.getMessage(),
                data
        );
    }
    /**
     * 성공 응답을 생성하는 메소드 (데이터 X)
     * 예를 들면 로그아웃에 성공했습니다 -> 따로 줄 데이터가 없음
     * @param successCode 성공 상태코드/메시지 Enum
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> success(final SuccessCode successCode) {
        return success(successCode, null);
    }
    /**
     * 실패 응답을 생성하는 메소드 (ErrorCode 기반)
     * @param errorCode 에러 코드 Enum
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> fail(final ErrorCode errorCode) {
        return new ApiResponseDto<>(
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                null
        );
    }

    /**
     * 실패 응답을 생성하는 메소드 (직접 커스텀 메시지를 줄 경우)
     * @return ApiResponseDto
     */
    public static <T> ApiResponseDto<T> fail(final ErrorCode errorCode, final String message) {
        return new ApiResponseDto<>(
                errorCode.getHttpStatus().value(),
                message,
                null
        );
    }
}