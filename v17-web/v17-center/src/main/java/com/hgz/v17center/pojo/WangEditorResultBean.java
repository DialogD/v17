package com.hgz.v17center.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: DJA
 * @Date: 2019/11/1 12:36
 * 针对WangEditor数据封装返回的bean
 */
@Data
@AllArgsConstructor
public class WangEditorResultBean {
    private String errno;
    private String[] data;
}
