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

    // Show text add popup
    $("#addbar-add-text").click(function() {
      wb_ui.prepareNewNodePopup("text");
      wb_ui.toggleAddbar();
    });

    // Show image add popup
    $("#addbar-add-image").click(function() {
      wb_ui.prepareNewNodePopup("image");
      wb_ui.toggleAddbar();
    });

    // Toggle cover on popup hide
    $(".modal").on("hide.bs.modal", function(e) {
      wb_ui.toggleCover();
    });

    // Toggle cover on popup show
    $(".modal").on("show.bs.modal", function(e) {
      wb_ui.toggleCover();
    });

    // Process node create button press
    $(".add-node-button").click(function(e) {
      var type = $(e.target).data("type");
      switch(type) {
        case "text":
          if($("#add-node-text-text").val().length < 1 || $("#add-node-text-title").val().length < 1) {
            wb_ui.showNewNodeError("<strong>Error:</strong> You must enter title and text for the new node")
          } else {
            // TODO right here
            wb_request.send(wb_request.protocol.bnode.create.request(workbench_benchid, 75, 75, 600, 400, $("#add-node-text-title").val(), "text", $("#add-node-text-text").val()), function(data) {
              // success!
              console.log("SUCCESS!");
            }, function(data) {
              wb_ui.showNewNodeError("<strong>Error:</strong> Failed to create new node (see console for details)");
            });
          }
        break;
        default:
        break;
      };
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
      containment:"#workbench",
      drag: function(event, ui) {
        var n = $(event.target).data("nodeid");
        console.log("MOVE SENT");
        wb_bench.socketController.sendMove(workbench_benchid, n, ui.offset.left, ui.offset.top);
      },
      stop: function(event, ui) {
        wb_request.send(wb_request.protocol.bnode.move.request(workbench_benchid, $(event.target).data("nodeid"), ui.offset.left, ui.offset.top), function(data) {
          console.log("MOVE-END SENT")
        }, function(data) {
          // TODO Notificiation - Failed to send node move event
        });
      }
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

  this.prepareNewNodePopup = function(type) {
    $("#add-node-title").html(" - " + type.charAt(0).toUpperCase() + type.slice(1));
    $("#popup-add-node .messagebox .alert").remove();
    $("#popup-add-node .hidden-form").hide();
    $("#popup-add-node .hidden-form." + type).show();
    $("#popup-add-node").modal("show");
  };

  this.showNewNodeError = function(error) {
    $("#popup-add-node .messagebox").html('<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>' + error + '</div>');
  }

  this.addNode = function(data) {

  };
};
