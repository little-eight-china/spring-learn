# spring-learn-07

第一阶段我们做了一个简易的spring ioc了，就像麻雀一样，五脏俱全了，所以后面的步骤，先慢慢优化这只”麻雀“先。

第二阶段我期望优化实例化bean这一块。

第三阶段，通过配置文件来实例化bean。

第四阶段，去做bean对象的扩展。

第五阶段，做应用上下文，更优雅地管理bean的生命周期。

第六阶段，实现注解方式的注入。

第七阶段，引入AOP。

Spring的AOP框架中是基于AOP联盟的规范去做，我们要先了解下什么是AOP联盟。

## AOP联盟
AOP联盟规范了一套用于规范AOP实现的底层API，通过这些统一的底层API，可以使得各个AOP实现及工具产品之间实现相互移植。
这些API主要以标准接口的形式提供，是AOP编程思想所要解决的横切交叉关注点问题各部件的最高抽象。

aop联盟在aop主要定义了两类接口，一类是JoinPoint，一类是Advice。
可以看到在aop联盟中的核心就是两个，一个就是连接点,一个就是通知,对于连接点表示的是事件,像方法调用，构造器调用，获取属性这行动态运行的事件,
而通知则是在这些事件进行拦截等操作。

### JOINPOINT家族
#### JOINPOINT
joinPoint表示的是通用的运行时连接点，这个运行时连接点表示的是一个静态连接点（程序中的某个地方）的事件，
这些事件可以是方法调用，构造器调用，获取属性等，这些调用式的连接点，则是Invocation,方法调用则是MethodInvocation,
构造器调用则是ConstructorInvocation。joinPoint主要有三个方法，分别为proceed,getThis,getStaticPart。
```java
public interface Joinpoint {
    // 执行这个事件
	Object proceed() throws Throwable;
	// 获取持有这个连接点的持有静态部分的对象(一般指target)
	Object getThis();
	// 获取这个连接点的静态部分(对于MethodInvocation则是其对应的method)
	AccessibleObject getStaticPart();
}
```
#### INVOCATION
invocation表示的是调用的连接点，其继承了joinPoint,并加了一个getArguments的方法，返回调用的参数。
```java
public interface Invocation extends Joinpoint {
	Object[] getArguments();
}
```

#### METHODINVOCATION
这个接口是spring最终实现了的接口，其表示的是事件为方法调用的连接点，其加入的一个新的方法是getMethod(),
这个方法的返回值和Joinpoint中的**getStaticPart()**的方法的返回值时一样的。
```java
public interface MethodInvocation extends Invocation {
	Method getMethod();
}
```

#### CONSTRUCTORINVOCATION
和MethodInvocation类似，这个接口表示的是事件为构造器调用的JoinPart,并加入了一个和getMethod()类似的方法getConstructor(),这个接口spring没有提供实现。
```java
public interface ConstructorInvocation extends Invocation {
    Constructor<?> getConstructor();
}
```
## ADVICE家族
advice表示的是一个通知，可以是任意实现的通知，而其中一种则是拦截器Interceptor,Interceptor表示的是一个拦截，表示的是程序运行时的事件的拦截，
这些可以是方法调用，则有了MethodInterceptor,也有构造器调用，则有ContructorInterceptor。

### ADVIVE
advice是一个标识接口，其中是没有任何实现，标识一个通知
```java
public interface Advice {
}
```
#### INTERCEPTOR
interceptor也是一个标识接口，其是对运行时的事件截断的抽象，这些事件可以是方法调用，获取属性等操作。
```java
public interface Interceptor extends Advice {
}
```

#### METHODINTERCEPTOR
这个接口则是比较比较具象的接口，其主要的方法是一个invoke的方法，这个方法传入了上面介绍的MethodInvocation接口为参数,
其中invocation.proceed则是调用原方法逻辑，在原逻辑前面等都可以做处理，其具体则是invoke的实现者进行操作的。
```java
public interface MethodInterceptor extends Interceptor {
	Object invoke(MethodInvocation invocation) throws Throwable;
}
```

#### CONSTRUCTORINTERCEPTOR
这个接口和MethodInterceptor差不多，不过表示的是构造器的拦截。
```java
public interface ConstructorInterceptor extends Interceptor  {
	Object construct(ConstructorInvocation invocation) throws Throwable;
}
```

## Spring AOP

先来了解下Spring AOP包含的几个概念：
- Pointcut(切入点)：程序运行中的某个阶段点，比如方法的调用等。
- Advice(通知)： 定义在连接点做什么，为切面增强提供织入接口，分为前置、后置、异常、最终、环绕五种情况。
- Advisor（通知器）： 完成Pointcut与Advice后，需要一个对象把它们结合到一起，通过Advisor可以定义应该在哪个切入点使用它并且使用哪个通知。
- Proxy(代理)：AOP框架创建的代理对象，有JDK动态代理跟CGLIB代理，前者基于接口，后者基于子类。


