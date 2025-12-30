package com.github.ityeri.oceanwiki.core.config

import java.nio.file.Path

interface AppConfig {
    val rootStorePath: Path

    val databaseHost: String
    val databaseUsername: String
    val databasePassword: String
    val databaseName: String
}
