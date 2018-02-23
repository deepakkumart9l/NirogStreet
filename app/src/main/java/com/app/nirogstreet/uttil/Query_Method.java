package com.app.nirogstreet.uttil;

import android.content.ContentValues;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by Deepak on 04-Dec-15.
 */
public class Query_Method {

    public static String getQuery(ContentValues params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Set<Map.Entry<String, Object>> values=params.valueSet();
        for (Map.Entry<String, Object> entry : values)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));

        }
        return result.toString();
    }

}
