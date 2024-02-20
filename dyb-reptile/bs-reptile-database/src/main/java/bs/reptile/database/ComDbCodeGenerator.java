package bs.reptile.database;

import bs.common.*;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;

import java.io.File;
import java.util.*;

public class ComDbCodeGenerator {
    //region 常量
    //数据库链接地址**/
    static final String MYSQL_URL = ComCfg.get("mysql.url");
    //数据库登录账号**/
    static final String MYSQL_USER_NAME = ComCfg.get("mysql.username");
    //数据库登录密码**/
    static final String MYSQL_PASSWORD = ComCfg.get("mysql.password");
    /**
     * 【需要修改】
     * 需要进行生成文件的表名
     * 多张表，表名间使用,号分割
     **/
    private static String[] Tables = null;
    /**
     * 【需要修改】
     * 生成类的注释
     * 作者名称
     */
    private static final String CODE_AUTHOR = "dyb";
    /**
     * 生成的文件存放地址 之
     * 文件路径
     */
    static String FILE_STORAGE_FILE_ROOT_PATH = null;
    /**
     * 生成的文件存放地址 之
     * 父级 jar包路径
     */
    private static final String FILE_STORAGE_FILE_JAR_PACKAGE = "bs.reptile";
    /**
     * 生成的文件存放地址 之
     * 模块 jar包名称
     */
    private static final String FILE_STORAGE_FILE_JAR_PACKAGE_MODULE = "database";
    /**
     * 生成的文件存放地址 之
     * Service 接口 存放地址
     */
    private static final String FILE_STORAGE_SERVICE_FILE_JAR_PACKAGE = "service";
    /**
     * 生成的文件存放地址 之
     * Service impl 实现类 存放地址
     */
    private static final String FILE_STORAGE_SERVICE_IMPL_FILE_JAR_PACKAGE = "impl";
    /**
     * 生成的文件存放地址 之
     * entity  实体类 存放地址
     */
    private static final String FILE_STORAGE_ENTITY_FILE_JAR_PACKAGE = "entity";
    /**
     * 生成的文件存放地址 之
     * mapper  操作类 存放地址
     */
    private static final String FILE_STORAGE_MAPPER_FILE_JAR_PACKAGE = "mapper";
    /**
     * 生成的文件存放地址 之
     * mapper  xml 文件 存放地址
     */
    private static final String FILE_STORAGE_MAPPER_XML_FILE_JAR_PACKAGE = "mapper";
    //endregion

    /**
     * 配置基础转换器
     */
    private static DataSourceConfig configDataSource() {
        //数据库链接配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                MYSQL_URL,
                MYSQL_USER_NAME,
                MYSQL_PASSWORD
        )
                .dbQuery(new MySqlQuery())
                //自定义转换器，将tinyint 转换为Integer
                .typeConvert(new EasyMySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .build();

        return dataSourceConfig;
    }

