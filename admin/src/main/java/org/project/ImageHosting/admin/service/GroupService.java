package org.project.ImageHosting.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.project.ImageHosting.admin.dao.entity.GroupDO;
import org.project.ImageHosting.admin.dto.req.ImageGroupSortReqDTO;
import org.project.ImageHosting.admin.dto.req.ImageGroupUpdateReqDTO;
import org.project.ImageHosting.admin.dto.resp.ImageGroupRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {
    /**
     * 新增图床分组
     */
    void saveGroup(String groupName);

    /**
     * 新增图床分组
     */
    void saveGroup(String username, String groupName);

    /**
     * 查询图床分组集合
     */
    List<ImageGroupRespDTO> listGroup();

    /**
     * 修改图床分组
     */
    void updateGroup(ImageGroupUpdateReqDTO reqParam);

    /**
     * 删除图床分组
     */
    void deleteGroup(String gid);

    /**
     * 图床分组排序
     */
    void sortGroup(List<ImageGroupSortReqDTO> reqParam);
}
