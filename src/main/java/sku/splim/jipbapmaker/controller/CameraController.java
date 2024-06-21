package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sku.splim.jipbapmaker.service.CameraService;

@RestController
@RequestMapping("/api/camera")
public class CameraController {

    private final CameraService cameraService;

    @Autowired
    private CameraController (CameraService cameraService) {
        this.cameraService =  cameraService;
    }

    /**
     * 객체 검출 API를 호출합니다.
     *
     * @param imageFile 업로드된 이미지 파일
     * @return API 응답에서 추출한 클래스 정보 (중복 없이)
     */
    @PostMapping("/detect")
    public String detectObject(@RequestParam("imageFile") MultipartFile imageFile) {
        return cameraService.callApi(imageFile);
    }
}