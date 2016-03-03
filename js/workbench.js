/* Workbench (c) 2016 Jeremy Teissler, Ethan Crowder, All Rights Reserved */
// TODO: Comprehensive JSDOC Documentation & Minification
$ = jQuery;

var workbench = workbench || {};
workbench.auth = workbench.auth || {};
workbench.user = workbench.user || {};
workbench.bench = workbench.bench || {};
workbench.core = workbench.core || {};
workbench.comm = workbench.comm || {};
workbench.ui = workbench.ui || {};
workbench.util = workbench.util || {};

workbench.auth = {
  login: function(user, password) {

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
  websocket: undefined, // Websocket object, not for direct use outside this namespace
  wsTarget: "ws://localhost", // TODO: Placeholder value for testing, repleace with implementation
  restTarget: "http://api.workbench.online/", // REST API target (base URI)

  post: function() {

  }
};

workbench.core = {
  initialize: function() {
    console.log($(this).parent());
    workbench.ui.loading.showTime(3000);
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

  loading: {
    visibility: false,
    timer: undefined,
    show: function(duration) {
      this.visibility = true;
      if(typeof duration == "undefined")
        $("#about").css("display", "block");
      else
        $("#about").fadeIn(duration);
    },
    hide: function(duration) {
      this.visibility = false;
      if(typeof duration == "undefined")
        $("#about").css("display", "none");
      else
        $("#about").fadeOut(duration);
    },
    showTime: function(time) {
      workbench.ui.loading.timer = setTimeout(function() { workbench.ui.loading.hide(750); }, time);
    }
  }
}

workbench.util = {

};

$(window).load(function() { workbench.core.initialize(); });
