package com.example.inventorycontrol.dto.auth_requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("squid:S1068")
public class AuthRequestDto {

//    @Size(min = 3, max = 100, message = "Имя пользователя должно содержать от 3 до 100 символов")
//    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;
//
//    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}

