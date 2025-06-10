<script lang="ts">
  import { createEventDispatcher } from "svelte";

  type Option = { value: string; desc: string };

  export let label: string | null = null;
  export let value: string;
  export let options: Option[] = [];
  export let defaultValue = false;

  $: id = label?.toLowerCase();
  $: finalOptions = (defaultValue ? [{ value: "", desc: "---" }] : []).concat(options);

  const dispatch = createEventDispatcher();

  function onChange() {
    dispatch("change");
  }
</script>

{#if label}
  <div>
    <label for={id}>{label}</label>
    <select {id} bind:value on:change={onChange}>
      {#each finalOptions as option (option.value)}
        <option value={option.value}>{option.desc}</option>
      {/each}
    </select>
  </div>
{:else}
  <select bind:value on:change={onChange}>
    {#each finalOptions as option (option.value)}
      <option value={option.value}>{option.desc}</option>
    {/each}
  </select>
{/if}

<style>
  select {
    background-color: #5f5f5f;
    font-family: sans-serif;
    font-size: 12px;
    height: 22px;
    color: #fff;
    border: 1px solid #ddd;
    margin-right: 20px;
  }
</style>
