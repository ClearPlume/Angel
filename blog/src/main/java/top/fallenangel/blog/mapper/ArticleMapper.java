package top.fallenangel.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.fallenangel.blog.entity.Article;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Article> list();
}
