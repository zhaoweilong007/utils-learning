// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Demo.proto

package com.zwl.protocolbuffer;

public interface ResultOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Result)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 code = 1;</code>
   * @return The code.
   */
  int getCode();

  /**
   * <code>string msg = 2;</code>
   * @return The msg.
   */
  java.lang.String getMsg();
  /**
   * <code>string msg = 2;</code>
   * @return The bytes for msg.
   */
  com.google.protobuf.ByteString
      getMsgBytes();

  /**
   * <code>.ResultType type = 3;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <code>.ResultType type = 3;</code>
   * @return The type.
   */
  com.zwl.protocolbuffer.ResultType getType();
}
