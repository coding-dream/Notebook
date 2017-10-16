package application.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import application.util.ThreadUtils;
import javafx.application.Platform;

public class AnnotationHandler {
	private static CopyOnWriteArrayList<TargetMethod> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

	public static void main(String[] args) {
//		String name = findClazzAnno(Article.class);
//		System.out.println(name);
//		findFieldAnno(Article.class);
	}

	private static String findClazzAnno(Class<?> clazz){
		Entity entity = clazz.getAnnotation(Entity.class);
		String name = entity.name();
		return name;
	}

	private static void findMethedAnno(Object subscriber){
		Class<?> clazz = subscriber.getClass();
		while (clazz != null && !isSystemCalss(clazz.getName())) {
			Method[] allMethods = clazz.getDeclaredMethods();
			for (int i = 0; i < allMethods.length; i++) {
				Method method = allMethods[i];
//				Annotation[] anns = method.getDeclaredAnnotations();
				ThreadMode annotation = method.getAnnotation(ThreadMode.class);
				if (annotation != null) {
					// 获取方法参数
					Class<?>[] paramsTypeClass = method.getParameterTypes();
					// 仅支持一个参数的消息发送
					if(paramsTypeClass != null && paramsTypeClass.length == 1){
						TargetMethod subscribeMethod = new TargetMethod(method,subscriber,annotation.tag(),annotation.mode());
						copyOnWriteArrayList.add(subscribeMethod);
					}else{
						throw new RuntimeException("注解方法参数错误,仅支持一个参数的注解方法");
					}
				}
			}
			break;
		}
	}

	private static void findFieldAnno(Class<?> clazz){
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

	public static void register(Object object) {
		findMethedAnno(object);
	}

	public static void sendMessage(String tag,String methodParam) {
		for(TargetMethod targetMethod : copyOnWriteArrayList){
			if(tag.equals(targetMethod.getTag())){
				handleMessage(targetMethod,methodParam);
			}
		}
	}

	private static void handleMessage(TargetMethod targetMethod, String methodParam) {
		if(targetMethod.getMode() == Mode.MAIN){
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					try {
						targetMethod.getMethod().invoke(targetMethod.getSubscriber(), methodParam);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}else{
			ThreadUtils.run(new Runnable() {

				@Override
				public void run() {
					try {
						targetMethod.getMethod().invoke(targetMethod.getSubscriber(), methodParam);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

	}
}
