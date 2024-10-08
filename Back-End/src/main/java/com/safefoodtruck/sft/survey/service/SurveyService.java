package com.safefoodtruck.sft.survey.service;

import com.safefoodtruck.sft.survey.dto.InsertSurveysRequestDto;
import com.safefoodtruck.sft.survey.dto.SelectSurveysResponseDto;
import java.util.List;

public interface SurveyService {
    void insertSurveys(String userEmail, List<InsertSurveysRequestDto> insertSurveysRequestDtoList);
    List<SelectSurveysResponseDto> selectSurveys(String sido, String sigungu, String gugun);
}
