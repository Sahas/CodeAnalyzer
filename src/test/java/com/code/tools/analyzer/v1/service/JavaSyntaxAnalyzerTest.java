package com.code.tools.analyzer.v1.service;

import com.code.tools.analyzer.v1.dto.SourceAnalysisReport;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringSubstitutor;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSyntaxAnalyzerTest {

  JavaSyntaxAnalyzer analyzer =  new JavaSyntaxAnalyzer();

  @Test
  public void testAnalysis() throws IOException {
    List<String> lines = FileUtils
      .readLines(new File("/Users/sahas.nanabala/AlterEgo/enthu/CodeAnalyzer/src/main/java/com/code/tools/analyzer/v1/service/JavaSyntaxAnalyzer.java"),
        StandardCharsets.UTF_8);

    SourceAnalysisReport report = analyzer.analyze(lines);
    System.out.println(report);
  }

  @Test
  public void testVarDeclaration() {
    String normalVarDecl = "String str;";
    String normalVarAssignemnt = "int blankLines = 0;";
    String normalVarAssignemntWithSpaces = "String   str =     \"Sahas\"  ;";
    String varAssignmentWithModifiers = "public static String str = \"Sahas\";";
    // pattern  = two words  ending with semicolon or more than two words with =
    //pattern  = ^[_]?[a-zA-Z0-9_]+[a-zA-Z0-9]+\s+[a-zA-Z_][a-zA-Z0-9_][a-zA-Z0-9]+\s*$
    String varDeclpattern = "^[_]?[a-zA-Z0-9_]+";
    Pattern pattern = Pattern.compile(varDeclpattern);
    Matcher matcher = pattern.matcher("normalVar_Decl");
    System.out.println(matcher.find());
//    Assert.assertTrue(JavaSyntaxAnalyzer.isVariableDeclaration(normalVarAssignemnt));
//    Assert.assertTrue(JavaSyntaxAnalyzer.isVariableDeclaration(normalVarDecl));
//    Assert.assertTrue(JavaSyntaxAnalyzer.isVariableDeclaration(normalVarAssignemntWithSpaces));
//    Assert.assertTrue(JavaSyntaxAnalyzer.isVariableDeclaration(varAssignmentWithModifiers));
  }

  @Test
  public void patternTests() {
    String stringBeginning = "^";
    // <  and > for supporting template literals
    //Supports variable names beginning with/containing underscores.
    // Doesn't match variables starting with numbers
    String varNameOrTypePattern = "_*?[a-zA-Z][a-zA-Z0-9_<]*[a-zA-Z0-9>]*";

    Pattern pattern = Pattern.compile(stringBeginning + varNameOrTypePattern);
    Assert.assertTrue(pattern.matcher("normalVar_Decl").find());
    Assert.assertTrue(pattern.matcher("normalVarDecl").find());
    Assert.assertTrue(pattern.matcher("_Underscored_Var").find());
    Assert.assertTrue(pattern.matcher("int").find());
    Assert.assertFalse(pattern.matcher("9int").find());
    // Blanks And Optional Semicolon or Assignment
    // "{" included for  lambda funtion Or Anon Inner classes assignment;
    String assignmentRHS = "=.+[;{]";
    pattern = Pattern.compile(assignmentRHS);
    Assert.assertTrue(pattern.matcher("= \"ABD\";").find());
    Assert.assertTrue(pattern.matcher("= \"ABD\";").find());
    //For covering +=, -=, *=,/=
    String preAssignmentPossibleOptionalOperators = "[\\+\\-\\/\\%\\*]?";
    String semiColonOrBracketStart = "[;{]?";
    String atleastOneSpace = "\\s+";
    String zeroOrMoreSpaces = "\\s*";
    String atleastOneCharacter = ".+";

    //  %s replaced by the parameter strings
    Map<String, String> templateMap = new HashMap<String, String>() {{
      put("preAssignmentPossibleOptionalOperators", preAssignmentPossibleOptionalOperators);
      put("semiColonOrBracketStart", semiColonOrBracketStart);
      put("atleastOneSpace", atleastOneSpace);
      put("zeroOrMoreSpaces", zeroOrMoreSpaces);
      put("atleastOneCharacter", atleastOneCharacter);
    }};

    StringSubstitutor substitutor = new StringSubstitutor(templateMap);
    String assignmentAndOrSemiColonInRHSTemplate = "${zeroOrMoreSpaces}[${preAssignmentPossibleOptionalOperators}=${atleastOneCharacter}${semiColonOrBracketStart}]|;]";
//    String assignmentAndOrSemiColonInRHS = String
//      .format("%s[%s=%s%s|;]", zeroOrMoreSpaces, preAssignmentPossibleOptionalOperators, atleastOneCharacter,
//        semiColonOrBracketStart);
    String assignmentAndOrSemiColonInRHSPattern = substitutor.replace(assignmentAndOrSemiColonInRHSTemplate);
    String varDeclPattern = stringBeginning + varNameOrTypePattern + atleastOneSpace + varNameOrTypePattern + assignmentAndOrSemiColonInRHSPattern;
    pattern = Pattern.compile(varDeclPattern);

    //Positive cases
    Assert.assertTrue(pattern.matcher("int normalVar_Decl ;").find());
    Assert.assertTrue(pattern.matcher("String normalVar_Decl;").find());
    Assert.assertTrue(pattern.matcher("String JAVA_IMPORT_STATEMENT_START_IDENTIFIER;").find());
    Assert.assertTrue(pattern.matcher("String JAVA_IMPORT_STATEMENT_START_IDENTIFIER   ;").find());
    Assert.assertTrue(pattern.matcher("L listener = listenerType.cast(Proxy.newProxyInstance(target.getClass().getClassLoader(),").find());

    Assert.assertTrue(pattern.matcher(
      "CodeStatementClassificationReport codeStatementClassificationReport = CodeStatementClassificationReport.builder()")
      .find());
    Assert.assertTrue(pattern.matcher(
      "List<SourceDetails> sourceDetailsList = SourceFileUtil.fetchSourceList(\"/Users/sahas.nanabala/AlterEgo/enthu/CodeAnalyzer/src/test/resources/Java_EventUtils.java\");")
      .find());
    Assert.assertTrue(pattern.matcher("int multiCommentLines += commentLength").find());
    Assert.assertTrue(pattern.matcher("int i += (commentLength - 1);").find());

    //Negative Cases
    Assert.assertFalse(pattern.matcher("multiCommentLines += commentLength").find());
    Assert.assertFalse(pattern.matcher("if (isImportStatement(cleanLine)) {").find());

  }
}
