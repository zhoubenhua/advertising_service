package com.zbh.advertising_service.model;

import java.io.Serializable;

/**
 * 用户表结构实体
 */
public class User implements Serializable {
    private int id;//用户id
    private String user_name;//用户名字
    private String tel;//用户手机号
    private String wx_uid;//微信uid
    private String wx_head_url;//微信用户头像地址
    private String company_name;//公司名字
    private String label;//标签
    private String invitationCode;//邀请码
    private int role;//用户角色 0普通员 1管理员  2虚拟用户

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getWx_uid() {
        return wx_uid;
    }

    public void setWx_uid(String wx_uid) {
        this.wx_uid = wx_uid;
    }

    public String getWx_head_url() {
        return wx_head_url;
    }

    public void setWx_head_url(String wx_head_url) {
        this.wx_head_url = wx_head_url;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
