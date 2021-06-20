package org.moson.checktx.app.utils;

/**
 * Copyright (C), 2016-2021
 * Author: 超人迪加
 * Date: 6/18/21 9:38 PM
 */
public enum DangerPermEnum {
    READ_CALENDAR("android.permission.READ_CALENDAR", "允许程序读取用户日历数据"),
    WRITE_CALENDAR("android.permission.WRITE_CALENDAR", "允许一个程序写入但不读取用户日历数据"),
    CAMERA("android.permission.CAMERA", "允许访问摄像头进行拍照"),
    READ_CONTACTS("android.permission.READ_CONTACTS", "允许程序读取用户联系人数据"),
    WRITE_CONTACTS("android.permission.WRITE_CONTACTS", "允许程序写入但不读取用户联系人数据"),
    GET_ACCOUNTS("android.permission.GET_ACCOUNTS", "访问一个帐户列表在Accounts Service中"),
    ACCESS_FINE_LOCATION("android.permission.ACCESS_FINE_LOCATION", "允许一个程序访问精良位置(如GPS)"),
    ACCESS_COARSE_LOCATION("android.permission.ACCESS_COARSE_LOCATION", "允许一个程序访问CellID或WiFi热点来获取粗略的位置"),
    RECORD_AUDIO("android.permission.RECORD_AUDIO", "允许程序录制音频"),
    CALL_PHONE("android.permission.CALL_PHONE", "允许一个程序初始化一个电话拨号不需通过拨号用户界面需要用户确认"),
    READ_PHONE_STATE("android.permission.READ_PHONE_STATE", "访问电话状态"),
    READ_CALL_LOG("android.permission.READ_CALL_LOG", "查看电话日志"),
    WRITE_CALL_LOG("android.permission.WRITE_CALL_LOG", "写入电话日志"),
    ADD_VOICEMAIL("android.permission.ADD_VOICEMAIL", "允许应用程序添加系统中的语音邮件"),
    USE_SIP("android.permission.USE_SIP", "允许程序使用SIP视频服务"),
    PROCESS_OUTGOING_CALLS("android.permission.PROCESS_OUTGOING_CALLS", "允许应用程序监视、修改、忽略拨出的电话"),
    BODY_SENSORS("android.permission.BODY_SENSORS", "允许该应用存取监测您身体状况的传感器所收集的数据，例如您的心率"),
    SEND_SMS("android.permission.SEND_SMS", "允许程序发送SMS短信"),
    RECEIVE_SMS("android.permission.RECEIVE_SMS", "允许程序监控一个将收到短信息，记录或处理"),
    READ_SMS("android.permission.READ_SMS", "允许程序读取短信息"),
    RECEIVE_WAP_PUSH("android.permission.RECEIVE_WAP_PUSH", "允许程序监控将收到WAP PUSH信息"),
    RECEIVE_MMS("android.permission.RECEIVE_MMS", "允许一个程序监控将收到MMS彩信,记录或处理"),
    WRITE_EXTERNAL_STORAGE("android.permission.WRITE_EXTERNAL_STORAGE", "允许程序写入外部存储，如SD卡上写文件"),
    READ_EXTERNAL_STORAGE("android.permission.READ_EXTERNAL_STORAGE", "访问您设备上的照片、媒体内容和文件");

    private String name;
    private String desc;

    DangerPermEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
