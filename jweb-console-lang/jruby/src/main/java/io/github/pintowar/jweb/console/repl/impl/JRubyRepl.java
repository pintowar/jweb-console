package io.github.pintowar.jweb.console.repl.impl;

import io.github.pintowar.jweb.console.repl.ReplJsr223;
import java.util.Map;

public class JRubyRepl extends ReplJsr223 {

  public JRubyRepl() {
    super();
  }

  public JRubyRepl(Map<String, Object> bindings) {
    super(bindings);
  }

  @Override
  public String getEngineName() {
    return "jruby";
  }
}
