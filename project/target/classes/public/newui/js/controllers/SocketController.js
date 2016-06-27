var SocketController = function(socketAddress, userController, nodeController, benchController, notificationController, chatController) {
  var persist = false;
  var socket = null;
  var agent = { id: userController.userId, token: userController.token };
  var benchList = [];

  function basicValidate(node, bench, id) {
    return typeof node === 'number' && node > 0 &&
      typeof bench === 'number' && bench >= 0 &&
      typeof id === 'number' && id > 0;
  }

  var protocol = {
    client: {
      verify: function(benchId) {
        return {
          type: 'verify',
          bench: benchId,
          agent: agent
        };
      },
      core: {
        move: function(nodeId, benchId, x, y) {
          return {
            type: 'core.move',
            node: nodeId,
            bench: benchId,
            dimensions: {
              x: x,
              y: y
            },
            agent: agent
          };
        },
        resize: function(nodeId, benchId, w, h) {
          return {
            type: 'core.resize',
            node: nodeId,
            bench: benchId,
            dimensions: {
              w: w,
              h: h
            },
            agent: agent
          };
        }
      },
      text: {
        cursor: function(nodeId, benchId, index, show) {
          return {
            type: 'text.cursor',
            node: nodeId,
            bench: benchId,
            content: {
              index: index,
              show: show
            },
            agent: agent
          };
        },
        modify: function(nodeId, benchId, begin, end, data) {
          return {
            type: 'text.modify',
            node: nodeId,
            bench: benchId,
            content: {
              begin: begin,
              end: end,
              data: data
            },
            agent: agent
          };
        },
        select: function(nodeId, benchId, begin, end) {
          return {
            type: 'text.select',
            node: nodeId,
            bench: benchId,
            content: {
              begin: begin,
              end: end
            },
            agent: agent
          };
        }
      },
      misc: {
        chat: function(benchId, message) {
          return {
            type: 'misc.chat',
            bench: benchId,
            message: message,
            agent: agent
          };
        }
      }
    },
    server: {
      verify: function(payload) {
        if(typeof payload.bench === 'number' && payload.bench > 0 &&
        typeof payload.role === 'number' && payload.role >= 2 && payload.role <= 3) {
          chatController.chat("Connected to chat...");
        }
      },
      core: {
        create: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id) &&
            typeof payload.dimensions.x === 'number' && payload.dimensions.x >= 0 &&
            typeof payload.dimensions.y === 'number' && payload.dimensions.y >= 0 &&
            typeof payload.dimensions.w === 'number' && payload.dimensions.w >= 0 &&
            typeof payload.dimensions.h === 'number' && payload.dimensions.h >= 0) {
            nodeController.create(
              payload.node,
              payload.bench,
              payload.dimensions.x,
              payload.dimensions.y,
              payload.dimensions.w,
              payload.dimensions.h,
              payload.content.title,
              payload.content.type,
              payload.content.data,
              payload.agent.name,
              payload.agent.id
            );
          }
        },
        delete: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id)) {
            nodeController.delete(
              payload.node,
              payload.bench,
              payload.agent.name,
              payload.agent.id
            );
          }
        },
        edit: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id)) {
            nodeController.edit(
              payload.node,
              payload.bench,
              payload.content.type,
              payload.content.data,
              payload.agent.name,
              payload.agent.id
            );
          }
        },
        move: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id) &&
            typeof payload.dimensions.x === 'number' && payload.dimensions.x >= 0 &&
            typeof payload.dimensions.y === 'number' && payload.dimensions.y >= 0) {
            nodeController.move(
              payload.node,
              payload.bench,
              payload.dimensions.x,
              payload.dimensions.y,
              payload.agent.name,
              payload.agent.id
            );
          }
        },
        rename: function(payload) {
            if(basicValidate(payload.node, payload.bench, payload.agent.id)) {
              nodeController.rename(
                payload.node,
                payload.bench,
                payload.content.title,
                payload.agent.name,
                payload.agent.id
              );
            }
        },
        resize: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id) &&
            typeof payload.dimensions.w === 'number' && payload.dimensions.w >= 0 &&
            typeof payload.dimensions.h === 'number' && payload.dimensions.h >= 0) {
            nodeController.resize(
              payload.node,
              payload.bench,
              payload.dimensions.w,
              payload.dimensions.h,
              payload.agent.name,
              payload.agent.id
            );
          }
        }
      },
      text: {
        cursor: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id) &&
            typeof payload.content.index === 'number' && payload.content.index >= 0) {
            nodeController.cursor(
              payload.node,
              payload.bench,
              payload.content.index,
              payload.content.show,
              payload.agent.name,
              payload.agent.id
            );
          }
        },
        modify: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id) &&
            typeof payload.content.begin === 'number' && payload.content.begin >= 0 &&
            typeof payload.content.end === 'number' && payload.content.end > 0) {
            nodeController.modify(
              payload.node,
              payload.bench,
              payload.content.begin,
              payload.content.end,
              payload.content.data,
              payload.agent.name,
              payload.agent.id
            );
          }
        },
        select: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id) &&
            typeof payload.content.begin === 'number' && payload.content.begin >= 0 &&
            typeof payload.content.end === 'number' && payload.content.end > 0) {
            nodeController.select(
              payload.node,
              payload.bench,
              payload.content.begin,
              payload.content.end,
              payload.agent.name,
              payload.agent.id
            );
          }
        }
      },
      misc: {
        chat: function(payload) {
          if(typeof payload.bench === 'number' && payload.bench >= 0 &&
            typeof payload.level === 'number' && payload.level >= 0 && payload.level <= 6 &&
            typeof payload.agent.id === 'number' && payload.agent.id > 0) {
            chatController.userChat(
              payload.bench,
              payload.message,
              payload.level,
              payload.agent.name,
              payload.agent.id
            );
          }
        },
        ignore: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id)) {
            nodeController.ignore(
              payload.bench,
              payload.node,
              payload.agent.name,
              payload.agent.id
            );
            notificationController.notify(
              payload.agent.name + " watched a card.",
              payload.agent.name + " is now watching " + nodeController.getNode(payload.node).title + " in " + benchController.getBench(payload.bench).title + "."
            );
          }
        },
        mod: function(payload) {
          if(typeof payload.bench === 'number' && payload.bench >= 0 &&
            typeof payload.id === 'number' && payload.id > 0) {
            if(payload.action == 'add') {
              benchController.addUser(
                payload.bench,
                payload.level,
                payload.user,
                payload.id,
                payload.avatar
              );
              notificationController.notify(
                "User Join!",
                payload.user + " joined " + benchController.getBench(payload.bench).title + " as " + benchController.translateLevel(payload.level) + "."
              );
            } else if(payload.action == 'remove') {
              benchController.removeUser(
                payload.bench,
                payload.user,
                payload.id
              );
              notificationController.notify(
                "User Leave!",
                payload.user + " was removed from " + benchController.getBench(payload.bench).title + "."
              );
            } else if(payload.action == 'modify') {
              benchController.modifyUser(
                payload.bench,
                payload.level,
                payload.user,
                payload.id
              );
              notificationController.notify(
                "Permission Change!",
                payload.user + " changed to " + benchController.translateLevel(payload.level) + " in " + benchController.getBench(payload.bench).title + "."
              );
            }
          }
        },
        notify: function(payload) {
          if(!(payload.text) && payload.header) {
            notificationController.notify(payload.header);
          } else if (payload.header) {
            notificationController.notify(payload.header, payload.text);
          }
        },
        watch: function(payload) {
          if(basicValidate(payload.node, payload.bench, payload.agent.id)) {
            nodeController.watch(
              payload.bench,
              payload.node,
              payload.agent.name,
              payload.agent.id
            );
            notificationController.notify(
              payload.agent.name + " ignored a card.",
              payload.agent.name + " is no longer watching " + nodeController.getNode(payload.node).title + " in " + benchController.getBench(payload.bench).title + "."
            );
          }
        }
      }
    }
  };

  function registerSocketHandlers()
  {
    socket.onerror = function(event) {
      notificationController.notify("Severe Websocket Error", event.data);
      if (socket.readyState > 1) {
        socket = new Websocket(socketAddress);
        registerSocketHandlers();
      }
    };

    socket.onopen = function(event) {
      if(persist) {
        notificationController.notify("Websocket Recovered");
      }
      persist = true;
      for(var i = 0; i < benchList.length; i++) {
        socket.send(JSON.stringify(protocol.verify(benchList[i])));
      }
    };

    socket.onclose = function() {
      if (persist) {
        setTimeout( function() {
          start(socketAddress);
        }, 5000);
      }
    };

    socket.onmessage = function(event) {
      var payload = JSON.parse(event.data);

      switch(payload.type) {
        case 'verify':
          protocol.server.verify(payload);
          break;
        case 'core.create':
          protocol.server.core.create(payload);
          break;
        case 'core.delete':
          protocol.server.core.delete(payload);
          break;
        case 'core.edit':
          protocol.server.core.edit(payload);
          break;
        case 'core.move':
          protocol.server.core.move(payload);
          break;
        case 'core.rename':
          protocol.server.core.rename(payload);
          break;
        case 'core.resize':
          protocol.server.core.resize(payload);
          break;
        case 'misc.chat':
          protocol.server.misc.chat(payload);
          break;
        case 'misc.ignore':
          protocol.server.misc.ignore(payload);
          break;
        case 'misc.mod':
          protocol.server.misc.mod(payload);
          break;
        case 'misc.notify':
          protocol.server.misc.notify(payload);
          break;
        case 'misc.watch':
          protocol.server.misc.watch(payload);
          break;
        case 'text.cursor':
          protocol.server.text.cursor(payload);
          break;
        case 'text.modify':
          protocol.server.text.modify(payload);
          break;
        case 'text.select':
          protocol.server.text.select(payload);
          break;
      }
    };
  }

  function send(payload) {
    if(benchList.indexOf(payload.bench) == -1)
    {
      notificationController.notify("Websocket Warning", "Communication has not been started for bench #" + payload.bench + ". message is ignored.");
    }

    switch (socket.readyState) {
        case 0:
          notificationController.notify("Websocket Warning", "Socket is not yet open; attempting to send payload anyway.");
          socket.send(JSON.stringify(payload));
          break;
        case 1:
          socket.send(JSON.stringify(payload));
          break;
        case 2:
          notificationController.notify("Websocket Error", "Socket is currently closing; payload will not be added to the buffer.");
          break;
        case 3:
          notificationController.notify("Websocket Error", "Socket is currently closed; payload will not be added to the buffer.");
          break;
    }
  }

  this.sendMove = function(benchId, nodeId, x, y) {
    send(protocol.client.core.move(nodeId, benchId, x, y));
  };

  this.sendResize = function(benchId, nodeId, w, h) {
    send(protocol.client.core.resize(nodeId, benchId, w, h));
  };

  this.sendChat = function(benchId, message) {
    send(protocol.client.misc.chat(benchId, message));
  };

  this.sendCursor = function(benchId, nodeId, index, show) {
    send(protocol.client.text.cursor(nodeId, benchId, index, show));
  };

  this.sendModify = function(benchId, nodeId, begin, end, data) {
    send(protocol.client.text.modify(nodeId, benchId, begin, end, data));
  };

  this.sendSelect = function(benchId, nodeId, begin, end) {
    send(protocol.client.text.select(nodeId, benchId, begin, end));
  };

  this.beginCommunication = function(benchId) {
    if(!(socket)) {
      socket = new Websocket(socketAddress);
      registerSocketHandlers();
    }
    if(benchList.indexOf(benchId) == -1) {
      benchList.push(benchId);
      if(socket.readyState == 1) {
        send(protocol.client.verify(benchId));
      }
    } else {
      notificationController.notify("Websocket Warning", "Socket communication has already begun (" + benchId + ").");
    }
  };

  this.endCommunication = function(benchId) {
    var index = benchList.indexOf(benchId);
    if(index != -1) {
      benchList.splice(index, 1);
    } else {
      notificationController.notify("Websocket Warning", "Socket communication has already been ended (" + benchId + ").");
    }
    if(benchList.length === 0)
    {
      persist = false;
      socket.close();
    }
  };

  this.terminateSocket = function() {
    persist = false;
    benchList = [];
    socket.close();
  };

  this.updateToken = function(token) {
    agent.token = token;
  };
};
