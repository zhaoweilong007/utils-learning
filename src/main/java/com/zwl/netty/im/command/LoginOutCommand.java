package com.zwl.netty.im.command;

import com.zwl.netty.im.model.LoginOutRequestPacket;
import com.zwl.netty.im.utils.LogUtils;
import io.netty.channel.Channel;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * 登出指令
 *
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@Slf4j
public class LoginOutCommand implements ConsoleCommand {

  @Override
  public void exec(Scanner scanner, Channel channel) {
    log.info("【退出系统】,输入Y/N确认");
    String command = scanner.nextLine();
    if ("y".equalsIgnoreCase(command)) {
      LoginOutRequestPacket packet = new LoginOutRequestPacket();
      packet.setUserId(LogUtils.getSession(channel).getUserId());
      channel.writeAndFlush(packet);
    } else if ("n".equalsIgnoreCase(command)) {
      log.info("已取消");
    } else {
      log.warn("输入指令错误");
    }
  }
}
