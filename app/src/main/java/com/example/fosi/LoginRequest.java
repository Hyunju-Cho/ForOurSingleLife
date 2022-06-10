package com.example.fosi;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL="http://192.168.78.126/Login.php"; //php서버, 로그인 처리하는 SQL 작성됨
    private Map<String,String> map;//DB 값 받아옴

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);//POST방식으로 전송

        map=new HashMap<>();
        map.put("userID",userID);//DB 테이블에 있는 변수로 넣음
        map.put("userPassword",userPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;//php문에 작성된 success 반환
    }

}
