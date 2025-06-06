package io.github.pintowar.jweb.console.repl.impl;

import io.github.pintowar.jweb.console.repl.ReplJsr223;
import io.github.pintowar.jweb.console.repl.ScriptResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

public class KotlinRepl extends ReplJsr223 {

  private final Pattern printlnPattern = Pattern.compile("(?<!\\w\\.)println\\s*\\(");
  private final Pattern printPattern = Pattern.compile("(?<!\\w\\.)print(?!ln)\\s*\\(");
  static final String KOTLIN_REPL_STD_OUT = "z7_0v7";

  public KotlinRepl() {
    super();
  }

  public KotlinRepl(Map<String, Object> bindings) {
    super(bindings);
  }

  @Override
  public String getEngineName() {
    return "kotlin";
  }

  @Override
  public ScriptResult execute(String script, Map<String, Object> bindings) {
    emptyCheck(script);
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
      Map<String, Object> override =
          mergeMaps(bindings, Collections.singletonMap(KOTLIN_REPL_STD_OUT, writer));
      String cleanedScript = cleanScript(script);
      ScriptResult result = super.execute(cleanedScript, override);
      return ScriptResult.create(result.getResult(), baos.toString());
    } catch (IOException e) {
      throw new IllegalStateException("Failed to flush repl stdio.", e);
    }
  }

  String cleanScript(String script) {
    // Apply replacements
    String result = printlnPattern.matcher(script).replaceAll(KOTLIN_REPL_STD_OUT + ".println(");
    return printPattern.matcher(result).replaceAll(KOTLIN_REPL_STD_OUT + ".print(");
  }
}
