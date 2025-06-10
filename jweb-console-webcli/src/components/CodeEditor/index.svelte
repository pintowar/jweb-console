<script lang="ts">
  import type { ScriptResult } from "../../lib/interfaces";
  import { oneDark } from "@codemirror/theme-one-dark";
  import { keymap } from "@codemirror/view";
  import CodeMirror from "svelte-codemirror-editor";

  import { NO_ENGINE } from "./constants";
  import { engineEval } from "../../lib/services";
  import { langByEngine } from "../../lib/langs";

  import EngineSelector from "./EngineSelector.svelte";
  import SampleSelector from "./SampleSelector.svelte";
  import Card from "../Card.svelte";

  import consoleLogo from "../../assets/console.png";

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
</script>

<Card>
  <div slot="header">
    <img src={consoleLogo} alt="console-logo" />
    <span class="title">Edit code</span>
    <span class="engine">Engine:</span>
    <EngineSelector bind:selectedEngine change={handleChangeEngine} />
    <button id="send-button" type="button" onclick={remoteEval}>&#9654; Execute</button>
    <span class="shortcuts">{shortcutsLabel}</span>

    <div class="pulled-right">
      <SampleSelector bind:scriptBody {sample} {selectedEngine} />
    </div>
  </div>

  <div slot="content">
    {#if selectedEngine && selectedEngine !== NO_ENGINE}
      <CodeMirror
        bind:value={scriptBody}
        lang={langByEngine(selectedEngine)}
        theme={oneDark}
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
  img {
    position: absolute;
    top: 1px;
  }

  .title {
    margin-left: 40px;
  }

  .engine {
    margin-left: 20px;
  }

  .shortcuts {
    margin-left: 20px;
  }

  button {
    background-color: #5f5f5f;
    font-family: sans-serif;
    font-size: 12px;
    height: 22px;
    color: #fff;
    border: 1px solid #ddd;
    margin-left: 20px;
  }

  .pulled-right {
    float: right;
  }
</style>
