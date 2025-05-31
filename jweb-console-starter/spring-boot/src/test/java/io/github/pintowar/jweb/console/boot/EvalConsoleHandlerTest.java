package io.github.pintowar.jweb.console.boot;

import static io.github.pintowar.jweb.console.boot.Helper.mockRequest;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import io.github.pintowar.jweb.console.repl.Repl;
import io.github.pintowar.jweb.console.repl.ScriptResult;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.servlet.function.RenderingResponse;
import org.springframework.web.servlet.function.ServerRequest;

class EvalConsoleHandlerTest {

  private final String mockEngine = "mock";

  private final ApplicationContext applicationContext = mock(ApplicationContext.class);

  private final Repl repl = mock(Repl.class);

  @Test
  void shouldRenderIndex() {
    try (MockedStatic<Repl> utilities = mockStatic(Repl.class)) {
      utilities.when(Repl::getNamedRepls).thenReturn(singletonMap(mockEngine, repl));

      EvalConsoleHandler handler = new EvalConsoleHandler(applicationContext);

      RenderingResponse response = (RenderingResponse) handler.index();

      assertEquals(HttpStatus.OK, response.statusCode());
      assertEquals(MediaType.TEXT_HTML_VALUE, response.headers().getFirst(CONTENT_TYPE));
      assertEquals("/console/index.html", response.name());
    }
  }

  @Test
  void shouldListEngines() {
    try (MockedStatic<Repl> utilities = mockStatic(Repl.class)) {
      utilities.when(Repl::getNamedRepls).thenReturn(singletonMap(mockEngine, repl));

      EvalConsoleHandler handler = new EvalConsoleHandler(applicationContext);
      EntityResponse<Set<String>> response = handler.engines();

      assertEquals(HttpStatus.OK, response.statusCode());
      assertEquals(MediaType.APPLICATION_JSON_VALUE, response.headers().getFirst(CONTENT_TYPE));
      assertEquals(singleton(mockEngine), response.entity());
    }
  }

  @Test
  void shouldEvalScript() {
    try (MockedStatic<Repl> utilities = mockStatic(Repl.class)) {
      ScriptResult expectedResult = ScriptResult.create(2, "");

      utilities.when(Repl::getNamedRepls).thenReturn(singletonMap(mockEngine, repl));
      when(repl.eval(anyString(), anyMap())).thenReturn(expectedResult);
      HttpServletRequest request = mockRequest("/console/mock/eval", "POST", "1 + 1", mockEngine);

      EvalConsoleHandler controller = new EvalConsoleHandler(applicationContext);

      EntityResponse<ScriptResult> response =
          controller.eval(ServerRequest.create(request, emptyList()));

      assertEquals(HttpStatus.OK, response.statusCode());
      assertEquals(MediaType.APPLICATION_JSON_VALUE, response.headers().getFirst(CONTENT_TYPE));
      assertEquals(expectedResult.getResult(), response.entity().getResult());
      assertEquals(expectedResult.getStdout(), response.entity().getStdout());
      assertEquals(expectedResult.getStderr(), response.entity().getStderr());
    }
  }
}
