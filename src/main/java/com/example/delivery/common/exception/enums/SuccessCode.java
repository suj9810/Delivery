package com.example.delivery.common.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // 공통
    OK(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),

    // 유저 관련
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입이 완료되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인에 성공하였습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃에 성공하였습니다."),
    WITHDRAWAL_SUCCESS(HttpStatus.OK, "회원 탈퇴가 정상적으로 처리되었습니다."),

    // 가게 관련
    STORE_CREATED(HttpStatus.CREATED, "가게가 성공적으로 생성되었습니다."),
    STORE_UPDATED(HttpStatus.OK, "가게 정보가 성공적으로 수정되었습니다."),
    STORE_CLOSED(HttpStatus.OK, "가게가 성공적으로 적폐업 처리되었습니다."),
    STORE_FIND_SUCCESS(HttpStatus.OK, "가게가 성공정적으로 조회되었습니다."),
    STORE_PAGING_SUCCESS(HttpStatus.OK, "가게 목록 조회가 되었습니다(페이징기반)"),
    STORE_PAGING_CURSOR_SUCCESS(HttpStatus.OK,"가게 목록 조회가 되었습니다(커서 기반 페이징)"),

    // 메뉴 관련
    MENU_CREATED(HttpStatus.CREATED, "메뉴가 성공적으로 등록되었습니다."),
    MENU_UPDATED(HttpStatus.OK, "메뉴가 성공적으로 수정되었습니다."),
    MENU_DELETED(HttpStatus.OK, "메뉴가 성공적으로 삭제되었습니다."),

    // 주문 관련
    ORDER_CREATED(HttpStatus.CREATED, "주문이 성공적으로 완료되었습니다."),
    ORDER_STATUS_UPDATED(HttpStatus.OK, "주문 상태가 성공적으로 변경되었습니다."),
    ORDER_PAGING_SUCCESS(HttpStatus.OK,"주문 목록이 조회되었습니다.(Slice)"),

    // 리뷰 관련
    REVIEW_CREATED(HttpStatus.CREATED, "리뷰가 성공적으로 등록되었습니다."),
    REVIEW_UPDATED(HttpStatus.OK, "리뷰가 성공적으로 수정되었습니다."),
    REVIEW_DELETED(HttpStatus.OK, "리뷰가 성공적으로 삭제되었습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
