package com.safefoodtruck.sft.member.service;

import com.safefoodtruck.sft.member.dto.request.MemberLoginRequestDto;
import com.safefoodtruck.sft.member.dto.response.MemberSelectResponseDto;
import com.safefoodtruck.sft.member.dto.request.MemberSignUpRequestDto;
import com.safefoodtruck.sft.member.dto.request.MemberUpdateRequestDto;
import java.time.LocalDate;

public interface MemberService {
    String signUp(MemberSignUpRequestDto signUpMemberDto, String signUpMethod);
    String login(MemberLoginRequestDto memberLoginRequestDto);
    MemberSelectResponseDto selectMember(String email);
    String checkDuplicateEmail(String email);
    String checkDuplicateNickname(String nickname);
    void updateMember(MemberUpdateRequestDto memberUpdateRequestDto);
    void updateIsResign(String email);
    void joinVip(String email);
    void deactivateVip(String email);
    void extendVip(String email);
    String searchEmail(String name, LocalDate birth, String phoneNumber);
    void searchPassword(String email, String name, LocalDate birth, String phoneNumber);

}
