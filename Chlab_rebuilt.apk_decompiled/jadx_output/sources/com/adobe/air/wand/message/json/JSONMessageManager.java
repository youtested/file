package com.adobe.air.wand.message.json;

import com.adobe.air.wand.message.Message;
import com.adobe.air.wand.message.MessageDataArray;
import com.adobe.air.wand.message.MessageDataObject;
import com.adobe.air.wand.message.MessageManager;
import com.adobe.air.wand.message.Notification;
import com.adobe.air.wand.message.Request;
import com.adobe.air.wand.message.Response;
import org.json.JSONObject;

/* loaded from: /home/codespace/chem/Chlab_rebuilt.apk_decompiled/build/apk/classes.dex */
public class JSONMessageManager extends MessageManager {
    @Override // com.adobe.air.wand.message.MessageManager
    public MessageDataObject createDataObject() {
        return new JSONMessageDataObject();
    }

    @Override // com.adobe.air.wand.message.MessageManager
    public MessageDataArray createDataArray() {
        return new JSONMessageDataArray();
    }

    @Override // com.adobe.air.wand.message.MessageManager
    public String serializeMessage(Message message) throws Exception {
        return createJSONMessage(message).toString();
    }

    @Override // com.adobe.air.wand.message.MessageManager
    public Message deserializeWandMessage(String str) throws Exception {
        return createWandMessage(new JSONObject(str));
    }

    @Override // com.adobe.air.wand.message.MessageManager
    public Request createWandRequest(String str, String str2, MessageDataArray messageDataArray) throws Exception {
        Request.Header header = new Request.Header(str, str2, System.currentTimeMillis());
        if (messageDataArray == null) {
            messageDataArray = new JSONMessageDataArray();
        }
        return new Request(header, new Request.Data(messageDataArray));
    }

    @Override // com.adobe.air.wand.message.MessageManager
    public Response createWandResponse(String str, String str2, MessageDataObject messageDataObject, Response.Status status) throws Exception {
        Response.Header header = new Response.Header(str, str2, System.currentTimeMillis(), status);
        if (messageDataObject == null) {
            messageDataObject = new JSONMessageDataObject();
        }
        return new Response(header, new Response.Data(messageDataObject));
    }

    @Override // com.adobe.air.wand.message.MessageManager
    public Notification createWandNotification(String str, MessageDataObject messageDataObject) throws Exception {
        Notification.Header header = new Notification.Header(str, System.currentTimeMillis());
        if (messageDataObject == null) {
            messageDataObject = new JSONMessageDataObject();
        }
        return new Notification(header, new Notification.Data(messageDataObject));
    }

    private static Message createWandMessage(JSONObject jSONObject) throws Exception {
        Message message;
        Response.Status status;
        synchronized (jSONObject) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("header");
            JSONObject jSONObject3 = jSONObject.getJSONObject("data");
            String string = jSONObject2.getString("title");
            String string2 = jSONObject2.getString("type");
            long j = jSONObject2.getLong("timestamp");
            message = null;
            if (string2.equals(Message.Type.REQUEST.toString())) {
                message = new Request(new Request.Header(string, jSONObject2.getString("taskID"), j), new Request.Data(new JSONMessageDataArray(jSONObject3.getJSONArray("arguments"))));
            } else if (string2.equals(Message.Type.RESPONSE.toString())) {
                String string3 = jSONObject2.getString("taskID");
                String string4 = jSONObject2.getString("status");
                if (string4.equals(Response.Status.SUCCESS.toString())) {
                    status = Response.Status.SUCCESS;
                } else if (string4.equals(Response.Status.ERROR.toString())) {
                    status = Response.Status.ERROR;
                } else {
                    throw new Exception("Unable to fetch Response status");
                }
                message = new Response(new Response.Header(string, string3, j, status), new Response.Data(new JSONMessageDataObject(jSONObject3.getJSONObject("result"))));
            } else if (string2.equals(Message.Type.NOTIFICATION.toString())) {
                message = new Notification(new Notification.Header(string, j), new Notification.Data(new JSONMessageDataObject(jSONObject3.getJSONObject("notification"))));
            }
        }
        return message;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static JSONObject createJSONMessage(Message message) throws Exception {
        Notification.Header header;
        JSONObject jSONObject;
        synchronized (message) {
            Message.Type type = message.getHeader().getType();
            JSONObject jSONObject2 = new JSONObject();
            JSONObject jSONObject3 = new JSONObject();
            switch (type) {
                case REQUEST:
                    Request.Header header2 = (Request.Header) message.getHeader();
                    Request.Data data = (Request.Data) message.getData();
                    jSONObject2.put("taskID", header2.getTaskID());
                    jSONObject3.put("arguments", ((JSONMessageDataArray) data.getArguments()).mJSONArray);
                    header = header2;
                    break;
                case RESPONSE:
                    Response.Header header3 = (Response.Header) message.getHeader();
                    Response.Data data2 = (Response.Data) message.getData();
                    jSONObject2.put("status", header3.getStatus().toString());
                    jSONObject2.put("taskID", header3.getTaskID());
                    jSONObject3.put("result", ((JSONMessageDataObject) data2.getResult()).mJSONObject);
                    header = header3;
                    break;
                case NOTIFICATION:
                    Notification.Header header4 = (Notification.Header) message.getHeader();
                    jSONObject3.put("notification", ((JSONMessageDataObject) ((Notification.Data) message.getData()).getNotification()).mJSONObject);
                    header = header4;
                    break;
                default:
                    throw new Exception("Unsupported message type");
            }
            jSONObject2.put("title", header.getTitle());
            jSONObject2.put("type", header.getType().toString());
            jSONObject2.put("timestamp", header.getTimestamp());
            jSONObject = new JSONObject();
            jSONObject.put("header", jSONObject2);
            jSONObject.put("data", jSONObject3);
        }
        return jSONObject;
    }
}
