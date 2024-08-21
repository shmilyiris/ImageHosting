package org.project.ImageHosting.admin.remote;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ImageHosting-project", url = "${aggregation.remote-url:}")
public interface ImageRemoteService {

}
