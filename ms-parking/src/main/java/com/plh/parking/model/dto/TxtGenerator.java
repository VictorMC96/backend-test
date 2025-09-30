package com.plh.parking.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class TxtGenerator {
    private Character separator;
    private List<?> data;
}
