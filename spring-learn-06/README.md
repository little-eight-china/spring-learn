# spring-learn-05

第一阶段我们做了一个简易的spring ioc了，就像麻雀一样，五脏俱全了，所以后面的步骤，先慢慢优化这只”麻雀“先。

第二阶段我期望优化实例化bean这一块。

第三阶段，通过配置文件来实例化bean。

第四阶段，去做bean对象的扩展。

第五阶段，做应用上下文，更优雅地管理bean的生命周期。

做到这里，已经是非常完整的spring ioc框架了，使用者可以通过spring.xml来配置自己想要的对象。

下一阶段，我想用的爽一点，所以先实现注解方式的注入，这样的话，我就不用大费周章地写spring.xml的信息，并且维护bean也更加方便。
那既然决定用注解，那就来点大的，尽可能地把好用的注解做出来。先列举一下：
- @Component 注入bean
- @Scope bean作用域
- @Autowired 注入属性
- @Qualifier 指定注入bean
- @Value 外部读取属性值

用注解，那肯定是要先扫描相关的类。在xml文件中，我们规定component-scan为扫描标签，base-package为扫描的包
```xml
<component-scan base-package="bdbk.springframework.test.bean"/>
```

先看看应用上下文中怎么加载xml文件的,在AbstractApplicationContext的refresh方法中，第二步就是加载xml文件的，
```java
public void refresh() throws BeansException {
        // ...
        
        // 2、创建bean工厂，并从xml文件中装载beanDefinition后注册到工厂里
        ConfigurableListableBeanFactory beanFactory = this.obtainFreshBeanFactory();
        // ...
    }

```
会在XmlBeanDefinitionReader的doLoadBeanDefinitions中解析，然后我们把扫描相关的逻辑加进去即可
```java

 protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException{
         SAXReader reader=new SAXReader();
         Document document=reader.read(inputStream);
         Element root=document.getRootElement();

         // 解析 context:component-scan 标签，扫描包中的类并提取相关信息，用于组装 BeanDefinition
         Element componentScan=root.element("component-scan");
         if(null!=componentScan){
         String scanPath=componentScan.attributeValue("base-package");
         if(StrUtil.isEmpty(scanPath)){
         throw new BeansException("The value of base-package attribute can not be empty or null");
         }
         scanPackage(scanPath);
         }
}


 private void scanPackage(String scanPath) {
         String[] basePackages = StrUtil.splitToArray(scanPath, ',');
         ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
         scanner.doScan(basePackages);
         }

```
我们创建了ClassPathBeanDefinitionScanner，做了一系列的操作
- 扫描带有@Component的类，然后转成BeanDefinition集合
- 遍历这个集合，再去处理带有@Scope的类然后改动BeanDefinition的作用域
- 把BeanDefinition集合注册到工厂里
- 最终还把处理@Autowired、@Value的BeanPostProcessor的AutowiredAnnotationBeanPostProcessor给注册到工厂(这个后面讲解)

```java
    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            // 扫描带有@Component的类
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidates) {
                // 解析 Bean 的作用域 singleton、prototype
                String beanScope = resolveBeanScope(beanDefinition);
                beanDefinition.setScope(beanScope);
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

        // 注册处理注解的 BeanPostProcessor（@Autowired、@Value）
        registry.registerBeanDefinition("AutowiredAnnotationBeanPostProcessor", new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

```

这样的话，我们就把相关的类的信息注册到工厂了。可以注意到我们在doScan最终new了一个AutowiredAnnotationBeanPostProcessor,他是最终实现于BeanPostProcessor
```java
public class AutowiredAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryAware {
    //...
}

public abstract class InstantiationAwareBeanPostProcessorAdapter implements InstantiationAwareBeanPostProcessor {
   //... 
}
```

因为@Autowired、@Qualifier、@Value都作用于bean的属性上，则这些注解肯定是bean的生命周期里属性注入前的扩展实现的，那自然是BeanPostProcessor接口相关的类了。

