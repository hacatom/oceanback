// 이거 안쓰임 그냥 location 패키지 전체가 안쓰임 ㅠㅠ
package com.github.ityeri.oceanwiki.core.location

interface LocationResolver {
    suspend fun resolveLocationFromCoords(coords: Point): Location?
}