package io.hasura.sms_gateway;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rishichandra on 26/7/17.
 */

public class ResponseMessage {
    @SerializedName("msg_body")
    String msg_body;

    @SerializedName("dest_num")
    String dest_num;

    public String getMsg_body(){ return msg_body;}
    public String getDest_num(){ return dest_num;}
}
