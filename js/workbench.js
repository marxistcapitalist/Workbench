/* Workbench (c) 2016 Jeremy Teissler, Ethan Crowder, All Rights Reserved */
// TODO: Comprehensive JSDOC Documentation

/**
 * Main Workbench object (controls all other components of the application)
 */
var Workbench = Workbench || {};

// Special thanks to https://addyosmani.com/blog/essential-js-namespacing/



/*
(Ignore this)

CONTENT as passed into the Window is an object stuctured as the following:
{
  type:"video" // The type of window; can be value "text", "video", "audio", "document", etc. Is always [DEFAULT] for a default window. (Might get rid of in favor of just mime)
  mime:"video/mp4" // The exact MIME of the content. May be rejected by window creation method in certain circumstances. Is always an empty string for a default window.
  uuid:"a93jkd03815glpe8jdo0" // UUID of the window/node
  title:"TestWindow" // (OPTIONAL) The title of the window to be created. If not specified, will be created based on file name.
  text:"asdfjkl;" // (OPTIONAL) For text windows with simple editable text, the text contained in the window
  url:"http://graphica.live/test.mp4" // (OPTIONAL) For simple embeds, links to the content being shown in the window.
  data:{ // (OPTIONAL) Data for the object contained in the window.
    a:"666"
    b:"789434"
    etc:"etc"
  }
}

Note that if one of "text", "url", or "data" is not defined (depending on window type), the window will be a Default Window
A Default Window just has a plus button and says "Click to Add Content", just like in Powerpoint. All windows not pre-loaded start like this.
After content is added, the window is saved, and the window is changed to reflect the changes.
*/
