package io.github.pintowar.jweb.console.repl.impl;

import io.github.pintowar.jweb.console.repl.ReplJsr223;
import java.util.Map;

public class GroovyRepl extends ReplJsr223 {

  public GroovyRepl() {
    super();
  }

  public GroovyRepl(Map<String, Object> bindings) {
    super(bindings);
  }

  @Override
  public String getEngineName() {
    return "groovy";
  }
}
