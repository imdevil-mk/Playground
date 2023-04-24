package com.imdevil.kapt.annotation

import kotlin.reflect.KClass


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OnClick(
    val viewId: Int,
    val activityClz: KClass<*>
)
