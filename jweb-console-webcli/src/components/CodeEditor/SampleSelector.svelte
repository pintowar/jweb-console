<script lang="ts">
  import { sampleSelect } from "../../lib/services";
  import Select from "../Select.svelte";

  let {
    scriptBody = $bindable(),
    sample,
    selectedEngine,
  } = $props<{ scriptBody: string; sample: string; selectedEngine: string }>();

  function samplePath(file: string) {
    const basePath = import.meta.env.DEV ? "" : import.meta.env.BASE_URL;
    return `${basePath}/samples/${file}`;
  }

  function listSamples(engine: string) {
    const samples = new Map<string, string>([
      ["get-environment-info", "Get environment info"],
      ["list-spring-beans", "List all spring beans"],
      ["is-it-friday", "Is it friday?"],
    ]);
    return Array.from(
      samples.keys().map((key) => ({
        value: samplePath(`${key}.${engine}`),
        desc: samples.get(key) || "",
      }))
    );
  }

  async function sampleToScript() {
    scriptBody = await sampleSelect(sample);
  }
</script>

<Select
  label="Sample Code:"
  options={listSamples(selectedEngine)}
  defaultValue={true}
  bind:value={sample}
  change={sampleToScript}
/>
