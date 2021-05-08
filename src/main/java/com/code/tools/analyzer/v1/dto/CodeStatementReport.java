package com.code.tools.analyzer.v1.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = true)
public class CodeStatementReport {
  private int numPackageStatements;
  private int numImports;
  private int numClassDeclarations;
  private int numVarDeclarations;
  private int numConditionals;
  private int numLoops;
  private int numReturnStatements;
}
