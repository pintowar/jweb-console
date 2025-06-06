package io.github.pintowar.jweb.console.repl.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.pintowar.jweb.console.repl.ScriptResult;
import java.util.Collections;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class KotlinReplTest {

  private KotlinRepl repl;

  @BeforeEach
  void setUp() {
    repl = new KotlinRepl(Collections.singletonMap("applicationContext", "some object"));
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
    ScriptResult result = repl.execute("println(2 + 3)");

    assertNull(result.getResult());
    assertArrayEquals(Arrays.array("5"), result.getStdout());
  }

  @Test
  void shouldIncludeBothOutputAndResultObjectInTheResult() {
    ScriptResult result = repl.execute("println(\"test\"); 2 + 3");

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
            RuntimeException.class, () -> repl.execute("throw RuntimeException(\"test\")"));

    assertEquals("test", thrown.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"2 <!> 3"})
  void shouldThrowException(String script) {
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> repl.execute(script));

    assertTrue(thrown.getMessage().contains("Error: incomplete code."));
  }

  @Test
  void shouldCleanScript() {
    String script =
        """
                println("Should be replaced")
                System.out.println("Should NOT be replaced")
                print("Should be replaced")
                obj.print("Should NOT be replaced")
                println( "Object with spaces" )
                myObj.println( "Object with spaces" )
                println(\"\"\"
                Multiline
                to be
                replaced
                \"\"\")
                """
            .stripIndent();

    String newScript = repl.cleanScript(script);
    String expectedResult =
        String.format(
            """
                %s.println("Should be replaced")
                System.out.println("Should NOT be replaced")
                %s.print("Should be replaced")
                obj.print("Should NOT be replaced")
                %s.println( "Object with spaces" )
                myObj.println( "Object with spaces" )
                %s.println(\"\"\"
                Multiline
                to be
                replaced
                \"\"\")
                """
                .stripIndent(),
            repl.kotlinReplStdOut,
            repl.kotlinReplStdOut,
            repl.kotlinReplStdOut,
            repl.kotlinReplStdOut);

    assertEquals(expectedResult, newScript);
  }
}
