package com.guqiankun.devhelper.requestcapture.support.json;

/**
 * @author guqiankun
 *
 */
public class CharTypes {

	private final static boolean[] HEX_FLAGS = new boolean[256];
	static {
		for (char c = 0; c < HEX_FLAGS.length; ++c) {
			if (c >= 'A' && c <= 'F') {
				HEX_FLAGS[c] = true;
			} else if (c >= 'a' && c <= 'f') {
				HEX_FLAGS[c] = true;
			} else if (c >= '0' && c <= '9') {
				HEX_FLAGS[c] = true;
			}
		}
	}

	public static boolean isHex(char c) {
		return c < 256 && HEX_FLAGS[c];
	}

	public static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private final static boolean[] FIRST_IDENTIFIER_FLAGS = new boolean[256];
	static {
		for (char c = 0; c < FIRST_IDENTIFIER_FLAGS.length; ++c) {
			if (c >= 'A' && c <= 'Z') {
				FIRST_IDENTIFIER_FLAGS[c] = true;
			} else if (c >= 'a' && c <= 'z') {
				FIRST_IDENTIFIER_FLAGS[c] = true;
			}
		}
		FIRST_IDENTIFIER_FLAGS['`'] = true;
		FIRST_IDENTIFIER_FLAGS['_'] = true;
		FIRST_IDENTIFIER_FLAGS['$'] = true;
	}

	public static boolean isFirstIdentifierChar(char c) {
		if (c <= FIRST_IDENTIFIER_FLAGS.length) {
			return FIRST_IDENTIFIER_FLAGS[c];
		}
		return c != '　' && c != '，';
	}

	private final static boolean[] IDENTIFIER_FLAGS = new boolean[256];
	static {
		for (char c = 0; c < IDENTIFIER_FLAGS.length; ++c) {
			if (c >= 'A' && c <= 'Z') {
				IDENTIFIER_FLAGS[c] = true;
			} else if (c >= 'a' && c <= 'z') {
				IDENTIFIER_FLAGS[c] = true;
			} else if (c >= '0' && c <= '9') {
				IDENTIFIER_FLAGS[c] = true;
			}
		}
		// identifierFlags['`'] = true;
		IDENTIFIER_FLAGS['_'] = true;
		IDENTIFIER_FLAGS['$'] = true;
		IDENTIFIER_FLAGS['#'] = true;
	}

	public static boolean isIdentifierChar(char c) {
		if (c <= IDENTIFIER_FLAGS.length) {
			return IDENTIFIER_FLAGS[c];
		}
		return c != '　' && c != '，';
	}

	private final static boolean[] WHITESPACE_FLAGS = new boolean[256];
	static {
		for (int i = 0; i <= 32; ++i) {
			WHITESPACE_FLAGS[i] = true;
		}

		WHITESPACE_FLAGS[0x1A] = false;
		for (int i = 0x7F; i <= 0xA0; ++i) {
			WHITESPACE_FLAGS[i] = true;
		}

		WHITESPACE_FLAGS[160] = true; // 特别处理
	}

	/**
	 * @return false if {@link LayoutCharacters#EOI}
	 */
	public static boolean isWhitespace(char c) {
		return (c <= WHITESPACE_FLAGS.length && WHITESPACE_FLAGS[c]) || c == '　'; // Chinese space
	}

}
