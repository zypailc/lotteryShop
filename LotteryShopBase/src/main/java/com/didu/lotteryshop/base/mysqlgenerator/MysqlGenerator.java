package com.didu.lotteryshop.base.mysqlgenerator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 * 自动生成映射工具类
 *
 * @author hubin
 *
 */
public class MysqlGenerator {

    private static String packageName="";    //文件路径
    private static File file = new File(packageName);
    private static String path = file.getAbsolutePath();

    /**
     * <p>
     * 测试 run 执行
     * </p>
     * <p>
     * 更多使用查看 http://mp.baomidou.com
     * </p>
     */
    public static void main(String[] args) {



        // 用来获取Mybatis-Plus.properties文件的配置信息
        ResourceBundle rb = ResourceBundle.getBundle("mybatis-plus");
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();

        gc.setOutputDir(rb.getString("OutputDir"));

        gc.setFileOverride(true);
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        //gc.setMapperName("%sMapper");
        //gc.setXmlName("%sMapper");
        //gc.setServiceName("%sService");
        //gc.setServiceImplName("%sServiceImpl");
        //gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        //dsc.setTypeConvert(new MySqlTypeConvert());
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(rb.getString("userName"));
        dsc.setPassword(rb.getString("passWord"));
        dsc.setUrl(rb.getString("url"));
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setTablePrefix(new String[] { "bmd_", "mp_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[] { rb.getString("tableName") }); // 需要生成的表
        // 字段名生成策略
        // strategy.setFieldNaming(NamingStrategy.underline_to_camel);
        // strategy.setSuperServiceImplClass("com.baomidou.springwind.service.support.BaseServiceImpl");
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("test");
        pc.setParent(rb.getString("parent"));// 自定义包路径
        pc.setController("controller");// 这里是控制器包名，默认 web
        pc.setEntity("model");
        pc.setMapper("mapper");
        //pc.setXml("mapping");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
		/*pc.setModuleName("test");
		pc.setParent(rb.getString("parent"));// 自定义包路径
		pc.setController("controller." + rb.getString("className"));// 这里是控制器包名，默认 web
		pc.setEntity("model." + rb.getString("className"));
		pc.setMapper("dao." + rb.getString("className"));
		pc.setXml("mapping." + rb.getString("className"));
		pc.setService("service." + rb.getString("className"));
		pc.setServiceImpl("service." + rb.getString("className") + ".impl");*/
        mpg.setPackageInfo(pc);
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        mpg.setCfg(
                // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
                new InjectionConfig() {
                    @Override
                    public void initMap() {
                        Map<String, Object> map = new HashMap<>();
                        map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                        this.setMap(map);
                    }
                }.setFileOutConfigList(Collections.<FileOutConfig>singletonList(new FileOutConfig("/templates/mapper.xml.vm") {
                    // 自定义输出文件目录
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return path+"/test-boot-client/src/main/resources/mapping/" + tableInfo.getEntityName() + "Mapper.xml";
                    }
                }))
        ).setTemplate(
                // 关闭默认 xml 生成，调整生成 至 根目录
                new TemplateConfig().setXml(null)
                // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
                // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
                // .setController("...");
                // .setEntity("...");
                // .setMapper("...");
                // .setXml("...");
                // .setService("...");
                // .setServiceImpl("...");
        );

        // 执行生成
        mpg.execute();
    }

}

