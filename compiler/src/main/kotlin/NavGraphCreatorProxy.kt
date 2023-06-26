import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.*
import java.util.Locale

/**
 * ckenergy  2022-11-11
 */
class NavGraphCreatorProxy(private val logBuilder: StringBuilder) {

    private val moduleMemberName = MemberName("com.ckenergy.compose.plugin.core", "composeModules")
    private val moduleClassName = ClassName("com.ckenergy.compose.plugin.core", "ModuleBuilder")
    private val getArguments = MemberName("com.ckenergy.compose.plugin.core", "getArguments")
    private val parseArguments = MemberName("com.ckenergy.compose.plugin.core", "parseArguments")

    /**
     * 创建Java代码
     *
     * @return
     */
    fun generateJavaCode(
        packageName: String,
        name: String,
        routerList: HashMap<KSFunctionDeclaration, ClassName?>
    ): FileSpec {
        val codeBlockBuilder =
            CodeBlock.Builder()
                .beginControlFlow(
                    "%M",
                    moduleMemberName
                )//This will take care of the {} and indentations
                .addStatement("packageName = \"$packageName\"")
        routerList.forEach {
            val funDec = it.key
            val composePackageName = funDec.containingFile!!.packageName.asString()
            val funName = funDec.simpleName.asString()
            val routeName =
                funDec.annotations.firstOrNull { it1 ->
                    it1.annotationType.resolve().declaration.qualifiedName?.asString() == Constants.KEY_KROUTER_NAME
                }?.arguments?.find { it1 -> it1.name?.getShortName() == "routeName" }?.value.toString()

            val composeFunName = MemberName(composePackageName, funName)

            if (it.value == null) {
                codeBlockBuilder.addStatement(
                    "composable(\"$routeName\") {" +
                            "%M()" +
                            "}", composeFunName
                )
            } else {
                val className = it.value!!
                val beanValueName =
                    className.simpleName.replaceFirstChar { it1 -> it1.lowercase(Locale.getDefault()) }
                val parameterStr = funDec.parameters.joinToString(separator = ",") { it1 ->
                    val valueName = it1.name!!.asString()
                    if (it1.hasDefault) {
                        log("$composePackageName.$funName function parameter $valueName set default value may not work")
                    }
                    "$valueName = ${beanValueName}." + valueName
                }

                codeBlockBuilder.addStatement(
                    "composable(\"$routeName/{${Constants.KEY_ARG_NAME}}\", \n" +
                            "arguments = %M()\n" +
                            ") { \n", getArguments
                )
                codeBlockBuilder.addStatement(
                    "   val $beanValueName: %T? = it.%M() \n", className, parseArguments,
                )
                codeBlockBuilder.addStatement(
                    "   if($beanValueName == null) return@composable \n"
                )
                codeBlockBuilder.addStatement(
                    "   %M($parameterStr) \n" , composeFunName
                )
                codeBlockBuilder.addStatement("}")
            }


        }
        codeBlockBuilder.endControlFlow()

        val module = PropertySpec.builder(name, moduleClassName)
            .initializer(codeBlockBuilder.build())
            .build()

        return FileSpec.builder(packageName, name)
            .addProperty(module)
            .build()
    }

    private fun log(msg: String) {
        logBuilder.append(msg + "\n")
    }

}