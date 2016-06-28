/*
    Workbench (c) 2016, All Rights Reserved
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
var workbench_user = {};
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
      wb_request.setAgent(docCookies.getItem("workbench_userid"), docCookies.getItem("workbench_token"));
      wb_request.send(wb_request.protocol.account.auth.request(), function(data) {
        wb_request.send(wb_request.protocol.account.user.request(docCookies.getItem("workbench_userid")), function(data) {
          workbench_user = data;
          //login changes and reload
          $("#nav-register").remove();
          $("#nav-login").remove();
          $("#navbar_buttons").append('<button id="nav-logout" class="btn btn-default">' + workbench_user.user + '  -  Logout</button>');
          $("#bench-list").show();
          $("#nologin").hide();

          $("#nav-logout").click(function() {
            docCookies.removeItem("workbench_userid");
            docCookies.removeItem("workbench_token");
            location.reload(true);
          });

          loadBenchTiles();

        }, function(data) {
          wb_notificiation.notify("User data Error!");
        });
      }, function(data) {
        wb_notificiation.notify("Authentification Error!");
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
  wb_ui = new HomeController();
  wb_ui.attachHandlers();
}
  /* === Instantiate === */

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

    $("#newbench-submit").click(function() {
      wb_ui.createBench();
    });
  };

    this.createBench = function() {
      wb_request.send(wb_request.protocol.bench.create.request($("#newbench-title").val(), 1000, 1000), function(data) {
        location.reload(true);
      }, function(data) {
        wb_notification.notify("Error!", "bench could not be created");
      });
    };


    this.login = function() {
      var loginkey = $("#login-email").val();
      var password = $("#login-password").val();

      wb_request.send(wb_request.protocol.account.login.request(loginkey, password), function(data) {
        docCookies.setItem("workbench_userid", data.agent.id);
        docCookies.setItem("workbench_token", data.token);
        location.reload(true);
      }, function(data) {
        wb_notification.notify("Login Failed!", "Invalid username, email, or password");
      });
    };

    this.register = function() {
      var username = $("#register-username").val();
      var email = $("#register-email").val();
      var password = $("#register-password").val();

      wb_request.send(wb_request.protocol.account.register.request(username, email, password), function(data) {
        if (data.success === true) {
          docCookies.setItem("workbench_userid", data.agent.id);
          docCookies.setItem("workbench_token", data.token);
          location.reload(true);
        } else {
          wb_notificiation.notify("Invalid Registration!", data.error);
        }
      }, function(data) {
        wb_notification.notify("Registration Failed!");
      });
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
};

function loadBenchTiles() {
  for (i = 0; i < workbench_user.owner.length; i++)
  {
    $("#bench-list").append(manufactureElement(workbench_user.owner[i], true));
  }
  for (i = 0; i < workbench_user.member.length; i++)
  {
    $("#bench-list").append(manufactureElement(workbench_user.member[i], false));
  }

  $("#bench-list").append('<p><a id="new_bench_button" class="btn btn-default" href="#" role="button" data-target-"#newbench" data-toggle="modal">New Bench +</a></p>');

  //TODO: MODAL POPUP FOR ADDING BENCHES
}

function manufactureElement(bench, owner) {

  if (bench.title.length > 18)
  {
    bench.title = bench.title.substring(0,18) + "...";
  }

  var you_own = "";

  if (owner) {
    you_own = "You are the owner of this bench.";
  } else {
    you_own = "You are a member of this bench.";
  }

  return '<div class="col-md-4">' +
    '<h2>' + bench.title + '</h2>' +
    '<hr style="border: 2px solid ' + bench.preview + ';">' +
    '<p>This bench has ' + bench.users + ' member(s).</p>' +
    '<p>' + you_own + '</p>' +
    '<p><a class="btn btn-default" href="http://workbench.online/bench?id=' + bench.id + '" role="button">Launch &raquo;</a></p>' +
  '</div>';
}

// === STARTUP RAW JS
window.onload = workbench_startup;
document.ready = workbench_launch;
