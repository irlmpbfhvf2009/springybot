package com.lwdevelop.dto;

import java.util.List;
import lombok.Data;

@Data
public class JobTreeDTO {
    Long id;
    String label;
    List<JobTreeDTO> children;
}
