var BenchController = function(userId, userToken, notificationController, requestController) {

  this.bench = {};

  this.getUsers = function() {
    return this.bench.members;
  };

  this.translateLevel = function(level) {
    switch(level) {
      case 1:
        return "Viewer";
      case 2:
        return "Editor";
      case 3:
        return "Owner";
      default:
        return "None";
    }
  };

  this.addUser = function(benchId, permissionLevel, addedUserName, addedUserId, addedUserAvatar) {
    this.bench.members[addedUserId] = {
      "id": addedUserId,
      "user": addedUserName,
      "role": permissionLevel,
      "avatar": addedUserAvatar
    };
  };

  this.removeUser = function(benchId, removedUserName, removedUserId) {
    this.bench.members.splice(removedUserId, 1);
  };

  this.nodeController = new NodeController(this);
  this.chatController = new ChatController();
  this.socketController = new SocketController("ws://workbench.online/ws", userId, userToken, this.nodeController, this, notificationController, this.chatController);

  function finishInitialization() {
    this.socketController.beginCommunication(this.bench.id);
  }

  this.initializeBench = function(benchId) {
    requestController.send(requestController.protocol.bench.bench(benchId), function(data) {
      this.bench = data;
      finishInitialization();
    }, function(data) {
      notificationController.notify("Bench Error!", "You are not authorized to view this bench :(");
    });
  };

  this.getBench = function(blah) {
    return this.bench;
  };
};
