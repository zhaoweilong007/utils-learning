package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.Packet;
import com.zwl.netty.im.utils.PacketCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 编码
 *
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
@Slf4j
public class PacketEnCode extends MessageToByteEncoder<Packet> {


  @Override
  protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf byteBuf)
      throws Exception {
    log.debug("encode:{}", packet);
    PacketCode.encode(byteBuf, packet);
  }
}
