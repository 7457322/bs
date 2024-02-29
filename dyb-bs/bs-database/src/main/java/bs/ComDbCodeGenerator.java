package bs;

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
    //region 属性
    static final ComDbCodeGenerator Self = new ComDbCodeGenerator();
    //数据库链接地址**/
    static final String MYSQL_URL = ComCfg.get("mysql.url");
    //数据库登录账号**/
    static final String MYSQL_USER_NAME = ComCfg.get("mysql.username");
    //数据库登录密码**/
    static final String MYSQL_PASSWORD = ComCfg.get("mysql.password");
    //是否生成自定义MapperXml模板
    static boolean isGeneratorCustomTemplateMapperXml = true;
    //是否生成自定义Dto模板
    static boolean isGeneratorCustomTemplateDto = true;
    static String Prefix = "bs";
    /**
     * 【需要修改】
     * 需要进行生成文件的表名
     * 多张表，表名间使用,号分割
     **/
    static String[] Tables = null;
    /**
     * 【需要修改】
     * 生成类的注释
     * 作者名称
     */
    static String CODE_AUTHOR = "dyb";
    /**
     * 生成的文件存放地址 之
     * 文件路径
     */
    static String OUTPUT_PATH = null;
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
        DataSourceConfig.Builder builder = new DataSourceConfig.Builder(
                MYSQL_URL,
                MYSQL_USER_NAME,
                MYSQL_PASSWORD
        );
        builder.dbQuery(new MySqlQuery())
                //自定义转换器，将tinyint 转换为Integer
                .typeConvert(new EasyMySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler());
        return builder.build();
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
                .outputDir(OUTPUT_PATH)
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
                .addTablePrefix(Prefix)
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
     * 设置代码层系统模板配置
     */
    private static TemplateConfig configSystemTemplateCodes() {
        TemplateConfig.Builder builder = new TemplateConfig.Builder();
        builder.disable(
                TemplateType.XML       //我不需要xml
        );
        return builder.build();
    }

    /**
     * 设置资源层系统模板配置
     */
    private static TemplateConfig configSystemTemplateResources() {
        TemplateConfig.Builder builder = new TemplateConfig.Builder();
        if (isGeneratorCustomTemplateMapperXml) {
            builder.disable();
        } else {
            builder.disable(
                    TemplateType.CONTROLLER,    //我不需要controller
                    TemplateType.SERVICE,       //我不需要service
                    TemplateType.SERVICE_IMPL,   //我不需要service impl
                    TemplateType.ENTITY,    //我不需要entity
                    TemplateType.MAPPER     //我不需要mapper
            );
        }
        return builder.build();
    }

    /**
     * 设置业务层系统模板配置
     */
    private static TemplateConfig configSystemTemplateBusiness() {
        TemplateConfig.Builder builder = new TemplateConfig.Builder();
        builder.disable(
                TemplateType.XML,       //我不需要xml
                TemplateType.ENTITY,    //我不需要entity
                TemplateType.MAPPER     //我不需要mapper
        );
        return builder.build();
    }

    /**
     * 设置数据层系统模板配置
     */
    private static TemplateConfig configSystemTemplateDatabase() {
        @SuppressWarnings("UnnecessaryLocalVariable")
        TemplateConfig.Builder builder = new TemplateConfig.Builder();
        builder.disable(
                TemplateType.CONTROLLER,    //我不需要controller
                TemplateType.SERVICE,       //我不需要service
                TemplateType.XML,           //我不需要mapper.xml
                TemplateType.SERVICE_IMPL   //我不需要service impl
        );
        return builder.build();
    }

    /**
     * 初使化配置
     */
    private static void initInjectionConfig(AutoGenerator ag, String outputPath) {
        InjectionConfig.Builder builder = new InjectionConfig.Builder();
        // 自定义 生成模板参数**/
        Map<String, Object> paramMap = new HashMap<>();
        // 自定义 生成模板文件**/
        Map<String, String> customFileMap = new HashMap<>();
        //自定义mapper
        if (isGeneratorCustomTemplateMapperXml) {
            customFileMap.put(outputPath + "mapper" + File.separator + "%smapper.xml", "/templates/mapper.xml.vm");
        }
        //DTO数据类
        if (isGeneratorCustomTemplateDto) {
            customFileMap.put("dto" + File.separator + "%sResDto.java", "/templates/resDto.java.vm");
            customFileMap.put("dto" + File.separator + "%sReqDto.java", "/templates/reqDto.java.vm");
        }
        builder.customMap(paramMap).customFile(customFileMap);
        InjectionConfig build = builder.build();
        ag.injection(build);
    }

    /**
     * 生成代码
     */
    static void generator(TemplateConfig templateConfig) {
        //数据库信息配置
        DataSourceConfig dataSourceConfig = configDataSource();
        //生成工具类
        AutoGenerator generator = new AutoGenerator(dataSourceConfig);
        //设置生成文件包名地址
        generator.packageInfo(configPackage());
        //生成文件的策略配置
        generator.strategy(configStratgy());
        //生成自定义代码、模板、输出路径
        initInjectionConfig(generator, OUTPUT_PATH);
        //自定义模板解析器
        CustomTemplateOutputEngine timerVelocityTemplateEngine = new CustomTemplateOutputEngine();
        //全局变量配置
        generator.global(configGlobel());
        //生成的类的模板配置
        generator.template(templateConfig);
        //生成代码
        generator.execute(timerVelocityTemplateEngine);
    }

    /**
     * 设置参数默认值
     */
    static void setParamsDefaultValues(String absoluteDir, String[] TableNames) {
        if (ComStr.isEmpty(absoluteDir)) {
            OUTPUT_PATH = ComCfg.getRunPath();
        } else {
            OUTPUT_PATH = ComStr.toStandardPath(absoluteDir);
        }
        ComFile.createDir(OUTPUT_PATH);

        if (TableNames == null || TableNames.length == 0) {
            //读取当前数据库中所有表
            List<Map<String, Object>> maps = ComDb.executeSql("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE()");
            Tables = maps.stream().map(t -> t.get("TABLE_NAME").toString()).toArray(String[]::new);
        } else {
            Tables = TableNames;
        }
    }

    //设置忽略前缀
    public static ComDbCodeGenerator setPrefix(String prefix) {
        Prefix = prefix;
        return Self;
    }

    //输出所有文件
    public static ComDbCodeGenerator output() {
        return output(null, null);
    }

    //输出所有文件
    public static ComDbCodeGenerator output(String absoluteDir) {
        return output(absoluteDir, null);
    }

    //输出所有文件
    public static ComDbCodeGenerator output(String[] TableNames) {
        return output(null, TableNames);
    }

    //输出所有文件
    public static ComDbCodeGenerator output(String absoluteDir, String[] TableNames) {
        setParamsDefaultValues(absoluteDir, TableNames);
        String outputPath = OUTPUT_PATH;

        isGeneratorCustomTemplateDto = true; // 是否生成自定义dto代码
        isGeneratorCustomTemplateMapperXml = false; // 是否生成自定义xml代码
        OUTPUT_PATH = outputPath + "java\\";
        generator(configSystemTemplateCodes());

        isGeneratorCustomTemplateDto = false; // 是否生成自定义dto代码
        isGeneratorCustomTemplateMapperXml = true; // 是否生成自定义xml代码
        OUTPUT_PATH = outputPath + "resources\\";
        generator(configSystemTemplateResources());

        return Self;
    }

    //输出数据层文件MAPPER XML,ENTITY,MAPPER
    public static ComDbCodeGenerator outputDatabase() {
        return outputDatabase(null, null);
    }

    //输出数据层文件MAPPER XML,ENTITY,MAPPER
    public static ComDbCodeGenerator outputDatabase(String absoluteDir) {
        return outputDatabase(absoluteDir, null);
    }

    //输出数据层文件MAPPER XML,ENTITY,MAPPER
    public static ComDbCodeGenerator outputDatabase(String[] TableNames) {
        return outputDatabase(null, TableNames);
    }

    //输出数据层文件MAPPER XML,ENTITY,MAPPER
    public static ComDbCodeGenerator outputDatabase(String absoluteDir, String[] TableNames) {
        setParamsDefaultValues(absoluteDir, TableNames);
        String outputPath = OUTPUT_PATH;

        isGeneratorCustomTemplateDto = false; // 是否生成自定义dto代码
        isGeneratorCustomTemplateMapperXml = false; // 是否生成自定义xml代码
        OUTPUT_PATH = outputPath + "java\\";
        generator(configSystemTemplateDatabase());

        isGeneratorCustomTemplateMapperXml = true; // 是否生成自定义xml代码
        OUTPUT_PATH = outputPath + "resources\\";
        generator(configSystemTemplateResources());

        return Self;
    }

    //输出业务层文件CONTROLLER,SERVICE,SERVICE_IMPL
    public static ComDbCodeGenerator outputBusiness() {
        return outputBusiness(null, null);
    }

    //输出业务层文件CONTROLLER,SERVICE,SERVICE_IMPL
    public static ComDbCodeGenerator outputBusiness(String absoluteDir) {
        return outputBusiness(absoluteDir, null);
    }

    //输出业务层文件CONTROLLER,SERVICE,SERVICE_IMPL
    public static ComDbCodeGenerator outputBusiness(String[] TableNames) {
        return outputBusiness(null, TableNames);
    }

    //输出业务层文件CONTROLLER,SERVICE,SERVICE_IMPL
    public static ComDbCodeGenerator outputBusiness(String absoluteDir, String[] TableNames) {
        setParamsDefaultValues(absoluteDir, TableNames);
        String outputPath = OUTPUT_PATH;

        isGeneratorCustomTemplateDto = true; // 是否生成自定义dto代码
        isGeneratorCustomTemplateMapperXml = false; // 是否生成自定义xml代码
        OUTPUT_PATH = outputPath + "java\\";
        generator(configSystemTemplateBusiness());

        return Self;
    }
}
