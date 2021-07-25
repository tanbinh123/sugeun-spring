package com.jamsil_team.sugeun.service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;

@Service
public class SendSmsServiceImp implements SendSmsService {

    private String apiKey = "NCSJFWVZU2LQKBYL";
    private String apiSecret = "AHODJBSNE04IT3VZW1KAZN4F8QGTH5ID";
    private String fromNumber ="01047027253";

    /**
     * SMS 보내기
     */
    @Transactional
    @Override
    public void sendSms(String toNumber, String certifyNum)  {

        Message coolsms = new Message(apiKey, apiSecret);


        HashMap<String, String> params = new HashMap<>();

        params.put("to", toNumber);
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("text", "[수근수근] 인증번호 " + certifyNum + "을 입력해주세요.");
        params.put("app_version", "test app 1.2");


        try{

            JSONObject object = (JSONObject) coolsms.send(params);
            System.out.println(object.toString());

        }catch(CoolsmsException e){
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }


    }
}
