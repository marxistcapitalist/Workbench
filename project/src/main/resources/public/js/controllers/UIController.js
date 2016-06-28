var UIController = function() {

  /* === UIController.attachHandlers ===
    Attach NEARLY ALL event handlers.
    Separated into sections for categorization.
  */
  this.currentPopup = "";

  this.attachHandlers = function() {
    console.log("[Workbench] Attaching handlers...");
    // Add node button
    $("#topbar-button-add").click(function() {
      $("#addbar").css("display", "block");
      $("#addbar").animate({left:0}, 200);
    });

    // Close addbar button
    $("#addbar-close").click(function() {
      wb_ui.toggleAddbar();
    });

    // Show text node
    $("#addbar-add-text").click(function() {
      $("#popup-add-node").modal("show");
    });

    // On any modal show, record modal opening
    $(".modal").on("hide.bs.modal", function(e) {
      wb_ui.toggleCover();
      /*if(wb_ui.currentPopup !== "") {
        $(wb_ui.currentPopup).on("hidden.bs.modal", function() {

        });
        $(wb_ui.currentPopup).modal("hide");
      }
      wb_ui.currentPopup = e.target;*/
    });

    $(".modal").on("show.bs.modal", function(e) {
      wb_ui.toggleCover();
    });

    // End
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
};
