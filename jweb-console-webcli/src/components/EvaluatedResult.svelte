<script lang="ts">
  import { SyncLoader } from "svelte-loading-spinners";
  import type { ScriptResult } from "../lib/interfaces";
  import Card from "./Card.svelte";

  import warning from "../assets/warning.png";

  let { evalResult } = $props<{ evalResult: Promise<ScriptResult> }>();

  function consoleOutput(buffer: string[]) {
    return buffer.join("\n");
  }
</script>

<Card>
  <div slot="header">
    <span class="title">Result</span>
  </div>

  <div slot="content">
    <div>
      <div class="result-output">
        {#await evalResult}
          <div class="loading">
            <SyncLoader size="60" color="white" unit="px" duration="1s" />
          </div>
        {:then evaluated}
          <div>
            {#if evaluated.stdout}
              <pre class="stdout">{consoleOutput(evaluated.stdout)}</pre>
            {/if}
          </div>
          <div>
            {#if evaluated.stderr}
              <pre class="stderr">{consoleOutput(evaluated.stderr)}</pre>
            {/if}
          </div>
          {#if evaluated.result}
            <div class="result">
              Result: <pre>{evaluated.result}</pre>
            </div>
          {/if}
        {:catch error}
          <div class="error">
            <img src={warning} alt="warning" class="bang" />
            <span class="msg">{error.message}</span>
          </div>
        {/await}
      </div>
    </div>
  </div>
</Card>

<style>
  .result-output {
    height: calc(100vh - 364px);
    color: white;
    background-color: #282c34;
    overflow-y: auto;
  }

  .result-output .loading {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .result-output .error {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    color: red;
  }

  .result-output .error .bang {
    height: 35px;
  }

  .result-output .error .msg {
    padding-left: 10px;
  }

  .result-output .result {
    color: #528bff;
  }

  .result-output .stdout {
    color: #abb2bf;
  }

  .result-output .stderr {
    color: red;
  }
</style>
