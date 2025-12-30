package com.github.ityeri.oceanwiki.core.config

import java.nio.file.Path
import java.nio.file.Paths

class SystemEnvAppConfig : AppConfig {
    override val rootStorePath: Path by lazy {
        Paths.get(System.getenv("ROOT_STORE_PATH") ?: throw IllegalStateException("ROOT_STORE_PATH not defined in environment variables"))
    }
    override val databaseHost: String by lazy {
        System.getenv("DB_HOST") ?: throw IllegalStateException("DB_HOST not defined in environment variables")
    }
    override val databaseUsername: String by lazy {
        System.getenv("DB_USER") ?: throw IllegalStateException("DB_USER not defined in environment variables")
    }
    override val databasePassword: String by lazy {
        System.getenv("DB_PASSWORD") ?: throw IllegalStateException("DB_PASSWORD not defined in environment variables")
    }
    override val databaseName: String by lazy {
        System.getenv("DB_NAME") ?: throw IllegalStateException("DB_NAME not defined in environment variables")
    }
}
