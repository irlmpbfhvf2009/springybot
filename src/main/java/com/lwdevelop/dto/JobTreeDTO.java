package com.lwdevelop.dto;

import java.util.List;
import lombok.Data;

@Data
public class JobTreeDTO {
    private Long id;
    private String label;
    private List<JobTreeDTO> children;
}
