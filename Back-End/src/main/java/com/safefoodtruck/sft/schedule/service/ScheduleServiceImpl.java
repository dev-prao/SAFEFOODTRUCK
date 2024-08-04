package com.safefoodtruck.sft.schedule.service;

import com.safefoodtruck.sft.schedule.domain.Schedule;
import com.safefoodtruck.sft.schedule.dto.ScheduleDto;
import com.safefoodtruck.sft.schedule.dto.request.ScheduleInsertRequestDto;
import com.safefoodtruck.sft.schedule.dto.response.ScheduleSelectResponseDto;
import com.safefoodtruck.sft.schedule.exception.InvalidRangeDayException;
import com.safefoodtruck.sft.schedule.exception.NotInsertedStoreException;
import com.safefoodtruck.sft.schedule.repository.ScheduleRepository;
import com.safefoodtruck.sft.store.domain.Store;
import com.safefoodtruck.sft.store.repository.StoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final StoreRepository storeRepository;

    @Override
    public void insertSchedule(String ownerEmail,
        ScheduleInsertRequestDto scheduleInsertRequestDto) {
        Integer day = scheduleInsertRequestDto.getDay();
        if (day < 0 || day > 6) throw new InvalidRangeDayException();

        Store store = storeRepository.findByOwnerEmail(ownerEmail)
            .orElseThrow(NotInsertedStoreException::new);
        Schedule schedule = scheduleRepository.findByStoreAndDay(store,
            scheduleInsertRequestDto.getDay());

        if (schedule != null) {
            schedule.updateSchedule(scheduleInsertRequestDto);
        } else {
            schedule = Schedule.builder()
                .store(store)
                .scheduleInsertRequestDto(scheduleInsertRequestDto)
                .build();
        }
        scheduleRepository.save(schedule);
    }

    @Override
    public ScheduleSelectResponseDto selectSchedule(Integer storeId) {
        List<ScheduleDto> scheduleList = scheduleRepository.findSchedules(storeId);
        return new ScheduleSelectResponseDto(scheduleList);
    }
}
