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
  status: false,
  login: function(username, password) {
    var loginobj = {
      loginkey: username,
      password: password
    };
    workbench.comm.http.post(loginobj, "http://syrukide.net/rest-test/post.php?req=login", function(resp) {
      console.log(resp.result);
      if(!resp.result)
        console.log("Error was: " + resp.error);
      console.log(resp.jqxhr);
    });
  },
  logout: function() {

  },
  authenticate: function() {
    
  }
  // TODO: Put register in here or user? Put in user for now.
};

workbench.bench = {

};

workbench.comm = {
  http: {
    restTarget: "http://api.workbench.online/", // REST API target (base URI)
    post: function(data, target, callback) { // Make a REST POST request, currently assumed to be in JSON format
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
          jqxhr: xhr
        });
      });
    },

    get: function() {
      // TODO: Implementation, or removal if not used
    }
  },

  websocket: {
    websocket: undefined, // Websocket object, not for direct use outside this namespace
    wsTarget: "ws://localhost", // TODO: Placeholder value for testing, repleace with implementation
  }
};

workbench.core = {
  version: "0.0.3";
  initialize: function() {
    var wait = 3000;
    workbench.ui.intro.showTime(wait);
    setTimeout(workbehch.auth.authenticate(), wait);
    console.log("Started Successfully!");
  }
};

workbench.user = {

};

workbench.ui = {
  visibility: {
    loading: false,
    about: false,
    intro: false,
    login: false,
    register: false,
    changepass: false,
    options: false,
  },

  intro: {
    visibility: false,
    timer: undefined,
    show: function(duration) {
      this.visibility = true;
      if(typeof duration == "undefined")
        $("#about").css("display", "block");
      else
        $("#about").fadeIn(duration);
      return this;
    },
    hide: function(duration) {
      this.visibility = false;
      if(typeof duration == "undefined")
        $("#about").css("display", "none");
      else
        $("#about").fadeOut(duration);
      return this;
    },
    showTime: function(time) {
      this.timer = setTimeout(function() { workbench.ui.intro.hide(750); }, time);
      return this;
    }
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
  }
};

$(window).load(function() { workbench.core.initialize(); });