我们一开始先定义Pointcut，有类跟方法2种切入点，其中ClassFilter、MethodMatcher只是做对应类型的匹配处理而已
```java
public interface Pointcut {

    /**
     * 返回当前切入点匹配的类
     */
    ClassFilter getClassFilter();

    /**
     * 返回当前切入点匹配的方法
     */
    MethodMatcher getMethodMatcher();
}

```

因为我们可以根据表达式来决定切入点，设计一个AspectJExpressionPointcut,专门处理表达式的
```java
/**
 * 根据表达式获取切入点的类
 * @author little8
 * @since 2022-06-26
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

}
```

接下来看Advice，简单起见，目前只支持方法的前置通知。

定义一个继承Advice的BeforeAdvice空方法接口，然后再定义方法的前置通知接口MethodBeforeAdvice
```java
public interface MethodBeforeAdvice extends BeforeAdvice {

    /**
     * 方法执行前的处理
     */
    void before(Method method, Object[] args, Object target) throws Throwable;

}

```
最终会被MethodBeforeAdviceInterceptor拦截下来，然后执行before

```java
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor() {
    }

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        return methodInvocation.proceed();
    }

}
```

切入点跟通知都设计完成，接下来设计通知器。先来个携带通知的通知器接口Advisor
```java
public interface Advisor {

    Advice getAdvice();

}
```

在这个基础上再来个带切入点的PointcutAdvisor
```java
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}

```

那我们就可以做个表达式的通知器AspectJExpressionPointcutAdvisor，用来实现PointcutAdvisor，一个比较完整的通知器就实现了。

最后来看看代理对象的处理。

定义一个获取代理对象AopProxy接口
```java
public interface AopProxy {

    Object getProxy();

}
```
然后再做一个ProxyFactory工厂，让它来决定选择哪种方式创建。

```java
public class ProxyFactory extends ProxyCreatorSupport {
    public ProxyFactory() {
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

}
```

最终会让DefaultAopProxyFactory来决定采用哪种代理，我们定义了2种创建代理对象的方式，Cglib2AopProxy与JdkDynamicAopProxy。
```java
public class DefaultAopProxyFactory implements AopProxyFactory {

    public AopProxy createAopProxy(AdvisedSupport advisedSupport) {
        if (advisedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advisedSupport);
        }
        return new JdkDynamicAopProxy(advisedSupport);
    }
}
```


Cglib2AopProxy是通过字节码底层继承要代理类来实现,核心方法是：
```java
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, objects, methodProxy);
            if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
                return advised.getMethodInterceptor().invoke(methodInvocation);
            }
            return methodInvocation.proceed();
        }
```

而JdkDynamicAopProxy是使用JDK自带的代理Proxy实现
```java
  public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), advised.getTargetSource().getTargetClass(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args));
        }
        return method.invoke(advised.getTargetSource().getTarget(), args);
    }
```

然后这2个到最终都会执行到MethodInterceptor的invoke，那我们定义一个实现类MethodBeforeAdviceInterceptor，然后核心方法如下。
因为我们只实现before，如果有实现其他通知的话，通过proceed方法来实现责任链模式决定下次执行的拦截器是哪个。

```java
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 先执行定义的通知的before方法
        this.advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        // 再执行下一个拦截器
        return methodInvocation.proceed();
    }
```
现在aop的部分基本都完成了，那我们要把它加入到bean的生命周期内的，先看看AbstractAutowireCapableBeanFactory的createBean：

```java
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		// 判断是否返回代理 Bean 对象
		Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
		if (null != bean) {
			return bean;
		}

		return doCreateBean(beanName, beanDefinition, args);
	}

```
可以发现resolveBeforeInstantiation如果拿到代理bean，是直接返回bean的，而不会走doCreateBean了。
这样的话，我们就定义继承InstantiationAwareBeanPostProcessor的类，来返回我们的代理类，也就是AbstractAutoProxyCreator
在postProcessBeforeInstantiation方法返回代理类：

```java
public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        if (isInfrastructureClass(beanClass)) return null;

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if (!classFilter.matches(beanClass)) continue;

            ProxyFactory proxyFactory = new ProxyFactory();

            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            proxyFactory.setTargetSource(targetSource);
            proxyFactory.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            proxyFactory.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            proxyFactory.setProxyTargetClass(false);

            return proxyFactory.getProxy();

        }

        return null;
    }

```

前面我们知道ProxyFactory的ProxyTargetClass的值是决定选择哪种方式来创建代理类的，上面可以注意到我们直接写死该值为false，是为了先简化aop，实现核心功能就好了。
而在源码中是这么设置ProxyTargetClass的值的
```java
public static MethodValidationPostProcessor methodValidationPostProcessor(
			Environment environment, @Lazy Validator validator) {
		MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
		boolean proxyTargetClass = environment
				.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
		processor.setProxyTargetClass(proxyTargetClass);
		processor.setValidator(validator);
		return processor;
	}
```

后面有机会再去优化这部分。


那现在的话只要把AbstractAutoProxyCreator注册到bean工厂即可(其实是DefaultAdvisorAutoProxyCreator，目前的话只是仿照源码做个层级设计)。
