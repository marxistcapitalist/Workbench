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
      workbench.ui.popup.login.showLoad(250);
    workbench.comm.http.post(loginobj, "login", function(resp) {
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
    if(docCookies.getItem("wb_user_token") === null) {
      location.assign("index.html#nointro");
      location.reload();
      return;
    }
    docCookies.removeItem("wb_user_token");
    docCookies.removeItem("wb_user_id");
    docCookies.removeItem("wb_user_name");
    location.assign("index.html#nointro");
    location.reload();
  },

  globalLogout: function() { // Logs the user out of their GLOBAL session, by invalidating their token
    if(docCookies.getItem("wb_user_token") === null)
      return;
    var usertoken = docCookies.getItem("wb_user_token");
    var logoutobj = {
      token: usertoken
    };
    workbench.comm.http.post(logoutobj, "logout", function(resp) {
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
    if(usertoken === null || userid === null) {
      failure();
      return;
    }
    var authobj = {
      token: usertoken,
      id: userid
    };
    workbench.comm.http.post(authobj, "authenticate", function(resp) {
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
      email: mail
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
      workbench.ui.popup.register.showLoad(250);
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
    workbench.comm.http.post(regobj, "register", function(resp) {
      if(resp.result) {
        if(resp.data.hasOwnProperty("success") && resp.data.success == true) {
          docCookies.setItem("wb_user_token", resp.data.token, Infinity);
          docCookies.setItem("wb_user_id", resp.data.agent.id, Infinity);
          docCookies.setItem("wb_user_name", resp.data.agent.user, Infinity);
          workbench.auth.status = true;
          workbench.ui.popup.register.success();
          workbench.bench.benchSelect();
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
  },

  validateCookies: function() {
    if(docCookies.getItem("wb_user_id") === null || docCookies.getItem("wb_user_token") === null)
      workbench.auth.logout();
    else
      return;
  }
};

workbench.bench = {

  // Whether or not a bench is loaded
  loaded: false,

  // NODE STORAGE
  nodes: [],
  nodeOrder: [],

  // Bench Properties - Every bench has all of these
  id: "0",
  title: "Untitled Bench",
  owner: {
    id: 0000000000,
    user: "User"
  },
  preview: "",
  background: "#CCC",
  members: [],
  created: "",

  // Node Movement
  dragging: false,

  benchSelect: function() { // Show bench selection screen
    var userid = docCookies.getItem("wb_user_id");
    var usertoken = docCookies.getItem("wb_user_token");
    if(userid === null || usertoken === null) {
     workbench.auth.authenticate(function() { workbench.bench.benchSelect(250); }, function() { workbench.ui.popup.login.show(); });
     if(workbench.core.debug)
      console.log("Failed to retrieve userid and usertoken for loading available benches");
     return;
  }
    var reqobj = {
      agent: {
        id: userid,
        token: usertoken
      }
    };
    workbench.comm.http.post(reqobj, "user", function(resp) {
      if(resp.result) {
        if(workbench.core.debug)
          console.log(resp.data.hasOwnProperty("email"));
        if(resp.data.hasOwnProperty("email")) {
          if(workbench.core.debug) {
            console.log("Owner: " + resp.data.hasOwnProperty("owner"));
            console.log("Member: " + resp.data.hasOwnProperty("member"));
            console.log("Owner Length: " + resp.data.owner.length);
            console.log("Member Length: " + resp.data.member.length);
          }
          if((!(resp.data.hasOwnProperty("owner")) || !(resp.data.hasOwnProperty("member"))) || (resp.data.owner.length < 1 && resp.data.member.length < 1)) {
            workbench.ui.popup.benchselect.showNoBenches();
            workbench.ui.popup.benchselect.sizeAdjust();
            workbench.ui.popup.benchselect.show(250);
            return;
          }
          var benches = [];
          for(var i=0;i<resp.data.owner.length;i++) {
            benches.push(resp.data.owner[i]);
          }
          for(var i=0;i<resp.data.member.length;i++) {
            benches.push(resp.data.member[i]);
          }
          if(workbench.core.debug)
            console.log(benches);
          workbench.ui.popup.benchselect.showBenches(benches);
          workbench.ui.popup.benchselect.sizeAdjust();
          workbench.ui.popup.benchselect.show(250);
        } else {
          workbench.auth.authenticate(function() { workbench.bench.benchSelect(); }, function() { workbench.ui.popup.login.show(250); });
        }
      } else {
        workbench.ui.popup.errorbox.showError("Unable to contact server to retrieve available user benches. Check your connection and try reloading the page.", "HTTP Error");
        return;
      }
    });
  },

  loadBench: function(benchid) {
    if(typeof benchid != "number")
      return;
    var userid = docCookies.getItem("wb_user_id");
    var usertoken = docCookies.getItem("wb_user_token");
    if(userid === null || usertoken === null || userid.length < 1 || usertoken.length < 1) {
      authenticate(function() { workbench.bench.logout(); }, function() { workbench.ui.popup.login.show(250); });
      return;
    }
    var reqobj = {
      id: benchid,
      verbosity: "high",
      agent: {
        id: userid,
        token: usertoken
      }
    };
    workbench.comm.http.post(reqobj, "bench", function(resp) {
      if(resp.result) {
        if(!resp.data.hasOwnProperty("id") || !resp.data.hasOwnProperty("title") || !resp.data.hasOwnProperty("owner") || !resp.data.hasOwnProperty("preview") || !resp.data.hasOwnProperty("background")) {
          workbench.ui.popup.errorbox.showError("An unexpected, malformed response was received from the bench API endpoint.", "Malformed Response Error");
          return;
        }
        workbench.bench.title = resp.data.title;
        workbench.bench.owner = resp.data.owner;
        workbench.bench.id = resp.data.id;
        workbench.bench.preview = resp.data.preview;
        workbench.bench.background = resp.data.background;
        workbench.bench.created = resp.data.created;
        workbench.bench.members = resp.data.members;
        workbench.bench.nodes = resp.data.nodes;
        console.log(resp.data.nodes);
        workbench.bench.popUI();
        workbench.comm.websocket.open();
        workbench.bench.loaded = true;
      } else {
        workbench.ui.popup.errorbox.showError("An HTTP error occured while attempting to load the bench, check your connection. See console for details", "HTTP Error");
        return;
      }
    });
  },

  createBench: function(name, width, height) {
    var userid = docCookies.getItem("wb_user_id");
    var usertoken = docCookies.getItem("wb_user_token");
    if(userid === null || usertoken === null || userid.length < 1 || usertoken.length < 1) {
      authenticate(function() { workbench.bench.logout(); }, function() { workbench.ui.popup.login.show(250); });
      return;
    }
    var reqobj = {
      title: name,
      dimensions: {
        w: width,
        h: height
      },
      agent: {
        id: userid,
        token: usertoken
      }
    };
    workbench.comm.http.post(reqobj, "create", function(resp) {
      if(resp.result) {
        if(!resp.data.hasOwnProperty("id")) {
          workbench.auth.logout();
          return;
        }
        workbench.ui.popup.closeAllPopups();
        workbench.bench.loadBench(resp.data.id);
      } else {
        workbench.ui.popup.errorbox.showError("An HTTP error occured while attempting to load the bench, check your connection. See console for details", "HTTP Error");
        return;
      }
    });
  },

  popUI: function() {
    $("#workbench").bind("dragover", function(event) {
      event.preventDefault();
    });
    $("#workbench").bind("drop", function(event) {
      event.preventDefault();
      workbench.bench.dragging = false;
      nodedata = JSON.parse(event.dataTransfer.getData("application/json"));
      node = $("#workbench").children("[data-nodeid='" + nodedata.id + "']");
      $(node).css("left", event.clientX + nodedata.offsetx + "px");
      $(node).css("top", event.clientY + nodedata.offsety + "px");
      workbench.auth.validateCookies();
      reqobj = {
        node: nodedata.id,
        dimensions: {
          x: event.clientX + nodedata.offsetx,
          y: event.clientY + nodedata.offsety
        },
        agent: {
          id: docCookies.getItem("wb_user_id"),
          token: docCookies.getItem("wb_user_token")
        }
      };
      workbench.comm.http.post(reqobj, workbench.bench.id + "/move", function(resp) {
        if(!resp.result || resp.data.length < 1) {
          console.error("Move error: ");
          console.error(resp.error);
        }
      });
      wsobj = {
        type: "move",
        node: nodedata.id,
        dimensions: {
          x: event.clientX + nodedata.offsetx,
          y: event.clientY + nodedata.offsety
        },
        agent: {
          id: docCookies.getItem("wb_user_id"),
          token: docCookies.getItem("wb_user_token")
        }
      };
      console.log("Sending WS JSON: ");
      wsstring = JSON.stringify(wsobj);
      console.log(wsstring);
      workbench.comm.websocket.socket.send(wsstring);
    });
    try {
      workbench.ui.popup.closeAllPopups();
      workbench.ui.popup.cover.hide(150);
      workbench.ui.toolbar.setTitle(workbench.bench.title);
      for(var i=0;i<workbench.bench.nodes.length;i++) {
        processedcontent = "";
        type = workbench.bench.nodes[i].contentType;
        if(type == "text")
          processedcontent = workbench.bench.nodes[i].content;
        else if(type == "image")
          processedcontent = '<img src="' + workbench.bench.nodes[i].content + '" />';
        var nodestr = '<div class="node" draggable="true" data-nodeid="' + workbench.bench.nodes[i].id + '"><div class="title"><span class="titletext">' + workbench.bench.nodes[i].title + '</span></div>' +
        '<div class="content">' + processedcontent + "</div></div>";
        console.log(workbench.bench.nodes[i].content);
        $("#workbench").append(nodestr);
        $("#workbench").children("[data-nodeid='" + workbench.bench.nodes[i].id + "']").css({
          "left": workbench.bench.nodes[i].position.x + "px",
          "top": workbench.bench.nodes[i].position.y + "px",
          "width": workbench.bench.nodes[i].position.w + "px",
          "height": workbench.bench.nodes[i].position.h + "px"
        });
      }
      $(".node").bind("dragstart", function(event) {
        transfer = {
          x: $(this).position().left,
          y: $(this).position().top,
          offsetx: $(this).position().left - event.clientX,
          offsety: $(this).position().top - event.clientY,
          id: $(this).data("nodeid")
        };
        console.log(event);
        transfer = JSON.stringify(transfer);
        event.dataTransfer.setData("application/json", transfer);
        workbench.bench.dragging = true;
        //workbench.bench.moveloop(event);
      });
    } catch(error) {
      console.error("Error while populating bench contents. See below: ")
      console.error(error);
      workbench.ui.popup.errorbox.showError("An error occured while attempting to load bench elements. See console for details.", "Load Error");
    }
  },

  moveloop: function(event) {
    nodedata = JSON.parse(event.dataTransfer.getData("application/json"));
    if(this.dragging) {
      transfer = {
        type: "move",
        node: nodedata.id,
        bench: this.id,
        dimensions: {
          x: nodedata.x,
          y: nodedata.y
        },
        agent: {
          id: docCookies.getItem("wb_user_id"),
          token: docCookies.getItem("wb_user_token")
        }
      };
      workbench.comm.websocket.socket.send(transfer);
      console.log("Sent WS");
      console.log(transfer);
      setTimeout(this.moveLoop, 100);
    } else return;
  },

  webSocketStart: function() {

    workbench.comm.websocket.open();
  },

  // VERIFY CREATE DELETE EDIT MOVE RENAME RESIZE NOTIFY MOD
  inbox: function(message) {
    console.log("Received WS Message: ");
    console.log(message);
    type = message.type;
    if(type == "move") {
      workbench.bench.moveNode(message.node, message.dimensions);
    }
  },

  moveNode: function(nodeid, dimensions) {
    node = $("#workbench").children("[data-nodeid='" + nodeid + "']");
    $(node).css({
      "left": dimensions.x + "px",
      "top": dimensions.y + "px"
    });
  },

  load: function() { // TODO !! IMPLEMENTATION !!
    return;
  }
};

workbench.comm = {
  http: {
    restTarget: "http://workbench.online/api/", // REST API target (base URI), not currently used
    ajaxProgress: false, // Whether or not there is currently an AJAX request in progress
    post: function(data, target, callback) { // Make a REST POST request, currently assumed to be in JSON format
      this.ajaxProgress = true;
      target = this.restTarget + target;
      try {
        var jsonstring = JSON.stringify(data);
        if(workbench.core.debug)
          console.log("Request: " + jsonstring);
        $.ajax({
          type: "POST",
          url: target,
          data: jsonstring,
          dataType: "text",
          contentType: "application/json; charset=UTF-8"
        })
        .done(function(data, status, xhr) { // Success
          data = JSON.parse(data);
          if(workbench.core.debug)
            console.log("Response: ");
            console.log(data);
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
    socket: undefined, // Websocket object, not for direct use outside this namespace
    wsTarget: "ws://workbench.online/api/ws",
    cleanClose: true, // Whether or not the websocket is in a clean close state at the moment. If this is false, and socket closes, the connection unexpectedly closed.

    open: function() {
      workbench.auth.validateCookies();
      try {
        this.socket = new WebSocket(this.wsTarget);
        this.socket.onmessage = this.onmessage;
        this.socket.onerror = this.onerror;
        this.socket.onopen = this.onopen;
        this.socket.onclose = this.onclose;
      } catch(err) {
        workbench.ui.popup.errorbox.showError("Failed to initialize WebSocket, see console for details.", "WebSocket Connection Failure");
      }
    },

    close: function() {
      try {
        this.cleanClose = true;
        this.socket.close();
      } catch(err) {
        console.log("Failed to properly close websocket. It probably wasn't open.");
      }
    },

    onmessage: function(messageevent) {
      try {
        var message = messageevent.data;
        console.log("MESSAGE: " + message);
        var respobj = JSON.parse(message);
        if(!respobj.hasOwnProperty("type") || !respobj.hasOwnProperty("bench") || respobj.type.length < 1 || respobj.bench.length < 1) {
          workbench.ui.popup.errorbox.showError("Received malformed JSON request form server (missing required properties)", "Server Error");
          return;
        }
        workbench.bench.inbox(respobj);
      } catch(error) {
        //console.log(error);
        return;
      }
    },

    onerror: function() {
      console.error("WebSocket Error! AKA something went horribly wrong. Reloading the page...");
      setTimeout(function() { location.replace("index.html#nointro"); }, 3000);
    },

    onopen: function() {
      workbench.auth.validateCookies();
      console.log("WebSocket Opened!");
      workbench.comm.websocket.cleanClose = false;
      try {
        reqobj = {
          type: "verify",
          bench: workbench.bench.id,
          agent: {
            id: docCookies.getItem("wb_user_id"),
            token: docCookies.getItem("wb_user_token")
          }
        };
        reqstring = JSON.stringify(reqobj);
        if(workbench.core.debug)
          console.log(reqstring);
        workbench.comm.websocket.socket.send(reqstring);
      } catch(err) {

      }
    },

    onclose: function() {
      if(workbench.comm.websocket.cleanClose) {
        console.log("WebSocket gracefully, cleanly closed.");
      } else {
        console.log("WebSocket uncleanly closed, will attempt to re-authenticate...");
        workbench.auth.authenticate(function() { workbench.comm.websocket.open(); }, function() { workbench.ui.popup.login.show(250); })
      }
    }
  }
};

workbench.core = {
  version: "Build 10",
  debug: true,
  initialize: function() {
    jQuery.event.props.push('dataTransfer'); // TODO MAJOR workaround as jQuery normally doesn't record dataTransfer. May break in future jQuery verions, although they'll hopefually add it before then.
    var wait = 3000;
    /* TODO Hash change detection. This is not a top priority, and currently used script is too old. */
    if(!(location.hash == "#nointro")) {
      workbench.ui.popup.intro.show(750);
      workbench.auth.authenticate(function() { setTimeout(function() { workbench.ui.popup.intro.hide(750, function() { workbench.bench.benchSelect(); }); }, 1000);  }, function() { setTimeout(function() { workbench.ui.popup.intro.hide(750, function() { workbench.ui.popup.login.show(750); }); }, 3000); });
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

  toolbar: {

    menustates: {
      main: false
    },

    setTitle: function(title) {
      $("#toolbar .left h1").html(title);
      this.adjustCenter();
    },

    initialize: function() {
      this.attachListeners();
    },

    adjustCenter: function() {
      var marginleft = $("#toolbar .left").outerWidth(true);
      var marginright = $("#toolbar .right").outerWidth(true);
      $("#toolbar .center").css("margin", "auto " + marginright + "px auto " + marginleft + "px");
    },

    attachListeners: function() {
      $("#toolbar_maindropdown_button").hover(function() {
        if(!workbench.ui.toolbar.menustates.main) {
          workbench.ui.toolbar.menustates.main = true;
          $(this).children("i").stop();
          $(this).children("i").animate({"margin-top": "3px"}, 150);
          $("#toolbar_maindropdown").stop();
          $("#toolbar_maindropdown").fadeIn(150);
        }
      }, function() {
        if(!workbench.ui.toolbar.menustates.main) {
          workbench.ui.toolbar.menustates.main = false;
          $(this).children("i").stop();
          $(this).children("i").animate({"margin-top":"0"}, 150);
          if(!$("#toolbar_maindropdown").is(":hover")) {
            $("#toolbar_maindropdown").stop();
            $("#toolbar_maindropdown").fadeOut(150);
          }
        }
      });

      $("#toolbar_maindropdown").mouseleave(function() {
        workbench.ui.toolbar.menustates.main = false
        $(this).stop();
        $(this).fadeOut(150);
      });

      $(".link_createnode").click(function(event) {
        event.preventDefault();
        workbench.ui.popup.createnode.sizeAdjust(true);
      });

      $("#createnode_contenttype").change(function() {
        if($(this).val() == "text")
          $("#nodecontentbox").html('<textarea id="createnode_content_text" cols="50" rows="6"></textarea>');
        else if($(this).val() == "image")
          $("#nodecontentbox").html('<input type="text" name="createnode_content_image" id="createnode_content_image" placeholder="Image URL" />');
      });

      $("#createnodeform").submit(function(event) { // TODO Error Box and error checking
        event.preventDefault();
        contenttype = $("#createnode_contenttype").val();
        contentdata = "";
        if(contenttype == "text")
          contentdata = $("#createnode_content_text").val();
        else if(contenttype == "image")
          contentdata = $("#createnode_content_image").val();
        try {
          reqobj = {
            dimensions: {
              x: 100,
              y: 100,
              w: $("#createnode_width").val(),
              h: $("#createnode_height").val()
            },
            content: {
              title: $("#createnode_title").val(),
              type: contenttype,
              data: contentdata
            },
            agent: {
              id: docCookies.getItem("wb_user_id"),
              token: docCookies.getItem("wb_user_token")
            }
          };
          workbench.comm.http.post(reqobj, workbench.bench.id + "/create", function(resp) {
            if(!resp.result || resp.data.length < 1) {
              workbench.ui.popup.errorbox.showError("Failed to create bench node due to invalid response. See console for details", "Card create error")
            }
            workbench.ui.popup.createnode.hide(150);
            workbench.ui.popup.cover.hide(150);
            return;
          });
        } catch(error) {
          console.error(error);
          workbench.ui.popup.errorbox.showError("Failed to create new card. See console for details", "Card create error");
        }
      });
    }
  },

  popup: {

    defaultDuration: 750, // Default number of msecs that a popup transition should take
    openPopups: [], // Popups that are currently open

    closeAllPopups: function() {
      for(var i=0;i<this.openPopups.length;i++) {
        this.openPopups[i].hide(250);
      }
    },

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
        if(!workbench.ui.popup.openPopups.indexOf(this) > -1)
          workbench.ui.popup.openPopups.push(this);
        if(!workbench.ui.popup.cover.visibility)
          workbench.ui.popup.cover.show(150);
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
        var thisindex = workbench.ui.popup.openPopups.indexOf(this);
        if(thisindex > -1)
          workbench.ui.popup.openPopups.splice(thisindex, 1);
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

    // This is the adjustable base object. It adds one function, sizeadjust, to any object that extends it.
    adjustable: {
      sizeAdjust: function(nothide) {
        this.show();
        var realheight = $(this.selector + " > .inner").height();
        if(typeof nothide == "undefined")
          this.hide();
        $(this.selector).height(realheight + 50);
      }
    },

    attachListeners: function() {

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

      $("#createbenchform").submit(function(event) {
        event.preventDefault();
        if($("#createbench_title").val().length < 1 || $("#createbench_title").val().length > 256) {
          workbench.ui.popup.createbench.hideLoad();
          workbench.ui.popup.createbench.showError("Title must be between 1 and 256 characters long");
          return;
        }
        workbench.bench.createBench($("#createbench_title").val(), 3840, 2160);
      }),

      /* Better Spec */
      $(".link_benchselect").click(function(event) {
        event.preventDefault();
        workbench.ui.popup.closeAllPopups()
        workbench.bench.benchSelect();
      });

      $(".link_logout").click(function(event) {
        event.preventDefault();
        workbench.auth.logout();
      });

      $(".link_createbench").click(function(event) {
        event.preventDefault();
        workbench.ui.popup.closeAllPopups();
        workbench.ui.popup.createbench.sizeAdjust();
        workbench.ui.popup.createbench.show(250);
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
          this.hide(250, callback);
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
          if(workbench.core.debug)
            console.log("Showing no benches...");
        },
        hideNoBenches: function() {
          $(this.selector + " .nobenches").css("display", "none");
        },
        showBenches: function(benches) {
          if(benches.length < 1) {
            console.error("No benches supplied to bench show function");
            return;
          }
          if(workbench.core.debug)
            console.log("Went somewhere");
          $("#bench_select .menu_content .item").remove();
          var benchItems = [];
          for(var i=0; i<benches.length; i++) { // Generate HTML for each bench
            if(!benches[i].hasOwnProperty("id"))
              continue;
            var itemStr;
            itemstr = '<div class="item left" data-benchid="' + benches[i].id + '">';
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
            benchItems.push(itemstr);
          }
          for(i=0;i<benchItems.length;i++) {
            $("#bench_select .menu_content").append(benchItems[i]);
          }
          $("#bench_select .menu_content").append('<div class="clear"></div>');
          $("#bench_select .menu_content .item").click(function(event) {
            event.preventDefault();
            console.log($(this).data("benchid"));
            workbench.ui.popup.closeAllPopups();
            workbench.bench.loadBench($(this).data("benchid"));
          });
          console.log(benchItems);
        }
      });

      // ERRORBOX - Error box
      this.errorbox = $.extend({}, this.popupbase, this.adjustable, {
        selector: ".error_box",
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

      // PROPERTYBOX - Create bench properties menu
      this.createbench = $.extend({}, this.popupbase, this.adjustable, {
        selector: "#create_bench"
      });

      // PROPERTYBOX - Create node properties menu
      this.createnode = $.extend({}, this.popupbase, this.adjustable, {
        selector: "#create_node"
      });

      // Attach listeners for UI updates
      this.attachListeners();
    }
  },

  // This creates all of the UI overlay and tool objects, as they require workbench.ui to be completed before implementation can occur.
  initialize: function() {
    this.popup.initialize();
    this.toolbar.initialize();
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
