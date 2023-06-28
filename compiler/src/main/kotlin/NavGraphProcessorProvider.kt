import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.File

class NavGraphProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return NavGraphProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}

private const val TAG = "KRouterProcessor"

class NavGraphProcessor(
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
        val funMap = hashMapOf<KSFunctionDeclaration, ClassName?>()

        symbols.forEach {
            if (it.parameters.isNotEmpty()) {
                val pair = it.getClassFullName()
                funMap[it] = ClassName.bestGuess("${pair.first}.${pair.second}")
            }else {
                funMap[it] = null
            }
        }

        createGraph(packageName, funMap)

        return emptyList()
    }


    private fun createGraph(packageName: String, symbols:HashMap<KSFunctionDeclaration, ClassName?>) {
        val className = Constants.KEY_CLASS_NAME
        checkFile(packageName, "$className.kt")
        try {
            val builder = StringBuilder()
            val classCreate = NavGraphCreatorProxy(builder)
            val typeSpec = classCreate.generateJavaCode(
                packageName,
                className, symbols
            )
            log(builder.toString())
            val fileSpec = FileSpec.get(packageName, typeSpec)
            fileSpec.writeTo(codeGenerator, true, symbols.keys.map { it.containingFile!! })
        } catch (e: Exception) {
            log(e.stackTraceToString())
        }
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
            throw RuntimeException("KRouter::Compiler >>> No module name, for more information, look at gradle log.")
        }
    }

}