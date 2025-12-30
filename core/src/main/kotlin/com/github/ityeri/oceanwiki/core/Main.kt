package com.github.ityeri.oceanwiki.core

import com.github.ityeri.oceanwiki.core.api.media.MediaService
import com.github.ityeri.oceanwiki.core.api.media.mediaController
import com.github.ityeri.oceanwiki.core.api.media.orm.MediaTable
import com.github.ityeri.oceanwiki.core.api.post.PostService
import com.github.ityeri.oceanwiki.core.api.post.postController
import com.github.ityeri.oceanwiki.core.api.user.UserService
import com.github.ityeri.oceanwiki.core.api.user.orm.UserTable
import com.github.ityeri.oceanwiki.core.api.user.userController
import com.github.ityeri.oceanwiki.core.config.AppConfig
import com.github.ityeri.oceanwiki.core.config.DotenvAppConfig
import com.github.ityeri.oceanwiki.core.config.SystemEnvAppConfig
import com.github.ityeri.oceanwiki.core.location.LocationResolver
import com.github.ityeri.oceanwiki.core.location.impl.LocationResolverImpl
import com.github.ityeri.oceanwiki.core.location.impl.orm.LocationTable
import com.github.ityeri.oceanwiki.core.post.PostTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    val appModule = module {
        single<AppConfig> {
            val appEnv = System.getenv("APP_ENV")
            when (appEnv) {
                "production" -> SystemEnvAppConfig()
                else -> DotenvAppConfig()
            }
        }

        single {
            val config: AppConfig = get()
            Database.connect(
                url = "jdbc:mysql://${config.databaseHost}/${config.databaseName}?useSSL=false&serverTimezone=UTC",
                driver = "com.mysql.cj.jdbc.Driver",
                user = config.databaseUsername,
                password = config.databasePassword
            )
        }

        single { MediaService(get()) }
        single { UserService() }
        single<LocationResolver> { LocationResolverImpl(get()) }
        single { PostService(get()) }

        single {
            val mediaService: MediaService = get()
            val userService: UserService = get()
            val postService: PostService = get()

            Server().addRoutes {
                mediaController(mediaService)
            }.addRoutes {
                userController(userService)
            }.addRoutes {
                postController(postService)
            }
        }
    }

    startKoin {
        printLogger()
        modules(appModule)
    }

    transaction(getKoin().get<Database>()) {
        SchemaUtils.create(UserTable)
        SchemaUtils.create(MediaTable)
        SchemaUtils.create(LocationTable) // New
        SchemaUtils.create(PostTable) // New
    }

    val server: Server = getKoin().get()
    server.run()
}