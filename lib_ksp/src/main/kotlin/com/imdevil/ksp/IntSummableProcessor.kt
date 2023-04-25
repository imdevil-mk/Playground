package com.imdevil.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.imdevil.ksp.annotation.IntSummable
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec

class IntSummableProcessor(
    private val options: Map<String, String>,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private lateinit var intType: KSType

    override fun process(resolver: Resolver): List<KSAnnotated> {
        println("process")
        intType = resolver.builtIns.intType
        val symbols = resolver.getSymbolsWithAnnotation(IntSummable::class.qualifiedName!!)
            .filter { it.validate() }

        symbols.filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(IntSummableVisitor(), Unit) }

        return symbols.filterNot { it.validate() }.toList()
    }

    inner class IntSummableVisitor : KSVisitorVoid() {

        private lateinit var className: String
        private lateinit var packageName: String
        private val summables: MutableList<String> = mutableListOf()

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val qualifiedName = classDeclaration.qualifiedName?.asString()

            //1. 合法性检查
            if (!classDeclaration.isDataClass()) {
                logger.error(
                    "@IntSummable cannot target non-data class $qualifiedName",
                    classDeclaration
                )
                return
            }

            if (qualifiedName == null) {
                logger.error(
                    "@IntSummable must target classes with qualified names",
                    classDeclaration
                )
                return
            }

            //2. 解析Class信息
            className = qualifiedName
            packageName = classDeclaration.packageName.asString()

            classDeclaration.getAllProperties()
                .forEach {
                    it.accept(this, Unit)
                }

            if (summables.isEmpty()) {
                return
            }

            //3. 代码生成
            val fileSpec = FileSpec.builder(
                packageName = packageName,
                fileName = "${classDeclaration.simpleName.asString()}_IntSummable"
            ).apply {
                addFunction(
                    FunSpec.builder("sumInts")
                        .receiver(ClassName.bestGuess(className))
                        .returns(Int::class)
                        .addStatement("val sum = ${summables.joinToString(" + ")}")
                        .addStatement("return sum")
                        .build()
                )
            }.build()

            codeGenerator.createNewFile(
                dependencies = Dependencies(aggregating = false),
                packageName = packageName,
                fileName = "${classDeclaration.simpleName.asString()}_IntSummable"
            ).use { outputStream ->
                outputStream.writer()
                    .use {
                        fileSpec.writeTo(it)
                    }
            }
        }

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            if (property.type.resolve().isAssignableFrom(intType)) {
                val name = property.simpleName.asString()
                summables.add(name)
            }
        }

        private fun KSClassDeclaration.isDataClass() = modifiers.contains(Modifier.DATA)
    }
}