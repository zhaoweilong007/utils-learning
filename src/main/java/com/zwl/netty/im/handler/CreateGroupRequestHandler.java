package com.zwl.netty.im.handler;

import cn.hutool.core.util.IdUtil;
import com.zwl.netty.im.model.CreateGroupRequestPacket;
import com.zwl.netty.im.model.CreateGroupRespPacket;
import com.zwl.netty.im.utils.LogUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 创建群聊处理器
 *
 * @author ZhaoWeiLong
 * @since 2021/8/19
 **/
@Slf4j
public class CreateGroupRequestHandler extends
    SimpleChannelInboundHandler<CreateGroupRequestPacket> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx,
      CreateGroupRequestPacket createGroupRequestPacket) throws Exception {
    List<String> userIds = createGroupRequestPacket.getUserIds();
    DefaultChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
    List<String> userNames = userIds.stream().map(id -> {
      Channel channel = LogUtils.getChannel(id);
      if (channel != null) {
        channelGroup.add(channel);
        return LogUtils.getSession(channel).getUserName();
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());

    String uuid = IdUtil.randomUUID();
    String groupId = uuid.substring(0, uuid.indexOf("-"));
    LogUtils.putGroupMap(groupId,channelGroup);
    CreateGroupRespPacket respPacket = new CreateGroupRespPacket();
    respPacket.setGroupId(groupId);
    respPacket.setSuccess(true);
    respPacket.setUserNames(userNames);
    ctx.channel().writeAndFlush(respPacket);
    log.info("创建群聊成功,groupId:{},群成员：{}", groupId, userNames);
  }
}
