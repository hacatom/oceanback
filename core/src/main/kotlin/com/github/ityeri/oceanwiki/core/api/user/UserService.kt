package com.github.ityeri.oceanwiki.core.api.user

import com.github.ityeri.oceanwiki.core.user.orm.UserRow
import org.jetbrains.exposed.sql.transactions.transaction

class UserService {
    fun findUserById(id: ULong): UserRow? {
        return transaction {
            UserRow.findById(id)
        }
    }
}
