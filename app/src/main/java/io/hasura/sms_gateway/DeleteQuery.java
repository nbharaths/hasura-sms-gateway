package io.hasura.sms_gateway;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rishichandra on 26/7/17.
 */

public class DeleteQuery {

    @SerializedName("type")
    String type = "delete";

    @SerializedName("args")
    DeleteQuery.Args args;

    class Args{
        @SerializedName("table")
        String table = "sms_data";
        @SerializedName("where")
        DeleteQuery.Where where;
    }

    class Where{
        @SerializedName("device_id")
        String device_id;

    }

    public DeleteQuery(String device_id){
        args = new DeleteQuery.Args();
        args.where = new DeleteQuery.Where();
        args.where.device_id = device_id;
    }
}


