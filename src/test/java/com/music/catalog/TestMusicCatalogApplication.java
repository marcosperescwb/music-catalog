package com.music.catalog;

import org.springframework.boot.SpringApplication;

public class TestMusicCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.from(MusicCatalogApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
