package com.ephirious.entities;

import lombok.*;

@Value
public class Currency {
    Long id;
    String code;
    String name;
    String sign;
}
