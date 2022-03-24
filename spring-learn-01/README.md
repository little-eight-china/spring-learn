# spring-learn-01

第一阶段，主要做个能生产bean对象的工厂出来，就像是做个简易的spring ioc出来。

首先，先定义一个描述bean的对象，记录这个bean是代表哪个类的(未来还会加这个bean的作用域什么等等...)，就像一个配方一样，工厂根据这个配方可以源源不断地生产出类似的产品，那么他就叫 **BeanDefinition** :

```java
public class BeanDefinition {

    private Class beanClass;

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

}

```

BeanDefinition定义了，那接下来是不是要制定一个存储它的方法，我们姑且叫 注册，于是 **BeanDefinitionRegistry** 接口便出来了：

```java
public interface BeanDefinitionRegistry {
    /**
	 * 向注册表中注BeanDefinition
	 *
	 * @param beanName
	 * @param beanDefinition
	 */
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}

```

那么既然是存储，那存储的数据结构肯定是集合了，而这个集合肯定是某个实际的bean工厂的成员变量了对吧，基于扩展性我们是不能直接做个实际的工厂出来的，我们还是得先制定它最基础的接口工厂。
所以 **BeanFactory** 便出来了，基础的方法就是 **getBean** ，客户端想拿某些bean都是通过这个方法来获取 ：

```java
public interface BeanFactory {

    Object getBean(String name) throws BeansException;

}
```

接口工厂出来了， 我们再做个抽象工厂 **AbstractBeanFactory**, 来实现接口工厂的 **getBean**，当然因为是抽象工厂，所以存储集合不能放到这里，于是在实现getBean里用了模板方法模式，对外开放 getBean 的入口，然后再把getBean里的  **createBean** 、**getBeanDefinition** 作为扩展方法开放出去，供高层使用：

```java
public abstract class AbstractBeanFactory implements BeanFactory {

	@Override
	public Object getBean(String name) throws BeansException {
		BeanDefinition beanDefinition = getBeanDefinition(name);
		return createBean(name, beanDefinition);
	}

	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
```

那接下来是不是要制定实际bean工厂了呢？理论上是可以这么做的，但spring却没有，它在这之前又加了一个抽象工厂 **AbstractAutowireCapableBeanFactory**, 主要是干实例化bean的工作：

```java
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
		return doCreateBean(beanName, beanDefinition);
	}

	protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
		Class beanClass = beanDefinition.getBeanClass();
		Object bean = null;
		try {
			bean = beanClass.newInstance();
		} catch (Exception e) {
			throw new BeansException("实例化bean失败", e);
		}
		return bean;
	}
}

```

为什么要做多一个 **AbstractAutowireCapableBeanFactory**呢？spring可能期望的是把个体的职责跟群体的职责拆分开了，你看后面 又做了代表群体性的

**DefaultListableBeanFactory**, 这样的话群体性的操作都放到这里然后再继承个体的 **AbstractAutowireCapableBeanFactory**，这样层级就很分明了，对扩展性跟维护性都有极大的提升。
当然，存储集合的家也是这个工厂了，所以我们可以把操作存储集合相关的方法都放到这个工厂里实现了，比如注册到集合里、从集合里获取，集合的数量是多少等等...

```java
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ListableBeanFactory {

	private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(beanName, beanDefinition);
	}

	@Override
    protected BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        if (containsBeanDefinition(beanName)) {
			throw new BeansException(String.format("No bean named %s is defined", beanName));
		}
		return beanDefinitionMap.get(beanName);
	}

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }
}
```

这样的话，我们就基本实现了能生产bean对象的工厂的目标，后面我们可以通过**DefaultListableBeanFactory**去拿到我们期望的bean对象，当然在此之前，我们得先把bean注册上去。基本步骤如下：
- 先new一个工厂，DefaultListableBeanFactory
- 往工厂里注册一个期望的对象，工厂就会记录下这个对象的基本信息，或者说配方。
- 客户端通过工厂的getBean方法，获取到该对象。