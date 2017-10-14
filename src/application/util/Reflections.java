package application.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class Reflections {
	private Class<?> clazz;
	private Object object;

	public Reflections(Object object) {
		if (object == null) return;
		this.object = object;
		this.clazz = object.getClass();
	}

	private static class NULL {}

	public interface MethodCallback{
		void done(Object returnValue);
	}

	public static Reflections newInstance(){
		return new Reflections(null);
	}

	public static Reflections on(Object object){
		return new Reflections(object);
	}

	private Map<String, Field> fields() {
        Map<String, Field> fieldsMap = new LinkedHashMap<String, Field>();
        Class tempClazz = clazz;
        while(tempClazz != null){
        	for(Field field : tempClazz.getDeclaredFields()){
        		if(!fieldsMap.containsKey(field.getName())){
        			fieldsMap.put(field.getName(), field);
        		}
        	}
        	tempClazz = tempClazz.getSuperclass();
        }
        return fieldsMap;
	}

    private static Class<?>[] types(Object... values) {
        if (values == null) {
            // 空
            return new Class[0];
        }

        Class<?>[] results = new Class[values.length];

        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            results[i] = value == null ? NULL.class : value.getClass();
        }

        return results;
    }

	public Reflections set(String key, Object value) {
		try {
			Field field = getField(key);
			field.set(object, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T create(Class<T> clazz, Object... args){
		try {
			Class[] parameterTypes = types(args);
			for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
				if(matchMethod(constructor.getParameterTypes(), parameterTypes)){
					constructor.setAccessible(true);
					return (T) constructor.newInstance(args);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Reflections callMethod(String methodName, MethodCallback callback, Object ... params) {
		try {
			Class[] parameterTypes = types(params);
			Method method = getMethod(methodName,parameterTypes);
			method.setAccessible(true);
			if(method.getReturnType() == void.class){
				method.invoke(object, params);
			}else{
				Object returnValue = method.invoke(object, params);
				if(callback != null) callback.done(returnValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}


	private Field getField(String key) {
		try {
			// 先尝试公有方法的获取(因为可以获取到继承的,范围更大)
			return this.clazz.getField(key);
		} catch (NoSuchFieldException e) {
			// 再尝试私有方法的获取(仅支持本类中的方法)
			try {
				Field field = this.clazz.getDeclaredField(key);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException ee) {
				throw new RuntimeException("No Such Field: " + key);
			}
		}
	}

	private Method getMethod(String methodName, Class[] parameterTypes) {
		// 匹配公有方法:
        for (Method method : this.clazz.getMethods()) {
            if (method.getName().equals(methodName) && matchMethod(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        // 匹配私有方法
        for (Method method : this.clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && matchMethod(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        throw new RuntimeException("No Such Method: " + methodName);
	}

	private boolean matchMethod(Class[] currentParamsTypes, Class[] parameterTypes) {
		if(currentParamsTypes.length == parameterTypes.length){
			for(int i = 0;i < parameterTypes.length;i++){
				if(parameterTypes[i] == NULL.class){
					continue;
				}
				if(wrapper(currentParamsTypes[i]).isAssignableFrom(wrapper(parameterTypes[i]))){
					continue;
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public Object get(String key) {
		try {
			Field field = getField(key);
			return field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	 public static Class<?> wrapper(Class<?> type) {
        if (type == null) {
            return null;
        } else if (type.isPrimitive()) { // isPrimitive 是否是基本类型
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }
        return type;
	 }
}
