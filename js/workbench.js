/* Workbench (c) 2016 Jeremy Teissler, Ethan Crowder, All Rights Reserved */
// TODO: Comprehensive JSDOC Documentation & Minification
$ = jQuery;

/**
 * Main Workbench object (initalizes, contains fancy variables, contains misc functions)
 */
function Workbench()
{
  var websocket = undefined; // WebsSocket object, created during initialization
  var wsTarget = "ws://localhost"; // WebSocket target host; needs to be changed with implementation
  var restTarget = "http://api.workbench.online/"; // RESTful API base URI

  try {
    websocket = new WebSocket(wsTarget);
  } catch(error) {
    console.error(error);
  }

  this.getRestTarget = function() { return restTarget; };
  this.getWebSocket = function() { return websocket; };
  this.getWebSocketTarget = function() { return wsTarget; };
}

// Define Workbench core/misc variables
Workbench.prototype.loading = false;

// Initialization Function
Workbench.prototype.initialize = function()
{
  setTimeout(wb.ui.hideAbout, 3000);
  this.attachListeners();

}

Workbench.prototype.attachListeners = function()
{
  $("#about").click(this.ui.hideAbout);
}

Workbench.prototype.ui = {};

Workbench.prototype.ui.showAbout = function()
{

}

Workbench.prototype.ui.hideAbout = function()
{
  $("#about").fadeOut(750);
}

var wb = wb || new Workbench();
$(window).load(function() { wb.initialize(); });
// $(document).ready(function() { wb.attachListeners; });
