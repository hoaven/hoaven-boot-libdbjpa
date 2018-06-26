### 一、简介
>对Hibernate框架的二次封装，简化对数据库的操作。

**数据库连接池：** Druid\
**项目构建：** SpringBoot
### 二、使用
#### 1、数据源相关配置
```java
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class HddMasterJpaConfig {

    public final static String prefix = "hddMaster";
    private final static String modelBasePackage = "com.wesd.hdd.libdbmaster.model";
    private final static String propertiesPrefix = "spring.datasource.druid.hdd-master";

    public final static String TRANSACTION_NAME = prefix + "TransactionManager";

    @Bean(prefix + "DataSource")
    @ConfigurationProperties(propertiesPrefix)
    public DataSource dataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = prefix + "EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource())
                .packages(modelBasePackage) //设置实体类所在位置
                .persistenceUnit(prefix + "PersistenceUnit")
                .build();
    }

    @Bean(name = prefix + "EntityManager")
    public EntityManager entityManager(
            @Qualifier(prefix + "EntityManagerFactory")
                    LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return entityManagerFactory.getObject().createEntityManager();
    }

    @Bean(name = TRANSACTION_NAME)
    public PlatformTransactionManager transactionManager(
            @Qualifier(prefix + "EntityManagerFactory")
                    LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}
```
**properties：**
```
spring.datasource.druid.hdd-master.driver-class-name = com.mysql.jdbc.Driver
spring.datasource.druid.hdd-master.url = jdbc:mysql://host:3306/hdd?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull
spring.datasource.druid.hdd-master.username = 
spring.datasource.druid.hdd-master.password = 
```

#### 2、model创建
```java
/**
 * Created by hoaven on 2018/5/9.
 */
@Entity
@Table(name = "hdd_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDO extends BaseModel {
    private String userRefId;
    private String itemMap;
}
//只读Model继承BaseReadOnlyModel
```
#### 3、通用dao封装：直接继承AbstractBaseDao
```java
@Repository
public class HddBaseDao extends AbstractBaseDao {
    @Autowired
    @Qualifier(HddMasterJpaConfig.prefix + "EntityManagerFactory")
    EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
```
#### 4、通用Service封装
```java
public interface IHddBaseService extends IBaseService {
}

@Service
public class HddBaseService implements IHddBaseService {

    @Resource
    HddBaseDao baseDao;


    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> T insert(T model) {
        return baseDao.insert(model);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> T update(T model) {
        return baseDao.update(model);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public <T> void delete(T model) {
        baseDao.delete(model);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.SUPPORTS, readOnly = true)
    public <T> T getById(Class<T> modelClass, Long id) {
        return baseDao.getById(modelClass, id);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.SUPPORTS, readOnly = true)
    public <T> List<T> listQuery(String hql, RowBounds rowBounds, Map<String, Object> params) {
        return baseDao.listQuery(hql, rowBounds, params);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.SUPPORTS, readOnly = true)
    public <T> List<T> listQuery(String hql, RowBounds rowBounds, Object... params) {
        return baseDao.listQuery(hql, rowBounds, params);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.SUPPORTS, readOnly = true)
    public <T> List<T> listQuery(String hql, Object... params) {
        return baseDao.listQuery(hql, params);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.SUPPORTS, readOnly = true)
    public <T> T singleBySQL(String hql, Object... params) {
        return baseDao.singleBySQL(hql, params);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer countBySQL(String hql, Object... params) {
        return baseDao.countBySQL(hql, params);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer update(String hql, Object... params) {
        return baseDao.update(hql, params);
    }

    @Override
    @Transactional(transactionManager = TRANSACTION_NAME, propagation = Propagation.SUPPORTS, readOnly = true)
    public <T> List<T> listQuery(final String hql, Map<String, Object> params) {
        return baseDao.listQuery(hql, params);
    }
}
```
#### 5、操作DB示例
```java
public class CartServiceImpl implements CartService {
    @Resource
    IHddBaseService hddBaseService;
    
    public CartDTO queryCartByUserRefId(String userRefId) {
        CartDO cartDO = hddBaseService.singleBySQL("from CartDO where userRefId = ?1", userRefId);
        return BeanConvertUtils.map(cartDO, CartDTO.class);
    }
}
```