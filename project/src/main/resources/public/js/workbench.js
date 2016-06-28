/*
    Workbench (c) 2016 Ethan Crowder and J. Teissler, All Rights Reserved
    Main workbench JS for creating Workbench app instance.
    jQuery MUST be loaded before this script will function.
*/

var workbench_debug = false;
var workbench_settings = {
  api: {
    uri: "http://workbench.online/api",
    port: "80"
  }
};

/* ===================================== */
/* === STARTUP                       === */
/* === Only used for loading         === */
/* ===================================== */
window.onload = function() {
  if(window.jQuery) {
    if(workbench_debug)
      console.log("[Workbench] jQuery is loaded, starting...");
    workbench_launch();
    return;
  } else {
    if(workbench_debug)
      console.log("[Workbench] jQuery not found, loading fallback...");
    var jreplace = document.createElement("script");
    jreplace.src = "js/jquery-2.2.4.min.js";
    document.getElementsByTagName("head").appendChild(jreplace);
    setTimeout(workbench_launch, 2000);
  }
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
  /* === Include dependencies for testing user and bench validity === */
  // TODO Failure handling
  $.getScript("js/controllers/NotificationController.js");
  $.getScript("js/controllers/RequestController.js");
  $.getScript("js/controllers/BenchController.js");

  $.getScript("js/cookies.js");

  /* === Test for user login and instnatiate if true === */
  wb_notificiation = new NotificiationController();
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

  /* === Include Remaning Dependencies === */
  $.getScript("js/jquery.mCustomScrollbar.js");
  $.getScript("js/jquery.scrollTo.min.js");
  $.getScript("https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js");

  $.getScript("js/controllers/NodeController.js");
  $.getScript("js/controllers/ChatController.js");
  $.getScript("js/controllers/SocketController.js");

  /* === Instantiate === */
  wb_ui = new UIController();
  wb_ui.attachHandlers();
}
