package com.kovizone.demo.model;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SmUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@DynamicInsert
@Entity(name = "sys_user")
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String salt;

    private String fullName;

    private String email;

    private String telephone;

    private String mobilePhone;

    private String remark;

    private LocalDateTime createDatetime;

    private Integer createUserId;

    private LocalDateTime updateDatetime;

    private Integer updateUserId;

    @Version
    private Integer version;

    public void setEncodePassword(String password) {
        this.salt = RandomUtil.randomString(128);
        this.password = encodePassword(password, salt);
    }

    public void setEncodePassword(String password, String salt) {
        this.salt = salt;
        this.password = encodePassword(password, salt);
    }

    public boolean verifyPassword(String verifyPassword) {
        return this.password.equals(encodePassword(verifyPassword, this.salt));
    }

    protected String encodePassword(String password, String salt) {
        String passwordSm3 = SmUtil.sm3(password);
        String basic
                = SmUtil.sm3(salt).substring(0, 32)
                + passwordSm3.substring(0, passwordSm3.length() / 2)
                + SmUtil.sm3(salt).substring(32)
                + passwordSm3.substring(passwordSm3.length() / 2);
        return SmUtil.sm3(basic);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(LocalDateTime createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Integer getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public LocalDateTime getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(LocalDateTime updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
