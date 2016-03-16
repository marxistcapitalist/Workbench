/* Workbench (c) 2016 Jeremy Teissler, Ethan Crowder, All Rights Reserved */
// TODO: Comprehensive JSDOC Documentation & Minification
$ = jQuery;

var workbench = workbench || {}; // The overarching Workbench object
workbench.auth = workbench.auth || {}; // Login, Logout, Authenticate, Registration???
workbench.bench = workbench.bench || {}; // Workbench manipulation, including nodes
workbench.comm = workbench.comm || {}; // HTTP/Websocket communication
workbench.core = workbench.core || {}; // Initialization and module management
workbench.ui = workbench.ui || {}; // UI Manipulation (toolbars, menus, etc)
workbench.user = workbench.user || {}; // User information retrieval and updates
workbench.util = workbench.util || {}; // Utility functions

workbench.auth = {
  status: false, // If the user is currently authenticated
  timeout: false, // If the auth functions are currently locked with timeout time
  timeoutTime: 1000, // The time after a call to one of the auth functions to timeout
  login: function(user, pass, ui) { // TODO: Impleent login/register/auth timeout to prevent request spam
    var loginobj = {
      loginkey: user,
      password: pass
    };
    if(this.timeout)
      return;
    this.timeout = true;
    setTimeout(function() { workbench.auth.timeout = false; }, this.timeoutTime);
    if(workbench.comm.http.ajaxProgress)
      return;
    var showUI = this.login.arguments.length > 2;
    if(user.length > 32 || pass.length > 256) {
      if(showUI)
        workbench.ui.popup.login.showError("Username or password too long");
      return;
    }
    if(user.length < 1 || pass.length < 1) {
      if(showUI)
        workbench.ui.popup.login.showError("Please enter a username and password");
      return;
    }
    if(showUI)
      workbench.ui.popup.login.showLoad(150);
    if(user == "marx" && pass == "mearse") { // TODO TESTING CODE, Remove with server implementation
      workbench.ui.popup.login.hideLoad();
      docCookies.setItem("wb_user_token", "jsdafoksndfasdlfjasidfoasjdfiojasiojdfiaoisejofaowiejfiosdfjidfopqjwoprpqorq3pirq3rri99i3q9ir90ruipfiafipaasidasdaposidpaosidiii", Infinity);
      docCookies.setItem("wb_user_id", "aaaaaaaaaa", Infinity);
      docCookies.setItem("wb_user_name", user, Infinity);
      workbench.auth.status = true;
      workbench.ui.popup.login.success(function() {
        workbench.bench.benchSelect();
      });
      workbench.bench.load();
      return;
    }
    workbench.comm.http.post(loginobj, "http://localhost:80/login", function(resp) {
      if(resp.result) {
        if(resp.data.hasOwnProperty("token") && resp.data.hasOwnProperty("agent")) {
          docCookies.setItem("wb_user_token", resp.data.token, Infinity);
          docCookies.setItem("wb_user_id", resp.data.agent.id, Infinity);
          docCookies.setItem("wb_user_name", resp.data.agent.user, Infinity);
          workbench.auth.status = true;
          workbench.ui.popup.login.success();
          workbench.bench.benchSelect();
        } else {
          workbench.ui.popup.login.hideLoad();
          workbench.ui.popup.login.showError("Incorrect username or password");
        }
      } else {
        workbench.ui.popup.login.hideLoad();
        workbench.ui.popup.login.showError("HTTP Error, check your connection");
      }
    });
  },

  logout: function() { // Logs the user out of their CURRENT session, by deleting their cookies
    if(docCookies.getItem("wb_user_token") === null)
      return;
    docCookies.removeItem("wb_user_token");
    docCookies.removeItem("wb_user_id");
    docCookies.removeItem("wb_user_name");
    location.replace("index.html#nointro");
  },

  globalLogout: function() { // Logs the user out of their GLOBAL session, by invalidating their token
    if(docCookies.getItem("wb_user_token") === null)
      return;
    var usertoken = docCookies.getItem("wb_user_token");
    var logoutobj = {
      token: usertoken
    };
    workbench.comm.http.post(logoutobj, "http://localhost:80/api/logout", function(resp) {
      if(resp.result) {
        // TODO UI show output
      } else {
        // TODO UI show gloabl logout failed message box
      }
    });
  },

  authenticate: function(success, failure) {
    var usertoken = docCookies.getItem("wb_user_token");
    var userid = docCookies.getItem("wb_user_id");
    var authobj = {
      token: usertoken,
      id: userid
    };
    workbench.comm.http.post(authobj, "http://localhost:80/api/authenticate", function(resp) {
      if(resp.result) {
        if(!resp.data.hasOwnProperty("token") || !resp.data.hasOwnProperty("id") || resp.data.token.length < 1 || resp.data.id.length < 1) {
          docCookies.removeItem("wb_user_token");
          docCookies.removeItem("wb_user_id");
          docCookies.removeItem("wb_user_name");
          if(typeof failure != "undefined")
            failure();
          return;
        } else {
          if(typeof success != "undefined")
            success();
          return;
        }
      } else {
        docCookies.removeItem("wb_user_token");
        docCookies.removeItem("wb_user_id");
        docCookies.removeItem("wb_user_name");
        if(typeof failure != "undefined")
          failure();
      }
    });
  },

  register: function(user, pass, mail, ui) {
    var regobj = {
      username: user,
      password: pass,
      mail: mail
    };
    if(this.timeout)
      return;
    this.timeout = true;
    setTimeout(function() { workbench.auth.timeout = false; }, this.timeoutTime);
    if(workbench.comm.http.ajaxProgress)
      return;
    var showUI = this.register.arguments.length > 2;
    var errors = [];
    if(user.length > 32 || user.length < 1) {
      errors.push("Username must be between 1 and 32 characters long");
    }
    if(pass.length > 256 || pass.length < 7)
      errors.push("Password must be between 7 and 256 characters long");
    if(mail.length < 6 || mail.length > 254)
      errors.push("Email must be between 6 and 254 characters long");
    if(showUI)
      workbench.ui.popup.register.showLoad(150);
    if(errors.length > 0) {
      if(showUI) {
        var err = "";
        if(errors.length > 1) {
          err = 'Multiple Errors<br /><br /><div class="clear"></div><ul>';
          for(var i=0;i < errors.length;i++) {
            err = err + "<li>" + errors[i] + "</li>";
          }
          err = err + "</ul>";
        }
        else
          err = errors[0];
        workbench.ui.popup.register.hideLoad();
        workbench.ui.popup.register.showError(err);
      }
      return;
    }
    workbench.comm.http.post(regobj, "http://localhost:80/api/register", function(resp) {
      if(resp.result) {
        if(resp.data.hasOwnProperty("success") && resp.data.success == false) {
          docCookies.setItem("wb_user_token", resp.data.token, Infinity);
          docCookies.setItem("wb_user_id", resp.data.agent.id, Infinity);
          docCookies.setItem("wb_user_name", resp.data.agent.user, Infinity);
          workbench.auth.status = true;
          workbench.ui.popup.register.success();
          workbench.bench.load();
        } else if(resp.data.hasOwnProperty("success") && resp.data.success == false && resp.data.hasOwnProperty("error")) {
          workbench.ui.popup.register.hideLoad();
          workbench.ui.popup.register.showError(resp.data.error);
        } else {
          workbench.ui.popup.register.hideLoad();
          workbench.ui.popup.register.showError("Malformed response; try again");
        }
      } else {
        workbench.ui.popup.register.hideLoad();
        workbench.ui.popup.register.showError("HTTP Error, check your connection");
      }
    });
  }
};

