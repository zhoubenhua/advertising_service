package com.zbh.advertising_service.utils;

import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

public class UploadImgUtil {
    /**
     * 文件上传公共方法
     *
     * @param imgFile
     *            单文件
     * @return
     */
    public Map<String, Object> uploadImg(MultipartFile imgFile) {
        Map<String, Object> map = Maps.newHashMap();
        // 文件保存目录URL
        String saveUrl = "/www/goods/";
        // 最大文件大小  5M
        long maxSize = 5242880;

        if (imgFile == null) {
            return returnErrorMap("请选择文件!");
        }
        String imgFileFileName = imgFile.getOriginalFilename();
        String fileType = imgFileFileName.substring(imgFileFileName.lastIndexOf(".") + 1).toLowerCase();// 文件类型
        Map<String, String> fileTypeMap = Maps.newHashMap();
        fileTypeMap.put("image", "gif,jpg,jpeg,png,bmp");
        if (fileTypeMap.containsKey(fileType)) {
            return returnErrorMap("上传文件扩展名[" + fileType + "]是不允许的扩展名。");
        }

        if (imgFile.getSize() > maxSize) {
            return returnErrorMap(
                    "[ " + imgFileFileName + " ]超过单个文件大小限制，文件大小[ " + imgFile.getSize() + " ]，限制为[ " + maxSize + " ] ");
        }
        String newFileName = System.currentTimeMillis() + "." + fileType;// 重新命名
        try {
            FileUtils.copyInputStreamToFile(imgFile.getInputStream(), new File(saveUrl, newFileName));// 生成文件
            map.put("code","0");
            map.put("url","https://laiwo.net.cn"+saveUrl+newFileName);
            return map;
        } catch (Exception e) {
            return returnErrorMap("图片上传失败");
        }
    }

//    /**
//     *
//     * @param response
//     * @param request
//     * @param imgFiles
//     *            多文件
//     * @return
//     */
//    @RequestMapping("/upload")
//    @ResponseBody
//    public String uploadImgs(HttpServletResponse response, HttpServletRequest request,
//                                           @RequestParam("imgFiles") MultipartFile[] imgFiles) {
//        response.setContentType("text/plain;charset=UTF-8");
//        String url = "";
//        JSONObject obj = new JSONObject();// 必须返回json格式否则swfupload.swf无法解析报错
//        try {
//            for (MultipartFile myFile : imgFiles) {
//                Map<String,Object> imgPath = uploadImg(response, request, myFile);// 上传方法
//                if (imgPath.get("code").equals("0")) {
//                    url += imgPath.get("url") + ",";
//                }
//            }
//            obj.put("code", 0);// 上传成功
//            if (url.length() > 0) {
//                obj.put("url", url.substring(0, url.length() - 1)); // 上传成功的所有的图片地址的路径
//            } else {
//                obj.put("url", url);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            obj.put("code", 1);// 上传失败
//            obj.put("url", url);
//        }
//        return obj.toString();
//    }

    /**
     * 错误提示
     *
     * @param message
     * @return
     */
    private Map<String, Object> returnErrorMap(String message) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", 1);
        map.put("message", message);
        return map;
    }

}
