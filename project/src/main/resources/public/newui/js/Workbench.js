/*
    Workbench (c) 2016 Ethan Crowder and J. Teissler, All Rights Reserved
    Main workbench JS for creating Workbench app instance.
    jQuery MUST be loaded before this script will function.
*/

var workbench_debug = false;
var workbench_launch_attempts = 0;
var workbench_launch_timeout;

/* ===================================== */
/* === STARTUP                       === */
/* === Only used for loading         === */
/* ===================================== */
window.onload = function() {
  if(window.jQuery) {
    if(workbench_debug)
      console.log("[Workbench] jQuery is loaded, starting...");
    // Insert starting function here
    return;
  } else {
    if(workbench_debug)
      console.log("[Workbench] jQuery not found, waiting...");
    workbench_launch_attempts++;
    workbench_launch_timeout = window.setTimeout(attemptBenchLoad, 1000);
  }
}

function attemptBenchLoad() {
  if(window.jQuery) {
    if(workbench_debug)
      console.log("[Workbench] jQuery is loaded, starting...");
    window.clearTimeout(workbench_launch_timeout);
    // Insert starting function here
    return;
  } else {
    if((workbench_launch_attempts % 4) === 0) {
      console.log("[Workbench] 4 jQuery attempts failed, loading alternate...");
      document.getElementById("jquery").parentNode.removeChild(document.getElementById("jquery"));
      var newScript = document.createElement("script");
      newScript.src = "./jquery-2.2.4.min.js";
      document.getElementsByTagName("head")[0].appendChild(newScript);
    } else {
      if(workbench_debug)
        console.log("[Workbench] jQuery not found, waiting...");
    }
    workbench_launch_attempts++;

  }
}
