/*
    Workbench (c) 2016 Ethan Crowder and J. Teissler, All Rights Reserved
    Main workbench JS for creating Workbench app instance.
    jQuery MUST be loaded before this script will function.
*/

var workbench_debug = true;
var workbench_settings = {
  api: {
    uri: "http://workbench.online/api",
    port: "80"
  }
};
var workbench_launchtimeout;
var workbench_launchattempts = 1;

/* ===================================== */
/* === STARTUP                       === */
/* === Only used for loading         === */
/* ===================================== */

function workbench_startup() {
  if(window.jQuery) {
    if(workbench_debug)
      console.log("[Workbench] jQuery is loaded, starting...");
    workbench_dependencies();
    return;
  } else {
    if(workbench_debug)
      console.log("[Workbench] jQuery not found, loading fallback...");
    var jreplace = document.createElement("script");
    jreplace.src = "js/jquery-2.2.4.min.js";
    document.getElementsByTagName("head").appendChild(jreplace);
    setTimeout(workbench_startup, 2000);
  }
}

function workbench_dependencies() {
  // TODO failure handling
  $.getScript("js/controllers/NotificationController.js");
  $.getScript("js/controllers/RequestController.js");
  $.getScript("js/controllers/BenchController.js");
  $.getScript("js/controllers/NodeController.js");
  $.getScript("js/controllers/ChatController.js");
  $.getScript("js/controllers/SocketController.js");

  $.getScript("https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js");
  $.getScript("js/cookies.js");
  $.getScript("js/jquery.mCustomScrollbar.concat.min.js");
  $.getScript("js/jquery.scrollTo.min.js");
  workbench_launch();
}


var UIController = function() {

  /* === UIController.attachHandlers ===
    Attach NEARLY ALL event handlers.
    Separated into sections for categorization.
  */
  this.attachHandlers = function() {

  }
}

/* ===================================== */
/* === INITIALIZATION                === */
/* === Start Workbench instance      === */
/* ===================================== */
function workbench_launch() {
  console.log("LAUNCHING!");
  try {
  /* === Test for user login and instnatiate if true === */
  wb_notificiation = new NotificationController();
  wb_request = new RequestController(workbench_settings.api.uri, wb_notificiation);
  if(docCookies.hasItem("workbench_userid") && docCookies.hasItem("workbench_token")) {
    wb_request.send(wb_request.protocol.account.auth(), function(data) {
      // TODO : Grab user data
      wb_bench = new BenchController(wb_user, wb_notificiation, wb_request);
    }, function(data) {
      window.location("/");
      return;
    });
  } else {
    window.location("/");
    return;
  }

  /* === Instantiate === */
  wb_ui = new UIController();
  wb_ui.attachHandlers();
  } catch(e) {
    if(workbench_launchattempts < 11) {
      console.warn("[Workbench] Load attempt " + workbench_launchattempts + " failed, retrying in 1 second...");
      clearTimeout(workbench_launchtimeout);
      workbench_launchtimeout = setTimeout(workbench_launch, 1000);
      workbench_launchattempts++;
    } else {
      clearTimeout(workbench_launchtimeout);
      console.error("[Workbench] Failed to load after 10 tries.");
      return;
    }
  }
}

// === STARTUP RAW JS
window.onload = workbench_startup;
document.ready = workbench_launch;
