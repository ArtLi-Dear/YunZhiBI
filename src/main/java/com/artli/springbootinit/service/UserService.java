package com.artli.springbootinit.service;

import com.artli.springbootinit.model.dto.user.UserQueryRequest;
import com.artli.springbootinit.model.entity.User;
import com.artli.springbootinit.model.vo.LoginUserVO;
import com.artli.springbootinit.model.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
* @author XIAO ming
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-08-12 09:24:56
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

//    LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request);

    LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    User getLoginUserPermitNull(HttpServletRequest request);

    boolean isAdmin(HttpServletRequest request);

    boolean isAdmin(User user);

    boolean userLogout(HttpServletRequest request);

    LoginUserVO getLoginUserVO(User user);

    UserVO getUserVO(User user);

    List<UserVO> getUserVO(List<User> userList);

    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);


}
