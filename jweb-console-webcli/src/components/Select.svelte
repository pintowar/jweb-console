<script lang="ts">
  type Option = { value: string; desc: string };

  let {
    value = $bindable(),
    options = [],
    defaultValue = false,
    label,
    change,
  } = $props<{ value: string; options: Option[]; defaultValue?: boolean; label?: string; change: () => void }>();

  const id = $derived(label?.toLowerCase());
  const finalOptions = $derived((defaultValue ? [{ value: "", desc: "---" }] : []).concat(options));
</script>

{#if label}
  <div>
    <label for={id}>{label}</label>
    <select {id} bind:value onchange={change}>
      {#each finalOptions as option (option.value)}
        <option value={option.value}>{option.desc}</option>
      {/each}
    </select>
  </div>
{:else}
  <select bind:value onchange={change}>
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
