package com.clyr.utils;

/**
 * Created by clyr on 2018/3/13 0013.
 * EventBus信息传递类
 */

public class EventBusMsg {
    private String message;
    private String messageTag;

    public EventBusMsg(String message) {
        this.message = message;
    }

    public EventBusMsg(String message, String messageTag) {
        this.message = message;
        this.messageTag = messageTag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

   /*
   EventBus.getDefault().register(this);

   @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {

        EventBus.getDefault().post(new MessageEvent(""));
        EventBus.getDefault().post(new MessageEvent(Config.mRefreash));
    }
    @Override
    public void Event(MessageEvent messageEvent) {
        super.Event(messageEvent);
        switch (messageEvent.getMessage()){
            case Config.mRefreash:
                break;
        }
    }*/
}
