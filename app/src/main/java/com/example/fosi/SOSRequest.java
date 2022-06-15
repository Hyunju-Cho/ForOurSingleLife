package com.example.fosi;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SOSRequest extends StringRequest {

    final static private String URL="http://IP주소/sos.php";
    private Map<String,String> map;

    public SOSRequest(String sos, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("sos",sos);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
