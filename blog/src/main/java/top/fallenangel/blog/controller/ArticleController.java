package top.fallenangel.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fallenangel.blog.entity.Article;
import top.fallenangel.blog.service.IArticleService;
import top.fallenangel.response.Result;

import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController {
    private final IArticleService articleService;

    public ArticleController(IArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("list")
    public Result<List<Article>> list() {
        return Result.ok(articleService.list());
    }
}
