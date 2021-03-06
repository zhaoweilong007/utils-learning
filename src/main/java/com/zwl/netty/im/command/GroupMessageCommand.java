package com.zwl.netty.im.command;

import com.zwl.netty.im.model.GroupMessageReqPacket;
import io.netty.channel.Channel;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/20
 **/
@Slf4j
public class GroupMessageCommand implements ConsoleCommand {

  @Override
  public void exec(Scanner scanner, Channel channel) {
    log.info("【发送群聊消息】,请输入群聊id+空格+消息");
    String command = scanner.nextLine();
    String[] split = command.split(" ");
    GroupMessageReqPacket packet = new GroupMessageReqPacket();
    packet.setGroupId(split[0]);
    packet.setMessage(split[1]);
    channel.writeAndFlush(packet);
  }
}
