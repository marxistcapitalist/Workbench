/*
   Workbench (c) 2016 J Teissler and Ethan Crowder, All Rights Reserved.
   NOTE: This is just a UI testing script. This should not be used in production.
   (It's also poorly written and completely procedural)
*/

/* Slidey sidebar */
$(document).ready(function() {
  $("#topbar-button-add").click(function() {
    $("#addbar").animate({width:'toggle'}, 200);
    console.log("clickylick");
  });
});

$(window).load(function() {
  $("#bench-viewport").mCustomScrollbar({
    axis:"yx"
  });
  // Workbench drag and drop
  $(".node").draggable({
    containment:"#workbench"
  });
  $(".workbench").droppable();
});

function closeSidebar() {
  $("#addbar").animate({width:'toggle'}, 200);
}
