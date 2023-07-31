package com.example.socksshop.models;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class Socks {
    private Color color;
    private Size size;
    private int cottonPart;
}
