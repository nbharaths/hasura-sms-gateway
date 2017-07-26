package io.hasura.sms_gateway;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rishichandra on 26/7/17.
 */

public class SelectQuery {
    @SerializedName("type")
    String type = "select";

    @SerializedName("args")
    Args args;

    class Args{
        @SerializedName("table")
        String table = "sms_data";

        @SerializedName("columns")
        String[] columns = {"device_id", "dest_num","msg_body"};

        @SerializedName("where")
        Where where;
    }

    class Where{
        @SerializedName("device_id")
        String device_id;
    }

    public SelectQuery(String device_id){
        args = new Args();
        args.where = new Where();
        args.where.device_id = device_id;

    }
}

