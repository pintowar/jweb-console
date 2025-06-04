package io.github.pintowar.jweb.console.repl.impl;

import io.github.pintowar.jweb.console.repl.ReplJsr223;
import io.github.pintowar.jweb.console.repl.ScriptResult;
import kotlin.script.experimental.jsr223.KotlinJsr223DefaultScriptEngineFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class KotlinRepl extends ReplJsr223 {

    public KotlinRepl() {
        super();
    }

    public KotlinRepl(Map<String, Object> bindings) {
        super(bindings);
    }

    @Override
    public String getEngineName() {
        return "kotlin";
    }

    @Override
    public ScriptResult execute(String script, Map<String, Object> bindings) {
        PrintStream previous = System.out;
        try {
            Charset utf8 = StandardCharsets.UTF_8;
            ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
            PrintStream customPrintStreamOut = new PrintStream(baosOut, true, utf8);
            System.setOut(customPrintStreamOut);

            ScriptResult result = super.execute(script, bindings);
            return ScriptResult.create(result.getResult(), baosOut.toString(utf8));
        } finally {
            System.setOut(previous);
        }
    }
}
