package org.project.ImageHosting.admin.dto.req;

import lombok.Data;

@Data
public class ImageGroupSortReqDTO {
    /**
     * 分组ID
     */
    private String gid;

    /**
     * 排序
     */
    private Integer sortOrder;
}
