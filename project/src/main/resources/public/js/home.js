/*
    Workbench (c) 2016 Ethan Crowder and J. Teissler, All Rights Reserved
    Homepage JS for login, registration, and bench listing.
    jQuery MUST be loaded before this script will function.
*/

var workbench_debug = false;
var workbench_launch_attempts = 0;
var workbench_launch_timeout;
var workbench_settings = {
  api: {
    uri: "http://workbench.online/api",
    port: "80"
  }
};
var workbench_user = {};

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

/* ===================================== */
/* === INITIALIZATION                === */
/* === Start Workbench instance      === */
/* ===================================== */
function workbench_launch() {
  /* === Include dependencies for testing user and bench validity === */
  // TODO Failure handling
  $.getScript("js/controllers/NotificationController.js");
  $.getScript("js/controllers/RequestController.js");

  $.getScript("js/cookies.js");

  /* === Test for user login === */
  wb_notificiation = new NotificationController();
  wb_request = new RequestController(workbench_settings.api.uri, wb_notificiation);
  if(docCookies.hasItem("workbench_userid") && docCookies.hasItem("workbench_token")) {
    wb_request.setAgent(docCookies.getItem("workbench_userid"), docCookies.getItem("workbench_token"))
    wb_request.send(wb_request.protocol.account.auth(), function(data) {
      wb_request.send(wb_request.protocol.account.user(docCookies.getItem("workbench_userid")), function(data) {
        workbench_user = data;
        //TODO: HANDLE CHANGE LOGIN

      }, function(data) {
        //TODO: handle unable to get user info
      });
    }, function(data) { // Fail
      return;
    });
  } else {
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
      var loginkey = $("#login-email").val();
      var password = $("#login-password").val();

      wb_request.send(wb_request.protocol.account.login(loginkey, password), function(data) {
        docCookies.setItem("workbench_userid", data.agent.id);
        docCookies.setItem("workbench_token", data.token);
        location.reload(true);
      }, function(data) {
        wb_notification.notify("Login Failed!", "Invalid username, email, or password");
      })
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
