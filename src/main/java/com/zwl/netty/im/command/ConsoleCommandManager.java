package com.zwl.netty.im.command;

import io.netty.channel.Channel;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * 指令管理器
 *
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@Slf4j
public class ConsoleCommandManager implements ConsoleCommand {

  private final Map<String, ConsoleCommand> consoleCommandMap = new ConcurrentHashMap<>();

  {
    consoleCommandMap.put("sendToUser", new SendToUserCommand());
    consoleCommandMap.put("logout", new LoginOutCommand());
    consoleCommandMap.put("createGroup", new CreateGroupCommand());
    consoleCommandMap.put("joinGroup", new JoinGroupCommand());
    consoleCommandMap.put("listGroup", new ListGroupCommand());
    consoleCommandMap.put("quitGroup", new QuitGroupCommand());
  }

  @Override
  public void exec(Scanner scanner, Channel channel) {
    log.info("请输入控制指令:{}", consoleCommandMap.keySet());
    String next = scanner.nextLine();
    ConsoleCommand consoleCommand = consoleCommandMap.get(next);
    if (consoleCommand == null) {
      log.warn("你输入的指令无效！");
      return;
    }
    consoleCommand.exec(scanner, channel);
  }
}
