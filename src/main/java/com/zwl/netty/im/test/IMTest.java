package com.zwl.netty.im.test;

import com.zwl.netty.im.model.LoginRequestPacket;
import com.zwl.netty.im.model.Packet;
import com.zwl.netty.im.serialize.JsonSerializerImpl;
import com.zwl.netty.im.serialize.PacketCode;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ZhaoWeiLong
 * @since 2021/7/27
 **/
public class IMTest {

  @Test
  public void testSerializer() {
    JsonSerializerImpl serializer = new JsonSerializerImpl();

    LoginRequestPacket packet = new LoginRequestPacket();
    packet.setUserId("12345678");
    packet.setUserName("admin");
    packet.setPassword("admin");
    packet.setVersion(((byte) 1));

    ByteBuf byteBuf = PacketCode.encode(packet);

    Packet decode = PacketCode.decode(byteBuf);

    Assert.assertArrayEquals(serializer.serialize(packet), serializer.serialize(decode));

  }

}
