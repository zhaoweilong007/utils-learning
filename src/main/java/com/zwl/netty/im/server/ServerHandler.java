package com.zwl.netty.im.server;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.zwl.netty.im.model.LoginRequestPacket;
import com.zwl.netty.im.model.LoginRespPacket;
import com.zwl.netty.im.model.MessageRequestPacket;
import com.zwl.netty.im.model.MessageResponsePacket;
import com.zwl.netty.im.model.Packet;
import com.zwl.netty.im.utils.LogUtils;
import com.zwl.netty.im.utils.PacketCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/7/29
 **/
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

  private static final Map<String, String> userMap = new HashMap<>() {{
    put("zhangsan", "123456");
  }};

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    Packet packet = PacketCode.decode(((ByteBuf) msg));
    if (packet == null) {
      return;
    }
    Packet respPacket = null;
    String date = new DateTime().toString(DatePattern.NORM_DATETIME_PATTERN);
    if (packet instanceof LoginRequestPacket) {
      LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
      LoginRespPacket loginRespPacket = new LoginRespPacket();
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
      respPacket = loginRespPacket;
    } else if (packet instanceof MessageRequestPacket) {
      MessageRequestPacket requestPacket = (MessageRequestPacket) packet;
      String message = requestPacket.getMessage();
      MessageResponsePacket responsePacket = new MessageResponsePacket();
      responsePacket.setMessage(message);
      respPacket = responsePacket;
    }
    ctx.writeAndFlush(PacketCode.encode(ctx.alloc().buffer(), respPacket));
  }


  public String generateToken(LoginRequestPacket requestPacket) {
    return MD5.create().digestHex(JSON.toJSONString(requestPacket));
  }

  public Boolean valid(LoginRequestPacket requestPacket) {
    String pwd = userMap.get(requestPacket.getUserName());
    return Objects.equals(requestPacket.getPassword(), pwd);
  }
}
