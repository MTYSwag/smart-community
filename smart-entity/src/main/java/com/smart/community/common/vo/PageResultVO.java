package com.smart.community.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResultVO<T> {
    private List<T> data;
    private Long total;
}