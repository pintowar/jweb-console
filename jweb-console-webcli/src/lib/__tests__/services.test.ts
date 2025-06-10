import type { ScriptResult } from "../interfaces";
import { engineEval, listEngines, sampleSelect } from "../services";
import { setupMockFetch } from "../../testHelpers";

const fetchCopy = globalThis.fetch;
const localStorageCopy = globalThis.window.localStorage;

describe("services suite tests", () => {
  afterAll(() => {
    globalThis.fetch = fetchCopy;
    globalThis.window.localStorage = localStorageCopy;
  });

  it("should succesfully eval engine", async () => {
    const engine = "groovy";
    const script = 'prinltn("test"); 1 + 3';
    const expectedResult = {
      result: "4",
      stdout: ["test"],
      stderr: [],
    } as ScriptResult;

    const fetchMock = setupMockFetch(expectedResult);

    const result = await engineEval(engine, script);

    const form = new FormData();
    form.append("script", script);
    expect(fetchMock).toHaveBeenCalledWith(`/console/${engine}/eval`, {
      method: "POST",
      body: form,
    });
    assert.deepEqual(result, expectedResult);
  });

  it("should succesfully list engines", async () => {
    const expectedResult = ["groovy", "jruby"];
    const fetchMock = setupMockFetch(expectedResult);

    const engines = await listEngines();

    expect(fetchMock).toHaveBeenCalledWith("/console/engines");
    assert.equal(engines, expectedResult);
  });

  it("should succesfully get sample", async () => {
    const expectedResult = "some script content";
    const sampleEndpoint = "/some-sample.script";
    const fetchMock = setupMockFetch(expectedResult, 200, "text");

    const sample = await sampleSelect(sampleEndpoint);

    expect(fetchMock).toHaveBeenCalledWith(sampleEndpoint);
    assert.equal(sample, expectedResult);
  });
});
