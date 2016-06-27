var NodeController = function(benchController) {

  function getNodesArray() {
    return benchController.bench.nodes;
  }

  //TODO: NOTE: ANY FUNCTION CALLS HERE WITH BENCHID OF ZERO SHOULD BE TREATED AS THE USERNODES
  // THE USERNODES AREN'T ON THE BENCH, THEY"RE IN THE FILE CABINET WHICH I THINK SHOULD POP
  // OUT FORM THE RIGHT SIDE OF THE SCREEN LIKE THE LEFT SIDE DOES KINDA

  this.getNode = function(nodeId) {
    return this.nodes[nodeId];
  };

  this.create = function(nodeId, benchId, x, y, w, h, title, dataType, data, userName, userId) {
    //TODO: create a node on the bench
  };

  this.delete = function(nodeId, benchId, userName, userId) {
    //TODO: delete a node
  };

  this.edit = function(nodeId, benchId, dataType, data, userName, userId) {
    //TODO: edit data inside a node
  };

  this.move = function(nodeId, benchId, x, y, userName, userId) {
    //TODO: move a noce
  };

  this.rename = function(nodeId, benchId, title, userName, userId) {
    //TODO: change the title of the node
  };

  this.resize = function(nodeId, benchId, w, h, userName, userId) {
    //TODO: change dimensions of node
  };

  this.cursor = function(nodeId, benchId, cursorIndex, showCursor, userName, userId) {
    //TODO: display someone else's cursor position and name on a text node
  };

  this.modify = function(nodeId, benchId, beginIndex, endIndex, data, userName, userId) {
    //TODO: someone else modified a text node
    // aka they changed more than a few characters of a text node
    // before actually clicking submit, all websockets will start
    // to recieve these, once the finishing editing (or whatever its called)
    // button is pushed, the edit function will be called and that'll override any
    // text changes if for some reason they dont match
  };

  this.select = function(nodeId, benchId, beginIndex, endIndex, userName, userId) {
    //TODO: shows someone selecting text on a textnode
  };

  this.ignore = function(benchId, nodeId, userName, userId) {
    //TODO: don't do anything with this for now
  };

  this.watch = function(benchId, nodeId, userName, userId) {
    //TODO: don't do anything with this for now
  };
};
