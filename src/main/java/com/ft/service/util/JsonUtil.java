package com.ft.service.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class JsonUtil {

	public static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Convert plain object to dot annotated map
	 * 
	 * @param currentPath
	 * @param jsonNode
	 * @param map
	 */
	public static void plainToFlattenObject(String currentPath, JsonNode jsonNode, Map<String, String> map) {
		if (jsonNode.isObject()) {
			ObjectNode objectNode = (ObjectNode) jsonNode;
			Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
			String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

			while (iter.hasNext()) {
				Map.Entry<String, JsonNode> entry = iter.next();
				plainToFlattenObject(pathPrefix + entry.getKey(), entry.getValue(), map);
			}
		} else if (jsonNode.isArray()) {
			ArrayNode arrayNode = (ArrayNode) jsonNode;
			for (int i = 0; i < arrayNode.size(); i++) {
				plainToFlattenObject(currentPath + "[" + i + "]", arrayNode.get(i), map);
			}
		} else if (jsonNode.isValueNode()) {
			ValueNode valueNode = (ValueNode) jsonNode;
			map.put(currentPath, valueNode.asText());
		}
	}

	/**
	 * Convert a JSON string into Map with flatten attributes
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> toPlaceholders(String json) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		plainToFlattenObject("", objectMapper.readTree(json), map);
		return map;
	}

	/**
	 * Convert nested object into flatten one using toString
	 * 
	 * @param map
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static Map<String, String> toPlaceholders(Map<String, Object> map)
			throws JsonProcessingException, IOException {
		return toPlaceholders(objectMapper.writeValueAsString(map));
	}

}
