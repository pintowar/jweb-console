package io.github.pintowar.console.repl;

import org.springframework.util.StringUtils;

public final class ScriptResult {

    private String[] output;
    private Object result;

    private ScriptResult() {
    }

    public String[] getOutput() {
        return output;
    }

    public Object getResult() {
        return result;
    }

    public static ScriptResult create(Throwable throwable) {
        String message = throwable.getMessage() == null ? throwable.getClass().getName() : throwable.getMessage();
        return create(null, message);
    }

    static ScriptResult create(Object result, String output) {
        ScriptResult scriptletResult = new ScriptResult();
        scriptletResult.result = result != null ? result.toString() : null;
        scriptletResult.output = StringUtils.hasLength(output) ? output.split(System.lineSeparator()) : new String[]{};
        return scriptletResult;
    }
}