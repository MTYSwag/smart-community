package com.smart.common.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResultVO<T> {
    private List<T> data;
    private Long total;
}