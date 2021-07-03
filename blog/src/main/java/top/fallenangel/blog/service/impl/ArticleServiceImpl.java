package top.fallenangel.blog.service.impl;

import org.springframework.stereotype.Service;
import top.fallenangel.blog.entity.Article;
import top.fallenangel.blog.mapper.ArticleMapper;
import top.fallenangel.blog.service.IArticleService;

import java.util.List;

@Service
public class ArticleServiceImpl implements IArticleService {
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public List<Article> list() {
        return articleMapper.list();
    }
}
