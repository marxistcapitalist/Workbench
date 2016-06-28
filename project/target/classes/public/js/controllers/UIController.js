var UIController = function() {

  /* === UIController.attachHandlers ===
    Attach NEARLY ALL event handlers.
    Separated into sections for categorization.
  */
  this.attachHandlers = function() {
    console.log("[Workbench] Attaching handlers...");
    $("#topbar-button-add").click(function() {
      $("#addbar").css("display", "block");
      $("#addbar").animate({left:0}, 200);
    });

    $("#addbar-close").click(function() {
      wb_ui.toggleAddbar();
    });

    $("#addbar-add-text").click(function() {
      wb_ui.toggleAddbar();
      wb_ui.toggleTestPopup();
    });

    console.log("[Workbench] Handlers attached.")
  };

  this.initialize = function () {
    $("#bench-viewport").mCustomScrollbar({
      axis:"yx"
    });

    $("#popuptest > .body").mCustomScrollbar({
      axis:"y"
    });

    $(".node").draggable({
      containment:"#workbench"
    });

    $(".workbench").droppable();
  };

  this.toggleAddbar = function() {
    $("#addbar").animate({left:-$("#addbar").width()}, 200, "swing", function() {
      $("#addbar").css("display", "none");
    });
  };

  this.toggleCover = function() {
    $("#cover").fadeToggle(200);
  };

  this.toggleTestPopup = function() {
    wb_ui.toggleCover();
    $("#popup-container").fadeToggle(200);
    $("#popuptest").fadeToggle(200);
  }
};
