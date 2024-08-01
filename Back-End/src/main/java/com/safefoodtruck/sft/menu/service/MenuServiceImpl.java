package com.safefoodtruck.sft.menu.service;

import com.safefoodtruck.sft.common.util.MemberInfo;
import com.safefoodtruck.sft.menu.domain.Menu;
import com.safefoodtruck.sft.menu.dto.request.MenuListRegistRequestDto;
import com.safefoodtruck.sft.menu.dto.response.MenuListResponseDto;
import com.safefoodtruck.sft.menu.dto.response.MenuResponseDto;
import com.safefoodtruck.sft.menu.repository.MenuRepository;
import com.safefoodtruck.sft.store.domain.Store;
import com.safefoodtruck.sft.store.exception.StoreNotFoundException;
import com.safefoodtruck.sft.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

	private final StoreRepository storeRepository;
	private final MenuRepository menuRepository;

	@Override
	public MenuListResponseDto registMenu(MenuListRegistRequestDto menuListRegistRequestDto) {
		String email = MemberInfo.getEmail();
		Store store = storeRepository.findByOwnerEmail(email).orElseThrow(StoreNotFoundException::new);

		List<MenuResponseDto> menuResponseDtos = menuListRegistRequestDto.menuRegistRequestDtos().stream()
			.map(dto -> {
				Menu menu = Menu.of(dto.name(), dto.price(), dto.description());
				menu.addStore(store);
				Menu savedMenu = menuRepository.save(menu);
				return MenuResponseDto.fromEntity(savedMenu);
			})
			.toList();

		return new MenuListResponseDto(menuResponseDtos);
	}

	@Override
	public MenuListResponseDto findAllMenu(int storeId) {
		Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
		List<Menu> menuList = store.getMenuList();

		List<MenuResponseDto> menuResponseDtos = menuList.stream()
			.map(MenuResponseDto::fromEntity)
			.toList();

		return new MenuListResponseDto(menuResponseDtos);
	}
}
