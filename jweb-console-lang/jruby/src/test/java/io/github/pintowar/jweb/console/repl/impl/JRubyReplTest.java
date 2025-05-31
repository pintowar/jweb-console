package io.github.pintowar.jweb.console.repl.impl;

import static org.junit.jupiter.api.Assertions.*;

import io.github.pintowar.jweb.console.repl.ScriptResult;
import java.util.Collections;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JRubyReplTest {

  private JRubyRepl repl;

  @BeforeEach
  void setUp() {
    repl = new JRubyRepl(Collections.singletonMap("applicationContext", "some object"));
  }

  @AfterEach
  void tearDown() {}

  @Test
  void shouldIncludeScriptReturnResultInTheResultObject() {
    ScriptResult result = repl.execute("2 + 3");

    assertEquals("5", result.getResult());
    assertArrayEquals(Arrays.array(), result.getStdout());
  }

  @Test
  void shouldIncludeScriptOutputInTheResultObject() {
    ScriptResult result = repl.execute("puts 2 + 3");

    assertNull(result.getResult());
    assertArrayEquals(Arrays.array("5"), result.getStdout());
  }

  @Test
  void shouldIncludeBothOutputAndResultObjectInTheResult() {
    ScriptResult result = repl.execute("puts('test'); 2 + 3");

    assertEquals("5", result.getResult());
    assertArrayEquals(Arrays.array("test"), result.getStdout());
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenScriptIsMissing() {
    assertThrows(IllegalArgumentException.class, () -> repl.execute(null));
  }

  @Test
  void shouldIncludeApplicationContextInBoundVariables() {
    ScriptResult result = repl.execute("applicationContext != nil");
    assertEquals("true", result.getResult());
    Assertions.assertArrayEquals(Arrays.array(), result.getStdout());
  }

  @Test
  void shouldWrapExceptionWhenExceptionIsThrown() {
    RuntimeException thrown =
        assertThrows(
            RuntimeException.class,
            () -> repl.execute("raise java.lang.RuntimeException.new('test')"));

    assertEquals("test", thrown.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"2 <!> 3"})
  void shouldThrowException(String script) {
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> repl.execute(script));

    assertTrue(thrown.getMessage().contains("(SyntaxError)"));
  }
}
