package org.cloud.webtemplate.config.mybatis;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"org.cloud.webtemplate.db.mapper.*"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisPlusConfig {

	 @Autowired
	 @Qualifier("secondDataSource")
	 private DataSource secondDataSource;
	 
	 @Bean
	 public SqlSessionFactory sqlSessionFactory() throws Exception {
		 	MybatisSqlSessionFactoryBean sqlSessionFactory  = new MybatisSqlSessionFactoryBean();
	        sqlSessionFactory .setDataSource(secondDataSource); 

	        sqlSessionFactory.setTypeAliasesPackage("org.cloud.webtemplate.db.entity");
	        sqlSessionFactory.setTypeEnumsPackage("org.cloud.webtemplate.db.entity.enums");
	        MybatisConfiguration configuration = new MybatisConfiguration();
	        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
	        configuration.setJdbcTypeForNull(JdbcType.NULL);
	        configuration.setMapUnderscoreToCamelCase(true);
	        sqlSessionFactory.setConfiguration(configuration);
	        PaginationInterceptor pagination = new PaginationInterceptor();
	        sqlSessionFactory.setPlugins(new Interceptor[]{
	                pagination,
	                new PerformanceInterceptor()
	        });
	        //sqlSessionFactory.setGlobalConfig(globalConfiguration);
	        
	        return sqlSessionFactory.getObject();
	    }

	    @Bean
	    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
	        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory()); 
	        return template;
	    }
	
    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    //@Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        /*
         * 【测试多租户】 SQL 解析处理拦截器<br>
         * 这里固定写成住户 1 实际情况你可以从cookie读取，因此数据看不到 【 麻花藤 】 这条记录（ 注意观察 SQL ）<br>
         */
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(1L);
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 这里可以判断是否过滤表
                /*if ("user".equals(tableName)) {
                    return true;
                }*/
                return false;
            }
        });

        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
//        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
//            @Override
//            public boolean doFilter(MetaObject metaObject) {
//                MappedStatement ms = PluginUtils.getMappedStatement(metaObject);
//                // 过滤自定义查询此时无租户信息约束【 麻花藤 】出现
//                if ("com.baomidou.springboot.mapper.UserMapper.selectListBySQL".equals(ms.getId())) {
//                    return true;
//                }
//                return false;
//            }
//        });
        return paginationInterceptor;
    }
    
    /**
     * 性能分析拦截器，不建议生产使用
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor(){
        return new PerformanceInterceptor();
    }
}
