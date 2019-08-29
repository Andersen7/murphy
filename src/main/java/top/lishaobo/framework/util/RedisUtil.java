package top.lishaobo.framework.util;

import org.springframework.stereotype.Component;


@Component
public class RedisUtil {

    /*@Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<String, Serializable> redisCacheTemplate;


    public void delete(String key){
        redisCacheTemplate.delete(key);
    }

    public void set(String key,Object obj, Long time){
        redisCacheTemplate.opsForValue().set(key, (Serializable) obj, time, TimeUnit.SECONDS);
    }

    public Object get(String key){
        return redisCacheTemplate.opsForValue().get(key);
    }*/
}
