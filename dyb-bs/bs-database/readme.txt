使用方法：
1、在config.properties文件中加入以下配置，并修改数据库参数
mysql.url=jdbc:mysql://8.218.116.162:3306/reptile?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
mysql.username=root
mysql.password=7457322
2、在启动项目中调用以下方法，即可生成数据相关文件
public static void main(String[] args) {
    String projectPath = ComCfg.getCallPath().replaceFirst("[^\\\\]+\\\\$", "")
            + "bs-reptile-database\\src\\main\\";
    ComDbCodeGenerator.outputDatabase(projectPath);
}