    /**
     * 自定义转换器转换器 内部类
     * 目的将数据库表中定义的  tinyint 或 bit类型转变为 java Integer 类型
     *
     * @author timerbin
     */
    static class EasyMySqlTypeConvert extends MySqlTypeConvert {
        @Override
        public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
            IColumnType iColumnType = super.processTypeConvert(config, fieldType);
            if (fieldType.equals("tinyint(1)")) {
                iColumnType = DbColumnType.INTEGER;
            }
            if (fieldType.equals("bit(1)")) {
                iColumnType = DbColumnType.BYTE;
            }
            return iColumnType;
        }
    }

    /**
     * 设置全局变量
     */
    private static GlobalConfig configGlobel() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        GlobalConfig globalConfig = new GlobalConfig.Builder()
                .disableOpenDir()
                //存放生成文件的文件夹地址
                .outputDir(FILE_STORAGE_FILE_ROOT_PATH)
                .author(CODE_AUTHOR)
                .dateType(DateType.ONLY_DATE)
                .commentDate("yyyy-MM-dd hh:mm:ss")
                .build();
        return globalConfig;
    }

    /**
     * 生成文件存储目录配置
     */
    private static PackageConfig configPackage() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        PackageConfig packageConfig = new PackageConfig.Builder()
                //存放生成文件的 父级 package 地址**/
                .parent(FILE_STORAGE_FILE_JAR_PACKAGE)
                //存放生成文件的 的 父级模块地址**/
                .moduleName(FILE_STORAGE_FILE_JAR_PACKAGE_MODULE)
                //存放生成文件的 service 接口 存放的package地址**/
                .service(FILE_STORAGE_SERVICE_FILE_JAR_PACKAGE)
                //存放生成文件的 service 接口实现类 存放的package地址**/
                .serviceImpl(FILE_STORAGE_SERVICE_IMPL_FILE_JAR_PACKAGE)
                //存放生成文件的 实体类 存放的package地址**/
                .entity(FILE_STORAGE_ENTITY_FILE_JAR_PACKAGE)
                //存放生成文件的 mapper 操作类 存放的package地址**/
                .mapper(FILE_STORAGE_MAPPER_FILE_JAR_PACKAGE)
                //存放生成文件的 mapper 操作类 xml 存放的package地址**/
                .xml(FILE_STORAGE_MAPPER_XML_FILE_JAR_PACKAGE)
                .build();
        return packageConfig;
    }

    /**
     * 生成文件的策略配置
     */
    private static StrategyConfig configStratgy() {
        //初始化配置 策略中实体字段的默认填充装置
        List<IFill> tableFillList = makeInitTableFills();
        @SuppressWarnings("UnnecessaryLocalVariable")
        StrategyConfig strategyConfig = new StrategyConfig.Builder()
                .addInclude(Tables)
                .entityBuilder()//开始定制实体
                //*禁用生成 serialVersionUID**/
                // .disableSerialVersionUID()
                .enableFileOverride()
                .enableActiveRecord()
                .enableLombok()
                .enableRemoveIsPrefix()
                .enableTableFieldAnnotation()
                .enableChainModel()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                //*逻辑删除字段名(数据库)**/
//                .logicDeleteColumnName("yn")
                .addTableFills(tableFillList)
                .idType(IdType.AUTO)
                //格式化实体类文件名称***/
                //.formatFileName("%sEntity")

                .serviceBuilder()//开始定制Service，由于我们不需要service此处略
                //*格式化 service 接口文件名称**/
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")

                .mapperBuilder()//开始定制映射器
                .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class)
                .superClass(BaseMapper.class)
                .enableBaseColumnList()
                .enableBaseResultMap()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")

                .build();
        return strategyConfig;
    }

    /**
     * 类生成策略
     * 配置字段默认填充装置
     */
    private static List<IFill> makeInitTableFills() {
        List<IFill> tableFillList = new ArrayList<>();
        //定义创建时间 插入时默认填充**/
        Column createTableFill = new Column("created", FieldFill.INSERT);
        // 定义修改时间 插入或修改时默认填充**/
        Column updateTableFill = new Column("modified", FieldFill.INSERT_UPDATE);
        tableFillList.add(createTableFill);
        tableFillList.add(updateTableFill);

        return tableFillList;
    }

    /**
     * 设置其他模板
     */
    private static TemplateConfig configTemplate() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        TemplateConfig templateConfig = new TemplateConfig.Builder()
                .disable(
                        TemplateType.CONTROLLER,    //我不需要controller 此处传null
                        TemplateType.SERVICE,       //我不需要service  此处传null
                        TemplateType.SERVICE_IMPL   //我不需要service impl 此处传null
                )
                .build();
        return templateConfig;
    }

    /**
     * 初使化配置
     */
    private static InjectionConfig initInjectionConfig() {
        //自定义生成模板参数**/
        Map<String, Object> paramMap = new HashMap<>();

        // 自定义 生成类**/
        Map<String, String> customFileMap = new HashMap<>();
        //PO实体**/
        customFileMap.put("po" + File.separator + "%sPO.java", "/templates/PO.java.vm");
        //Vo实体**/
        customFileMap.put("vo" + File.separator + "%sVO.java", "/templates/VO.java.vm");
        //DTO实体
        customFileMap.put("dto" + File.separator + "%sDTO.java", "/templates/DTO.java.vm");

        return new InjectionConfig.Builder()
                .customMap(paramMap)
                .customFile(customFileMap)
                .build();
    }

    public static void output() {
        output(null, null);
    }

    public static void output(String absoluteDir) {
        output(absoluteDir, null);
    }

    public static void output(String[] TableNames) {
        output(null, TableNames);
    }

    public static void output(String absoluteDir, String[] TableNames) {
        if (ComStr.isEmpty(absoluteDir)) {
            FILE_STORAGE_FILE_ROOT_PATH = ComCfg.getRunPath() + "Java";
        } else {
            FILE_STORAGE_FILE_ROOT_PATH = absoluteDir;
        }
        ComFile.createDir(FILE_STORAGE_FILE_ROOT_PATH);

        if (TableNames == null || TableNames.length == 0) {
            //读取当前数据库中所有表
            List<Map<String, Object>> maps = ComDb.executeSql("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE()");
            Tables = (String[]) maps.stream().map(t -> t.get("TABLE_NAME").toString()).toArray(String[]::new);
        } else {
            Tables = TableNames;
        }

        //数据库信息配置
        DataSourceConfig dataSourceConfig = configDataSource();
        //生成工具类
        AutoGenerator generator = new AutoGenerator(dataSourceConfig);
        //全局变量配置
        generator.global(configGlobel());
        //设置生成文件包名地址
        generator.packageInfo(configPackage());

        //生成文件的策略配置
        generator.strategy(configStratgy());

        //生成的类的模板配置
        generator.template(configTemplate());

        //自定义实体信息
        generator.injection(initInjectionConfig());

        //自定义模板解析器
        TimerVelocityTemplateEngine timerVelocityTemplateEngine = new TimerVelocityTemplateEngine();

        //生成代码
        generator.execute(timerVelocityTemplateEngine);
    }

}
