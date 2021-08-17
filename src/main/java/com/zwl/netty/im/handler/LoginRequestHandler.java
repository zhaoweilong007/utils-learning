package com.zwl.netty.im.handler;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.zwl.netty.im.model.LoginRequestPacket;
import com.zwl.netty.im.model.LoginRespPacket;
import com.zwl.netty.im.utils.LogUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/17
 **/
@Slf4j
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

  private static final Map<String, String> userMap = new HashMap<>() {{
    put("zhangsan", "123456");
  }};

  @Override
  protected void channelRead0(ChannelHandlerContext ctx,
      LoginRequestPacket loginRequestPacket) throws Exception {
    LoginRespPacket loginRespPacket = new LoginRespPacket();
    String date = new DateTime().toString(DatePattern.NORM_DATETIME_PATTERN);
    if (valid(loginRequestPacket)) {
      //标记为已登录
      LogUtils.markAsLogin(ctx.channel());
      log.info("{}于{}登录系统{}", loginRequestPacket.getUserName(), date, "成功");
      String message = StrUtil
          .format("😀登录成功♥，{}你好，当前时间：{}", loginRequestPacket.getUserName(), date);
      loginRespPacket.setMsg(message);
      loginRespPacket.setToken(generateToken(loginRequestPacket));
    } else {
      log.info("{}于{}登录系统{}", loginRequestPacket.getUserName(), date, "失败");
      loginRespPacket.setSuccess(false);
      loginRespPacket.setMsg("登录失败！请检查你的用户名密码");
    }
    ctx.writeAndFlush(loginRespPacket);
  }

  public String generateToken(LoginRequestPacket requestPacket) {
    return MD5.create().digestHex(JSON.toJSONString(requestPacket));
  }

  public Boolean valid(LoginRequestPacket requestPacket) {
    String pwd = userMap.get(requestPacket.getUserName());
    return Objects.equals(requestPacket.getPassword(), pwd);
  }
}
