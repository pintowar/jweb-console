<script lang="ts">
  import type { ScriptResult } from "../../lib/interfaces";
  import { tokyoNight } from "@uiw/codemirror-theme-tokyo-night";
  import { tokyoNightDay } from "@uiw/codemirror-theme-tokyo-night-day";
  import { keymap } from "@codemirror/view";
  import CodeMirror from "svelte-codemirror-editor";

  import { NO_ENGINE } from "./constants";
  import { engineEval } from "../../lib/services";
  import { langByEngine } from "../../lib/langs";

  import EngineSelector from "./EngineSelector.svelte";
  import SampleSelector from "./SampleSelector.svelte";
  import Card from "../Card.svelte";

  import consoleLogo from "../../assets/console.png";
  import sun from "../../assets/sun.svg";
  import moon from "../../assets/moon.svg";

  const emptyEval = Promise.resolve({ result: "", stdout: [], stderr: [] });
  const shortcuts = [
    { key: "Alt-Enter", mac: "Ctrl-Enter" },
    { key: "Alt-r", mac: "Ctrl-r" },
    { key: "Alt-w", mac: "Ctrl-w" },
  ];

  const keymaps = keymap.of([
    {
      ...shortcuts[0],
      run: () => {
        remoteEval();
        return true;
      },
    },
    {
      ...shortcuts[1],
      run: () => {
        remoteEval();
        return true;
      },
    },
    {
      ...shortcuts[2],
      run: () => {
        evalResult = emptyEval;
        return true;
      },
    },
  ]);

  const shortcutsLabel = shortcutsDescription();

  let { evalResult = $bindable() } = $props<{ evalResult: Promise<ScriptResult> }>();

  let sample = $state("");
  let selectedEngine: string = $state(NO_ENGINE);
  let scriptBody = $state("");
  let darkTheme = $state(true);

  const selectedTheme = $derived(darkTheme ? tokyoNight : tokyoNightDay);

  function shortcutsDescription() {
    const platform = navigator.userAgent.toUpperCase();
    if (platform.indexOf("MAC") >= 0) {
      return `${shortcuts[0].mac} or ${shortcuts[1].mac} (to run) | ${shortcuts[2].mac} (to clean)`;
    } else {
      return `${shortcuts[0].key} or ${shortcuts[1].key} (to run) | ${shortcuts[2].key} (to clean)`;
    }
  }

  function handleChangeEngine() {
    scriptBody = "";
    sample = "";
  }

  function remoteEval() {
    evalResult = emptyEval;
    if (scriptBody) {
      evalResult = engineEval(selectedEngine, scriptBody);
    }
  }

  function toggleTheme() {
    darkTheme = !darkTheme;
  }
</script>

<Card>
  <div slot="header" class="top-panel">
    <div class="left-side">
      <div class="logo">
        <img src={consoleLogo} alt="console-logo" />
        <span class="title">Code Editor</span>
      </div>
      <div class="selector">
        <span class="engine">Engine:</span>
        <EngineSelector bind:selectedEngine change={handleChangeEngine} />
      </div>

      <button id="send-button" type="button" onclick={remoteEval}>&#9654; Execute</button>
      <span class="shortcuts">{shortcutsLabel}</span>
    </div>

    <div class="right-side">
      <button id="toggle-theme" type="button" onclick={toggleTheme}>
        <img src={darkTheme ? moon : sun} alt="toggle-icon" />
      </button>
      <SampleSelector bind:scriptBody {sample} {selectedEngine} />
    </div>
  </div>

  <div slot="content">
    {#if selectedEngine && selectedEngine !== NO_ENGINE}
      <CodeMirror
        bind:value={scriptBody}
        lang={langByEngine(selectedEngine)}
        theme={selectedTheme}
        extensions={[keymaps]}
        styles={{
          "&": {
            width: "100%",
            height: "300px",
          },
        }}
      />
    {/if}
  </div>
</Card>

<style>
  .top-panel {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }

  .left-side {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 16px;
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .selector {
    display: inline-flex;
    align-items: center;
    gap: 8px;
  }

  button {
    background-color: #5f5f5f;
    font-family: sans-serif;
    font-size: 12px;
    height: 22px;
    color: #fff;
    border: 1px solid #ddd;
    align-items: center;
    justify-content: center;
  }

  .right-side {
    display: flex;
    flex-direction: row;
    gap: 16px;
    margin-right: 16px;
  }
</style>
