package com.example.delivery.domain.store.service;

import com.example.delivery.common.exception.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.auth.jwt.UserDetailsImpl;
import com.example.delivery.domain.store.dto.request.SaveStoreRequestDto;
import com.example.delivery.domain.store.dto.request.UpdateStoreRequestDto;
import com.example.delivery.domain.store.dto.response.SaveStoreResponseDto;
import com.example.delivery.domain.store.dto.response.StoreIdAndNameResponseDto;
import com.example.delivery.domain.store.dto.response.StorePageResponse;
import com.example.delivery.domain.store.dto.response.StoreResponseDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public SaveStoreResponseDto saveStore(UserDetailsImpl userDetails, SaveStoreRequestDto dto) {
        
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        
        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .minOrderPrice(dto.getMinOrderPrice())
                .openTime(dto.getOpenTime())
                .closedTime(dto.getClosedTime())
                .user(user)
                .build();
        
        Store savedStore = storeRepository.save(store);

        return new SaveStoreResponseDto(
                savedStore.getId(),
                savedStore.getStoreName(),
                savedStore.getAddress(),
                savedStore.getPhoneNumber(),
                savedStore.getMinOrderPrice(), 
                savedStore.getOpenTime(),
                savedStore.getClosedTime());
    }

    public StorePageResponse getStores(Pageable pageable, String storeName) {

        if (storeName == null) {
            throw new CustomException(ErrorCode.MISSING_PARAMETER);
        }

        Page<StoreIdAndNameResponseDto> stores = storeRepository.findStoreIdAndStoreNameByStoreName(pageable, storeName);

        List<StoreIdAndNameResponseDto> content = stores.getContent();

        return StorePageResponse.builder()
                .totalElements(stores.getTotalElements())
                .totalPages(stores.getTotalPages())
                .hasNextPage(stores.hasNext())
                .hasPreviousPage(stores.hasPrevious())
                .content(content)
                .build();
    }


    public StoreResponseDto getStore(Long storeId) {
        
        Store store = findByIdOrElseThrow(storeId);
        
        return new StoreResponseDto(
                store.getId(), 
                store.getStoreName(), 
                store.getAddress(), 
                store.getPhoneNumber(), 
                store.getMinOrderPrice(),
                store.getOpenTime(),
                store.getClosedTime()
        );
    }

    public void updateStore(UserDetailsImpl userDetails, Long storeId, UpdateStoreRequestDto dto) {

        Store store = findByIdOrElseThrow(storeId);

        validateStoreOwner(userDetails.getId(), store);
        
        store.update(
                dto.getStoreName(),
                dto.getAddress(),
                dto.getPhoneNumber(),
                dto.getOpenTime(),
                dto.getClosedTime(),
                dto.getMinOrderPrice()
        );
    }

    public void deleteStore(UserDetailsImpl userDetails, Long storeId) {

        Store store = findByIdOrElseThrow(storeId);

        validateStoreOwner(userDetails.getId(), store);

        storeRepository.delete(store);
    }
    
    private Store findByIdOrElseThrow (Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    private void validateStoreOwner (Long userId, Store store) {
        if (userId.equals(store.getUser().getId())) {
            throw  new CustomException(ErrorCode.ONLY_OWNER_MANAGE_STORE);
        }
    }
}
