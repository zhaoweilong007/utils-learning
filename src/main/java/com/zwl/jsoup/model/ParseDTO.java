package com.zwl.jsoup.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

/**
 * @author zhao_wei_long
 * @since 2021/7/1
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParseDTO {

  private Document document;

  private Answer answer;


}
