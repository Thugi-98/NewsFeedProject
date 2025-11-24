package com.example.newsfeedproject.common.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * 비밀번호를 암호화 하는 클래스
 *
 * @author jiwon jung
 */
@Component
public class PasswordEncoder {

    /**
     * 사용자가 입력한 원본 비밀번호를 BCrypt 알고리즘으로 암호화
     */
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    /**
     * 암호화 된 비밀번호와 입력한 비밀번호가 일치하는지 검증
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }

}
