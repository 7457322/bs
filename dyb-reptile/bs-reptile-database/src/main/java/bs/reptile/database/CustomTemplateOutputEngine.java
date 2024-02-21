package bs.reptile.database;

import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;


import java.io.File;
import java.util.List;
import java.util.Map;

public class CustomTemplateOutputEngine extends VelocityTemplateEngine {

    @Override
    protected void outputCustomFile(List<CustomFile> customFiles, TableInfo tableInfo, Map<String, Object> objectMap) {
        //数据库表映射实体名称
        String entityName = tableInfo.getEntityName();
        //数据库表映射实体名称 驼峰命名法
        objectMap.put("humpEentityName", toLowerCaseFirstOne(entityName));

        String pathParent = this.getPathInfo(OutputFile.parent);
        customFiles.forEach((file) -> {
            String fileName,tempFileName = file.getFileName();
            if(tempFileName.substring(1,3).equals(":\\")){
                fileName = String.format(file.getFileName(), entityName);
            }else{
                fileName = String.format(pathParent + File.separator + file.getFileName(), entityName);
            }
            this.outputFile(new File(fileName), objectMap, file.getTemplatePath(), true);
        });
    }

    /**
     * 首字母转为小写
     */
    private String toLowerCaseFirstOne(String name) {
        if (Character.isLowerCase(name.charAt(0))) return name;
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}