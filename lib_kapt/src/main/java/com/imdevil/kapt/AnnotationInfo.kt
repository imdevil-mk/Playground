package com.imdevil.kapt

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

class AnnotationInfo(
    val packageName: String,
    val typeElement: TypeElement
) {
    val onClickElements: MutableList<Element> = mutableListOf()
}