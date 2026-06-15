package com.ephirious.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private Long id;
    private String code;
    private String name;
    private String sign;
}
