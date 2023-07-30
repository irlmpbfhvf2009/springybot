package org.springybot.dto;

import java.util.List;
import lombok.Data;

@Data
public class JobTreeDTO {
    private Long id;
    private String label;
    private List<JobTreeDTO> children;

    List<JobPostingDTO> jobPostingDTO;
    List<JobSeekerDTO> jobSeekerDTO;

}
