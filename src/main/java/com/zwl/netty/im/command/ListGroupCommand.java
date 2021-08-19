package com.zwl.netty.im.command;

import com.zwl.netty.im.model.ListGroupReqPacket;
import io.netty.channel.Channel;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@Slf4j
public class ListGroupCommand implements ConsoleCommand {

  @Override
  public void exec(Scanner scanner, Channel channel) {
    log.info("【列出群聊】，请输入群聊id");
    String command = scanner.nextLine();
    ListGroupReqPacket reqPacket = new ListGroupReqPacket();
    reqPacket.setGroupId(command);
    channel.writeAndFlush(reqPacket);
  }
}
