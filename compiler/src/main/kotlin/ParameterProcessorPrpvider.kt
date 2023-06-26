import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.FileSpec
import java.io.File

private const val TAG = "ParameterProcessor"

class ParameterProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ParameterProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}

class ParameterProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    private var invoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            return emptyList()
        }
        invoked = true

        log("[$TAG]")
        val routerName = Constants.KEY_KROUTER_NAME
        val symbols = resolver
            .getSymbolsWithAnnotation(routerName)
            .filterIsInstance<KSFunctionDeclaration>().toList()

        if (symbols.isEmpty()) {
            return emptyList()
        }

        val packageName = options.findPackageName(logger)

        symbols.forEach {
            if (it.parameters.isNotEmpty()) {
                val pair = it.getClassFullName()
                val composePackageName = pair.first
                val className = pair.second

                checkFile(composePackageName, "$className.kt")
                val file = codeGenerator.createNewFile(
                    Dependencies(false, it.containingFile!!),
                    composePackageName,
                    className
                )

                file.bufferedWriter().use { writer ->
                    try {
                        val builder = StringBuilder()
                        val classCreate = ParameterCreatorProxy(builder)
                        val typeSpec = classCreate.generateJavaCode(
                            className, it.parameters
                        )
                        log(builder.toString())
                        val fileSpec = FileSpec.get(packageName, typeSpec)
                        fileSpec.writeTo(writer)
                    } catch (e: Exception) {
                        log(e.stackTraceToString())
                    }
                }
            }
        }

        return emptyList()
    }

    private fun checkFile(packageName: String, fileName: String) {
        val file = File(packageName.replace(".", "/"), fileName)
        if (file.exists()) {
            file.delete()
        }
    }

    private fun log(msg: String) {
        logger.warn("$TAG: $msg")
    }

    private fun Map<String, String>.findPackageName(logger: KSPLogger): String {
        val name = this[Constants.KEY_KROUTER_PACKAGE]
        return if (!name.isNullOrEmpty()) {
            name
        } else {
            logger.error("These no module name, at 'build.gradle', like :\n" +
                    "ksp {\n" +
                    "    arg(\"${Constants.KEY_KROUTER_NAME}\", project.getName()) {\n" +
                    "}\n")
            throw RuntimeException("ARouter::Compiler >>> No module name, for more information, look at gradle log.")
        }
    }

}