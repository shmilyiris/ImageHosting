package org.project.ImageHosting.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.project.ImageHosting.admin.dao.mapper") // 持久层扫描注解，扫描持久层接口对应的地址，一般要具体到扫描的包路径
public class ImageHostingAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImageHostingAdminApplication.class, args);
    }
}
