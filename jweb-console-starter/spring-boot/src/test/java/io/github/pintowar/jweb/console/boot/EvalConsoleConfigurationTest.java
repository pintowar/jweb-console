package io.github.pintowar.jweb.console.boot;

import static java.util.Collections.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.EntityResponse.fromObject;
import static org.springframework.web.servlet.function.ServerResponse.ok;

import io.github.pintowar.jweb.console.repl.ScriptResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ContextConfiguration(classes = {EvalConsoleConfiguration.class})
@TestPropertySource(properties = "jweb.console.enabled=true")
class EvalConsoleConfigurationTest {

  private final String mockEngine = "mock";

  @Autowired private MockMvc mockMvc;

  @MockBean private EvalConsoleHandler handler;

  @BeforeEach
  public void setup() {
    when(handler.index()).thenReturn(ok().contentType(TEXT_HTML).build());
    when(handler.engines()).thenReturn(fromObject(singleton(mockEngine)).build());
    when(handler.eval(any())).thenReturn(fromObject(ScriptResult.create(2, "")).build());
  }

  @Test
  void shouldRouteIndex() throws Exception {
    mockMvc
        .perform(get("/console"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TEXT_HTML));

    verify(handler).index();
  }

  @Test
  void shouldRouteEngines() throws Exception {
    mockMvc
        .perform(get("/console/engines"))
        //        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().json(String.format("[\"%s\"]", mockEngine)));

    verify(handler).engines();
  }

  @Test
  void shouldRouteEval() throws Exception {
    mockMvc
        .perform(post(String.format("/console/%s/eval", mockEngine)).param("script", "1 + 1"))
        //        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().json("{\"stdout\":[],\"stderr\":null,\"result\":\"2\"}"));

    verify(handler).eval(any());
  }
}
