package io.github.pintowar.jweb.console.boot;

import static java.util.Collections.singletonMap;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.web.servlet.function.ServerResponse.ok;

import io.github.pintowar.jweb.console.repl.Repl;
import io.github.pintowar.jweb.console.repl.ScriptResult;
import java.util.Map;
import java.util.Set;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/** Handler for evaluating scripts from web console. */
public class EvalConsoleHandler {

  private Map<String, Repl> repls;
  private ApplicationContext applicationContext;

  public EvalConsoleHandler(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.repls = Repl.getNamedRepls();
  }

  public ServerResponse index() {
    return ok().contentType(TEXT_HTML).render("/console/index.html");
  }

  public EntityResponse<Set<String>> engines() {
    Set<String> engines = repls.keySet();
    return EntityResponse.fromObject(engines).contentType(APPLICATION_JSON).build();
  }

  public EntityResponse<ScriptResult> eval(ServerRequest req) {
    Repl repl = repls.get(req.pathVariable("engine"));
    ScriptResult result =
        repl.eval(
            req.param("script").orElse(null),
            singletonMap("applicationContext", applicationContext));
    return EntityResponse.fromObject(result).contentType(APPLICATION_JSON).build();
  }
}
