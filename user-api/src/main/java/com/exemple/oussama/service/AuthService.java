package com.exemple.oussama.service;

import com.exemple.oussama.model.dto.AuthResponseDTO;
import com.exemple.oussama.model.dto.LoginRequestDTO;
import com.exemple.oussama.model.dto.SignupRequestDTO;

public interface AuthService {

    AuthResponseDTO signup(SignupRequestDTO dto);

    AuthResponseDTO login(LoginRequestDTO dto);
}
