package io.github.pintowar.jweb.console.micronaut;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import io.github.pintowar.jweb.console.repl.Repl;
import io.github.pintowar.jweb.console.repl.ScriptResult;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.io.ResourceResolver;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class EvalConsoleControllerTest {

  private String mockEngine = "mock";

  private ApplicationContext applicationContext = mock(ApplicationContext.class);

  private ResourceResolver resourceResolver = mock(ResourceResolver.class);

  private Repl repl = mock(Repl.class);

  @Test
  void shouldRenderIndex() {
    try (MockedStatic<Repl> utilities = mockStatic(Repl.class)) {
      InputStream expectedStream = new ByteArrayInputStream("<h1>test</h1>".getBytes());

      utilities.when(Repl::getNamedRepls).thenReturn(singletonMap(mockEngine, repl));
      when(resourceResolver.getResourceAsStream(anyString()))
          .thenReturn(Optional.of(expectedStream));

      EvalConsoleController controller =
          new EvalConsoleController(applicationContext, resourceResolver);
      controller.index().ifPresent((index) -> assertEquals("<h1>test</h1>", index));
    }
  }

  @Test
  void shouldListEngines() {
    try (MockedStatic<Repl> utilities = mockStatic(Repl.class)) {
      utilities.when(Repl::getNamedRepls).thenReturn(singletonMap(mockEngine, repl));

      EvalConsoleController controller =
          new EvalConsoleController(applicationContext, resourceResolver);
      Set<String> engines = controller.engines();

      assertEquals(singleton(mockEngine), engines);
    }
  }

  @Test
  void shouldEvalScript() {
    try (MockedStatic<Repl> utilities = mockStatic(Repl.class)) {
      ScriptResult expectedResult = ScriptResult.create(2, "");

      utilities.when(Repl::getNamedRepls).thenReturn(singletonMap(mockEngine, repl));
      when(repl.eval(anyString(), anyMap())).thenReturn(expectedResult);

      EvalConsoleController controller =
          new EvalConsoleController(applicationContext, resourceResolver);
      ScriptResult scriptResult = controller.eval(mockEngine, "1 + 1");

      assertEquals(expectedResult.getResult(), scriptResult.getResult());
      assertEquals(expectedResult.getStdout(), scriptResult.getStdout());
      assertEquals(expectedResult.getStderr(), scriptResult.getStderr());
    }
  }
}
