package bs.reptile.winform.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReptileRootConfig {
    //来源
    private String source;
    //配置列表
    private List<ReptileConfig> configs;
}
