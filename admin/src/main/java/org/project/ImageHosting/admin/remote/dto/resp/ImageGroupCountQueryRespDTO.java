package org.project.ImageHosting.admin.remote.dto.resp;

import lombok.Data;

@Data
public class ImageGroupCountQueryRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 图片数量
     */
    private Integer imageCount;
}
