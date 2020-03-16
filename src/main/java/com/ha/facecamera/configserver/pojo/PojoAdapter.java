package main.java.com.ha.facecamera.configserver.pojo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import main.java.com.ha.sdk.util.JSON;

public abstract class PojoAdapter implements Cloneable {

	public Object clone() {
		Class<?> clazz = this.getClass();
		Field[] fields = clazz.getDeclaredFields();
		try {
			Constructor<?> ctor = clazz.getDeclaredConstructor();
			ctor.setAccessible(true);
			Object that = ctor.newInstance();
			for(Field f : fields) {
				if(Modifier.isStatic(f.getModifiers())) continue;
				f.setAccessible(true);
				f.set(that, f.get(this));
			}
			return that;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String toJson() {
		try {
			return JSON.format(this);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return super.toString();
	}
	
	@Override
	public String toString() {
		return toJson();
	}
}
