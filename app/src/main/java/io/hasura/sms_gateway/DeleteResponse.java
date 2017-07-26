package io.hasura.sms_gateway;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rishichandra on 26/7/17.
 */

public class DeleteResponse {
    @SerializedName("affected_rows")
    int affected_rows;

    public int getAffected_rows(){ return affected_rows;}
}
