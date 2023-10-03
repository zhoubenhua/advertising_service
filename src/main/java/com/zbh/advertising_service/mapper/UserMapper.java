package com.zbh.advertising_service.mapper;

import com.zbh.advertising_service.model.User;

import java.util.List;

/**
 * 用户表增删查改
 */
public interface UserMapper {
   public void save(User user);
   public int update(User user);
   boolean delete(int id);
   public User getUserByInviteCode(String inviteCode);//根据推荐码查询用户信息
   public User getUser(String wxUid);//根据微信id查询用户信息
   public User queryUser(int uid);//根据用户id查询用户信息
   public List<User> findAll();
   public List<User> queryUserByRole(int role);//根据角色查询用户
}