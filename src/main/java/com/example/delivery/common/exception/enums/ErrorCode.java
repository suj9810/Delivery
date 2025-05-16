package com.example.delivery.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 - Bad Request (잘못된 요청)
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 요청입니다."),
    INVALID_ORDER_PRICE(HttpStatus.BAD_REQUEST, "최소 주문 금액 이상으로 주문해야 합니다."),
    STORE_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "이미 폐업 처리된 가게입니다."),
    ORDER_STATUS_CHANGE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "주문 상태를 순차적으로 변경해야 합니다."),
    SELF_REQUEST_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신에게 요청할 수 없습니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    ALREADY_LOGGED_IN(HttpStatus.BAD_REQUEST, "이미 로그인된 사용자입니다."),
    ALREADY_LOGGED_OUT(HttpStatus.BAD_REQUEST, "이미 로그아웃된 사용자입니다."),
    ALREADY_WITHDRAWN(HttpStatus.BAD_REQUEST, "이미 탈퇴한 사용자입니다."),
    INVALID_REVIEW(HttpStatus.BAD_REQUEST, "완료된 주문 건에 대해서만 리뷰 작성이 가능합니다."),

    ORDER_MINIMUM_PRICE(HttpStatus.BAD_REQUEST,"최소 주문 금액을 넘겨주세요."),
    STORE_CLOSED(HttpStatus.BAD_REQUEST, "가게 영업시간이 아닙니다."),


    // 401 - Unauthorized (인증 실패)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // 403 - Forbidden (권한 부족)
    NO_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ONLY_OWNER_CREATE_STORE(HttpStatus.FORBIDDEN, "사장님 권한이 있는 사용자만 가게를 생성할 수 있습니다."),
    ONLY_OWNER_MANAGE_STORE(HttpStatus.FORBIDDEN, "본인 가게만 수정/폐업할 수 있습니다."),
    ONLY_CUSTOMER_ORDER(HttpStatus.FORBIDDEN, "고객만 주문할 수 있습니다."),
    OWNER_PERMISSION_REQUIRED(HttpStatus.FORBIDDEN, "사장님 권한이 필요합니다."),

    // 404 - Not Found (리소스 없음)
    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배달 정보입니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 사진이 없습니다."),

    // 405 - 지원하지 않는 메서드
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메소드입니다."),


    // 409 - Conflict (충돌)
    STORE_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "가게는 최대 3개까지만 등록할 수 있습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    ALREADY_REVIEWED(HttpStatus.CONFLICT, "이미 리뷰를 작성한 주문입니다."),

    // 500 - Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"파일 업로드에 실패했습니다"),
    TEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "테스트 에러입니다.");




    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
