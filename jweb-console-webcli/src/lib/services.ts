import type { ScriptResult } from "./interfaces";

export async function engineEval(engine: string, scriptBody: string): Promise<ScriptResult> {
  const data = new FormData();
  data.append("script", scriptBody);

  const resp = await fetch(`/console/${engine}/eval`, {
    method: "POST",
    body: data,
  });
  const scriptResult = (await resp.json()) as ScriptResult;
  return scriptResult;
}

export async function listEngines(): Promise<string[]> {
  try {
    const resp = await fetch(`/console/engines`);
    const engines = (await resp.json()) as string[];
    return engines;
  } catch {
    return [];
  }
}

export async function sampleSelect(sample: string): Promise<string> {
  if (sample) {
    const resp = await fetch(sample);
    return await resp.text();
  } else {
    return "";
  }
}
