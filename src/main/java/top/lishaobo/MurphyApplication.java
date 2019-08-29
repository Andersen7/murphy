package top.lishaobo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("top.lishaobo.example.dao.mapper") //扫描的mapper
@SpringBootApplication
public class MurphyApplication {


	public static void main(String[] args) {
		SpringApplication.run(MurphyApplication.class, args);
	}
}
