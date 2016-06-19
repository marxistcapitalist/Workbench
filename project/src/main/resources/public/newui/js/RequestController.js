var RequestController = function(remoteAddress, userId, token, notificationController) {

  function send(payload) {
    payload.dataType = "json";
    $.ajax(payload);
  }

  function agent(payload) {
    payload.agent = {
      "token": token,
      "id": userId
    };
    return payload;
  }

  var protocol = {
    ping: {
      request: function() {
        return {
          method: "HEAD",
          url: remoteAddress + "/ping",
          data: {},
          success: this.handler
        };
      },
      handler: function(data, status, jqXHR) {
        return {

        };
      }
    },
    account: {
      auth: {
        request: function() {
          return {
            method: "POST",
            url: remoteAddress + "/auth",
            data: agent({}),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },`
      login: {
        request: function(login, password) {
          return {
            method: "POST",
            url: remoteAddress + "/login",
            data: agent({
              "loginkey": login,
              "password": password
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      logout: {
        request: function() {
          return {
            method: "POST",
            url: remoteAddress + "/logout",
            data: agent({
              "token": token
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      register: {
        request: function(username, email, password) {
          return {
            method: "POST",
            url: remoteAddress + "/register",
            data: agent({
              "username": username,
              "email": email,
              "password": password
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      user: {
        request: function(userId, username, email, password) {
          return {
            method: "GET",
            url: remoteAddress + "/user/" + userId,
            data: agent({}),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      useredit: {
        request: function(userId, username, email, password, avatar) {
          return {
            method: "PUT",
            url: remoteAddress + "/user/" + userId,
            data: agent({
              "username": username,
              "email": email,
              "password": password,
              "avatar": avatar
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
    },
    bench: {
      user: {
        add: {
          request: function(benchId, userId, permission) {
            return {
              method: "PUT",
              url: remoteAddress + "/bench/" + benchId + "/user/" + userId,
              data: agent({
                "permission": permission
              }),
              success: this.handler
            };
          },
          handler: function(data, status, jqXHR) {
            return {

            };
          }
        },
        modify: {
          request: function(benchId, userId, permission) {
            return {
              method: "PUT",
              url: remoteAddress + "/bench/" + benchId + "/user/" + userId,
              data: agent({
                "permission": permission
              }),
              success: this.handler
            };
          },
          handler: function(data, status, jqXHR) {
            return {

            };
          }
        },
        remove: {
          request: function(benchId, userId) {
            return {
              method: "DELETE",
              url: remoteAddress + "/bench/" + benchId + "/user/" + userId,
              data: agent({}),
              success: this.handler
            };
          },
          handler: function(data, status, jqXHR) {
            return {

            };
          }
        },
      },
      bench: {
        request: function(benchId, verbosity) {
          return {
            method: "GET",
            url: remoteAddress + "/bench/" + benchId,
            data: agent({
              "verbosity": verbosity
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      create: {
        request: function(benchId, title, width, height) {
          return {
            method: "POST",
            url: remoteAddress + "/bench/" + benchId,
            data: agent({
              "title": title,
              "dimensions": {
                "width": width,
                "height": height
              }
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      delete: {
        request: function(benchId) {
          return {
            method: "DELETE",
            url: remoteAddress + "/bench/" + benchId,
            data: agent({}),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      edit: {
        request: function(benchId, width, height, title, background) {
          return {
            method: "PUT",
            url: remoteAddress + "/bench/" + benchId,
            data: agent({
              "dimensions": {
                "width": width,
                "height": height
              },
              "content": {
                "title": title,
                "backrground": background
              }
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
    },
    bnode: {
      copy: {
        request: function(benchId, nodeId, title) {
          return {
            method: "PUT",
            url: remoteAddress + "/bench/" + benchId + "/copy/" + nodeId,
            data: agent({
              "title": title
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      create: {
        request: function(benchId, nodeId, x, y, width, height, title, type, data) {
          return {
            method: "POST",
            url: remoteAddress + "/bench/" + benchId + "/node/" + nodeId,
            data: agent({
              "dimensions": {
                "x": x,
                "y": y,
                "width": width,
                "height": height
              },
              "content": {
                "title": title,
                "type": type,
                "data": data
              }
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      delete: {
        request: function(benchId, nodeId) {
          return {
            method: "DELETE",
            url: remoteAddress + "/bench/" + benchId + "/node/" + nodeId,
            data: agent({}),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      edit: {
        request: function(benchId, nodeId, type, data, title) {
          return {
            method: "POST",
            url: remoteAddress + "/bench/" + benchId + "/node/" + nodeId,
            data: agent({
              "content": {
                "type": type,
                "data": data,
                "title": title
              }
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      move: {
        request: function(benchId, nodeId, x, y) {
          return {
            method: "POST",
            url: remoteAddress + "/bench/" + benchId + "/node/" + nodeId + "/move",
            data: agent({
              "dimensions": {
                "x": x,
                "y": y
              }
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      resize: {
        request: function(benchId, nodeId, width, height) {
          return {
            method: "POST",
            url: remoteAddress + "/bench/" + benchId + "/node/" + nodeId + "/resize",
            data: agent({
              "dimensions": {
                "width": width,
                "height": height
              }
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      spawn: {
        request: function(benchId, nodeId, title) {
          return {
            method: "PUT",
            url: remoteAddress + "/bench/" + benchId + "/spawn/" + nodeId,
            data: agent({
              "title": title
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      ignore: {
        request: function(benchId, nodeId) {
          return {
            method: "POST",
            url: remoteAddress + "/bench/" + benchId + "/node/" + nodeId + "/ignore",
            data: agent({}),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      watch: {
        request: function(benchId, nodeId) {
          return {
            method: "POST",
            url: remoteAddress + "/bench/" + benchId + "/node/" + nodeId + "/watch",
            data: agent({}),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
    },
    unode: {
      create: {
        request: function(nodeId, width, height, title, type, data) {
          return {
            method: "POST",
            url: remoteAddress + "/node/" + nodeId,
            data: agent({
              "dimensions": {
                "width": width,
                "height": height
              },
              "content": {
                "title": title,
                "type": type,
                "data": data
              }
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      delete: {
        request: function(nodeId) {
          return {
            method: "DELETE",
            url: remoteAddress + "/node/" + nodeId,
            data: agent({}),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      },
      edit: {
        request: function(nodeId, width, height, title, type, data) {
          return {
            method: "PUT",
            url: remoteAddress + "/node/" + nodeId,
            data: agent({
              "dimensions": {
                "width": width,
                "height": height
              },
              "content": {
                "title": title,
                "type": type,
                "data": data
              }
            }),
            success: this.handler
          };
        },
        handler: function(data, status, jqXHR) {
          return {

          };
        }
      }
    }
  };
};