workbench.bench = {
  benchSelect: function() { // Show bench selection screen
    var userid = docCookies.getItem("wb_user_id");
    var usertoken = docCookies.getItem("wb_user_token");
    if(userid == null || usertoken == null) {
     workbench.auth.authenticate(function() { workbench.bench.benchSelect(); }, function() { workbench.ui.popup.login.show(); });
     console.log("Failed to retrieve userid and usertoken for loading available benches");
     return;
   }
    var reqobj = {
      agent: {
        id: userid,
        token: usertoken
      }
    };
    workbench.comm.http.post(reqobj, "http://localhost:80/api/user", function(resp) {
      if(resp.result) {
        if(resp.data.hasOwnProperty("email")) {
          if(!(resp.data.hasOwnProperty("owner")) || !(resp.data.hasOwnProperty("member")) || resp.data.owner.length < 1 || resp.data.member.length < 1) {
            workbench.ui.popup.benchselect.showNoBenches();
            return;
          }
          var benches = [];
          for(var i=0;i<resp.data.owner.length;i++) {
            benches.push(resp.data.owner[i]);
          }
          for(var i=0;i<resp.data.member.length;i++) {
            benches.push(resp.data.member[i]);
          }
          workbench.ui.popup.benchselect.showBenches(benches);
        } else {
          workbench.auth.authenticate(function() { workbench.bench.benchSelect(); }, function() { workbench.ui.popup.login.show(150); });
        }
      } else {
        workbench.ui.popup.errorbox.showError("Unable to contact server to retrieve available user benches. Check your connection and try reloading the page.", "HTTP Error");

        return;
      }
    });
  },

  load: function() { // TODO !! IMPLEMENTATION !!
    return;
  }
};

