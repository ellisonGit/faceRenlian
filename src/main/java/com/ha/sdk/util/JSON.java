package main.java.com.ha.sdk.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import main.java.com.ha.facecamera.configserver.pojo.PojoAdapter;

public final class JSON {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final List<String> supportFieldTyps = Arrays.asList("class java.util.Date",
			"class java.lang.Boolean","boolean",
			"class java.lang.Double","double",
			"class java.lang.Long","long",
			"class java.lang.Float","float",
			"class java.lang.Integer","int",
			"class java.lang.Short","short",
			"class java.lang.Byte","byte",
			"class java.lang.Character","char",
			"class java.lang.String",
			"class [J","class [[J",
			"class [I","class [[I",
			"class [S","class [[S",
			"class [C","class [[C",
			"class [B","class [[B",
			"class [Z","class [[Z"
			);
	
	static {
		Collections.sort(supportFieldTyps);
	}
	
	public interface Observer {
		/**
		 * 观察者
		 * 
		 * @return 如果返回false则代表这个字段不进行计算
		 * @throws IllegalAccessException 
		 * @throws IllegalArgumentException 
		 */
		boolean intercept(Object bean, Field f, StringBuilder sb) throws IllegalArgumentException, IllegalAccessException;
	}

	/**
	 * 对象格式化输出JSON字符串
	 * <br>
	 * 只支持Pojo对象（即类中只有基础数据类型才行）
	 * 
	 * @param bean 要转换的对象
	 * @return 输出的JSON格式字符串
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static String format(Object bean) throws IllegalArgumentException, IllegalAccessException {
		return format(bean, null);
	}
	
	public static String format(Object bean, Observer observer) throws IllegalArgumentException, IllegalAccessException {
		if(bean == null)
			return null;
		Class<?> clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sb = new StringBuilder("{");
		for(Field f : fields) {
			if(Modifier.isStatic(f.getModifiers())) continue;
			f.setAccessible(true);
			String xclass = f.getType().toString();
			if((observer == null || observer.intercept(bean, f, sb)) &&
					(Collections.binarySearch(supportFieldTyps, xclass) >= 0 || PojoAdapter.class.isAssignableFrom(f.getType()) ||
							(xclass.startsWith("class [L") && PojoAdapter.class.isAssignableFrom(f.getType().getComponentType())))) {
				sb.append('"').append(f.getName()).append("\":");
				if(PojoAdapter.class.isAssignableFrom(f.getType())) {
					sb.append(format(f.get(bean))); // observer??
				}
				else if (xclass.startsWith("class [L") && PojoAdapter.class.isAssignableFrom(f.getType().getComponentType())) {
					if(f.get(bean) == null) {
						sb.append("null");
					} else if(Array.getLength(f.get(bean)) == 0) {
						sb.append("[]");
					} else {
						Object a = f.get(bean);
						int l = Array.getLength(a);
						sb.append("[");
						for(int i = 0; i < l ; ++i) {
							sb.append(format(Array.get(a, i))).append(","); // observer??
						}
						sb.setCharAt(sb.length() - 1, ']');
					}
				}
				else if ("class java.util.Date".equals(xclass)) {
					Object d = f.get(bean);
					if(d == null)
						sb.append("null");
					else {
						String sv = sdf.format(d);
						sb.append('"').append(sv).append('"');
					}
				}
				else if ("class java.lang.Boolean".equals(xclass) || "boolean".equals(xclass)) {
					sb.append(f.getBoolean(bean));
				}
				else if ("class java.lang.Double".equals(xclass) || "double".equals(xclass)) {
					sb.append(f.getDouble(bean));
				}
				else if ("class java.lang.Long".equals(xclass) || "long".equals(xclass)) {
					sb.append(f.getLong(bean));
				}
				else if ("class java.lang.Float".equals(xclass) || "float".equals(xclass)) {
					sb.append(f.getFloat(bean));
				}
				else if ("class java.lang.Integer".equals(xclass) || "int".equals(xclass)) {
					sb.append(f.getInt(bean));
				}
				else if ("class java.lang.Short".equals(xclass) || "short".equals(xclass)) {
					sb.append(f.getShort(bean));
				}
				else if ("class java.lang.Byte".equals(xclass) || "byte".equals(xclass)) {
					sb.append(f.getByte(bean));
				}
				else if ("class java.lang.Character".equals(xclass) || "char".equals(xclass)) {
					sb.append(f.getChar(bean));
				}
				else if ("class java.lang.String".equals(xclass)) {
					Object d = f.get(bean);
					if(d == null)
						sb.append("null");
					else {
						sb.append('"').append(d).append('"');
					}
				}
				else if(xclass.startsWith("class [[")) {
					sb.append(Arrays.deepToString((Object[])f.get(bean)));
				}
				else if("class [J".equals(xclass)) {
					sb.append(Arrays.toString((long[])f.get(bean)));
				}
				else if("class [I".equals(xclass)) {
					sb.append(Arrays.toString((int[])f.get(bean)));
				}
				else if("class [S".equals(xclass)) {
					sb.append(Arrays.toString((short[])f.get(bean)));
				}
				else if("class [C".equals(xclass)) {
					sb.append(Arrays.toString((char[])f.get(bean)));
				}
				else if("class [B".equals(xclass)) {
					sb.append(Arrays.toString((byte[])f.get(bean)));
				}
				else if("class [Z".equals(xclass)) {
					sb.append(Arrays.toString((boolean[])f.get(bean)));
				}
				sb.append(',');
			}
		}
		if(sb.charAt(sb.length() - 1) == ',') {
			sb.setCharAt(sb.length() - 1, '}');
		}
		else
			sb.append("}");
		return sb.toString();
	}
}
