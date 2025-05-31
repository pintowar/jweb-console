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

class GroovyShellTest {

  private GroovyShell repl;

  @BeforeEach
  void setUp() {
    repl = new GroovyShell(Collections.singletonMap("applicationContext", "some object"));
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
    ScriptResult result = repl.execute("println 2 + 3");

    assertNull(result.getResult());
    assertArrayEquals(Arrays.array("5"), result.getStdout());
  }

  @Test
  void shouldIncludeBothOutputAndResultObjectInTheResult() {
    ScriptResult result = repl.execute("println('test'); 2 + 3");

    assertEquals("5", result.getResult());
    assertArrayEquals(Arrays.array("test"), result.getStdout());
  }

  @Test
  void shouldThrowIllegalArgumentExceptionWhenScriptIsMissing() {
    assertThrows(IllegalArgumentException.class, () -> repl.execute(null));
  }

  @Test
  void shouldIncludeApplicationContextInBoundVariables() {
    ScriptResult result = repl.execute("applicationContext != null");
    assertEquals("true", result.getResult());
    Assertions.assertArrayEquals(Arrays.array(), result.getStdout());
  }

  @Test
  void shouldWrapExceptionWhenExceptionIsThrown() {
    RuntimeException thrown =
        assertThrows(
            RuntimeException.class, () -> repl.execute("throw new RuntimeException('test')"));

    assertEquals("test", thrown.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"Thread.sleep(500); 2 + 3", "System.exit(0); 2 + 3", "2 <!> 3"})
  void shouldThrowException(String script) {
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> repl.execute(script));

    assertTrue(thrown.getMessage().contains("startup failed:"));
  }
}
