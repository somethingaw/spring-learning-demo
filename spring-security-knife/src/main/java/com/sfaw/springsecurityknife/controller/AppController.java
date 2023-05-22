package com.sfaw.springsecurityknife.controller;

import com.sfaw.springsecurityknife.dto.AppDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AppController
 * @author ArthurW
 * @version 1.0
 * @date 2023/5/18 14:03
 **/
@RestController
@RequestMapping("/app")
@Tag(name = "app信息")
public class AppController {

	public static Map<String, AppDTO> map = new HashMap<>();

	static {
		AppDTO appDTO = new AppDTO();
		appDTO.setCode("admin");
		appDTO.setSecret("admin");
		map.put("admin", appDTO);
	}

	@GetMapping("getApp")
	@ResponseBody
	public ResponseEntity<Object> getApp(HttpServletRequest request) {
		List<AppDTO> apps = new ArrayList<>();
		for (AppDTO appDTO : map.values()) {
			apps.add(appDTO);
		}
		return ResponseEntity.ok(apps);
	}

	@PostMapping("addApp")
	@ResponseBody
	public ResponseEntity<Object> addApp(HttpServletRequest request, String code) {
		AppDTO appDTO = new AppDTO();
		appDTO.setCode(code);
		appDTO.setSecret(code);
		map.put(code, appDTO);
		return ResponseEntity.ok(appDTO);
	}
}
