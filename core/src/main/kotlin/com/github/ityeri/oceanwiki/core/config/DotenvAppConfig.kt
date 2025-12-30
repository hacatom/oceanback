package com.github.ityeri.oceanwiki.core.config

import io.github.cdimascio.dotenv.Dotenv
import java.nio.file.Path
import java.nio.file.Paths

class DotenvAppConfig : AppConfig {
    private val dotenv: Dotenv = Dotenv.load()

    override val rootStorePath: Path by lazy {
        Paths.get(dotenv["ROOT_STORE_PATH"] ?: throw IllegalStateException("ROOT_STORE_PATH not defined in .env"))
    }
    override val databaseHost: String by lazy {
        dotenv["DB_HOST"] ?: throw IllegalStateException("DB_HOST not defined in .env")
    }
    override val databaseUsername: String by lazy {
        dotenv["DB_USER"] ?: throw IllegalStateException("DB_USER not defined in .env")
    }
    override val databasePassword: String by lazy {
        dotenv["DB_PASSWORD"] ?: throw IllegalStateException("DB_PASSWORD not defined in .env")
    }
    override val databaseName: String by lazy {
        dotenv["DB_NAME"] ?: throw IllegalStateException("DB_NAME not defined in .env")
    }
}
