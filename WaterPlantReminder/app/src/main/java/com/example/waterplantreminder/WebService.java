package com.example.waterplantreminder;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class WebService {


    public static String questToWebService(String wsdl_url, String name_space, String methodName, String plantName) {
        String result = "";

        HttpTransportSE httpTransportSE = new HttpTransportSE(wsdl_url);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);

        SoapObject request = new SoapObject(name_space, methodName);

        request.addProperty("plantName",plantName);

        envelope.setOutputSoapObject(request);
        try {

            httpTransportSE.call(methodName, envelope);
            if (envelope.getResponse() != null) {
                result = envelope.getResponse().toString().trim();
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return result;
    }

}
