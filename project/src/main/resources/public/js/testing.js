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
    toggleAddbar();
  });

  $("#addbar-add-text").click(function() {
    toggleAddbar();
    toggleTestPopup();
  });
});

$(window).load(function() {
  $("#bench-viewport").mCustomScrollbar({
    axis:"yx"
  });
  $("#popuptest > .body").mCustomScrollbar({
    axis:"y"
  });
  // Workbench drag and drop
  $(".node").draggable({
    containment:"#workbench"
  });
  $(".workbench").droppable();
});

function toggleAddbar() {
  $("#addbar").animate({left:-$("#addbar").width()}, 200, "swing", function() {
    $("#addbar").css("display", "none");
  });
}
function toggleCover() {
  $("#cover").fadeToggle(200);
}

function toggleTestPopup() {
  toggleCover();
  $("#popup-container").fadeToggle(200);
  $("#popuptest").fadeToggle(200);
}
