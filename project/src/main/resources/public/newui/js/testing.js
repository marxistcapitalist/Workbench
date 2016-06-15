/*
   Workbench (c) 2016 J Teissler and Ethan Crowder, All Rights Reserved.
   NOTE: This is just a UI testing script. This should not be used in production.
   (It's also poorly written and completely procedural)
*/

/* Slidey sidebar */
$(document).ready(function() {
  $("#topbar-button-add").click(function() {
    $("#addbar").css("display", "block");
    $("#addbar").animate({left:0}, 200);
    console.log("clickylick");
  });

  $("#addbar-close").click(function() {
    $("#addbar").animate({left:"-250px"}, 200, "swing", function() {
      $("#addbar").css("display", "none");
    });
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
