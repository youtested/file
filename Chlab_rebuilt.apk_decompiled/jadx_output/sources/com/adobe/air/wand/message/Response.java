package com.adobe.air.wand.message;

import com.adobe.air.wand.message.Message;
import com.distriqt.extension.inappbilling.BuildConfig;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class Response extends Message {

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public enum Status {
        SUCCESS("SUCCESS"),
        ERROR("ERROR");

        private final String mStatus;

        Status(String str) {
            this.mStatus = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mStatus;
        }
    }

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public static class Header extends Message.Header {
        protected Status mStatus;
        protected String mTaskID;

        public Header(String str, String str2, long j, Status status) {
            super(str, Message.Type.RESPONSE, j);
            this.mStatus = null;
            this.mTaskID = BuildConfig.FLAVOR;
            this.mStatus = status;
            this.mTaskID = str2;
        }

        public Status getStatus() {
            return this.mStatus;
        }

        public void setStatus(Status status) {
            this.mStatus = status;
        }

        public String getTaskID() {
            return this.mTaskID;
        }

        public void setTaskID(String str) {
            this.mTaskID = str;
        }
    }

    /* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
    public static class Data extends Message.Data {
        protected MessageDataObject mResult = null;

        public Data(MessageDataObject messageDataObject) {
            setResult(messageDataObject);
        }

        public Object getResult() {
            return this.mResult;
        }

        public void setResult(MessageDataObject messageDataObject) {
            this.mResult = messageDataObject;
        }
    }

    public Response(Header header, Data data) {
        super(header, data);
    }

    @Override // com.adobe.air.wand.message.Message
    public Header getHeader() {
        return (Header) this.mHeader;
    }

    @Override // com.adobe.air.wand.message.Message
    public Data getData() {
        return (Data) this.mData;
    }
}
