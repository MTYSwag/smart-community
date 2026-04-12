package com.smart.community.common.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageQueryDTO {
    @Min(value = 1, message = "页码最小为1")
    private Long pageNum = 1L;

    @Min(value = 10, message = "每页条数最小为10")
    private Long pageSize = 10L;
}
