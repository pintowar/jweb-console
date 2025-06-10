import { LanguageSupport, StreamLanguage } from "@codemirror/language";

import { groovy as groovyParser } from "@codemirror/legacy-modes/mode/groovy";
import { ruby as rubyParser } from "@codemirror/legacy-modes/mode/ruby";
import { kotlin as kotlinParser } from "@codemirror/legacy-modes/mode/clike";

function groovy() {
  return new LanguageSupport(StreamLanguage.define(groovyParser));
}

function ruby() {
  return new LanguageSupport(StreamLanguage.define(rubyParser));
}

function kotlin() {
  return new LanguageSupport(StreamLanguage.define(kotlinParser));
}

export function langByEngine(engine: string): LanguageSupport {
  switch (engine) {
    case "groovy":
      return groovy();
    case "ruby":
    case "jruby":
      return ruby();
    case "kotlin":
      return kotlin();
    default:
      throw new Error("Invalid language");
  }
}
