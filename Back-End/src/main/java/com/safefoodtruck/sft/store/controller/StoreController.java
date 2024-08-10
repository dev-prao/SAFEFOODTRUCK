package com.safefoodtruck.sft.store.controller;

import com.safefoodtruck.sft.common.dto.ErrorResponseDto;
import com.safefoodtruck.sft.menu.dto.response.MenuListResponseDto;
import com.safefoodtruck.sft.store.dto.request.StoreLocationRequestDto;
import com.safefoodtruck.sft.store.dto.request.StoreNoticeRegistRequestDto;
import com.safefoodtruck.sft.store.dto.request.StoreRegistRequestDto;
import com.safefoodtruck.sft.store.dto.request.StoreUpdateRequestDto;
import com.safefoodtruck.sft.store.dto.response.StoreFindResponseDto;
import com.safefoodtruck.sft.store.dto.response.StoreInfoListResponseDto;
import com.safefoodtruck.sft.store.dto.response.StoreNoticeResponseDto;
import com.safefoodtruck.sft.store.exception.NullListException;
import com.safefoodtruck.sft.store.exception.StoreImageNotFoundException;
import com.safefoodtruck.sft.store.exception.StoreNotFoundException;
import com.safefoodtruck.sft.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/stores")
@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_owner', 'ROLE_vip_owner')")
    @Operation(summary = "점포 등록", description = "점포를 등록할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "점포 등록에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> registStore(@RequestBody StoreRegistRequestDto storeRegistRequestDto) {
        storeService.registStore(storeRegistRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_owner', 'ROLE_vip_owner')")
    @Operation(summary = "내 점포 조회", description = "점포를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "점포조회에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<StoreFindResponseDto> findStore() {
        StoreFindResponseDto storeFindResponseDto = storeService.findMyStore();
        return new ResponseEntity<>(storeFindResponseDto, HttpStatus.OK);
    }

    @GetMapping("{storeId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "특정 점포 조회", description = "점포를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "점포 조회에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<StoreFindResponseDto> findStore(@PathVariable("storeId") Integer storeId) {
        StoreFindResponseDto storeFindResponseDto = storeService.findStoreById(storeId);
        return new ResponseEntity<>(storeFindResponseDto, HttpStatus.OK);
    }

    @GetMapping("{storeId}/menus")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "해당 점포 메뉴 전체 조회", description = "해당 점포의 메뉴 전체를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "해당 점포 메뉴 전체 조회에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<MenuListResponseDto> findStoreMenus(@PathVariable("storeId") Integer storeId) {
        MenuListResponseDto allMenu = storeService.findStoreMenus(storeId);
        return new ResponseEntity<>(allMenu, HttpStatus.OK);
    }

    @PatchMapping("notice")
    @PreAuthorize("hasAnyRole('ROLE_owner', 'ROLE_vip_owner')")
    @Operation(summary = "내 점포 공지사항 등록/수정", description = "내 점포 공지사항 등록/수정할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "내 점포 공지사항 등록/수정에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> updateStoreNotice(@RequestBody StoreNoticeRegistRequestDto storeNoticeRegistRequestDto) {
        storeService.updateStoreNotice(storeNoticeRegistRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{storeId}/notice")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "특정 점포 공지사항 조회", description = "특정 점포 공지사항 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "특정 점포 공지사항 조회에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<StoreNoticeResponseDto> findStoreNotice(@PathVariable("storeId") Integer storeId) {
        StoreNoticeResponseDto storeNoticeResponseDto = storeService.findStoreNotice(storeId);
        return new ResponseEntity<>(storeNoticeResponseDto, HttpStatus.OK);
    }

    @PatchMapping("notice/delete")
    @PreAuthorize("hasAnyRole('ROLE_owner', 'ROLE_vip_owner')")
    @Operation(summary = "내 점포 공지사항 삭제", description = "내 점포 공지사항 삭제할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "내 점포 공지사항 삭제에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteStoreNotice() {
        storeService.deleteStoreNotice();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PatchMapping
    @PreAuthorize("hasAnyRole('ROLE_owner', 'ROLE_vip_owner')")
    @Operation(summary = "점포 수정", description = "점포를 수정할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "점포수정에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> updateStore(@RequestBody StoreUpdateRequestDto storeUpdateRequestDto) {
        storeService.updateStore(storeUpdateRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_owner', 'ROLE_vip_owner')")
    @Operation(summary = "점포 삭제", description = "점포를 삭제할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "점포삭제에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> deleteStore() {
        storeService.deleteStore();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/open")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "점포 영업 상태 조회", description = "점포의 영업 상태를 조회할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "점포 영업 상태 조회에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Boolean> getStoreStatus() {
        Boolean storeStatus = storeService.getStoreStatus();
        return new ResponseEntity<>(storeStatus, HttpStatus.OK);
    }

    @PatchMapping("/open")
    @PreAuthorize("hasAnyRole('ROLE_owner', 'ROLE_vip_owner')")
    @Operation(summary = "점포 영업 상태 변경", description = "점포의 영업 상태를 변경할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "점포 영업 상태 변경에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> updateStoreStatus() {
        storeService.updateStoreStatus();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/open/all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "영업 중인 모든 점포 조회", description = "영업 중인 모든 점포를 조회하는데 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "영업 중인 모든 점포 조회에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<StoreInfoListResponseDto> findOpenStores() {
        StoreInfoListResponseDto openStores = storeService.findOpenStores();
        return new ResponseEntity<>(openStores, HttpStatus.OK);
    }

    @PatchMapping("/location")
    @PreAuthorize("hasAnyRole('ROLE_owner', 'ROLE_vip_owner')")
    @Operation(summary = "점포 영업 위치 변경", description = "점포의 영업 위치를 변경할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "점포 영업 위치 변경에 성공하였습니다!", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Error Message 로 전달함", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Void> updateStoreLocation(@RequestBody StoreLocationRequestDto storeLocationRequestDto) {
        storeService.updateStoreLocation(storeLocationRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler({StoreNotFoundException.class, StoreImageNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleStoreException(Exception e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }

    @ExceptionHandler({NullListException.class})
    public ResponseEntity<ErrorResponseDto> NullLIstException(Exception e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }

}
