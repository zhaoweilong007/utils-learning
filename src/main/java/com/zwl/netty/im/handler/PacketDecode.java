package com.zwl.netty.im.handler;

import com.zwl.netty.im.utils.PacketCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * 解码
 *
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
public class PacketDecode extends ByteToMessageDecoder {


  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
      List<Object> list) throws Exception {
    list.add(PacketCode.decode(byteBuf));
  }
}
