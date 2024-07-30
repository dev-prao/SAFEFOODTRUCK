package com.safefoodtruck.sft.store.service;

import com.safefoodtruck.sft.store.domain.Store;
import com.safefoodtruck.sft.store.dto.request.StoreRegistRequestDto;
import com.safefoodtruck.sft.store.dto.request.StoreUpdateRequestDto;
import com.safefoodtruck.sft.store.dto.response.StoreRegistResponseDto;

public interface StoreService {

	StoreRegistResponseDto registStore(StoreRegistRequestDto storeRegistRequestDto);

	Store updateStore(StoreUpdateRequestDto storeUpdateRequestDto);

	Store findStore();

	Store findStore(int storeId);

	void deleteStore();

	boolean getStoreStatus();

	boolean updateStoreStatus();
}
