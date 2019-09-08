/**
 * projectName: mmall
 * fileName: IFileService.java
 * packageName: com.mmall.service
 * date: 2019-09-06 16:08
 * copyright(c) HanYu
 */
package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @version: V1.0
 * @author: HanYu
 * @interfaceName: IFileService
 * @packageName: com.mmall.service
 * @description:
 * @data: 2019-09-06 16:08
 **/
public interface IFileService {
    public String uplode(MultipartFile file, String path);
}