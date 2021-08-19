package com.zwl.netty.im.command;

import com.zwl.netty.im.model.JoinGroupReqPacket;
import io.netty.channel.Channel;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@Slf4j
public class JoinGroupCommand implements ConsoleCommand {

  @Override
  public void exec(Scanner scanner, Channel channel) {
    log.info("【拉人群聊】,输入群聊id+空格+用户id，多个id用逗号隔开");
    String command = scanner.nextLine();
    JoinGroupReqPacket reqPacket = new JoinGroupReqPacket();
    reqPacket.setGroupId(command);
    channel.writeAndFlush(reqPacket);
  }
}
