package bs.reptile.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data()
@TableName(value = "t_user")
public class User {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Date createTime;

    private String description;

    private String email;

    private Boolean enable;

    private String name;

    private String password;

    private String pic;

    private String salt;

    private Date updateTime;

    private String username;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", enable=" + enable +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", pic='" + pic + '\'' +
                ", salt='" + salt + '\'' +
                ", updateTime=" + updateTime +
                ", username='" + username + '\'' +
                '}';
    }
}

