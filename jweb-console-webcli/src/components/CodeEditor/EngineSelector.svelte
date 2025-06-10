<script lang="ts">
  import { onMount } from "svelte";

  import { listEngines } from "../../lib/services";
  import { NO_ENGINE } from "./constants";
  import Select from "../Select.svelte";

  let { selectedEngine = $bindable(), change } = $props<{ selectedEngine: string; change: () => void }>();

  let engines = $state<string[]>([]);

  onMount(async () => {
    const resp = await listEngines();
    engines = resp.length > 0 ? resp : [NO_ENGINE];
    if (engines.length >= 1) {
      selectedEngine = engines[0];
    }
  });
</script>

{#if engines.length > 1}
  <Select
    options={engines.map((it) => ({
      value: it,
      desc: it,
    }))}
    bind:value={selectedEngine}
    {change}
  />
{:else}
  <span>{selectedEngine}</span>
{/if}
