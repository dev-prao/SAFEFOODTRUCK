package com.safefoodtruck.sft.survey.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.safefoodtruck.sft.common.util.StoreType;
import com.safefoodtruck.sft.member.domain.Member;
import com.safefoodtruck.sft.member.exception.NotFoundMemberException;
import com.safefoodtruck.sft.member.repository.MemberRepository;
import com.safefoodtruck.sft.survey.domain.Survey;
import com.safefoodtruck.sft.survey.dto.InsertSurveysRequestDto;
import com.safefoodtruck.sft.survey.dto.SelectSurveysResponseDto;
import com.safefoodtruck.sft.survey.exception.AlreadyRegisteredEmailException;
import com.safefoodtruck.sft.survey.exception.UnSatisfyLengthException;
import com.safefoodtruck.sft.survey.repository.SurveyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void insertSurveys(
        String userEmail,
        List<InsertSurveysRequestDto> insertSurveysRequestDtoList
    ) {
        int size = insertSurveysRequestDtoList.size();
        if (size == 0 || size > 3) {
            throw new UnSatisfyLengthException();
        }

        Member member = memberRepository.findByEmail(userEmail).orElseThrow(
            NotFoundMemberException::new);;
        if (!member.getSurveyList().isEmpty()) {
            throw new AlreadyRegisteredEmailException();
        }

        for (InsertSurveysRequestDto insertSurveyRequestDto : insertSurveysRequestDtoList) {
            surveyRepository.save(Survey.builder()
                .member(member)
                .storeType(insertSurveyRequestDto.getStoreType())
                .sido(insertSurveyRequestDto.getSido())
                .sigungu(insertSurveyRequestDto.getSigungu())
                .dong(insertSurveyRequestDto.getDong())
                .latitude(insertSurveyRequestDto.getLatitude())
                .longitude(insertSurveyRequestDto.getLongitude())
                .build());
        }
    }

    @Override
    public List<SelectSurveysResponseDto> selectSurveys(String sido, String sigungu, String dong) {
        List<SelectSurveysResponseDto> selectSurveysResponseDtoList = surveyRepository.findSurveysResponse(sido, sigungu, dong);
        for (SelectSurveysResponseDto selectSurveysResponseDto : selectSurveysResponseDtoList) {
            String englishTypeName = selectSurveysResponseDto.getStoreType();
            selectSurveysResponseDto.setStoreType(StoreType.parseKorean(englishTypeName));
        }
        return selectSurveysResponseDtoList;
    }
}
