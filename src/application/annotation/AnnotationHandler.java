package application.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import application.bean.Article;

public class AnnotationHandler {

	public static void main(String[] args) {
		String name = findClazz(Article.class);
		System.out.println(name);
		findFieldAnno(Article.class);

	}

	public static String findClazz(Class<?> clazz){
		Entity entity = clazz.getAnnotation(Entity.class);
		String name = entity.name();
		return name;
	}

	public static void findMethedAnno(Class<?> clazz){
		while (clazz != null && !isSystemCalss(clazz.getName())) {
			Method[] allMethods = clazz.getDeclaredMethods();
			for (int i = 0; i < allMethods.length; i++) {
				Method method = allMethods[i];
//				Annotation[] anns = method.getDeclaredAnnotations();
				onClick annotation = method.getAnnotation(onClick.class);
				if (annotation != null) {
					// 获取方法参数
					Class<?>[] paramsTypeClass = method.getParameterTypes();
					for(Class cls : paramsTypeClass){
						System.out.println(cls.getSimpleName());
					}
				}
			}
		}
	}

	public static  void findFieldAnno(Class<?> clazz){
		while (clazz != null && !isSystemCalss(clazz.getName())) {
			Field[] allFields = clazz.getDeclaredFields();
			Map<String,String> columnsDefs =  new HashMap<>();
			for (int i = 0; i < allFields.length; i++) {
				Field field = allFields[i];
//				Annotation[] anns = field.getDeclaredAnnotations();
				Column column = field.getAnnotation(Column.class);
			}
		}
	}

	private static boolean isSystemCalss(String name) {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.");
    }

	private static Class<?> convertType(Class<?> eventType) {
        Class<?> returnClass = null;
        if (eventType.equals(boolean.class)) {
            returnClass = Boolean.class;
        } else if (eventType.equals(int.class)) {
            returnClass = Integer.class;
        } else if (eventType.equals(float.class)) {
            returnClass = Float.class;
        } else if (eventType.equals(double.class)) {
            returnClass = Double.class;
        }

        return returnClass;
    }
}
