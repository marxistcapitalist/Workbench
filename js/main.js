/* Workbench (c) 2016 Jeremy Teissler and Ethan Crowder, ALl Rights Reserved

* !!! AS OF PRE-ALPHA 0.0.2, THIS WILL NO LONGER BE USED !!!
* The project is now moving to an OOP structure, with classes defined in workbench.js
* This file WILL be removed and WILL NOT exist after Pre-Alpha 0.0.2

*/

function drag_start(event) // When we start the drag, on the dragable element...
{
    var style = window.getComputedStyle(event.target, null); // Get gradded element style
    event.dataTransfer.setData("text/plain", // It's plaintext
    (parseInt(style.getPropertyValue("left"),10) - event.clientX) + ',' + (parseInt(style.getPropertyValue("top"),10) - event.clientY)); // Get left absolute, top absolute, store it x,y format
}

function drag_over(event) // Allow dragging over this element
{
    event.preventDefault();
    return false;
}

function drop(event) // Drop on droppable thing
{
    var offset = event.dataTransfer.getData("text/plain").split(','); // Make array, splitting x and y to offet[0] and offset[1] respectively
    var dm = document.getElementsByClassName('window')[0]; // Currently, just grab the single window class there is on the page (This needs to change, most definitely)
    dm.style.left = (event.clientX + parseInt(offset[0],10)) + 'px'; // Set left (x) axis offset based on drop x
    dm.style.top = (event.clientY + parseInt(offset[1],10)) + 'px'; // Set top (y) axis offset based on drop Y
    event.preventDefault(); // ALWAYS prevent default
    console.error(event);
    return false; // Don't let the default event actions happen at all
}

function loadScript() // What to do once we're loaded
{
    var dm = document.getElementsByClassName("window")[0]; // Grab our single window we got here
    dm.addEventListener('dragstart', drag_start, false); // Add listener for the drag start event
    document.getElementsByClassName("workbench")[0].addEventListener('dragover', drag_over, false); // Add listener to the workbench to execute the dragover event once that happens
    document.getElementsByClassName("workbench")[0].addEventListener('drop', drop, false); // Add listener to the workbench to execute the drop event once that happens
}

function initScrollbar() {
  $("body").mCustomScrollbar({
    axis:"yx"
  });
}

function loadScreen()
{
  $(".loadscreen").fadeOut(1000, function() {
    initScrollbar();
  });
}

$(window).load(loadScript);
$(document).ready(function() { setTimeout(loadScreen, 5000); })
