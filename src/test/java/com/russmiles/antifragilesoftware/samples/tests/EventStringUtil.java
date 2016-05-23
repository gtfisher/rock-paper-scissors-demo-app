package com.russmiles.antifragilesoftware.samples.tests;


import com.russmiles.antifragilesoftware.samples.es.api.BaseEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventStringUtil {
	public static String toString(BaseEvent baseEvent) {
		String simpleName = baseEvent.getClass().getSimpleName();
		Map<String, Object> values = new HashMap<String, Object>();
		for (Field field : baseEvent.getClass().getFields()) {
			try {
				Object object = field.get(baseEvent);
				if (object instanceof UUID) {
					object = ((UUID)object).getLeastSignificantBits() % 255;
				}
				values.put(field.getName(), object);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
		return simpleName + values;
	}
}
