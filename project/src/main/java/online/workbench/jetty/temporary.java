enum TemporaryDesign
{
  @Serialized("ws|move <-")WEBSOCKET_CLIENT_MOVE(Class.class),
  @Serialized("ws|resize <-")WEBSOCKET_CLIENT_RESIZE(Class.class),
  @Serialized("ws|textcursor <-")WEBSOCKET_CLIENT_TEXTCURSOR(Class.class),
  @Serialized("ws|textmodify <-")WEBSOCKET_CLIENT_TEXTMODIFY(Class.class),
  @Serialized("ws|textselect <-")WEBSOCKET_CLIENT_TEXTSELECT(Class.class),
  
  @Serialized("ws|create ->")WEBSOCKET_SERVER_CREATE(Class.class),
  @Serialized("ws|delete ->")WEBSOCKET_SERVER_DELETE(Class.class),
  @Serialized("ws|edit ->")WEBSOCKET_SERVER_EDIT(Class.class),
  @Serialized("ws|move ->")WEBSOCKET_SERVER_MOVE(Class.class),
  @Serialized("ws|rename ->")WEBSOCKET_SERVER_RENAME(Class.class),
  @Serialized("ws|resize ->")WEBSOCKET_SERVER_RESIZE(Class.class),
  @Serialized("ws|chat ->")WEBSOCKET_SERVER_CHAT(Class.class),
  @Serialized("ws|ignore ->")WEBSOCKET_SERVER_IGNORE(Class.class),
  @Serialized("ws|watch ->")WEBSOCKET_SERVER_WATCH(Class.class),
  @Serialized("ws|notify ->")WEBSOCKET_SERVER_NOTIFY(Class.class),
  @Serialized("ws|update ->")WEBSOCKET_SERVER_UPDATE(Class.class),
  @Serialized("ws|textcursor ->")WEBSOCKET_SERVER_TEXTCURSOR(Class.class),
  @Serialized("ws|textmodify ->")WEBSOCKET_SERVER_TEXTMODIFY(Class.class),
  @Serialized("ws|textselect ->")WEBSOCKET_SERVER_TEXTSELECT(Class.class);
  
  
}
