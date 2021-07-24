package input;

/**
 * @author Juyas
 */
public class Keys {
	/** Printable keys. */
	public static final int
			AZ_KEY_SPACE         = 32,
			AZ_KEY_APOSTROPHE    = 39,
			AZ_KEY_COMMA         = 44,
			AZ_KEY_MINUS         = 45,
			AZ_KEY_PERIOD        = 46,
			AZ_KEY_SLASH         = 47,
			AZ_KEY_0             = 48,
			AZ_KEY_1             = 49,
			AZ_KEY_2             = 50,
			AZ_KEY_3             = 51,
			AZ_KEY_4             = 52,
			AZ_KEY_5             = 53,
			AZ_KEY_6             = 54,
			AZ_KEY_7             = 55,
			AZ_KEY_8             = 56,
			AZ_KEY_9             = 57,
			AZ_KEY_SEMICOLON     = 59,
			AZ_KEY_EQUAL         = 61,
			AZ_KEY_A             = 65,
			AZ_KEY_B             = 66,
			AZ_KEY_C             = 67,
			AZ_KEY_D             = 68,
			AZ_KEY_E             = 69,
			AZ_KEY_F             = 70,
			AZ_KEY_G             = 71,
			AZ_KEY_H             = 72,
			AZ_KEY_I             = 73,
			AZ_KEY_J             = 74,
			AZ_KEY_K             = 75,
			AZ_KEY_L             = 76,
			AZ_KEY_M             = 77,
			AZ_KEY_N             = 78,
			AZ_KEY_O             = 79,
			AZ_KEY_P             = 80,
			AZ_KEY_Q             = 81,
			AZ_KEY_R             = 82,
			AZ_KEY_S             = 83,
			AZ_KEY_T             = 84,
			AZ_KEY_U             = 85,
			AZ_KEY_V             = 86,
			AZ_KEY_W             = 87,
			AZ_KEY_X             = 88,
			AZ_KEY_Y             = 89,
			AZ_KEY_Z             = 90,
			AZ_KEY_LEFT_BRACKET  = 91,
			AZ_KEY_BACKSLASH     = 92,
			AZ_KEY_RIGHT_BRACKET = 93,
			AZ_KEY_GRAVE_ACCENT  = 96,
			AZ_KEY_WORLD_1       = 161,
			AZ_KEY_WORLD_2       = 162;

	/** Function keys. */
	public static final int
			AZ_KEY_ESCAPE        = 256,
			AZ_KEY_ENTER         = 257,
			AZ_KEY_TAB           = 258,
			AZ_KEY_BACKSPACE     = 259,
			AZ_KEY_INSERT        = 260,
			AZ_KEY_DELETE        = 261,
			AZ_KEY_RIGHT         = 262,
			AZ_KEY_LEFT          = 263,
			AZ_KEY_DOWN          = 264,
			AZ_KEY_UP            = 265,
			AZ_KEY_PAGE_UP       = 266,
			AZ_KEY_PAGE_DOWN     = 267,
			AZ_KEY_HOME          = 268,
			AZ_KEY_END           = 269,
			AZ_KEY_CAPS_LOCK     = 280,
			AZ_KEY_SCROLL_LOCK   = 281,
			AZ_KEY_NUM_LOCK      = 282,
			AZ_KEY_PRINT_SCREEN  = 283,
			AZ_KEY_PAUSE         = 284,
			AZ_KEY_F1            = 290,
			AZ_KEY_F2            = 291,
			AZ_KEY_F3            = 292,
			AZ_KEY_F4            = 293,
			AZ_KEY_F5            = 294,
			AZ_KEY_F6            = 295,
			AZ_KEY_F7            = 296,
			AZ_KEY_F8            = 297,
			AZ_KEY_F9            = 298,
			AZ_KEY_F10           = 299,
			AZ_KEY_F11           = 300,
			AZ_KEY_F12           = 301,
			AZ_KEY_F13           = 302,
			AZ_KEY_F14           = 303,
			AZ_KEY_F15           = 304,
			AZ_KEY_F16           = 305,
			AZ_KEY_F17           = 306,
			AZ_KEY_F18           = 307,
			AZ_KEY_F19           = 308,
			AZ_KEY_F20           = 309,
			AZ_KEY_F21           = 310,
			AZ_KEY_F22           = 311,
			AZ_KEY_F23           = 312,
			AZ_KEY_F24           = 313,
			AZ_KEY_F25           = 314,
			AZ_KEY_KP_0          = 320,
			AZ_KEY_KP_1          = 321,
			AZ_KEY_KP_2          = 322,
			AZ_KEY_KP_3          = 323,
			AZ_KEY_KP_4          = 324,
			AZ_KEY_KP_5          = 325,
			AZ_KEY_KP_6          = 326,
			AZ_KEY_KP_7          = 327,
			AZ_KEY_KP_8          = 328,
			AZ_KEY_KP_9          = 329,
			AZ_KEY_KP_DECIMAL    = 330,
			AZ_KEY_KP_DIVIDE     = 331,
			AZ_KEY_KP_MULTIPLY   = 332,
			AZ_KEY_KP_SUBTRACT   = 333,
			AZ_KEY_KP_ADD        = 334,
			AZ_KEY_KP_ENTER      = 335,
			AZ_KEY_KP_EQUAL      = 336,
			AZ_KEY_LEFT_SHIFT    = 340,
			AZ_KEY_LEFT_CONTROL  = 341,
			AZ_KEY_LEFT_ALT      = 342,
			AZ_KEY_LEFT_SUPER    = 343,
			AZ_KEY_RIGHT_SHIFT   = 344,
			AZ_KEY_RIGHT_CONTROL = 345,
			AZ_KEY_RIGHT_ALT     = 346,
			AZ_KEY_RIGHT_SUPER   = 347,
			AZ_KEY_MENU          = 348,
			AZ_KEY_LAST          = AZ_KEY_MENU;

	/** If this bit is set one or more Shift keys were held down. */
	public static final int AZ_MOD_SHIFT = 0x1;

	/** If this bit is set one or more Control keys were held down. */
	public static final int AZ_MOD_CONTROL = 0x2;

	/** If this bit is set one or more Alt keys were held down. */
	public static final int AZ_MOD_ALT = 0x4;

	/** If this bit is set one or more Super keys were held down. */
	public static final int AZ_MOD_SUPER = 0x8;

	/** If this bit is set the Caps Lock key is enabled and the LOCK_KEY_MODS input mode is set. */
	public static final int AZ_MOD_CAPS_LOCK = 0x10;

	/** If this bit is set the Num Lock key is enabled and the LOCK_KEY_MODS input mode is set. */
	public static final int AZ_MOD_NUM_LOCK = 0x20;
}
