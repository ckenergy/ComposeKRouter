import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.File

private const val TAG = "KRouterProcessor"

class KRouterProcessor(
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
        val routerName = "com.ckenergy.compose.plugin.core.KRouter"
        val symbols = resolver
            .getSymbolsWithAnnotation(routerName)
            .filterIsInstance<KSFunctionDeclaration>().toList()

        if (symbols.isEmpty()) {
            return emptyList()
        }

        val packageName = options.findPackageName(logger)

        val className = "NavGraph"
        checkFile(packageName, "$className.kt")
        try {
            val builder = StringBuilder()
            val classCreate = ClassCreatorProxy(builder)
            val fileSpec = classCreate.generateJavaCode(
                packageName,
                className, symbols
            )
            fileSpec.writeTo(codeGenerator, true, symbols.map { it.containingFile!! })
        } catch (e: Exception) {
            log(e.stackTraceToString())
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
                    "    arg(\"AROUTER_MODULE_NAME\", project.getName()) {\n" +
                    "}\n")
            throw RuntimeException("ARouter::Compiler >>> No module name, for more information, look at gradle log.")
        }
    }

}