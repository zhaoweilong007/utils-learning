package com.zwl.netty.im.handler;

import com.zwl.netty.im.model.Packet;
import com.zwl.netty.im.utils.PacketCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 解码
 *
 * @author ZhaoWeiLong
 * @since 2021/8/16
 **/
@Slf4j
public class PacketDecode extends ByteToMessageDecoder {


  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
      List<Object> list) throws Exception {
    Packet packet = PacketCode.decode(byteBuf);
    log.debug("decode:{}",packet);
    list.add(packet);
  }
}
