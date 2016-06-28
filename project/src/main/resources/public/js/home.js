/*
    Workbench (c) 2016 Ethan Crowder and J. Teissler, All Rights Reserved
    Homepage JS for login, registration, and bench listing.
    jQuery MUST be loaded before this script will function.
*/

var workbench_debug = false;
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

  $.getScript("https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js");
  $.getScript("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js");
  $.getScript("js/cookies.js");
  $.getScript("js/jquery.mCustomScrollbar.concat.min.js");
  $.getScript("js/jquery.scrollTo.min.js");
  workbench_launch();
}

/* ===================================== */
/* === INITIALIZATION                === */
/* === Start Workbench instance      === */
/* ===================================== */
function workbench_launch() {
  try {
    wb_notificiation = new NotificationController();
    wb_request = new RequestController(workbench_settings.api.uri, wb_notificiation);
    if(docCookies.hasItem("workbench_userid") && docCookies.hasItem("workbench_token")) {
      wb_request.send(wb_request.protocol.account.auth(), function(data) {
        // TODO : Grab user data
        wb_bench = new BenchController(wb_user, wb_notificiation, wb_request);
      }, function(data) { // Fail
        return;
      });
    } else {
      return;
    }
  } catch(e) {
    console.error(e.message);
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

  /* === Include Remaning Dependencies === */
  $.getScript("js/jquery.mCustomScrollbar.js");
  $.getScript("js/jquery.scrollTo.min.js");
  $.getScript("https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js");

  $.getScript("js/controllers/NodeController.js");
  $.getScript("js/controllers/ChatController.js");
  $.getScript("js/controllers/SocketController.js");

  /* === Instantiate === */
  wb_ui = new HomeController();
  wb_ui.attachHandlers();
}

var HomeController = function() {
  // TODO pass in controllers through constructor

  // === Attach UI handlers ===
  this.attachHandlers = function() {
    // Login to register button
    $("#login-nav-register").click(function() {
      $("#login").on("hidden.bs.modal", function() {
        $("#register").modal("show");
        $("#login").off("hidden.bs.modal");
      });
      $("#login").modal("hide");
    });

    // Register to login button
    $("#register-nav-login").click(function() {
      $("#register").on("hidden.bs.modal", function() {
        $("#login").modal("show");
        $("#register").off("hidden.bs.modal");
      });
      $("#register").modal("hide");
    });

    $("#login-submit").click(function() {
      wb_ui.login();
    });

    $("#register-submit").click(function() {
      wb_ui.register();
    });

    this.login = function() {

    };

    this.register = function() {

    };

    this.showLoginAlert = function(message, type) {
      if($("#login .alert").length)
        $("#login .alert").remove();
      $("#login .messagebox").append('<div class="alert alert-dismissable" id="login-alert" role="alert"><button type="button" class="close" data-dismiss="alert">&times;</button>' + message + '</div>');
      if(typeof type === 'undefined') {
        $("#login-alert").addClass("alert-info");
        return;
      }
      if(type == "error")
        $("#login-alert").addClass("alert-danger");
      else if(type == "warning")
        $("#login-alert").addClass("alert-warning");
      else if(type == "success")
        $("#login-alert").addClass("alert-success");
      else {
        $("#login-alert").addClass("alert-info");
      }
    };
  }
}

// === STARTUP RAW JS
window.onload = workbench_startup;
document.ready = workbench_launch;
