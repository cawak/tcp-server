package tcpserver.handler.handlers;

public enum HandlerType {
    Default("Default"),
    Get("get"),
    Set("set"),
    LeftAdd("leftAdd"),
    RightAdd("rightAdd"),
    GetAllKeys("getAllKeys");

    private String type;

    HandlerType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }
}
