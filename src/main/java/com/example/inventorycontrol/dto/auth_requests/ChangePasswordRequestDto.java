package com.example.inventorycontrol.dto.auth_requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequestDto {

//    @NotBlank(message = "это поле не может быть пустым")

    private String oldPassword;

//    @NotBlank(message = "это поле не может быть пустым")
    private String newPassword;
}
