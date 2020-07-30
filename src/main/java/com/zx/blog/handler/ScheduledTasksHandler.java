package com.zx.blog.handler;

import com.zx.blog.dao.BlogMapper;
import com.zx.blog.util.RedisComponentUtils;
import com.zx.blog.dto.BlogViewsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ScheduledTasksHandler {

    @Autowired
    private RedisComponentUtils redisService;

    @Autowired
    private BlogMapper blogMapper;
    /**
     * 同步文章访问量
     */
    @Scheduled(cron = "00 30 04 ? * * ") //每天凌晨4:30执行
    public void syncPostViews() {
        List<BlogViewsDto> dtoList = new ArrayList<>();
        //从redis取值封装List
        Integer prefixLength = "blog_views::blog_views_id_".length();
        Set<String> keySet = redisService.keys("blog_views::blog_views_id_*");
        for (String key : keySet) {
            Integer hKey = (Integer) redisService.hmGet(key, key.substring(prefixLength));
            dtoList.add(new BlogViewsDto(Long.parseLong(key.substring(prefixLength)),hKey));
        }
        //更新到数据库中
        blogMapper.batchUpdateBlogViews(dtoList);
    }
}