workbench.comm = {
  http: {
    restTarget: "http://api.workbench.online/", // REST API target (base URI), not currently used
    ajaxProgress: false, // Whether or not there is currently an AJAX request in progress
    post: function(data, target, callback) { // Make a REST POST request, currently assumed to be in JSON format
      this.ajaxProgress = true;
      try {
        $.post(target, data, null, "json")
        .done(function(data, status, xhr) { // Success
          callback({
            result: true,
            response: status,
            data: data,
            jqxhr: xhr
          });
        })
        .fail(function(xhr, status, error) { // Failure
          callback({
            result: false,
            response: status,
            error: error,
            jqxhr: xhr,
            errorobj: null
          });
        })
        .always(function() {
          workbench.comm.http.ajaxProgress = false;
        });
      } catch(err) {
        workbench.comm.http.ajaxProgress = false;
        callback({
          result: false,
          response: null,
          error: null,
          jqxhr: null,
          errorobj: err
        });
      }
    }
  },

  websocket: {
    websocket: undefined, // Websocket object, not for direct use outside this namespace
    wsTarget: "ws://localhost:80/" // TODO: Placeholder value for testing, repleace with implementation
  }
};

workbench.core = {
  version: "0.0.3",
  initialize: function() {
    var wait = 3000;
    /* TODO Hash change detection. This is not a top priority, and currently used script is too old. */
    if(!(location.hash == "#nointro")) {
      workbench.ui.popup.intro.showTime(wait);
      setTimeout(workbench.auth.authenticate(function() { workbench.bench.benchSelect(); }, function() { setTimeout(function() { workbench.ui.popup.login.show(750) }, wait + 750); }), wait);
    } else {
      workbench.ui.popup.intro.hide();
      workbench.auth.authenticate(function() { workbench.bench.benchSelect(); }, function() { workbench.ui.popup.login.show(750); });
    }
    console.log("Started Successfully!");
  }
};

workbench.user = {
  getName: function() {
    if(docCookies.getItem("wb_user_name") != null)
      return docCookies.getItem("wb_user_name");
    else
      return "";
  }
};

