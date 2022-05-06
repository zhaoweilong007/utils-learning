package com.zwl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.zwl.json.A;
import com.zwl.json.B;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 描述：树形菜单转换
 *
 * @author zwl
 * @since 2022/5/6 10:04
 */
public class TreeConvertTest {

  @Test
  public void test() {
    String json =
        FileUtil.readString(ResourceUtil.getResource("mock.json"), StandardCharsets.UTF_8);
    List<A> array = JSON.parseArray(json, A.class);

    // 获取父节点
    List<B> collect =
        array.stream()
            .filter(m -> StrUtil.isEmpty(m.getFbm()))
            .map(
                (m) -> {
                  B b = new B();
                  b.setId(m.getGsxh());
                  b.setName(m.getFmc());
                  b.setBm(m.getBm());
                  b.setFbm(b.getFbm());
                  b.setCompany(getChildrens(m, array));
                  return b;
                })
            .collect(Collectors.toList());
    System.out.println("-------转json输出结果-------");
    System.out.println(JSON.toJSONString(collect, true));
  }

  /**
   * 递归查询子节点
   *
   * @param root 根节点
   * @param all 所有节点
   * @return 根节点信息
   */
  private List<B> getChildrens(A root, List<A> all) {
    return all.stream()
        .filter(
            m ->
                Objects.equals(root.getBm(), m.getFbm())
                    && !Objects.equals(root.getGsxh(), m.getGsxh()))
        .map(
            (m) -> {
              B b = new B();
              b.setId(m.getGsxh());
              b.setName(m.getFmc());
              b.setBm(m.getBm());
              b.setFbm(b.getFbm());
              b.setCompany(getChildrens(m, all));
              return b;
            })
        .collect(Collectors.toList());
  }
}
