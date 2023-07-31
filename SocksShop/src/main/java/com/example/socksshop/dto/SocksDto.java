package com.example.socksshop.dto;

import com.example.socksshop.models.Color;
import com.example.socksshop.models.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocksDto {
    @NotNull
    private Color color;
    @NotNull
    private Size size;
    @Range(min = 0, max = 100, message = "")
    private int cottonPart;
    @Positive
    @Setter
    private int quantity;
}
