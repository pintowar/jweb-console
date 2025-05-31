package io.github.pintowar.jweb.console.micronaut;

import static java.util.Collections.singletonMap;

import io.github.pintowar.jweb.console.repl.Repl;
import io.github.pintowar.jweb.console.repl.ScriptResult;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.io.IOUtils;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Requires(property = "jweb.console.enabled", value = "true", defaultValue = "false")
@Controller("/console")
public class EvalConsoleController {

  private Map<String, Repl> repls;
  private ApplicationContext applicationContext;

  private ResourceResolver resourceResolver;

  public EvalConsoleController(
      ApplicationContext applicationContext, ResourceResolver resourceResolver) {
    this.repls = Repl.getNamedRepls();
    this.applicationContext = applicationContext;
    this.resourceResolver = resourceResolver;
  }

  @Get(produces = {MediaType.TEXT_HTML})
  public Optional<String> index() {
    return resourceResolver
        .getResourceAsStream("classpath:public/console/index.html")
        .map(
            ins -> {
              try (BufferedReader reader = new BufferedReader(new InputStreamReader(ins))) {
                return IOUtils.readText(reader);
              } catch (IOException e) {
                return "";
              }
            });
  }

  @Get(
      value = "/engines",
      produces = {MediaType.APPLICATION_JSON})
  public Set<String> engines() {
    return repls.keySet();
  }

  @Post(
      value = "/{engine}/eval",
      produces = {MediaType.APPLICATION_JSON},
      consumes = {MediaType.MULTIPART_FORM_DATA})
  public ScriptResult eval(@PathVariable String engine, @QueryValue String script) {
    return repls.get(engine).eval(script, singletonMap("applicationContext", applicationContext));
  }
}
