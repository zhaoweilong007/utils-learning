package com.zwl.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

/**
 * <P>byteBuff字节容器，netty的数据载体,
 * byteBuff通过引用计数的方式管理，需要手动释放，否则会造成内存泄漏 当创建完一个 ByteBuf，它的引用为1，然后每次调用 retain() 方法， 它的引用就加一， release()
 * 方法原理是将引用计数减一，减完之后如果发现引用计数为0，则直接回收 ByteBuf 底层的内存
 * </P>
 *
 * @author ZhaoWeiLong
 * @since 2021/7/26
 **/
public class ByteBuffTest {


  @Test
  public void test() {
    ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
    print("allocate ByteBuf(9, 100)", buffer);
    // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写
    buffer.writeBytes(new byte[]{1, 2, 3, 4});
    print("writeBytes(1,2,3,4)", buffer);
    // write 方法改变写指针，写完之后写指针未到 capacity 的时候，buffer 仍然可写, 写完 int 类型之后，写指针增加4
    buffer.writeInt(12);
    print("writeInt(12)", buffer);
    // write 方法改变写指针, 写完之后写指针等于 capacity 的时候，buffer 不可写
    buffer.writeBytes(new byte[]{5});
    print("writeBytes(5)", buffer);
    // write 方法改变写指针，写的时候发现 buffer 不可写则开始扩容，扩容之后 capacity 随即改变
    buffer.writeBytes(new byte[]{6});
    print("writeBytes(6)", buffer);
    // get 方法不改变读写指针
    System.out.println("getByte(3) return: " + buffer.getByte(3));
    System.out.println("getShort(3) return: " + buffer.getShort(3));
    System.out.println("getInt(3) return: " + buffer.getInt(3));
    print("getByte()", buffer);
    // set 方法不改变读写指针
    buffer.setByte(buffer.readableBytes() + 1, 0);
    print("setByte()", buffer);
    // read 方法改变读指针
    byte[] dst = new byte[buffer.readableBytes()];
    buffer.readBytes(dst);
    print("readBytes(" + dst.length + ")", buffer);
  }

  private void print(String action, ByteBuf buffer) {
    System.out.println("after ===========" + action + "============");
    System.out.println("capacity(): " + buffer.capacity());
    System.out.println("maxCapacity(): " + buffer.maxCapacity());
    System.out.println("readerIndex(): " + buffer.readerIndex());
    System.out.println("readableBytes(): " + buffer.readableBytes());
    System.out.println("isReadable(): " + buffer.isReadable());
    System.out.println("writerIndex(): " + buffer.writerIndex());
    System.out.println("writableBytes(): " + buffer.writableBytes());
    System.out.println("isWritable(): " + buffer.isWritable());
    System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());
    System.out.println();
  }
}
