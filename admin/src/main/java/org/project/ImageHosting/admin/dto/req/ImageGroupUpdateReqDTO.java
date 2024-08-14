package org.project.ImageHosting.admin.dto.req;

import lombok.Data;

@Data
public class ImageGroupUpdateReqDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名
     */
    private String name;
}