workbench.ui = {
  /*visibility: {
    loading: false,
    about: false,
    intro: false,
    login: false,
    register: false,
    changepass: false,
    options: false,
  },*/


  popup: {

    defaultDuration: 750, // Default number of msecs that a popup transition should take

    // This is the base popup object. All popups extend this object. Calling functions on this object will do nothing.
    popupbase: {
      visibility: false,
      timer: null,
      selector: "",
      show: function(duration, callback) {
        if(typeof duration == "undefined")
          $(this.selector).css("display", "block");
        else {
          if(typeof callback == "function")
            $(this.selector).fadeIn(duration, callback);
          else
            $(this.selector).fadeIn(duration);
        }
        this.visibility = true;
        return this;
      },

      hide: function(duration, callback) {
        if(typeof duration == "undefined")
          $(this.selector).css("display", "none");
        else {
          if(typeof callback == "function")
            $(this.selector).fadeOut(duration, callback);
          else
            $(this.selector).fadeOut(duration);
        }
        this.visibility = false;
        return this;
      }
    },

    // This is the authbox popup object. All authbox popups extend this object.
    authboxbase: {
      showLoad: function(duration) {
        if(typeof duration != "undefined")
          $(this.selector + " .formcover").fadeIn(duration);
        else
          $(this.selector + " .formcover").css("display", "block");
      },
      hideLoad: function(duration) {
        if(typeof duration != "undefined")
          $(this.selector + " .formcover").fadeOut(duration);
        else
          $(this.selector + " .formcover").css("display", "none");
      }
    },

    // This is the selection menu popup object. All selection menu popups extend this object.
    selectmenubase: {
      sizeAdjust: function(nothide) {
        this.show();
        var realheight = $(this.selector + " > .inner").height();
        if(typeof nothide == "undefined")
          this.hide();
        $(this.selector).height(realheight + 50);
      }
    },

    attachListeners: function() {
      $("#login_submit").click(function(event) {
        event.preventDefault();
        $("#loginform").submit();
      });

      $("#loginform").submit(function(event) {
        event.preventDefault();
        workbench.auth.login($("#login_user").val(), $("#login_pass").val(), true);
      });

      $("#login_forgotlink").click(function(event) {
        event.preventDefault();
        workbench.ui.popup.login.hide(500, function() {
          workbench.ui.popup.forgotpass.sizeAdjust();
          workbench.ui.popup.forgotpass.show(500);
        });
      });

      $("#login_registerlink").click(function(event) {
        event.preventDefault();
        workbench.ui.popup.login.hide(500, function() {
          workbench.ui.popup.register.sizeAdjust();
          workbench.ui.popup.register.show(500);
        });
      });

      $("#forgotpass_backlink").click(function(event) {
        event.preventDefault();
        workbench.ui.popup.forgotpass.hide(500, function() {
          workbench.ui.popup.login.sizeAdjust();
          workbench.ui.popup.login.show(500);
        });
      });

      $("#register_backlink").click(function(event) {
        event.preventDefault();
        workbench.ui.popup.register.hide(500, function() {
          workbench.ui.popup.login.sizeAdjust();
          workbench.ui.popup.login.show(500);
        });
      });

      $("#registerform").submit(function(event) {
        event.preventDefault();
        if($("#register_pass").val() == $("#register_pass2").val())
          workbench.auth.register($("#register_user").val(), $("#register_pass").val(), $("#register_email").val(), true);
        else {
          workbench.ui.popup.register.hideLoad();
          workbench.ui.popup.register.showError("Passwords do not match");
        }
      });

      $("#register_submit").click(function(event) {
        event.preventDefault();
        $("#registerform").submit();
      });
    },

    initialize: function() {
      // Define UI Objects

      // Popup back cover object
      this.cover = $.extend({}, this.popupbase, {
        visibility: true,
        selector: "#backcover"
      });

      // Intro screen object
      this.intro = $.extend({}, this.popupbase, {
        visibility: true,
        selector: "#intro",
        showTime: function(time) {
          this.timer = setTimeout(function() { workbench.ui.popup.intro.hide(750); }, time);
          return this;
        },
        showButtons: function() {
          $(this.selector + " .buttons").height($(this.selector).height()-100-($(this.selector + " .subtext").height() + $(this.selector + " .headertext").height()));
          $(this.selector + " .buttons").css({
            "margin":"50px auto",
            "display":"block",
            "top":"50px"
          });
        },
        hideButtons: function() {
          $(this.selector + " .buttons").css("display", "none");
        }
      });

      // AUTH - Login Screen Object
      this.login = $.extend({}, this.popupbase, this.authboxbase, {
        selector: "#login",
        sizeAdjust: function(nothide) {
          this.show();
          var realheight = $(this.selector + " > .inner").height();
          if(typeof nothide == "undefined")
            this.hide();
          $(this.selector).height(realheight + 50);
        },
        showError: function(message) {
          if($(this.selector + " .errbox").length == 0) {
            $(this.selector + " h1").after('<div class="errbox error"><i class="erricon fa fa-times-circle fa-2x"></i><p>' + message + '</p><div class="clear"></div></div>');
          } else {
            if(!($(this.selector + " .errbox").hasClass("error")))
              $(this.selector + " .errbox").attr("class", "errbox error");
            $(this.selector + " .errbox").html('<i class="erricon fa fa-times-circle fa-2x"></i><p>' + message + '</p><div class="clear"></div>');
          }
          $(this.selector + " .errbox").click(function(event) {
            var cur = $(event.delegateTarget).parent().parent().attr("id");
            if(cur == "login")
              workbench.ui.popup.login.hideError();
            else if(cur == "register")
              workbench.ui.popup.register.hideError();
            else
              return;
          });
          this.sizeAdjust(true);
        },
        hideError: function() {
          $(this.selector + " .errbox").remove();
          this.sizeAdjust(true);
        },
        success: function(callback) {
          this.hide(150, callback);
        }
      });

      // AUTH - Forgot Password Screen Object
      this.forgotpass = $.extend({}, this.login, {
        selector: "#forgotpass"
      });

      // AUTH - Registration Screen Object
      this.register = $.extend({}, this.login, {
        selector: "#register"
      });

      // BENCHSELECT - Bench Selection Menu
      this.benchselect = $.extend({}, this.popupbase, this.selectmenubase, {
        selector: "#bench_select",
        showNoBenches: function() {
          $(this.selector + " .nobenches").css("display", "block");
        },
        hideNoBenches: function() {
          $(this.selector + " .nobenches").css("display", "none");
        },
        showBenches: function(benches) {
          if(benches.length < 1) {
            console.error("No benches supplied to bench show function");
            return;
          }
          var benchItems;
          for(var i=0; i<benches.length; i++) { // Generate HTML for each bench
            if(!benches[i].hasOwnProperty("id"))
              continue;
            var itemStr;
            itemstr = '<div class="item left" id="' + benches[i].id + '">';
            if(benches[i].hasOwnProperty("preview"))
              itemstr = itemstr + '<div class="image"><div class="color" style="background: ' + benches[i].preview + ';"></div><div class="cover"></div></div>';
            else
              itemstr = itemstr + '<div class="image"><div class="color" style="background: LightSkyBlue;"></div><div class="cover"></div></div>';
            itemstr = itemstr + '<div class="subtext">';
            if(benches[i].hasOwnProperty("title") && benches[i].title.length > 0)
              itemstr = itemstr + '<div class="left"><p class="description">' + benches[i].title + '</p></div>';
            else
              itemstr = itemstr + '<div class="left"><p class="description">Untitled Bench</p></div>';
            itemstr = itemstr + '<div class="right"><div class="profile_image"><div class="color" style="background: #420420"></div></div></div><div class="clear"></div></div>'; // TODO: Get user image? With user?
          }
          console.log(benchItems);
        }
      });

      // ERRORBOX - Error box
      this.errorbox = $.extend({}, this.popupbase, {
        selector: ".error_box",
        sizeAdjust: function(nothide) {
          this.show();
          var realheight = $(this.selector + " > .inner").height();
          if(typeof nothide == "undefined")
            this.hide();
          $(this.selector).height(realheight + 50);
        },
        showError: function(error, title) {
          if(typeof error == "undefined")
            error = "An unspecified error occured.";
          if(typeof title == "undefined")
            title = "Error";
          if($(this.selector).length == 0) {
            $("body").append('<div class="error_box error"><div class="inner"><div class="left"><div class="icon"><i class="fa fa-times-circle fa-4x"></i></div></div><div class="left"><div class="error_title"><h1>' + title + '</h1></div></div><div class="clear"></div><div class="error_desc"><p>' + error + '</p></div><button type="button" class="closewindow">Close</button><div class="clear"></div></div></div>');
          } else {
            if(!($(this.selector).hasClass("error")))
              $(this.selector).attr("class", "error_box error");
            $(this.selector).html('<div class="inner"><div class="left"><div class="icon"><i class="fa fa-times-circle fa-4x"></i></div></div><div class="left"><div class="error_title"><h1>' + title + '</h1></div></div><div class="clear"></div><div class="error_desc"><p>' + error + '</p></div><button type="button" class="closewindow">Close</button><div class="clear"></div></div>');
          }
          this.sizeAdjust(true);
          $(this.selector + " button").click(function(event) {
            workbench.ui.popup.errorbox.closeError();
          });
        },
        closeError: function() {
          $(this.selector).remove()
        }
      });

      // Attach listeners for UI updates
      this.attachListeners();
    }
  },

  // This creates all of the UI overlay and tool objects, as they require workbench.ui to be completed before implementation can occur.
  initialize: function() {
    this.popup.initialize();
    this.adjustSizes();
  },

  adjustSizes: function() {
    this.popup.login.sizeAdjust();
  }
}

workbench.util = {
  timer: {
    timers: [],
    getTimer: function(id) {
      if(this.timers[id])
        return this.timers[id];
      else
        return false;
    },
    cancelTimer: function(id) {
      if(this.timers[id])
        window.clearTimeout(id);
      else
        return false;
    },
    setTimer: function(time, callback) {
      try {
        var t = window.setTimeout(callback, time);
        this.timers.push(t);
        return true;
      } catch(e) {
        return false;
      }
    }
  },

  urlhash: {
    // TODO Implementation; this feature is not currently top priority.
  }
};
$(document).ready(function() { workbench.ui.initialize(); });
$(window).load(function() { workbench.core.initialize(); });
