/* Workbench (c) 2016 Jeremy Teissler, Ethan Crowder, All Rights Reserved */
// TODO: Comprehensive JSDOC Documentation & Minification
$ = jQuery;

/**
 * Main Workbench object (initalizes & contains fancy variables)
 */
function Workbench()
{
  var websocket = undefined; // WebsSocket object, created during initialization
  var wsTarget = "ws://localhost"; // WebSocket target host; needs to be changed with implementation
  var restTarget = "http://api.workbench.online/"; // RESTful API base URI

  this.loadEvent = function()
  {
    workbench.initialize();
    console.log(this);
  }

  this.initialize = function()
  {
    try
    {
      websocket = new WebSocket(wsTarget);
    } catch(error) {
      console.error(error);
    }
  }
}

var workbench = workbench || new Workbench();
$(window).load(workbench.loadEvent);
