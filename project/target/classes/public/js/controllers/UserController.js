var UserController = function(requestController, notificationController) {

  this.user = {};

  requestController.send(requestController.protocol.account.user(), function(data) {
    this.user = data;
  }, function(data) {
    notificationController.notify("User Error", "User data could not be retrieved :(");
  });
};
