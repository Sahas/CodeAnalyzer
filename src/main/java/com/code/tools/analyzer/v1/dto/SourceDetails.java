package com.code.tools.analyzer.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@Accessors(fluent = true)
public class SourceDetails {
  private String fileName;
  private String extension;
  private String fileLocation;
}
