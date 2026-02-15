package com.notification.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TemplateRenderService {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public String render(String template, String payloadJson) {

		if (template == null)
			return null;

		try {
			Map<String, Object> variables = objectMapper.readValue(payloadJson,
					new TypeReference<Map<String, Object>>() {
					});

			for (Map.Entry<String, Object> entry : variables.entrySet()) {
				template = template.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
			}

			return template;

		} catch (Exception e) {
			throw new RuntimeException("Template rendering failed");
		}
	}
}