我们回到属性注入前的方法： AbstractAutowireCapableBeanFactory的doCreateBean
```java

protected Object doCreateBean(String beanName,BeanDefinition beanDefinition,Object[]args)throws BeansException{
        //...
        // 给 Bean 填充属性
        populateBean(beanName,beanDefinition,bean);

        //...
        }

protected void populateBean(String beanName,BeanDefinition beanDefinition,Object bean){
        // 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
        applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName,bean,beanDefinition);
        applyPropertyValues(beanName,beanDefinition,bean);
        }

/**
 * 在设置属性之前，允许 BeanPostProcessor 修改属性值
 */
protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName,Object bean,BeanDefinition beanDefinition){
        for(BeanPostProcessor beanPostProcessor:getBeanPostProcessors()){
        if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
        PropertyValues pvs=((InstantiationAwareBeanPostProcessor)beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(),bean,beanName);
        if(null!=pvs){
        for(PropertyValue propertyValue:pvs.getPropertyValues()){
        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
        }
        }
        }
        }
        }

```

那看看AutowiredAnnotationBeanPostProcessor的postProcessPropertyValues，就是处理这几个注解的。

```java
 public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        // 1. 处理注解 @Value
        Class<?> clazz = bean.getClass();

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (null != valueAnnotation) {
                String value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(value);
                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
        }

        // 2. 处理注解 @Autowired、@Qualifier
        for (Field field : declaredFields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (null != autowiredAnnotation) {
                Class<?> fieldType = field.getType();
                String dependentBeanName = null;
                Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                Object dependentBean = null;
                if (null != qualifierAnnotation) {
                    dependentBeanName = qualifierAnnotation.value();
                    dependentBean = beanFactory.getBean(dependentBeanName, fieldType);
                } else {
                    dependentBean = beanFactory.getBean(fieldType);
                }
                BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
            }
        }

        return pvs;
    }
}

```

这里额外说下@Value，因为需要解析我们设定好的格式${},那这样的话，我们需要在创建bean工厂后，就把相关的解析类加到bean工厂里，也就是与
还是refresh方法，处理BeanFactoryPostProcessor

```java

  public void refresh() throws BeansException {
       //...

        // 4、在 bean 实例化之前，执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        //...
       
    }

  private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
          Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
          for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
          beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
          }
          }

```
我们创建一个属性解析类PropertyPlaceholderConfigurer，步骤：
- 加载properties、yml等文件
- 占位符替换属性值，并存到解析器的properties里
- 把解析器注册到工厂里

```java

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    /**
     * 默认的@value注解前缀
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * 默认的@value注解后缀
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // 加载属性文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);

            // 占位符替换属性值
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            // 向容器中添加字符串解析器，供解析@Value注解使用
            StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(valueResolver);

        } catch (IOException e) {
            throw new BeansException("Could not load properties", e);
        }
    }
}
```

回到AutowiredAnnotationBeanPostProcessor的postProcessPropertyValues，可以看到它是通过工厂的解析器来解析@Value的值的

```java
 public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        // 1. 处理注解 @Value
        Class<?> clazz = bean.getClass();

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (null != valueAnnotation) {
                String value = valueAnnotation.value();
                value = beanFactory.resolveEmbeddedValue(value);
                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
        }
        // ...
    }

public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
        result = resolver.resolveStringValue(result);
        }
        return result;
        }


private String resolvePlaceholder(String value, Properties properties) {
        String strVal = value;
        StringBuilder buffer = new StringBuilder(strVal);
        int startIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int stopIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
        String propKey = strVal.substring(startIdx + 2, stopIdx);
        String propVal = properties.getProperty(propKey);
        buffer.replace(startIdx, stopIdx + 1, propVal);
        }
        return buffer.toString();
        }


```

这样的话，注解注入就大工告成！

