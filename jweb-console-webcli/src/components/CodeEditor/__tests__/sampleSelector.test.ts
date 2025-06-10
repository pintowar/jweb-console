import { render, screen } from "@testing-library/svelte";
import userEvent from "@testing-library/user-event";
import SampleSelector from "../SampleSelector.svelte";
import { setupMockFetch } from "../../../testHelpers";

const fetchCopy = globalThis.fetch;
const localStorageCopy = globalThis.window.localStorage;

describe("SampleSelector.svelte", () => {
  afterAll(() => {
    globalThis.fetch = fetchCopy;
    globalThis.window.localStorage = localStorageCopy;
  });

  it("should do initial render", () => {
    const { container } = render(SampleSelector, {
      selectedEngine: "groovy",
      sample: "",
      scriptBody: "",
    });

    const options = screen.queryAllByRole("option");

    expect(container).toBeTruthy();
    expect(options).toHaveLength(4);
    expect((options[0] as HTMLOptionElement).selected).toBeTruthy();
  });

  it("should apply sample to scriptBody", async () => {
    userEvent.setup();
    const fetchMock = setupMockFetch("1 + 1", 200, "text");
    render(SampleSelector, {
      selectedEngine: "groovy",
      sample: "",
      scriptBody: "",
    });

    const isFriday = screen.getByRole("option", { name: "Is it friday?" });
    await userEvent.selectOptions(screen.getByRole("combobox"), isFriday);

    expect((isFriday as HTMLOptionElement).selected).toBeTruthy();
    expect(fetchMock).toHaveBeenCalledWith("/samples/is-it-friday.groovy");
  });
});
