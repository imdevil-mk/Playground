package com.imdevil.kapt

import com.imdevil.kapt.annotation.OnClick
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

class OnClickProcessor : AbstractProcessor() {

    private lateinit var messager: Messager
    private lateinit var elementUtils: Elements
    private val infoMap: MutableMap<String, AnnotationInfo> = mutableMapOf()

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            OnClick::class.java.name
        )
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils
    }

    override fun process(
        annotaions: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        println("process $annotaions")
        if (annotaions.size == 0) {
            return false
        }
        roundEnv.getElementsAnnotatedWith(OnClick::class.java).forEach {
            if (it.kind != ElementKind.METHOD) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Only method can be annotated!")
                return true
            }
            val typeElement = it.enclosingElement as TypeElement
            val clzName = typeElement.qualifiedName.toString()
            val packageName = elementUtils.getPackageOf(typeElement).toString()
            val savedInfo = infoMap[clzName] ?: AnnotationInfo(
                packageName,
                typeElement
            ).also { annotationInfo ->
                infoMap[clzName] = annotationInfo
            }
            savedInfo.apply {
                onClickElements += it
            }
        }
        messager.printMessage(Diagnostic.Kind.WARNING, "annotated method size = ${infoMap.size}")
        if (infoMap.isEmpty()) {
            return false
        }
        infoMap.forEach { (_, annotationInfo) ->
            val fileName = "${annotationInfo.typeElement.simpleName}_ViewBinging"
            println(fileName)
            val fileBuilder = FileSpec.builder(annotationInfo.packageName, fileName)
            val classBuilder = TypeSpec.classBuilder(fileName)

            annotationInfo.onClickElements.forEach {
                val annotatedMethodName = (it as ExecutableElement).simpleName.toString()
                classBuilder.addFunction(
                    FunSpec.builder(annotatedMethodName)
                        .addStatement("println(%S)", annotatedMethodName)
                        .build()
                )
            }

            val file = fileBuilder.addType(classBuilder.build()).build()
            file.writeTo(processingEnv.filer)
        }

        return false
    }
}