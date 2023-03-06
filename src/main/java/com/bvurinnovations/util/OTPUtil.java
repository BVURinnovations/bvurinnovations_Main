package com.bvurinnovations.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class OTPUtil {
    public boolean sendOTPWithTemplate(String mobile, String template, int otp) throws UnirestException {
        HttpResponse<String> response = null;
        String url = Constants.OTP_SITE_URL + "/" + mobile + "/" + otp +"/" + template;
        response = Unirest.get(url).asString();
        if (response != null) {
            if (response.getStatus() == 200)
                System.out.println("success");
            return true;
        }
        return false;
    }
}
