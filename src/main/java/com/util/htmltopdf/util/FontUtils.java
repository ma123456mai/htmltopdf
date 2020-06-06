package com.util.htmltopdf.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.awt.*;
import java.io.InputStream;
import java.util.List;
import java.util.*;

public class FontUtils {
	private final static List<String> fontPaths = new ArrayList<String>();
	private static final Map<String, Font> PATH_FONTS = new HashMap<String, Font>();

	public static List<String> getFonts() throws Exception {
		if (fontPaths.isEmpty()) {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath:/font/*");
			for (Resource resource : resources) {
				String filePath = "/font/" + resource.getFilename();
				System.out.println(String.format("加载字体:%s", filePath));
				fontPaths.add(filePath);
			}
		}
		return fontPaths;
	}

	public static String getCanDisplayFontFamily(char c, String... fontPaths) {
		Font font = getFont(fontPaths[0]);
		if (!font.canDisplay(c)) {
			String family = font.getFamily(Locale.US);
			for (int i = 1; i < fontPaths.length; i++) {
				Font spareFont = getFont(fontPaths[i]);
				if (spareFont.canDisplay(c)) {
					String spareFamily = spareFont.getFamily(Locale.US);
					System.out.println(String.format("字符:%s,字体:%s,无法正常显示,使用备用字体:%s", c, family, spareFamily));
					return spareFamily;
				}
			}
			throw new RuntimeException(String.format("字符:%s,字体:%s,无法正常显示", c, family));
		}
		return null;
	}

	private static Font getFont(String fontPath) {
		Font font = PATH_FONTS.get(fontPath);
		if (font == null) {
			InputStream inputStream = FontUtils.class.getResourceAsStream(fontPath);
			try {
				font = Font.createFont(0, inputStream);
				System.out.println(fontPath + ":" + font.getFamily(Locale.US));
				PATH_FONTS.put(fontPath, font);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return font;
	}

}
