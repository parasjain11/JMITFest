package com.jmit.festmanagement.utils;

/**
 * Created by arpitkh996 on 07-09-2016.
 */

public class URL_API {
    public static final String test="http://192.168.137.139:8080/AmazeIOT";
    public static final String main="http://52.89.213.87:8080/AmazeIOT";
    public static final String mqtt_broker="tcp://52.89.213.87:1883";
    public static final String subscribe_group="/status/";
    public static final String online_status_group="/online/";
    public static final String send_group="/devices/";
    public static final String baseUrl=main;
    public static final String GET_DEVICES_API=baseUrl+"/getDeviceIds";
    public static final String LOGIN_API=baseUrl+"/login";
    public static final String LOGOUT_API=baseUrl+"/logout";
    public static final String CHECK_LOGIN_API=baseUrl+"/isLoggedIn";
    public static final String GET_ELECTRONICS_API=baseUrl+"/getElectronics";
}
