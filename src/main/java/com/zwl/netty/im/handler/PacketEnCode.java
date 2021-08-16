package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.Packet;
import com.zwl.netty.im.utils.PacketCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
public class PacketEnCode extends MessageToByteEncoder<Packet> {


  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf byteBuf)
      throws Exception {
    PacketCode.encode(byteBuf, packet);
  }
}
