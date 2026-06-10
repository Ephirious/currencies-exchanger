package com.ephirious.entities;

import lombok.*;

@Value
public class Currency {
    Long id;
    String name;
    String code;
    String sign;
}
