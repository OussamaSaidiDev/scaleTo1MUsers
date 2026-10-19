package com.exemple.oussama.builder;

import com.exemple.oussama.model.domain.User;
import com.exemple.oussama.model.dto.AuthResponseDTO;
import com.exemple.oussama.model.dto.SignupRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class UserBuilder {

    public User build(SignupRequestDTO dto, String encodedPassword) {
        return User.builder()
            .email(dto.getEmail())
            .password(encodedPassword)
            .fullName(dto.getFullName())
            .build();
    }

    public AuthResponseDTO buildAuthResponse(User user) {
        return AuthResponseDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .fullName(user.getFullName())
            .build();
    }
}
